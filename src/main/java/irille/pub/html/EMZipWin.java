package irille.pub.html;

import irille.gl.gl.GlEntryDef;
import irille.gl.gl.GlEntryDefLine;
import irille.pub.FileTools;
import irille.pub.ext.Ext;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;

/**
 * EXT复合窗口类
 * 
 * @author whx
 * 
 */
public class EMZipWin<T extends EMZipWin> extends EMBase<T> {
	private VFld _outFld;
	private ExtList _formList = new ExtList();
	private int _width = 600;

	/**
	 * 
	 */
	public EMZipWin(Tb tb, VFld outFld) {
		super(tb);
		_outFld = outFld;
	}

	public static void main(String[] args) {
		Tb tb = GlEntryDef.TB;
		new GlEntryDefLine();
		new EMZipWin(tb, GlEntryDefLine.T.PKEY.getFld().getVFld())
		.backupFiles().crtFile().cmpFileIgnoreBlank();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "view/" + getPack() + "/" + getClazz()
				+ "/Win.js";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.view." + getPack() + "." + getClazz() + ".Win";
	}

	@Override
	public void initAttrs() {
		super.initAttrs();
		add(EXTEND, "Ext.window.Window");
		add(WIDTH, _width);
		add(RESIZABLE, false);
		add("modal", true);
		add(ICON_CLS, "app-icon");
		add("pkeyFlag", true);
		add(INS_FLAG, true);
	}

	@Override
	public void initForm() {
		super.initForm();
		EMWin.initForm(getForm(), getPack(), getClazz());
		getFormList().addExp(
				XTYPE,
				"Ext.create('mvc.view." + getPack() + "." + getClazz()
						+ ".ListForm',{height : 300,border : false })");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#initActs()
	 */
	@Override
	public void initActs() {
		super.initActs();
		AddAct(new Act(getTb(), OAct.RESET),EMZipWin.class);
		AddAct(new Act(getTb(), OAct.CLOSE),EMZipWin.class);
		AddAct(new Act(getTb(), OAct.SAVE),EMZipWin.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.EMBase#initComponent(irille.pub.html.ExtFunDefine)
	 */
	@Override
	public void initComponent(ExtFunDefine fun) {
		fun.add("		this.items =");
		fun.AddDime(getForm(), getFormList());
		fun.add("		this.buttonAlign = 'right'," + LN);
		fun.add("this.buttons =");
		fun.add(getActs());

		fun.add(loadFunCode(EMZipWin.class, "initComponent2"));
//@formatter:off	
/** Begin initComponent2 ********
		this.callParent(arguments);
		this.addEvents('create');
		this.form = this.items.items[0];
		this.lineTable = this.items.items[1];
*** End initComponent2 *********/
//@formatter:on
	}

	@Override
	public void initFuns() {
		AddFun("setActiveRecord",EMZipWin.class,_outFld.getCode()).addFunParasExp("record");
		super.initFuns();
	}

	//@formatter:off	
		/** Begin setActiveRecord ********
		this.form.activeRecord = record;
		if (record || this.form.activeRecord) {
			this.form.getForm().loadRecord(record);
			this.lineTable.store.filter([{'id':'filter','property':'【0】','value':record.get('bean.pkey')}]);
		} else {
			this.form.getForm().reset();
			this.lineTable.store.removeAll();
		}
		*** End setActiveRecord *********/

/** Begin onReset ********
		this.setActiveRecord(this.form.activeRecord);
*** End onReset *********/

/** Begin onClose ********
		this.lineTable.cellEditing.cancelEdit();
		this.close();
*** End onClose *********/

		/** Begin onSave ********
		var form = this.form.getForm();
		if (form.isValid()) {
			form.submit({
				url : this.form.url,
				submitEmptyText: false,
				type : 'ajax',
				params : mvc.Tools.storeValues(this.lineTable.store,{insFlag : this.insFlag}),
				success : function(form, action) {
					this.fireEvent('create', this, action.result);
					this.onClose();
				},
				failure : mvc.Tools.formFailure(),
				waitTitle : wait_title,
				waitMsg : wait_msg,
				scope : this
			});
		}
		*** End onSave *********/
		//@formatter:on		

	/**
	 * @return the formList
	 */
	public ExtList getFormList() {
		return _formList;
	}

	public int getWidth() {
		return _width;
	}

	public void setWidth(int width) {
		_width = width;
	}
	
}
