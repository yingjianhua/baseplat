package irille.pub.view;

import irille.pub.html.ExtList;
import irille.pub.tb.Fld;
import irille.pub.tb.FldDec;
import irille.pub.tb.Fld.OOutType;

public class VFldDec<T extends VFldDec> extends VFld<VFldDec> {
	public static String TYPE_MILLION = "0.0001"; //万元显示
	public static String TYPE_MILLION_H = "0.00000001"; //亿元显示
	public static String TYPE_PERCENT = "100"; //百分比显示
	public static String TYPE_PERCENT_H = "1000"; //千分比显示
	public static String TYPE_JESTD = "JESTD"; //金额千分位显示
	public static String TYPE_JE = "JE"; //金额

	public VFldDec(Fld fld) {
		super(fld,"numberfield", "float");
		setUseNull(true);
		setAlign(OAlign.MIDDLE_RIGHT);
		if(typeIsMillionOrPercent())
			setXtype("pctfield");
	}

	@Override
	public T copyNew(Fld fld) {
		return (T) copy(new VFldDec(fld));
	}

	public String extModel() {
		String expend = "";
		if (getType() != null && getType()!=OOutType.JE
				&&getType()!=OOutType.JESTD)
			expend = ", convert:mvc.Tools.pctConvert(" + getType() + "), isPct:" + getType();
		return "{name : 'bean." + getCode() + "' , mapping : '" + getCode() + "' , type : 'float', useNull : true" + expend
		    + "}";
	}

	public String extRenderer() {
		if ( getType()==OOutType.JESTD)
			return "renderer : mvc.Tools.numberRenderer()";
		return super.extRenderer();
	}

	public String extAlign() {
		if (getType() != null)
			return "align:'right'";
		return super.extAlign();
	}

	public String extSearch(String lnt) {
		FldDec fld = (FldDec) getFld();
		String tmp = "";
		if (typeIsMillionOrPercent())
			tmp = "" +
			/**/"{" + lnt +
			/**/"	xtype : 'label'," + lnt +
			/**/"	text : '" + getName() + "：'" + lnt +
			/**/"},{" + lnt +
			/**/"	xtype : 'pctfield'," + lnt +
			/**/"	name : '" + getCode() + "'," + lnt +
			/**/"	decimalPrecision : " + fld.getScale() + "," + lnt +
			/**/"	pctValue : " + getType() + lnt +
			/**/"}";
			else
			tmp = "" +
			/**/"{" + lnt +
			/**/"	xtype : 'label'," + lnt +
			/**/"	text : '" + getName() + "：'" + lnt +
			/**/"},{" + lnt +
			/**/"	xtype : 'numberfield'," + lnt +
			/**/"	name : '" + getCode() + "'" + lnt +
			/**/"}";
		return tmp;
	}

	public String extForm(String lnt, boolean isComp) {
		FldDec fld = (FldDec) getFld();
		String tmp = "";
		if (typeIsMillionOrPercent())
			tmp = "" +
			/**/"{" + lnt +
			/**/"	xtype : 'pctfield'," + lnt +
			/**/"	name : 'bean." + getCode() + "'," + lnt +
			/**/"	fieldLabel : '" + getName() + "',decimalPrecision : " + fld.getScale() + "," + lnt + extFormNull(lnt,isComp) +extFormEmpty(lnt, isComp) +
			/**/"	pctValue: " + getType() + lnt +
			/**/"}";
			else
			tmp = "" +
			/**/"{" + lnt +
			/**/"	xtype : 'numberfield'," + lnt +
			/**/"	name : 'bean." + getCode() + "'," + lnt + extFormNull(lnt,isComp) +extFormEmpty(lnt, isComp) +
			/**/"	fieldLabel : '" + getName() + "',decimalPrecision : " + fld.getScale() + lnt +
			/**/"}";
		return tmp;
	}
	
