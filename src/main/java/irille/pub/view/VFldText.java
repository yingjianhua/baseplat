package irille.pub.view;

import irille.pub.html.ExtList;
import irille.pub.tb.Fld;

public class VFldText<T extends VFldText> extends VFld<VFldText> {

	public VFldText(Fld fld) {
		super(fld,"textareafield","string");
	}

	@Override
	public T copyNew(Fld fld) {
		return (T) copy(new VFldText(fld));
	}

	public String extModel() {
		return "{name : 'bean." + getCode() + "' , mapping : '" + getCode() + "' , type : 'string'},";
	}

	public String extForm(String lnt, boolean isComp) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"xtype : 'textareafield'," + lnt +
		/**/"name : 'bean." + getCode() + "'," + lnt + extFormNull(lnt,isComp) + extFormEmpty(lnt, isComp) +
		/**/"fieldLabel : '" + getName() + "'" + lnt +
		/**/"}";
		return tmp;
	}
	
	public String extWinSearch(String lnt, boolean isComp) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'label'," + lnt +
		/**/"	text : '" + getName() + "'" + lnt +
		/**/"},mvc.Tools.crtComboBase(false,{" +
		/**/"	sotre : strstore," + lnt +
		/**/"	name : 'optcht_'" + getCode() + "," + lnt +
		/**/"	value : 1," + lnt +
		/**/"	width : 80" + lnt +
		/**/"}),{" + lnt +
		/**/"	xtype : 'textfield'," + lnt +
		/**/"	name : '" + getCode() + "'," + lnt + extFormNull(lnt,isComp) + extFormEmpty(lnt, isComp) +
		/**/"}";
		return tmp;
	}
	
	public String extEdit(String lnt) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'textareafield'," + lnt +
		/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
		/**/"	fieldLabel : '" + getName() + "'" + lnt +
		/**/"}";
		return tmp;
	}
	
	public String extEditList(String lnt) {
		return "editor: {}";
	}
	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#editList(irille.pub.html.ExtList)
	 */
	@Override
	public void editList(ExtList v) {
	}
	
	@Override
	protected void winSearchOptcht(ExtList l) {
		super.winSearchOptcht(l);
		l.add(VALUE, 1)
		.addExp(STORE, "strstore");
	}

	@Override
	protected void winSearchForm(ExtList v, boolean isComp) {
		// TODO Auto-generated method stub
		String xtype = getXtype();
		setXtype("textfield");
		super.winSearchForm(v, isComp);
		setXtype(xtype);
	}
}
