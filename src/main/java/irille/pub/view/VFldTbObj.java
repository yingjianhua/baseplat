package irille.pub.view;

import irille.pub.html.ExtList;
import irille.pub.tb.Fld;
import irille.pub.tb.Fld.OOutType;

/**
 * 对应IntTbObj与LongTbObj
 * 外键选择器先写死，需要程序员更改
 */
public class VFldTbObj<T extends VFldTbObj> extends VFld<VFldTbObj> {

	public VFldTbObj(Fld fld) {
		super(fld, "beantrigger", "string");
		setWidthList(80);
	}

	@Override
	public T copyNew(Fld fld) {
		return (T) copy(new VFldTbObj(fld));
	}
	
	public String extModel() {
		return "{name : 'bean." + getCode() + "' , mapping : '" + getCode() + "' , type : 'string', outkey : true}";
	}

	public String extRenderer() {
		return "renderer : mvc.Tools.beanRenderer()";
	}

	public String extSearch(String lnt) {
		String cname = "SysOrg";
		String pag = "sys";
		String tmp = "";
		if (getType() != null && getType()==OOutType.COMBO)
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
		String cname = "SysOrg";
		String pag = "sys";
		String tmp = "";
		if (getType() != null && getType()==OOutType.COMBO)
			tmp = "" +
			/**/"mvc.Tools.crtComboTrigger(" + (isComp?true:isNull()) + ",'" + pag + "_" + cname + "','',{" + lnt +
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
			/**/"	beanType : '" + pag + "'," + lnt + extFormNull(lnt,isComp) +
			/**/"	emptyText : form_empty_text" + lnt +
			/**/"}";
		return tmp;
	}

	public String extEdit(String lnt) {
		String cname = "SysOrg";
		String pag = "sys";
		String tmp = "";
		if (getType() != null && getType()==OOutType.COMBO)
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
		String cname = "SysOrg";
		String pag = "sys";
		String tmp = "";
		if (getType() != null && getType()==OOutType.COMBO)
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
			/**/"}" + lnt.substring(0,lnt.length()-1);
		return tmp;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.view.VFld#model(irille.pub.html.ExtList)
	 */
	@Override
	public void model(ExtList v) {
		super.model(v);
		v.add("outkey", true);
	}

	@Override
	public void renderer(ExtList v) {
		v.addExp(RENDERER, "mvc.Tools.beanRenderer()");
		// return "renderer : mvc.Tools.beanRenderer()";
	}
	
}
