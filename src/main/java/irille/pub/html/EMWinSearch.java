package irille.pub.html;

import irille.pss.sal.SalOrder;
import irille.pub.bean.BeanBill;
import irille.pub.bean.BeanForm;
import irille.pub.bean.CmbBill;
import irille.pub.bean.CmbForm;
import irille.pub.ext.Ext;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFldDate;
import irille.pub.view.VFldImg;
import irille.pub.view.VFldTime;
import irille.pub.view.VFlds;

/**
 * ExtJs高级搜索窗口
 * 
 * @author whx
 * 
 */
public class EMWinSearch<T extends EMWinSearch> extends EMBase<T> {
	public EMWinSearch(Tb tb, VFlds... vflds) {
		super(tb, vflds);
		CmbForm.TB.getCode();
		if (BeanBill.class.isAssignableFrom(tb.getClazz()))
			getVFlds().moveLast(CmbBill.T.STATUS, CmbBill.T.ORG, CmbBill.T.DEPT, CmbBill.T.CREATED_BY,
			    CmbBill.T.CREATED_TIME, CmbBill.T.APPR_BY, CmbBill.T.APPR_TIME, CmbBill.T.TALLY_BY, CmbBill.T.TALLY_TIME,
			    CmbBill.T.REM);
		else if (BeanForm.class.isAssignableFrom(tb.getClazz()))
			getVFlds().moveLast(CmbForm.T.STATUS, CmbForm.T.ORG, CmbForm.T.DEPT, CmbForm.T.CREATED_BY,
			    CmbForm.T.CREATED_TIME, CmbForm.T.APPR_BY, CmbForm.T.APPR_TIME, CmbForm.T.REM);
	}

	public static void main(String[] args) {
		Tb tb = SalOrder.TB;
		tb.resetAllVFld();
		VFlds flds = new VFlds().addAll(tb);
		new EMWinSearch(tb, flds).backupFiles().init().crtFile().cmpFileIgnoreBlank();
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "view/" + getPack() + "/" + getClazz() + "/WinSearch.js";
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.view." + getPack() + "." + getClazz() + ".WinSearch";
	}

	@Override
	public void initAttrs() {
		super.initAttrs();
		add(EXTEND, "Ext.window.Window");
		add("oldId", getClazz() + "_searchWin_");
		add(WIDTH, 680);
		add(LAYOUT, "fit");
		add(RESIZABLE, true);
		add(ICON_CLS, "app-icon");
		add("listCmp", null);
	}

	@Override
	public void initForm() {
		super.initForm();
		initForm(getForm(), getPack(), getClazz());
	}

	public void initForm(ExtList v, String pack, String clazz) {
		ExtList panel = new ExtList();
		ExtList form = new ExtList();
		formSearch(form);
		panelSearch(panel);
		form.add(ITEMS, getColumns());
		panel.add(ITEMS, new ExtDime(form));
		v.add(ANCHOR, "100%").add(PLAIN, true).add(ITEMS, new ExtDime(panel));
	}

	private void panelSearch(ExtList v) {
		v.add(XTYPE, "panel").add(LAYOUT, "form").add(BORDER, false).add(FRAME, false).add("bodyPadding", "5 5 5 5")
		    .addExp(URL, Ext.url(Ext.getPag(getTb()), Ext.getClazz(getTb()), "list"))
		    .add(FIELD_DEFAULTS, new ExtList().add(LABEL_WIDTH, 100).add(LABEL_STYLE, "font-weight : bold"));
	}

