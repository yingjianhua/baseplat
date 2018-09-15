package irille.action.pur;

import irille.action.ActionForm;
import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysShiping;
import irille.core.sys.Sys.OBillFlag;
import irille.core.sys.Sys.OShipingMode;
import irille.gl.gs.GsDemand;
import irille.gl.gs.GsDemandDirect;
import irille.pss.pur.PurMvIn;
import irille.pss.pur.PurMvInDAO;
import irille.pss.pur.PurMvInLine;
import irille.pss.pur.PurMvIn.OFromType;
import irille.pss.pur.PurMvIn.OGoodsTo;
import irille.pss.sal.SalMvOut;
import irille.pss.sal.SalMvOutLine;
import irille.pss.sal.SalPriceProtMvDAO;
import irille.pss.sal.SalSaleDirect;
import irille.pss.sal.SalSaleDirectLine;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanGoodsPrice;
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

public class PurMvInAction extends ActionForm<PurMvIn, PurMvInLine> {

	private Integer whsId;
	private String dlines;
	private Integer dirId;
	private SysShiping _ship;

	public Integer getDirId() {
		return dirId;
	}

	public void setDirId(Integer dirId) {
		this.dirId = dirId;
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

	public String getDlines() {
		return dlines;
	}

	public void setDlines(String dlines) {
		this.dlines = dlines;
	}

	@Override
	public Class beanClazz() {
		return PurMvIn.class;
	}

	public PurMvIn getBean() {
		return _bean;
	}

	public void setBean(PurMvIn bean) {
		this._bean = bean;
	}

	public List<PurMvInLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<PurMvInLine> listLine) {
		_listLine = listLine;
	}

