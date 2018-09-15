package irille.pub.html;

import irille.pub.ext.Ext;
import irille.pub.html.Exts.ExtAct;
import irille.pub.html.Exts.ExtActs;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.FldV;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * 属性类的基类
 * 
 * @author whx
 * 
 */
public class EMList<T extends EMList> extends EMBase<T> {
	private boolean _upd, _ins, _del, _edit, _print;
	private boolean _lineActs; // 是否有明细操作按钮
	private VFlds _searchVFlds = new VFlds();
	private ExtActs _actsLine = new ExtActs();
	private ExtList _formDocked = new ExtList();
	private ExtDime _formSearch = new ExtDime();
	private ExtList _formTable = new ExtList();
	private String _extend = "Ext.grid.Panel";

	/**
	 * @param fileName
	 */
	/**
	 * 
	 */
	public EMList(Tb tb, VFlds... vflds) {
		super(tb, vflds);
		_upd = tb.checkAct("upd") != null;
		_ins = tb.checkAct("ins") != null;
		_del = tb.checkAct("del") != null;
		_edit = tb.checkAct("edit") != null;
		_print = tb.checkAct("print") != null;
		_lineActs = _upd || _del; // 是否有明细操作按钮
		_lineActs = false; //默认不显示，除非手动设置
	}

//	public static void main(String[] args) {
//		Tb tb = SalPriceCtlApplyLine.TB;
//		VFlds flds = new VFlds().addWithout(tb, "pkey");
//		System.err.println(new EMForm(tb, flds).init().out());
//	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "view/" + getPack() + "/" + getClazz() + "/List.js";
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.view." + getPack() + "." + getClazz() + ".List";
	}

	@Override
	public void initAttrs() {
		super.initAttrs();
		add(EXTEND, getExtend())// 父类
		    .add("oldId", "btn_" + getClazz())// 页面中组件ID的前缀
		    .add("lock", true) // 按钮启用/禁用的标识
		    .add("disableSelection", false)// 表示可以选择表格行或列
		    .add("loadMask", true)// 加载元素时显示遮盖效果
		    .add("multiSelect", true) // 表示允许多行选择
		    .add("roles", "")// 当前用户在此菜单中，所有的功能权限组，左侧菜单初始化时已加载到MenuList中
		    .addExp("selModel", "{selType : 'checkboxmodel'}");
		if (getExtend().equals("Ext.grid.Panel"))
			addExp("viewConfig", "{enableTextSelection : true}");
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#initActs()
	 */
	@Override
	public void initActs() {
		super.initActs();
		loadTbActs(EMList.class); // 根据Tb对象的功能建立ExtAct对象，List功能被忽略

		getActsLine().AddActRow(this, new Act(getTb(), OAct.UPD_ROW), EMList.class).getFun()
		    .addFunParasExp("grid, rowIndex");
		getActsLine().AddActRow(this, new Act(getTb(), OAct.DEL_ROW), EMList.class, getPack(), getClazz()).getFun()
		    .addFunParasExp("grid, rowIndex");
		;
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#loadTbAct(java.lang.Class, irille.action.Act)
	 */
	@Override
	public void loadTbAct(Class funCodeFile, Act act) {
		IEnumOpt oact = act.getAct();
		//		if (oact != OAct.INS && oact != OAct.EDIT && oact != OAct.UPD && oact != OAct.DEL && oact != OAct.DO_ENABLED
		//		    && oact != OAct.UN_ENABLED)
		//			return;
		ExtAct v = new ExtAct(this, act, funCodeFile);
		v.add(TEXT, act.getName()).add(ICON_CLS, act.getIcon()).addExp("itemId", "this.oldId+'" + act.getCode() + "'")
		    .add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper());
		getActs().add(v);
//		if (oact == OAct.EDIT || oact == OAct.UPD || oact == OAct.DEL || oact == OAct.DO_ENABLED || oact == OAct.UN_ENABLED
//				|| oact == OAct.PRINT) {
//			v.addExp("disabled", "this.lock");
//		}
		if(act.isNeedSel())
			v.addExp("disabled", "this.lock");
	}

	@Override
	public void initFuns() {

		// 行选择事件处理
		ExtFunDefine fun = AddList("listeners") // 表格行选择事件，主要设置按钮是否可用
		    .AddFunDefine("selectionchange", new ExtExp("selModel, selected"));
		selectionChanges(fun);
		super.initFuns();

		add(AddFun("onSearchCancel", EMList.class));
		add(AddFun("onSearch", EMList.class));
		add(AddFun("onSearchAdv", EMList.class, getPack(), getClazz()));
		add(AddFunD("onSearchDo", EMList.class, new ExtExp("array")));
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#initFunsAddAct(irille.pub.html.Exts.ExtAct)
	 */
	@Override
	public void initFunsAddAct(ExtAct act) {
		// if (_print)
		// selectionchangePrint(fun);
		IEnumOpt oact = act.getAct().getAct();
		if (oact == OAct.INS) {
			add(AddFun("onSaveRecord", EMList.class).addFunParasExp("form, data"));

		}
		if (oact == OAct.UPD) {
			add(AddFun("onUpdateRecord", EMList.class, getPack(), getClazz()).addFunParasExp("form, data"));
			add(act.getFun());
			add(getActsLine().getAct(OAct.UPD_ROW).getFun());
			add(AddFun("onUpdWin", EMList.class, getPack(), getClazz()).addFunParasExp("selection"));
			return;
		}

		if (oact == OAct.DEL) {
			add(act.getFun());
			add(getActsLine().getAct(OAct.DEL_ROW).getFun());
			return;
		}

		super.initFunsAddAct(act);
	}

	public String getExtend() {
		return _extend;
	}

	public void setExtendRow() {
		_extend = "mvc.tools.RowexpanderGrid";
	}

	/**
	 * 整个列表组件初始化方法入口
	 * 
	 * @param fun
	 */
	@Override
	public void initComponent(ExtFunDefine fun) {
		// 明细行功能定义
		initLineActs(fun);

		// 主界面功能定义
		initMainActs(fun);

		// 单元格定义 //表格列定义：包括名称、对应模型名、排序及渲染器
		fun.add("this.columns = ");
		fun.add(getColumns());
		if (getIndexGoods() > 0)
			fun.add("		mvc.Tools.doGoodsLine(this.columns, " + getIndexGoods() + ");");
		fun.add(loadFunCode(EMList.class, "initComponent", getPack(), getClazz()));
//@formatter:off	
/** Begin initComponent ********
		if (mainActs.length > 0)
			this.tbar=mainActs;
		this.store=Ext.create('mvc.store.【0】.【1】'); 
		this.store.remoteFilter = true;
		this.store.proxy.filterParam = 'filter';
		this.on({cellclick:mvc.Tools.onCellclick});
*** End initComponent *********/
//@formatter:on

		fun.add("this.dockedItems=");
		fun.AddDime().add(getFormDocked()).add(getFormTable());
		fun.add("		this.callParent(arguments);");
		fun.add("		mvc.Tools.onENTER2SearchBar(this.down('[dock=top]'),this);");
	}

	/**
	 * 
	 * @param v
	 */
	public void initFormDocked() {
		ExtList form = getFormDocked();
		form.add("dock", "top") // 表格顶部工具条，包括搜索条件及按钮
		    .add(XTYPE, "toolbar");

		initFormSearch();
		form.add(ITEMS, getFormSearch());
	}

	// 搜索栏定义
	public void initFormSearch() {
		ExtDime form = getFormSearch();
		for (VFld vfld : _searchVFlds.getVFlds()) {
			vfld.search(getFormSearch());
		}
		Exts.addButton(form.AddList(), "撤销", EXP_THIS, "win-close-icon", "this.onSearchCancel");
		ExtList elist = form.AddList();
		Exts.addButtonSplit(elist, "搜索", EXP_THIS, "win-ok-icon", "this.onSearch");
		elist.addExp("menu", "[{text:'高级搜索',iconCls : 'win-ok-icon', scope : this,handler: this.onSearchAdv}]");
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

	/**
	 * 明细行功能定义
	 * 
	 * @param fun
	 */
	public void initLineActs(ExtFunDefine fun) {
		if (!isLineActs())
			return;

		fun.add("var lineActs = [];"); // 表格行上的功能图标
		IEnumOpt oact;
		for (ExtAct act : getActsLine().getActs()) {
			initLineAct(fun, act);
		}
	}

	public void initLineAct(ExtFunDefine fun, ExtAct act) {
		fun.add("		if (this.roles.indexOf('" + actCodeCutRow(act.getAct()) + "') != -1)" + LN);
		fun.addParas("lineActs.push", act);
	}

	/**
	 * 主界面功能定义
	 * 
	 * @param fun
	 */
	public void initMainActs(ExtFunDefine fun) {
		fun.add("var mainActs = [];");

		IEnumOpt oact;
		for (ExtAct act : getActs().getActs()) {
			initMainAct(fun, act);
		}
	}

	public void initMainAct(ExtFunDefine fun, ExtAct act) {
		fun.add("		if (this.roles.indexOf('" + act.getAct().getCode() + "') != -1)" + LN);
		fun.addParas("mainActs.push", act);
	}

	/**
	 * 行选择事件
	 * 
	 * @param fun
	 */
	public void selectionChanges(ExtFunDefine fun) {
		IEnumOpt oact;
		for (ExtAct act : getActs().getActs()) {
			selectionChange(fun, act);
		}
	}

	public void selectionChange(ExtFunDefine fun, ExtAct act) {
		System.err.println(act.getCode()+":"+act.getAct().isNeedSel());
		if (act.getAct().isNeedSel() == false)
			return;
		fun.add("		if (this.roles.indexOf('" + act.getAct().getCode() + "') != -1)" + LN);
		fun.add("			this.down('#'+this.oldId+'" + act.getAct().getCode() + "').setDisabled(selected.length === 0);	" + LN);
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#initColumns()
	 */
	@Override
	public void initColumns() {
		super.initColumns();
		int index=0;
		for (VFld fld : getVFlds().getVFlds()) {
			if (fld.getFld() instanceof FldLines || fld.getFld() instanceof FldV ||
					fld.getCode().equals("rowVersion"))
				continue;
			//忽略ONE2ONE情况
			if (fld.getCode().equals("pkey") && fld.getFld() instanceof FldOutKey == false) {
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
		if (_lineActs)
			columnsAttr(getColumns().AddList());
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#setFldAttr(irille.pub.view.VFld,
	 * irille.pub.html.ExtList)
	 */
	@Override
	public void setFldAttr(VFld fld, ExtList fldList) {
		fld.list(fldList);
		initOutFldJump(fld, fldList);
		super.setFldAttr(fld, fldList);
	}

	public void columnsAttr(ExtList v) {
		v.add(XTYPE, "actioncolumn").add(WIDTH, 80).add(SORTABLE, false).add("menuDisabled", true).add("header", "操作")
		    .addExp(ITEMS, "lineActs");
	}

	//@formatter:off	
/** Begin onEdit ********
		var selection = this.getView().getSelectionModel().getSelection()[0];
		if (selection){
			var win = Ext.create('mvc.view.【0】.【1】.WinEdit',{
				title : this.title+'['+selection.get('bean.name')+']>编辑',
				record : selection
			});
			win.show();
			win.loadData();
		}	
*** End onEdit *********/

/** Begin onSaveRecord ********
		this.store.insert(0,data);
		this.getView().select(0);
		Ext.example.msg(msg_title, msg_text);	
*** End onSaveRecord *********/

	/** Begin onIns ********
		var win = Ext.create('mvc.view.【0】.【1】.Win',{
			title : this.title+'>新增'
		});
		win.on('create',this.onSaveRecord,this);
		win.show();		
	*** End onIns *********/

		/** Begin onUpdateRecord ********
		var selection = this.getView().getSelectionModel().getSelection()[0];
		var bean = Ext.create('mvc.model.【0】.【1】', data);
		Ext.apply(selection.data,bean.data);
		selection.commit();
		this.getView().select(selection);
		Ext.example.msg(msg_title, msg_text);			
		*** End onUpdateRecord *********/

		/** Begin onUpd ********
		var selection = this.getView().getSelectionModel().getSelection()[0];
		this.onUpdWin(selection);
		*** End onUpd *********/

		/** Begin onUpdRow ********
		var selection = this.getStore().getAt(rowIndex);
		this.getView().deselect(this.getView().getSelectionModel().getSelection());
		this.getView().select(selection);
		this.onUpdWin(selection);			
		*** End onUpdRow *********/

		/** Begin onUpdWin ********
		if (selection){
			var win = Ext.create('mvc.view.【0】.【1】.Win',{
				title : this.title+'>修改',
				insFlag : false
			});
			win.on('create',this.onUpdateRecord,this);
			win.show();
			win.setActiveRecord(selection);
		}			
		*** End onUpdWin *********/

		/** Begin onDel ********
		var selection = this.getView().getSelectionModel().getSelection();
		if (selection){
			var me = this;
			Ext.MessageBox.confirm(msg_confirm_title, msg_confirm_msg, 
				function(btn) {
					if (btn != 'yes')
						return;
					var arr=new Array();
					var arrv = new Array();
					for(var i = 0; i < selection.length; i++){
						arr.push(selection[i].get('bean.pkey'));
						arrv.push(selection[i].get(BEAN_VERSION));
					}
					Ext.Ajax.request({
						url : base_path+'/【0】_【1】_delMulti?pkeys='+arr.toString()+'&rowVersions='+arrv.toString(),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								me.getStore().remove(selection);
								Ext.example.msg(msg_title, msg_del);
							}else{
								Ext.MessageBox.show({
									title : msg_title, 
									msg : result.msg,
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.ERROR
								});
							}
						}
					});
				}
			);
		}
		*** End onDel *********/

		/** Begin onDelRow ********
		var me = this;
		var row = me.getStore().getAt(rowIndex);
		Ext.MessageBox.confirm(msg_confirm_title, msg_confirm_msg,
			function(btn) {
				if (btn != 'yes')
					return;
				Ext.Ajax.request({
					url : base_path+'/【0】_【1】_del?pkey='+row.get('bean.pkey')+'&rowVersion='+row.get(BEAN_VERSION),
					success : function (response, options) {
						var result = Ext.decode(response.responseText);
						if (result.success){
							me.getStore().removeAt(rowIndex);
							Ext.example.msg(msg_title, msg_del);
						}else{
							Ext.MessageBox.show({ 
								title : msg_title,
								msg : result.msg,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
						}
					}
				});
			}
		);
		*** End onDelRow *********/

		/** Begin onSearchCancel ********
		this.getSelectionModel().deselectAll();
		mvc.Tools.searchClear(this.down('toolbar'));
		this.store.clearFilter();
		*** End onSearchCancel *********/

		/** Begin onSearch ********
		var array = mvc.Tools.searchValues(this.down('toolbar'));
		this.onSearchDo(array);
		*** End onSearch *********/
	
	/** Begin onSearchDo ********
		this.getSelectionModel().deselectAll();
		if (array.length == 0){
			this.store.clearFilter();
			return;
		}
		this.store.clearFilter(true);
		this.store.filter(array);
	*** End onSearchDo *********/
	
	/** Begin onSearchAdv ********
		var win = Ext.create('mvc.view.【0】.【1】.WinSearch',{
			title : this.title+'>高级搜索',
			listCmp : this
		});
		win.show();
	*** End onSearchAdv *********/
	
	/** Begin onPrint ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var key = selection.get('bean.pkey');
		window.open('/print/General/PrintReport.jsp?report=【0】.grf&data=【0】.jsp&pkey='+key,'_blank');		 
	 *** End onPrint *********/
	
	/** Begin onDoEnabled ********
		var selection = this.getView().getSelectionModel().getSelection();
		if (selection) {
			var me = this;
			Ext.MessageBox.confirm(msg_confirm_title, '您确认要启用所选的记录吗?',
					function(btn) {
						if (btn != 'yes')
							return;
						var arr = new Array();
						var arrv = new Array();
						for (var i = 0; i < selection.length; i++) {
							arr.push(selection[i].get('bean.pkey'));
							arrv.push(selection[i].get(BEAN_VERSION));
						}
						Ext.Ajax.request({
							url : base_path + '/【0】_【1】_doEnabled?pkeys='+arr.toString()+'&rowVersions='+arrv.toString(),
							success : function(response, options) {
								var result = Ext.decode(response.responseText);
								if (result.success) {
									me.getStore().reload();
									me.getView().deselect(me.getView().getSelectionModel().getSelection());
									Ext.example.msg(msg_title, msg_submit);
								} else {
									Ext.MessageBox.show({
										title : msg_title,
										msg : result.msg,
										buttons : Ext.MessageBox.OK,
										icon : Ext.MessageBox.ERROR
									});
								}
							}
						});
					});
		}
	*** End onDoEnabled *********/
	
	/** Begin onUnEnabled ********
		var selection = this.getView().getSelectionModel().getSelection();
		if (selection) {
			var me = this;
			Ext.MessageBox.confirm(msg_confirm_title, '您确认要停用所选的记录吗?',
					function(btn) {
						if (btn != 'yes')
							return;
						var arr = new Array();
						var arrv = new Array();
						for (var i = 0; i < selection.length; i++) {
							arr.push(selection[i].get('bean.pkey'));
							arrv.push(selection[i].get(BEAN_VERSION));
						}
						Ext.Ajax.request({
							url : base_path + '/【0】_【1】_unEnabled?pkeys='+arr.toString()+'&rowVersions='+arrv.toString(),
							success : function(response, options) {
								var result = Ext.decode(response.responseText);
								if (result.success) {
									me.getStore().reload();
									me.getView().deselect(me.getView().getSelectionModel().getSelection());
									Ext.example.msg(msg_title, msg_submit);
								} else {
									Ext.MessageBox.show({
										title : msg_title,
										msg : result.msg,
										buttons : Ext.MessageBox.OK,
										icon : Ext.MessageBox.ERROR
									});
								}
							}
						});
					});
		}
	 *** End onUnEnabled *********/
	
		//@formatter:on		

	/**
	 * @return the lineActs
	 */
	public boolean isLineActs() {
		return _lineActs;
	}

	/**
	 * @param lineActs
	 *          the lineActs to set
	 */
	public void setLineActs(boolean lineActs) {
		_lineActs = lineActs;
	}

	/**
	 * @return the searchFlds
	 */
	public VFlds getSearchVFlds() {
		return _searchVFlds;
	}

	/**
	 * @param searchVFlds
	 *          the searchFlds to set
	 */
	public T setSearchVFlds(VFlds... searchVFlds) {
		_searchVFlds = VFlds.newVFlds(searchVFlds);
		return (T) this;
	}

	/**
	 * @return the actsLine
	 */
	public ExtActs getActsLine() {
		return _actsLine;
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
	public ExtDime getFormSearch() {
		return _formSearch;
	}

	/**
	 * @return the formTable
	 */
	public ExtList getFormTable() {
		return _formTable;
	}

}
