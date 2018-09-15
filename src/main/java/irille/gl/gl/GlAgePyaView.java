package irille.gl.gl;

import irille.core.sys.SysDept;
import irille.core.sys.SysUser;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbView;

import java.math.BigDecimal;

/**
 * 入库时的操作明细表，如何使用待定
 * @author whx
 * @version 创建时间：2014年8月25日 上午10:22:10
 */
public class GlAgePyaView extends BeanLong<GlAgePyaView> {
	public static final Tb TB = new TbView(GlAgePyaView.class, "应付账龄").setAutoLocal().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		DEPT(SYS.DEPT, true),
		BUSINESS_MEMBER(SYS.BUSINESS_MEMBER, "业务员", true),
		CODE(SYS.STR__40_NULL, "代码"),
		NAME(SYS.STR__100_NULL, "名称"),
		BALANCE(SYS.AMT, "余额"),
		BALANCE_A(SYS.AMT, "0-30天"),
		BALANCE_B(SYS.AMT, "30-60天"),
		BALANCE_C(SYS.AMT, "60-90天"),
		BALANCE_D(SYS.AMT, "90天以上"),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
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
		private T(Fld fld) {_fld=TB.add(fld, this); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Integer _dept;	// 部门 <表主键:SysDept>  INT<null>
  private Integer _businessMember;	// 业务员 <表主键:SysUser>  INT<null>
  private String _code;	// 代码  STR(40)<null>
  private String _name;	// 名称  STR(100)<null>
  private BigDecimal _balance;	// 余额  DEC(16,2)
  private BigDecimal _balanceA;	// 0-30天  DEC(16,2)
  private BigDecimal _balanceB;	// 30-60天  DEC(16,2)
  private BigDecimal _balanceC;	// 60-90天  DEC(16,2)
  private BigDecimal _balanceD;	// 90天以上  DEC(16,2)

	@Override
  public GlAgePyaView init(){
		super.init();
    _dept=null;	// 部门 <表主键:SysDept>  INT
    _businessMember=null;	// 业务员 <表主键:SysUser>  INT
    _code=null;	// 代码  STR(40)
    _name=null;	// 名称  STR(100)
    _balance=ZERO;	// 余额  DEC(16,2)
    _balanceA=ZERO;	// 0-30天  DEC(16,2)
    _balanceB=ZERO;	// 30-60天  DEC(16,2)
    _balanceC=ZERO;	// 60-90天  DEC(16,2)
    _balanceD=ZERO;	// 90天以上  DEC(16,2)
    return this;
  }

  //方法----------------------------------------------
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public Integer getDept(){
    return _dept;
  }
  public void setDept(Integer dept){
    _dept=dept;
  }
  public SysDept gtDept(){
    if(getDept()==null)
      return null;
    return (SysDept)get(SysDept.class,getDept());
  }
  public void stDept(SysDept dept){
    if(dept==null)
      setDept(null);
    else
      setDept(dept.getPkey());
  }
  public Integer getBusinessMember(){
    return _businessMember;
  }
  public void setBusinessMember(Integer businessMember){
    _businessMember=businessMember;
  }
  public SysUser gtBusinessMember(){
    if(getBusinessMember()==null)
      return null;
    return (SysUser)get(SysUser.class,getBusinessMember());
  }
  public void stBusinessMember(SysUser businessMember){
    if(businessMember==null)
      setBusinessMember(null);
    else
      setBusinessMember(businessMember.getPkey());
  }
  public String getCode(){
    return _code;
  }
  public void setCode(String code){
    _code=code;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public BigDecimal getBalance(){
    return _balance;
  }
  public void setBalance(BigDecimal balance){
    _balance=balance;
  }
  public BigDecimal getBalanceA(){
    return _balanceA;
  }
  public void setBalanceA(BigDecimal balanceA){
    _balanceA=balanceA;
  }
  public BigDecimal getBalanceB(){
    return _balanceB;
  }
  public void setBalanceB(BigDecimal balanceB){
    _balanceB=balanceB;
  }
  public BigDecimal getBalanceC(){
    return _balanceC;
  }
  public void setBalanceC(BigDecimal balanceC){
    _balanceC=balanceC;
  }
  public BigDecimal getBalanceD(){
    return _balanceD;
  }
  public void setBalanceD(BigDecimal balanceD){
    _balanceD=balanceD;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
