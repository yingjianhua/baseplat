package irille.pub.view;

import irille.pub.html.ExtFunCall;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.ExtList;
import irille.pub.tb.Fld;
import irille.pub.tb.OptCust;

public class VFldOptCust<T extends VFldOptCust> extends VFld<VFldOptCust> {

	public VFldOptCust(Fld fld) {
		super(fld,"combocust","string");
	}

	@Override
	public T copyNew(Fld fld) {
		return (T) copy(new VFldOptCust(fld));
	}

	@Override
	public String extModel() {
		return "{name : 'bean." + getCode() + "' , mapping : '" + getCode() + "' , type : 'string', optCust : true}";
	}

	public String extRenderer() {
		return "renderer : mvc.Tools.beanRenderer()";
	}

	public String extSearch(String lnt) {
		OptCust opt = (OptCust) getOpt();
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'label'," + lnt +
		/**/"	text : '" + getName() + "：'" + lnt +
		/**/"},{" + lnt +
		/**/"	xtype : 'combocust'," + lnt +
		/**/"	name : '" + getCode() + "'," + lnt +
		/**/"	mode : 'local'," + lnt +
		/**/"	valueField : 'value'," + lnt +
		/**/"	triggerAction : 'all'," + lnt +
		/**/"	typeAhead : true," + lnt +
		/**/"	editable : false," + lnt +
		/**/"	emptyText : form_empty_text," + lnt +
		/**/"	store : Ext.create('mvc.comboCust." + opt.getCode() + "')" + lnt +
		/**/"}";
		return tmp;
	}

	public String extForm(String lnt, boolean isComp) {
		OptCust opt = (OptCust) getOpt();
		String tmp = "" +
		/**/"mvc.Tools.crtComboCust(" + (isComp?true:isNull()) + ",{" + lnt +
		/**/"	name : 'bean." + getCode() + "'," + lnt +
		/**/"	fieldLabel : '" + getName() + "'," + lnt + extFormEmpty(lnt, isComp) +
		/**/"	store : Ext.create('mvc.comboCust." + opt.getCode() + "')" + lnt +
		/**/"})";
		return tmp;
	}
	
	public String extWinSearch(String lnt, boolean isComp) {
		OptCust opt = (OptCust) getOpt();
		String tmp = "{" + lnt +
		/**/"	xtype : 'label'," + lnt +
		/**/"	text : '" + getName() + "'" + lnt +
		/**/"},mvc.Tools.crtComboBase(false,{" +
		/**/"	sotre : numstore," + lnt +
		/**/"	name : 'optcht_'" + getCode() + "," + lnt +
		/**/"	value : 3," + lnt +
		/**/"	width : 80" + lnt +
		/**/"}),mvc.Tools.crtComboCust(" + (isComp?true:isNull()) + ",{" + lnt +
		/**/"	name : '" + getCode() + "'," + lnt +
		/**/"	fieldLabel : '" + getName() + "'," + lnt + extFormEmpty(lnt, isComp) +
		/**/"	store : Ext.create('mvc.comboCust." + opt.getCode() + "')" + lnt +
		/**/"})";
		return tmp;
	}

	public String extEdit(String lnt) {
		OptCust opt = (OptCust) getOpt();
		String tmp = "" +
		/**/"mvc.Tools.crtComboCust(true,{" + lnt +
		/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
		/**/"	fieldLabel : '" + getName() + "'," + lnt +
		/**/"	store : Ext.create('mvc.comboCust." + opt.getCode() + "')" + lnt +
		/**/"})";
		return tmp;
	}

