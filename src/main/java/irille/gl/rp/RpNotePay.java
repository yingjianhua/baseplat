package irille.gl.rp;

import irille.core.sys.SysUser;
import irille.gl.gl.GlNote;
import irille.gl.rp.Rp.OPayType;
import irille.pub.Log;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.util.Date;

public class RpNotePay extends BeanLong<RpNotePay> { //implements ICmbRpRpt{
	private static final Log LOG = new Log(RpNotePay.class);

	public static final Tb TB = new Tb(RpNotePay.class, "付款凭条（非现金）").addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		NOTE(GL.NOTE_ONE2ONE),
		TYPE(TB.crt(Rp.OPayType.DEFAULT)),
		DATE(SYS.DATE__NULL, "票据日期"),
		PAY_DATE(SYS.DATE__NULL,"实际付款日期"),
		NAME(SYS.NAME__100_NULL, "收款方名称"),
		ACCOUNT(SYS.CODE__40_NULL, "收款方账号"),
		BANK(SYS.NAME__100_NULL, "收款方银行"),
		CASHIER(SYS.USER_SYS, "出纳",true),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<

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
			_fld = TB.add(fld,this);
		}

		public Fld getFld() {
			return _fld;
		}
	}

	static { //在此可以加一些对FLD进行特殊设定的代码
		T.NOTE.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on


	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Byte _type;	// 付款方式 <OPayType>  BYTE
	// CASH:0,现金
	// BANK_NET:1,网银
	// BANK_TRAN:2,转账支票
	// BANK_T_TRAN:21,电汇
	// BANK_CASH:22,现金支票
	// BANK_CONSIGN:31,委托付款
	// BANK_AUTO:32,自动划转
	// OTHER:99,其他
  private Date _date;	// 票据日期  DATE<null>
  private Date _payDate;	// 实际付款日期  DATE<null>
  private String _name;	// 收款方名称  STR(100)<null>
  private String _account;	// 收款方账号  STR(40)<null>
  private String _bank;	// 收款方银行  STR(100)<null>
  private Integer _cashier;	// 出纳 <表主键:SysUser>  INT<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public RpNotePay init(){
		super.init();
    _type=OPayType.DEFAULT.getLine().getKey();	// 付款方式 <OPayType>  BYTE
    _date=null;	// 票据日期  DATE
    _payDate=null;	// 实际付款日期  DATE
    _name=null;	// 收款方名称  STR(100)
    _account=null;	// 收款方账号  STR(40)
    _bank=null;	// 收款方银行  STR(100)
    _cashier=null;	// 出纳 <表主键:SysUser>  INT
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  //取一对一表对象: GlNote
  public GlNote gtNote(){
    return get(GlNote.class,getPkey());
  }
  public void stNote(GlNote note){
      setPkey(note.getPkey());
  }
  public Byte getType(){
    return _type;
  }
  public void setType(Byte type){
    _type=type;
  }
  public OPayType gtType(){
    return (OPayType)(OPayType.BANK_NET.getLine().get(_type));
  }
  public void stType(OPayType type){
    _type=type.getLine().getKey();
  }
  public Date getDate(){
    return _date;
  }
  public void setDate(Date date){
    _date=date;
  }
  public Date getPayDate(){
    return _payDate;
  }
  public void setPayDate(Date payDate){
    _payDate=payDate;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public String getAccount(){
    return _account;
  }
  public void setAccount(String account){
    _account=account;
  }
  public String getBank(){
    return _bank;
  }
  public void setBank(String bank){
    _bank=bank;
  }
  public Integer getCashier(){
    return _cashier;
  }
  public void setCashier(Integer cashier){
    _cashier=cashier;
  }
  public SysUser gtCashier(){
    if(getCashier()==null)
      return null;
    return (SysUser)get(SysUser.class,getCashier());
  }
  public void stCashier(SysUser cashier){
    if(cashier==null)
      setCashier(null);
    else
      setCashier(cashier.getPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
