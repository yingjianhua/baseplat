package irille.gl.rp;

import irille.core.sys.SysCell;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OEnabled;
import irille.gl.rp.Rp.ORpJournalType;
import irille.pub.Log;
import irille.pub.bean.BeanLong;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

public class RpWorkBox extends BeanLong<RpWorkBox> implements IExtName {
	private static final Log LOG = new Log(RpWorkBox.class);

	public static final Tb TB = new Tb(RpWorkBox.class, "工作箱").setAutoIncrement().addActIUDL().addActEnabled();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		NAME(SYS.NAME__100,"名称"),
		ENABLED(TB.crt(SYS.ENABLED)),
		TYPE(TB.crt(ORpJournalType.CASHIER)),
		USER_SYS(SYS.USER_SYS,"当前保管人"), 
		MNG_CELL(SYS.CELL,"管理核算单元"),
		REM(SYS.REM__200_NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_MNG_CELL = TB.addIndex("mngCell", false, MNG_CELL);
		public static final Index IDX_USER = TB.addIndex("userSys", false, USER_SYS);
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
		T.NAME.getFld().getTb();//加锁所有字段,不可以修改
	}
	//@formatter:on
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName()).setName("所属工作箱"); //XXX
	}

	public static Fld fldOutKey(String code, String name) {
		Fld fld = Tb.crtOutKey(TB, code, name);
		fld.setType(null);
		return fld;
	}

	@Override
	public String getExtName() {
		return getName();
	}

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private String _name;	// 名称  STR(100)
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Byte _type;	// 账户类型 <ORpJournalType>  BYTE
	// CASHIER:1,现金
	// COM:2,对公账户
	// CARD:3,对私卡账户
	// OTHER:9,第三方支付
  private Integer _userSys;	// 当前保管人 <表主键:SysUser>  INT
  private Integer _mngCell;	// 管理核算单元 <表主键:SysCell>  INT
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public RpWorkBox init(){
		super.init();
    _name=null;	// 名称  STR(100)
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _type=ORpJournalType.DEFAULT.getLine().getKey();	// 账户类型 <ORpJournalType>  BYTE
    _userSys=null;	// 当前保管人 <表主键:SysUser>  INT
    _mngCell=null;	// 管理核算单元 <表主键:SysCell>  INT
    _rem=null;	// 备注  STR(200)
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
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Byte getEnabled(){
    return _enabled;
  }
  public void setEnabled(Byte enabled){
    _enabled=enabled;
  }
  public Boolean gtEnabled(){
    return byteToBoolean(_enabled);
  }
  public void stEnabled(Boolean enabled){
    _enabled=booleanToByte(enabled);
  }
  public Byte getType(){
    return _type;
  }
  public void setType(Byte type){
    _type=type;
  }
  public ORpJournalType gtType(){
    return (ORpJournalType)(ORpJournalType.CASHIER.getLine().get(_type));
  }
  public void stType(ORpJournalType type){
    _type=type.getLine().getKey();
  }
  public Integer getUserSys(){
    return _userSys;
  }
  public void setUserSys(Integer userSys){
    _userSys=userSys;
  }
  public SysUser gtUserSys(){
    if(getUserSys()==null)
      return null;
    return (SysUser)get(SysUser.class,getUserSys());
  }
  public void stUserSys(SysUser userSys){
    if(userSys==null)
      setUserSys(null);
    else
      setUserSys(userSys.getPkey());
  }
  public Integer getMngCell(){
    return _mngCell;
  }
  public void setMngCell(Integer mngCell){
    _mngCell=mngCell;
  }
  public SysCell gtMngCell(){
    if(getMngCell()==null)
      return null;
    return (SysCell)get(SysCell.class,getMngCell());
  }
  public void stMngCell(SysCell mngCell){
    if(mngCell==null)
      setMngCell(null);
    else
      setMngCell(mngCell.getPkey());
  }
  public String getRem(){
    return _rem;
  }
  public void setRem(String rem){
    _rem=rem;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
