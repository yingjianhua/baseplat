package irille.pub.idu;

import irille.pub.Cn;
import irille.pub.bean.Bean;

/**
 * 数据库功能操作：其他操作
 * 在子类中必须声明常量CN，用类似的语句：public static Cn CN = new Cn("功能 代码", "功能名称");
 * @param <BEAN>
 */
public class IduOther<T extends IduOther, BEAN extends Bean> extends Idu<T, BEAN> {
	public static Cn CN = new Cn("cz", "操作");

	/**
	 * 操作库的操作直接在各重构的方法中执行
	 */
	@Override
	public final void commit() {
		before();
		valid();
		run();
		after();
		log();
	}
}
