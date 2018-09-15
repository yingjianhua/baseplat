//Created on 2005-10-20
package irille.core.sys;

import irille.pub.bean.BeanStr;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

/**
 * Title:序号表明细 <br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class SysSeqLine extends BeanStr<SysSeqLine> {

	public static final Tb TB = new Tb(SysSeqLine.class, "序号表明细", "序号明细");

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtStrPkey(30)), //此字段的内容为[表pkey:机构pkey]
		ORG(SYS.ORG,true),   //此字段在查询显示用
		YMD(SYS.STR__8_NULL,"年月日"),
		SEQNUM(SYS.INT),
		ROW_VERSION(SYS.ROW_VERSION),		
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld, this);}
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private String _pkey;	// 编号  STR(30)
  private Integer _org;	// 机构 <表主键:SysOrg>  INT<null>
  private String _ymd;	// 年月日  STR(8)<null>
  private Integer _seqnum;	// INT  INT
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysSeqLine init(){
		super.init();
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _ymd=null;	// 年月日  STR(8)
    _seqnum=0;	// INT  INT
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public String getPkey(){
    return _pkey;
  }
  public void setPkey(String pkey){
    _pkey=pkey;
  }
  public Integer getOrg(){
    return _org;
  }
  public void setOrg(Integer org){
    _org=org;
  }
  public SysOrg gtOrg(){
    if(getOrg()==null)
      return null;
    return (SysOrg)get(SysOrg.class,getOrg());
  }
  public void stOrg(SysOrg org){
    if(org==null)
      setOrg(null);
    else
      setOrg(org.getPkey());
  }
  public String getYmd(){
    return _ymd;
  }
  public void setYmd(String ymd){
    _ymd=ymd;
  }
  public Integer getSeqnum(){
    return _seqnum;
  }
  public void setSeqnum(Integer seqnum){
    _seqnum=seqnum;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
