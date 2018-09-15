package irille.pub.view;

import irille.pub.html.ExtFunDefine;
import irille.pub.html.ExtList;
import irille.pub.tb.Fld;

public class VFldTime<T extends VFldTime> extends VFld<VFldTime> {
	private static final String FMT="Y-m-d H:i:s";
	public VFldTime(Fld fld) {
		super(fld,"datefield","date");
	}

	@Override
	public T copyNew(Fld fld) {
		return (T) copy(new VFldTime(fld));
	}

	public String extModel() {
		return "{name : 'bean." + getCode() + "' , mapping : '" + getCode()
		    + "' , type : 'date' ,dateFormat : 'Y-m-d H:i:s'}";
	}

	public String extRenderer() {
		return "renderer : Ext.util.Format.dateRenderer('Y-m-d H:i:s')";
	}

	public String extSearch(String lnt) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'label'," + lnt +
		/**/"	text : '" + getName() + "：'" + lnt +
		/**/"},{" + lnt + "\t" +
		/**/"xtype : 'datefield'," + lnt +
		/**/"	name : '" + getCode() + "'," + lnt +
		/**/"	format : 'Y-m-d'" + lnt +
		/**/"}";
		return tmp;
	}

	public String extForm(String lnt, boolean isComp) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'datefield'," + lnt +
		/**/"	name : 'bean." + getCode() + "'," + lnt +
		/**/"	fieldLabel : '" + getName() + "'," + lnt + extFormNull(lnt, isComp) + extFormEmpty(lnt, isComp) +
		/**/"	format : 'Y-m-d H:i:s'" + lnt +
		/**/"}";
		return tmp;
	}
	
	public String extWinSearch(String lnt, boolean isComp) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'label'," + lnt +
		/**/"	text : '" + getName() + "'" + lnt +
		/**/"},mvc.Tools.crtComboBase(false,{" +
		/**/"	sotre : datestore," + lnt +
		/**/"	name : 'optcht_'" + getCode() + "," + lnt +
		/**/"	value : 11," + lnt +
		/**/"	width : 80" + lnt +
		/**/"	listeners : {" + lnt +
		/**/"		scope : this," + lnt +
		/**/"		change : function(field,newv,oldv,opts) {" + lnt +
		/**/"			this.onBetween(newv,this.oldId + '" + getCode() + "_label',this.oldId + '" + getCode() + "Ed',this.oldId + '" + getCode() + "_empty');" + lnt +
		/**/"			this.doLayout();" + lnt +
		/**/"		}" + lnt +
		/**/"	}" + lnt +
		/**/"}),{" + lnt +
		/**/"	xtype : 'datefield'," + lnt +
		/**/"	name : '" + getCode() + "'," + 
		/**/ lnt + extFormNull(lnt, isComp) + extFormEmpty(lnt, isComp) + lnt +
		/**/"	format : 'Y-m-d H:i:s'" + lnt +
		/**/"},{" + lnt +
		/**/"	xtype : 'label'," + lnt +
		/**/"	text : '至'," + lnt +
		/**/"	itemId : this.oldId + '" + getCode() + "_label'" + lnt +
		/**/"},{" + lnt +
		/**/"	xtype : 'datefield'," + lnt +
		/**/"	name : '" + getCode() + "Ed'," + 
		/**/ lnt + extFormNull(lnt, isComp) + extFormEmpty(lnt, isComp) + lnt +
		/**/"	format : 'Y-m-d H:i:s'," + lnt +
		/**/"	colspan : 2," + lnt +
		/**/"	itemId : this.oldId + '" + getCode() + "Ed'" + lnt +
		/**/"},{" + lnt +
		/**/"	xtype : 'label'," + lnt +
		/**/"	text : ''," + lnt +
		/**/"	colspan : 3," + lnt +
		/**/"	hidden : true," + lnt +
		/**/"	itemId : this.oldId + '" + getCode() + "_empty'" + lnt +
		/**/"}";
		return tmp;
	}

	public String extEdit(String lnt) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'datefield'," + lnt +
		/**/"	fieldLabel : '" + getName() + "'," + lnt +
		/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
		/**/"	format : 'Y-m-d H:i:s'" + lnt +
		/**/"}";
		return tmp;
	}

	public String extEditList(String lnt) {
		String tmp = "" + lnt +
		/**/"editor: {" + lnt +
		/**/"	xtype : 'datefield'," + lnt +
		/**/"	format : 'Y-m-d H:i:s'" + lnt +
		/**/"}" + lnt.substring(0, lnt.length() - 1);
		return tmp;
	}
