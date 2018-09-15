//Created on 2005-10-24
package irille.gl.gl;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysCustom;
import irille.core.sys.SysDept;
import irille.core.sys.SysEm;
import irille.core.sys.SysModule;
import irille.core.sys.SysOrg;
import irille.core.sys.SysProject;
import irille.core.sys.SysSupplier;
import irille.core.sys.SysTemplat;
import irille.core.sys.SysUser;
import irille.pub.Log;
import irille.pub.bean.PackageBase;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.IEnumOptObj;
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
public class Gl extends PackageBase {
	private static final Log LOG = new Log(Gl.class);
	public static final Gl INST = new Gl();
	public static TbBase TB = new TbBase<Tb>(Gl.class, "财务核算"); // 定义公共的Fld对象用
	public static final Sys.T SYS = Sys.T.AMT;

	private Gl() {
	}

	@Override
	public void initTbMsg() { // 初始化表信息
		addTb(1, GlSubject.class); //21s
		addTb(2, GlEntryDef.class); //20001
		addTb(3, GlDaybook.class);//20002
		addTb(4, GlTransform.class, Sys.OTableType.BILL); //20003
		addTb(5, GlSubjectMap.class);//906
		addTb(6, GlJournal.class);//907
		addTb(7, GlDailyLedger.class);//936
		addTb(8, GlNote.class);//937
		addTb(9, GlNoteAmtCancel.class); //938
		addTb(10, GlNoteWriteoff.class); //939
		addTb(11, GlRateType.class); //940
		addTb(12, GlRate.class);//941
		addTb(13, GlReport.class);
		addTb(14, GlReportAsset.class);
		addTb(15, GlReportProfit.class);
		addTb(16, GlCarryOver.class);
		addTb(17, GlGoods.class);
		addTb(18, GlAgeRvaView.class);
		addTb(19, GlAgePyaView.class);
	}
	public void initTranData() { //初始化PrvTranData表数据
		addTD(new TranDataMsg(GlSubject.TB).c(GlSubject.T.TEMPLAT,SysTemplat.T.MNG_CELL));
		addTD(new TranDataMsg(GlEntryDef.TB).c(GlEntryDef.T.TEMPLAT,SysTemplat.T.MNG_CELL));
		addTD(new TranDataMsg(GlDaybook.TB).u(GlDaybook.T.TALLY_BY).d(GlDaybook.T.DEPT).c(GlDaybook.T.CELL).o(GlDaybook.T.ORG));
		addTD(new TranDataMsg(GlTransform.TB).u(GlTransform.T.CREATED_BY).d(GlTransform.T.DEPT).c(GlTransform.T.CELL).o(GlTransform.T.ORG));
		addTD(new TranDataMsg(GlSubjectMap.TB).c(GlSubjectMap.T.TEMPLAT,SysTemplat.T.MNG_CELL));
		addTD(new TranDataMsg(GlJournal.TB).c(GlJournal.T.CELL).o(GlJournal.T.ORG));
		addTD(new TranDataMsg(GlDailyLedger.TB).c(GlDailyLedger.T.CELL).o(GlDailyLedger.T.ORG));
		addTD(new TranDataMsg(GlNote.TB)); //TODO 有问题
		addTD(new TranDataMsg(GlReport.TB));
		addTD(new TranDataMsg(GlReportAsset.TB).o(GlReportAsset.T.ORG));
		addTD(new TranDataMsg(GlReportProfit.TB).o(GlReportProfit.T.ORG));
		addTD(new TranDataMsg(GlCarryOver.TB));
		addTD(new TranDataMsg(GlGoods.TB).c(GlGoods.T.JOURNAL,GlJournal.T.CELL).o(GlGoods.T.JOURNAL,GlJournal.T.ORG));
	}
	
	@Override
	public SysModule initModule() {
		return iuModule(Gl.TB, "gl-财务管理-80");
	}
	
	/**
	 * 初始化，在运行期间仅执行一次
	 */
	public void initOnlyOne() { // 初始化方法，在每次启动时执行一次
		super.initOnlyOne();
	}

	public enum T implements IEnumFld {
		//		SUBJECT(TB.crtOutKey(GlSubject.class, "subject", "科目")),
		//		JL_AMT(TB.crtOutKey(GlJlAmt.class, "jlAmt", "普通分户账")),
		//		DAYBOOK(TB.crtOutKey(GlDaybook.class, "daybook", "流水账")),
		//		DAYBOOK_LINE(TB.crtOutKey(GlDaybookLine.class, "daybookLine", "流水账行")),
		//		RATE_TYPE(TB.crtOutKey(GlRateType.class, "rateType", "利率类别")),

