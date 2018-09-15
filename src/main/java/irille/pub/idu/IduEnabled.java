package irille.pub.idu;

import irille.core.sys.Sys;
import irille.pub.Cn;
import irille.pub.bean.Bean;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

/**
 * 数据库功能操基类
 * 在子类中必须声明常量CN，用类似的语句：public static Cn CN = new Cn("功能 代码", "功能名称");
 * @author whx
 * 
 * @param <BEAN>
 */
public class IduEnabled<T extends IduEnabled, BEAN extends Bean> extends Idu<T, BEAN> {
	public static Cn CN = new Cn("doEnabled", "启用");

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
		Tb tb = Tb.getTBByBean(clazz());
		if (tb.chk("enabled")) {
			Fld fld = tb.get("enabled");
			getB().propertySet(fld, Sys.OEnabled.TRUE.getLine().getKey());
			getB().upd();
		}
	}

}
