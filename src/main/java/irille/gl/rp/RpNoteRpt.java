package irille.gl.rp;

import irille.core.sys.SysUser;
import irille.gl.gl.GlNote;
import irille.gl.rp.Rp.ORptType;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

public class RpNoteRpt extends BeanLong<RpNoteRpt> { //implements ICmbRpRpt{
	public static final Tb TB = new Tb(RpNoteRpt.class, "收款凭条（非现金）").addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		NOTE(GL.NOTE_ONE2ONE),
		TYPE(TB.crt(Rp.ORptType.DEFAULT)),
		RECEIVE_DATE(SYS.DATE__NULL, "到账日期"), //银行的到款日期
		NAME(SYS.NAME__100_NULL, "付款方名称"),
		CASHIER(SYS.USER_SYS, "出纳",true),
		TRAN_TIME(SYS.DATE_TIME__NULL,"收款时间"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		public static final Index IDX_TRAN_TIME= TB.addIndex("tranTime",false,TRAN_TIME);

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
  private Byte _type;	// 收款方式 <ORptType>  BYTE
	// CASH:0,现金
	// BANK_NET:1,网银
	// BANK_TRAN:2,转账
	// BANK_T_TRAN:21,电汇
	// BANK_CASH:22,现金存入
	// BANK_CONSIGN:31,委托收款
	// OTHER:99,其他
  private Date _receiveDate;	// 到账日期  DATE<null>
  private String _name;	// 付款方名称  STR(100)<null>
  private Integer _cashier;	// 出纳 <表主键:SysUser>  INT<null>
  private Date _tranTime;	// 收款时间  TIME<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public RpNoteRpt init(){
		super.init();
    _type=ORptType.DEFAULT.getLine().getKey();	// 收款方式 <ORptType>  BYTE
    _receiveDate=null;	// 到账日期  DATE
    _name=null;	// 付款方名称  STR(100)
    _cashier=null;	// 出纳 <表主键:SysUser>  INT
    _tranTime=null;	// 收款时间  TIME
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
  public ORptType gtType(){
    return (ORptType)(ORptType.BANK_NET.getLine().get(_type));
  }
  public void stType(ORptType type){
    _type=type.getLine().getKey();
  }
  public Date getReceiveDate(){
    return _receiveDate;
  }
  public void setReceiveDate(Date receiveDate){
    _receiveDate=receiveDate;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
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
  public Date getTranTime(){
    return _tranTime;
  }
  public void setTranTime(Date tranTime){
    _tranTime=tranTime;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
