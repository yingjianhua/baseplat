package irille.pub.html;

import irille.pub.ext.Ext;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * XXX 此程序未调试，没找到相应的表案例！！！ whx 20141019
 * @author whx
 * 
 */
public class EMWinEdit<T extends EMWinEdit> extends EMBase<T> {
	private VFld[] _outKeyFlds;
	private ExtList _formTable=new ExtList();
	private ExtDime _columnsTable=new ExtDime();


	/**
	 * 
	 */
	public EMWinEdit(Tb tb, VFlds[] vflds, VFld[] outs) {
		super(tb, vflds);
		_outKeyFlds = outs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "view/" + getPack() + "/" + getClazz()
				+ "/WinEdit.js";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.view." + getPack() + "." + getClazz() + ".WinEdit";
	}

	@Override
	public void initAttrs() {
		super.initAttrs();
		add(EXTEND, "Ext.window.Window");
		add(WIDTH, 800);
		add("height", 500);
		add(LAYOUT, "border");
		add(RESIZABLE, true);
		add("maximizable", true);
		add("modal", true);
		add(ICON_CLS, "app-icon");
		addExp("record", "null");
		add("oldId", getClazz() + "_edit_");
		add("isEdit", true);
	}
	/* (non-Javadoc)
	 * @see irille.pub.html.EMBase#initForm()
	 */
	@Override
	public void initForm() {
		super.initForm();
		ExtList item = getForm();
		item.add("region", "north").add(XTYPE, "form").add(BORDER, false)
				.add(BODY_PADDING, "5 5 0 5").AddList(LAYOUT).add(TYPE, "table")
				.add(COLUMNS, 2).add(ITEM_CLS, "x-layout-table-items");
		item.AddList("fieldDefaults").add(LABEL_WIDTH, 120).add(WIDTH, 300)
				.add(LABEL_STYLE, "font-weight : bold").add(READ_ONLY, true);
		item.add(ITEMS,getColumns());

		initColumnsTable();
		ExtList itemTable = getFormTable();
		itemTable.add("region", "center").add(XTYPE, "tabpanel").add(BORDER, false);
		itemTable.add(ITEMS,getColumnsTable());
	}

	/* (non-Javadoc)
	 * @see irille.pub.html.EMBase#initColumns()
	 */
	@Override
	public void initColumns() {
		super.initColumns();
			for (VFld vfld : getVFlds().getVFlds()) {
				if (vfld.getFld() instanceof FldLines || vfld.getFld() instanceof FldV || vfld.getCode().equals("rowVersion"))
					continue;
				setFldAttr(vfld, getColumns().AddList());
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
		fld.edit(fldList);
		super.setFldAttr(fld, fldList);
	}


	@Override
	public void initFuns() {
		AddFun("loadData", EMWinEdit.class);
		AddFun("close", EMWinEdit.class);
		super.initFuns();
	}

	public void initComponent(ExtFunDefine fun) {
		fun.add(loadFunCode(EMWinEdit.class, "initComponent1", getPack(),
				getClazz()));
//@formatter:off	
/** Begin initComponent1 ********
		var mainPkey = this.record.get('bean.pkey');
*** End initComponent1 *********/
//@formatter:on
		fun.add("this.items =");
		ExtDime items = fun.AddDime();
		items.add(getForm());
		items.add(getFormTable());
		fun.add("		this.callParent(arguments);");
	}

	
	public void initColumnsTable() {
		int i = 1;
		for (VFld out : _outKeyFlds) {
			Tb lineTb = (Tb) out.getFld().getTb();
			String linepag = Ext.getPag(lineTb);
			String linecname = Ext.getClazz(lineTb);
			ExtFunCall fun = getColumnsTable().AddList().AddFunCall(XTYPE + ": Ext.create");
			ExtList list = fun.addFunParas(
					"mvc.view." + linepag + "." + linecname + ".ListEdit")
					.AddFunParaList();
			list.add(BORDER, false).add("title", lineTb.getName())
					.addExp("itemId", "this.oldId+'" + i + "'").addExp("mainPkey", "mainPkey")
					.add(ICON_CLS, "tab-user-icon").addExp("isEdit", "this.isEdit");
			if (i > 1) {
				list.AddList("listeners").addExp("activate", "function(tab){tab.onLoadFirst();}");
			}
//			listeners: {
//					                    activate: function(tab){
//					                        tab.onLoadFirst();
//					                    }
//					                }
			i++;
		}

	}

	//@formatter:off	
		/** Begin loadData ********
		this.down('#'+this.oldId+'1').onLoadFirst();
		*** End loadData *********/
		//@formatter:on		
//@formatter:off	
/** Begin close ********
		//关闭WIN窗口时需要手动调用单元格编辑取消事件]
		var tab = this.down('tabpanel').getActiveTab();
		if (tab.isEdit)
			this.down('tabpanel').getActiveTab().cellEditing.cancelEdit();
		this.callParent(arguments);
*** End close *********/
//@formatter:on

	/**
	 * @return the formTable
	 */
	public ExtList getFormTable() {
		return _formTable;
	}

	/**
	 * @return the columnsTable
	 */
	public ExtDime getColumnsTable() {
		return _columnsTable;
	}
}