	public String extEditList(String lnt) {
		OptCust opt = (OptCust) getOpt();
		String tmp = "" + lnt +
		/**/"editor: mvc.Tools.crtComboCustCell(true,{" + lnt +
		/**/"	store : Ext.create('mvc.comboCust." + opt.getCode() + "')" + lnt +
		/**/"})" + lnt.substring(0,lnt.length()-1);
		return tmp;
	}
//-------------
	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#model(irille.pub.html.ExtList)
	 */
	@Override
	public void model(ExtList v) {
		super.model(v);
		v.add("optCust",true);
//		return "{name : 'bean." + getCode() + "' , mapping : '" + getCode() + "' , type : 'string', optCust : true}";
	}

	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#renderer(irille.pub.html.ExtList)
	 */
	@Override
	public void renderer(ExtList v) {
		v.addExp(RENDERER, "mvc.Tools.beanRenderer()");	
//		return "renderer : mvc.Tools.beanRenderer()";
	}

	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#searchCode(irille.pub.html.ExtList)
	 */
	@Override
	public void searchCode(ExtList v) {
		super.searchCode(v);
		OptCust opt = (OptCust) getOpt();
		v.add(MODE,	"local").add(VALUE_FIELD, "value").add(TRIGGER_ACTION, "all")
			.add(TYPE_AHEAD,	true).add(EDITABLE, false).addExp(EMPTY_TEXT,"form_empty_text")
			.addExp(STORE, "Ext.create('mvc.comboCust." + opt.getCode() + "')");
//		String tmp = "" +
//		/**/"{" + lnt +
//		/**/"	xtype : 'label'," + lnt +
//		/**/"	text : '" + getName() + "：'" + lnt +
//		/**/"},{" + lnt +
//		/**/"	xtype : 'combocust'," + lnt +
//		/**/"	name : '" + getCode() + "'," + lnt +
//		/**/"	mode : 'local'," + lnt +
//		/**/"	valueField : 'value'," + lnt +
//		/**/"	triggerAction : 'all'," + lnt +
//		/**/"	typeAhead : true," + lnt +
//		/**/"	editable : false," + lnt +
//		/**/"	emptyText : form_empty_text," + lnt +
//		/**/"	store : Ext.create('mvc.comboCust." + opt.getCode() + "')" + lnt +
//		/**/"}";
//		return tmp;
	}

	@Override
	public void form(ExtList v, boolean isComp) {
		OptCust opt = (OptCust) getOpt();
		v.setCloseStr(null); //不输出“{}”
		ExtList l=new ExtList(v);
		v=v.add(new ExtFunDefine(v,"mvc.Tools.crtComboCust",(isComp?true:isNull()),l));
		l.add(NAME, getBeanName())
				.add(FIELD_LABEL, getName());
		formEmpty(v, isComp);
		l.addExp(STORE,"Ext.create('mvc.comboCust." + opt.getCode() + "')");
//	
//		String tmp = "" +
//		/**/"mvc.Tools.crtComboCust(" + (isComp?true:isNull()) + ",{" + lnt +
//		/**/"	name : 'bean." + getCode() + "'," + lnt +
//		/**/"	fieldLabel : '" + getName() + "'," + lnt + extFormEmpty(lnt, isComp) +
//		/**/"	store : Ext.create('mvc.comboCust." + opt.getCode() + "')" + lnt +
//		/**/"})";
//		return tmp;
	}

	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#edit(irille.pub.html.ExtList)
	 */
	@Override
	public void edit(ExtList v) {
		OptCust opt = (OptCust) getOpt();
		v.setCloseStr(null); //不输出“{}”
		ExtList l=new ExtList(v);
		v=v.add(new ExtFunDefine(v,"mvc.Tools.crtComboCust",true,l));
		l.addExp(VALUE,"this.record.get('"+getBeanName() + "')")
				.add(FIELD_LABEL, getName());
		l.addExp(STORE,"Ext.create('mvc.comboCust." +  opt.getCode() + "')");

//		String tmp = "" +
//		/**/"mvc.Tools.crtComboCust(true,{" + lnt +
//		/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
//		/**/"	fieldLabel : '" + getName() + "'," + lnt +
//		/**/"	store : Ext.create('mvc.comboCust." + opt.getCode() + "')" + lnt +
//		/**/"})";
//		return tmp;
	}
	@Override
	public void editList(ExtList v) {
		OptCust opt = (OptCust) getOpt();
		v.setCloseStr(null); //不输出“{}”
		ExtList l=new ExtList(v);
		v=v.add(new ExtFunCall(v,"mvc.Tools.crtComboCustCell",true,l));
		l.addExp(STORE,"Ext.create('mvc.comboCust." + opt.getCode() + "')");
//
//		String tmp = "" + lnt +
//		/**/"editor: mvc.Tools.crtComboCustCell(true,{" + lnt +
//		/**/"	store : Ext.create('mvc.comboCust." + opt.getCode() + "')" + lnt +
//		/**/"})" + lnt.substring(0,lnt.length()-1);
//		return tmp;
	}

	@Override
	protected void winSearchOptcht(ExtList l) {
		super.winSearchOptcht(l);
		l.add(VALUE, 3)
		.addExp(STORE, "numstore");
	}
	
	@Override
	protected void winSearchForm(ExtList v, boolean isComp) {
		v.setTabs(false);
		OptCust opt = (OptCust) getOpt();
		v.setCloseStr(null); //不输出“{}”
		ExtList l=new ExtList(v);
		v=v.add(new ExtFunDefine(v,"mvc.Tools.crtComboCust",(isComp?true:isNull()),l));
		l.add(NAME, getCode());
		formEmpty(v, isComp);
		l.addExp(STORE,"Ext.create('mvc.comboCust." + opt.getCode() + "')");
	}
}
