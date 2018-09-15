//Created on 2005-10-24
package irille.pss.sal;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysCustom;
import irille.core.sys.SysModule;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.PackageBase;
import irille.pub.tb.EnumLine;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.OptCust;
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
public class Sal extends PackageBase {
	private static final Log LOG = new Log(Sal.class);
	public static final Sal INST = new Sal();
	public static TbBase TB = new TbBase<Tb>(Sal.class, "销售模块"); // 定义公共的Fld对象用
	public static final Sys.T SYS = Sys.T.AMT;

	private Sal() {
	}

	@Override
	public void initTbMsg() { // 初始化表信息
		addTb(1, SalOrder.class, Sys.OTableType.FORM); //40001
		addTb(2, SalSale.class, Sys.OTableType.FORM);//40002
		addTb(3, SalRtn.class, Sys.OTableType.FORM);//40003
		addTb(4, SalSaleDirect.class, Sys.OTableType.FORM);//40005
		addTb(5, SalPresent.class, Sys.OTableType.FORM);//40007
		addTb(6, SalReserve.class, Sys.OTableType.FORM);//40010
		addTb(11, SalDiscountPriv.class);//933
		addTb(14, SalMvOut.class, Sys.OTableType.FORM);//40201
		addTb(15, SalPriceProt.class);
		addTb(16, SalCustGoods.class);
		addTb(17, SalCollect.class);
		addTb(18, SalPriceProtMv.class);
	}

	public void initTranData() { //初始化PrvTranData表数据
		addTD(new TranDataMsg(SalOrder.TB).u(SalOrder.T.OPERATOR).d(SalOrder.T.DEPT).c(SalOrder.T.CELL).o(SalOrder.T.ORG));
		addTD(new TranDataMsg(SalSale.TB).u(SalSale.T.OPERATOR).d(SalSale.T.DEPT).c(SalSale.T.CELL).o(SalSale.T.ORG));
		addTD(new TranDataMsg(SalRtn.TB).u(SalRtn.T.OPERATOR).d(SalRtn.T.DEPT).c(SalRtn.T.CELL).o(SalRtn.T.ORG));
		addTD(new TranDataMsg(SalSaleDirect.TB).u(SalSaleDirect.T.OPERATOR).d(SalSaleDirect.T.DEPT).c(SalSaleDirect.T.CELL)
		    .o(SalSaleDirect.T.ORG));
		addTD(new TranDataMsg(SalPresent.TB).u(SalPresent.T.OPERATOR).d(SalPresent.T.DEPT).c(SalPresent.T.CELL)
		    .o(SalPresent.T.ORG));
		addTD(new TranDataMsg(SalReserve.TB).u(SalReserve.T.OPERATOR).d(SalReserve.T.DEPT).o(SalReserve.T.ORG));
		addTD(new TranDataMsg(SalDiscountPriv.TB));
		addTD(new TranDataMsg(SalMvOut.TB).d(SalMvOut.T.WAREHOUSE).c(SalMvOut.T.CELL).o(SalMvOut.T.ORG));
		addTD(new TranDataMsg(SalPriceProt.TB).u(SalPriceProt.T.CUST, SysCustom.T.BUSINESS_MEMBER)
		    .d(SalPriceProt.T.CUST, SysCustom.T.MNG_DEPT).o(SalPriceProt.T.CUST, SysCustom.T.MNG_ORG));
		addTD(new TranDataMsg(SalCustGoods.TB).u(SalCustGoods.T.CUST, SysCustom.T.BUSINESS_MEMBER)
		    .d(SalCustGoods.T.CUST, SysCustom.T.MNG_DEPT).o(SalCustGoods.T.CUST, SysCustom.T.MNG_ORG));
		addTD(new TranDataMsg(SalPriceProtMv.TB).c(SalPriceProtMv.T.CELL_PUR).o(SalPriceProtMv.T.CELL_PUR, SysCell.T.ORG));
		addTD(new TranDataMsg(SalPriceProtMv.TB).c(SalPriceProtMv.T.CELL_SAL).o(SalPriceProtMv.T.CELL_SAL, SysCell.T.ORG));
	}