		DIRECT(TB.crt(ODirect.CR)), 
		DIRECT_SF(TB.crt(ODirectSf.S)), 
		IN_FLAG(SYS.YN, "表内标志"), 
		NOTE_ONE2ONE(GlNote
		    .fldOneToOne()), NOTE(GlNote.fldOutKey()), ;
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
			_fld = TB.add(fld, this);
		}

		public Fld getFld() {
			return _fld;
		}
	}

	static { //在此可以加一些对FLD进行特殊设定的代码
	//		T.LOGIN.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}

	public enum ODirect implements IEnumOpt {
		DR(1, "借方"), CR(2, "贷方");
		public static String NAME = "借贷标志";
		public static final ODirect DEFAULT = DR; // 定义缺省值
		private EnumLine _line;

		private ODirect(int key, String name) {
			_line = new EnumLine(this, key, name);
		}

		public EnumLine getLine() {
			return _line;
		}
	}

	public enum ODirectSf implements IEnumOpt {
		S(1, "收"), F(2, "付");
		public static String NAME = "收付标志";
		public static final ODirectSf DEFAULT = S; // 定义缺省值
		private EnumLine _line;

		private ODirectSf(int key, String name) {
			_line = new EnumLine(this, key, name);
		}

		public EnumLine getLine() {
			return _line;
		}
	}

	public enum OAccJournalType implements IEnumOpt {//@formatter:off
		AMT(1,"金额"),QTY_AMT(2,"数量金额"),QTY_AMT_BATCH(3,"分批次数量金额");
		public static final String NAME="明细账金额类型";
		public static final OAccJournalType DEFAULT = AMT; // 定义缺省值
		private EnumLine _line;
		private OAccJournalType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OTallyFlag implements IEnumOpt {//@formatter:off
		TOTAL(1,"每日汇总成一笔"),BILL(2,"按单据汇总"),ONE(9,"逐笔")	;
		public static final String NAME="记明细汇总标志";
		public static final OTallyFlag DEFAULT = ONE; // 定义缺省值
		private EnumLine _line;
		private OTallyFlag(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OJlState implements IEnumOpt {//@formatter:off
		NORMAL(1,"正常"),CTRL(2,"余额控制"),CLOSE(9,"已销户");	
		public static final String NAME="状态";
		public static final OJlState DEFAULT = NORMAL; // 定义缺省值
		private EnumLine _line;
		private OJlState(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OInterestAccrual implements IEnumOpt {//@formatter:off
		NO(0,"不计息"),MONTH(1,"按月计息");	
		public static final String NAME="计息标志";
		public static final OInterestAccrual DEFAULT = NO; // 定义缺省值
		private EnumLine _line;
		private OInterestAccrual(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OFrostFlag implements IEnumOpt {//@formatter:off
		NORMAL(1,"正常"),NO_DR(2,"禁止借(付)方发生"),NO_CR(3,"禁止贷(收)方发生"),NO(4,"禁止借贷双方发生");
		public static final String NAME="冻结标志";
		public static final OFrostFlag DEFAULT = NORMAL; // 定义缺省值
		private EnumLine _line;
		private OFrostFlag(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	// 科目别名------------------------------------------------------------------------
	//	public static final String ACCOUNT_ALIAS_XJ = "gg.xj";
	//	public static final Opt ACCOUNT_ALIAS = new Opt(LOG, "accountAlias", "科目别名")
	//			.add(ACCOUNT_ALIAS_XJ, "现金");
	//
	//	// 科目关联------------------------------------------------------------------------
	//	public static final String ACCOUNT_MAP_LX = "gg.lx";
	//	public static final String ACCOUNT_MAP_ZJ = "gg.zj";
	//	public static final String ACCOUNT_MAP_SJ = "gg.sj";
	//	public static final Opt ACCOUNT_MAP = new Opt(LOG, "accountMap", "科目关联")
	//			.add(ACCOUNT_MAP_LX, "利息").add(ACCOUNT_MAP_ZJ, "折旧")
	//			.add(ACCOUNT_MAP_SJ, "税金");
	//
	//	// ------------------------------------------------------------------------
	//	public static final byte WRITEOFF_NO = 1;
	//	public static final byte WRITEOFF_NEW = 2;
	//	public static final byte WRITEOFF_WRITE = 3;
	//	public static final byte WRITEOFF_AUTO = 4;
	//	public static final Opt WRITEOFF_FLAG = new Opt(LOG, "writeoffFlag", "销账标志")
	//			.add(WRITEOFF_NO, "不销账").add(WRITEOFF_NEW, "新建销账计划")
	//			.add(WRITEOFF_WRITE, "核销").add(WRITEOFF_AUTO, "自动核销");
	//
	//	// ------------------------------------------------------------------------
	//	public static final byte BOOK_TYPE_ORG = 1;
	//	public static final byte BOOK_TYPE_DEPT = 2;
	//	public static final Opt BOOK_TYPE = new Opt(LOG, "bookType", "类型").add(
	//			BOOK_TYPE_ORG, "机构").add(BOOK_TYPE_DEPT, "部门");
	//	// ------------------------------------------------------------------------
	//	public static final byte DAY_STATUS_NO = 1;
	//	public static final byte DAY_STATUS_DOC = 2;
	//	public static final byte DAY_STATUS_UNDO = 9;
	//	public static final Opt DAY_STATUS = new Opt(LOG, "dayStatus", "状态")
	//			.add(DAY_STATUS_NO, "未复核").add(DAY_STATUS_DOC, "已入账")
	//			.add(DAY_STATUS_UNDO, "被抹账");
	//	// ------------------------------------------------------------------------
	//	public static final byte CLEAR_FLAG_NO = 1;
	//	public static final byte CLEAR_FLAG_UNDO = 2;
	//	public static final byte CLEAR_FLAG_DO = 3;
	//	public static final Opt CLEAR_FLAG = new Opt(LOG, "clearFlag", "清算标志")
	//			.add(CLEAR_FLAG_NO, "不需清算").add(CLEAR_FLAG_UNDO, "未清算")
	//			.add(CLEAR_FLAG_DO, "已清算");
	//	// ------------------------------------------------------------------------
	//	public static final byte ORG_STATUS_WORK = 1;
	//	public static final byte ORG_STATUS_DOING = 2;
	//	public static final byte ORG_STATUS_OK = 3;
	//	public static final Opt ORG_STATUS = new Opt(LOG, "orgStatus", "机构状态")
	//			.add(ORG_STATUS_WORK, "营业中").add(ORG_STATUS_DOING, "日终处理中")
	//			.add(ORG_STATUS_OK, "日终完成");

	public enum OWriteoffFlag implements IEnumOpt {//@formatter:off
		WRITEOFF_NO(1,"不销账"),WRITEOFF_NEW(2,"新建销账计划"),WRITEOFF_WRITE(3,"核销");
		public static final String NAME="销账标志";
		public static final OWriteoffFlag DEFAULT = WRITEOFF_NO; // 定义缺省值
		private EnumLine _line;
		private OWriteoffFlag(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OUseScope implements IEnumOpt {//@formatter:off
		ORG(1,"机构"),CELL(2,"核算单元"),ALL(3,"机构与核算单元都可用"),
		CLEARING_CENTER(11,"清算中心");
		public static final String NAME="应用范围";
		public static final OUseScope DEFAULT = ALL; // 定义缺省值
		private EnumLine _line;
		private OUseScope(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OAccType implements IEnumOptObj<Class> {//@formatter:off
		ONE(1,"单账户科目",null),MUCH(2,"多账户科目",null),
		ORG(61,"机构",SysOrg.class),CELL(62,"核算单元",SysCell.class), 
		DEPT(63,"部门",SysDept.class),PROJECT(65,"项目",SysProject.class),
		EM(66,"职员",SysEm.class),USER(67,"用户",SysUser.class),
		CUSTOM(68,"客户",SysCustom.class),	SUPPLIER(69,"供应商",SysSupplier.class);
		public static final String NAME="账户类型";
		public static final OAccType DEFAULT = ONE; // 定义缺省值
		private EnumLine _line;
		private Class _obj;
		private OAccType(int key, String name,Class obj) {
			_line=new EnumLine(this,key,name);_obj=obj;	}
		public EnumLine getLine(){return _line;	}
		public Class getObj(){return _obj;	}
	} //@formatter:on

	public enum OSType implements IEnumOpt {//@formatter:off
		ZC(1,"资产"), FZ(2,"负债"),QY(3,"权益"),CB(4,"成本"),SY(5,"损益"),BW(9,"表外");
		public static final String NAME="科目类型";
		public static final OSType DEFAULT = ZC; // 定义缺省值
		private EnumLine _line;
		private OSType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}	
	public enum OTableType implements IEnumOpt {//@formatter:off
		ZC((byte)1,"资产"), FZ((byte)2,"负债"),QY((byte)3,"所有者权益"),lR((byte)4,"利润表");
		public static final String NAME="表类型";
		public static final OTableType DEFAULT = ZC; // 定义缺省值
		private EnumLine _line;
		private OTableType(byte key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}	//@formatter:on
	
	public enum OValueType implements IEnumOpt {//@formatter:off
		YE((byte)1,"余额"), XJ((byte)2,"小计"),ZJ((byte)3,"总计"),W((byte)4,"无");
		public static final String NAME="值类型";
		public static final OValueType DEFAULT = YE; // 定义缺省值
		private EnumLine _line;
		private OValueType(byte key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}	//@formatter:on
	
	public enum OSymbolType implements IEnumOpt {//@formatter:off
		ADD((byte)1,"加"), SUB((byte)2,"减");
		public static final String NAME="加减类型";
		public static final OSymbolType DEFAULT = ADD; // 定义缺省值
		private EnumLine _line;
		private OSymbolType(byte key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}	//@formatter:on
	
	public enum OSubjectKind implements IEnumOptObj<OSType> {
		XJ(11,"现金",OSType.ZC),YHCK(12,"银行存款",OSType.ZC),LDZC(13,"流动资产",OSType.ZC),CH(14,"存货",OSType.ZC),
		FLDZC(15,"非流动资产",OSType.ZC),GDZC(16,"固定资产",OSType.ZC),LJZJ(17,"累计折旧",OSType.ZC),
		LDFZ(21,"流动负债",OSType.FZ),ZB(31,"资本",OSType.QY),LJYYBNLR(32,"累计盈余--本年利润",OSType.QY),
		LJYY(33,"累计盈余",OSType.QY),SCCB(41,"生产成本",OSType.CB),
		SR(51,"收入",OSType.SY),QTSR(52,"其他收入",OSType.SY),
		XSCB(53,"销售成本",OSType.SY),QTFY(54,"其他费用",OSType.SY),
		FY(55,"费用",OSType.SY),BW(91,"表外",OSType.BW);
		public static final String NAME="分类";
		public static final OSubjectKind DEFAULT = XJ; // 定义缺省值
		private EnumLine _line;
		private OSType _obj;
		private OSubjectKind(int key, String name,OSType obj) {
			_line=new EnumLine(this,key,name);_obj=obj;	}
		public EnumLine getLine(){return _line;	}
		public OSType getObj(){return _obj;	}
		//@formatter:on
	}

	public enum OAccSource implements IEnumOpt {//@formatter:off
		TARGET(2,"指定目标科目取账户"), MAP(3,"根据源科目取关联科目")	;
		public static final String NAME="账号来源";
		public static final OAccSource DEFAULT = TARGET; // 定义缺省值
		private EnumLine _line;
		private OAccSource(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum ODirect3 implements IEnumOpt {//@formatter:off
		DR(1, "借方"), CR(2, "贷方"), VALUE(3,"取参数的借贷标志");
		public static final String NAME="借贷标志";
		public static final ODirect3 DEFAULT = VALUE; // 定义缺省值
		private EnumLine _line;
		private ODirect3(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OState implements IEnumOpt {//@formatter:off
		NO(1,"未复核"),DOC(2,"已入账"),CANCEL(3,"被抹账");
		public static final String NAME="状态";
		public static final OState DEFAULT = NO; // 定义缺省值
		private EnumLine _line;
		private OState(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OTallyState implements IEnumOpt {//@formatter:off
		INIT(0,"初始"),WAIT_TOTAL(12,"待被汇总入账"),DONE_ONE(21,"已入账-单笔"),
		DONE_TOTAL_BY(22,"已被汇总入账"),
		DONE_TOTAL(23,"汇总入账");
		public static final String NAME="入账标志";
		public static final OTallyState DEFAULT = INIT; // 定义缺省值
		private EnumLine _line;
		private OTallyState(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on
	
	public enum OWriteoffState implements IEnumOpt {//@formatter:off
		NO(1,"未销账"),PART(2,"部分销账"),CLEAR(9,"已销账");
		public static final String NAME="状态";
		public static final OWriteoffState DEFAULT = NO; // 定义缺省值
		private EnumLine _line;
		private OWriteoffState(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on
	
	public enum OTransformType implements IEnumOpt {//@formatter:off
		NORMAL(1,"普通"),CARRY(2,"损益结转"),SUM(3,"汇总入账");
		public static final String NAME="类型";
		public static final OTransformType DEFAULT = NORMAL; // 定义缺省值
		private EnumLine _line;
		private OTransformType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on
}