	private void formSearch(ExtList v) {
		v.add(XTYPE, "form").add(BORDER, false)
		    .add(LAYOUT, new ExtList().add(TYPE, "table").add(COLUMNS, 6).add(ITEM_CLS, "x-layout-table-items-form"));
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#initActs()
	 */
	@Override
	public void initActs() {
		super.initActs();
		AddActWin(new Act(getTb(), OAct.RESET), EMWin.class);
		AddActWin(new Act(getTb(), OAct.CLOSE), EMWin.class);
		AddActWin(new Act(getTb(), OAct.SEARCH_ADV), EMWinSearch.class);
	}

	@Override
	public void initFuns() {
		AddFun("setActiveRecord", EMWin.class).addFunParasExp("record");
		AddFun("onBetween", EMWinSearch.class).addFunParasExp("newv,label,dateEd");
		super.initFuns();
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#initColumns()
	 */
	@Override
	public void initColumns() {
		super.initColumns();
		//		getColumns().setCloseStrToParas();  //改为输出“()”
		//		boolean showKey = false; // 列表是否显示主键 | 系统自增与逻辑自增不显示

		int i = 0;
		for (VFld vfld : getVFlds().getVFlds()) {
			if (vfld.getFld() instanceof FldLines || vfld.getFld() instanceof FldV || vfld.getCode().equals("rowVersion"))
				continue;
			if (vfld.getCode().equals("pkey")) {
				if (getTb().isAutoIncrement())
					continue;
				if (getTb().isAutoLocal() && vfld.getFld().getJavaType().equals(String.class) == false)
					continue;
				//					showKey = true;
			}
			if (!(vfld instanceof VFldImg))
				setFldAttr(vfld, getColumns().AddList());
			i++;
		}
		if (i % 2 != 0) {
			getColumns().add(new ExtList().add(XTYPE, "label").add(TEXT, "").add("colspan", 3));
		}
		//		// 自增长主键放在最后
		//		if (showKey == false)
		//			getTb().get("pkey").getVFld().form(getColumns().AddList(null), false);
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#setFldAttr(irille.pub.view.VFld,
	 * irille.pub.html.ExtList)
	 */
	@Override
	public void setFldAttr(VFld fld, ExtList fldList) {
		fld.winSearch(fldList, true);
		//super.setFldAttr(fld, fldList);
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#initComponent(irille.pub.html.ExtFunDefine)
	 */
	@Override
	public void initComponent(ExtFunDefine fun) {
		fun.add("	var strstore = Ext.create('mvc.combo.sys.SysOOptCht');" + LN);
		fun.add("	var datestore = Ext.create('mvc.combo.sys.SysOOptCht');" + LN);
		fun.add("	var numstore = Ext.create('mvc.combo.sys.SysOOptCht');" + LN);
		fun.add("	var outstore = Ext.create('mvc.combo.sys.SysOOptCht');" + LN);
		fun.add("	strstore.filter('value', new RegExp('^([1-4|9]|[1][0])$'));" + LN);
		fun.add("	datestore.filter('value', new RegExp('^([3-9]|[1][0-1])$'));" + LN);
		fun.add("	numstore.filter('value', new RegExp('^([3-9]|[1][0])$'));" + LN);
		fun.add("	outstore.filter('value', new RegExp('^([3|4|9]|[1][0])$'));" + LN);
		fun.add("	this.items =");
		fun.add(getForm()); // FORM
		fun.add("	this.buttonAlign = 'right'," + LN);
		fun.add("	this.buttons =");
		fun.add(getActs());
		fun.add("		this.callParent(arguments);" + LN);
		fun.add("		this.addEvents('create');" + LN);
		fun.add("		this.form = this.items.items[0];" + LN);
	}

//@formatter:off	
	/** Begin onSearchAdv ********
	var array = mvc.Tools.advancedSearchValues(this.down('form'));
	this.listCmp.onSearchDo(array);
	this.close();
*** End onSearchAdv *********/
	
/** Begin onBetween ********
	if(newv == 11) {
		this.down('#'+label).hide();
		this.down('#'+label).setValue(null);
		this.down('#'+dateEd).show();
	} else {
		this.down('#'+label).show();
		this.down('#'+dateEd).hide();
		this.down('#'+dateEd).setValue(null);
	}
*** End onBetween *********/
//@formatter:on

}
