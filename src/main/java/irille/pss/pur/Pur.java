//Created on 2005-10-24
package irille.pss.pur;

import irille.core.sys.Sys;
import irille.core.sys.SysModule;
import irille.core.sys.SysSupplier;
import irille.pub.Log;
import irille.pub.Str;
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
public class Pur extends PackageBase {
	private static final Log LOG = new Log(Pur.class);
	public static final Pur INST = new Pur();
	public static TbBase TB = new TbBase<Tb>(Pur.class, "采购模块"); // 定义公共的Fld对象用
	public static final Sys.T SYS = Sys.T.AMT;

	private Pur() {
	}

	@Override
	public void initTbMsg() { // 初始化表信息
		addTb(1, PurOrder.class, TABLE_TYPE.FORM); //50001
		addTb(2, PurRev.class, TABLE_TYPE.FORM); //50002
		addTb(3, PurRtn.class, TABLE_TYPE.FORM); //50003
		addTb(4, PurPresent.class, TABLE_TYPE.FORM); //50004
		addTb(5, PurProt.class); //50003
		addTb(6, PurProtApply.class, TABLE_TYPE.FORM); //50006
		addTb(7, PurProtGoods.class); //50007
		addTb(8, PurProtGoodsApply.class, TABLE_TYPE.FORM); //50008
		addTb(9, PurMvIn.class, TABLE_TYPE.FORM); //50009
		addTb(10, PurOrderDirect.class, TABLE_TYPE.FORM); //500010
	}

	public void initTranData() { //初始化PrvTranData表数据
		addTD(new TranDataMsg(PurOrder.TB).u(PurOrder.T.BUYER).d(PurOrder.T.DEPT).c(PurOrder.T.CELL).o(PurOrder.T.ORG));
		addTD(new TranDataMsg(PurRev.TB).d(PurRev.T.WAREHOUSE).c(PurRev.T.CELL).o(PurRev.T.ORG));
		addTD(new TranDataMsg(PurRtn.TB).d(PurRtn.T.WAREHOUSE).c(PurRtn.T.CELL).o(PurRtn.T.ORG));
		addTD(new TranDataMsg(PurPresent.TB).d(PurPresent.T.WAREHOUSE).c(PurPresent.T.CELL).o(PurPresent.T.ORG));

		addTD(new TranDataMsg(PurProt.TB).u(PurProt.T.SUPPLIER, SysSupplier.T.BUSINESS_MEMBER)
		    .d(PurProt.T.SUPPLIER, SysSupplier.T.MNG_DEPT).o(PurProt.T.SUPPLIER, SysSupplier.T.MNG_ORG));
		addTD(new TranDataMsg(PurProtApply.TB).u(PurProtApply.T.SUPPLIER, SysSupplier.T.BUSINESS_MEMBER)
		    .d(PurProtApply.T.SUPPLIER, SysSupplier.T.MNG_DEPT).o(PurProtApply.T.SUPPLIER, SysSupplier.T.MNG_ORG));
		addTD(new TranDataMsg(PurProtApply.TB).u(PurProtApply.T.CREATED_BY).d(PurProtApply.T.DEPT).o(PurProtApply.T.ORG));

		addTD(new TranDataMsg(PurProtGoods.TB).u(PurProtGoods.T.SUPPLIER, SysSupplier.T.BUSINESS_MEMBER)
		    .d(PurProtGoods.T.SUPPLIER, SysSupplier.T.MNG_DEPT).o(PurProtGoods.T.SUPPLIER, SysSupplier.T.MNG_ORG));
		addTD(new TranDataMsg(PurProtGoodsApply.TB).u(PurProtGoodsApply.T.SUPPLIER, SysSupplier.T.BUSINESS_MEMBER)
		    .d(PurProtGoodsApply.T.SUPPLIER, SysSupplier.T.MNG_DEPT).o(PurProtGoodsApply.T.SUPPLIER, SysSupplier.T.MNG_ORG));
		addTD(new TranDataMsg(PurProtGoodsApply.TB).u(PurProtGoodsApply.T.CREATED_BY).d(PurProtGoodsApply.T.DEPT)
		    .o(PurProtGoodsApply.T.ORG));

		addTD(new TranDataMsg(PurMvIn.TB).d(PurMvIn.T.WAREHOUSE).c(PurMvIn.T.CELL).o(PurMvIn.T.ORG));

		addTD(new TranDataMsg(PurOrderDirect.TB).u(PurOrderDirect.T.BUYER).d(PurOrderDirect.T.DEPT)
		    .c(PurOrderDirect.T.CELL).o(PurOrderDirect.T.ORG));
	}

	@Override
	public SysModule initModule() {
		return iuModule(Pur.TB, "pur-采购管理-20");
	}

	/**
	 * 初始化，在运行期间仅执行一次
	 */
	public void initOnlyOne() { // 初始化方法，在每次启动时执行一次
		super.initOnlyOne();
	}

	public static enum SubjectAlias implements ISubjectAlias {//@formatter:off
		PUR_INCOME("采购-应付账款"),
		PUR_PAY_SUP("采购-预付账款"),
		PUR_PENDING("采购-待处理账户"),
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
		CST_PUR("成本核算.采购收货单"),
		CST_PUR_DIRECT("成本核算.采购直销单"),
		CST_PUR_PRESENT("成本核算.采购受赠单"),
		CST_PUR_RTN("成本核算.采购退货单"),
		CST_PUR_IN("成本核算.调入单"),
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

	public enum OSettleType implements IEnumOpt {
		CS(1, "现结"), OC(2, "挂账");
		public static String NAME = "结算类型";
		public static final OSettleType DEFAULT = CS; // 定义缺省值
		private EnumLine _line;

		private OSettleType(int key, String name) {
			_line = new EnumLine(this, key, name);
		}

		public EnumLine getLine() {
			return _line;
		}
	}

	public enum OSpeType implements IEnumOpt {//@formatter:off
		CO(1,"普通订单"),SO(2,"特价订单");
		public static final String NAME="订单类型";
		public static final OSpeType DEFAULT = CO; // 定义缺省值
		private EnumLine _line;
		private OSpeType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		

	public enum OSettleFlag implements IEnumOpt {//@formatter:off
		REV(1,"按到货数量"),SEND(2,"按发货数量");
		public static final String NAME="结算标准";
		public static final OSettleFlag DEFAULT = REV; // 定义缺省值
		private EnumLine _line;
		private OSettleFlag(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}	
	
	public enum OOrderStatus implements IEnumOpt {
		INIT(1, "未完成"), CLOSE(2,"已关闭");
		public static String NAME = "开单状态";
		public static final OOrderStatus DEFAULT = INIT; // 定义缺省值
		private EnumLine _line;
		private OOrderStatus(int key, String name) {
			_line = new EnumLine(this, key, name);
		}
		public EnumLine getLine() { return _line;}
	}
	
}
