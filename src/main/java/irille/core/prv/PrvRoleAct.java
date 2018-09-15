package irille.core.prv;

import irille.core.sys.SysMenu;
import irille.core.sys.SysMenuAct;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

public class PrvRoleAct extends BeanInt<PrvRoleAct> {
	private static final Log LOG = new Log(PrvRoleAct.class);
	public static final Tb TB = new Tb(PrvRoleAct.class, "交易功能权限").setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		ROLE(PrvRole.fldOutKey()),
		MENU(SysMenu.fldOutKey()), 
		ACT(TB.crtDime(SysMenuAct.fldOutKey().setNull(), new int[]{1,2,3,4,5,6,7,8,9,10,11,12},
				new String[]{"功能1","功能2","功能3","功能4","功能5","功能6","功能7","功能8","功能9","功能10","功能11","功能12"})),
		
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		ACT1(TB.get("act1")),	//功能1
		ACT2(TB.get("act2")),	//功能2
		ACT3(TB.get("act3")),	//功能3
		ACT4(TB.get("act4")),	//功能4
		ACT5(TB.get("act5")),	//功能5
		ACT6(TB.get("act6")),	//功能6
		ACT7(TB.get("act7")),	//功能7
		ACT8(TB.get("act8")),	//功能8
		ACT9(TB.get("act9")),	//功能9
		ACT10(TB.get("act10")),	//功能10
		ACT11(TB.get("act11")),	//功能11
		ACT12(TB.get("act12")),	//功能12
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_ROLE_MENU = TB.addIndex("roleMenu",
				true,ROLE,MENU);
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
	
	//清空所有功能，用于权限修改时调用
	public void clearAct() {
		for (int i=1; i<=12; i++)
			stAct(i, null);
	}
	
	//@formatter:on

	// >>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _role;	// 角色管理 <表主键:PrvRole>  INT
  private Integer _menu;	// 系统菜单 <表主键:SysMenu>  INT
  private Integer _act1;	// 功能1 <表主键:SysMenuAct>  INT<null>
  private Integer _act2;	// 功能2 <表主键:SysMenuAct>  INT<null>
  private Integer _act3;	// 功能3 <表主键:SysMenuAct>  INT<null>
  private Integer _act4;	// 功能4 <表主键:SysMenuAct>  INT<null>
  private Integer _act5;	// 功能5 <表主键:SysMenuAct>  INT<null>
  private Integer _act6;	// 功能6 <表主键:SysMenuAct>  INT<null>
  private Integer _act7;	// 功能7 <表主键:SysMenuAct>  INT<null>
  private Integer _act8;	// 功能8 <表主键:SysMenuAct>  INT<null>
  private Integer _act9;	// 功能9 <表主键:SysMenuAct>  INT<null>
  private Integer _act10;	// 功能10 <表主键:SysMenuAct>  INT<null>
  private Integer _act11;	// 功能11 <表主键:SysMenuAct>  INT<null>
  private Integer _act12;	// 功能12 <表主键:SysMenuAct>  INT<null>

	@Override
  public PrvRoleAct init(){
		super.init();
    _role=null;	// 角色管理 <表主键:PrvRole>  INT
    _menu=null;	// 系统菜单 <表主键:SysMenu>  INT
    _act1=null;	// 功能1 <表主键:SysMenuAct>  INT
    _act2=null;	// 功能2 <表主键:SysMenuAct>  INT
    _act3=null;	// 功能3 <表主键:SysMenuAct>  INT
    _act4=null;	// 功能4 <表主键:SysMenuAct>  INT
    _act5=null;	// 功能5 <表主键:SysMenuAct>  INT
    _act6=null;	// 功能6 <表主键:SysMenuAct>  INT
    _act7=null;	// 功能7 <表主键:SysMenuAct>  INT
    _act8=null;	// 功能8 <表主键:SysMenuAct>  INT
    _act9=null;	// 功能9 <表主键:SysMenuAct>  INT
    _act10=null;	// 功能10 <表主键:SysMenuAct>  INT
    _act11=null;	// 功能11 <表主键:SysMenuAct>  INT
    _act12=null;	// 功能12 <表主键:SysMenuAct>  INT
    return this;
  }

