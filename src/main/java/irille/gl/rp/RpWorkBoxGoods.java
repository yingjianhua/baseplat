package irille.gl.rp;

import irille.core.sys.SysUser;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

public class RpWorkBoxGoods extends BeanLong<RpWorkBoxGoods> {
	private static final Log LOG = new Log(RpWorkBoxGoods.class);

	public static final Tb TB = new Tb(RpWorkBoxGoods.class, "工作箱物品").setAutoLocal();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		CMB_BOX_GOODS(CmbBoxGoods.fldFlds()), //可为印章、发票、支票等对象的主键；如为现金则是出纳现金账户
		USER_SYS(SYS.USER_SYS,"当前保管人"), //与主表一致 
		DATE(SYS.DATE,"入箱日期"),
		REM(SYS.REM__200_NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		BOX_GOODS(TB.get("boxGoods")),	//物品
		NAME(TB.get("name")),	//物品名称
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_BOX_GOODS = TB.addIndex("boxGoods", false, BOX_GOODS);
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
		T.REM.getFld().getTb();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _boxGoods;	// 物品  LONG
  private String _name;	// 物品名称  STR(100)
  private Integer _userSys;	// 当前保管人 <表主键:SysUser>  INT
  private Date _date;	// 入箱日期  DATE
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public RpWorkBoxGoods init(){
		super.init();
    _boxGoods=null;	// 物品  LONG
    _name=null;	// 物品名称  STR(100)
    _userSys=null;	// 当前保管人 <表主键:SysUser>  INT
    _date=null;	// 入箱日期  DATE
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
  public Long getBoxGoods(){
    return _boxGoods;
  }
  public void setBoxGoods(Long boxGoods){
    _boxGoods=boxGoods;
  }
  //外部主键对象: IBeanLong
  public Bean gtBoxGoods(){
    return (Bean)gtLongTbObj(getBoxGoods());
  }
  public void stBoxGoods(Bean boxGoods){
      setBoxGoods(boxGoods.gtLongPkey());
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
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
  public Date getDate(){
    return _date;
  }
  public void setDate(Date date){
    _date=date;
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
