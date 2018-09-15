package irille.pub.view;

import irille.pub.html.ExtList;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

public class VFldKeyInt<T extends VFldKeyInt> extends VFld<VFldKeyInt> {

	public VFldKeyInt(Fld fld) {
		super(fld, "numberfield", "int");
		setUseNull(true);
	}

	@Override
	public T copyNew(Fld fld) {
		return (T) copy(new VFldKeyInt(fld));
	}

	public String extModel() {
		return "{name : 'bean." + getCode() + "' , mapping : '" + getCode()
				+ "' , type : 'int', useNull : true}";
	}

	public String extSearch(String lnt) {
		String tmp = "" +
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
		String tmp = null;
		// 自增长主键不显示
		Tb tb = (Tb) getFld().getTb();
		if (tb.isAutoIncrement() || tb.isAutoLocal())
			tmp = "" +
			/**/"{" + lnt +
			/**/"	xtype : 'hiddenfield'," + lnt +
			/**/"	name : 'bean.pkey'" + lnt +
			/**/"}";
		else
			tmp = "" +
			/**/"{" + lnt +
			/**/"	xtype : 'numberfield'," + lnt +
			/**/"	name : 'bean.pkey'," + lnt +
			/**/"	fieldLabel : '" + getName() + "'," + lnt +
			/**/"	allowDecimals : false," + lnt +
			/**/"	afterLabelTextTpl : required," + lnt +
			/**/"	allowBlank : false," + lnt + extFormEmpty(lnt, isComp) +
			/**/"	readOnly : this.insFlag==false" + lnt +
			/**/"}";
		return tmp;
	}

	public String extEdit(String lnt) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'numberfield'," + lnt +
		/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
		/**/"	fieldLabel : '" + getName() + "'" + lnt +
		/**/"}";
		return tmp;
	}

	public String extEditList(String lnt) {
		return "editor: {allowBlank : false}";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.view.VFld#form(irille.pub.html.ExtList, boolean)
	 */
	@Override
	public void form(ExtList v, boolean isComp) {
		Tb tb = (Tb) getFld().getTb();
		if (tb.isAutoIncrement() || tb.isAutoLocal()) {
			v.add(XTYPE, "hiddenfield").add(NAME, getBeanName());
			return;
		}
		v.add(XTYPE, getXtype()).add(NAME, getBeanName());
		formNull(v, isComp);
		v.add(ALLOW_DECIMALS, false).addExp(AFTER_LABEL_TEXT_TPL, "required")
				.add(ALLOW_BLANK, false);
		formEmpty(v, isComp);
		v.add(FIELD_LABEL, getName());
		addReadOnly(v);
		addHidden(v);

		// // 自增长主键不显示
		// if (tb.isAutoIncrement() || tb.isAutoLocal())
		// tmp = "" +
		// /**/"{" + lnt +
		// /**/"	xtype : 'hiddenfield'," + lnt +
		// /**/"	name : 'bean.pkey'" + lnt +
		// /**/"}";
		// else
		// tmp = "" +
		// /**/"{" + lnt +
		// /**/"	xtype : 'numberfield'," + lnt +
		// /**/"	name : 'bean.pkey'," + lnt +
		// /**/"	fieldLabel : '" + getName() + "'," + lnt +
		// /**/"	allowDecimals : false," + lnt +
		// /**/"	afterLabelTextTpl : required," + lnt +
		// /**/"	allowBlank : false," + lnt + extFormEmpty(lnt, isComp) +
		// /**/"	readOnly : this.insFlag==false" + lnt +
		// /**/"}";
		// return tmp;
	}
	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#editList(irille.pub.html.ExtList)
	 */
	@Override
	public void editList(ExtList v) {
		v.add(ALLOW_BLANK,false);
//		return "editor: {allowBlank : false}";
	}

}
