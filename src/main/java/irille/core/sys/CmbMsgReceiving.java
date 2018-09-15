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
public class CmbMsgReceiving extends BeanInt<CmbMsgReceiving> {
	public static final Tb TB = new TbCmb(CmbMsgReceiving.class, "收货信息");

	public enum T implements IEnumFld {//@formatter:off
		NAME(SYS.NAME__100_NULL),
		ADDR(SYS.ADDR__200_NULL),
		MOBILE(SYS.MOBILE__NULL),
		TEL(SYS.TEL__NULL),
		REM(SYS.REM__200_NULL),
		INF(TB.crtInf()),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
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
		T.NAME.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldFlds() {
		return Tb.crtCmbFlds(TB);
	}
	public static Fld fldCmb(String code,String name) {
		return TB.crtCmb(code, name, TB);
	}
	//@formatter:on


	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private String _name;	// 名称  STR(100)<null>
  private String _addr;	// 地址  STR(200)<null>
  private String _mobile;	// 手机号码  STR(20)<null>
  private String _tel;	// 电话  STR(20)<null>
  private String _rem;	// 备注  STR(200)<null>

	@Override
  public CmbMsgReceiving init(){
		super.init();
    _name=null;	// 名称  STR(100)
    _addr=null;	// 地址  STR(200)
    _mobile=null;	// 手机号码  STR(20)
    _tel=null;	// 电话  STR(20)
    _rem=null;	// 备注  STR(200)
    return this;
  }

  //方法----------------------------------------------
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public String getAddr(){
    return _addr;
  }
  public void setAddr(String addr){
    _addr=addr;
  }
  public String getMobile(){
    return _mobile;
  }
  public void setMobile(String mobile){
    _mobile=mobile;
  }
  public String getTel(){
    return _tel;
  }
  public void setTel(String tel){
    _tel=tel;
  }
  public String getRem(){
    return _rem;
  }
  public void setRem(String rem){
    _rem=rem;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
