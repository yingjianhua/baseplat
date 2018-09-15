package irille.pss.pur;

import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;
import irille.pub.svr.Env;

public class PurProtDAO {
	public static final Log LOG = new Log(PurProtDAO.class);

	public static class Ins extends IduIns<Ins, PurProt> {
		@Override
		public void before() {
			super.before();
			getB().setUpdatedTime(Env.getTranBeginTime());
			getB().setUpdatedBy(getUser().getPkey());
		}
	}

	public static class Upd extends IduUpd<Upd, PurProt> {

		public void before() {
			super.before();
			PurProt dbBean = loadThisBeanAndLock();
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), PurProt.T.PKEY,
					PurProt.T.UPDATED_BY, PurProt.T.UPDATED_TIME);
			setB(dbBean);
		}
	}

	public static void actionByApply(PurProtApply pap) {
		PurProt prot = PurProt.chkUniqueTempCust(true, pap.getTemplat(),
				pap.getSupplier());
		if (prot == null) {
			Ins insDao = new Ins();
			prot = new PurProt();
			prot.setTemplat(pap.getTemplat());
			prot.setSupplier(pap.getSupplier());
			prot.setName(pap.getName());
			prot.setSettle(pap.getAftSettle());
			prot.setSettleMonth(pap.getAftSettleMonth());
			prot.setSettleNextDay(pap.getAftSettleNextDay());
			prot.setCreditLevel(pap.getAftCreditLevel());
			prot.setCreditLimit(pap.getAftCreditLimit());
			prot.setCreditOther(pap.getAftCreditOther());
			prot.setTaxRate(pap.getAftTaxRate());
			prot.setDescKind(pap.getAftDescKind());
			prot.setDescSal(pap.getAftDescSal());
			prot.setPackDemand(pap.getAftPackDemand());
			prot.setShipMode(pap.getAftShipMode());
			prot.setShipType(pap.getAftShipType());
			prot.setDateProt(pap.getAftDateProt());
			prot.setDateStart(pap.getAftDateStart());
			prot.setDateEnd(pap.getAftDateEnd());
			insDao.setB(prot);
			insDao.commit();
		} else {
			Upd updDao = new Upd();
			prot.setSettle(pap.getAftSettle());
			prot.setSettleMonth(pap.getAftSettleMonth());
			prot.setSettleNextDay(pap.getAftSettleNextDay());
			prot.setCreditLevel(pap.getAftCreditLevel());
			prot.setCreditLimit(pap.getAftCreditLimit());
			prot.setCreditOther(pap.getAftCreditOther());
			prot.setTaxRate(pap.getAftTaxRate());
			prot.setDescKind(pap.getAftDescKind());
			prot.setDescSal(pap.getAftDescSal());
			prot.setPackDemand(pap.getAftPackDemand());
			prot.setShipMode(pap.getAftShipMode());
			prot.setShipType(pap.getAftShipType());
			prot.setDateProt(pap.getAftDateProt());
			prot.setDateStart(pap.getAftDateStart());
			prot.setDateEnd(pap.getAftDateEnd());
			updDao.setB(prot);
			updDao.commit();
		}
	}

	public static void checkSupplier(int templatId, int supplierId) {
		PurProt prot = PurProt.chkUniqueTempCust(false, templatId, supplierId);
		if (prot == null)
			throw LOG.err("ins", "供应商未签协议！");
		if (prot.getDateStart() != null
				&& prot.getDateStart().getTime() > Env.getSystemTime()
						.getTime())
			throw LOG.err("ins", "供应商[{0}]协议未启用！", prot.getName());
		if (prot.getDateEnd() != null
				&& prot.getDateEnd().getTime() < Env.getSystemTime().getTime())
			throw LOG.err("ins", "供应商[{0}]协议已到期！", prot.getName());
	}
}
