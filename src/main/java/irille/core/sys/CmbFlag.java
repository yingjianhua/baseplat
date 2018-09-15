package irille.core.sys;

import irille.core.sys.Sys.OYn;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbCmb;

/**
 * 组合字段 - 零售标志
 * @author whx
 * @version 创建时间：2014年11月27日 下午3:34:37
 */
public class CmbFlag extends BeanInt<CmbFlag> {
	public static final Tb TB = new TbCmb(CmbFlag.class, "零售标志");

	public enum T implements IEnumFld {//@formatter:off
		FLAG_SAL(SYS.YN, "零售标志"),
		FLAG_PF(SYS.YN, "批发标志"),
		FLAG_MVOUT(SYS.YN, "调出标志"),
		FLAG_MVIN(SYS.YN, "调入标志"),
		FLAG_PUR(SYS.YN, "采购标志"),
		FLAG_FINI(SYS.YN, "产成品标志"),
		FLAG_HALF(SYS.YN, "半成品标志"),
		FLAG_PRIV(SYS.YN, "自用品标志"),
		
		//>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		;
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
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
		T.FLAG_SAL.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldFlds() {
		return Tb.crtCmbFlds(TB);
	}
	public static Fld fldCmb(String code,String name) {
		return TB.crtCmb(code, name, TB);
	}
	//@formatter:on


  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //实例变量定义-----------------------------------------
  private Byte _flagSal;	// 零售标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _flagPf;	// 批发标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _flagMvout;	// 调出标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _flagMvin;	// 调入标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _flagPur;	// 采购标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _flagFini;	// 产成品标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _flagHalf;	// 半成品标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _flagPriv;	// 自用品标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否

	@Override
  public CmbFlag init(){
		super.init();
    _flagSal=OYn.DEFAULT.getLine().getKey();	// 零售标志 <OYn>  BYTE
    _flagPf=OYn.DEFAULT.getLine().getKey();	// 批发标志 <OYn>  BYTE
    _flagMvout=OYn.DEFAULT.getLine().getKey();	// 调出标志 <OYn>  BYTE
    _flagMvin=OYn.DEFAULT.getLine().getKey();	// 调入标志 <OYn>  BYTE
    _flagPur=OYn.DEFAULT.getLine().getKey();	// 采购标志 <OYn>  BYTE
    _flagFini=OYn.DEFAULT.getLine().getKey();	// 产成品标志 <OYn>  BYTE
    _flagHalf=OYn.DEFAULT.getLine().getKey();	// 半成品标志 <OYn>  BYTE
    _flagPriv=OYn.DEFAULT.getLine().getKey();	// 自用品标志 <OYn>  BYTE
    return this;
  }

  //方法----------------------------------------------
  public Byte getFlagSal(){
    return _flagSal;
  }
  public void setFlagSal(Byte flagSal){
    _flagSal=flagSal;
  }
  public Boolean gtFlagSal(){
    return byteToBoolean(_flagSal);
  }
  public void stFlagSal(Boolean flagSal){
    _flagSal=booleanToByte(flagSal);
  }
  public Byte getFlagPf(){
    return _flagPf;
  }
  public void setFlagPf(Byte flagPf){
    _flagPf=flagPf;
  }
  public Boolean gtFlagPf(){
    return byteToBoolean(_flagPf);
  }
  public void stFlagPf(Boolean flagPf){
    _flagPf=booleanToByte(flagPf);
  }
  public Byte getFlagMvout(){
    return _flagMvout;
  }
  public void setFlagMvout(Byte flagMvout){
    _flagMvout=flagMvout;
  }
  public Boolean gtFlagMvout(){
    return byteToBoolean(_flagMvout);
  }
  public void stFlagMvout(Boolean flagMvout){
    _flagMvout=booleanToByte(flagMvout);
  }
  public Byte getFlagMvin(){
    return _flagMvin;
  }
  public void setFlagMvin(Byte flagMvin){
    _flagMvin=flagMvin;
  }
  public Boolean gtFlagMvin(){
    return byteToBoolean(_flagMvin);
  }
  public void stFlagMvin(Boolean flagMvin){
    _flagMvin=booleanToByte(flagMvin);
  }
  public Byte getFlagPur(){
    return _flagPur;
  }
  public void setFlagPur(Byte flagPur){
    _flagPur=flagPur;
  }
  public Boolean gtFlagPur(){
    return byteToBoolean(_flagPur);
  }
  public void stFlagPur(Boolean flagPur){
    _flagPur=booleanToByte(flagPur);
  }
  public Byte getFlagFini(){
    return _flagFini;
  }
  public void setFlagFini(Byte flagFini){
    _flagFini=flagFini;
  }
  public Boolean gtFlagFini(){
    return byteToBoolean(_flagFini);
  }
  public void stFlagFini(Boolean flagFini){
    _flagFini=booleanToByte(flagFini);
  }
  public Byte getFlagHalf(){
    return _flagHalf;
  }
  public void setFlagHalf(Byte flagHalf){
    _flagHalf=flagHalf;
  }
  public Boolean gtFlagHalf(){
    return byteToBoolean(_flagHalf);
  }
  public void stFlagHalf(Boolean flagHalf){
    _flagHalf=booleanToByte(flagHalf);
  }
  public Byte getFlagPriv(){
    return _flagPriv;
  }
  public void setFlagPriv(Byte flagPriv){
    _flagPriv=flagPriv;
  }
  public Boolean gtFlagPriv(){
    return byteToBoolean(_flagPriv);
  }
  public void stFlagPriv(Boolean flagPriv){
    _flagPriv=booleanToByte(flagPriv);
  }

}
