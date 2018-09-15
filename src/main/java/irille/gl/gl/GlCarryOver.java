package irille.gl.gl;

import irille.core.sys.SysTemplat;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

/**
 * 损益结转定义
 * @author whx
 * @version 创建时间：2015年4月24日 下午2:37:15
 */
public class GlCarryOver extends BeanInt<GlCarryOver> {
	public static final Tb TB = new Tb(GlCarryOver.class, "损益结转定义").setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		TEMPLAT(SysTemplat.fldOutKey()),
		SUBJECT_SOURCE(GlSubject.fldOutKey().setName("源科目")),
		SUBJECT_TARGET(GlSubject.fldOutKey().setName("目标科目")),
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
		private T(Fld fld) {_fld=TB.add(fld,this); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
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
  private Integer _pkey;	// 编号  INT
  private Integer _templat;	// 财务模板 <表主键:SysTemplat>  INT
  private Integer _subjectSource;	// 源科目 <表主键:GlSubject>  INT
  private Integer _subjectTarget;	// 目标科目 <表主键:GlSubject>  INT
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlCarryOver init(){
		super.init();
    _templat=null;	// 财务模板 <表主键:SysTemplat>  INT
    _subjectSource=null;	// 源科目 <表主键:GlSubject>  INT
    _subjectTarget=null;	// 目标科目 <表主键:GlSubject>  INT
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
  public Integer getSubjectSource(){
    return _subjectSource;
  }
  public void setSubjectSource(Integer subjectSource){
    _subjectSource=subjectSource;
  }
  public GlSubject gtSubjectSource(){
    if(getSubjectSource()==null)
      return null;
    return (GlSubject)get(GlSubject.class,getSubjectSource());
  }
  public void stSubjectSource(GlSubject subjectSource){
    if(subjectSource==null)
      setSubjectSource(null);
    else
      setSubjectSource(subjectSource.getPkey());
  }
  public Integer getSubjectTarget(){
    return _subjectTarget;
  }
  public void setSubjectTarget(Integer subjectTarget){
    _subjectTarget=subjectTarget;
  }
  public GlSubject gtSubjectTarget(){
    if(getSubjectTarget()==null)
      return null;
    return (GlSubject)get(GlSubject.class,getSubjectTarget());
  }
  public void stSubjectTarget(GlSubject subjectTarget){
    if(subjectTarget==null)
      setSubjectTarget(null);
    else
      setSubjectTarget(subjectTarget.getPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
