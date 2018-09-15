package irille.pub.html;

import irille.gl.gl.GlEntryDef;
import irille.gl.gl.GlEntryDefLine;
import irille.pub.FileTools;
import irille.pub.Str;
import irille.pub.bean.BeanBase;
import irille.pub.ext.Ext;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldOutKey;
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
public class EMZipListLine<T extends EMZipListLine> extends EMBase<T> {
	private String _outPack, _outClazz;
	private VFld _outfld;
	private ExtList _formDocked = new ExtList();

	/**
	 * @param fileName
	 */
	/**
	 * 
	 */
	public EMZipListLine(Tb tb, VFld outfld, VFlds... vflds) {
		super(tb, vflds);
		_outfld = outfld;
		_outPack = getTbPackage(outfld.getFld().getTb());
		_outClazz = getTbClazz(outfld.getFld().getTb());
	}

	public EMZipListLine(Tb tb, IEnumFld outfld, VFlds... vflds) {
		this(tb, outfld.getFld().getVFld(), vflds);
	}

	public static void main(String[] args) {
		new GlEntryDefLine();
		Tb tb = GlEntryDefLine.TB;
		VFlds flds = new VFlds().addWithout(tb, GlEntryDefLine.T.NEGATIVE_ABLE);
		EMZipListLine form = new EMZipListLine(GlEntryDef.TB, GlEntryDefLine.T.PKEY, flds);
		form.backupFiles().crtFile().cmpFileIgnoreBlank();
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "view/" + getPack() + "/" + getClazz() + "/ListLine"
		    + Str.tranFirstUpper(_outfld.getFld().getTb().getCode()) + ".js";
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.view." + getPack() + "." + getClazz() + ".ListLine"
		    + Str.tranFirstUpper(_outfld.getFld().getTb().getCode());
	}

	@Override
	public void initAttrs() {
		super.initAttrs();
		add(EXTEND, "Ext.grid.Panel").add("disableSelection", false).add("loadMask", true).add("multiSelect", true);
		addExp("viewConfig", "{enableTextSelection : true}");
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
			.add("dock","top")
			.add(XTYPE,"toolbar")
			.add(XTYPE,"pagingtoolbar")
			.addExp("store","this.store")
			.add("dock","bottom")
			.add("displayInfo",true)
			.add("displayMsg","显示 {0} - {1} 条，共计 {2} 条")
			.add("emptyMsg","没有数据")
			.AddDime(ITEMS, getForm());					
		//@formatter:on	
	}

	public void initComponent(ExtFunDefine fun) {
		// 单元格定义
		fun.add("		this.columns = ").add(getColumns());
		if (getIndexGoods() > 0)
			fun.add("		mvc.Tools.doGoodsLine(this.columns, " + getIndexGoods() + ");");
		fun.add(loadFunCode(EMZipListLine.class, "initComponent", _outPack, _outClazz));
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
		initOutFldJump(fld, fldList);
		super.setFldAttr(fld, fldList);
	}

	/**
	 * @return the formDocked
	 */
	public ExtList getFormDocked() {
		return _formDocked;
	}
}
