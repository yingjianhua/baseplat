/**
 * 
 */
package irille.core.prv;

import irille.core.sys.SysTable;
import irille.core.sys.Sys.OYn;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

public class PrvTranData extends BeanInt<PrvTranData> {
	private static final Log LOG = new Log(PrvTranData.class);
	public static final Tb TB = new Tb(PrvTranData.class, "资源数据").setAutoIncrement().addActList().addActUpd();
	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		NAME(SYS.NAME__40),
		TRAN(SYS.TABLE_ID,"交易"), 
		TRAN_CODE(SYS.CODE__100, "交易代码"),
		TRAN_NAME(SYS.NAME__40, "交易名称"),
		IS_FORM(SYS.YN, "是否为表单"),
		GRP(PrvTranGrp.fldOutKey().setName("所属资源控制组").setNull()),
		USER_FLD(SYS.CODE__40, "用户字段", true),
		USER_NAME(SYS.NAME__40, "用户字段名称", true),
		DEPT_FLD(SYS.CODE__40, "部门字段", true),
		DEPT_NAME(SYS.NAME__40, "部门字段名称", true),
		CELL_FLD(SYS.CODE__40, "核算单元字段", true),
		CELL_NAME(SYS.NAME__40, "核算单元字段名称", true),
		ORG_FLD(SYS.CODE__40, "机构字段", true),
		ORG_NAME(SYS.NAME__40, "机构字段名称", true),
		ROW_VERSION(SYS.ROW_VERSION),
				
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_TRAN_NAME = TB.addIndex("tranName",true, T.TRAN, T.NAME);
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
  private String _name;	// 名称  STR(40)
  private Integer _tran;	// 交易 <表主键:SysTable>  INT
  private String _tranCode;	// 交易代码  STR(100)
  private String _tranName;	// 交易名称  STR(40)
  private Byte _isForm;	// 是否为表单 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Integer _grp;	// 所属资源控制组 <表主键:PrvTranGrp>  INT<null>
  private String _userFld;	// 用户字段  STR(40)<null>
  private String _userName;	// 用户字段名称  STR(40)<null>
  private String _deptFld;	// 部门字段  STR(40)<null>
  private String _deptName;	// 部门字段名称  STR(40)<null>
  private String _cellFld;	// 核算单元字段  STR(40)<null>
  private String _cellName;	// 核算单元字段名称  STR(40)<null>
  private String _orgFld;	// 机构字段  STR(40)<null>
  private String _orgName;	// 机构字段名称  STR(40)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public PrvTranData init(){
		super.init();
    _name=null;	// 名称  STR(40)
    _tran=null;	// 交易 <表主键:SysTable>  INT
    _tranCode=null;	// 交易代码  STR(100)
    _tranName=null;	// 交易名称  STR(40)
    _isForm=OYn.DEFAULT.getLine().getKey();	// 是否为表单 <OYn>  BYTE
    _grp=null;	// 所属资源控制组 <表主键:PrvTranGrp>  INT
    _userFld=null;	// 用户字段  STR(40)
    _userName=null;	// 用户字段名称  STR(40)
    _deptFld=null;	// 部门字段  STR(40)
    _deptName=null;	// 部门字段名称  STR(40)
    _cellFld=null;	// 核算单元字段  STR(40)
    _cellName=null;	// 核算单元字段名称  STR(40)
    _orgFld=null;	// 机构字段  STR(40)
    _orgName=null;	// 机构字段名称  STR(40)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static PrvTranData loadUniqueTranName(boolean lockFlag,Integer tran,String name) {
    return (PrvTranData)loadUnique(T.IDX_TRAN_NAME,lockFlag,tran,name);
  }
  public static PrvTranData chkUniqueTranName(boolean lockFlag,Integer tran,String name) {
    return (PrvTranData)chkUnique(T.IDX_TRAN_NAME,lockFlag,tran,name);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Integer getTran(){
    return _tran;
  }
  public void setTran(Integer tran){
    _tran=tran;
  }
  public SysTable gtTran(){
    if(getTran()==null)
      return null;
    return (SysTable)get(SysTable.class,getTran());
  }
  public void stTran(SysTable tran){
    if(tran==null)
      setTran(null);
    else
      setTran(tran.getPkey());
  }
  public String getTranCode(){
    return _tranCode;
  }
  public void setTranCode(String tranCode){
    _tranCode=tranCode;
  }
  public String getTranName(){
    return _tranName;
  }
  public void setTranName(String tranName){
    _tranName=tranName;
  }
  public Byte getIsForm(){
    return _isForm;
  }
  public void setIsForm(Byte isForm){
    _isForm=isForm;
  }
  public Boolean gtIsForm(){
    return byteToBoolean(_isForm);
  }
  public void stIsForm(Boolean isForm){
    _isForm=booleanToByte(isForm);
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
  public String getUserFld(){
    return _userFld;
  }
  public void setUserFld(String userFld){
    _userFld=userFld;
  }
  public String getUserName(){
    return _userName;
  }
  public void setUserName(String userName){
    _userName=userName;
  }
  public String getDeptFld(){
    return _deptFld;
  }
  public void setDeptFld(String deptFld){
    _deptFld=deptFld;
  }
  public String getDeptName(){
    return _deptName;
  }
  public void setDeptName(String deptName){
    _deptName=deptName;
  }
  public String getCellFld(){
    return _cellFld;
  }
  public void setCellFld(String cellFld){
    _cellFld=cellFld;
  }
  public String getCellName(){
    return _cellName;
  }
  public void setCellName(String cellName){
    _cellName=cellName;
  }
  public String getOrgFld(){
    return _orgFld;
  }
  public void setOrgFld(String orgFld){
    _orgFld=orgFld;
  }
  public String getOrgName(){
    return _orgName;
  }
  public void setOrgName(String orgName){
    _orgName=orgName;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	// <<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