	// 需求新增时初始化
	public void init() throws Exception {
		JSONObject json = new JSONObject();
		PurMvIn mv = new PurMvIn();
		mv.setWarehouse(getWhsId());
		mv.setOrg(mv.gtWarehouse().getOrg());
		SysCell cell = SysCellDAO.getCellByDept(getWhsId());
		if (cell == null)
			throw LOG.err("err", "找不到部门[{0}]的核算单元", getWhsId());
		mv.setCell(cell.getPkey());
		mv.setBillFlag(OBillFlag.DEFAULT.getLine().getKey());
		mv.stFromType(OFromType.DEMAND);
		mv.stShipingMode(OShipingMode.DEFAULT);
		JSONArray jlines = new JSONArray();
		BigDecimal amt = BigDecimal.ZERO;
		for (String dl : getDlines().split(",")) {
			GsDemand dm = BeanBase.load(GsDemand.class, dl);
			PurMvInLine line = new PurMvInLine();
			line.setPkey(dm.getPkey());
			line.setGoods(dm.getGoods());
			line.setUom(dm.getUom());
			line.setQty(dm.getQty());
			line.setPrice(SalPriceProtMvDAO.getPrice(Idu.getCell(), line.gtGoods()));
			line.setAmt(line.getQty().multiply(line.getPrice()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
			line.setCostPur(BigDecimal.ZERO);
			amt = amt.add(line.getAmt());
			JSONObject lineJson = crtJsonByBean(line, "bean.");
			lineJson.put("link.goodsName", line.gtGoods().getName());
			lineJson.put("link.goodsSpec", line.gtGoods().getSpec());
			jlines.put(lineJson);
		}
		mv.setAmt(amt);
		JSONObject inJson = crtJsonByBean(mv, "bean.");
		List<SysPersonLink> warelinks = Idu.getLines(SysPersonLink.T.TB_OBJ_LONG, mv.gtWarehouse()
		    .gtDept().gtLongPkey());
		if (warelinks.size() > 0) {
			SysPersonLink link = warelinks.get(0);
			inJson.put("ship.name", link.getName());
			inJson.put("ship.addr", link.getOfAddr());
			inJson.put("ship.mobile", link.getPeMobile());
			inJson.put("ship.tel", link.getOfTel());
		}
		inJson.put("bean.rowVersion", 0);
		json.put("mvIn", inJson);
		json.put("lines", jlines);
		writerOrExport(json);
	}

	// 调入直销时初始化
	public void initDr() throws Exception {

		GsDemandDirect dr = BeanBase.load(GsDemandDirect.class, getDirId());
		JSONObject json = new JSONObject();
		PurMvIn mv = new PurMvIn();
		List<BeanGoodsPrice> lines = null;
		String shipName = "";
		String shipAddr = "";
		String shipMobile = "";
		String shipTel = "";
		if (dr.gtOrigForm() instanceof SalMvOut) {
			SalMvOut mvOut = (SalMvOut) dr.gtOrigForm();
			if (mvOut.getWarehouseOther() == null) {
				// throw LOG.err("initDr","调入仓库未确定！");
				json.put("message", "调入仓库未确定！");
				writerOrExport(json);
				return;
			}
			mv.setOrg(dr.getOrg());
			mv.setCell(dr.getCell());
			mv.setBillFlag(OBillFlag.DEFAULT.getLine().getKey());
			mv.stFromType(OFromType.DIRECT);
			mv.stGoodsTo(OGoodsTo.DIRECT_DEMAND);
			if (mvOut.getShiping() != null) {
				shipName = mvOut.gtShiping().getName();
				shipAddr = mvOut.gtShiping().getAddr();
				shipMobile = mvOut.gtShiping().getMobile();
				shipTel = mvOut.gtShiping().getTel();
			}
			mv.setOrigForm(dr.getOrigForm());
			mv.setOrigFormNum(dr.getOrigFormNum());
			lines = Idu.getLinesTid(mvOut, SalMvOutLine.class);
		} else if (dr.gtOrigForm() instanceof SalSaleDirect) {
			SalSaleDirect mvOut = (SalSaleDirect) dr.gtOrigForm();
			mv.setOrg(dr.getOrg());
			mv.setCell(dr.getCell());
			mv.setBillFlag(OBillFlag.DEFAULT.getLine().getKey());
			mv.stFromType(OFromType.DIRECT);
			mv.stGoodsTo(OGoodsTo.DIRECT_DEMAND);
			if (mvOut.getShiping() != null) {
				shipName = mvOut.gtShiping().getName();
				shipAddr = mvOut.gtShiping().getAddr();
				shipMobile = mvOut.gtShiping().getMobile();
				shipTel = mvOut.gtShiping().getTel();
			}
			mv.setOrigForm(dr.getOrigForm());
			mv.setOrigFormNum(dr.getOrigFormNum());
			lines = Idu.getLinesTid(mvOut, SalSaleDirectLine.class);
		}
		JSONArray jlines = new JSONArray();
		BigDecimal amt = BigDecimal.ZERO;
		for (BeanGoodsPrice dm : lines) {
			PurMvInLine line = new PurMvInLine();
			line.setGoods(dm.getGoods());
			line.setUom(dm.getUom());
			line.setQty(dm.getQty());
			line.setPrice(SalPriceProtMvDAO.getPrice(Idu.getCell(), line.gtGoods()));
			line.setAmt(line.getQty().multiply(line.getPrice()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
			line.setCostPur(BigDecimal.ZERO);
			amt = amt.add(line.getAmt());
			JSONObject lineJson = crtJsonByBean(line, "bean.");
			lineJson.put("link.goodsName", line.gtGoods().getName());
			lineJson.put("link.goodsSpec", line.gtGoods().getSpec());
			jlines.put(lineJson);
		}
		mv.stShipingMode(OShipingMode.DEFAULT);
		mv.setAmt(amt);
		JSONObject inJson = crtJsonByBean(mv, "bean.");
		inJson.put("ship.name", shipName);
		inJson.put("ship.addr", shipAddr);
		inJson.put("ship.mobile", shipMobile);
		inJson.put("ship.tel", shipTel);
		inJson.put("bean.rowVersion", 0);
		json.put("mvIn", inJson);
		json.put("lines", jlines);
		writerOrExport(json);
	}

	public void chk() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		PurMvInDAO.Chk upd = new PurMvInDAO.Chk();
		upd._ship = getShip();
		upd.setB(getBean());
		upd.setLines(getListLine());
		upd.commit();
//		JSONObject json = crtJsonByBean(upd.getB(), "bean.");
//		json.put("success", true);
//		response.getWriter().print(json.toString());
		writeSuccess(upd.getB());
	}

	public void cIn() throws Exception {
		PurMvInDAO.CIn act = new PurMvInDAO.CIn();
		act.setBKey(getPkey());
		act.commit();
		writeSuccess(act.getB());
	}

	@Override
	public PurMvIn insRun() throws Exception {
		PurMvInDAO.Ins ins = new PurMvInDAO.Ins();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	}

	@Override
	public PurMvIn updRun() throws Exception {
		PurMvInDAO.Upd ins = new PurMvInDAO.Upd();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		if (!(bean instanceof PurMvIn))
			return json;
		SysShiping smode = ((PurMvIn) bean).gtShiping();
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
