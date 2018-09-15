package irille.pub.idu;

import irille.core.sys.SysSeqDAO;
import irille.pub.Cn;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.IBill;
import irille.pub.bean.IForm;
import irille.pub.svr.Env;
import irille.pub.tb.Tb;

/**
 * 数据库功能操基类
 * 在子类中必须声明常量CN，用类似的语句：public static Cn CN = new Cn("功能 代码", "功能名称");
 * @author whx
 * @param <BEAN>
 */
public class IduIns<T extends IduIns, BEAN extends Bean> extends Idu<T, BEAN> {
	private static final Log LOG = new Log(IduIns.class);
	public static Cn CN = new Cn("xz", "新增");
	
	@Override
	public final void commit() {
	 before();
	 valid();
	 run();
	 after();
	 log();
	}
	
	@Override
	public void run() {
		super.run();
		b.ins();
	}
	
	public void valid() {
		getB().setRowVersion((short)0);
		super.valid();
		super.validIu();
	}
	
	public void initForm(IForm form) {
		form.setCode(SysSeqDAO.getSeqnumForm(Tb.getTBByBean(form.getClass())));
		form.stStatus(STATUS_INIT);
		form.setOrg(getUser().getOrg());
		form.setDept(getUser().getDept());
		form.setCreatedBy(getUser().getPkey());
		form.setCreatedTime(Env.INST.getSystemTime());
	}
	
	public void initBill(IBill bill) {
		initForm(bill);
		bill.stCell(Idu.getCell());
	}

}
