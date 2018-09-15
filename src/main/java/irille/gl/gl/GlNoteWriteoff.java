//Created on 2005-10-20
package irille.gl.gl;

import irille.gl.gl.Gl.OWriteoffState;
import irille.pub.Log;
import irille.pub.bean.BeanLong;
import irille.pub.gl.CmbWriteoff;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Title: 销账计划<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class GlNoteWriteoff extends BeanLong<GlNoteWriteoff> {
	private static final Log LOG = new Log(GlNoteWriteoff.class);

	public static final Tb TB = new Tb(GlNoteWriteoff.class, "销账计划").setAutoLocal().addActIUDL()
			.addActApprove();
	public enum T implements IEnumFld {//@formatter:off
		CMB_WRITEOFF(CmbWriteoff.fldCmbFlds()), 
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		NOTE(TB.get("note")),	//记账条
		WRITEOFF_STATE(TB.get("writeoffState")),	//状态
		BALANCE(TB.get("balance")),	//余额
		DATE_START(TB.get("dateStart")),	//起始日期
		DATE_DUE(TB.get("dateDue")),	//到期日期
		TALLY_DATE(TB.get("tallyDate")),	//记账日期
		UPDATED_DATE(TB.get("updatedDate")),	//更新日期
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		public static final Index IDX_TALLY_DATE_PKEY= 
			TB.addIndex("tallyDatePkey",false,TALLY_DATE,PKEY);

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
		T.CMB_WRITEOFF.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	
	public static Fld fldViewFlds() {
		return Tb.crtFlds("writeoff",TB,T.CMB_WRITEOFF,T.PKEY,T.NOTE);
	}

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Byte _writeoffState;	// 状态 <OWriteoffState>  BYTE
	// NO:1,未销账
	// PART:2,部分销账
	// CLEAR:9,已销账
  private BigDecimal _balance;	// 余额  DEC(16,2)
  private Date _dateStart;	// 起始日期  DATE<null>
  private Date _dateDue;	// 到期日期  DATE<null>
  private Date _tallyDate;	// 记账日期  DATE<null>
  private Date _updatedDate;	// 更新日期  DATE
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlNoteWriteoff init(){
		super.init();
    _writeoffState=OWriteoffState.DEFAULT.getLine().getKey();	// 状态 <OWriteoffState>  BYTE
    _balance=ZERO;	// 余额  DEC(16,2)
    _dateStart=null;	// 起始日期  DATE
    _dateDue=null;	// 到期日期  DATE
    _tallyDate=null;	// 记账日期  DATE
    _updatedDate=Env.getWorkDate();	// 更新日期  DATE
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
  public Byte getWriteoffState(){
    return _writeoffState;
  }
  public void setWriteoffState(Byte writeoffState){
    _writeoffState=writeoffState;
  }
  public OWriteoffState gtWriteoffState(){
    return (OWriteoffState)(OWriteoffState.NO.getLine().get(_writeoffState));
  }
  public void stWriteoffState(OWriteoffState writeoffState){
    _writeoffState=writeoffState.getLine().getKey();
  }
  public BigDecimal getBalance(){
    return _balance;
  }
  public void setBalance(BigDecimal balance){
    _balance=balance;
  }
  public Date getDateStart(){
    return _dateStart;
  }
  public void setDateStart(Date dateStart){
    _dateStart=dateStart;
  }
  public Date getDateDue(){
    return _dateDue;
  }
  public void setDateDue(Date dateDue){
    _dateDue=dateDue;
  }
  public Date getTallyDate(){
    return _tallyDate;
  }
  public void setTallyDate(Date tallyDate){
    _tallyDate=tallyDate;
  }
  public Date getUpdatedDate(){
    return _updatedDate;
  }
  public void setUpdatedDate(Date updatedDate){
    _updatedDate=updatedDate;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
