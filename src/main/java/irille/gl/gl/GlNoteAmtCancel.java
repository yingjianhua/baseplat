//Created on 2005-10-20
package irille.gl.gl;

import irille.pub.Log;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

/**
 * Title: 内转单<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class GlNoteAmtCancel extends BeanLong<GlNoteAmtCancel> {
	private static final Log LOG = new Log(GlNoteAmtCancel.class);

	public static final Tb TB = new Tb(GlNoteAmtCancel.class, "金额转账冲正单").addActIUDL().addActApprove().setAutoLocal();

	public enum T implements IEnumFld {//@formatter:off
		NOTE(GL.NOTE_ONE2ONE),
		CANCEL_NOTE(GL.NOTE,"被冲正记账条",false),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
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
		T.NOTE.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on
	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _cancelNote;	// 被冲正记账条 <表主键:GlNote>  LONG
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlNoteAmtCancel init(){
		super.init();
    _cancelNote=null;	// 被冲正记账条 <表主键:GlNote>  LONG
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
  //取一对一表对象: GlNote
  public GlNote gtNote(){
    return get(GlNote.class,getPkey());
  }
  public void stNote(GlNote note){
      setPkey(note.getPkey());
  }
  public Long getCancelNote(){
    return _cancelNote;
  }
  public void setCancelNote(Long cancelNote){
    _cancelNote=cancelNote;
  }
  public GlNote gtCancelNote(){
    if(getCancelNote()==null)
      return null;
    return (GlNote)get(GlNote.class,getCancelNote());
  }
  public void stCancelNote(GlNote cancelNote){
    if(cancelNote==null)
      setCancelNote(null);
    else
      setCancelNote(cancelNote.getPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
