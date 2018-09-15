package irille.pub.view;

import irille.pub.Str;
import irille.pub.html.ExtFunCall;
import irille.pub.html.ExtList;
import irille.pub.tb.Fld;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.Fld.OOutType;

public class VFldOutKey<T extends VFldOutKey> extends VFld<VFldOutKey> {
	private String _diySql = "";

	public VFldOutKey(Fld fld) {
		super(fld, "beantrigger", "string");
		if (getDefaultValue() != null && getDefaultValue().equals("Idu.getUser().getPkey()")) //前台的默认值在E中另外设置
			setDefaultValue(null);
	}

	@Override
	public T copyNew(Fld fld) {
		return (T) copy(new VFldOutKey(fld).setDiySql(getDiySql()));
	}

	public String getDiySql() {
		return _diySql;
	}

	public VFldOutKey setDiySql(String diySql) {
		_diySql = diySql;
		return this;
	}

	public String extModel() {
		return "{name : 'bean." + getCode() + "' , mapping : '" + getCode() + "' , type : 'string', outkey : true}";
	}

	public String extRenderer() {
		return "renderer : mvc.Tools.beanRenderer()";
	}

	public String extSearch(String lnt) {
		FldOutKey outkey = (FldOutKey) getFld();
		String cname = outkey.getOutTbClazz().getSimpleName();
		String pag = outkey.getOutTbClazz().getName().split("\\.")[2];
		String tmp = "";
		if (getType() != null && getType() == OOutType.COMBO)
			tmp = "" +
			/**/"{" + lnt +
			/**/"	xtype : 'label'," + lnt +
			/**/"	text : '" + getName() + "：'" + lnt +
			/**/"},mvc.Tools.crtComboTrigger(true,'" + pag + "_" + cname + "','',{" + lnt +
			/**/"	name : '" + getCode() + "'" + lnt +
			/**/"})";
		else
			tmp = "" +
			/**/"{" + lnt +
			/**/"	xtype : 'label'," + lnt +
			/**/"	text : '" + getName() + "：'" + lnt +
			/**/"},{" + lnt +
			/**/"	xtype : 'beantrigger'," + lnt +
			/**/"	name : '" + getCode() + "'," + lnt +
			/**/"	bean : '" + cname + "'," + lnt +
			/**/"	beanType : '" + pag + "'," + lnt +
			/**/"	emptyText : form_empty_text" + lnt +
			/**/"}";
		return tmp;
	}

	public String extForm(String lnt, boolean isComp) {
		FldOutKey outkey = (FldOutKey) getFld();
		String cname = outkey.getOutTbClazz().getSimpleName();
		String pag = outkey.getOutTbClazz().getName().split("\\.")[2];
		String tmp = "";
		if (getType() != null && getType() == OOutType.COMBO)
			tmp = "" +
			/**/"mvc.Tools.crtComboTrigger(" + (isComp ? true : isNull()) + ",'" + pag + "_" + cname + "','',{" + lnt +
			/**/"	name : 'bean." + getCode() + "'," + lnt +
			/**/"	fieldLabel : '" + getName() + "'" + lnt +
			/**/"})";
		else
			tmp = "" +
			/**/"{" + lnt +
			/**/"	xtype : 'beantrigger'," + lnt +
			/**/"	name : 'bean." + getCode() + "'," + lnt +
			/**/"	fieldLabel : '" + getName() + "'," + lnt +
			/**/"	bean : '" + cname + "'," + lnt +
			/**/"	beanType : '" + pag + "'," + lnt + extFormNull(lnt, isComp) +
			/**/"	emptyText : form_empty_text" + lnt +
			/**/"}";
		return tmp;
	}

