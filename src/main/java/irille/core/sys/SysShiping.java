package irille.core.sys;


import irille.core.sys.Sys.OShipingMode;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanLong;
import irille.pub.bean.IForm;
import irille.pub.tb.Fld;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.util.Date;

/**
 * 发货运输信息
 * 
 * @author whx
 */
public class SysShiping extends BeanLong<SysShiping> {
	public static final Tb TB = new Tb(SysShiping.class, "发货信息");

	public enum T implements IEnumFld {//@formatter:off
		FORM(SYS.PKEY_FORM,"主单据"),
		SHIPING_FORM(SYS.FORM_CODE,"运输单", true),
		TIME_SHIP_PLAN(SYS.DATE_TIME, "计划发货时间", true), //计划发货日期
		TIME_SHIP(SYS.DATE_TIME, "实际发货时间", true), //实际发货时间
		TIME_ARR_PLAN(SYS.DATE_TIME, "预计到货时间", true),
		TIME_ARR(SYS.DATE_TIME, "实际到货时间", true),
		RECEIVING_INFO(CmbMsgReceiving.fldFlds()),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		NAME(TB.get("name")),	//名称
		ADDR(TB.get("addr")),	//地址
		MOBILE(TB.get("mobile")),	//手机号码
		TEL(TB.get("tel")),	//电话
		REM(TB.get("rem")),	//备注
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
		private T(Fld fld) {_fld=TB.add(fld, this); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.SHIPING_FORM.getFld().setNull();
	}
	public static Fld fldFlds() {
		return Tb.crtCmbFlds(TB);
	}
	public static Fld fldCmb(String code,String name) {
		return TB.crtCmb(code, name, TB);
	}
	
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private String _shipingForm;	// 运输单  STR(40)<null>
  private Date _timeShipPlan;	// 计划发货时间  TIME<null>
  private Date _timeShip;	// 实际发货时间  TIME<null>
  private Date _timeArrPlan;	// 预计到货时间  TIME<null>
  private Date _timeArr;	// 实际到货时间  TIME<null>
  private String _name;	// 名称  STR(100)<null>
  private String _addr;	// 地址  STR(200)<null>
  private String _mobile;	// 手机号码  STR(20)<null>
  private String _tel;	// 电话  STR(20)<null>
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysShiping init(){
		super.init();
    _shipingForm=null;	// 运输单  STR(40)
    _timeShipPlan=null;	// 计划发货时间  TIME
    _timeShip=null;	// 实际发货时间  TIME
    _timeArrPlan=null;	// 预计到货时间  TIME
    _timeArr=null;	// 实际到货时间  TIME
    _name=null;	// 名称  STR(100)
    _addr=null;	// 地址  STR(200)
    _mobile=null;	// 手机号码  STR(20)
    _tel=null;	// 电话  STR(20)
    _rem=null;	// 备注  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  //取一对多表对象: IForm
  public IForm gtForm(){
    return (IForm)gtLongTbObj(getPkey());
  }
  public void stForm(IForm form){
      setPkey(form.gtLongPkey());
  }
  public String getShipingForm(){
    return _shipingForm;
  }
  public void setShipingForm(String shipingForm){
    _shipingForm=shipingForm;
  }
  public Date getTimeShipPlan(){
    return _timeShipPlan;
  }
  public void setTimeShipPlan(Date timeShipPlan){
    _timeShipPlan=timeShipPlan;
  }
  public Date getTimeShip(){
    return _timeShip;
  }
  public void setTimeShip(Date timeShip){
    _timeShip=timeShip;
  }
  public Date getTimeArrPlan(){
    return _timeArrPlan;
  }
  public void setTimeArrPlan(Date timeArrPlan){
    _timeArrPlan=timeArrPlan;
  }
  public Date getTimeArr(){
    return _timeArr;
  }
  public void setTimeArr(Date timeArr){
    _timeArr=timeArr;
  }
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
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
