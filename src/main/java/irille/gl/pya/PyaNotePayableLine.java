package irille.gl.pya;

import irille.gl.gl.Gl;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.pub.Log;
import irille.pub.bean.BeanLong;
import irille.pub.gl.CmbWriteoffLine;
import irille.pub.gl.IWriteoff;
import irille.pub.idu.Idu;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

public class PyaNotePayableLine extends BeanLong<PyaNotePayableLine> implements IWriteoff {
	public static final Log LOG = new Log(PyaNotePayableLine.class);
	public static final Tb TB = new Tb(PyaNotePayableLine.class, "其它应付款核销凭条");

	public enum T implements IEnumFld {//@formatter:off
		CMB_WRITEOFF_LINE(CmbWriteoffLine.fldFlds()), 
		MAIN_NOTE(Tb.crtOutKey(PyaNotePayable.class, "mainNote", "应付款")),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		NOTE(TB.get("note")),	//记账条
		TALLY_DATE(TB.get("tallyDate")),	//记账日期
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_MAIN_NOTE_PKEY = TB.addIndex("mainNotePkey", false,MAIN_NOTE,PKEY);
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
		T.CMB_WRITEOFF_LINE.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	
	public void tallyWriteoff(GlNote note) {
		PyaNotePayable write = loadAndLock(PyaNotePayable.class, getMainNote());
		GlNote writeNote = write.gtNote();
		if (note.gtDirect() == writeNote.gtDirect())
			write.setBalance(write.getBalance().add(note.getAmt()));
		else
			write.setBalance(write.getBalance().subtract(note.getAmt()));
		if (write.getBalance().signum() == 0)
			write.stWriteoffState(Gl.OWriteoffState.CLEAR);
		else
			write.stWriteoffState(Gl.OWriteoffState.PART);
		if (write.getBalance().signum()<0)
			throw LOG.err("errAmt", "账户[{0}]的核销计划余额不可为负数!", note.gtJournal().getName());
		write.setUpdatedDate(new Date());
		write.upd();
		setTallyDate(Idu.getOrg().getWorkDate());
		upd();
		GlNoteDAO.recBackByWriteoff(writeNote, writeNote.getAmt().subtract(write.getBalance()));
	}
	
	public void tallyWriteoffCancel(GlNote note) {
		PyaNotePayable write = loadAndLock(PyaNotePayable.class, getMainNote());
		GlNote writeNote = write.gtNote();
		if (note.gtDirect() == writeNote.gtDirect())
			write.setBalance(write.getBalance().subtract(note.getAmt()));
		else
			write.setBalance(write.getBalance().add(note.getAmt()));
		if (write.getBalance().compareTo(write.gtNote().getAmt()) == 0)
			write.stWriteoffState(Gl.OWriteoffState.NO);
		else
			write.stWriteoffState(Gl.OWriteoffState.PART);
		write.upd();
		setTallyDate(null);
		upd();
		GlNoteDAO.recBackByWriteoff(writeNote, writeNote.getAmt().subtract(write.getBalance()));
	}

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Date _tallyDate;	// 记账日期  DATE<null>
  private Long _mainNote;	// 应付款 <表主键:PyaNotePayable>  LONG
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public PyaNotePayableLine init(){
		super.init();
    _tallyDate=null;	// 记账日期  DATE
    _mainNote=null;	// 应付款 <表主键:PyaNotePayable>  LONG
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
  public Date getTallyDate(){
    return _tallyDate;
  }
  public void setTallyDate(Date tallyDate){
    _tallyDate=tallyDate;
  }
  public Long getMainNote(){
    return _mainNote;
  }
  public void setMainNote(Long mainNote){
    _mainNote=mainNote;
  }
  public PyaNotePayable gtMainNote(){
    if(getMainNote()==null)
      return null;
    return (PyaNotePayable)get(PyaNotePayable.class,getMainNote());
  }
  public void stMainNote(PyaNotePayable mainNote){
    if(mainNote==null)
      setMainNote(null);
    else
      setMainNote(mainNote.getPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
