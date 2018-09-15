//Created on 2005-10-24
package irille.gl.frm;

import irille.core.sys.Sys;
import irille.core.sys.SysModule;
import irille.core.sys.SysUser;
import irille.pub.Log;
import irille.pub.bean.PackageBase;
import irille.pub.tb.EnumLine;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.tb.TbBase;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class Frm extends PackageBase {
	private static final Log LOG = new Log(Frm.class);
	public static final Frm INST = new Frm();
	public static TbBase TB = new TbBase<Tb>(Frm.class, "单据模块"); // 定义公共的Fld对象用
	public static final Sys.T SYS = Sys.T.AMT;

	private Frm() {
	}

	@Override
	public void initTbMsg() { // 初始化表信息
		addTb(1, FrmHandover.class);
		addTb(2, FrmLink.class);
		addTb(3, FrmPending.class);
	}

	public void initTranData() { //初始化PrvTranData表数据
		addTD(new TranDataMsg(FrmHandover.TB).u(FrmHandover.T.CREATED_BY).d(FrmHandover.T.DEPT).o(FrmHandover.T.ORG));
		addTD(new TranDataMsg(FrmHandover.TB).u(FrmHandover.T.APPR_BY).d(FrmHandover.T.APPR_BY, SysUser.T.DEPT)
		    .o(FrmHandover.T.ORG));
		addTD(new TranDataMsg(FrmLink.TB));
		addTD(new TranDataMsg(FrmPending.TB).u(FrmPending.T.USER_SYS).d(FrmPending.T.USER_SYS, SysUser.T.DEPT).c(FrmPending.T.CELL)
		    .o(FrmPending.T.ORG));
	}

	@Override
	public SysModule initModule() {
		return iuModule(Frm.TB, null);
	}

	/**
	 * 初始化，在运行期间仅执行一次
	 */
	public void initOnlyOne() { // 初始化方法，在每次启动时执行一次
		super.initOnlyOne();
	}


	public enum OLinkType implements IEnumOpt {//@formatter:off
		SOURCE_DESC(1,"源--目的"),MAIN_NOTE(2,"主--明细"),
		LINK(3,"关联")
		;
		public static final String NAME="关联类型";
		public static final OLinkType DEFAULT = SOURCE_DESC; // 定义缺省值
		private EnumLine _line;
		private OLinkType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

}
