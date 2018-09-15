package irille.pub.idu;

import irille.pub.Cn;
import irille.pub.bean.Bean;

/**
 * 数据库功能操基类
 * 在子类中必须声明常量CN，用类似的语句：public static Cn CN = new Cn("功能 代码", "功能名称");
 * @author whx
 * 
 * @param <BEAN>
 */
public class IduUpd<T extends IduUpd, BEAN extends Bean> extends Idu<T, BEAN> {
	public static Cn CN = new Cn("xg", "修改");
	
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
		b.upd();
	}
	
	public void valid() {
		super.valid();
		super.validIu();
	}
}
