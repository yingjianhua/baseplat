package irille.gl.frm;

import irille.pub.bean.Bean;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

public class FrmHandoverLine extends BeanLong<FrmHandoverLine> {
	public static final Tb TB = new Tb(FrmHandoverLine.class, "单据交接单明细").setAutoLocal();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		FORM(SYS.ORIG_FORM__CODE, "单据"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		FORM_NUM(TB.get("formNum")),	//单据号
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
		private T(Fld fld) {_fld=TB.add(fld,this); }
		public Fld getFld(){return _fld;}
	}

	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _form;	// 单据  LONG
  private String _formNum;	// 单据号  STR(40)
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public FrmHandoverLine init(){
		super.init();
    _form=null;	// 单据  LONG
    _formNum=null;	// 单据号  STR(40)
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
  public Long getForm(){
    return _form;
  }
  public void setForm(Long form){
    _form=form;
  }
  //外部主键对象: IForm
  public Bean gtForm(){
    return (Bean)gtLongTbObj(getForm());
  }
  public void stForm(Bean form){
      setForm(form.gtLongPkey());
  }
  public String getFormNum(){
    return _formNum;
  }
  public void setFormNum(String formNum){
    _formNum=formNum;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
