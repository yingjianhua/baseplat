package irille.pub.html;

import irille.core.sys.SysDept;
import irille.pub.FileTools;
import irille.pub.Str;
import irille.pub.ext.Ext;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.Fld;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * 
 * @author whx
 * 
 */
public class EMListTrigger<T extends EMListTrigger> extends EMBase<T> {
	private IEnumFld _dispFld;
	private VFlds _combFlds;
	private ExtList _formDocked = new ExtList();
	private ExtList _formSearchFlds = new ExtList();
	private ExtList _formTable = new ExtList();
	private String _extend = "Ext.grid.Panel";
	private Integer _tdCount = null;

	/**
	 * 
	 */
	public EMListTrigger(Tb tb, IEnumFld dispFld, VFlds... vflds) {
		super(tb, vflds);
		_dispFld = dispFld;
	}

	public static void main(String[] args) {
		Tb tb = SysDept.TB;
		VFlds flds = new VFlds().addAll(tb);
		EMListTrigger form = new EMListTrigger(tb, SysDept.T.NAME, flds);
		form.setCombFlds(new VFlds(SysDept.T.CODE, SysDept.T.NAME, SysDept.T.ORG)).backupFiles().crtFile()
		    .cmpFileIgnoreBlank();
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "view/" + getPack() + "/" + getClazz() + "/TriggerList.js";
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.view." + getPack() + "." + getClazz() + ".TriggerList";
	}

	@Override
	public void initAttrs() {
		super.initAttrs();
		add(EXTEND, getExtend()).add("disableSelection", false).add("loadMask", true)
		    .addExp("selModel", "{selType : 'checkboxmodel'}");
		if (getExtend().equals("Ext.grid.Panel"))
			addExp("viewConfig", "{enableTextSelection : true}");
		if (getTdCount() != null)
			add("oneTdCount", getTdCount());
		addExp("searchField", "null");
	}

	@Override
	public void initFuns() {
		itemdblclick(AddList("listeners").AddFunDefine("itemdblclick"));

		AddFun("onTriggerList", EMListTrigger.class, _dispFld.getFld().getCode());
		super.initFuns();
	}

