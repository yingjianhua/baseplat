//Created on 2005-10-20
package irille.core.sys;

import irille.core.sys.Sys.OType;
import irille.core.sys.Sys.OYn;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

/**
 * Title:序号主表 <br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class SysSeq extends BeanInt<SysSeq> {
	public static final Tb TB = new Tb(SysSeq.class, "序号主表", "序号主表").setAutoLocal().addActList();

	public enum T implements IEnumFld {//@formatter:off
		SYS_TABLE(TB.crtOneToOne(SysTable.class, "sysTable",	 "表")),
		ORG_FLAG(SYS.YN, "是否按机构编号"),
		FIRST_STR(SYS.STR__10_NULL,"前缀字符"),
		TYPE(TB.crt(Sys.OType.DEFAULT)),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		//public static final Index IDX_CODE = TB.addIndex("code", true,T.CODE);
		private Fld _fld;
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.SYS_TABLE.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Byte _orgFlag;	// 是否按机构编号 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private String _firstStr;	// 前缀字符  STR(10)<null>
  private Byte _type;	// 编号类型 <OType>  BYTE
	// NONE:1,连续编号
	// YYYY:2,4位年份(YYYY)
	// YY:3,2位年份(YY)
	// YYMM:4,2位年份与月份(YYMM)
	// DAY:5,按日编号
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysSeq init(){
		super.init();
    _orgFlag=OYn.DEFAULT.getLine().getKey();	// 是否按机构编号 <OYn>  BYTE
    _firstStr=null;	// 前缀字符  STR(10)
    _type=OType.DEFAULT.getLine().getKey();	// 编号类型 <OType>  BYTE
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
  //取一对一表对象: SysTable
  public SysTable gtSysTable(){
    return get(SysTable.class,getPkey());
  }
  public void stSysTable(SysTable sysTable){
      setPkey(sysTable.getPkey());
  }
  public Byte getOrgFlag(){
    return _orgFlag;
  }
  public void setOrgFlag(Byte orgFlag){
    _orgFlag=orgFlag;
  }
  public Boolean gtOrgFlag(){
    return byteToBoolean(_orgFlag);
  }
  public void stOrgFlag(Boolean orgFlag){
    _orgFlag=booleanToByte(orgFlag);
  }
  public String getFirstStr(){
    return _firstStr;
  }
  public void setFirstStr(String firstStr){
    _firstStr=firstStr;
  }
  public Byte getType(){
    return _type;
  }
  public void setType(Byte type){
    _type=type;
  }
  public OType gtType(){
    return (OType)(OType.NONE.getLine().get(_type));
  }
  public void stType(OType type){
    _type=type.getLine().getKey();
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
