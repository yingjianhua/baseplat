package irille.pub.view;

import irille.pub.html.ExtList;
import irille.pub.tb.Fld;

/**
 * 在ext产生器中产生支持多语言的字段
 * @author yingjianhua
 */
public class VFldLanguage<T extends VFldLanguage> extends VFld<VFldLanguage> {

	public VFldLanguage(Fld fld) {
		super(fld,"textfield","string");
	}

	@Override
	public T copyNew(Fld fld) {
		return (T) copy(new VFldLanguage(fld));
	}

	// FORM控件
	public void form(ExtList v, boolean isComp) {
		v.setTabs(false);
		v.add(XTYPE, "multilanguagefield").add(NAME, getBeanName());
//		v.add(XTYPE, "textfield").add(NAME, getBeanName());
		formNull(v, isComp);
		formEmpty(v, isComp);
		v.add(FIELD_LABEL, getName());
		addReadOnly(v);
		addHidden(v);
	}
	public String extModel() {
		return "{name : 'bean." + getCode() + "' , mapping : '" + getCode() + "' , type : 'string'}";
	}
	
	public String extRenderer() {
		//return "renderer : mvc.Tools.JsonRenderer(\"zh_CH\")";
		return "renderer : mvc.Tools.JsonRenderer()";
	}
	
	@Override
	public void renderer(ExtList v) {
		//v.addExp(RENDERER, "mvc.Tools.JsonRenderer(\"zh_CH\")");
		v.addExp(RENDERER, "mvc.Tools.JsonRenderer()");
	}

	public String extForm(String lnt, boolean isComp) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'textfield'," + lnt +
		/**/"	name : 'bean."+getCode()+"'," + lnt + extFormNull(lnt,isComp) + extFormEmpty(lnt, isComp) +
		/**/"	fieldLabel : '" + getName() + "'" + lnt +
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
		/**/"	name : '" + getCode() + "'" + lnt + extFormNull(lnt,isComp) + extFormEmpty(lnt, isComp) +
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
		return "editor: {}";
	}
	
	//------
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
}