	public String extWinSearch(String lnt, boolean isComp) {
		FldOutKey outkey = (FldOutKey) getFld();
		String cname = outkey.getOutTbClazz().getSimpleName();
		String pag = outkey.getOutTbClazz().getName().split("\\.")[2];
		String tmp = "";
		String temp = "{" + lnt +
		/**/"	xtype : 'label'," + lnt +
		/**/"	text : '" + getName() + "'" + lnt +
		/**/"},mvc.Tools.crtComboBase(false,{" +
		/**/"	sotre : outstore," + lnt +
		/**/"	name : 'optcht_'" + getCode() + "," + lnt +
		/**/"	value : 3," + lnt +
		/**/"	width : 80" + lnt +
		/**/"}),";
		if (getType() != null && getType() == OOutType.COMBO)
			tmp = temp +
			/**/"mvc.Tools.crtComboTrigger(" + (isComp ? true : isNull()) + ",'" + pag + "_" + cname + "','',{" + lnt +
			/**/"	name : '" + getCode() + "'" + lnt +
			/**/"})";
		else
			tmp = temp +
			/**/"{" + lnt +
			/**/"	xtype : 'beantrigger'," + lnt +
			/**/"	name : 'bean." + getCode() + "'," + lnt +
			/**/"	bean : '" + cname + "'," + lnt +
			/**/"	beanType : '" + pag + "'," + lnt + extFormNull(lnt, isComp) +
			/**/"	emptyText : form_empty_text" + lnt +
			/**/"}";
		return tmp;
	}

	public String extEdit(String lnt) {
		FldOutKey outkey = (FldOutKey) getFld();
		String cname = outkey.getOutTbClazz().getSimpleName();
		String pag = outkey.getOutTbClazz().getName().split("\\.")[2];
		String tmp = "";
		if (getType() != null && getType() == OOutType.COMBO)
			tmp = "" +
			/**/"mvc.Tools.crtComboTrigger(true,'" + pag + "_" + cname + "','',{" + lnt +
			/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
			/**/"	fieldLabel : '" + getName() + "'" + lnt +
			/**/"})";
		else
			tmp = "" +
			/**/"{" + lnt +
			/**/"	xtype : 'beantrigger'," + lnt +
			/**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
			/**/"	fieldLabel : '" + getName() + "'," + lnt +
			/**/"	bean : '" + cname + "'," + lnt +
			/**/"	beanType : '" + pag + "'," + lnt +
			/**/"	emptyText : form_empty_text" + lnt +
			/**/"}";
		return tmp;
	}

	public String extEditList(String lnt) {
		FldOutKey outkey = (FldOutKey) getFld();
		String cname = outkey.getOutTbClazz().getSimpleName();
		String pag = outkey.getOutTbClazz().getName().split("\\.")[2];
		String tmp = "";
		if (getType() != null && getType() == OOutType.COMBO)
			tmp = "" + lnt +
			/**/"editor: {" + lnt +
			/**/"	xtype : 'combotriggercell'," + lnt +
			/**/"	mode : 'local'," + lnt +
			/**/"	valueField : 'value'," + lnt +
			/**/"	triggerAction : 'all'," + lnt +
			/**/"	typeAhead : true," + lnt +
			/**/"	editable : false," + lnt +
			/**/"	store : Ext.create('mvc.store.ComboTrigger',{" + lnt +
			/**/"		actUrl : '" + pag + "_" + cname + "'," + lnt +
			/**/"		actWhere : ''" + lnt +
			/**/"	})," + lnt +
			/**/"	emptyText : form_empty_text" + lnt +
			/**/"}";
		else
			tmp = "" + lnt +
			/**/"editor: {" + lnt +
			/**/"	xtype : 'beantriggercell'," + lnt +
			/**/"	bean : '" + cname + "'," + lnt +
			/**/"	beanType : '" + pag + "'," + lnt +
			/**/"	beanName : 'bean." + getCode() + "'," + lnt +
			/**/"	grid : this," + lnt +
			/**/"	emptyText : form_empty_text" + lnt +
			/**/"}" + lnt.substring(0, lnt.length() - 1);
		return tmp;
	}