  //方法----------------------------------------------
  public static PrvRoleAct loadUniqueRoleMenu(boolean lockFlag,Integer role,Integer menu) {
    return (PrvRoleAct)loadUnique(T.IDX_ROLE_MENU,lockFlag,role,menu);
  }
  public static PrvRoleAct chkUniqueRoleMenu(boolean lockFlag,Integer role,Integer menu) {
    return (PrvRoleAct)chkUnique(T.IDX_ROLE_MENU,lockFlag,role,menu);
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
  public Integer getMenu(){
    return _menu;
  }
  public void setMenu(Integer menu){
    _menu=menu;
  }
  public SysMenu gtMenu(){
    if(getMenu()==null)
      return null;
    return (SysMenu)get(SysMenu.class,getMenu());
  }
  public void stMenu(SysMenu menu){
    if(menu==null)
      setMenu(null);
    else
      setMenu(menu.getPkey());
  }
  //数组对象: Integer
  public Integer gtAct(int i){
    switch(i) {
    case 1:
    	return getAct1();
    case 2:
    	return getAct2();
    case 3:
    	return getAct3();
    case 4:
    	return getAct4();
    case 5:
    	return getAct5();
    case 6:
    	return getAct6();
    case 7:
    	return getAct7();
    case 8:
    	return getAct8();
    case 9:
    	return getAct9();
    case 10:
    	return getAct10();
    case 11:
    	return getAct11();
    case 12:
    	return getAct12();
  	default: throw LOG.err("dimeErr","Dime numb[{0}] invalid.",i);
		}
  }
  public void stAct( int i, Integer act){
    switch(i) {
    case 1:
    	setAct1(act);
    	return;
    case 2:
    	setAct2(act);
    	return;
    case 3:
    	setAct3(act);
    	return;
    case 4:
    	setAct4(act);
    	return;
    case 5:
    	setAct5(act);
    	return;
    case 6:
    	setAct6(act);
    	return;
    case 7:
    	setAct7(act);
    	return;
    case 8:
    	setAct8(act);
    	return;
    case 9:
    	setAct9(act);
    	return;
    case 10:
    	setAct10(act);
    	return;
    case 11:
    	setAct11(act);
    	return;
    case 12:
    	setAct12(act);
    	return;
  	default: throw LOG.err("dimeErr","Dime numb[{0}] invalid.",i);
		}
  }
  public Integer getAct1(){
    return _act1;
  }
  public void setAct1(Integer act1){
    _act1=act1;
  }
  public SysMenuAct gtAct1(){
    if(getAct1()==null)
      return null;
    return (SysMenuAct)get(SysMenuAct.class,getAct1());
  }
  public void stAct1(SysMenuAct act1){
    if(act1==null)
      setAct1(null);
    else
      setAct1(act1.getPkey());
  }
  public Integer getAct2(){
    return _act2;
  }
  public void setAct2(Integer act2){
    _act2=act2;
  }
  public SysMenuAct gtAct2(){
    if(getAct2()==null)
      return null;
    return (SysMenuAct)get(SysMenuAct.class,getAct2());
  }
  public void stAct2(SysMenuAct act2){
    if(act2==null)
      setAct2(null);
    else
      setAct2(act2.getPkey());
  }
  public Integer getAct3(){
    return _act3;
  }
  public void setAct3(Integer act3){
    _act3=act3;
  }
  public SysMenuAct gtAct3(){
    if(getAct3()==null)
      return null;
    return (SysMenuAct)get(SysMenuAct.class,getAct3());
  }
  public void stAct3(SysMenuAct act3){
    if(act3==null)
      setAct3(null);
    else
      setAct3(act3.getPkey());
  }
  public Integer getAct4(){
    return _act4;
  }
  public void setAct4(Integer act4){
    _act4=act4;
  }
  public SysMenuAct gtAct4(){
    if(getAct4()==null)
      return null;
    return (SysMenuAct)get(SysMenuAct.class,getAct4());
  }
  public void stAct4(SysMenuAct act4){
    if(act4==null)
      setAct4(null);
    else
      setAct4(act4.getPkey());
  }
  public Integer getAct5(){
    return _act5;
  }
  public void setAct5(Integer act5){
    _act5=act5;
  }
  public SysMenuAct gtAct5(){
    if(getAct5()==null)
      return null;
    return (SysMenuAct)get(SysMenuAct.class,getAct5());
  }
  public void stAct5(SysMenuAct act5){
    if(act5==null)
      setAct5(null);
    else
      setAct5(act5.getPkey());
  }
  public Integer getAct6(){
    return _act6;
  }
  public void setAct6(Integer act6){
    _act6=act6;
  }
  public SysMenuAct gtAct6(){
    if(getAct6()==null)
      return null;
    return (SysMenuAct)get(SysMenuAct.class,getAct6());
  }
  public void stAct6(SysMenuAct act6){
    if(act6==null)
      setAct6(null);
    else
      setAct6(act6.getPkey());
  }
  public Integer getAct7(){
    return _act7;
  }
  public void setAct7(Integer act7){
    _act7=act7;
  }
  public SysMenuAct gtAct7(){
    if(getAct7()==null)
      return null;
    return (SysMenuAct)get(SysMenuAct.class,getAct7());
  }
  public void stAct7(SysMenuAct act7){
    if(act7==null)
      setAct7(null);
    else
      setAct7(act7.getPkey());
  }
  public Integer getAct8(){
    return _act8;
  }
  public void setAct8(Integer act8){
    _act8=act8;
  }
  public SysMenuAct gtAct8(){
    if(getAct8()==null)
      return null;
    return (SysMenuAct)get(SysMenuAct.class,getAct8());
  }
  public void stAct8(SysMenuAct act8){
    if(act8==null)
      setAct8(null);
    else
      setAct8(act8.getPkey());
  }
  public Integer getAct9(){
    return _act9;
  }
  public void setAct9(Integer act9){
    _act9=act9;
  }
  public SysMenuAct gtAct9(){
    if(getAct9()==null)
      return null;
    return (SysMenuAct)get(SysMenuAct.class,getAct9());
  }
  public void stAct9(SysMenuAct act9){
    if(act9==null)
      setAct9(null);
    else
      setAct9(act9.getPkey());
  }
  public Integer getAct10(){
    return _act10;
  }
  public void setAct10(Integer act10){
    _act10=act10;
  }
  public SysMenuAct gtAct10(){
    if(getAct10()==null)
      return null;
    return (SysMenuAct)get(SysMenuAct.class,getAct10());
  }
  public void stAct10(SysMenuAct act10){
    if(act10==null)
      setAct10(null);
    else
      setAct10(act10.getPkey());
  }
  public Integer getAct11(){
    return _act11;
  }
  public void setAct11(Integer act11){
    _act11=act11;
  }
  public SysMenuAct gtAct11(){
    if(getAct11()==null)
      return null;
    return (SysMenuAct)get(SysMenuAct.class,getAct11());
  }
  public void stAct11(SysMenuAct act11){
    if(act11==null)
      setAct11(null);
    else
      setAct11(act11.getPkey());
  }
  public Integer getAct12(){
    return _act12;
  }
  public void setAct12(Integer act12){
    _act12=act12;
  }
  public SysMenuAct gtAct12(){
    if(getAct12()==null)
      return null;
    return (SysMenuAct)get(SysMenuAct.class,getAct12());
  }
  public void stAct12(SysMenuAct act12){
    if(act12==null)
      setAct12(null);
    else
      setAct12(act12.getPkey());
  }

	// <<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
