package irille.pub.html;

import irille.gl.gl.GlJournal;
import irille.gl.gl.GlJournalLine;
import irille.pub.ext.Ext;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * 属性类的基类
 * 
 * @author whx
 */
public class EMZipList<T extends EMZipList> extends EMBase<T> {
	private boolean _upd, _ins, _del;
	private boolean _lineActs; // 是否有明细操作按钮
	private VFlds _searchVFlds=new VFlds();
	private VFlds _outVFlds = _searchVFlds;
	private ExtList _formDocked = new ExtList();
	private ExtList _formSearch = new ExtList();
	private ExtList _formListMain = new ExtList();
	private ExtList _formMainAct = new ExtList();
	private ExtList _formMainTable = new ExtList();
	private ExtList _formTabpanel = new ExtList();
	private ExtList _columnsOut = new ExtList();
	private ExtDime _columnsSearch = new ExtDime();
	private int _tableColumns = 3;

	public EMZipList(Tb tb, VFlds... vflds) {
		super(tb, vflds);
		_upd = tb.checkAct("upd") != null;
		_ins = tb.checkAct("ins") != null;
		_del = tb.checkAct("del") != null;
		_lineActs = _upd || _del; // 是否有明细操作按钮
	}

	public static void main(String[] args) {
		Tb tb = GlJournal.TB;
		VFlds flds = new VFlds(GlJournal.T.CODE, GlJournal.T.NAME, GlJournal.T.CELL, GlJournal.T.SUBJECT,
		    GlJournal.T.DIRECT, GlJournal.T.BALANCE, GlJournal.T.BALANCE_USE);
		EMZipList form = new EMZipList(tb, flds);
		form.setTableColumns(3);
		form.setSearchVFlds(new VFlds(GlJournal.T.CODE, GlJournal.T.NAME, GlJournal.T.SUBJECT))
		    .setOutVFlds(new VFlds(GlJournalLine.T.MAIN_PKEY)).backupFiles().crtFile().cmpFileIgnoreBlank();
	}

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
		add(EXTEND, "Ext.panel.Panel").add("oldId", getClazz() + "_list_").add("loadMask", true).add("multiSelect", true)
		    .add("roles", "").add(LAYOUT, "border").add("lock", true).addExp("mdSearch", "null").addExp("mdAct", "null")
		    .addExp("mdMain", "null").addExp("mdMainTable", "null").addExp("mdLineTable", "null");
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#initActs()
	 */
	@Override
	public void initActs() {
		super.initActs();
		loadTbActs(EMZipList.class); // 根据Tb对象的功能建立ExtAct对象，List功能被忽略
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#loadTbAct(java.lang.Class, irille.action.Act)
	 */
	@Override
	public void loadTbAct(Class funCodeFile, Act act) {
		IEnumOpt oact = act.getAct();
		if (oact != OAct.INS && oact != OAct.EDIT && oact != OAct.UPD && oact != OAct.DEL && oact != OAct.DO_APPR
		    && oact != OAct.UN_APPR && oact != OAct.DO_TALLY && oact != OAct.UN_TALLY && oact != OAct.DO_ENABLED
		    && oact != OAct.UN_ENABLED && oact != OAct.DO_NOTE && oact != OAct.PRINT)
			return;
		ExtAct v = null;
		if (oact == OAct.DO_APPR || oact == OAct.UN_APPR ||
				oact == OAct.DO_TALLY || oact == OAct.UN_TALLY)
			v = new ExtAct(this, act, funCodeFile, getPack(), getClazz(), getTb().getName());
		else if (oact == OAct.DO_NOTE)
			v = new ExtAct(this, act, funCodeFile, getTb().getClazz().getName());
		else if (oact == OAct.PRINT)
			v = new ExtAct(this, act, funCodeFile, getTb().getClazz().getSimpleName());
		else
			v = new ExtAct(this, act, funCodeFile);
		v.add(TEXT, act.getName()).add(ICON_CLS, act.getIcon()).addExp("itemId", "this.oldId+'" + act.getCode() + "'")
		    .add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper());
		getActs().add(v);
		if (oact == OAct.EDIT || oact == OAct.UPD || oact == OAct.DEL || oact == OAct.DO_APPR || oact == OAct.UN_APPR
				|| oact == OAct.DO_TALLY || oact == OAct.UN_TALLY || oact == OAct.DO_NOTE
		    || oact == OAct.DO_ENABLED || oact == OAct.UN_ENABLED || oact == OAct.PRINT) {
			v.addExp("disabled", "this.lock");
		}

	}

