package irille.core.sys;

import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbCmb;

import java.util.Date;

/**
 * 起止日期
 * 
 * @author whx
 */
public class CmbBeginEndDate extends BeanInt<CmbBeginEndDate> {
	public static final Tb TB = new TbCmb(CmbBeginEndDate.class, "起止日期");

	public enum T implements IEnumFld {//@formatter:off
		B_DATE(SYS.DATE__NULL,"起始日期"),
		E_DATE(SYS.DATE__NULL,"结束日期"),
		INF(TB.crtInf());
		
		//>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		;
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
		T.B_DATE.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
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
  private Date _bDate;	// 起始日期  DATE<null>
  private Date _eDate;	// 结束日期  DATE<null>

	@Override
  public CmbBeginEndDate init(){
		super.init();
    _bDate=null;	// 起始日期  DATE
    _eDate=null;	// 结束日期  DATE
    return this;
  }

  //方法----------------------------------------------
  public Date getBDate(){
    return _bDate;
  }
  public void setBDate(Date bDate){
    _bDate=bDate;
  }
  public Date getEDate(){
    return _eDate;
  }
  public void setEDate(Date eDate){
    _eDate=eDate;
  }
	public static interface ICmbBeginEndDate{
	  public Date getBDate();
	  public void setBDate(Date bDate);
	  public Date getEDate();
	  public void setEDate(Date eDate);
	}

}
