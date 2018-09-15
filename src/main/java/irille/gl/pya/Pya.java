//Created on 2005-10-24
package irille.gl.pya;

import irille.core.sys.Sys;
import irille.core.sys.SysModule;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.PackageBase;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
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
public class Pya extends PackageBase {
	private static final Log LOG = new Log(Pya.class);
	public static final Pya INST = new Pya();
	public static TbBase TB = new TbBase<Tb>(Pya.class, "应付模块"); // 定义公共的Fld对象用
	public static final Sys.T SYS = Sys.T.AMT;

	private Pya() {
	}

	@Override
	public void initTbMsg() { // 初始化表信息
		addTb(1, PyaNotePayable.class);
		addTb(2, PyaNotePayableLine.class);
		addTb(3, PyaNoteAccountPayable.class);
		addTb(4, PyaNoteAccountPayableLine.class);
		addTb(5, PyaNoteDepositPayable.class);
		addTb(6, PyaNoteDepositPayableLine.class);
		
		addTb(11, PyaPayBill.class);
		addTb(12, PyaPayWriteoffBill.class);
		addTb(15, PyaPayOtherBill.class);
		addTb(16, PyaPayOtherWriteoffBill.class);
		addTb(18, PyaPayDepBill.class);
		addTb(19, PyaPayDepWriteoffBill.class);
		
	}
	
	public void initTranData() { //初始化PrvTranData表数据
		addTD(new TranDataMsg(PyaPayBill.TB).u(PyaPayBill.T.CREATED_BY).d(PyaPayBill.T.DEPT).c(PyaPayBill.T.CELL).o(PyaPayBill.T.ORG));
		addTD(new TranDataMsg(PyaPayWriteoffBill.TB).u(PyaPayWriteoffBill.T.CREATED_BY).d(PyaPayWriteoffBill.T.DEPT).c(PyaPayWriteoffBill.T.CELL).o(PyaPayWriteoffBill.T.ORG));
		addTD(new TranDataMsg(PyaPayOtherBill.TB).u(PyaPayOtherBill.T.CREATED_BY).d(PyaPayOtherBill.T.DEPT).c(PyaPayOtherBill.T.CELL).o(PyaPayOtherBill.T.ORG));
		addTD(new TranDataMsg(PyaPayOtherWriteoffBill.TB).u(PyaPayOtherWriteoffBill.T.CREATED_BY).d(PyaPayOtherWriteoffBill.T.DEPT).c(PyaPayOtherWriteoffBill.T.CELL).o(PyaPayOtherWriteoffBill.T.ORG));
		addTD(new TranDataMsg(PyaPayDepBill.TB).u(PyaPayDepBill.T.CREATED_BY).d(PyaPayDepBill.T.DEPT).c(PyaPayDepBill.T.CELL).o(PyaPayDepBill.T.ORG));
		addTD(new TranDataMsg(PyaPayDepWriteoffBill.TB).u(PyaPayDepWriteoffBill.T.CREATED_BY).d(PyaPayDepWriteoffBill.T.DEPT).c(PyaPayDepWriteoffBill.T.CELL).o(PyaPayDepWriteoffBill.T.ORG));
	}
	
	@Override
	public SysModule initModule() {
		return iuModule(Pya.TB, "pya-应收应付-70");
	}
	
	/**
	 * 初始化，在运行期间仅执行一次
	 */
	public void initOnlyOne() { // 初始化方法，在每次启动时执行一次
		super.initOnlyOne();
	}

	public enum T implements IEnumFld {
		;
		private Fld _fld;

		private T(Class clazz, String name, boolean... isnull) {
			_fld = TB.addOutKey(clazz, this, name, isnull);
		}

		private T(IEnumFld fld, boolean... isnull) {
			this(fld, null, isnull);
		}

		private T(IEnumFld fld, String name, boolean... null1) {
			_fld = TB.add(fld, this, name, null1);
		}

		private T(IEnumFld fld, String name, int strLen) {
			_fld = TB.add(fld, this, name, strLen);
		}

		private T(Fld fld) {
			_fld = TB.add(fld);
		}

