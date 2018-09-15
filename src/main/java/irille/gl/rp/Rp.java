//Created on 2005-10-24
package irille.gl.rp;

import irille.core.sys.Sys;
import irille.core.sys.SysModule;
import irille.core.sys.SysUser;
import irille.pub.Log;
import irille.pub.bean.PackageBase;
import irille.pub.tb.EnumLine;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.IEnumOptObj;
import irille.pub.tb.Tb;
import irille.pub.tb.TbBase;

public class Rp extends PackageBase {
	private static final Log LOG = new Log(Rp.class);
	public static final Rp INST = new Rp();
	public static TbBase TB = new TbBase<Tb>(Rp.class, "出纳模块"); // 定义公共的Fld对象用
	public static final Sys.T SYS = Sys.T.AMT;

	private Rp() {
	}

	@Override
	public void initTbMsg() { // 初始化表信息
		addTb(1, RpJournal.class); //935
		addTb(2, RpHandover.class, Sys.OTableType.FORM);//60001
		addTb(3, RpSeal.class);
		addTb(4, RpWorkBox.class);
		addTb(5, RpFundMv.class);
		addTb(6, RpFundMvIn.class);
		addTb(7, RpFundMvOut.class);
		addTb(11, RpStimatePay.class);
		addTb(12, RpStimateRec.class);
		addTb(13, RpNotePayBill.class);
		addTb(14, RpNoteRptBill.class);
		addTb(21, RpNoteCashPay.class);
		addTb(22, RpNoteCashRpt.class);
		addTb(23, RpNotePay.class);
		addTb(24, RpNoteRpt.class);
	}

	public void initTranData() { //初始化PrvTranData表数据
		addTD(new TranDataMsg(RpJournal.TB).u(RpJournal.T.CASHIER).d(RpJournal.T.CASHIER, SysUser.T.DEPT)
		    .c(RpJournal.T.CELL).o(RpJournal.T.ORG));
		addTD(new TranDataMsg(RpHandover.TB).u(RpHandover.T.SOURCE).d(RpHandover.T.DEPT).c(RpHandover.T.CELL)
		    .o(RpHandover.T.ORG));
		addTD(new TranDataMsg(RpHandover.TB).u(RpHandover.T.DESC_BY).d(RpHandover.T.DEPT).c(RpHandover.T.CELL)
		    .o(RpHandover.T.ORG));
		addTD(new TranDataMsg(RpWorkBox.TB).u(RpWorkBox.T.USER_SYS).d(RpWorkBox.T.USER_SYS, SysUser.T.DEPT).c(RpWorkBox.T.MNG_CELL)
		    .o(RpWorkBox.T.USER_SYS, SysUser.T.ORG));
		addTD(new TranDataMsg(RpSeal.TB).u(RpSeal.T.USER_SYS).d(RpSeal.T.USER_SYS, SysUser.T.DEPT).c(RpSeal.T.MNG_CELL)
		    .o(RpSeal.T.USER_SYS, SysUser.T.ORG));
		//addTD(new TranDataMsg(RpFundMvOld.TB).u(RpFundMvOld.T.DESC_BY).d(RpFundMvOld.T.DESC_BY, SysUser.T.DEPT)
		  //  .c(RpFundMvOld.T.DESC_CELL).o(RpFundMvOld.T.DESC_ORG));
		addTD(new TranDataMsg(RpFundMv.TB).u(RpFundMv.T.CREATED_BY).d(RpFundMv.T.DEPT).c(RpFundMv.T.CELL).o(RpFundMv.T.ORG));
		addTD(new TranDataMsg(RpFundMvIn.TB).u(RpFundMvIn.T.CREATED_BY).d(RpFundMvIn.T.DEPT).c(RpFundMvIn.T.CELL).o(RpFundMvIn.T.ORG));
		addTD(new TranDataMsg(RpFundMvOut.TB).u(RpFundMvOut.T.CREATED_BY).d(RpFundMvOut.T.DEPT).c(RpFundMvOut.T.CELL).o(RpFundMvOut.T.ORG));
		addTD(new TranDataMsg(RpStimatePay.TB).u(RpStimatePay.T.CREATED_BY).d(RpStimatePay.T.DEPT).c(RpStimatePay.T.CELL)
		    .o(RpStimatePay.T.ORG));
		addTD(new TranDataMsg(RpStimateRec.TB).u(RpStimateRec.T.CREATED_BY).d(RpStimateRec.T.DEPT).c(RpStimateRec.T.CELL)
		    .o(RpStimateRec.T.ORG));
		addTD(new TranDataMsg(RpNotePayBill.TB).u(RpNotePayBill.T.CREATED_BY).d(RpNotePayBill.T.DEPT)
		    .c(RpNotePayBill.T.CELL).o(RpNotePayBill.T.ORG));
		addTD(new TranDataMsg(RpNoteRptBill.TB).u(RpNoteRptBill.T.CREATED_BY).d(RpNoteRptBill.T.DEPT)
		    .c(RpNoteRptBill.T.CELL).o(RpNoteRptBill.T.ORG));
	}
	
