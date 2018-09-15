package irille.pub.view;

import irille.pub.html.ExtList;
import irille.pub.tb.Fld;

public class VFldNum<T extends VFldNum> extends VFld<VFldNum> {

	public VFldNum(Fld fld) {
		super(fld, "numberfield", "int");
		setUseNull(true);
	}

	@Override
	public T copyNew(Fld fld) {
		return (T) copy(new VFldNum(fld));
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
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'numberfield'," + lnt +
		/**/"	name : 'bean." + getCode() + "'," + lnt +
		/**/"	fieldLabel : '" + getName() + "'," + lnt + extFormNull(lnt, isComp)
				+ extFormEmpty(lnt, isComp) +
				/**/"	allowDecimals : false" + lnt +
				/**/"}";
		return tmp;
	}
	
	public String extWinSearch(String lnt, boolean isComp) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'label'," + lnt +
		/**/"	text : '" + getName() + "'" + lnt +
		/**/"},mvc.Tools.crtComboBase(false,{" +
		/**/"	sotre : numstore," + lnt +
		/**/"	name : 'optcht_'" + getCode() + "," + lnt +
		/**/"	value : 3," + lnt +
		/**/"	width : 80" + lnt +
		/**/"}),{" + lnt +
		/**/"	xtype : 'numberfield'," + lnt +
		/**/"	name : '" + getCode() + "'" + lnt + extFormNull(lnt,isComp) + extFormEmpty(lnt, isComp) +
		/**/"	allowDecimals : false" + lnt +
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
		String tmp = "" + lnt +
		/**/"editor:{" + lnt +
		/**/"	xtype : 'numberfield'," + lnt +
		/**/"	allowDecimals : false" + lnt +
		/**/"}" + lnt.substring(0, lnt.length() - 1);
		return tmp;
	}

	// --------

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.view.VFld#form(irille.pub.html.ExtList, boolean)
	 */
	@Override
	public void form(ExtList v, boolean isComp) {
		super.form(v, isComp);
		v.add(ALLOW_DECIMALS, false);
		// String tmp = "" +
		// /**/"{" + lnt +
		// /**/"	xtype : 'numberfield'," + lnt +
		// /**/"	name : 'bean." + getCode() + "'," + lnt +
		// /**/"	fieldLabel : '" + getName() + "'," + lnt + extFormNull(lnt,isComp)
		// + extFormEmpty(lnt, isComp) +
		// /**/"	allowDecimals : false" + lnt +
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
		super.editList(v);
		v.add(ALLOW_DECIMALS, false);

		// /**/"editor:{" + lnt +
		// /**/"	xtype : 'numberfield'," + lnt +
		// /**/"	allowDecimals : false" + lnt +
		// /**/"}" + lnt.substring(0,lnt.length()-1);
		// return tmp;
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
		v.add(ALLOW_DECIMALS, false);
	}
}
