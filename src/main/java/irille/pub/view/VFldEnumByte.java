package irille.pub.view;

import irille.pub.ext.Ext;
import irille.pub.html.ExtFunCall;
import irille.pub.html.ExtList;
import irille.pub.tb.Fld;
import irille.pub.tb.FldEnumByte;

public class VFldEnumByte<T extends VFldEnumByte> extends VFld<VFldEnumByte> {

	public VFldEnumByte(Fld fld) {
		super(fld,"combo","int");
		setUseNull(true);
	}

	@Override
	public T copyNew(Fld fld) {
		return (T) copy(new VFldEnumByte(fld));
	}

	public String extModel() {
		return "{name : 'bean." + getCode() + "' , mapping : '" + getCode() + "' , type : 'int', useNull : true}";
	}

	
	public String extRenderer() {
		Class clazz = ((FldEnumByte) getFld()).getEnum().getClass();
		return "renderer : mvc.Tools.optRenderer('" + Ext.getPag(clazz) + "','" + Ext.getOptClazz(clazz)
		    + "','" + Ext.getVarName(clazz) + "')";
	}

	public String extSearch(String lnt) {
		Class clazz = ((FldEnumByte) getFld()).getEnum().getClass();
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'label'," + lnt +
		/**/"	text : '" + getName() + "：'" + lnt +
		/**/"},{" + lnt +
		/**/"	xtype : 'combo'," + lnt +
		/**/"	name : '" + getCode() + "'," + lnt +
		/**/"	mode : 'local'," + lnt +
		/**/"	valueField : 'value'," + lnt +
		/**/"	triggerAction : 'all'," + lnt +
		/**/"	forceSelection : true," + lnt +
		/**/"	typeAhead : true," + lnt +
		/**/"	editable : false," + lnt +
		/**/"	emptyText : form_empty_text," + lnt +
		/**/"	store : Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')" + lnt +
		/**/"}";
		return tmp;
	}

	public String extForm(String lnt, boolean isComp) {
		Class clazz = ((FldEnumByte) getFld()).getEnum().getClass();
		String odefault = Ext.getOpeDefault(clazz);
		String tmp = "";
		if (odefault == null)
			tmp = "" +
			/**/"mvc.Tools.crtComboForm(" + (isComp ? true : isNull()) + ",{" + lnt +
			/**/"	name : 'bean." + getCode() + "'," + lnt +
			/**/"	fieldLabel : '" + getName() + "'," + lnt + extFormEmpty(lnt, isComp) +
			/**/"	store : Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')" + lnt +
			/**/"})";
		else
			tmp = "" +
			/**/"mvc.Tools.crtComboForm(" + (isComp ? true : isNull()) + ",{" + lnt +
			/**/"	name : 'bean." + getCode() + "'," + lnt +
			/**/"	fieldLabel : '" + getName() + "'," + lnt +
			/**/"	store : Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')," + lnt +
			/**/"	value : " + odefault + lnt +
			/**/"})";
		return tmp;
	}
	
	public String extWinSearch(String lnt, boolean isComp) {
		Class clazz = ((FldEnumByte) getFld()).getEnum().getClass();
		String odefault = Ext.getOpeDefault(clazz);
		String tmp = "";
		String temp = "{" + lnt +
				/**/"	xtype : 'label'," + lnt +
				/**/"	text : '" + getName() + "'" + lnt +
				/**/"},mvc.Tools.crtComboBase(false,{" +
				/**/"	sotre : numstore," + lnt +
				/**/"	name : 'optcht_'" + getCode() + "," + lnt +
				/**/"	value : 3," + lnt +
				/**/"	width : 80" + lnt +
				/**/"}),";
		if (odefault == null)
			tmp = temp +
			/**/"mvc.Tools.crtComboForm(" + (isComp ? true : isNull()) + ",{" + lnt +
			/**/"	name : 'bean." + getCode() + "'," + lnt +
			/**/"	fieldLabel : '" + getName() + "'," + lnt + extFormEmpty(lnt, isComp) +
			/**/"	store : Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')" + lnt +
			/**/"})";
		else
			tmp = "" +
			/**/"mvc.Tools.crtComboForm(" + (isComp ? true : isNull()) + ",{" + lnt +
			/**/"	name : 'bean." + getCode() + "'," + lnt +
			/**/"	fieldLabel : '" + getName() + "'," + lnt +
			/**/"	store : Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')," + lnt +
			/**/"	value : " + odefault + lnt +
			/**/"})";
		return tmp;
	}
	
	public String extEdit(String lnt) {
		Class clazz = ((FldEnumByte) getFld()).getEnum().getClass();
		String tmp = "" +
		/**mvc.Tools.crtComboForm(true,{" + lnt +
		/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
		/**/"	fieldLabel : '" + getName() + "'," + lnt +
		/**/"	store : Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')" + lnt +
		/**/"})";
		return tmp;
	}

