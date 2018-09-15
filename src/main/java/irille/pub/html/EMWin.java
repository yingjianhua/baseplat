package irille.pub.html;

import irille.core.sys.SysDept;
import irille.pub.FileTools;
import irille.pub.ext.Ext;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.Tb;

/**
 * 
 * @author whx
 * 
 */
public class EMWin<T extends EMWin> extends EMBase<T> {
	private int _width = 400;

	public EMWin(Tb tb) {
		super(tb);
	}

	public static void main(String[] args) {
		Tb tb = SysDept.TB;
		tb.resetAllVFld();
		new EMWin(tb).crtFile().cmpFileIgnoreBlank();
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
		add(LAYOUT, "fit");
		addExp(FORM, "null");
		add(RESIZABLE, true);
		add("modal", true);
		add(ICON_CLS, "app-icon");
		add(INS_FLAG, true);
	}

	@Override
	public void initForm() {
		super.initForm();
		initForm(getForm(),getPack(),getClazz()	);
	}

	public static void initForm(ExtList v,String pack, String clazz) {
		v.add(ANCHOR, "100%")
		.add("plain", true)
		.addExp(
				XTYPE,
				"Ext.create('mvc.view." + pack + "." +clazz
						+ ".Form',{	insFlag : this.insFlag})");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#initActs()
	 */
	@Override
	public void initActs() {
		super.initActs();
		AddActWin(new Act(getTb(), OAct.RESET),EMWin.class);
		AddActWin(new Act(getTb(), OAct.CLOSE),EMWin.class);
		AddActWin(new Act(getTb(), OAct.SAVE),EMWin.class);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.EMBase#initComponent(irille.pub.html.ExtFunDefine)
	 */
	@Override
	public void initComponent(ExtFunDefine fun) {
		fun.add("		this.items =");
		fun.add(getForm()); // FORM
		fun.add("		this.buttonAlign = 'right',"+LN);
		fun.add("this.buttons =");
		fun.add(getActs());

		fun.add(loadFunCode(EMWin.class, "initComponent2"));
//@formatter:off	
/** Begin initComponent2 ********
		this.callParent(arguments);
		this.addEvents('create');
		this.form = this.items.items[0];
*** End initComponent2 *********/
//@formatter:on
	}

	@Override
	public void initFuns() {
		AddFun("setActiveRecord",EMWin.class).addFunParasExp("record");
		super.initFuns();
	}

	public int getWidth() {
		return _width;
	}

	public void setWidth(int width) {
		_width = width;
	}

	//
	// /**
	// *
	// *
	// * @param fun
	// */
	// public void initButtons(ExtDime v) {
	// ExtFuns.addActWin(v.AddList(null), "重置", EXP_THIS, "win-refresh-icon",
	// "this.onReset");
	// ExtFuns.addActWin(v.AddList(null), "关闭", EXP_THIS, "win-close-icon",
	// "this.onClose");
	// ExtFuns.addActWin(v.AddList(null), "保存", EXP_THIS, "win-save-icon",
	// "this.onSave");
	// }
		//@formatter:off	
		/** Begin setActiveRecord ********
		this.form.activeRecord = record;
		if (record || this.form.activeRecord) {
			this.form.getForm().loadRecord(record);
		} else {
			this.form.getForm().reset();
		}
		*** End setActiveRecord *********/

	/** Begin onReset ********
		this.setActiveRecord(this.form.activeRecord);
*** End onReset *********/

/** Begin onClose ********
		this.close();
*** End onClose *********/

		/** Begin onSave ********
		var form = this.form.getForm();
		if (form.isValid()) {
			form.submit({
				url : this.form.url,
				submitEmptyText: false,
				type : 'ajax',
				params : {insFlag : this.insFlag},
				success : function(form, action) {
					this.fireEvent('create', this, action.result);
					this.close();
				},
				failure : mvc.Tools.formFailure(),
				waitTitle : wait_title,
				waitMsg : wait_msg,
				scope : this
			});
		}
		*** End onSave *********/
		//@formatter:on		
	
	
}
