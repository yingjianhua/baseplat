package irille.core.sys;


import irille.pss.lgt.LgtShipMode;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbCmb;

import java.util.Date;

/**
 * 发货运输信息
 * 
 * @author whx
 */
public class CmbMsgShip extends BeanInt<CmbMsgShip> {
	public static final Tb TB = new TbCmb(CmbMsgShip.class, "发货运输信息");

	public enum T implements IEnumFld {//@formatter:off
		PACK_DEMAND(SYS.PACK_DEMAND, true), //包装要求
		SHIP_MODE(LgtShipMode.fldOutKey().setNull()), //运输方式
		TIME_SHIP_PLAN(SYS.DATE_TIME, "计划发货日期", true), //计划发货日期
		TIME_SHIP(SYS.DATE_TIME, "实际发货日期", true), //实际发货时间
		TIME_ARR_PLAN(SYS.DATE_TIME, "预计到货时间", true),
		TIME_ARR(SYS.DATE_TIME, "实际到货时间", true),
		//>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		;
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
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
		T.PACK_DEMAND.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
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
  private String _packDemand;	// 包装要求  STR(200)<null>
  private Integer _shipMode;	// 运输方式 <表主键:LgtShipMode>  INT<null>
  private Date _timeShipPlan;	// 计划发货日期  TIME<null>
  private Date _timeShip;	// 实际发货日期  TIME<null>
  private Date _timeArrPlan;	// 预计到货时间  TIME<null>
  private Date _timeArr;	// 实际到货时间  TIME<null>

	@Override
  public CmbMsgShip init(){
		super.init();
    _packDemand=null;	// 包装要求  STR(200)
    _shipMode=null;	// 运输方式 <表主键:LgtShipMode>  INT
    _timeShipPlan=null;	// 计划发货日期  TIME
    _timeShip=null;	// 实际发货日期  TIME
    _timeArrPlan=null;	// 预计到货时间  TIME
    _timeArr=null;	// 实际到货时间  TIME
    return this;
  }

  //方法----------------------------------------------
  public String getPackDemand(){
    return _packDemand;
  }
  public void setPackDemand(String packDemand){
    _packDemand=packDemand;
  }
  public Integer getShipMode(){
    return _shipMode;
  }
  public void setShipMode(Integer shipMode){
    _shipMode=shipMode;
  }
  public LgtShipMode gtShipMode(){
    if(getShipMode()==null)
      return null;
    return (LgtShipMode)get(LgtShipMode.class,getShipMode());
  }
  public void stShipMode(LgtShipMode shipMode){
    if(shipMode==null)
      setShipMode(null);
    else
      setShipMode(shipMode.getPkey());
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

}
