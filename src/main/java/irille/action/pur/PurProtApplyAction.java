package irille.action.pur;

import irille.action.ActionBase;
import irille.core.sys.SysSupplier;
import irille.pss.pur.PurProt;
import irille.pss.pur.PurProtApply;
import irille.pub.bean.BeanBase;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduUnapprove;

import org.json.JSONObject;

public class PurProtApplyAction extends ActionBase<PurProtApply> {

	private Integer temId;
	private Integer supId;

	public Integer getTemId() {
		return temId;
	}

	public void setTemId(Integer temId) {
		this.temId = temId;
	}

	public Integer getSupId() {
		return supId;
	}

	public void setSupId(Integer supId) {
		this.supId = supId;
	}

	@Override
	public Class beanClazz() {
		return PurProtApply.class;
	}

	public PurProtApply getBean() {
		return _bean;
	}

	public void setBean(PurProtApply bean) {
		this._bean = bean;
	}

	public void approve() throws Exception {
		IduApprove act = newApprove();
		act.setBKey(getPkey());
		act.commit();
		writeSuccess(act.getB());
	}

	public void init() throws Exception {
		JSONObject json = new JSONObject();
		PurProt bean = PurProt.chkUniqueTempCust(false, getTemId(), getSupId());
		SysSupplier sup = BeanBase.load(SysSupplier.class, getSupId());
		PurProtApply pap = new PurProtApply();
		if (bean != null) {
			pap.setSupplier(bean.getSupplier());
			pap.setTemplat(bean.getTemplat());
			pap.setName(bean.getName());
			pap.setSettle(bean.getSettle());
			pap.setSettleMonth(bean.getSettleMonth());
			pap.setSettleNextDay(bean.getSettleNextDay());
			pap.setCreditLevel(bean.getCreditLevel());
			pap.setCreditLimit(bean.getCreditLimit());
			pap.setCreditOther(bean.getCreditOther());
			pap.setTaxRate(bean.getTaxRate());
			pap.setDescKind(bean.getDescKind());
			pap.setDescSal(bean.getDescSal());
			pap.setPackDemand(bean.getPackDemand());
			pap.setShipMode(bean.getShipMode());
			pap.setShipType(bean.getShipType());
			pap.setDateProt(bean.getDateProt());
			pap.setDateStart(bean.getDateStart());
			pap.setDateEnd(bean.getDateEnd());

			pap.setAftSettle(bean.getSettle());
			pap.setAftSettleMonth(bean.getSettleMonth());
			pap.setAftSettleNextDay(bean.getSettleNextDay());
			pap.setAftCreditLevel(bean.getCreditLevel());
			pap.setAftCreditLimit(bean.getCreditLimit());
			pap.setAftCreditOther(bean.getCreditOther());
			pap.setAftTaxRate(bean.getTaxRate());
			pap.setAftDescKind(bean.getDescKind());
			pap.setAftDescSal(bean.getDescSal());
			pap.setAftPackDemand(bean.getPackDemand());
			pap.setAftShipMode(bean.getShipMode());
			pap.setAftShipType(bean.getShipType());
			pap.setAftDateProt(bean.getDateProt());
			pap.setAftDateStart(bean.getDateStart());
			pap.setAftDateEnd(bean.getDateEnd());

			json = crtJsonByBean(pap, "bean.");
			json.put("success", "1");
		} else {
			pap.setSupplier(sup.getPkey());
			pap.setTemplat(getTemId());
			pap.setName(sup.getName());
			json = crtJsonByBean(pap, "bean.");
			json.put("success", "0");
		}
		json.put("name", sup.getName());
		writerOrExport(json);
	}
}
