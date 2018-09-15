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
public class IduDel<T extends IduDel, BEAN extends Bean> extends Idu<T, BEAN> {
	public static Cn CN = new Cn("sc", "删除");

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
		b.del();
	}

}