	@Override
	public SysModule initModule() {
		return iuModule(Sal.TB, "sal-销售管理-10");
	}

	/**
	 * 初始化，在运行期间仅执行一次
	 */
	public void initOnlyOne() { // 初始化方法，在每次启动时执行一次
		super.initOnlyOne();
	}

	public static enum SubjectAlias implements ISubjectAlias {//@formatter:off
		SAL_INCOME("销售-应收账款"),
		SAL_PAY_CUST("销售-预收账款"),
		SAL_PENDING("销售-待处理账户"),
		SAL_MV("存财务中心款项"),
		;
		private String _name;
		private SubjectAlias(String name) { _name=name;} 
		public String getName(){return _name;}
		public String getSubName(int i){return Str.split(_name,".")[i]; }
		public String getCode(){return Str.tranLineUpperToField(name());}
 }			//@formatter:on

	@Override
	public ISubjectAlias[] getSubjectAliases() { //取所有的科目别名
		return SubjectAlias.values();
	}

	public static enum SubjectAliasCst implements ISubjectAlias {//@formatter:off
		CST_SAL("成本核算.销售单"),
		CST_SAL_DIRECT("成本核算.销售直销单"),
		CST_SAL_PRESENT("成本核算.销售赠送单"),
		CST_SAL_RTN("成本核算.销售退货单"),
		CST_SAL_OUT("成本核算.调出单"),
		;
		private String _name;
		private SubjectAliasCst(String name) { _name=name;} 
		public String getName(){return _name;}
		public String getSubName(int i){return Str.split(_name,".")[i]; }
		public String getCode(){return Str.tranLineUpperToField(name());}
}//@formatter:on

	@Override
	public ISubjectAlias[] getSubjectAliasesCst() {
		return SubjectAliasCst.values();
	}

	//用户选项定义
	public static enum OptCustEnum implements IOptCustEnum {//@formatter:off
		 SALE_GROSS("saleGross", "毛利类型");;
			private OptCust _opt;
			private OptCustEnum(String code,String name) { _opt= new OptCust(code,name);  } 
			public OptCust getOpt(){return _opt;}
	 }			//@formatter:on

	@Override
	public IOptCustEnum[] getOptCusts() { //取用户选项
		return OptCustEnum.values();
	}

	public enum OSalGroupType implements IEnumOpt {
		EM(1, "职员"), DATE(2, "日期"), ORG(3, "机构"), DEPT(4, "部门"), GOOD(5, "商品");
		public static String NAME = "销售分组类型";
		public static final OSalGroupType DEFAULT = EM; // 定义缺省值
		private EnumLine _line;

		private OSalGroupType(int key, String name) {
			_line = new EnumLine(this, key, name);
		}

		public EnumLine getLine() {
			return _line;
		}
	}

	public enum ODateGroupType implements IEnumOpt {
		Y(1, "按年"), M(2, "按月");
		public static String NAME = "日期分类类型";
		public static final ODateGroupType DEFAULT = Y; // 定义缺省值
		private EnumLine _line;

		private ODateGroupType(int key, String name) {
			_line = new EnumLine(this, key, name);
		}

		public EnumLine getLine() {
			return _line;
		}
	}

	public enum OGoodsGroupType implements IEnumOpt {
		G(1, "按商品"), GC(2, "按商品类别");
		public static String NAME = "商品分类类型";
		public static final OGoodsGroupType DEFAULT = G; // 定义缺省值
		private EnumLine _line;

		private OGoodsGroupType(int key, String name) {
			_line = new EnumLine(this, key, name);
		}

		public EnumLine getLine() {
			return _line;
		}
	}

