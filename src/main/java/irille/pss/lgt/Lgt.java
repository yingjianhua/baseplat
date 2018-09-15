//Created on 2005-10-24
package irille.pss.lgt;

import irille.core.sys.Sys;
import irille.core.sys.SysModule;
import irille.pub.Log;
import irille.pub.bean.PackageBase;
import irille.pub.tb.Tb;
import irille.pub.tb.TbBase;

/**
 * Title: Logistics<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class Lgt extends PackageBase {
	private static final Log LOG = new Log(Lgt.class);
	public static final Lgt INST = new Lgt();
	public static TbBase TB = new TbBase<Tb>(Lgt.class, "物流模块"); // 定义公共的Fld对象用
	public static final Sys.T SYS = Sys.T.AMT;

	private Lgt() {
	}

	@Override
	public void initTbMsg() { // 初始化表信息
	}

	@Override
	public void initTranData() {
	}

	@Override
	public SysModule initModule() {
		return iuModule(Lgt.TB, null);
	}

	/**
	 * 初始化，在运行期间仅执行一次
	 */
	public void initOnlyOne() { // 初始化方法，在每次启动时执行一次
		super.initOnlyOne();
	}

}
