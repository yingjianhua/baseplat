package irille.core.sys;

import irille.core.prv.PrvRoleAct;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlReport;
import irille.gl.gl.GlSubject;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.IBeanLong;
import irille.pub.bean.IBill;
import irille.pub.bean.IForm;
import irille.pub.bean.PackageBase;
import irille.pub.svr.Env;
import irille.pub.svr.Env.SysConf;
import irille.pub.svr.IEnumConf;
import irille.pub.svr.StartInitServlet;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.Fld.OOutType;
import irille.pub.tb.FldByte;
import irille.pub.tb.FldDate;
import irille.pub.tb.FldDec;
import irille.pub.tb.FldImg;
import irille.pub.tb.FldInt;
import irille.pub.tb.FldJSONObject;
import irille.pub.tb.FldLanguage;
import irille.pub.tb.FldLong;
import irille.pub.tb.FldShort;
import irille.pub.tb.FldStr;
import irille.pub.tb.FldText;
import irille.pub.tb.FldTime;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.IEnumOpt.IEnumBoolean;
import irille.pub.tb.IEnumOptObj;
import irille.pub.tb.OptCust;
import irille.pub.tb.Tb;
import irille.pub.tb.TbBase;
import irille.pub.valid.VNumber;

public class Sys extends PackageBase {
	private static final Log LOG = new Log(Sys.class);
	public static final Sys INST = new Sys();

	public static TbBase TB = new TbBase<Tb>(Sys.class, "系统模块"); // 定义公共的Fld对象用

	private Sys() {
		//系统初始化，必须建好重量与体积单位
		//单位界面，设置好默认的转换重、体积相关属性
	}

	public static void main(String[] args) {
		StartInitServlet.initBeanLoad();
		PrvRoleAct.TB.getCode();
		ConfPackage.INST.install();
	}

	/**
	 * 初始化，在运行期间仅执行一次
	 */
	public void initOnlyOne() { // 初始化方法，在每次启动时执行一次
		super.initOnlyOne();
	}

	/**
	 * 初始化表信息，指定的表ID号能变动，因为一些数据中会ID信息
	 */
	@Override
	public void initTbMsg() { //初始化表信息
		addTb(1, SysOrg.class);
		addTb(2, SysDept.class);
		addTb(3, SysEm.class);
		addTb(4, SysUser.class);
		addTb(5, SysCell.class);
		addTb(6, SysTemplat.class);
		addTb(7, SysProject.class);
		addTb(9, SysGrp.class);
		addTb(10, SysPost.class);
		addTb(11, SysMenu.class);
		addTb(12, SysMenuAct.class);
		addTb(13, SysPersonLink.class);
		addTb(14, SysSupplier.class);
		addTb(15, SysCustom.class);
		addTb(16, SysSeq.class);
		addTb(17, SysTable.class);
		addTb(18, SysCtype.class);
		addTb(19, SysAccessory.class);
	}

	public void initTranData() { //初始化PrvTranData表数据
		addTD(new TranDataMsg(SysOrg.TB).o(SysOrg.T.PKEY));
		addTD(new TranDataMsg(SysDept.TB).d(SysDept.T.PKEY).o(SysDept.T.ORG));
		addTD(new TranDataMsg(SysEm.TB).u(SysEm.T.USER_SYS).d(SysEm.T.DEPT).c(SysEm.T.CELL).o(SysEm.T.ORG));
		addTD(new TranDataMsg(SysUser.TB).u(SysUser.T.PKEY).d(SysUser.T.DEPT).c(SysUser.T.DEPT, SysDept.T.CELL)
		    .o(SysUser.T.ORG));
		addTD(new TranDataMsg(SysCell.TB).c(SysCell.T.PKEY).o(SysCell.T.ORG));
		addTD(new TranDataMsg(SysTemplat.TB).c(SysTemplat.T.MNG_CELL));
		addTD(new TranDataMsg(SysSupplier.TB).u(SysSupplier.T.BUSINESS_MEMBER).d(SysSupplier.T.MNG_DEPT)
		    .o(SysSupplier.T.MNG_ORG));
		addTD(new TranDataMsg(SysCustom.TB).u(SysCustom.T.BUSINESS_MEMBER).d(SysCustom.T.MNG_DEPT).o(SysCustom.T.MNG_ORG));
		addTD(new TranDataMsg(SysSeq.TB));
		addTD(new TranDataMsg(SysCtype.TB));
	}

	@Override
	public SysModule initModule() {
		return iuModule(Sys.TB, "sys-系统管理-999");
	}

	//科目别名定义，Bill与Note会用到
	public static enum SubjectAlias implements ISubjectAlias {//@formatter:off
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

	//用户选项定义
	public static enum OptCustEnum implements IOptCustEnum {//@formatter:off
		  SYS_POST("sysPost", "职务");
			private OptCust _opt;
			private OptCustEnum(String code,String name) { _opt= new OptCust(code,name);  } 
			public OptCust getOpt(){return _opt;}
	 }			//@formatter:on

	@Override
	public IOptCustEnum[] getOptCusts() { //取用户选项
		return OptCustEnum.values();
	}

