package irille.gl.frm;

import irille.gl.frm.Frm.OLinkType;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

/**
 * 单据关联关系表，包括源--目的，主--明细，关联单据在此定义
 * @author surface1
 */
public class FrmLink extends BeanLong<FrmLink> { // implements ICmbRpRpt{
	public static final Tb TB = new Tb(FrmLink.class, "单据关联").setAutoIncrement().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		TYPE(TB.crt(Frm.OLinkType.SOURCE_DESC)),
		MAIN_FORM(SYS.ORIG_FORM__CODE, "主单据"),
		LINK_FORM(SYS.ORIG_FORM__CODE, "关联单据"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		MAIN_FORM_NUM(TB.get("mainFormNum")),	//主单据号
		LINK_FORM_NUM(TB.get("linkFormNum")),	//关联单据号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		public static final Index IDX_MAIN_LINK= TB.addIndex("mainLink",true,
				MAIN_FORM, LINK_FORM);
		public static final Index IDX_LINK_FORM= TB.addIndex("linkForm",false,
				LINK_FORM);
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
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
  private Long _pkey;	// 编号  LONG
  private Byte _type;	// 关联类型 <OLinkType>  BYTE
	// SOURCE_DESC:1,源--目的
	// MAIN_NOTE:2,主--明细
	// LINK:3,关联
  private Long _mainForm;	// 主单据  LONG
  private String _mainFormNum;	// 主单据号  STR(40)
  private Long _linkForm;	// 关联单据  LONG
  private String _linkFormNum;	// 关联单据号  STR(40)
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public FrmLink init(){
		super.init();
    _type=OLinkType.DEFAULT.getLine().getKey();	// 关联类型 <OLinkType>  BYTE
    _mainForm=null;	// 主单据  LONG
    _mainFormNum=null;	// 主单据号  STR(40)
    _linkForm=null;	// 关联单据  LONG
    _linkFormNum=null;	// 关联单据号  STR(40)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static FrmLink loadUniqueMainLink(boolean lockFlag,Long mainForm,Long linkForm) {
    return (FrmLink)loadUnique(T.IDX_MAIN_LINK,lockFlag,mainForm,linkForm);
  }
  public static FrmLink chkUniqueMainLink(boolean lockFlag,Long mainForm,Long linkForm) {
    return (FrmLink)chkUnique(T.IDX_MAIN_LINK,lockFlag,mainForm,linkForm);
  }
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public Byte getType(){
    return _type;
  }
  public void setType(Byte type){
    _type=type;
  }
  public OLinkType gtType(){
    return (OLinkType)(OLinkType.SOURCE_DESC.getLine().get(_type));
  }
  public void stType(OLinkType type){
    _type=type.getLine().getKey();
  }
  public Long getMainForm(){
    return _mainForm;
  }
  public void setMainForm(Long mainForm){
    _mainForm=mainForm;
  }
  //外部主键对象: IForm
  public Bean gtMainForm(){
    return (Bean)gtLongTbObj(getMainForm());
  }
  public void stMainForm(Bean mainForm){
      setMainForm(mainForm.gtLongPkey());
  }
  public String getMainFormNum(){
    return _mainFormNum;
  }
  public void setMainFormNum(String mainFormNum){
    _mainFormNum=mainFormNum;
  }
  public Long getLinkForm(){
    return _linkForm;
  }
  public void setLinkForm(Long linkForm){
    _linkForm=linkForm;
  }
  //外部主键对象: IForm
  public Bean gtLinkForm(){
    return (Bean)gtLongTbObj(getLinkForm());
  }
  public void stLinkForm(Bean linkForm){
      setLinkForm(linkForm.gtLongPkey());
  }
  public String getLinkFormNum(){
    return _linkFormNum;
  }
  public void setLinkFormNum(String linkFormNum){
    _linkFormNum=linkFormNum;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