	public enum ODateType implements IEnumOpt {//@formatter:off
		TY(1, "本年"), TM(2, "本月"), TD(3, "本日"), LM(4, "上月"), AP(5, "指定");
		public static final String NAME="日期类型";
		public static final ODateType DEFAULT = TY; // 定义缺省值
		private EnumLine _line;
		private ODateType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OSquType implements IEnumOpt {//@formatter:off
		PS(1, "正序"), IS(2, "逆序");
		public static final String NAME="名次类型";
		public static final OSquType DEFAULT = PS; // 定义缺省值
		private EnumLine _line;
		private OSquType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OBaseType implements IEnumOpt {//@formatter:off
		SV(1, "销售额");
		public static final String NAME="积分基数";
		public static final OBaseType DEFAULT = SV; // 定义缺省值
		private EnumLine _line;
		private OBaseType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OSalType implements IEnumOpt {//@formatter:off
		VIS(1, "上门"), TEL(2, "电话"), INT(3, "网上"), OTH(4, "其他");
		public static final String NAME="销售类型";
		public static final OSalType DEFAULT = VIS; // 定义缺省值
		private EnumLine _line;
		private OSalType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum ODiscountLevel implements IEnumOpt {//@formatter:off
		ON(1, "级别一"), TW(2, "级别二"), TH(3, "级别三"), FO(4, "级别四"), FI(5, "级别五"),
		SI(6, "级别六"), SE(7, "级别七"), EI(8, "级别八"), NI(9, "级别九"), TE(10, "级别十"), 
		EL(11, "级别十一"), TWE(12, "级别十二");
		public static final String NAME="折扣级别";
		public static final ODiscountLevel DEFAULT = ON; // 定义缺省值
		private EnumLine _line;
		private ODiscountLevel(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OCreditLevel implements IEnumOpt {//@formatter:off
		LO(1, "低"), MI(2, "中"), HI(3, "高");
		public static final String NAME="信用等级";
		public static final OCreditLevel DEFAULT = LO; // 定义缺省值
		private EnumLine _line;
		private OCreditLevel(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OProtocoType implements IEnumOpt {//@formatter:off
		RATE(1, "固定折扣"), PROT(2, "按协议价"), SAL(3, "按普通另售价");
		public static final String NAME="协议类型";
		public static final OProtocoType DEFAULT = RATE; // 定义缺省值
		private EnumLine _line;
		private OProtocoType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OGroupType implements IEnumOpt {
		CU(1, "客户"), DATE(2, "日期"), ORG(3, "机构"), DEPT(4, "部门"), GOOD(5, "商品");
		public static String NAME = "分组类型";
		public static final OGroupType DEFAULT = CU; // 定义缺省值
		private EnumLine _line;

		private OGroupType(int key, String name) {
			_line = new EnumLine(this, key, name);
		}

		public EnumLine getLine() {
			return _line;
		}
	}

	public enum OPoType implements IEnumOpt {
		PUR(1, "采购"), MV(2, "调拨");
		public static String NAME = "供应类型";
		public static final OPoType DEFAULT = PUR; // 定义缺省值
		private EnumLine _line;

		private OPoType(int key, String name) {
			_line = new EnumLine(this, key, name);
		}

		public EnumLine getLine() {
			return _line;
		}
	}

	public enum OShipType implements IEnumOpt {
		MY(1, "我方支付"), OTHER(2, "他方支付"), WAIT(3, "待定");
		public static String NAME = "费用承担方式";
		public static final OShipType DEFAULT = WAIT; // 定义缺省值
		private EnumLine _line;

		private OShipType(int key, String name) {
			_line = new EnumLine(this, key, name);
		}

		public EnumLine getLine() {
			return _line;
		}
	}

	public enum OOrderStatus implements IEnumOpt {
		INIT(1, "未完成"), CLOSE(2, "已关闭");
		public static String NAME = "开单状态";
		public static final OOrderStatus DEFAULT = INIT; // 定义缺省值
		private EnumLine _line;

		private OOrderStatus(int key, String name) {
			_line = new EnumLine(this, key, name);
		}

		public EnumLine getLine() {
			return _line;
		}
	}

	public enum OInoutStatus implements IEnumOpt {
		INIT(1, "未完成"), CRT(2, "已产生出入库单"), DONE(3, "已完成");
		public static String NAME = "出库状态";
		public static final OInoutStatus DEFAULT = INIT; // 定义缺省值
		private EnumLine _line;

		private OInoutStatus(int key, String name) {
			_line = new EnumLine(this, key, name);
		}

		public EnumLine getLine() {
			return _line;
		}
	}

}
