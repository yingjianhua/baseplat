package irille.pub.html;

import irille.core.sys.SysCtypeCode;
import irille.pub.FileTools;
import irille.pub.ext.Ext;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * 
 * @author whx
 * 
 */
public class EMListEdit<T extends EMListEdit> extends EMBase<T> {
	private VFld _outfld;
	private ExtList _formDocked = new ExtList();

	/**
	 * 
	 */
	public EMListEdit(Tb tb,VFld outfld, VFlds... vflds) {
		super(tb,vflds);
		_outfld = outfld;
	}

	public static void main(String[] args) {
		Tb tb = SysCtypeCode.TB;
		VFlds flds = new VFlds().addAll(tb);
		flds.get("codeValue").setWidthList(120);
		flds.get("codeName").setWidthList(150);
		flds.get("codeDes").setWidthList(200);
		new EMListEdit(tb,
				SysCtypeCode.T.CTYPE_TYPE.getFld().getVFld(), flds).backupFiles().crtFile().cmpFileIgnoreBlank();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "view/" + getPack() + "/" + getClazz() + "/ListEdit.js";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.view." + getPack() + "." +  getClazz() + ".ListEdit";
	}

	@Override
	public void initAttrs() {
		super.initAttrs();
		add(EXTEND, "Ext.grid.Panel")
				.add("disableSelection", false)
				.add("loadMask", true)
				.addExp("cellEditing",
						"Ext.create('Ext.grid.plugin.CellEditing', { clicksToEdit: 1 })")
				.addExp("mainPkey", "null");
	}

	@Override
	public void initFuns() {
		AddFun("onLoadFirst", EMListEdit.class);
		initFunsAddActs();
		initFunsAddOtherFuns();
	}

	public void initComponent(ExtFunDefine fun) {
		// 主界面功能定义
		fun.add("var mainActs =");
		fun.add(getActs());

		fun.add(loadFunCode(EMListEdit.class, "initComponent1"));
//@formatter:off	
/** Begin initComponent1 ********
			if (this.isEdit)
				this.tbar=mainActs;
*** End initComponent1 *********/
//@formatter:on

		// 单元格定义
		fun.add("this.columns = ");
		fun.add(getColumns());

		fun.add(loadFunCode(EMListEdit.class, "initComponent2", getPack(), getClazz()));
		fun.add("		this.dockedItems=");
		fun.AddDime(getFormDocked());
		fun.add(loadFunCode(EMListEdit.class, "initComponent3"));
//@formatter:off	
/** Begin initComponent2 ********
		this.store=Ext.create('mvc.store.【0】.【1】');
		this.store.remoteFilter = true;
		this.store.proxy.filterParam = 'filter';
*** End initComponent2 *********/

		/** Begin initComponent3 ********
		if (this.isEdit)
			this.plugins = [this.cellEditing];
		this.callParent(arguments);
*** End initComponent3 *********/
//@formatter:on
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
		getForm().addExp(XTYPE,
				"Ext.create('mvc.tools.ComboxPaging',{myList : this})");
	}


	public void initFormDocked() {
		//@formatter:off	
		getFormDocked()
			.add("dock","top")
			.add(XTYPE,"pagingtoolbar")
			.addExp("store","this.store")
			.add("dock","bottom")
			.add("displayInfo",true)
			.add("displayMsg","显示 {0} - {1} 条，共计 {2} 条")
			.add("emptyMsg","没有数据")
			.AddDime(ITEMS, getForm());					
		//@formatter:on	
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.EMBase#initActs()
	 */
	@Override
	public void initActs() {
		super.initActs();
		AddAct(new Act(getTb(), OAct.INS), EMListEdit.class,  getPack(), getClazz());
		AddAct(new Act(getTb(), OAct.DEL),  EMListEdit.class);
		getActs().add("->");
		AddAct(new Act(getTb(), OAct.SAVE),  EMListEdit.class,  getPack(), getClazz());
		AddAct(new Act(getTb(), OAct.RESET),  EMListEdit.class, _outfld.getFld().getCode());
	}

	/* (non-Javadoc)
	 * @see irille.pub.html.EMBase#initColumns()
	 */
	@Override
	public void initColumns() {
		super.initColumns();
			for (VFld fld : getVFlds().getVFlds()) {
				if (fld.getFld() instanceof FldLines || fld.getFld() instanceof FldV ||
						fld.getCode().equals("rowVersion"))
					continue;
				if (fld.getCode().equals("pkey")) {
					if (getTb().isAutoIncrement())
						continue;
					if (getTb().isAutoLocal()
							&& fld.getFld().getJavaType().equals(String.class) == false)
						continue;
				}
				// 从表中作为外键的，界面上不必要显示，在ACTION中自动负值
				if (fld.getCode().equals(_outfld.getFld().getCode()))
					continue;
				// TODO 处理-各VFLD实现单元格编辑
				setFldAttr(fld, getColumns().AddList());
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.EMBase#setFldAttr(irille.pub.view.VFld,
	 * irille.pub.html.ExtList)
	 */
	@Override
	public void setFldAttr(VFld fld, ExtList fldList) {
		fld.listEdit(fldList);
		super.setFldAttr(fld, fldList);
	}
	//@formatter:off	
	/** Begin onIns ********
		var model = Ext.create('mvc.store.【0】.【1】');
        this.store.insert(0, model);
        this.cellEditing.startEditByPosition({row: 0, column: 0});
	*** End onIns *********/

		/** Begin onDel ********
		var selection = this.getView().getSelectionModel().getSelection();
		if (selection)
			this.getStore().remove(selection);
		*** End onDel *********/

		/** Begin onSave ********
		this.cellEditing.cancelEdit();
		var me = this,
			rtn,flds,
			syncValues = {};
		flds = this.store.model.getFields();
		rtn  = mvc.Tools.syncValues(this.store.getNewRecords(),flds,'insLines');
		Ext.apply(syncValues, rtn);
		rtn  = mvc.Tools.syncValues(this.store.getUpdatedRecords(),flds,'updLines');
		Ext.apply(syncValues, rtn);
		rtn  = mvc.Tools.syncValues(this.store.getRemovedRecords(),flds,'delLines');
		Ext.apply(syncValues, rtn);
		var change = false;
		for(var tmp in syncValues){
			change = true;
			break;
		}
		if (change){
			Ext.Ajax.request({
				url : base_path+'/【0】_【1】_sync?mainPkey='+me.mainPkey,
				params : syncValues,
				success : function (response, options) {
					var result = Ext.decode(response.responseText);
					if (result.success){
						me.onReset();
						Ext.example.msg(msg_title, msg_text);
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
		*** End onSave *********/

/** Begin onReset ********
		this.store.filter([{'id':'main','property':'【0】','value':this.mainPkey}]);
*** End onReset *********/

/** Begin onLoadFirst ********
		if (this.isLoadFirst)
			return;
		this.isLoadFirst = true;
		this.onReset();
*** End onLoadFirst *********/
//@formatter:on
	/**
	 * @return the formDocked
	 */
	public ExtList getFormDocked() {
		return _formDocked;
	}

}
