package irille.pss.sal;

import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;
import irille.pub.svr.Env;

public class SalDiscountPrivDAO {
	public static final Log LOG = new Log(SalDiscountPrivDAO.class);

	public static class Ins extends IduIns<Ins, SalDiscountPriv> {
		@Override
		public void before() {
			super.before();
			if(BeanBase.chk(SalDiscountPriv.class, getB().getPkey())!=null)
				throw LOG.err("err","该用户已经设置了折扣权限!");
			getB().stUpdatedBy(getUser());
			getB().setUpdatedTime(Env.INST.getWorkDate());
		}

	}

	public static class Upd extends IduUpd<Upd, SalDiscountPriv> {

		public void before() {
			super.before();
			getB().stUpdatedBy(getUser());
			getB().setUpdatedTime(Env.INST.getWorkDate());
		}

	}

}
