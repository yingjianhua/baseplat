package irille.gl.rp;

import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

public class RpHandoverLine extends BeanLong<RpHandoverLine> {
	private static final Log LOG = new Log(RpHandoverLine.class);

	public static final Tb TB = new Tb(RpHandoverLine.class, "物品交接明细")
			.addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		CMB_BOX_GOODS(CmbBoxGoods.fldFlds()), //可为印章、发票、支票及工作箱等对象的主键；如为现金则是出纳现金账户
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		BOX_GOODS(TB.get("boxGoods")),	//物品
		NAME(TB.get("name")),	//物品名称
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		private Fld _fld;

		private T(Class clazz, String name, boolean... isnull) {
			_fld = TB.addOutKey(clazz, this, name, isnull);
		}

		private T(IEnumFld fld, boolean... isnull) {
			this(fld, null, isnull);
		}

		private T(IEnumFld fld, String name, boolean... null1) {
			_fld = TB.add(fld, this, name, null1);
		}

		private T(IEnumFld fld, String name, int strLen) {
			_fld = TB.add(fld, this, name, strLen);
		}

		private T(Fld fld) {
			_fld = TB.add(fld);
		}

		public Fld getFld() {
			return _fld;
		}
	}

	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _boxGoods;	// 物品  LONG
  private String _name;	// 物品名称  STR(100)
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public RpHandoverLine init(){
		super.init();
    _boxGoods=null;	// 物品  LONG
    _name=null;	// 物品名称  STR(100)
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
  public Long getBoxGoods(){
    return _boxGoods;
  }
  public void setBoxGoods(Long boxGoods){
    _boxGoods=boxGoods;
  }
  //外部主键对象: IBeanLong
  public Bean gtBoxGoods(){
    return (Bean)gtLongTbObj(getBoxGoods());
  }
  public void stBoxGoods(Bean boxGoods){
      setBoxGoods(boxGoods.gtLongPkey());
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
