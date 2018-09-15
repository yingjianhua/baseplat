package irille.core.prv;

import irille.core.prv.Prv.OPrvType;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

public class PrvRoleTran extends BeanInt<PrvRoleTran> {
	private static final Log LOG = new Log(PrvRoleTran.class);
	public static final Tb TB = new Tb(PrvRoleTran.class, "交易资源权限").setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		ROLE(PrvRole.fldOutKey()),
		GRP(PrvTranGrp.fldOutKey()),
		TYPE(TB.crt(Prv.OPrvType.NO)),
		DAY(SYS.INT, "天数"),
		
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_ROLE_GRP = TB.addIndex("roleGrp",
				true,ROLE,GRP);
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
  private Integer _grp;	// 资源控制组 <表主键:PrvTranGrp>  INT
  private Byte _type;	// 权限类型 <OPrvType>  BYTE
	// NO:0,无法查看
	// USER:1,本人
	// DEPT:11,本部门
	// DEPT_EXT:12,本级及下级部门
	// CELL:21,本核算单元
	// CELL_EXT:22,本级及下级核算单元
	// ORG:31,本机构
	// ORG_EXT:32,本级及下级机构
	// ALL:99,不限制
  private Integer _day;	// 天数  INT

	@Override
  public PrvRoleTran init(){
		super.init();
    _role=null;	// 角色管理 <表主键:PrvRole>  INT
    _grp=null;	// 资源控制组 <表主键:PrvTranGrp>  INT
    _type=OPrvType.DEFAULT.getLine().getKey();	// 权限类型 <OPrvType>  BYTE
    _day=0;	// 天数  INT
    return this;
  }

  //方法----------------------------------------------
  public static PrvRoleTran loadUniqueRoleGrp(boolean lockFlag,Integer role,Integer grp) {
    return (PrvRoleTran)loadUnique(T.IDX_ROLE_GRP,lockFlag,role,grp);
  }
  public static PrvRoleTran chkUniqueRoleGrp(boolean lockFlag,Integer role,Integer grp) {
    return (PrvRoleTran)chkUnique(T.IDX_ROLE_GRP,lockFlag,role,grp);
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
  public Integer getGrp(){
    return _grp;
  }
  public void setGrp(Integer grp){
    _grp=grp;
  }
  public PrvTranGrp gtGrp(){
    if(getGrp()==null)
      return null;
    return (PrvTranGrp)get(PrvTranGrp.class,getGrp());
  }
  public void stGrp(PrvTranGrp grp){
    if(grp==null)
      setGrp(null);
    else
      setGrp(grp.getPkey());
  }
  public Byte getType(){
    return _type;
  }
  public void setType(Byte type){
    _type=type;
  }
  public OPrvType gtType(){
    return (OPrvType)(OPrvType.NO.getLine().get(_type));
  }
  public void stType(OPrvType type){
    _type=type.getLine().getKey();
  }
  public Integer getDay(){
    return _day;
  }
  public void setDay(Integer day){
    _day=day;
  }

	// <<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
