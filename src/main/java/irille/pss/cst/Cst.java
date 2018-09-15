//Created on 2005-10-24
package irille.pss.cst;

import irille.core.sys.Sys;
import irille.core.sys.SysModule;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.PackageBase;
import irille.pub.tb.EnumLine;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.tb.TbBase;

public class Cst extends PackageBase {
	private static final Log LOG = new Log(Cst.class);
	public static final Cst INST = new Cst();
	public static TbBase TB = new TbBase<Tb>(Cst.class, "成本核算"); // 定义公共的Fld对象用
	public static final Sys.T SYS = Sys.T.AMT;

	private Cst() {
	}

	@Override
	public void initTbMsg() { // 初始化表信息
		addTb(1, CstSalInvoice.class);
		addTb(2, CstSalInvoiceRed.class);
		addTb(3, CstPurInvoice.class);
		addTb(4, CstPurInvoiceRed.class);
		addTb(5, CstMvInvoiceIn.class);
		addTb(6, CstMvInvoiceOut.class);
		addTb(7, CstOut.class);
		addTb(8, CstOutRed.class);
		addTb(9, CstIn.class);
		addTb(10, CstInRed.class);
	}

	public void initTranData() { //初始化PrvTranData表数据
		addTD(new TranDataMsg(CstSalInvoice.TB).u(CstSalInvoice.T.CREATED_BY).d(CstSalInvoice.T.DEPT)
		    .c(CstSalInvoice.T.CELL).o(CstSalInvoice.T.ORG));
		addTD(new TranDataMsg(CstSalInvoiceRed.TB).u(CstSalInvoiceRed.T.CREATED_BY).d(CstSalInvoiceRed.T.DEPT)
		    .c(CstSalInvoiceRed.T.CELL).o(CstSalInvoiceRed.T.ORG));
		addTD(new TranDataMsg(CstPurInvoice.TB).u(CstPurInvoice.T.CREATED_BY).d(CstPurInvoice.T.DEPT)
		    .c(CstPurInvoice.T.CELL).o(CstPurInvoice.T.ORG));
		addTD(new TranDataMsg(CstPurInvoiceRed.TB).u(CstPurInvoiceRed.T.CREATED_BY).d(CstPurInvoiceRed.T.DEPT)
		    .c(CstPurInvoiceRed.T.CELL).o(CstPurInvoiceRed.T.ORG));
		addTD(new TranDataMsg(CstMvInvoiceIn.TB).u(CstMvInvoiceIn.T.CREATED_BY).d(CstMvInvoiceIn.T.DEPT)
		    .c(CstMvInvoiceIn.T.CELL).o(CstMvInvoiceIn.T.ORG));
		addTD(new TranDataMsg(CstMvInvoiceOut.TB).u(CstMvInvoiceOut.T.CREATED_BY).d(CstMvInvoiceOut.T.DEPT)
		    .c(CstMvInvoiceOut.T.CELL).o(CstMvInvoiceOut.T.ORG));
		addTD(new TranDataMsg(CstOut.TB).u(CstOut.T.CREATED_BY).d(CstOut.T.DEPT).c(CstOut.T.CELL).o(CstOut.T.ORG));
		addTD(new TranDataMsg(CstOutRed.TB).u(CstOutRed.T.CREATED_BY).d(CstOutRed.T.DEPT).c(CstOutRed.T.CELL)
		    .o(CstOutRed.T.ORG));
		addTD(new TranDataMsg(CstIn.TB).u(CstIn.T.CREATED_BY).d(CstIn.T.DEPT).c(CstIn.T.CELL).o(CstIn.T.ORG));
		addTD(new TranDataMsg(CstInRed.TB).u(CstInRed.T.CREATED_BY).d(CstInRed.T.DEPT).c(CstInRed.T.CELL).o(CstInRed.T.ORG));
	}

	@Override
	public SysModule initModule() {
		return iuModule(Cst.TB, "cst-成本管理-50");
	}

	public void initOnlyOne() { // 初始化方法，在每次启动时执行一次
		super.initOnlyOne();
	}

	public static enum SubjectAlias implements ISubjectAlias {//@formatter:off
		CST_INCOME("发票-销售收入"),
		CST_TAX("发票-销售税金"),
		CST_PUR("发票-商品采购"),
		CST_MV_GOODSIN("调入发票-商品采购-内部"),
		CST_MV_GOODSOUT("调入发票-商品采购-外部"),
		CST_MV_IN("调出发票-调拨收入-内部"),
		CST_MV_OUT("调出发票-调拨收入-外部"),
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

	public enum OInvoiceType implements IEnumOpt {
		CO(1, "普通发票"), AT(2, "增值税发票");
		public static String NAME = "发票类型";
		public static final OInvoiceType DEFAULT = CO; // 定义缺省值
		private EnumLine _line;

		private OInvoiceType(int key, String name) {
			_line = new EnumLine(this, key, name);
		}

		public EnumLine getLine() {
			return _line;
		}
	}

}