	//模块可配置的参数定义。一些无法在数据库中定义的参数，可以在此定义并引用，这样系统把这些参数放到配置文件中，以方便用户设置
	public static enum VarsConf implements IEnumConf {//@formatter:off
			;
			private String _name;
			private Object _obj;
			VarsConf(String name,Object obj) { _name=name; _obj=obj;}
			public String getName() { return _name; }
			public Object getDefaultValue() {return _obj;	}
		}//@formatter:on

	@Override
	public IEnumConf[] getVarsConf() { //模块的变量
		return SysConf.values();
	}

	//临时目录定义，在使用时可以调用方法mkDir, 如目录未建，则自动建立
	public static enum TmpDirEnum implements ITmpDir {//@formatter:off
			;
			private String _name;
			private String _dir;
			TmpDirEnum(String name,String dir) { _name=name; _dir=dir;}
			public String getName() { return _name; }
			public String getDir() {return _dir;	}
			@Override
			public void mkDir() { mkTmpDirOnlyOne(this); } //建立目录，可重复调用，但每次启动后，系统只会在第一次调用时新建目录！
		}//@formatter:on

	@Override
	public ITmpDir[] getTmpDirs() { //取临时目录
		return TmpDirEnum.values();
	}

	public enum T implements IEnumFld {//@formatter:off
		USER_SYS(SysUser.class,"用户",null),
		PERSON(SysPerson.class,"联系人",null),
		PERSONLINK(SysPersonLink.class,"供应商联系人",null),
		CASHIER(USER_SYS,"出纳"),
		RATIFY_BY_NULL(USER_SYS,"审批员",true),
		APPR_BY_NULL(USER_SYS,"审核员",true),
		CREATED_BY(USER_SYS,"建档员"),
		UPDATED_BY(USER_SYS,"更新员"),
		TALLY_BY(USER_SYS,"记账员", true),
		CHECK_BY__NULL(USER_SYS,"复核员",true),
		DEPT(SysDept.class,"部门", null),
		EM(SysEm.class,"职员", null),
		BUSINESS_MEMBER(USER_SYS,"业务代表"),
		ORG(SysOrg.class,"机构",OOutType.COMBO),
		TABLE_ID(SysTable.class,"表",OOutType.COMBO),
		MANAGER_NULL(SysUser.class,"负责人",null,true),
		MENU(SysMenu.class,"菜单",OOutType.COMBO),
		ACT(SysTableAct.class,"功能",OOutType.COMBO),
		CELL(SysCell.class,"核算单元",OOutType.COMBO),
		TEMPLAT(SysTemplat.class,"科目模板",OOutType.COMBO),
		CUST(SysCustom.class,"客户",null),
		SUPPLIER(SysSupplier.class,"供应商",OOutType.COMBO),
		COM(SysCom.class,"单位基本信息",OOutType.COMBO),
		
		YN(TB.crt(OYn.YES)), 
		DELETED(TB.crt(ODeleted.YES)), 
		NY(TB.crt(OYn.NO)), 
		ENABLED(TB.crt(OEnabled.TRUE)), 
		SEX(TB.crt(OSex.UNKNOW)), 
		MERRY(TB.crt(OMerry.UNKNOW)), 
		STATUS(TB.crt(OBillStatus.INIT)), 
		CARD_TYPE__PERSON(TB.crt(OCardPersonType.NONE)),
		DISP_SCOPE(TB.crt(ODispScope4.GRP)),
		//基本类型
		BYTE(new FldByte<FldByte>("byte","BYTE")),
		BYTE_PLUS_OR_ZERO(new FldByte<FldByte>("byte","Byte正数或零").setValids(VNumber.PLUS_OR_ZERO)),//正数或零
		SHORT(new FldShort<FldShort>("short","SHORT")),
		SHORT_PLUS_OR_ZERO(new FldShort<FldShort>("short","Short正数或零").setValids(VNumber.PLUS_OR_ZERO)),//正数或零
		INT(new FldInt<FldInt>("int","INT")),
		INT_PLUS_OR_ZERO(new FldInt<FldInt>("int","Int正数或零").setValids(VNumber.PLUS_OR_ZERO)),//正数或零
		LONG(new FldLong<FldLong>("long","Long")),
		LONG_PLUS_OR_ZERO(new FldLong<FldLong>("long","Long正数或零").setValids(VNumber.PLUS_OR_ZERO)),//正数或零
		DATE(new FldDate<FldDate>("date","日期",false)), //不可为空的日期
		DATE__NULL(DATE,true), //可以为空的日期
		TIME((new FldTime<FldTime>("time","时间",false)).setDispDate(false)), //不可为空的时间, 不显示日期
		TIME__NULL(TIME,true), //可以为空的时间，不显示日期
		DATE_TIME__NULL(new FldTime<FldTime>("updatedDateTime","日期时间", true)),
		DATE_TIME(new FldTime<FldTime>("updatedDateTime","日期时间")
				.setDefaultValue("Env.getTranBeginTime()")), //不可为空的时间
	  AMT(new FldDec("amt", "金额", Env.getConfParaInt(SysConf.AMT_LEN),
	  		Env.getConfParaInt(SysConf.AMT_DIGITS)).setViewDecJeStd()),
		AMT_COST(AMT, "费用合计"),  //需要改为EXPENSES
		COST(AMT,"成本"),
		EXPENSES(AMT, "费用"),
		EXPENSES_SALE(AMT, "销售费用"),
		COST_PUR(COST, "采购成本"),
		TAX_OUTPUT(AMT, "销项税"),
		TAX_INPUT(TAX_OUTPUT, "进项税"),
		TAX_INPUT_INSIDE(TAX_OUTPUT, "内部进项税"),
		TAX_INPUT_OUTSIDE(TAX_OUTPUT, "外部进项税"),
	  QTY(new FldDec("qty", "数量", Env.getConfParaInt(SysConf.QTY_LEN),
	  		Env.getConfParaInt(SysConf.QTY_DIGITS)).setViewDecJeStd()),
	  PRICE(new FldDec("price", "单价", Env.getConfParaInt(SysConf.PRICE_LEN),
	  		Env.getConfParaInt(SysConf.PRICE_DIGITS)).setViewDecJeStd()),
	 PRICE_NULL(new FldDec("price", "单价", Env.getConfParaInt(SysConf.PRICE_LEN),
	  			Env.getConfParaInt(SysConf.PRICE_DIGITS), true).setViewDecJeStd()),
  
