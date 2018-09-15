package irille.pub.gl;

import irille.gl.gl.GlNote;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.util.Date;

/**
 * 起止日期
 * 
 * @author whx
 */
public class CmbWriteoffLine extends BeanLong<CmbWriteoffLine> {
	public static final Tb TB = new Tb(CmbWriteoffLine.class, "销账登记核销组件");

	public enum T implements IEnumFld {//@formatter:off
		NOTE(GL.NOTE_ONE2ONE),
		TALLY_DATE(SYS.TALLY_DATE__NULL),
		//>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		PKEY(TB.get("pkey")),	//编号
		;
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
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
		T.NOTE.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldFlds() {
		return Tb.crtCmbFlds(TB);
	}
	public static Fld fldCmb(String code,String name) {
		return TB.crtCmb(code, name, TB);
	}
	
	//@formatter:on

  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Date _tallyDate;	// 记账日期  DATE<null>

	@Override
  public CmbWriteoffLine init(){
		super.init();
    _tallyDate=null;	// 记账日期  DATE
    return this;
  }

  //方法----------------------------------------------
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  //取一对一表对象: GlNote
  public GlNote gtNote(){
    return get(GlNote.class,getPkey());
  }
  public void stNote(GlNote note){
      setPkey(note.getPkey());
  }
  public Date getTallyDate(){
    return _tallyDate;
  }
  public void setTallyDate(Date tallyDate){
    _tallyDate=tallyDate;
  }

}
