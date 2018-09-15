package irille.action.pur;

import irille.action.ActionForm;
import irille.core.sys.Sys;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysPersonLinkDAO;
import irille.core.sys.SysShiping;
import irille.core.sys.SysSupplier;
import irille.core.sys.SysTemplatCellDAO;
import irille.core.sys.Sys.OBillFlag;
import irille.core.sys.Sys.OShipingMode;
import irille.gl.gs.GsDemandDirect;
import irille.pss.pur.PurOrderDirect;
import irille.pss.pur.PurOrderDirectDAO;
import irille.pss.pur.PurOrderDirectLine;
import irille.pss.pur.PurProtGoods;
import irille.pss.sal.SalMvOut;
import irille.pss.sal.SalMvOutLine;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

public class PurOrderDirectAction extends ActionForm<PurOrderDirect, PurOrderDirectLine> {

	private Integer dirId;
	private Integer superId;
	public SysShiping _ship;

	public Integer getDirId() {
		return dirId;
	}

	public void setDirId(Integer dirId) {
		this.dirId = dirId;
	}

	public Integer getSuperId() {
		return superId;
	}

	public void setSuperId(Integer superId) {
		this.superId = superId;
	}

	public SysShiping getShip() {
		return _ship;
	}

	public void setShip(SysShiping ship) {
		_ship = ship;
	}

	@Override
	public Class beanClazz() {
		return PurOrderDirect.class;
	}

	public PurOrderDirect getBean() {
		return _bean;
	}

	public void setBean(PurOrderDirect bean) {
		this._bean = bean;
	}