	public String extWinSearch(String lnt, boolean isComp) {
		FldDec fld = (FldDec) getFld();
		String tmp = "";
		String temp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'label'," + lnt +
		/**/"	text : '" + getName() + "'" + lnt +
		/**/"},mvc.Tools.crtComboBase(false,{" +
		/**/"	sotre : numstore," + lnt +
		/**/"	name : 'optcht_'" + getCode() + "," + lnt +
		/**/"	value : 3," + lnt +
		/**/"	width : 80" + lnt +
		/**/"}),{" + lnt;
		if (typeIsMillionOrPercent())
			tmp = temp + lnt +
			/**/"	xtype : 'pctfield'," + lnt +
			/**/"	name : '" + getCode() + "'," + lnt +
			/**/"	decimalPrecision : " + fld.getScale() + "," + lnt + extFormNull(lnt,isComp) +extFormEmpty(lnt, isComp) +
			/**/"	pctValue: " + getType() + lnt +
			/**/"}";
			else
			tmp = temp + lnt +
			/**/"	xtype : 'numberfield'," + lnt +
			/**/"	name : '" + getCode() + "'," + lnt + extFormNull(lnt,isComp) +extFormEmpty(lnt, isComp) +
			/**/"	decimalPrecision : " + fld.getScale() + lnt +
			/**/"}";
		return tmp;
	}

	public String extEdit(String lnt) {
		String tmp = "";
		if (typeIsMillionOrPercent())
			tmp = "" +
			/**/"{" + lnt +
			/**/"	xtype : 'pctfield'," + lnt +
			/**/"	fieldLabel : '" + getName() + "'," + lnt +
			/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
			/**/"	pctValue: " + getType() + lnt +
			/**/"}";
			else
			tmp = "" +
			/**/"{" + lnt +
			/**/"	xtype : 'numberfield'," + lnt +
			/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
			/**/"	fieldLabel : '" + getName() + "'" + lnt +
			/**/"}";
		return tmp;
	}
	
