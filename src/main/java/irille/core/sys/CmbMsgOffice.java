package irille.core.sys;

import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbCmb;

/**
 * 公司信息
 * 
 * @author whx
 */
public class CmbMsgOffice extends BeanInt<CmbMsgOffice> {
	public static final Tb TB = new TbCmb(CmbMsgOffice.class, "公司");

	public enum T implements IEnumFld {//@formatter:off
		COMPANY_NAME(SYS.COMPANY_NAME__NULL, "名称"),
		DEPT_NAME(SYS.DEPT_NAME__NULL),
		POST(SYS.POST__NULL),
		TEL(SYS.TEL__NULL),
		FAX(SYS.FAX__NULL),
		WEBSITE(SYS.WEBSITE__NULL),
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
  private String _companyName;	// 公司名称  STR(100)<null>
  private String _deptName;	// 所在部门  STR(20)<null>
  private String _post;	// 职务  STR(20)<null>
  private String _tel;	// 电话  STR(20)<null>
  private String _fax;	// 传真  STR(20)<null>
  private String _website;	// 网址  STR(200)<null>
  private String _addr;	// 地址  STR(200)<null>
  private String _zipCode;	// 邮编  STR(6)<null>

	@Override
  public CmbMsgOffice init(){
		super.init();
    _companyName=null;	// 公司名称  STR(100)
    _deptName=null;	// 所在部门  STR(20)
    _post=null;	// 职务  STR(20)
    _tel=null;	// 电话  STR(20)
    _fax=null;	// 传真  STR(20)
    _website=null;	// 网址  STR(200)
    _addr=null;	// 地址  STR(200)
    _zipCode=null;	// 邮编  STR(6)
    return this;
  }

  //方法----------------------------------------------
  public String getCompanyName(){
    return _companyName;
  }
  public void setCompanyName(String companyName){
    _companyName=companyName;
  }
  public String getDeptName(){
    return _deptName;
  }
  public void setDeptName(String deptName){
    _deptName=deptName;
  }
  public String getPost(){
    return _post;
  }
  public void setPost(String post){
    _post=post;
  }
  public String getTel(){
    return _tel;
  }
  public void setTel(String tel){
    _tel=tel;
  }
  public String getFax(){
    return _fax;
  }
  public void setFax(String fax){
    _fax=fax;
  }
  public String getWebsite(){
    return _website;
  }
  public void setWebsite(String website){
    _website=website;
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
