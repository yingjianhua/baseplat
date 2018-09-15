package irille.gl.gs;

import irille.pub.bean.BeanLong;
import irille.pub.bean.IForm;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.math.BigDecimal;

public class ZGsBatchLine extends BeanLong<ZGsBatchLine> {
	public static final Tb TB = new Tb(ZGsBatchLine.class, "存货批次明细", "存货批次明细").setAutoIncrement();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		QTY(SYS.QTY),
		ORIG_FORM(SYS.ORIG_FORM__CODE),
	  //>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		ORIG_FORM_NUM(TB.get("origFormNum")),
		;
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		// 索引
		//public static final Index IDX_CODE = TB.addIndex("code", true,CODE);
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

	//>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
	//实例变量定义-----------------------------------------
	private Long _pkey; // 编号  LONG
	private BigDecimal _qty; // 数量  DEC(14,4)
	private Long _origForm; // 源单据  LONG
	private String _origFormNum; // 源单据号  STR(40)

	@Override
	public ZGsBatchLine init() {
		super.init();
		_qty = ZERO; // 数量  DEC(14,4)
		_origForm = (long) 0; // 源单据  LONG
		_origFormNum = null; // 源单据号  STR(40)
		return this;
	}

	//方法----------------------------------------------
	public Long getPkey() {
		return _pkey;
	}

	public void setPkey(Long pkey) {
		_pkey = pkey;
	}

	public BigDecimal getQty() {
		return _qty;
	}

	public void setQty(BigDecimal qty) {
		_qty = qty;
	}

	public Long getOrigForm() {
		return _origForm;
	}

	public void setOrigForm(Long origForm) {
		_origForm = origForm;
	}

	//外部主键对象: IForm
	public IForm gtOrigForm() {
		return (IForm) gtLongTbObj(getOrigForm());
	}

	public void stOrigForm(IForm origForm) {
		setOrigForm(origForm.getPkey());
	}

	public String getOrigFormNum() {
		return _origFormNum;
	}

	public void setOrigFormNum(String origFormNum) {
		_origFormNum = origFormNum;
	}

}