//--------------

	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#model(irille.pub.html.ExtList)
	 */
	@Override
	public void model(ExtList v) {
		super.model(v);
		v.add(DATE_FORMAT,FMT);
//		return "{name : 'bean." + getCode() + "' , mapping : '" + getCode()
//		    + "' , type : 'date' ,dateFormat : 'Y-m-d H:i:s'}";
	}

	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#renderer(irille.pub.html.ExtList)
	 */
	@Override
	public void renderer(ExtList v) {
		v.addExp(RENDERER, "Ext.util.Format.dateRenderer('" + FMT + "')");
//		return "renderer : Ext.util.Format.dateRenderer('Y-m-d H:i:s')";
	}

	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#searchCode(irille.pub.html.ExtList)
	 */
	@Override
	public void searchCode(ExtList v) {
		super.searchCode(v);
		v.add(FORMAT,FMT);
	}
//	public String extSearch(String lnt) {
//		String tmp = "" +
//		/**/"{" + lnt +
//		/**/"	xtype : 'label'," + lnt +
//		/**/"	text : '" + getName() + "：'" + lnt +
//		/**/"},{" + lnt + "\t" +
//		/**/"xtype : 'datefield'," + lnt +
//		/**/"	name : '" + getCode() + "'," + lnt +
//		/**/"	format : 'Y-m-d'" + lnt +
//		/**/"}";
//		return tmp;
//	}

	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#form(irille.pub.html.ExtList, boolean)
	 */
	@Override
	public void form(ExtList v, boolean isComp) {
		super.form(v, isComp);
		v.add(FORMAT,FMT);
//		String tmp = "" +
//		/**/"{" + lnt +
//		/**/"	xtype : 'datefield'," + lnt +
//		/**/"	name : 'bean." + getCode() + "'," + lnt +
//		/**/"	fieldLabel : '" + getName() + "'," + lnt + extFormNull(lnt, isComp) + extFormEmpty(lnt, isComp) +
//		/**/"	format : 'Y-m-d H:i:s'" + lnt +
//		/**/"}";
//		return tmp;
	}

	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#edit(irille.pub.html.ExtList)
	 */
	@Override
	public void edit(ExtList v) {
		super.edit(v);
		v.add(FORMAT,FMT);
//		String tmp = "" +
//		/**/"{" + lnt +
//		/**/"	xtype : 'datefield'," + lnt +
//		/**/"	fieldLabel : '" + getName() + "'," + lnt +
//		/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
//		/**/"	format : 'Y-m-d H:i:s'" + lnt +
//		/**/"}";
//		return tmp;
	}
	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#editList(irille.pub.html.ExtList)
	 */
	@Override
	public void editList(ExtList v) {
		super.editList(v);
		v.add(FORMAT,FMT);
//		String tmp = "" + lnt +
//		/**/"editor: {" + lnt +
//		/**/"	xtype : 'datefield'," + lnt +
//		/**/"	format : 'Y-m-d H:i:s'" + lnt +
//		/**/"}" + lnt.substring(0, lnt.length() - 1);
//		return tmp;
	}

	@Override
	protected void winSearchOptcht(ExtList l) {
		super.winSearchOptcht(l);
		ExtList d = new ExtList(l);
		ExtFunDefine f = new ExtFunDefine(d, "change");
		l.add(VALUE, 7)
		.addExp(STORE, "datestore")
		.add(LISTENERS, d);
		d.addExp(SCOPE, "this")
		.add(f);
		f.addFunParasExp("field,newv,oldv,opts")
		.add("this.onBetween(newv,this.oldId + '" + getCode() + "',this.oldId + '" + getCode() + "Ed',this.oldId + '" + getCode() + "_empty');")
		.add("this.doLayout();");
	}
	
	@Override
	protected void winSearchForm(ExtList v, boolean isComp) {
		ExtList st = new ExtList(v);
		ExtList ed = new ExtList(v);
		v.setCloseStr(null);
		v.setTabs(false);
		v.add(st);
		v.add(ed);
		st.add(XTYPE, getXtype()).add(NAME, getCode()).add(FORMAT,FMT).addExp("itemId", "this.oldId + '" + getCode() + "'");
		ed.add(XTYPE, getXtype()+"exp").add(NAME, getCode() + "Ed").add(FORMAT,"Y-m-d").add(HIDDEN, true).addExp("itemId", "this.oldId + '" + getCode() + "Ed'");
	}
}