	// ----------------
	/*
	 * (non-Javadoc)
	 * @see irille.pub.view.VFld#model(irille.pub.html.ExtList)
	 */
	@Override
	public void model(ExtList v) {
		super.model(v);
		v.add("outkey", true);
		// return "{name : 'bean." + getCode() + "' , mapping : '" + getCode() +
		// "' , type : 'string', outkey : true}";
	}

	@Override
	public void renderer(ExtList v) {
		v.addExp(RENDERER, "mvc.Tools.beanRenderer()");
		// return "renderer : mvc.Tools.beanRenderer()";
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.view.VFld#searchCode(irille.pub.html.ExtList)
	 */
	@Override
	public void searchCode(ExtList v) {
		FldOutKey outkey = (FldOutKey) getFld();
		String cname = outkey.getOutTbClazz().getSimpleName();
		String pag = outkey.getOutTbClazz().getName().split("\\.")[2];
		if (outkey.getOutTbClazz().getSimpleName().equals("GsGoods")) { //货物特殊处理
			v.add(XTYPE, "comboauto").add(NAME, getCode()).addExp("listConfig", "{minWidth:250}").addExp("fields", "['pkey','code','name','spec']")
			    .addExp("valueField", "['pkey']").add("textField", "code").add("queryParam", "code").add("name", "goods")
			    .addExp("url", "base_path + '/gs_GsGoods_autoComplete'").add("urlExt", "gs.GsGoods")
			    .addExp("hasBlur", "false");
			return;
		}
		if (getType() != null && getType() == OOutType.COMBO) {
			v.setCloseStr(null); // 不输出“{}”
			ExtList l = new ExtList(v);
			v = v.add(new ExtFunCall(v, "mvc.Tools.crtComboTrigger", true, pag + "_" + cname, getDiySql(), l));
			l.add(NAME, getCode());
			return;
		}
		// tmp = "" +
		// /**/"{" + lnt +
		// /**/"	xtype : 'label'," + lnt +
		// /**/"	text : '" + getName() + "：'" + lnt +
		// /**/"},mvc.Tools.crtComboTrigger(true,'" + pag + "_" + cname + "','',{" +
		// lnt +
		// /**/"	name : '" + getCode() + "'" + lnt +
		// /**/"})";
		super.searchCode(v);
		if (Str.isEmpty(getDiySql()) == false)
			v.add(DIY_SQL, getDiySql());
		v.add("bean", cname).add("beanType", pag).addExp(EMPTY_TEXT, "form_empty_text");
		// else
		// tmp = "" +
		// /**/"{" + lnt +
		// /**/"	xtype : 'label'," + lnt +
		// /**/"	text : '" + getName() + "：'" + lnt +
		// /**/"},{" + lnt +
		// /**/"	xtype : 'beantrigger'," + lnt +
		// /**/"	name : '" + getCode() + "'," + lnt +
		// /**/"	bean : '" + cname + "'," + lnt +
		// /**/"	beanType : '" + pag + "'," + lnt +
		// /**/"	emptyText : form_empty_text" + lnt +
		// /**/"}";
		// return tmp;
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.view.VFld#form(irille.pub.html.ExtList, boolean)
	 */
	@Override
	public void form(ExtList v, boolean isComp) {
		FldOutKey outkey = (FldOutKey) getFld();
		String cname = outkey.getOutTbClazz().getSimpleName();
		String pag = outkey.getOutTbClazz().getName().split("\\.")[2];
		if (getType() != null && getType() == OOutType.COMBO) {
			v.setCloseStr(null); // 不输出“{}”
			ExtList l = new ExtList(v);
			v = v.add(new ExtFunCall(v, "mvc.Tools.crtComboTrigger", (isComp ? true : isNull()), pag + "_" + cname,
			    getDiySql(), l));
			l.add(NAME, getBeanName()).add(FIELD_LABEL, getName());
			if (getDefaultValue() != null)
				l.addExp(VALUE, getDefaultValue().toString());
			addReadOnly(l);
			addHidden(l);
			return;
		}
		// tmp = "" +
		// /**/"mvc.Tools.crtComboTrigger(" + (isComp?true:isNull()) + ",'" + pag +
		// "_" + cname + "','',{" + lnt +
		// /**/"	name : 'bean." + getCode() + "'," + lnt +
		// /**/"	fieldLabel : '" + getName() + "'" + lnt +
		// /**/"})";
		// else
		v.add(XTYPE, getXtype()).add(NAME, getBeanName()).add(FIELD_LABEL, getName());
		if (Str.isEmpty(getDiySql()) == false)
			v.add(DIY_SQL, getDiySql());
		v.add("bean", cname).add("beanType", pag).addExp(EMPTY_TEXT, "form_empty_text");
		formNull(v, isComp);
		if (getDefaultValue() != null)
			v.addExp(VALUE, getDefaultValue().toString());
		addReadOnly(v);
		addHidden(v);
		// tmp = "" +
		// /**/"{" + lnt +
		// /**/"	xtype : 'beantrigger'," + lnt +
		// /**/"	name : 'bean." + getCode() + "'," + lnt +
		// /**/"	fieldLabel : '" + getName() + "'," + lnt +
		// /**/"	bean : '" + cname + "'," + lnt +
		// /**/"	beanType : '" + pag + "'," + lnt + extFormNull(lnt, isComp) +
		// /**/"	emptyText : form_empty_text" + lnt +
		// /**/"}";
		// return tmp;
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.view.VFld#edit(irille.pub.html.ExtList)
	 */
	@Override
	public void edit(ExtList v) {
		FldOutKey outkey = (FldOutKey) getFld();
		String cname = outkey.getOutTbClazz().getSimpleName();
		String pag = outkey.getOutTbClazz().getName().split("\\.")[2];
		if (getType() != null && getType() == OOutType.COMBO) {
			v.setCloseStr(null); // 不输出“{}”
			ExtList l = new ExtList(v);
			v = v.add(new ExtFunCall(v, "mvc.Tools.crtComboTrigger", true, pag + "_" + cname, getDiySql(), l));
			l.addExp(VALUE, "this.record.get('" + getBeanName() + "')").add(FIELD_LABEL, getName());
			return;
		}
		// tmp = "" +
		// /**/"mvc.Tools.crtComboTrigger(true,'" + pag + "_" + cname + "','',{"
		// + lnt +
		// /**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
		// /**/"	fieldLabel : '" + getName() + "'" + lnt +
		// /**/"})";
		// else
		v.add(XTYPE, getXtype()).addExp(VALUE, "this.record.get('" + getBeanName() + "')")
		    .add(FIELD_LABEL, getName());
		if (Str.isEmpty(getDiySql()) == false)
			v.add(DIY_SQL, getDiySql());
		v.add("bean", cname).add("beanType", pag).addExp(EMPTY_TEXT, "form_empty_text");
		// tmp = "" +
		// /**/"{" + lnt +
		// /**/"	xtype : 'beantrigger'," + lnt +
		// /**/"	value: this.record.get('bean." + getCode() + "')," + lnt +
		// /**/"	fieldLabel : '" + getName() + "'," + lnt +
		// /**/"	bean : '" + cname + "'," + lnt +
		// /**/"	beanType : '" + pag + "'," + lnt +
		// /**/"	emptyText : form_empty_text" + lnt +
		// /**/"}";
		// return tmp;
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.view.VFld#editList(irille.pub.html.ExtList)
	 */
	@Override
	public void editList(ExtList v) {
		FldOutKey outkey = (FldOutKey) getFld();
		String cname = outkey.getOutTbClazz().getSimpleName();
		String pag = outkey.getOutTbClazz().getName().split("\\.")[2];
		if (getType() != null && getType() == OOutType.COMBO) {
			ExtList l = new ExtList(null);
			v.add(XTYPE, "combotriggercell").add(MODE, "local").add(VALUE_FIELD, "value").add(TRIGGER_ACTION, "all")
			    .add(TYPE_AHEAD, true).add(EDITABLE, false);
			v.addExp(STORE, "Ext.create('mvc.store.ComboTrigger',{actUrl:'" + pag + "_" + cname + "',actWhere:''})");
			//			v.AddFunCall(STORE).addFunParas("mvc.store.ComboTrigger", l);
			//			l.add("actUrl", pag + "_" + cname).add("actWhere", "");
			v.addExp(EMPTY_TEXT, "form_empty_text");
			return;
		}
		// tmp = "" + lnt +
		// /**/"editor: {" + lnt +
		// /**/"	xtype : 'combotriggercell'," + lnt +
		// /**/"	mode : 'local'," + lnt +
		// /**/"	valueField : 'value'," + lnt +
		// /**/"	triggerAction : 'all'," + lnt +
		// /**/"	typeAhead : true," + lnt +
		// /**/"	editable : false," + lnt +
		// /**/"	store : Ext.create('mvc.store.ComboTrigger',{" + lnt +
		// /**/"		actUrl : '" + pag + "_" + cname + "'," + lnt +
		// /**/"		actWhere : ''" + lnt +
		// /**/"	})," + lnt +
		// /**/"	emptyText : form_empty_text" + lnt +
		// /**/"}";
		// else
		v.add(XTYPE, "beantriggercell").add("bean", cname).add("beanType", pag).add("beanName", getBeanName())
		    .addExp("grid", "this").addExp(EMPTY_TEXT, "form_empty_text");
		// tmp = "" + lnt +
		// /**/"editor: {" + lnt +
		// /**/"	xtype : 'beantriggercell'," + lnt +
		// /**/"	bean : '" + cname + "'," + lnt +
		// /**/"	beanType : '" + pag + "'," + lnt +
		// /**/"	beanName : 'bean." + getCode() + "'," + lnt +
		// /**/"	grid : this," + lnt +
		// /**/"	emptyText : form_empty_text" + lnt +
		// /**/"}" + lnt.substring(0, lnt.length() - 1);
		// return tmp;
	}

	@Override
	protected void winSearchOptcht(ExtList l) {
		super.winSearchOptcht(l);
		l.add(VALUE, 3).addExp(STORE, "outstore");
	}

	public void winSearchForm(ExtList v, boolean isComp) {
		FldOutKey outkey = (FldOutKey) getFld();
		String cname = outkey.getOutTbClazz().getSimpleName();
		String pag = outkey.getOutTbClazz().getName().split("\\.")[2];
		if (getType() != null && getType() == OOutType.COMBO) {
			v.setCloseStr(null); // 不输出“{}”
			ExtList l = new ExtList(v);
			v = v.add(new ExtFunCall(v, "mvc.Tools.crtComboTrigger", (isComp ? true : isNull()), pag + "_" + cname,
			    getDiySql(), l));
			l.add(NAME, getCode());
			addReadOnly(l);
			addHidden(l);
			return;
		}
		// tmp = "" +
		// /**/"mvc.Tools.crtComboTrigger(" + (isComp?true:isNull()) + ",'" + pag +
		// "_" + cname + "','',{" + lnt +
		// /**/"	name : 'bean." + getCode() + "'," + lnt +
		// /**/"	fieldLabel : '" + getName() + "'" + lnt +
		// /**/"})";
		// else
		v.add(XTYPE, getXtype()).add(NAME, getCode());
		if (Str.isEmpty(getDiySql()) == false)
			v.add(DIY_SQL, getDiySql());
		v.add("bean", cname).add("beanType", pag).addExp(EMPTY_TEXT, "form_empty_text");
		formNull(v, isComp);
		// tmp = "" +
		// /**/"{" + lnt +
		// /**/"	xtype : 'beantrigger'," + lnt +
		// /**/"	name : 'bean." + getCode() + "'," + lnt +
		// /**/"	fieldLabel : '" + getName() + "'," + lnt +
		// /**/"	bean : '" + cname + "'," + lnt +
		// /**/"	beanType : '" + pag + "'," + lnt + extFormNull(lnt, isComp) +
		// /**/"	emptyText : form_empty_text" + lnt +
		// /**/"}";
		// return tmp;
	}
}