	  STR__6(new FldStr<FldStr>("str","字符6",6,false)),
		STR__6_NULL(STR__6,true), //可以为空
		STR__8(STR__6,"字符8",8),
		STR__8_NULL(STR__8,true), //可以为空
		STR__10(new FldStr<FldStr>("str","字符10",10,false)),
		STR__10_NULL(STR__10,true), //可以为空
		STR__20(new FldStr<FldStr>("str","字符20",20,false)),
		STR__20_NULL(STR__20,true), //可以为空
		STR__30(new FldStr<FldStr>("str","字符30",30,false)),
		STR__30_NULL(STR__30,true), //可以为空
		STR__40(STR__20,"字符40",40),
		STR__40_NULL(STR__40,true), //可以为空
		STR__50(new FldStr<FldStr>("str","字符50",50,false)),
		STR__50_NULL(STR__50,true), //可以为空
		STR__100(STR__20,"字符100",100),
		STR__100_NULL(STR__100,true), //可以为空
		STR__200(STR__20,"字符200",200),
		STR__200_NULL(STR__200,true), //可以为空
		STR__500(STR__20, "字符500", 500),
		STR__500_NULL(STR__500,true),
		STR__1000(STR__20, "字符1000", 1000),
		STR__1000_NULL(STR__1000,true),
		STR__2000(STR__20, "字符2000", 2000),
		STR__2000_NULL(STR__2000,true),
		STR__20000(STR__20, "字符20000", 20000),
		STR__20000_NULL(STR__20000,true),
		TEXT__200(new FldText<FldText>("text","字符200", 200, false)),
		TEXT__200_NULL(TEXT__200, "字符200", true),
		TEXT__500_NULL(TEXT__200, "字符500", true),
		TEXT__1000_NULL(TEXT__200, "字符1000", true),
		TEXT__2000_NULL(TEXT__200, "字符2000", true),
		TEXT__20000_NULL(TEXT__200, "字符20000", true),
		
		JSON(new FldJSONObject<FldJSONObject>("json" , "JSON")),
		MUILTI_LANGUAGE(new FldLanguage<FldLanguage>("language" , "语言")),
		MUILTI_LANGUAGE_NULL(MUILTI_LANGUAGE, true),
		
		IMG__200(new FldImg<FldImg>("image","图片", 200, false)),
		IMG__200_NULL(IMG__200, "图片", true),
		IMG__400(new FldImg<FldImg>("image","图片", 400, false)),
		IMG__400_NULL(IMG__400, "图片", true),
		IMG_MULTI__600(new FldImg<FldImg>("image","图片", 600, false)),
		IMG_MULTI__600_NULL(IMG_MULTI__600, "图片", true),
		IMG_MULTI__800(new FldImg<FldImg>("image","图片", 800, false)),
		IMG_MULTI__800_NULL(IMG_MULTI__800, "图片", true),
		IMG_MULTI__1000(new FldImg<FldImg>("image","图片", 1000, false)), 
		IMG_MULTI__1000_NULL(IMG_MULTI__1000, "图片", true),
		IMG_MULTI__2000(new FldImg<FldImg>("image","图片", 2000, false)),
		IMG_MULTI__2000_NULL(IMG_MULTI__2000, "图片", true),
		IMG_MULTI__5000(new FldImg<FldImg>("image","图片", 5000, false)),
		IMG_MULTI__5000_NULL(IMG_MULTI__5000, "图片", true),
		
		CNT(INT,"笔数"),
		DR_QTY(INT,"借方发生笔数"),
		CR_QTY(INT,"贷方发生笔数"),
		BALANCE(AMT,"余额"),
		DR_BALANCE(AMT,"借方余额"),
		CR_BALANCE(AMT,"贷方余额"),
		DR_AMT(AMT,"借方发生额"),
		CR_AMT(AMT,"贷方发生额"),
		SORT__SHORT(SHORT,"排序号"),
		SORT__INT(INT,"排序号"),
		YEAR(SHORT,"年份"),
		YEAR_MONTH(INT,"年月"),

