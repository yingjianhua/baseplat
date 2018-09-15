package irille.pub.idu;

import irille.pub.PropertyUtils;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBill;
import irille.pub.bean.BeanMain;
import irille.pub.gl.Tally;
import irille.pub.svr.Env;

public class IduBase {

	public static class Ins extends IduIns<Ins, BeanMain> {
	}

	public static class Upd extends IduUpd<Upd, BeanMain> {

		public void before() {
			super.before();
			BeanMain dbBean = loadThisBeanAndLock();
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), Bean.tb(clazz()).get("pkey"));
			setB(dbBean);
		}

	}

	public static class Del extends IduDel<Del, BeanMain> {}

	public static class Page extends IduPage<Page, BeanMain> {}
	
	public static class Enabled extends IduEnabled<Enabled, BeanMain> {}
	
	public static class UnEnabled extends IduUnEnabled<UnEnabled, BeanMain> {}
	
	public static class DoTally extends IduTally<DoTally, BeanBill> {

		@Override
		public void run() {
			super.run();
			BeanBill model = loadThisBeanAndLock();
			Tally.checkOrgStatus(model.gtOrg()); //内部的记账程序不检查机构状态，日终过程中会自动产生内转单并记账
			Tally.runTally(model); 
			setB(model);
		}
	}

	public static class UnTally extends IduUnTally<UnTally, BeanBill> {

		public void run() {
			super.run();
			BeanBill model = loadThisBeanAndLock();
			Tally.checkOrgStatus(model.gtOrg());
			Tally.runTallyCancel(model);
			setB(model);
		}
	}
}
