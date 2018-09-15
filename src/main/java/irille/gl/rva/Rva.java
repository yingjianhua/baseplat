//Created on 2005-10-24
package irille.gl.rva;

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
public class Rva extends PackageBase {
	private static final Log LOG = new Log(Rva.class);
	public static final Rva INST = new Rva();
	public static TbBase TB = new TbBase<Tb>(Rva.class, "应收模块"); // 定义公共的Fld对象用
	public static final Sys.T SYS = Sys.T.AMT;

	private Rva() {
	}

	@Override
	public void initTbMsg() { // 初始化表信息
		addTb(1, RvaNoteAccount.class);
		addTb(2, RvaNoteAccountLine.class);
		addTb(3, RvaNoteDeposit.class);
		addTb(4, RvaNoteDepositLine.class);
		addTb(5, RvaNoteOther.class);
		addTb(6, RvaNoteOtherLine.class);

		addTb(11, RvaRecBill.class);
		addTb(12, RvaRecWriteoffBill.class);
		addTb(15, RvaRecOtherBill.class);
		addTb(16, RvaRecOtherWriteoffBill.class);
		addTb(18, RvaRecDepBill.class);
		addTb(19, RvaRecDepWriteoffBill.class);
	}
	public void initTranData() { //初始化PrvTranData表数据
		addTD(new TranDataMsg(RvaRecBill.TB).u(RvaRecBill.T.CREATED_BY).d(RvaRecBill.T.DEPT).c(RvaRecBill.T.CELL).o(RvaRecBill.T.ORG));
		addTD(new TranDataMsg(RvaRecWriteoffBill.TB).u(RvaRecWriteoffBill.T.CREATED_BY).d(RvaRecWriteoffBill.T.DEPT).c(RvaRecWriteoffBill.T.CELL).o(RvaRecWriteoffBill.T.ORG));
		addTD(new TranDataMsg(RvaRecOtherBill.TB).u(RvaRecOtherBill.T.CREATED_BY).d(RvaRecOtherBill.T.DEPT).c(RvaRecOtherBill.T.CELL).o(RvaRecOtherBill.T.ORG));
		addTD(new TranDataMsg(RvaRecOtherWriteoffBill.TB).u(RvaRecOtherWriteoffBill.T.CREATED_BY).d(RvaRecOtherWriteoffBill.T.DEPT).c(RvaRecOtherWriteoffBill.T.CELL).o(RvaRecOtherWriteoffBill.T.ORG));
		addTD(new TranDataMsg(RvaRecDepBill.TB).u(RvaRecDepBill.T.CREATED_BY).d(RvaRecDepBill.T.DEPT).c(RvaRecDepBill.T.CELL).o(RvaRecDepBill.T.ORG));
		addTD(new TranDataMsg(RvaRecDepWriteoffBill.TB).u(RvaRecDepWriteoffBill.T.CREATED_BY).d(RvaRecDepWriteoffBill.T.DEPT).c(RvaRecDepWriteoffBill.T.CELL).o(RvaRecDepWriteoffBill.T.ORG));
	}
	
	@Override
	public SysModule initModule() {
		return iuModule(Rva.TB, null);
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
		private T(Class clazz,String name,boolean... isnull) 
		{_fld=TB.addOutKey(clazz,this,name,isnull);	}
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
//		T.LOGIN.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}//@formatter:on

	public static enum SubjectAlias implements ISubjectAlias {//@formatter:off
		RA_CUST("应收账款.客户"),  //需要有选择器
		RA_CELL("应收账款.外机构核算单元"),
		RA_INNER_CELL("应收账款.本机构核算单元"),
		RA_OTHER("应收账款.散户"), //单账户科目
		RD_CUST("预收账款.客户"),
		RD_CELL("预收账款.外机构核算单元"),
		RD_INNER_CELL("预收账款.本机构核算单元"),
		RD_OTHER("预收账款.散户"), //单账户科目
		RO_USER("其它应收款.职员"),
		RO_CELL("其它应收款.外机构核算单元"),
		RO_INNER_CELL("其它应收款.本机构核算单元"),
		RO_DEPT("其它应收款.部门"),
		RO_ACCOUNT("其它应收款.指定账户"),
		RO_OTHER("其它应收款.其它"), //单账户科目		
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

	public enum ORaType implements IEnumOpt {//@formatter:off
		CUST(1,SubjectAlias.RA_CUST),OUTER_CELL(2,SubjectAlias.RA_CELL),
		INNER_CELL(3,SubjectAlias.RA_INNER_CELL),OTHER(99,SubjectAlias.RA_OTHER);
		public static final String NAME="应收账款类型";
		public static final ORaType DEFAULT = CUST; // 定义缺省值
		private EnumLine _line;
		private SubjectAlias _alias;
		private ORaType(int key, SubjectAlias alias) {
			_alias=alias;
			_line=new EnumLine(this,key,alias.getSubName(1));	
		}
		public EnumLine getLine(){return _line;	}
		public SubjectAlias getAlias() {return _alias; }
	}		//@formatter:on

	public enum ORdType implements IEnumOpt {//@formatter:off
		CUST(1,SubjectAlias.RD_CUST),OUTER_CELL(2,SubjectAlias.RD_CELL),
	INNER_CELL(3,SubjectAlias.RD_INNER_CELL),OTHER(99,SubjectAlias.RD_OTHER);
	public static final String NAME="预收账款类型";
	public static final ORdType DEFAULT = CUST; // 定义缺省值
	private EnumLine _line;
	private SubjectAlias _alias;
	private ORdType(int key, SubjectAlias alias) {
		_alias=alias;
		_line=new EnumLine(this,key,alias.getSubName(1));	
	}
	public EnumLine getLine(){return _line;	}
	public SubjectAlias getAlias() {return _alias; }
}		//@formatter:on

	public enum ORoType implements IEnumOpt {//@formatter:off
	USER(1,SubjectAlias.RO_USER),OUTER_CELL(2,SubjectAlias.RO_CELL),
	INNER_CELL(3,SubjectAlias.RO_INNER_CELL),
	DEPT(4,SubjectAlias.RO_DEPT),ACCOUNT(5,SubjectAlias.RO_ACCOUNT),
	OTHER(99,SubjectAlias.RO_OTHER);
	public static final String NAME="其它应收款类型";
	public static final ORoType DEFAULT = USER; // 定义缺省值
	private EnumLine _line;
	private SubjectAlias _alias;
	private ORoType(int key, SubjectAlias alias) {
		_alias=alias;
		_line=new EnumLine(this,key,alias.getSubName(1));	
	}
	public EnumLine getLine(){return _line;	}
	public SubjectAlias getAlias() {return _alias; }
}		//@formatter:on
}