	@Override
	public void initComponent(ExtFunDefine fun) {
		// 单元格定义
		fun.add("this.columns = ");
		fun.add(getColumns());
		if (getIndexGoods() > 0)
			fun.add("		mvc.Tools.doGoodsLine(this.columns, " + getIndexGoods() + ");");
		fun.add("		this.store=Ext.create('mvc.store." + getPack() + "." + getClazz() + "');");
		fun.add("this.dockedItems = ");
		fun.AddDime().add(getFormDocked()).add(getFormTable());
		if(_extend.equals("mvc.tools.RowexpanderGrid")) {
			fun.add(loadFunCode(EMListTrigger.class, "initComponentRowexpander"));	
		} else {
			fun.add(loadFunCode(EMListTrigger.class, "initComponent1"));	
		}
//@formatter:off	
/** Begin initComponent1 ********
		this.callParent(arguments);
		this.searchField = this.down('irilleSearchfield');
*** End initComponent1 *********/
//@formatter:on
		
//@formatter:off	
/** Begin initComponentRowexpander ********
		this.callParent(arguments);
		this.searchField = this.down('irilleSearchfield');
		this.getPlugin("rowexpanderplugin").expandOnDblClick=false;
*** End initComponentRowexpander *********/
//@formatter:on
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#initColumns()
	 */
	@Override
	public void initColumns() {
		super.initColumns();
		int index = 0;
		for (VFld fld : getVFlds().getVFlds()) {
			if (fld.getFld() instanceof FldLines || fld.getFld() instanceof FldV || fld.getCode().equals("rowVersion"))
				continue;
			if (fld.getCode().equals("pkey")) {
				if (getTb().isAutoIncrement())
					continue;
				if (getTb().isAutoLocal() && fld.getFld().getJavaType().equals(String.class) == false)
					continue;
			}
			setFldAttr(fld, getColumns().AddList());
			index++;
			if (fld.getCode().equals("goods"))
				setIndexGoods(index);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#setFldAttr(irille.pub.view.VFld,
	 * irille.pub.html.ExtList)
	 */
	@Override
	public void setFldAttr(VFld fld, ExtList fldList) {
		fld.list(fldList);
		super.setFldAttr(fld, fldList);
	}

	public void initFormDocked() {

		String defaultCode = _combFlds.getVFlds().length > 0 ? _combFlds.getVFlds()[0].getFld().getCode() : "";

		ExtList docked = getFormDocked().add("dock", "top").add(XTYPE, "toolbar");
		ExtDime dimeItems = docked.AddDime(ITEMS, new ExtExp("\"搜索：\""));

		ExtList searchFlds = getFormSearchFlds();
		dimeItems.add(searchFlds);
		searchFlds.add(XTYPE, "combo").add(MODE, "local").add(VALUE_FIELD, "value").add(TRIGGER_ACTION, "all")
		    .add(FORCE_SELECTION, true).add(TYPE_AHEAD, true).add(EDITABLE, false).add(WIDTH, 100);
		searchFlds.add(VALUE, defaultCode);

		ExtList searchFlds_store = searchFlds.AddFunCall("store:	Ext.create", "Ext.data.Store").AddFunParaList();
		searchFlds_store.addExp("fields", " ['value', 'text']");
		initSearchFlds(searchFlds_store.AddDime("data"));// 【选择集】

		searchFlds.AddList(LISTENERS).add(SCOPE, EXP_THIS)
		    .addExp("change", "function(field,newv,oldv,opts) {	this.searchField.flds = newv; }");

		dimeItems.add("=").AddList().add(WIDTH, 250).add(XTYPE, "irilleSearchfield").add("flds", defaultCode)
		    .addExp("store", "this.store");
		dimeItems.add("->").AddList().add(XTYPE, "button").add(TEXT, "确定").add(SCOPE, EXP_THIS)
		    .add(ICON_CLS, "win-ok-icon").addExp(HANDLER, "this.onTriggerList");

	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#initForm()
	 */
	@Override
	public void initForm() {
		super.initForm();
		initFormDocked();
		initFormTable();
		getForm().addExp(XTYPE, "Ext.create('mvc.tools.ComboxPaging',{myList : this})");
	}

	public void initFormTable() {
		getFormTable().add(XTYPE, "pagingtoolbar").addExp("store", "this.store").add("dock", "bottom")
		    .add("displayInfo", true).add("displayMsg", "显示 {0} - {1} 条，共计 {2} 条").add("emptyMsg", "没有数据")
		    .AddDime(ITEMS, getForm());
	}

	// 【选择集】
	public void initSearchFlds(ExtDime v) {
		for (VFld fld : _combFlds.getVFlds())
			v.AddList().setTabs(false).add(VALUE, fld.getCode()).add(TEXT, fld.getName());
	}

	public void itemdblclick(ExtFunDefine v) {
		v.add(loadFunCode(EMListTrigger.class, "itemdblclick"));
//@formatter:off	
/** Begin itemdblclick ********
			this.onTriggerList();	
*** End itemdblclick *********/
//@formatter:on
	}

	public void onTrigger(ExtFunDefine v) {
		v.add(loadFunCode(EMListTrigger.class, "onTrigger", _dispFld.getFld().getCode()));
//@formatter:off	
/** Begin onTriggerList ********
			var selection = this.getView().getSelectionModel().getSelection()[0];
		if (selection){
			this.fireEvent('trigger', selection.get('bean.pkey') + bean_split + selection.get('bean.【0】'), null);
		}
*** End onTriggerList *********/
//@formatter:on
	}

	/**
	 * @return the combFlds
	 */
	public VFlds getCombFlds() {
		return _combFlds;
	}

	/**
	 * @param combFlds
	 *          the combFlds to set
	 */
	public T setCombFlds(VFlds combFlds) {
		_combFlds = combFlds;
		return (T) this;
	}

	/**
	 * @param combFlds
	 *          the combFlds to set
	 */
	public T setCombFlds(IEnumFld... combFlds) {
		_combFlds = new VFlds(combFlds);
		return (T) this;
	}

	/**
	 * @return the formDocked
	 */
	public ExtList getFormDocked() {
		return _formDocked;
	}

	/**
	 * @return the formSearchFlds
	 */
	public ExtList getFormSearchFlds() {
		return _formSearchFlds;
	}

	/**
	 * @return the formTable
	 */
	public ExtList getFormTable() {
		return _formTable;
	}

	public void setExtendRow() {
		_extend = "mvc.tools.RowexpanderGrid";
	}

	public String getExtend() {
		return _extend;
	}

	public Integer getTdCount() {
		return _tdCount;
	}

	public void setTdCount(Integer tdCount) {
		_tdCount = tdCount;
	}

}