		public Fld getFld() {
			return _fld;
		}
	}

	static { //在此可以加一些对FLD进行特殊设定的代码
		//		T.LOGIN.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}

	//科目别名定义，Bill与Note会用到
	public static enum SubjectAlias implements ISubjectAlias {//@formatter:off
			PA_SUPPLIER("应付账款.供应商"),  //需要有选择器
			PA_CELL("应付账款.外机构核算单元"),
			PA_INNER_CELL("应付账款.本机构核算单元"),
			PA_OTHER("应付账款.散户"), //单账户科目
			PD_SUPPLIER("预付账款.供应商"),
			PD_CELL("预付账款.外机构核算单元"),
			PD_INNER_CELL("预付账款.本机构核算单元"),
			PD_OTHER("预付账款.散户"), //单账户科目
			PO_USER("其它应付款.职员"),
			PO_CELL("其它应付款.外机构核算单元"),
			PO_INNER_CELL("其它应付款.本机构核算单元"),
			PO_DEPT("其它应付款.部门"),
			PO_ACCOUNT("其它应付款.指定账户"),
			PO_OTHER("其它应付款.其它"), //单账户科目		
			;
			private String _name;
			private SubjectAlias(String name) { _name=name;} 
			public String getName(){return _name;}
			public String getSubName(int i){return Str.split(_name,".")[i]; }
			public String getCode(){return Str.tranLineUpperToField(name());}
	 }//@formatter:on

	@Override
	public ISubjectAlias[] getSubjectAliases() { //取所有的科目别名
		return SubjectAlias.values();
	}
	
	public enum OPaType implements IEnumOpt {//@formatter:off
			SUPPLIER(1,SubjectAlias.PA_SUPPLIER),OUTER_CELL(2,SubjectAlias.PA_CELL),
			INNER_CELL(3,SubjectAlias.PA_INNER_CELL),OTHER(99,SubjectAlias.PA_OTHER);
			public static final String NAME="应付账款类型";
			public static final OPaType DEFAULT = SUPPLIER; // 定义缺省值
			private EnumLine _line;
			private SubjectAlias _alias;
			private OPaType(int key, SubjectAlias alias) {
				_alias=alias;
				_line=new EnumLine(this,key,alias.getSubName(1));	
			}
			public EnumLine getLine(){return _line;	}
			public SubjectAlias getAlias() {return _alias; }
		}		//@formatter:on
	
	public enum OPdType implements IEnumOpt {//@formatter:off
		SUPPLIER(1,SubjectAlias.PD_SUPPLIER),OUTER_CELL(2,SubjectAlias.PD_CELL),
		INNER_CELL(3,SubjectAlias.PD_INNER_CELL),OTHER(99,SubjectAlias.PD_OTHER);
		public static final String NAME="预付账款类型";
		public static final OPdType DEFAULT = SUPPLIER; // 定义缺省值
		private EnumLine _line;
		private SubjectAlias _alias;
		private OPdType(int key, SubjectAlias alias) {
			_alias=alias;
			_line=new EnumLine(this,key,alias.getSubName(1));	
		}
		public EnumLine getLine(){return _line;	}
		public SubjectAlias getAlias() {return _alias; }
	}		//@formatter:on
	
	public enum OPayableType implements IEnumOpt {//@formatter:off
		USER(1,SubjectAlias.PO_USER),OUTER_CELL(2,SubjectAlias.PO_CELL),
		INNER_CELL(3,SubjectAlias.PO_INNER_CELL),
		DEPT(4,SubjectAlias.PO_DEPT),ACCOUNT(5,SubjectAlias.PO_ACCOUNT),
		OTHER(99,SubjectAlias.PO_OTHER);
		public static final String NAME="其它应付款类型";
		public static final OPayableType DEFAULT = USER; // 定义缺省值
		private EnumLine _line;
		private SubjectAlias _alias;
		private OPayableType(int key, SubjectAlias alias) {
			_alias=alias;
			_line=new EnumLine(this,key,alias.getSubName(1));	
		}
		public EnumLine getLine(){return _line;	}
		public SubjectAlias getAlias() {return _alias; }
	}		//@formatter:on

}