	@Override
	public void initFuns() {

		AddFun("getStore", EMZipList.class);

		super.initFuns();
		add(AddFun("onSearchCancel", EMZipList.class));
		add(AddFun("onSearch", EMZipList.class));
		add(AddFun("onSearchAdv", EMZipList.class, getPack(), getClazz()));
		add(AddFunD("onSearchDo", EMZipList.class, new ExtExp("array")));
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
			add(AddFun("onSaveRecord", EMZipList.class).addFunParasExp("form, data"));
		}
		super.initFunsAddAct(act);
	}

	// 主表字段信息定义
	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#initColumns()
	 */
	@Override
	public void initColumns() {
		super.initColumns();
			for (VFld vfld : getVFlds().getVFlds())
				setFldAttr(vfld, getColumns().AddList());

		initColumnsOut();
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#setFldAttr(irille.pub.view.VFld,
	 * irille.pub.html.ExtList)
	 */
	@Override
	public void setFldAttr(VFld fld, ExtList fldList) {
		fld.form(fldList, true);
		super.setFldAttr(fld, fldList);
	}

	// 从表信息定义
	public void initColumnsOut() {
		ExtList form = getColumnsOut();

			for (VFld vfld : _outVFlds.getVFlds()) {
				ExtList l = form
				    .AddFunCall(
				        "xtype : Ext.create",
				        "mvc.view." + getPack() + "." + getClazz() + ".ListLine"
				            + vfld.getFld().getTb().getClazz().getSimpleName()).AddFunParaList();
				l.add("title", vfld.getFld().getTb().getName()).addExp("itemId", "this.oldId+'linetable'")
				    .add(ICON_CLS, "tab-user-icon");
			}
	}

	public void initComponent(ExtFunDefine fun) {
		// 主界面功能定义
		initMainActs(fun);

		ExtDime dime = fun.add("		this.items =").AddDime();
		dime.add(getFormDocked());

		dime.add(getFormTabpanel());

		fun.add(loadFunCode(EMZipList.class, "initComponent"));
//@formatter:off	
/** Begin initComponent ********
		this.callParent(arguments);
		this.mdSearch = this.down('#'+this.oldId+'search');
		this.mdAct = this.down('#'+this.oldId+'act');
		this.mdMain = this.down('#'+this.oldId+'main');
		this.mdMainTable = this.down('#'+this.oldId+'maintable');
		this.mdLineTable = this.down('#'+this.oldId+'linetable');
		mvc.Tools.onENTER2SearchBar(this.mdSearch,this);
		if (mainActs.length == 0)
			this.down('[region=north]').remove(this.mdAct);
*** End initComponent *********/
//@formatter:on
	}

	public void initFormListMain() {
		ExtList form = getFormListMain();
		ExtList l = form.AddFunCall("xtype : Ext.create", "mvc.view." + getPack() + "." + getClazz() + ".ListMain")
		    .AddFunParaList();
		l.add("title", getTb().getName()).addExp("itemId", "this.oldId+'maintable'").add(ICON_CLS, "tab-user-icon")
		    .addExp("roles", "this.roles");
		if (getTb().chk("status") && getTb().checkAct(OAct.DO_APPR) != null && getTb().checkAct(OAct.PRINT) != null)
			l.addExp("listeners", loadFunCode(EMZipList.class, "initFormListMainStatusPrint", _outVFlds.get(0).getCode()));
		else if (getTb().chk("status") && getTb().checkAct(OAct.DO_APPR) != null)
			l.addExp("listeners", loadFunCode(EMZipList.class, "initFormListMainStatus", _outVFlds.get(0).getCode()));
		else if (getTb().chk("status") && getTb().checkAct(OAct.EDIT) != null)
			l.addExp("listeners", loadFunCode(EMZipList.class, "initFormListMainEdit", _outVFlds.get(0).getCode()));
		else
			l.addExp("listeners", loadFunCode(EMZipList.class, "initFormListMain", _outVFlds.get(0).getCode()));
		//@formatter:off	
					/** Begin initFormListMain ********
							 {
								scope : this,
				                selectionchange: function(model, records) {
				                    if (records.length === 1){
				                        this.mdMain.getForm().loadRecord(records[0]);
        								this.mdLineTable.store.filter([{'id':'filter', 'property':'【0】','value':records[0].get('bean.pkey')}]);
    									if (this.roles.indexOf('upd') != -1)
											this.down('#'+this.oldId+'upd').setDisabled(false);
										if (this.roles.indexOf('del') != -1)
											this.down('#'+this.oldId+'del').setDisabled(false);
				                    }else{
				                    	this.mdMain.getForm().reset();
				                    	this.mdLineTable.store.removeAll();
				                    	if (this.roles.indexOf('upd') != -1)
											this.down('#'+this.oldId+'upd').setDisabled(true);
										if (this.roles.indexOf('del') != -1)
											this.down('#'+this.oldId+'del').setDisabled(true);
				                    }
				                }
			                }
					*** End initFormListMain *********/
		/** Begin initFormListMainEdit ********
		 {
			scope : this,
           selectionchange: function(model, records) {
               if (records.length === 1){
                   this.mdMain.getForm().loadRecord(records[0]);
					this.mdLineTable.store.filter([{'id':'filter', 'property':'【0】','value':records[0].get('bean.pkey')}]);
					if (this.roles.indexOf('upd') != -1)
						this.down('#'+this.oldId+'upd').setDisabled(false);
					if (this.roles.indexOf('del') != -1)
						this.down('#'+this.oldId+'del').setDisabled(false);
					if (this.roles.indexOf('edit') != -1)
						this.down('#'+this.oldId+'edit').setDisabled(false);
               }else{
               	this.mdMain.getForm().reset();
               	this.mdLineTable.store.removeAll();
               	if (this.roles.indexOf('upd') != -1)
						this.down('#'+this.oldId+'upd').setDisabled(true);
					if (this.roles.indexOf('del') != -1)
						this.down('#'+this.oldId+'del').setDisabled(true);
					if (this.roles.indexOf('edit') != -1)
						this.down('#'+this.oldId+'edit').setDisabled(true);
               }
           }
       }
*** End initFormListMainEdit *********/
		
		/** Begin initFormListMainStatus ********
												{
												scope : this,
				                selectionchange: function(model, records) {
				                    if (records.length === 1){
				                        this.mdMain.getForm().loadRecord(records[0]);
        								this.mdLineTable.store.filter([{'id':'filter', 'property':'【0】','value':records[0].get('bean.pkey')}]);
        								var status = records[0].get('bean.status'); //根据单据状态判断
        								//初始状态
        								if (status==STATUS_INIT){
	    									if (this.roles.indexOf('upd') != -1)
												this.down('#'+this.oldId+'upd').setDisabled(false);
											if (this.roles.indexOf('del') != -1)
												this.down('#'+this.oldId+'del').setDisabled(false);
											if (this.roles.indexOf('doAppr') != -1)
												this.down('#'+this.oldId+'doAppr').setDisabled(false);
											if (this.roles.indexOf('unAppr') != -1)
												this.down('#'+this.oldId+'unAppr').setDisabled(true);
        								}else if (status==STATUS_APPROVE){
        									if (this.roles.indexOf('upd') != -1)
												this.down('#'+this.oldId+'upd').setDisabled(true);
											if (this.roles.indexOf('del') != -1)
												this.down('#'+this.oldId+'del').setDisabled(true);
											if (this.roles.indexOf('doAppr') != -1)
												this.down('#'+this.oldId+'doAppr').setDisabled(true);
											if (this.roles.indexOf('unAppr') != -1)
												this.down('#'+this.oldId+'unAppr').setDisabled(false);
        								}
				                    }else{
				                    	this.mdMain.getForm().reset();
				                    	this.mdLineTable.store.removeAll();
				                    	if (this.roles.indexOf('upd') != -1)
											this.down('#'+this.oldId+'upd').setDisabled(true);
										if (this.roles.indexOf('del') != -1)
											this.down('#'+this.oldId+'del').setDisabled(true);
										if (this.roles.indexOf('doAppr') != -1)
											this.down('#'+this.oldId+'doAppr').setDisabled(true);
										if (this.roles.indexOf('unAppr') != -1)
											this.down('#'+this.oldId+'unAppr').setDisabled(true);
				                    }
				                }
				            }
		 *** End initFormListMainStatus *********/
		
		/** Begin initFormListMainStatusPrint ********
								{
								scope : this,
						    selectionchange: function(model, records) {
						        if (records.length === 1){
						            this.mdMain.getForm().loadRecord(records[0]);
								this.mdLineTable.store.filter([{'id':'filter', 'property':'【0】','value':records[0].get('bean.pkey')}]);
								var status = records[0].get('bean.status'); //根据单据状态判断
								//初始状态
								if (status==STATUS_INIT){
								if (this.roles.indexOf('upd') != -1)
								this.down('#'+this.oldId+'upd').setDisabled(false);
							if (this.roles.indexOf('del') != -1)
								this.down('#'+this.oldId+'del').setDisabled(false);
							if (this.roles.indexOf('print') != -1)
								this.down('#'+this.oldId+'print').setDisabled(false);
							if (this.roles.indexOf('doAppr') != -1)
								this.down('#'+this.oldId+'doAppr').setDisabled(false);
							if (this.roles.indexOf('unAppr') != -1)
								this.down('#'+this.oldId+'unAppr').setDisabled(true);
								}else if (status==STATUS_APPROVE){
									if (this.roles.indexOf('upd') != -1)
								this.down('#'+this.oldId+'upd').setDisabled(true);
							if (this.roles.indexOf('del') != -1)
								this.down('#'+this.oldId+'del').setDisabled(true);
							if (this.roles.indexOf('doAppr') != -1)
								this.down('#'+this.oldId+'doAppr').setDisabled(true);
							if (this.roles.indexOf('unAppr') != -1)
								this.down('#'+this.oldId+'unAppr').setDisabled(false);
								}
						        }else{
						        	this.mdMain.getForm().reset();
						        	this.mdLineTable.store.removeAll();
						        	if (this.roles.indexOf('upd') != -1)
							this.down('#'+this.oldId+'upd').setDisabled(true);
						if (this.roles.indexOf('del') != -1)
							this.down('#'+this.oldId+'del').setDisabled(true);
						if (this.roles.indexOf('print') != -1)
							this.down('#'+this.oldId+'print').setDisabled(true);
						if (this.roles.indexOf('doAppr') != -1)
							this.down('#'+this.oldId+'doAppr').setDisabled(true);
						if (this.roles.indexOf('unAppr') != -1)
							this.down('#'+this.oldId+'unAppr').setDisabled(true);
						        }
						    }
						}
*** End initFormListMainStatusPrint *********/
		
					//@formatter:on
	}

	/**
	 * 
	 * @param v
	 */
	public void initFormDocked() {
		ExtList form = getFormDocked();
		form.add("region", "north") // 表格顶部工具条，包括搜索条件及按钮
		    .add(XTYPE, "panel").add(BORDER, false);

		initFormSearch();
		ExtDime items = form.AddDime(ITEMS, getFormSearch());

		initFormMainAct();
		items.add(getFormMainAct());

		items.add(getForm());

	}

	//@formatter:off
	public void initFormMainAct() {
		ExtList form = getFormMainAct();
		form.add(XTYPE, "toolbar")
			.addExp("itemId", "this.oldId+'act'")
			.addExp(ITEMS, "mainActs");
	}

	// 搜索栏定义
	public void initFormSearch() {
		ExtList form = getFormSearch();
		form.add(XTYPE,"toolbar")
			.addExp("itemId","this.oldId+'search'");
		
		initColumnsSearch();
		form.add(ITEMS,getColumnsSearch());
			
	}
	public void initColumnsSearch() {
		ExtDime form = getColumnsSearch();
			for (VFld vfld : _searchVFlds.getVFlds())
				vfld.search(form);
		Exts.addButton(form.AddList(), "撤销", EXP_THIS, "win-close-icon",
				"this.onSearchCancel");
		ExtList elist = form.AddList();
		Exts.addButtonSplit(elist, "搜索", EXP_THIS, "win-ok-icon",
				"this.onSearch");
		elist.addExp("menu", "[{text:'高级搜索',iconCls : 'win-ok-icon', scope : this,handler: this.onSearchAdv}]");
	}

	public void initFormMainTable() {
		ExtList form = getFormMainTable();
		form.add(XTYPE,"fieldset")
			.add("title",getTb().getName()+"信息")
			.add("collapsible",true)
			.AddList("layout")
				.add(TYPE,"table")
				.add(COLUMNS,getTableColumns());
		
		form.add(ITEMS,getColumns());  //主表字段
	}

	public void initFormTabpanel() {
		ExtList form = getFormTabpanel();
		form.add("region","center")
			.add(XTYPE,"tabpanel")
			.AddList("tabBar").add("style","background:#fff");
		
		ExtDime items=form.AddDime(ITEMS);
		initFormListMain();
		items.add(getFormListMain())
		.add(getColumnsOut());
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.EMBase#initForm()
	 */
	@Override
	public void initForm() {
		super.initForm();
		initFormDocked();
		ExtList form = getForm();
		form.add(XTYPE,"form")
			.addExp("itemId"," this.oldId+'main'")
			.add("bodyPadding","5 5 0 5")
			.AddList("fieldDefaults")
				.add(ANCHOR,"100%")
				.add(LABEL_WIDTH,100)
				.add(WIDTH,275)
				.add("labelAlign","right")
				.add(READ_ONLY,true);
		
		initFormTabpanel();
		initFormMainTable();
		form.AddDime(ITEMS, getFormMainTable());
		
	//@formatter:on
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

	//@formatter:off	
		/** Begin getStore ********
		return this.mdMainTable.store;
		*** End getStore *********/

/** Begin onSaveRecord ********
		this.mdMainTable.store.insert(0,data);
		this.mdMainTable.getView().select(0);
		Ext.example.msg(msg_title, msg_text);
*** End onSaveRecord *********/

	/** Begin onIns ********
		var win = Ext.create('mvc.view.【0】.【1】.Win',{
			title : this.title+'>新增'
		});
		win.on('create',this.onSaveRecord,this);
		win.show();
	*** End onIns *********/

		/** Begin onUpd ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		this.mdMainTable.onUpdWin(selection);
		*** End onUpd *********/

		/** Begin onDel ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection();
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
								me.mdMainTable.getStore().remove(selection);
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
	
	/** Begin onDoAppr ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.MessageBox.confirm(msg_confirm_title, '【2】['+selection.get('bean.code') + '] - 审核确认吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/【0】_【1】_approve?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								var bean  = Ext.create('mvc.model.【0】.【1】',result);
								Ext.apply(selection.data, bean.data);
								selection.commit();
								me.mdMainTable.getSelectionModel().deselectAll();
								me.mdMainTable.getView().select(selection);
								Ext.example.msg(msg_title, '审核--成功');
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
	*** End onDoAppr *********/
	
	/** Begin onUnAppr ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.MessageBox.confirm(msg_confirm_title, '【2】['+selection.get('bean.code') + '] - 弃审确认吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/【0】_【1】_unapprove?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								var bean  = Ext.create('mvc.model.【0】.【1】',result);
								Ext.apply(selection.data, bean.data);
								selection.commit();
								me.mdMainTable.getSelectionModel().deselectAll();
								me.mdMainTable.getView().select(selection);
								Ext.example.msg(msg_title, '弃审--成功');
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
*** End onUnAppr *********/
	
	/** Begin onDoTally ********
	var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
	var me = this;
	if (selection){
		Ext.MessageBox.confirm(msg_confirm_title, '【2】['+selection.get('bean.code') + '] - 记账确认吗？',
			function(btn) {
				if (btn != 'yes')
					return;
				Ext.Ajax.request({
					url : base_path+'/【0】_【1】_tally?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
					success : function (response, options) {
						var result = Ext.decode(response.responseText);
						if (result.success){
							var bean  = Ext.create('mvc.model.【0】.【1】',result);
							Ext.apply(selection.data, bean.data);
							selection.commit();
							me.mdMainTable.getSelectionModel().deselectAll();
							me.mdMainTable.getView().select(selection);
							Ext.example.msg(msg_title, '记账--成功');
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
*** End onDoTally *********/

/** Begin onUnTally ********
	var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
	var me = this;
	if (selection){
		Ext.MessageBox.confirm(msg_confirm_title, '【2】['+selection.get('bean.code') + '] - 记账取消确认吗？',
			function(btn) {
				if (btn != 'yes')
					return;
				Ext.Ajax.request({
					url : base_path+'/【0】_【1】_untally?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
					success : function (response, options) {
						var result = Ext.decode(response.responseText);
						if (result.success){
							var bean  = Ext.create('mvc.model.【0】.【1】',result);
							Ext.apply(selection.data, bean.data);
							selection.commit();
							me.mdMainTable.getSelectionModel().deselectAll();
							me.mdMainTable.getView().select(selection);
							Ext.example.msg(msg_title, '记账取消--成功');
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
*** End onUnTally *********/
	
	/** Begin onDoNote ********
	var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
	if (selection){
		var win = Ext.create('mvc.view.gl.GlNote.WinNote',{
			title : this.title+'>记账便签',
			insFlag : false,
			tableCode : '【0】'
		});
		win.show();
		win.setActiveRecord(selection);
	}
	 *** End onDoNote *********/

		/** Begin onSearchCancel ********
		this.mdMainTable.getSelectionModel().deselectAll();
		mvc.Tools.searchClear(this.mdSearch);
		this.mdMainTable.store.clearFilter();
		*** End onSearchCancel *********/

		/** Begin onSearch ********
		var array = mvc.Tools.searchValues(this.mdSearch);
		this.onSearchDo(array);
		*** End onSearch *********/
	
	/** Begin onSearchDo ********
		this.mdMainTable.getSelectionModel().deselectAll();
		if (array.length == 0){
			this.mdMainTable.store.clearFilter();
			return;
		}
		this.mdMainTable.store.clearFilter(true);
		this.mdMainTable.store.filter(array);
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
	var selection = this.mdMainTable.getView().getSelectionModel().getSelection();
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
								me.mdMainTable.getSelectionModel().deselectAll();
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
	var selection = this.mdMainTable.getView().getSelectionModel().getSelection();
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
								me.mdMainTable.getSelectionModel().deselectAll();
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
	/** Begin onEdit ********
	var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
	if (selection){
		var win = Ext.create('mvc.view.【0】.【1】.WinEdit',{
			title : this.title+'['+selection.get('bean.name')+']>编辑',
			record : selection
		});
		win.show();
		win.loadData();
	}	
*** End onEdit *********/	
	
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
	 * @return the outFlds
	 */
	public VFlds getOutVFlds() {
		return _outVFlds;
	}

	/**
	 * @param outVFlds
	 *          the outFlds to set
	 */
	public T setOutVFlds(VFlds... outVFlds) {
		_outVFlds =  VFlds.newVFlds(outVFlds);
		return (T) this;
	}

	/**
	 * @return the formDocked
	 */
	public ExtList getFormDocked() {
		return _formDocked;
	}

	/**
	 * @return the formSearch
	 */
	public ExtList getFormSearch() {
		return _formSearch;
	}

	/**
	 * @return the formListMain
	 */
	public ExtList getFormListMain() {
		return _formListMain;
	}

	/**
	 * @return the formMainAct
	 */
	public ExtList getFormMainAct() {
		return _formMainAct;
	}

	/**
	 * @return the formMainTable
	 */
	public ExtList getFormMainTable() {
		return _formMainTable;
	}

	/**
	 * @return the tableColumns
	 */
	public int getTableColumns() {
		return _tableColumns;
	}

	/**
	 * @param tableColumns
	 *          the tableColumns to set
	 */
	public void setTableColumns(int tableColumns) {
		_tableColumns = tableColumns;
	}

	/**
	 * @return the formTabpanel
	 */
	public ExtList getFormTabpanel() {
		return _formTabpanel;
	}

	/**
	 * @return the columnsOut
	 */
	public ExtList getColumnsOut() {
		return _columnsOut;
	}

	/**
	 * @return the columnsSearch
	 */
	public ExtDime getColumnsSearch() {
		return _columnsSearch;
	}
}
