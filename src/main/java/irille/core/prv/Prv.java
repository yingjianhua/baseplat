package irille.core.prv;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysModule;
import irille.pub.Log;
import irille.pub.bean.PackageBase;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.tb.TbBase;

public class Prv extends PackageBase {
	private static final Log LOG = new Log(Prv.class);
	public static final Prv INST = new Prv();
	public static TbBase TB = new TbBase<Tb>(Prv.class, "权限管理"); // 定义公共的Fld对象用
	public static final Sys.T SYS = Sys.T.AMT;

	private Prv() {
	}

	@Override
	public void initTbMsg() { // 初始化表信息
		addTb(1, PrvRole.class);
		addTb(2, PrvTranData.class);
		addTb(3, PrvTranGrp.class);
		addTb(4, PrvRoleTran.class);
	}
	public void initTranData() { //初始化PrvTranData表数据
		addTD(new TranDataMsg(PrvRole.TB).c(PrvRole.T.CELL).o(PrvRole.T.CELL,SysCell.T.ORG));
		addTD(new TranDataMsg(PrvTranData.TB));
		addTD(new TranDataMsg(PrvTranGrp.TB));
	}
	
	@Override
	public SysModule initModule() {
		return iuModule(Prv.TB, null);
	}
	
	/**
	 * 初始化，在运行期间仅执行一次
	 */
	public void initOnlyOne() { // 初始化方法，在每次启动时执行一次
		super.initOnlyOne();
	}

	public enum T implements IEnumFld {//@formatter:off
		;
		private Fld _fld;
		private T(Class clazz, String name, boolean... isnull) {
			_fld = TB.addOutKey(clazz, this, name, isnull);}
		private T(IEnumFld fld, boolean... isnull) {this(fld, null, isnull);}
		private T(IEnumFld fld, String name, boolean... null1) {_fld = TB.add(fld, this, name, null1);}
		private T(IEnumFld fld, String name, int strLen) {_fld = TB.add(fld, this, name, strLen);}
		private T(Fld fld) {_fld = TB.add(fld);}
		public Fld getFld() {return _fld;}
	}//@formatter:on

	static { //在此可以加一些对FLD进行特殊设定的代码
		//		T.LOGIN.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}

	public enum OPrvType implements IEnumOpt {//@formatter:off
		NO(0, "无法查看"),
		USER(1,"本人"),DEPT(11,"本部门"),DEPT_EXT(12,"本级及下级部门"),
		CELL(21,"本核算单元"),CELL_EXT(22,"本级及下级核算单元"),
		ORG(31,"本机构"),ORG_EXT(32,"本级及下级机构"),ALL(99,"不限制");
		public static final String NAME="权限类型";
		public static final OPrvType DEFAULT = NO; // 定义缺省值
		private EnumLine _line;
		private OPrvType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

}
