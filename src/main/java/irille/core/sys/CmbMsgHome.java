package irille.core.sys;

import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbCmb;

/**
 * 家庭信息
 * 
 * @author whx
 */
public class CmbMsgHome extends BeanInt<CmbMsgHome> {
	public static final Tb TB = new TbCmb(CmbMsgHome.class, "家庭");

	public enum T implements IEnumFld {//@formatter:off
		TEL(SYS.TEL__NULL),
		ADDR(SYS.ADDR__200_NULL),
		ZIP_CODE(SYS.ZIP__COCE),
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
		T.ADDR.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
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
  private String _tel;	// 电话  STR(20)<null>
  private String _addr;	// 地址  STR(200)<null>
  private String _zipCode;	// 邮编  STR(6)<null>

	@Override
  public CmbMsgHome init(){
		super.init();
    _tel=null;	// 电话  STR(20)
    _addr=null;	// 地址  STR(200)
    _zipCode=null;	// 邮编  STR(6)
    return this;
  }

  //方法----------------------------------------------
  public String getTel(){
    return _tel;
  }
  public void setTel(String tel){
    _tel=tel;
  }
  public String getAddr(){
    return _addr;
  }
  public void setAddr(String addr){
    _addr=addr;
  }
  public String getZipCode(){
    return _zipCode;
  }
  public void setZipCode(String zipCode){
    _zipCode=zipCode;
  }

}
