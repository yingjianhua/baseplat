package irille.pub.view;

import irille.pub.html.ExtList;
import irille.pub.tb.Fld;

public class VFldKeyStr<T extends VFldKeyStr> extends VFld<VFldKeyStr> {

	public VFldKeyStr(Fld fld) {
		super(fld, "textfield", "string");
	}

	@Override
	public T copyNew(Fld fld) {
		return (T) copy(new VFldKeyStr(fld));
	}

	public String extModel() {
		return "{name : 'bean." + getCode() + "' , mapping : '" + getCode()
				+ "' , type : 'string'}";
	}

	public String extForm(String lnt, boolean isComp) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'textfield'," + lnt +
		/**/"	name : 'bean.pkey'," + lnt +
		/**/"	fieldLabel : '" + getName() + "'," + lnt +
		/**/"	afterLabelTextTpl : required," + lnt +
		/**/"	allowBlank : false," + lnt + extFormEmpty(lnt, isComp) +
		/**/"	readOnly : this.insFlag==false" + lnt +
		/**/"}";
		return tmp;
	}

	public String extEdit(String lnt) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'textfield'," + lnt +
		/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
		/**/"	fieldLabel : '" + getName() + "'" + lnt +
		/**/"}";
		return tmp;
	}

	public String extEditList(String lnt) {
		return "editor: {allowBlank : false}";
	}

	// ------
	@Override
	public void form(ExtList v, boolean isComp) {
		// Tb tb = (Tb) getFld().getTb();
		v.add(XTYPE, getXtype()).add(NAME, getBeanName());
		formNull(v, isComp);
		v.addExp(AFTER_LABEL_TEXT_TPL, "required").add(ALLOW_BLANK, false);
		formEmpty(v, isComp);
		v.add(FIELD_LABEL, getName());
		addReadOnly(v);
		addHidden(v);
		
		
		// /**/"{" + lnt +
		// /**/"	xtype : 'textfield'," + lnt +
		// /**/"	name : 'bean.pkey'," + lnt +
		// /**/"	fieldLabel : '" + getName() + "'," + lnt +
		// /**/"	afterLabelTextTpl : required," + lnt +
		// /**/"	allowBlank : false," + lnt + extFormEmpty(lnt, isComp) +
		// /**/"	readOnly : this.insFlag==false" + lnt +
		// /**/"}";
		// return tmp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.view.VFld#editList(irille.pub.html.ExtList)
	 */
	@Override
	public void editList(ExtList v) {
		v.add(ALLOW_BLANK, false);
		// return "editor: {allowBlank : false}";
	}
}
