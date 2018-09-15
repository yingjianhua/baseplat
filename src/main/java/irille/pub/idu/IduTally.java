package irille.pub.idu;

import irille.gl.gs.GsGain;
import irille.pub.Cn;
import irille.pub.bean.Bean;

/**
 * 数据库功能操作：其他操作 在子类中必须声明常量CN，用类似的语句：public static Cn CN = new Cn("功能 代码",
 * "功能名称");
 * 
 * @param <BEAN>
 */
public class IduTally<T extends IduTally, BEAN extends Bean> extends
		Idu<T, BEAN> {
	public static Cn CN = new Cn("doTally", "记账");

	/**
	 * 操作库的操作直接在各重构的方法中执行
	 */
	@Override
	public final void commit() {
		before();
		run();
		after();
		log();
	}
}