	public List<PurOrderDirectLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<PurOrderDirectLine> listLine) {
		_listLine = listLine;
	}

	// 调入直销时初始化
	public void initDr() throws Exception {

		GsDemandDirect dr = BeanBase.load(GsDemandDirect.class, getDirId());
		JSONObject json = new JSONObject();
		PurOrderDirect order = new PurOrderDirect();
		List<BeanGoodsPrice> lines = null;
//		String shipName = "";
//		String shipAddr = "";
//		String shipMobile = "";
//		String shipTel = "";

		if (dr.gtOrigForm() instanceof SalMvOut) {
			SalMvOut mvOut = (SalMvOut) dr.gtOrigForm();
			order.stCell(Idu.getCell());
			order.setOrigForm(dr.getOrigForm());
			order.setOrigFormNum(dr.getOrigFormNum());
			order.setOrg(dr.getOrg());
			order.setBillFlag(OBillFlag.DEFAULT.getLine().getKey());
			order.setBuyer(getLoginSys().getPkey());
			order.setAmtXf(BigDecimal.ZERO);
			order.setAmtGz(BigDecimal.ZERO);
			SysPersonLink link = SysPersonLinkDAO.getDefault(mvOut.gtDept(), Sys.OLinkType.SAL);
			order.setRevAddr(link == null ? "" : link.getOfAddr());
//			if (mvOut.getShiping() != null) {
//				shipName = mvOut.gtShiping().getName();
//				shipAddr = mvOut.gtShiping().getAddr();
//				shipMobile = mvOut.gtShiping().getMobile();
//				shipTel = mvOut.gtShiping().getTel();
//			}
			lines = Idu.getLinesTid(mvOut, SalMvOutLine.class);
		} else if (dr.gtOrigForm() instanceof SalSaleDirect) {
			SalSaleDirect direct = (SalSaleDirect) dr.gtOrigForm();
			order.stCell(Idu.getCell());
			order.setOrigForm(dr.getOrigForm());
			order.setOrigFormNum(dr.getOrigFormNum());
			order.setOrg(dr.getOrg());
			order.setBillFlag(OBillFlag.DEFAULT.getLine().getKey());
			order.setBuyer(getLoginSys().getPkey());
			order.setAmtXf(BigDecimal.ZERO);
			order.setAmtGz(BigDecimal.ZERO);
			order.setRevAddr(direct.gtShiping() == null ? "" : direct.gtShiping().getAddr());
//			if (direct.getShiping() != null) {
//				shipName = direct.gtShiping().getName();
//				shipAddr = direct.gtShiping().getAddr();
//				shipMobile = direct.gtShiping().getMobile();
//				shipTel = direct.gtShiping().getTel();
//			}
			lines = Idu.getLinesTid(direct, SalSaleDirectLine.class);
		}
		removeKindOfWork(lines);//去除劳务类型的货物
		JSONArray jlines = new JSONArray();
		for (BeanGoodsPrice dm : lines) {
			PurOrderDirectLine line = new PurOrderDirectLine();
			line.setGoods(dm.getGoods());
			line.setUom(dm.getUom());
			line.setQty(dm.getQty());
			line.setPrice(BigDecimal.ZERO);
			line.setAmt(line.getQty().multiply(line.getPrice()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
			line.setCostPur(BigDecimal.ZERO);
			JSONObject lineJson = crtJsonByBean(line, "bean.");
			lineJson.put("link.goodsName", line.gtGoods().getName());
			lineJson.put("link.goodsSpec", line.gtGoods().getSpec());
			jlines.put(lineJson);
		}
		order.setAmt(BigDecimal.ZERO);
		order.stShipingMode(OShipingMode.DEFAULT);
		order.setRowVersion((short)0);
		JSONObject inJson = crtJsonByBean(order, "bean.");
//		inJson.put("ship.name", shipName);
//		inJson.put("ship.addr", shipAddr);
//		inJson.put("ship.mobile", shipMobile);
//		inJson.put("ship.tel", shipTel);
		json.put("order", inJson);
		json.put("lines", jlines);
		writerOrExport(json);
	}
	
	private void removeKindOfWork(List<BeanGoodsPrice> list) {
		List<BeanGoodsPrice> tempList = new ArrayList<BeanGoodsPrice>();
		for (BeanGoodsPrice line : list)
			if (line.gtGoods().isWork())
				tempList.add(line);
		for (BeanGoodsPrice tempLine : tempList)
			list.remove(tempLine);
	}

	public void loadPrice() throws Exception {
		JSONObject json = new JSONObject();
		Integer pk = getDirId();
		long key1 = pk / 100000 * 100000;
		long key2 = key1 + 100000;
		String where = " pkey > " + key1 + " AND pkey < " + key2;
		List list = null;
		if (Bean.gtLongClass(pk.longValue()).equals(SalMvOut.class)) {
			list = BeanBase.list(SalMvOutLine.class, where, false);
		} else if (Bean.gtLongClass(pk.longValue()).equals(SalSaleDirect.class)) {
			list = BeanBase.list(SalSaleDirectLine.class, where, false);
		}
		removeKindOfWork(list);//去除劳务类型的货物
		JSONArray jlines = new JSONArray();
		BigDecimal amt = BigDecimal.ZERO;
		for (Object dm1 : list) {
			BeanGoodsPrice dm = (BeanGoodsPrice) dm1;
			PurProtGoods pg = PurProtGoods.chkUniqueTempCustObj(false,
			    SysTemplatCellDAO.getPurTmpl().getPkey(), getSuperId(), dm.getGoods());
			PurOrderDirectLine line = new PurOrderDirectLine();
			line.setGoods(dm.getGoods());
			line.setUom(dm.getUom());
			line.setQty(dm.getQty());
			if (pg == null)
				line.setPrice(BigDecimal.ZERO);
			else
				line.setPrice(pg.getPriceLast() == null ? pg.getPrice() : pg.getPriceLast());
			line.setAmt(line.getPrice().multiply(line.getQty()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
			amt = amt.add(line.getAmt());
			line.setCostPur(BigDecimal.ZERO);
			JSONObject lineJson = crtJsonByBean(line, "bean.");
			lineJson.put("link.goodsName", line.gtGoods().getName());
			lineJson.put("link.goodsSpec", line.gtGoods().getSpec());
			jlines.put(lineJson);
		}
		json.put("name", BeanBase.load(SysSupplier.class, getSuperId()).getName());
		json.put("lines", jlines);
		json.put("amt", amt);
		writerOrExport(json);
	}

	@Override
	public PurOrderDirect insRun() throws Exception {
		PurOrderDirectDAO.Ins ins = new PurOrderDirectDAO.Ins();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	}

	@Override
	public PurOrderDirect updRun() throws Exception {
		PurOrderDirectDAO.Upd ins = new PurOrderDirectDAO.Upd();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	}
	
	public void checkPrice() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		PurOrderDirectDAO.CheckPrice chk = new PurOrderDirectDAO.CheckPrice();
		chk._ship = getShip();
		chk.setB(getBean());
		chk.setLines(getListLine());
		chk.commit();
//		JSONObject json = crtJsonByBean(upd.getB(), "bean.");
//		json.put("success", true);
//		response.getWriter().print(json.toString());
		writeSuccess(chk.getB());
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		if (!(bean instanceof PurOrderDirect))
			return json;
		SysShiping smode = ((PurOrderDirect) bean).gtShiping();
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
