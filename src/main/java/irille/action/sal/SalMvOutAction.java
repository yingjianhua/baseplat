package irille.action.sal;

import irille.action.ActionForm;
import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysShiping;
import irille.core.sys.Sys.OBillFlag;
import irille.core.sys.Sys.OShipingMode;
import irille.gl.gs.GsDemand;
import irille.pss.pur.PurRev;
import irille.pss.pur.PurRevDAO;
import irille.pss.sal.SalMvOut;
import irille.pss.sal.SalMvOutDAO;
import irille.pss.sal.SalMvOutLine;
import irille.pss.sal.SalPriceProtMvDAO;
import irille.pss.sal.SalMvOut.OFromType;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanMain;
import irille.pub.idu.Idu;
import irille.pub.inf.IExtName;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

public class SalMvOutAction extends ActionForm<SalMvOut, SalMvOutLine> {
	public static final Log LOG = new Log(SalMvOutAction.class);
	private Integer whsId;
	private String dlines;
	public SysShiping _ship;

	public String getDlines() {
		return dlines;
	}

	public void setDlines(String dlines) {
		this.dlines = dlines;
	}

	public Integer getWhsId() {
		return whsId;
	}

	public void setWhsId(Integer whsId) {
		this.whsId = whsId;
	}

	public SysShiping getShip() {
		return _ship;
	}

	public void setShip(SysShiping ship) {
		_ship = ship;
	}

	@Override
	public Class beanClazz() {
		return SalMvOut.class;
	}

	public SalMvOut getBean() {
		return _bean;
	}

	public void setBean(SalMvOut bean) {
		this._bean = bean;
	}

	public List<SalMvOutLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<SalMvOutLine> listLine) {
		_listLine = listLine;
	}

	public void init() throws Exception {
		JSONObject json = new JSONObject();
		SalMvOut mv = new SalMvOut();
		mv.setWarehouseOther(getWhsId());
		mv.setOrgOther(mv.gtWarehouseOther().getOrg());
		SysCell cell = SysCellDAO.getCellByDept(getWhsId());
		if (cell == null)
			throw LOG.err("err", "部门[{0}]的核算单元未定义", mv.gtWarehouseOther().getExtName());
		mv.setCellOther(cell.getPkey());
		mv.setBillFlag(OBillFlag.DEFAULT.getLine().getKey());
		mv.stFromType(OFromType.DEMAND);
		mv.stShipingMode(OShipingMode.DEFAULT);

		JSONArray jlines = new JSONArray();
		BigDecimal amt = BigDecimal.ZERO;
		for (String dl : getDlines().split(",")) {
			GsDemand dm = BeanBase.load(GsDemand.class, dl);
			SalMvOutLine line = new SalMvOutLine();
			line.setPkey(dm.getPkey());
			line.setGoods(dm.getGoods());
			line.setUom(dm.getUom());
			line.setQty(dm.getQty());
			line.setPrice(SalPriceProtMvDAO.getPrice(Idu.getCell(), line.gtGoods()));
			line.setAmt(line.getQty().multiply(line.getPrice()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
			line.setExpensesSale(BigDecimal.ZERO);
			amt = amt.add(line.getAmt());
			JSONObject lineJson = crtJsonByBean(line, "bean.");
			lineJson.put("link.goodsName", line.gtGoods().getName());
			lineJson.put("link.goodsSpec", line.gtGoods().getSpec());
			jlines.put(lineJson);
		}
		mv.setAmt(amt);
		JSONObject outJson = crtJsonByBean(mv, "bean.");
		List<SysPersonLink> warelinks = Idu.getLines(SysPersonLink.T.TB_OBJ_LONG, mv.gtWarehouseOther()
		    .gtDept().gtLongPkey());
		if (warelinks.size() > 0) {
			SysPersonLink link = warelinks.get(0);
			outJson.put("ship.name", link.getName());
			outJson.put("ship.addr", link.getOfAddr());
			outJson.put("ship.mobile", link.getPeMobile());
			outJson.put("ship.tel", link.getOfTel());
		}
		outJson.put("bean.rowVersion", 0);
		json.put("mvOut", outJson);
		json.put("lines", jlines);
		writerOrExport(json);
	}

	public void cOut() throws Exception {
		SalMvOutDAO.COut act = new SalMvOutDAO.COut();
		act.setBKey(getPkey());
		act.commit();
		writeSuccess(act.getB());
	}

	public void chk() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		SalMvOutDAO.Chk upd = new SalMvOutDAO.Chk();
		upd._ship = getShip();
		upd.setB(getBean());
		upd.setLines(getListLine());
		upd.commit();
		writeSuccess(upd.getB());
//		JSONObject json = crtJsonByBean(upd.getB(), "bean.");
//		json.put("success", true);
//		response.getWriter().print(json.toString());
	}

	@Override
	public SalMvOut insRun() throws Exception {
		SalMvOutDAO.Ins ins = new SalMvOutDAO.Ins();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	}

	public SalMvOut updRun() throws Exception {
		SalMvOutDAO.Upd ins = new SalMvOutDAO.Upd();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	};

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		if (!(bean instanceof SalMvOut))
			return json;
		SysShiping smode = ((SalMvOut) bean).gtShiping();
		if (smode == null)
			return json;
		String ship = "";
		if (Str.isEmpty(pref) == false)
			ship = "ship.";
		json.put(ship + SysShiping.T.TIME_ARR_PLAN.getFld().getCode(), nv(smode.getTimeArrPlan()));
		json.put(ship + SysShiping.T.TIME_SHIP_PLAN.getFld().getCode(), nv(smode.getTimeShipPlan()));
		json.put(ship + SysShiping.T.NAME.getFld().getCode(), nv(smode.getName()));
		json.put(ship + SysShiping.T.ADDR.getFld().getCode(), nv(smode.getAddr()));
		json.put(ship + SysShiping.T.MOBILE.getFld().getCode(), nv(smode.getMobile()));
		json.put(ship + SysShiping.T.TEL.getFld().getCode(), nv(smode.getTel()));
		return json;
	}

	private Object nv(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Date)
			return toTimeJson((Date) obj); // 注意类型
		if (obj instanceof BeanMain) {
			BeanMain b = (BeanMain) obj;
			return b.getPkey() + BEAN_SPLIT + ((IExtName) b).getExtName();
		}
		return obj;
	}
}