	@Override
	public SysModule initModule() {
		return iuModule(Rp.TB, "rp-出纳管理-60");
	}

	/**
	 * 初始化，在运行期间仅执行一次
	 */
	public void initOnlyOne() { // 初始化方法，在每次启动时执行一次
		super.initOnlyOne();
	}

	public enum ORpJournalType implements IEnumOpt {
		CASHIER(1, "现金"), COM(2, "对公账户"), CARD(3, "对私卡账户"), OTHER(9, "第三方支付");
		public static String NAME = "账户类型";
		public static final ORpJournalType DEFAULT = CASHIER; // 定义缺省值
		private EnumLine _line;

		private ORpJournalType(int key, String name) {
			_line = new EnumLine(this, key, name);
		}

		public EnumLine getLine() {
			return _line;
		}
	}

	//	
	//	public enum OJournalType implements IEnumOpt {
	//		CA(1, "现金"), BA(2, "银行存款"), CB(3, "现金带回");
	//		public static String NAME = "账户类型";
	//		public static final OJournalType DEFAULT = CA; // 定义缺省值
	//		private EnumLine _line;
	//		private OJournalType(int key, String name) {
	//			_line = new EnumLine(this, key, name);
	//		}
	//		public EnumLine getLine() { return _line;}
	//	}

	public enum OSummaryType implements IEnumOpt {//@formatter:off
		PU(1,"交款"),AF(2,"拨款");
		public static final String NAME="摘要";
		public static final OSummaryType DEFAULT = PU; // 定义缺省值
		private EnumLine _line;
		private OSummaryType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public static interface IRpVars {

	}

	public static final void changeCreatedNameAndLock(Tb tb) {
		tb.get("createdBy").setName("交接人");
		tb.get("createdTime").setName("交出时间");
		tb.lockAllFlds();
	}

	public enum OPayType implements IEnumOptObj<Class> {//@formatter:off
		CASH(0,"现金",null), //	P_TMP(-12,"收:临时存欠",null),
		BANK_NET(1,"网银",null),
		BANK_TRAN(2,"转账支票",null),
		BANK_T_TRAN(21,"电汇",null),	
		BANK_CASH(22,"现金支票",null),
		BANK_CONSIGN(31,"委托付款",null),
		BANK_AUTO(32,"自动划转",null),	
		OTHER(99,"其他",null),
		;
		public static String NAME="付款方式";
		public static OPayType DEFAULT = BANK_NET; // 定义缺省值
		private EnumLine _line;
		private Class _obj;
		private OPayType(int key, String name,Class obj) {
			_line=new EnumLine(this,key,name);_obj=obj;	}
		public EnumLine getLine(){return _line;	}
		public Class getObj(){return _obj;	}
	}	//@formatter:on

	public enum ORptType implements IEnumOptObj<Class> {//@formatter:off
		CASH(0,"现金",null),
		BANK_NET(1,"网银",null),
		BANK_TRAN(2,"转账",null),
		BANK_T_TRAN(21,"电汇",null),	
		BANK_CASH(22,"现金存入",null),
		BANK_CONSIGN(31,"委托收款",null),
		OTHER(99,"其他",null),
		;
		public static String NAME="收款方式";
		public static ORptType DEFAULT = BANK_NET; // 定义缺省值
		private EnumLine _line;
		private Class _obj;
		private ORptType(int key, String name,Class obj) {
			_line=new EnumLine(this,key,name);_obj=obj;	}
		public EnumLine getLine(){return _line;	}
		public Class getObj(){return _obj;	}
	}//@formatter:on

}