		RATE(new FldDec("rate", "利率",8,4)),
		RATE_NULL(new FldDec("rate", "利率",8,4, true)),
		HAPPEN_DATE(new FldDate<FldDate>("date","发生日期")
				.setDefaultValue("Env.getWorkDate()")), //不可为空的日期
		BIRTHDAY_NULL(DATE__NULL,"出生日期"),
		WORK_DATE(DATE,"工作日期"),
		UPDATED_DATE(HAPPEN_DATE,"更新日期"),
		CREATED_DATE(HAPPEN_DATE,"建档日期"),
		DUE_DATE(HAPPEN_DATE,"到期日期"),
		TALLY_DATE__NULL(HAPPEN_DATE,"记账日期",true),
		APPR_DATE__NULL(HAPPEN_DATE,"审核日期",true),
		BUSINESS_DATE(DATE,"业务日期"),
		BUSINESS_DATE_TIME(DATE_TIME,"业务时间"),
		BUSINESS_DATE_TIME__NULL(DATE_TIME,"业务时间",true),
		UPDATED_DATE_TIME(DATE_TIME,"更新时间"),
		CREATED_DATE_TIME(UPDATED_DATE_TIME,"建档时间"),
		RATIFY_DATE_TIME__NULL(UPDATED_DATE_TIME,"审批时间",true),
		APPR_DATE_TIME__NULL(UPDATED_DATE_TIME,"审核时间",true),
		
		CODE__20(STR__20,"代码"), //2个字符"_"表示Fld.getCode()的内容不包括"__"之后的部分
		CODE__40(STR__40,"代码"),
		CODE__40_NULL(STR__40_NULL,"代码"),
		CODE__100_NULL(STR__100_NULL,"代码"),
		CODE__100(STR__100,"代码"),
		CODE_THIS__10(STR__10,"本级编号"),
		NAME__PERSON_40(STR__40,"姓名"),
		NAME__40(STR__40,"名称"),
		NAME__40_NULL(STR__40_NULL,"名称"),
		NAME__PERSON_100(STR__40,"姓名"),
		NAME__100(STR__100,"名称"),
		NAME__100_NULL(STR__100_NULL,"名称"),
		COMPANY_NAME__NULL(STR__100_NULL,"公司名称"),
		DEPT_NAME__NULL(STR__100_NULL,"所在部门"),
		POST__NULL(STR__100_NULL,"职务"),
		NICKNAME__20_NULL(STR__20_NULL,"昵称"),
		NICKNAME__40_NULL(STR__40_NULL,"昵称"),
		SHORT_NAME__20_NULL(STR__20_NULL,"简称"),
		ADDR__200_NULL(STR__200_NULL,"地址"),
		ADD_OFFICE__200_NULL(STR__200_NULL,"单位地址"),
		ADD_HOME__200_NULL(STR__200_NULL,"家庭地址"),
		REM__200_NULL(STR__200_NULL,"备注"),
		REM_TEXT__200_NULL(TEXT__200_NULL,"备注"),
		REM_TEXT__500_NULL(TEXT__500_NULL,"备注"),
		REM_TEXT__1000_NULL(TEXT__1000_NULL,"备注"),
		REM_TEXT__2000_NULL(TEXT__2000_NULL,"备注"),
		DESCRIP__200_NULL(REM__200_NULL,"描述"),
		SUMMARY__200_NULL(STR__200_NULL,"摘要"),
		SUMMARY__100_NULL(STR__100_NULL,"摘要"),
		PACK_DEMAND(STR__200, "包装要求"),
		
		ROW_VERSION(SHORT, "版本"),
		FORM_PKEY(LONG,"单据编号"),
		FORM_NUM(STR__40,"单据号"),
		FORM_NUM__NULL(FORM_NUM,true),
		DOC_NUM__NULL(STR__40_NULL,"票据号"),
		DOC_DATE__NULL(DATE__NULL,"票据日期"),
		MOBILE__NULL(STR__20_NULL,"手机号码"),
		TEL__NULL(STR__20_NULL,"电话"),
		FAX__NULL(TEL__NULL,"传真"),
		TEL_OFFICE__NULL(TEL__NULL,"办公电话"),
		TEL_HOME__NULL(TEL__NULL,"家庭电话"),
		ID_CARD__NULL(STR__20_NULL,"身份证号码"),
		CARD_NUMB__NULL(STR__20_NULL,"证件号码"),
		ZIP__COCE(STR__6_NULL,"邮编"),
		WEBSITE__NULL(STR__200_NULL,"网址"),
		URL__NULL(WEBSITE__NULL,"URL"),
		EMAIL__NULL(STR__100_NULL,"邮箱"),
		IP__NULL(STR__20_NULL,"IP地址",30),
		SHORTKEY__NULL(STR__40_NULL,"快捷键"),
		PHOTO__NULL(STR__200_NULL,"图片"), //XXX 以后要处理
		ICO__NULL(PHOTO__NULL,"图标"), 
		BELIEF__NULL(STR__20_NULL,"信仰"),
		PARTY__NULL(STR__20_NULL,"党派"),
		EDU__NULL(STR__20_NULL,"学历"),
		DEGREE__NULL(STR__20_NULL,"学位"),
		POSITIONAL_TITLE__NULL(STR__20_NULL,"职称"),
		DRIVING_LICENSE__NULL(STR__20_NULL,"驾照类别"),
		PASSWORD__NULL(STR__40_NULL,"密码"),
		PASSWORD(STR__40,"密码"),

