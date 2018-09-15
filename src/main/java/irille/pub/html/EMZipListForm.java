package irille.pub.html;

import irille.gl.gl.GlEntryDef;
import irille.gl.gl.GlEntryDefLine;
import irille.gl.gs.GsUom;
import irille.gl.gs.GsUomType;
import irille.pub.FileTools;
import irille.pub.bean.CmbGoods;
import irille.pub.ext.Ext;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * 属性类的基类
 * 
 * @author whx
 * 
 */
public class EMZipListForm<T extends EMZipListForm> extends EMBase<T> {
	private String _outPack, _outClazz;
	protected VFld _outfld;

	/**
	 * @param fileName
	 */
	/**
	 * 
	 */
	public EMZipListForm(Tb tb, VFld outfld, VFlds... vflds) {
		super(tb, vflds);
		_outfld = outfld;
		_outPack = getTbPackage(outfld.getFld().getTb());
		_outClazz = getTbClazz(outfld.getFld().getTb());
	}

	public static void main(String[] args) {
		new GlEntryDefLine();
		Tb tb = GlEntryDefLine.TB;
		VFlds flds = new VFlds().addWithout(tb, GlEntryDefLine.T.NEGATIVE_ABLE);
		//		extComp(GsUomType.TB, false, GsUom.T.UOM_TYPE, new IEnumFld[] { GsUomType.T.NAME,
		//		    GsUomType.T.SHORTKEY, GsUomType.T.ENABLED, GsUomType.T.REM }, GsUomType.T.NAME,
		//		    GsUomType.T.SHORTKEY);

		EMZipListForm form = new EMZipListForm(GlEntryDef.TB, GlEntryDefLine.T.PKEY.getFld().getVFld(), flds);
		form.backupFiles().crtFile().cmpFileIgnoreBlank();
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "view/" + getPack() + "/" + getClazz() + "/ListForm.js";
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.view." + getPack() + "." + getClazz() + ".ListForm";
	}

	@Override
	public void initAttrs() {
		super.initAttrs();
		add(EXTEND, "Ext.grid.Panel").add("disableSelection", false).add("loadMask", true)
		    .addExp("cellEditing", "Ext.create('Ext.grid.plugin.CellEditing', { clicksToEdit: 1 })")
		    .addExp("mainPkey", "null");
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#initActs()
	 */
	@Override
	public void initActs() {
		super.initActs();
		AddAct(new Act(getTb(), OAct.INS), EMZipListForm.class, _outPack, _outClazz);
		AddAct(new Act(getTb(), OAct.DEL), EMZipListForm.class);
	}

	public void initComponent(ExtFunDefine fun) {
		// 主界面功能定义
		fun.add("		var mainActs = ").add(getActs());
		fun.add("		this.tbar = mainActs;");
		fun.add("		this.columns =");
		fun.add(getColumns());
		if (getIndexGoods() > 0) {
			fun.add("		mvc.Tools.doGoodsLine(this.columns, "+getIndexGoods()+");");
			fun.add(loadFunCode(EMZipListForm.class, "initComponentGoods", _outPack, _outClazz));
		} else
			fun.add(loadFunCode(EMZipListForm.class, "initComponent", _outPack, _outClazz));
//@formatter:off	
/** Begin initComponent ********
		this.store=Ext.create('mvc.store.【0】.【1】');
		this.store.pageSize = 0;
		this.store.remoteFilter = true;
		this.store.proxy.filterParam = 'filter';
		this.plugins = [this.cellEditing];
		this.callParent(arguments);	
*** End initComponent *********/
		
		/** Begin initComponentGoods ********
		this.store=Ext.create('mvc.store.【0】.【1】');
		this.store.pageSize = 0;
		this.store.remoteFilter = true;
		this.store.proxy.filterParam = 'filter';
		this.plugins = [this.cellEditing];
		this.on('edit', function(editor, e) {
			if (e.field == 'bean.goods'){
				if (this.oldGoods != e.value){ //值变更后触发
					mvc.Tools.onLoadInfo(e.value, e.record, this);
				}
			}
		});
		this.on('beforeedit', function(editor, e) {
			this.diySql = null;
			if (e.field == 'bean.goods')
				this.oldGoods = e.value;
			else if (e.field == 'bean.uom' && e.value){//CELL-EDITOR对象找不到，暂只能把参数存储到GRID中
				var s = e.record.get('bean.uom').split(bean_split);
				this.diySql = 'uom_type = (select uom_type from gs_uom where pkey='+s[0]+')';
			}
		});
		this.callParent(arguments);	
		 *** End initComponentGoods *********/
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
				if (fld.getFld().getCode().equals("pkey") && getTb().isAutoIncrement())
					continue;
				if (fld.getCode().equals(_outfld.getFld().getCode()))
					continue;
				index++;
				if (fld.getCode().equals("goods")) {
					setIndexGoods(index);
					getColumns().addExp(loadFunCode(EMZipListForm.class, "goodsColumn"));
					continue;
				}
				setFldAttr(fld, getColumns().AddList());
			}
		
		//@formatter:off	
		/** Begin goodsColumn ********
		
		{text : '货物', width : 120, dataIndex : 'bean.goods', sortable : true, renderer : mvc.Tools.beanRenderer(), 
				editor: {
					xtype : 'comboautocell',
					listConfig : {minWidth:250},
					fields : ['pkey','code','name','spec'],//查询返回信息model
					valueField : ['pkey'],//提交值
					textField : 'code', //显示信息
					queryParam : 'code',//搜索使用
					name : 'goods', //提交使用
					url : base_path + '/gs_GsGoods_autoComplete',
					urlExt : 'gs.GsGoods',
					hasBlur : false,
					grid : this,
				}}
			*** End goodsColumn *********/
			//@formatter:on		
		
	}

	/*
	 * (non-Javadoc)
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
		if (selection){
			this.getStore().remove(selection);
		}
		*** End onDel *********/
		//@formatter:on		
}
