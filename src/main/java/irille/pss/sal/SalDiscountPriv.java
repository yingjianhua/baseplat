package irille.pss.sal;

import irille.core.sys.SysUser;
import irille.pss.sal.Sal.ODiscountLevel;
import irille.pub.bean.BeanInt;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.util.Date;

/**
 * 与用户表一对一关系
 * @author whx
 * @version 创建时间：2014年8月14日 下午3:18:46
 */
public class SalDiscountPriv extends BeanInt<SalDiscountPriv> {
	public static final Tb TB = new Tb(SalDiscountPriv.class, "折扣权限", "折扣权限").setAutoLocal().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		USER(TB.crtOneToOne(SysUser.class,"user", "用户" )),
		DISCOUNT_LEVEL(TB.crt(Sal.ODiscountLevel.DEFAULT).setName("折扣级别")),
		UPDATED_BY(SYS.UPDATED_BY), //更新员
		UPDATED_TIME(SYS.UPDATED_DATE_TIME), //更新日期
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		//public static final Index IDX_CODE = TB.addIndex("code", true,CODE);
		private Fld _fld;
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); }
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld,this); }
		public Fld getFld(){return _fld;}
	}

	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Byte _discountLevel;	// 折扣级别 <ODiscountLevel>  BYTE
	// ON:1,级别一
	// TW:2,级别二
	// TH:3,级别三
	// FO:4,级别四
	// FI:5,级别五
	// SI:6,级别六
	// SE:7,级别七
	// EI:8,级别八
	// NI:9,级别九
	// TE:10,级别十
	// EL:11,级别十一
	// TWE:12,级别十二
  private Integer _updatedBy;	// 更新员 <表主键:SysUser>  INT
  private Date _updatedTime;	// 更新时间  TIME
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SalDiscountPriv init(){
		super.init();
    _discountLevel=ODiscountLevel.DEFAULT.getLine().getKey();	// 折扣级别 <ODiscountLevel>  BYTE
    _updatedBy=null;	// 更新员 <表主键:SysUser>  INT
    _updatedTime=Env.getTranBeginTime();	// 更新时间  TIME
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  //取一对一表对象: SysUser
  public SysUser gtUser(){
    return get(SysUser.class,getPkey());
  }
  public void stUser(SysUser user){
      setPkey(user.getPkey());
  }
  public Byte getDiscountLevel(){
    return _discountLevel;
  }
  public void setDiscountLevel(Byte discountLevel){
    _discountLevel=discountLevel;
  }
  public ODiscountLevel gtDiscountLevel(){
    return (ODiscountLevel)(ODiscountLevel.ON.getLine().get(_discountLevel));
  }
  public void stDiscountLevel(ODiscountLevel discountLevel){
    _discountLevel=discountLevel.getLine().getKey();
  }
  public Integer getUpdatedBy(){
    return _updatedBy;
  }
  public void setUpdatedBy(Integer updatedBy){
    _updatedBy=updatedBy;
  }
  public SysUser gtUpdatedBy(){
    if(getUpdatedBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getUpdatedBy());
  }
  public void stUpdatedBy(SysUser updatedBy){
    if(updatedBy==null)
      setUpdatedBy(null);
    else
      setUpdatedBy(updatedBy.getPkey());
  }
  public Date getUpdatedTime(){
    return _updatedTime;
  }
  public void setUpdatedTime(Date updatedTime){
    _updatedTime=updatedTime;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