		WX(STR__100_NULL,"微信"),
		MSN(WX,"MSN"),
		QQ(WX,"QQ"),
		WB(WX,"微博"),
		CURRENCY(TB.crt(OCurrency.RMB)),
		
		FORM_IDKEY(TB.crtTbAndKey("form", "单据")),
		FORM_SRC_IDKEY(TB.crtTbAndKey("formSrc", "源单据")),
		FORM_DES_IDKEY(TB.crtTbAndKey("formDes", "目标单据")),
		FORM_CODE(CODE__40,"单据号"),
		BILL_IDKEY(TB.crtTbAndKey("bill", "凭证")),
		BILL_SRC_IDKEY(TB.crtTbAndKey("billSrc", "源凭证")),
		BILL_DES_IDKEY(TB.crtTbAndKey("billDes", "目标凭证")),
		BILL_CODE(CODE__40,"凭证号"),
		FORM(TB.crtLongTbObj("form", "单据",IForm.class)),
		FORM_MAIN(FORM,"主单据"),
		PKEY_FORM(TB.crtOneToMany(IForm.class,"form","单据")),
		PKEY_BILL(TB.crtOneToMany(IBill.class,"bill","凭证")),
		ORIG_FORM__CODE(TB.crtFormAndCode("ORIG_FORM", "源单据")),
		BILL(TB.crtLongTbObj("bill", "凭证",IBill.class)),
		NOTE(GlNote.class, "凭条",null),
		SUBJECT(GlSubject.class, "科目", null),
		JOURNAL(GlJournal.class,"分户账",null),
		REPORT(GlReport.class, "报表设置",null),
		TABLE_LONG(TB.crtLongTbObj("tableLong", "主表外键")),
		BOX_GOODS(TB.crtLongTbObj("boxBoods", "物品",IBeanLong.class)),
		BEAN_LONG(TB.crtLongTbObj("beanLong", "对象",IBeanLong.class)),  //数据库中的对象，长整型的唯一值
    ; 
		static {
			WORK_DATE.getFld().setDefaultValue("Env.getWorkDate()");
			CREATED_BY.getFld().setDefaultValue("Idu.getUser().getPkey()");
		}
		private Fld _fld;
		private T(Class clazz,String name, OOutType type,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);
			_fld.setType(type);
			}
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
//		PASSWORD__NULL. XXX 设置密码说明
//		T.NY._fld.setDefaultValue(OYn.NO._line.getKey()); //默认值为NO
//		T.USER.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}

	//@formatter:on

	/**
	 * 选项类别
	 */

	public enum OYn implements IEnumOpt, IEnumBoolean {
		//@formatter:off
		YES(1, "是"), NO(0, "否");
		public static String NAME = "是否";
		public static OYn DEFAULT = YES; // 定义缺省值
		private EnumLine _line;
		private OYn(int key, String name) {_line = new EnumLine(this, key, name);	}
		public EnumLine getLine() {	return _line;	}
		//@formatter:on
	}

	public enum OBillFlag implements IEnumOpt, IEnumBoolean {
		//@formatter:off
		YES(1, "开票"), NO(0, "不开票"), WAIT(3, "待定");
		public static String NAME = "开票标准";
		public static OBillFlag DEFAULT = YES; // 定义缺省值
		private EnumLine _line;
		private OBillFlag(int key, String name) {_line = new EnumLine(this, key, name);	}
		public EnumLine getLine() {	return _line;	}
		//@formatter:on
	}

	public enum ODeleted implements IEnumOpt, IEnumBoolean {
		//@formatter:off
		YES(OYn.YES),
		NO(OYn.NO);
		public static String NAME="状态";
		public static ODeleted DEFAULT = NO; // 定义缺省值
		private EnumLine _line;
		private ODeleted(IEnumOpt obj) {_line=new EnumLine(this,obj);	}
		public EnumLine getLine(){return _line;	}
		//@formatter:on
	}

	public enum OEnabled implements IEnumOpt, IEnumBoolean {
		//@formatter:off
		 TRUE(1, "启用"),FALSE(0, "停用");
		public static String NAME = "启用标志";
		public static OEnabled DEFAULT = TRUE; // 定义缺省值
		private EnumLine _line;
		private OEnabled(int key, String name)  {_line = new EnumLine(this, key, name);	}
		public EnumLine getLine() {	return _line;	}
		//@formatter:on
	}

	public enum OComPersonFlag implements IEnumOpt {
		//@formatter:off
		COM(1, "单位"), PERSON(2, "个人");
		public static String NAME = "性质";
		public static OComPersonFlag DEFAULT = COM; // 定义缺省值
		private EnumLine _line;
		private OComPersonFlag(int key, String name)  {_line = new EnumLine(this, key, name);	}
		public EnumLine getLine() {	return _line;	}
		//@formatter:on
	}