	public String extEditList(String lnt) {
		Class clazz = ((FldEnumByte) getFld()).getEnum().getClass();
		String tmp = "" + lnt +
		/**/"editor: mvc.Tools.crtComboForm(true,{" + lnt +
		/**/"	name : 'bean." + getCode() + "'," + lnt +
		/**/"	store : Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')" + lnt +
		/**/"})" + lnt.substring(0, lnt.length() - 1);
		return tmp;
	}
	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#addRenderer(irille.pub.html.ExtList)
	 */
	@Override
	public void renderer(ExtList v) {
		Class clazz = ((FldEnumByte) getFld()).getEnum().getClass();
		v.addExp(RENDERER,"mvc.Tools.optRenderer('" + Ext.getPag(clazz) + "','" + Ext.getOptClazz(clazz)
		    + "','" + Ext.getVarName(clazz) + "')");
	}
	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#searchCode(irille.pub.html.ExtList)
	 */
	@Override
	public void searchCode(ExtList v) {
		super.searchCode(v);
		Class clazz = ((FldEnumByte) getFld()).getEnum().getClass();
		v.add(MODE, "local").add(VALUE_FIELD,"value").add(TRIGGER_ACTION,"all")
			.add(FORCE_SELECTION,true).add(TYPE_AHEAD,true).add(EDITABLE,false)
			.addExp(EMPTY_TEXT,"form_empty_text")
			.addExp(STORE,"Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')");
	}
	@Override
	public void form(ExtList v, boolean isComp) {
		Class clazz = ((FldEnumByte) getFld()).getEnum().getClass();
		String odefault = Ext.getOpeDefault(clazz);
		//		if (getDefaultValue() != null)
		//			odefault = getDefaultValue().toString();
		v.setCloseStr(null); //不输出“{}”
		ExtList l=new ExtList(v);
		v=v.add(new ExtFunCall(v,"mvc.Tools.crtComboForm",(isComp ? true : isNull()),l));
		l.add(NAME, getBeanName())
				.add(FIELD_LABEL, getName());
		if (getDefaultValue() == null) {
			formEmpty(v, isComp);
			l.addExp(STORE,"Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')");
			if (isComp == false)
				l.addExp(VALUE, odefault);
			addReadOnly(l);
			addHidden(l);
		}
//			tmp = "" +
//			/**/"mvc.Tools.crtComboForm(" + (isComp ? true : isNull()) + ",{" + lnt +
//			/**/"	name : 'bean." + getCode() + "'," + lnt +
//			/**/"	fieldLabel : '" + getName() + "'," + lnt + extFormEmpty(lnt, isComp) +
//			/**/"	store : Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')" + lnt +
//			/**/"})";
		else {
			l.addExp(STORE,"Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')");
			if (isComp == false)
				l.addExp(VALUE, getDefaultValue().toString());
			addReadOnly(l);
			addHidden(l);
		}
//			tmp = "" +
//			/**/"mvc.Tools.crtComboForm(" + (isComp ? true : isNull()) + ",{" + lnt +
//			/**/"	name : 'bean." + getCode() + "'," + lnt +
//			/**/"	fieldLabel : '" + getName() + "'," + lnt +
//			/**/"	store : Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')," + lnt +
//			/**/"	value : " + odefault + lnt +
//			/**/"})";
	}

	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#edit(irille.pub.html.ExtList)
	 */
	@Override
	public void edit(ExtList v) {
		Class clazz = ((FldEnumByte) getFld()).getEnum().getClass();
		v.setCloseStr(null); //不输出“{}”
		ExtList l=new ExtList(v);
		v=v.add(new ExtFunCall(v,"mvc.Tools.crtComboForm",true,l));
		l.addExp(VALUE,"this.record.get('"+getBeanName() + "')")
				.add(FIELD_LABEL, getName());
		l.addExp(STORE,"Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')");
//		
//		String tmp = "" +
//		/**mvc.Tools.crtComboForm(true,{" + lnt +
//		/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
//		/**/"	fieldLabel : '" + getName() + "'," + lnt +
//		/**/"	store : Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')" + lnt +
//		/**/"})";
//		return tmp;
	}

	/* (non-Javadoc)
	 * @see irille.pub.view.VFld#editList(irille.pub.html.ExtList)
	 */
	@Override
	public void editList(ExtList v) {
		Class clazz = ((FldEnumByte) getFld()).getEnum().getClass();
		v.setCloseStr(null); //不输出“{}”
		ExtList l=new ExtList(v);
		v=v.add(new ExtFunCall(v,"mvc.Tools.crtComboForm",true,l));
		l.add(NAME, getBeanName());
		l.addExp(STORE,"Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')");
//		String tmp = "" + lnt +
//		/**/"editor: mvc.Tools.crtComboForm(true,{" + lnt +
//		/**/"	name : 'bean." + getCode() + "'," + lnt +
//		/**/"	store : Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')" + lnt +
//		/**/"})" + lnt.substring(0, lnt.length() - 1);
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
		Class clazz = ((FldEnumByte) getFld()).getEnum().getClass();
		//以下备注部分代码为使用默认值
//		String odefault = Ext.getOpeDefault(clazz);
		v.setCloseStr(null); //不输出“{}”
		ExtList l=new ExtList(v);
		v=v.add(new ExtFunCall(v,"mvc.Tools.crtComboForm",(isComp ? true : isNull()),l));
		l.add(NAME, getCode());
//		if (odefault == null) {
//			formEmpty(v, isComp);
//			l.addExp(STORE,"Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')");
//		} else {
			l.addExp(STORE,"Ext.create('mvc.combo." + Ext.getPag(clazz) + "." + Ext.getClassVarName(clazz) + "')");
//			l.addExp(VALUE,odefault);
//		}
	}
}