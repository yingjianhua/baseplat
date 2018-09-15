package irille.core.sys;

import irille.core.sys.Sys.OAccType;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.math.BigDecimal;
import java.util.Date;

public class SysAccessory extends BeanInt<SysAccessory> {
	private static final Log LOG = new Log(SysAccessory.class);

	public static final Tb TB = new Tb(SysAccessory.class, "附件表", "附件表")
			.setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		TBANDKEY(TB.crtTbAndKey("parent","所属对象")),	
		TYPE(TB.crt(Sys.OAccType.DEFAULT).setNull()),
		NAME(SYS.NAME__100_NULL),
		PATH(SYS.PHOTO__NULL, "路径"),
		SIZE(SYS.AMT, "附件大小", true),
		CREATED_BY(SYS.CREATED_BY, true),
		CREATED_TIME(SYS.CREATED_DATE_TIME, true),
		ROW_VERSION(SYS.ROW_VERSION),
//		(FLDS.),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		TBANDKEY_TABLE(TB.get("tbandkeyTable")),	//所属对象
		TBANDKEY_PKEY(TB.get("tbandkeyPkey")),	//所属对象主键值
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		static {
			TB.get("tbandkeyTable").setNull();
			TB.get("tbandkeyPkey").setNull();
		}
		// 索引
		//public static final Index IDX_CODE = TB.addIndex("code", true,CODE);
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld, this); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on


	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _tbandkeyTable;	// 所属对象 <表主键:SysTable>  INT<null>
  private Integer _tbandkeyPkey;	// 所属对象主键值  INT<null>
  private Byte _type;	// 附件类型 <OAccType>  BYTE<null>
	// FILE:1,文件
	// PICTURE:2,图片
	// FRUIT:3,成果
  private String _name;	// 名称  STR(100)<null>
  private String _path;	// 路径  STR(200)<null>
  private BigDecimal _size;	// 附件大小  DEC(16,2)<null>
  private Integer _createdBy;	// 建档员 <表主键:SysUser>  INT<null>
  private Date _createdTime;	// 建档时间  TIME<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysAccessory init(){
		super.init();
    _tbandkeyTable=null;	// 所属对象 <表主键:SysTable>  INT
    _tbandkeyPkey=null;	// 所属对象主键值  INT
    _type=OAccType.DEFAULT.getLine().getKey();	// 附件类型 <OAccType>  BYTE
    _name=null;	// 名称  STR(100)
    _path=null;	// 路径  STR(200)
    _size=null;	// 附件大小  DEC(16,2)
    _createdBy=null;	// 建档员 <表主键:SysUser>  INT
    _createdTime=null;	// 建档时间  TIME
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
  //根据表字段及主键字段的值取对象
  public Bean gtTbandkey(){
    if(getTbandkeyPkey()==null)
    	return null;
    return gtTbAndPkeyObj(getTbandkeyTable(),getTbandkeyPkey());
  }
  public void stTbandkey(Bean tbandkey){
    setTbandkeyPkey((Integer)tbandkey.pkeyValues()[0]);
    setTbandkeyTable(tbandkey.gtTbId());
  }
  public Integer getTbandkeyTable(){
    return _tbandkeyTable;
  }
  public void setTbandkeyTable(Integer tbandkeyTable){
    _tbandkeyTable=tbandkeyTable;
  }
  public SysTable gtTbandkeyTable(){
    if(getTbandkeyTable()==null)
      return null;
    return (SysTable)get(SysTable.class,getTbandkeyTable());
  }
  public void stTbandkeyTable(SysTable tbandkeyTable){
    if(tbandkeyTable==null)
      setTbandkeyTable(null);
    else
      setTbandkeyTable(tbandkeyTable.getPkey());
  }
  public Integer getTbandkeyPkey(){
    return _tbandkeyPkey;
  }
  public void setTbandkeyPkey(Integer tbandkeyPkey){
    _tbandkeyPkey=tbandkeyPkey;
  }
  public Byte getType(){
    return _type;
  }
  public void setType(Byte type){
    _type=type;
  }
  public OAccType gtType(){
    return (OAccType)(OAccType.FILE.getLine().get(_type));
  }
  public void stType(OAccType type){
    _type=type.getLine().getKey();
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public String getPath(){
    return _path;
  }
  public void setPath(String path){
    _path=path;
  }
  public BigDecimal getSize(){
    return _size;
  }
  public void setSize(BigDecimal size){
    _size=size;
  }
  public Integer getCreatedBy(){
    return _createdBy;
  }
  public void setCreatedBy(Integer createdBy){
    _createdBy=createdBy;
  }
  public SysUser gtCreatedBy(){
    if(getCreatedBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getCreatedBy());
  }
  public void stCreatedBy(SysUser createdBy){
    if(createdBy==null)
      setCreatedBy(null);
    else
      setCreatedBy(createdBy.getPkey());
  }
  public Date getCreatedTime(){
    return _createdTime;
  }
  public void setCreatedTime(Date createdTime){
    _createdTime=createdTime;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