	public enum ODispScope4 implements IEnumOptObj<Class> {
		//@formatter:off
	GRP(1,"集团级",SysGrp.class),   
	ORG(2,"机构",SysOrg.class),
	DEPT(3,"部门级",SysDept.class),
	USER(4,"用户",SysUser.class);
		;
		public static String NAME="可视范围";
		public static ODispScope4 DEFAULT = GRP; // 定义缺省值
		private EnumLine _line;
		private Class _obj;
		private ODispScope4(int key, String name,Class obj) {
			_line=new EnumLine(this,key,name);_obj=obj;	}
		public EnumLine getLine(){return _line;	}
		public Class getObj(){return _obj;	}
		//@formatter:on
	}

	public enum ODispScope2 implements IEnumOptObj<Class> {
		//@formatter:off
	GRP(1,"集团级",null),   
	ORG(2,"机构",SysOrg.class),
		;
		public static String NAME="可视范围";
		public static ODispScope2 DEFAULT = GRP; // 定义缺省值
		private EnumLine _line;
		private Class _obj;
		private ODispScope2(int key, String name,Class obj) {
			_line=new EnumLine(this,key,name);_obj=obj;	}
		public EnumLine getLine(){return _line;	}
		public Class getObj(){return _obj;	}
		//@formatter:on
	}

	public enum ORangeType implements IEnumOptObj<Class> {
		//@formatter:off
	GRP(1,"集团级",null),   
	ORG_ALL(11,"上下级机构",SysOrg.class),
	ORG_DOWN(12,"及下级机构",SysOrg.class),
	ORG_UP(13,"及上级机构",SysOrg.class),
	ORG(14,"本机构",SysOrg.class),
	CELL_ALL(21,"上下级核算单元",SysCell.class),
	CELL_DOWN(22,"及下级核算单元",SysCell.class),
	CELL_UP(23,"及上级核算单元",SysCell.class),
	CELL(24,"本核算单元",SysCell.class),
		;
		public static String NAME="可视范围";
		public static ORangeType DEFAULT = GRP; // 定义缺省值
		private EnumLine _line;
		private Class _obj;
		private ORangeType(int key, String name,Class obj) {
			_line=new EnumLine(this,key,name);_obj=obj;	}
		public EnumLine getLine(){return _line;	}
		public Class getObj(){return _obj;	}
		//@formatter:on
	}

