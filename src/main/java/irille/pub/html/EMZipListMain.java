package irille.pub.html;

import irille.gl.gl.GlEntryDef;
import irille.pub.FileTools;
import irille.pub.bean.BeanBase;
import irille.pub.ext.Ext;
import irille.pub.html.Exts.ExtAct;
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
public class EMZipListMain<T extends EMZipListMain> extends EMBase<T> {
	private boolean _upd, _del, _lineActs;
	private ExtList _formDocked = new ExtList();
	private String _extend = "Ext.grid.Panel";

	public boolean isUpd() {
		return _upd;
	}

	public void setUpd(boolean upd) {
		this._upd = upd;
	}

	public boolean isDel() {
		return _del;
	}

	public void setDel(boolean del) {
		this._del = del;
	}

	/**
	 * @param fileName
	 */
	/**
	 * 
	 */
	public EMZipListMain(Tb tb, VFlds... vflds) {
		super(tb, vflds);
		_upd = tb.checkAct("upd") != null;
		_del = tb.checkAct("del") != null;
		_lineActs = _upd || _del;
	}

	public static void main(String[] args) {
		Tb tb = GlEntryDef.TB;
		VFlds flds = new VFlds(GlEntryDef.T.PKEY, GlEntryDef.T.TEMPLAT, GlEntryDef.T.TABLE_ID, GlEntryDef.T.CODE);
		EMZipListMain form = new EMZipListMain(tb, flds);
		form.backupFiles().crtFile().cmpFileIgnoreBlank();
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "view/" + getPack() + "/" + getClazz() + "/ListMain.js";
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.view." + getPack() + "." + getClazz() + ".ListMain";
	}

	@Override
	public void initAttrs() {
		super.initAttrs();
		add(EXTEND, getExtend()).add("disableSelection", false).add("loadMask", true).add("multiSelect", true)
		    .add("roles", "").addExp("selModel", "{selType : 'checkboxmodel'}");
		if (getExtend().equals("Ext.grid.Panel"))
			addExp("viewConfig", "{enableTextSelection : true}");
	}

	public String getExtend() {
		return _extend;
	}

	public void setExtendRow() {
		_extend = "mvc.tools.RowexpanderGrid";
	}

	@Override
	public void initComponent(ExtFunDefine fun) {
		// 明细功能定义 TODO 主明细的行功能操作暂取消，因还需要STATUS判断
		// initLineActs(fun);

		// 单元格定义
		fun.add("this.columns =").add(getColumns());
		if (getIndexGoods() > 0)
			fun.add("		mvc.Tools.doGoodsLine(this.columns, " + getIndexGoods() + ");");

		fun.add(loadFunCode(EMZipListMain.class, "initComponent", getPack(), getClazz()));
		fun.add("		this.dockedItems=");
		fun.AddDime(getFormDocked());
		fun.add("		this.callParent(arguments);");

//@formatter:off	
/** Begin initComponent ********
		this.store=Ext.create('mvc.store.【0】.【1】');
		this.store.remoteFilter = true;
		this.store.proxy.filterParam = 'filter';
		this.on({cellclick:mvc.Tools.onCellclick});
*** End initComponent *********/
//@formatter:on

	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#initForm()
	 */
	@Override
	public void initForm() {
		super.initForm();
		initFormDocked();
		getForm().addExp(XTYPE, "Ext.create('mvc.tools.ComboxPaging',{myList : this})");
	}

	public void initFormDocked() {
		//@formatter:off	
		getFormDocked()
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
	 * @see irille.pub.html.ExtFile#initActs()
	 */
	@Override
	public void initActs() {
		super.initActs();
		AddActRow(new Act(getTb(), OAct.UPD_ROW), EMZipListMain.class).getFun().addFunParasExp("grid, rowIndex");
		AddActRow(new Act(getTb(), OAct.DEL_ROW), EMZipListMain.class, getPack(), getClazz()).getFun().addFunParasExp(
		    "grid, rowIndex");

	}

	@Override
	public void initFuns() {
		// 为兼容原来的代码产生器，按原顺序产生各Function, 否则可以直接用AddFun方法增加新的Function，并调用超类的本方法即可
		if (_upd) {
			ExtFunDefine onUpdateRecord = AddFun("onUpdateRecord", EMZipListMain.class, getPack(), getClazz())
			    .addFunParasExp("form, data");
			ExtFunDefine onUpdWin = AddFun("onUpdWin", EMZipListMain.class, getPack(), getClazz())
			    .addFunParasExp("selection");

			add(onUpdateRecord);
			add(getAct(OAct.UPD_ROW).getFun());
			add(onUpdWin);
		}
		if (_del) {
			add(getAct(OAct.DEL_ROW).getFun());
		}
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
			if (fld.getFld().getCode().equals("pkey") && getTb().isAutoIncrement())
				continue;
			setFldAttr(fld, getColumns().AddList());
			index++;
			if (fld.getCode().equals("goods"))
				setIndexGoods(index);
		}
		// 明细功能定义 TODO 主明细的行功能操作暂取消，因还需要STATUS判断
		// columnOpt(getColumns());
	}

	public void columnOpt(ExtDime v) {
		if (isLineActs()) {
			v.AddList().add(XTYPE, "actioncolumn")
			//
			    .add(WIDTH, 80).add(SORTABLE, false)
			    //
			    .add("menuDisabled", true).add("header", "操作").addExp("items", "lineActs");
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
		initOutFldJump(fld, fldList);
		super.setFldAttr(fld, fldList);
	}

	/**
	 * 明细行功能定义
	 * 
	 * @param fun
	 */
	public void initLineActs(ExtFunDefine fun) {
		if (!isLineActs())
			return;
		fun.add("var lineActs = [];" + LN);
		IEnumOpt oact;
		for (ExtAct act : getActs().getActs()) {
			initLineAct(fun, act);
		}
	}

	public void initLineAct(ExtFunDefine fun, ExtAct act) {
		fun.add("		if (this.roles.indexOf('" + actCodeCutRow(act.getAct()) + "') != -1)" + LN);
		fun.addParas("lineActs.push", act);
	}

	//@formatter:off	
		/** Begin onUpdateRecord ********
		var selection = this.getView().getSelectionModel().getSelection()[0];
		var bean = Ext.create('mvc.model.【0】.【1】', data);
		Ext.apply(selection.data,bean.data);
		selection.commit();
		this.getSelectionModel().deselectAll();
		this.getView().select(selection);
		Ext.example.msg(msg_title, msg_text);
		*** End onUpdateRecord *********/

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

	/** Begin onDelRow ********
		var me = this;
		Ext.MessageBox.confirm(msg_confirm_title, msg_confirm_msg,
			function(btn) {
				if (btn != 'yes')
					return;
				var row = me.getStore().getAt(rowIndex);
				Ext.Ajax.request({
					url : '/【0】_【1】_del?pkey='+row.get('bean.pkey')+'&rowVersion='+row.get(BEAN_VERSION),
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
		//@formatter:on		

	/**
	 * @return the formDocked
	 */
	public ExtList getFormDocked() {
		return _formDocked;
	}

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
	public T setLineActs(boolean lineActs) {
		_lineActs = lineActs;
		return (T) this;
	}
}
