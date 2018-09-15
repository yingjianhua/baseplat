package irille.gl.gl;

import irille.core.sys.SysTemplat;
import irille.pub.bean.BeanStr;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

/**
 * 科目别名
 * @author whx
 */
public class GlSubjectMap extends BeanStr {
	public static final Tb TB = new Tb(GlSubjectMap.class, "科目别名").addActList().addActUpd()
	    .addActOpt("refresh", "刷新别名", "ins-icon");

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtStrPkey(40)),  //主键的内容为：TEMPLAT-ALIAS_SOURCE-ALIAS_TARGET
		TEMPLAT(SysTemplat.fldOutKey()),
		ALIAS_SOURCE(SYS.CODE__20,"源别名或科目号"),
		ALIAS_TARGET(SYS.STR__20_NULL,"目标别名"), //为空表时源别名直接对应的科目	
		NAME(SYS.NAME__100),
		SUBJECT(GlSubject.fldOutKey().setNull()), //当自动产生源科目的一些别名时，此字段先为空值
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
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
		T.ALIAS_SOURCE._fld.setHelp("为数字表示科目号，英文或中文表示别名。");
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
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
  private String _pkey;	// 编号  STR(40)
  private Integer _templat;	// 财务模板 <表主键:SysTemplat>  INT
  private String _aliasSource;	// 源别名或科目号  STR(20)
  private String _aliasTarget;	// 目标别名  STR(20)<null>
  private String _name;	// 名称  STR(100)
  private Integer _subject;	// 科目字典 <表主键:GlSubject>  INT<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlSubjectMap init(){
		super.init();
    _templat=null;	// 财务模板 <表主键:SysTemplat>  INT
    _aliasSource=null;	// 源别名或科目号  STR(20)
    _aliasTarget=null;	// 目标别名  STR(20)
    _name=null;	// 名称  STR(100)
    _subject=null;	// 科目字典 <表主键:GlSubject>  INT
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
  public Integer getTemplat(){
    return _templat;
  }
  public void setTemplat(Integer templat){
    _templat=templat;
  }
  public SysTemplat gtTemplat(){
    if(getTemplat()==null)
      return null;
    return (SysTemplat)get(SysTemplat.class,getTemplat());
  }
  public void stTemplat(SysTemplat templat){
    if(templat==null)
      setTemplat(null);
    else
      setTemplat(templat.getPkey());
  }
  public String getAliasSource(){
    return _aliasSource;
  }
  public void setAliasSource(String aliasSource){
    _aliasSource=aliasSource;
  }
  public String getAliasTarget(){
    return _aliasTarget;
  }
  public void setAliasTarget(String aliasTarget){
    _aliasTarget=aliasTarget;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Integer getSubject(){
    return _subject;
  }
  public void setSubject(Integer subject){
    _subject=subject;
  }
  public GlSubject gtSubject(){
    if(getSubject()==null)
      return null;
    return (GlSubject)get(GlSubject.class,getSubject());
  }
  public void stSubject(GlSubject subject){
    if(subject==null)
      setSubject(null);
    else
      setSubject(subject.getPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
