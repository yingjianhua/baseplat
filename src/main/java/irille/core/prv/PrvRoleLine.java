package irille.core.prv;

import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

public class PrvRoleLine extends BeanInt<PrvRoleLine> {
	private static final Log LOG = new Log(PrvRoleLine.class);
	public static final Tb TB = new Tb(PrvRoleLine.class, "子角色")
			.setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		ROLE(PrvRole.fldOutKey()),
		CHILD(PrvRole.fldOutKey("child","子角色")),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_ROLE_CHILD = TB.addIndex("roleChild", true,ROLE,CHILD);
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
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	//@formatter:on

	// >>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _role;	// 角色管理 <表主键:PrvRole>  INT
  private Integer _child;	// 子角色 <表主键:PrvRole>  INT

	@Override
  public PrvRoleLine init(){
		super.init();
    _role=null;	// 角色管理 <表主键:PrvRole>  INT
    _child=null;	// 子角色 <表主键:PrvRole>  INT
    return this;
  }

  //方法----------------------------------------------
  public static PrvRoleLine loadUniqueRoleChild(boolean lockFlag,Integer role,Integer child) {
    return (PrvRoleLine)loadUnique(T.IDX_ROLE_CHILD,lockFlag,role,child);
  }
  public static PrvRoleLine chkUniqueRoleChild(boolean lockFlag,Integer role,Integer child) {
    return (PrvRoleLine)chkUnique(T.IDX_ROLE_CHILD,lockFlag,role,child);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Integer getRole(){
    return _role;
  }
  public void setRole(Integer role){
    _role=role;
  }
  public PrvRole gtRole(){
    if(getRole()==null)
      return null;
    return (PrvRole)get(PrvRole.class,getRole());
  }
  public void stRole(PrvRole role){
    if(role==null)
      setRole(null);
    else
      setRole(role.getPkey());
  }
  public Integer getChild(){
    return _child;
  }
  public void setChild(Integer child){
    _child=child;
  }
  public PrvRole gtChild(){
    if(getChild()==null)
      return null;
    return (PrvRole)get(PrvRole.class,getChild());
  }
  public void stChild(PrvRole child){
    if(child==null)
      setChild(null);
    else
      setChild(child.getPkey());
  }

	// <<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