	public enum OCardPersonType implements IEnumOpt {//@formatter:off
		NONE(0,"无"),
		ID_CARD(1,"身份证"),
		PASSPORT(2,"护照"),
		MILITARY(3,"军人证"),
		DRIVER(4,"驾驶证");
		public static final String NAME="证件类型";
		public static final OCardPersonType DEFAULT = ID_CARD; // 定义缺省值
		private EnumLine _line;
		private OCardPersonType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OSex implements IEnumOpt {
		//@formatter:off
		UNKNOW(0,"未知"),
		MALE(1,"男"),
		FEMALE(2,"女");
		public static String NAME="性别";
		public static OSex DEFAULT = UNKNOW; // 定义缺省值
		private EnumLine _line;
		private OSex(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
		//@formatter:on
	}

	//
	// // 婚姻状况
	// public static final String MER_SINGLE = "1"; // 未婚
	// public static final String MER_MARRIED = "2"; // 已婚
	// public static final String MER_DIVORCED = "3"; // 离异
	// public static final String MER_WIDOWED = "4"; // 丧偶
	// public static final Opt MARRY = new Opt(LOG, "marry", "婚姻状况")
	// .add(MER_SINGLE, "未婚").add(MER_MARRIED, "已婚").add(MER_DIVORCED, "离异")
	// .add(MER_WIDOWED, "丧偶");

	public enum OMerry implements IEnumOpt {
		//@formatter:off
		UNKNOW(0,"未知"),
		SINGLE(1,"未婚"),
		MARRIED(2,"已婚"),
		DIVORCED(3,"离异"),
		WIDOWED(4,"丧偶");
		public static String NAME="婚姻状况";
		public static OMerry DEFAULT = UNKNOW; // 定义缺省值
		private EnumLine _line;
		private OMerry(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
		//@formatter:on
	}

	//
	// // -----------------------------------------------------------
	// public static final byte STATUS_CS = 11;// 初始
	// public static final byte STATUS_FHZ = 53; // 复核中
	// public static final byte STATUS_YFH = 58; // 已复核
	// public static final byte STATUS_SHZ = 63; // 审核中
	// public static final byte STATUS_YSH = 68; // 已审核
	// public static final byte STATUS_SPZ = 73;// 审批中
	// public static final byte STATUS_YSP = 78;// 已审批
	// public static final byte STATUS_KJZ = 83;// 可记账
	// public static final byte STATUS_WC = 98;// 完成
	// public static final byte STATUS_ZF = 99; // 作废
	// public static final Opt STATUS = new Opt(LOG, "status", "状态")
	// .add(STATUS_CS, "初始").add(STATUS_FHZ, "复核中").add(STATUS_YFH, "已复核")
	// .add(STATUS_SHZ, "审核中").add(STATUS_YSH, "已审核").add(STATUS_SPZ, "审批中")
	// .add(STATUS_YSP, "已审批").add(STATUS_KJZ, "可记账").add(STATUS_WC, "完成")
	// .add(STATUS_ZF, "作废");

	public enum OBillStatus implements IEnumOpt {
		//@formatter:off
		INIT(11,"初始"),
		OK(21,"已输入确认"),
		VERIFING(53,"复核中"),
		VERIFIED(58,"已复核"),
		CHECKING(63,"审核中"),
		CHECKED(68,"已审核"),
		VETTING(73,"审批中"),
		VETTED(78,"已审批"),
		INOUT(81,"已出入库"),
		TALLY_ABLE(83,"可记账"),
		DONE(98,"完成"),
		DELETED(99,"作废");
		public static String NAME="状态";
		public static OBillStatus DEFAULT = INIT; // 定义缺省值
		private EnumLine _line;
		private OBillStatus(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
		//@formatter:on
	}

	public enum OLoginState implements IEnumOpt {
		//@formatter:off
	LOGOUT(0,"未登录"),
	PC(1,"PC在线"),
	WAP(2,"WAP在线"),
	LOCKED(3,"非法密码锁定"),
	DISABLE(4,"停用"),
	NOT_ACTIVE(5,"未激活");
	public static String NAME="登录状态";
	public static OLoginState DEFAULT = LOGOUT; // 定义缺省值
	private EnumLine _line;
	private OLoginState(int key, String name) {_line=new EnumLine(this,key,name);	}
	public EnumLine getLine(){return _line;	}
	//@formatter:on
	}

	public enum OLogType implements IEnumOpt {
		//@formatter:off
	SYS(1,"系统"),
	CTRL(2,"调度");
	public static String NAME="日志类型";
	public static OLogType DEFAULT = SYS; // 定义缺省值
	private EnumLine _line;
	private OLogType(int key, String name) {_line=new EnumLine(this,key,name);	}
	public EnumLine getLine(){return _line;	}
	//@formatter:on
	}

	public enum OLogOptype implements IEnumOpt {
		//@formatter:off
	LOGIN(1,"登录")	,
	LOGOUT(2,"注销"),
	FUN(3,"功能");
	public static String NAME="操作类型";
	public static OLogOptype DEFAULT = FUN; // 定义缺省值
	private EnumLine _line;
	private OLogOptype(int key, String name) {_line=new EnumLine(this,key,name);	}
	public EnumLine getLine(){return _line;	}
	//@formatter:on
	}

	public enum OParaType implements IEnumOpt {
		//@formatter:off
			SYS(1,"系统");
			public static String NAME="参数类型";
			public static OParaType DEFAULT = SYS; // 定义缺省值
			private EnumLine _line;
			private OParaType(int key, String name) {_line=new EnumLine(this,key,name);	}
			public EnumLine getLine(){return _line;	}
			//@formatter:on
	}

	public enum OOrgState implements IEnumOpt {//@formatter:off
			DAY_END(1,"日终处理结束"),
			OPENING(2,"营业中"),
			DAY_END_DOING(3,"日终处理中...");
			public static final String NAME="机构状态";
			public static final OOrgState DEFAULT = DAY_END; // 定义缺省值
			private EnumLine _line;
			private OOrgState(int key, String name) {_line=new EnumLine(this,key,name);	}
			public EnumLine getLine(){return _line;	}
		}		//@formatter:on

	public enum OValuationMethods implements IEnumOpt {//@formatter:off
			AVERAGE(1,"核算单元库存的平均价");
			public static final String NAME="存货计价方式";
			public static final OValuationMethods DEFAULT = AVERAGE; // 定义缺省值
			private EnumLine _line;
			private OValuationMethods(int key, String name) {_line=new EnumLine(this,key,name);	}
			public EnumLine getLine(){return _line;	}
		}		//@formatter:on

	public enum OType implements IEnumOpt {//@formatter:off
			NONE(1,"连续编号"),
			YYYY(2,"4位年份(YYYY)"),
			YY(3,"2位年份(YY)"),
			YYMM(4,"2位年份与月份(YYMM)"),
			DAY(5,"按日编号"), //流水账中使用
			;
			public static final String NAME="编号类型";
			public static final OType DEFAULT = NONE; // 定义缺省值
			private EnumLine _line;
			private OType(int key, String name) {_line=new EnumLine(this,key,name);	}
			public EnumLine getLine(){return _line;	}
		}		//@formatter:on

	public enum OBufType implements IEnumOpt {
		//@formatter:off
			NO(0,"不缓冲"),
			RECORDS(1,"指定记录缓冲"),
			ALL(2,"整表缓冲");
			public static String NAME="缓冲类型";
			public static OBufType DEFAULT = NO; // 定义缺省值
			private EnumLine _line;
			private OBufType(int key, String name) {_line=new EnumLine(this,key,name);	}
			public EnumLine getLine(){return _line;	}
			//@formatter:on
	}

	public enum OUserType implements IEnumOptObj<Class> {
		//@formatter:off
			ADMIN(1,"系统用户",null),
			EM(2,"职员",SysEm.class),
			OUTER_PERSON(5,"个人客户",null),//XXX
			OUTER_COMPANY(6,"企业客户",null);//XXX
			public static String NAME="用户类型";
			public static OUserType DEFAULT = EM; // 定义缺省值
			private EnumLine _line;
			private Class _clazz;
			private OUserType(int key, String name,Class clazz) {
				_line=new EnumLine(this,key,name);_clazz=clazz;	}
			public EnumLine getLine(){return _line;	}
			public Class getObj(){return _clazz;	}
			//@formatter:on
	}

	public enum OState implements IEnumOpt {//@formatter:off
			PROBATION_PERIOD(0,"试用期"),
			NORMAL(1,"在职"),
			STOP_PAY(11,"停薪留职"),		
			RETIRE(81,"退休"),
			RESIGN(91,"辞职"),
			PROBATION_PERIOD_LEAVE(92,"试用期离职"),
			FIRE(93,"开除"),
			;
			public static final String NAME="职员状态";
			public static final OState DEFAULT = NORMAL; // 定义缺省值
			private EnumLine _line;
			private OState(int key, String name) {_line=new EnumLine(this,key,name);	}
			public EnumLine getLine(){return _line;	}
		}		//@formatter:on

	public enum OPasswordType implements IEnumOpt {//@formatter:off
			PASSWORD(1,"密码"),
			QQ(2,"QQ验证"),
			SINA(3,"新浪验证"),
			;
			public static final String NAME="密码类型";
			public static final OPasswordType DEFAULT = PASSWORD; // 定义缺省值
			private EnumLine _line;
			private OPasswordType(int key, String name) {_line=new EnumLine(this,key,name);	}
			public EnumLine getLine(){return _line;	}
		}		//@formatter:on

	public enum OCellLevel implements IEnumOptObj<Class> {
		//@formatter:off
		ORG(1,"机构",SysOrg.class),
		DEPT(2,"部门",SysDept.class),
		;
		public static String NAME="核算级别";
		public static OCellLevel DEFAULT = ORG; // 定义缺省值
		private EnumLine _line;
		private Class _clazz;
		private OCellLevel(int key, String name,Class clazz) {
			_line=new EnumLine(this,key,name);_clazz=clazz;	}
		public EnumLine getLine(){return _line;	}
		public Class getObj(){return _clazz;	}
		//@formatter:on
	}

	public enum OCurrency implements IEnumOpt {//@formatter:off
			RMB(1,"人民币"),MY(2,"美元"),OY(3,"欧元"),
			GB(4,"港币"),RY(5,"日元"),HB(6,"韩币");
			public static final String NAME="币种";
			public static OCurrency DEFAULT = RMB; // 定义缺省值
//			public static final OCurrency DEFAULT =(OCurrency) RMB.getLine()
//			.get(Env.getConfParaInt(GlConf.DEFAULT_CURRENCY)); // 取系统参数的缺省值 TODO 循环引用
			private EnumLine _line;
			private OCurrency(int key, String name) {_line=new EnumLine(this,key,name);	}
			public EnumLine getLine(){return _line;	}
		}		//@formatter:on

	public enum OTemplateType implements IEnumOpt {
		//@formatter:off
			GL(1,"财务"),PUR(3,"采购");
			public static String NAME="模板类型";
			public static OTemplateType DEFAULT = GL; // 定义缺省值
			private EnumLine _line;
			private OTemplateType(int key, String name) {_line=new EnumLine(this,key,name);	}
			public EnumLine getLine(){return _line;	}
			//@formatter:on
	}

	public enum OOptCht implements IEnumOpt {
		//@formatter:off  系统表、单据、凭证、NOTE
		INC(1,"包含"),ST(2,"开头"),EQU(3,"="),NOEQU(4,"≠"),GT(5,">"),LT(6,"<"),GE(7,"≥"),LE(8,"≤"),EMP(9,"为空"),NOEMP(10,"不为空"),BTW(11,"间于");
		public static String NAME="表类型";
		public static OOptCht DEFAULT = INC; // 定义缺省值
		private EnumLine _line;
		private OOptCht(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
		//@formatter:on
	}

	public enum OSupplierType implements IEnumOpt {//@formatter:off
		SUPPLIER(1,"供货商"),CARRIER(2,"运输商"),OUTSOURCING(3,"委托加工商"),SERVER(4,"服务提供商"),
		OTHER(99,"其它");
		public static final String NAME="类型";
		public static final OSupplierType DEFAULT = SUPPLIER; // 定义缺省值
		private EnumLine _line;
		private OSupplierType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OLinkType implements IEnumOpt {//@formatter:off
		SAL(1,"收货"),PUR(2,"发货"),GL(3,"财务");
		public static final String NAME="类型";
		public static final OLinkType DEFAULT = SAL; // 定义缺省值
		private EnumLine _line;
		private OLinkType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OShipingMode implements IEnumOpt {//@formatter:off
		NO(1,"不需运输"),SELF(2,"自提"),EXPRESS(10,"快递"),
		ROAD(30,"陆运"),SEA(50,"海运"),AIR(70,"空运"),OTHER(99,"其它");
		public static final String NAME="运输方式";
		public static final OShipingMode DEFAULT = NO; // 定义缺省值
		private EnumLine _line;
		private OShipingMode(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OAccType implements IEnumOpt {//@formatter:off
		FILE(1,"文件"),
		PICTURE(2, "图片"),
		FRUIT(3, "成果");
		public static final String NAME="附件类型";
		public static final OAccType DEFAULT = FILE; // 定义缺省值
		private EnumLine _line;
		private OAccType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}
}
