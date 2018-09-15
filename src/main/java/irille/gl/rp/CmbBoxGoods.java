package irille.gl.rp;

import irille.pub.bean.BeanInt;
import irille.pub.bean.IBeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbCmb;

/**
 * 起止日期
 * 
 * @author whx
 */
public class CmbBoxGoods extends BeanInt<CmbBoxGoods> {
	public static final TbCmb TB = new TbCmb(CmbBoxGoods.class, "可交接物品");

	public enum T implements IEnumFld {//@formatter:off
		BOX_GOODS(SYS.BOX_GOODS), //可为印章、发票、支票等对象的主键；如为现金则是出纳现金账户
		NAME(SYS.NAME__100,"物品名称"),
		
		//>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		;
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
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


  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //实例变量定义-----------------------------------------
  private Long _boxGoods;	// 物品  LONG
  private String _name;	// 物品名称  STR(100)

	@Override
  public CmbBoxGoods init(){
		super.init();
    _boxGoods=(long)0;	// 物品  LONG
    _name=null;	// 物品名称  STR(100)
    return this;
  }

  //方法----------------------------------------------
  public Long getBoxGoods(){
    return _boxGoods;
  }
  public void setBoxGoods(Long boxGoods){
    _boxGoods=boxGoods;
  }
  //外部主键对象: IBeanLong
  public IBeanLong gtBoxGoods(){
    return (IBeanLong)gtLongTbObj(getBoxGoods());
  }
  public void stBoxGoods(IBeanLong boxGoods){
      setBoxGoods(boxGoods.getPkey());
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }

}