	public String extEditList(String lnt) {
		FldDec fld = (FldDec) getFld();
		String tmp = "";
		if (typeIsMillionOrPercent())
			tmp = "" +
			/**/"editor: {" + lnt +
			/**/"	xtype : 'pctfield'," + lnt +
			/**/"	isCell:true," + lnt +
			/**/"	pctValue: " + getType() + "," + lnt +
			/**/"	decimalPrecision : "+fld.getScale() + lnt +
			/**/"}" + lnt.substring(0,lnt.length()-2);
		else
			tmp = "" + lnt +
			/**/"editor: {" + lnt +
			/**/"	xtype : 'numberfield'," + lnt +
			/**/"	decimalPrecision : "+fld.getScale() + lnt +
			/**/"}" + lnt.substring(0,lnt.length()-1);
		return tmp;
	}
	public boolean typeIsMillionOrPercent() {
		if (getType()==null)
			return false;
		switch (getType()) {
		case MILLION:
		case MILLION_H:
		case PERCENT:
		case PERCENT_H:
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFld#model(irille.pub.html.ExtList)
	 */
	@Override
	public void model(ExtList v) {
		super.model(v);
		if (getType() != null && getType() != OOutType.JE
				&& getType() != OOutType.JESTD)
			v.addExp(CONVERT, "mvc.Tools.pctConvert(" + getType() + ")").add(
					IS_PCT, getType());
	}

	@Override
	public void renderer(ExtList v) {
		if (getType() == OOutType.JESTD) {
			if (getFld().getScale() == 2)
				v.addExp(RENDERER, "mvc.Tools.numberRenderer()");
			else
				v.addExp(RENDERER, "mvc.Tools.numberRenderer("+getFld().getScale()+")");
		}else
			super.renderer(v);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFld#search(irille.pub.html.ExtList)
	 */
	@Override
	public void searchCode(ExtList v) {
		super.searchCode(v);
		FldDec fld = (FldDec) getFld();
		if (typeIsMillionOrPercent())
			v.add(DECIMAL_PRECISION, fld.getScale()).add(PCT_VALUE, getType());
		// tmp = "" +
		// /**/"{" + lnt +
		// /**/"	xtype : 'label'," + lnt +
		// /**/"	text : '" + getName() + "：'" + lnt +
		// /**/"},{" + lnt +
		// /**/"	xtype : 'pctfield'," + lnt +
		// /**/"	name : '" + getCode() + "'," + lnt +
		// /**/"	decimalPrecision : " + fld.getScale() + "," + lnt +
		// /**/"	pctValue : " + getType() + lnt +
		// /**/"}";
//		else
//			v.add(XTYPE, "numberfield").add(NAME, getCode());
		// tmp = "" +
		// /**/"{" + lnt +
		// /**/"	xtype : 'label'," + lnt +
		// /**/"	text : '" + getName() + "：'" + lnt +
		// /**/"},{" + lnt +
		// /**/"	xtype : 'numberfield'," + lnt +
		// /**/"	name : '" + getCode() + "'" + lnt +
		// /**/"}";

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFld#form(irille.pub.html.ExtList, boolean)
	 */
	@Override
	public void form(ExtList v, boolean isComp) {
		super.form(v, isComp);
		FldDec fld = (FldDec) getFld();
		if (typeIsMillionOrPercent()) {
			v.add(DECIMAL_PRECISION, fld.getScale());
			v.add(PCT_VALUE, getType());
			// tmp = "" +
			// /**/"{" + lnt +
			// /**/"	xtype : 'pctfield'," + lnt +
			// /**/"	name : 'bean." + getCode() + "'," + lnt +
			// /**/"	fieldLabel : '" + getName() + "',decimalPrecision : " +
			// fld.getScale() + ","
			// + lnt + extFormNull(lnt,isComp) +extFormEmpty(lnt, isComp) +
			// /**/"	pctValue: " + getType() + lnt +
			// /**/"}";
		} else {
			v.add(DECIMAL_PRECISION, fld.getScale());
			// tmp = "" +
			// /**/"{" + lnt +
			// /**/"	xtype : 'numberfield'," + lnt +
			// /**/"	name : 'bean." + getCode() + "'," + lnt +
			// extFormNull(lnt,isComp) +extFormEmpty(lnt, isComp) +
			// /**/"	fieldLabel : '" + getName() + "',decimalPrecision : " +
			// fld.getScale() + lnt +
			// /**/"}";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFld#edit(irille.pub.html.ExtList)
	 */
	@Override
	public void edit(ExtList v) {
		super.edit(v);
		if (typeIsMillionOrPercent())
			v.add(PCT_VALUE, getType());
		// tmp = "" +
		// /**/"{" + lnt +
		// /**/"	xtype : 'pctfield'," + lnt +
		// /**/"	fieldLabel : '" + getName() + "'," + lnt +
		// /**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
		// /**/"	pctValue: " + getType() + lnt +
		// /**/"}";
//		else
//			v.add(XTYPE, "numberfield")
//			.add(FIELD_LABEL, getName())
//			.addExp(VALUE,
//					"this.record.get('" + getBeanName() + getCode() + ")'");
//			tmp = "" +
//			/**/"{" + lnt +
//			/**/"	xtype : 'numberfield'," + lnt +
//			/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
//			/**/"	fieldLabel : '" + getName() + "'" + lnt +
//			/**/"}";
	}

	/* (non-Javadoc)
	 * @see irille.pub.html.ExtFld#addEditList(irille.pub.html.ExtList)
	 */
	@Override
	public void editList(ExtList v) {
		super.editList(v);
		FldDec fld = (FldDec) getFld();
		if (typeIsMillionOrPercent())
			v.add(IS_CELL,true)
			.add(DECIMAL_PRECISION, fld.getScale())
			.add(PCT_VALUE, getType());
//			tmp = "" +
//			/**/"editor: {" + lnt +
//			/**/"	xtype : 'pctfield'," + lnt +
//			/**/"	isCell:true," + lnt +
//			/**/"	pctValue: " + getType() + "," + lnt +
//			/**/"	decimalPrecision : "+fld.getScale() + lnt +
//			/**/"}" + lnt.substring(0,lnt.length()-2);
		else
			v.add(DECIMAL_PRECISION, fld.getScale());
//			tmp = "" + lnt +
//			/**/"editor: {" + lnt +
//			/**/"	xtype : 'numberfield'," + lnt +
//			/**/"	decimalPrecision : "+fld.getScale() + lnt +
//			/**/"}" + lnt.substring(0,lnt.length()-1);
	}
	
	@Override
	protected void winSearchOptcht(ExtList l) {
		super.winSearchOptcht(l);
		l.add(VALUE, 3)
		.addExp(STORE, "numstore");
	}
	
	@Override
	protected void winSearchForm(ExtList v, boolean isComp) {
		super.winSearchForm(v, isComp);
		FldDec fld = (FldDec) getFld();
		if (typeIsMillionOrPercent()) {
			v.add(DECIMAL_PRECISION, fld.getScale());
			v.add(PCT_VALUE, getType());
		} else {
			v.add(DECIMAL_PRECISION, fld.getScale());
		}
	}
}

