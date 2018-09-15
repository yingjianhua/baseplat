package irille.pub.html;

import irille.core.sys.SysOrg;
import irille.pub.FileTools;
import irille.pub.ext.Ext;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.Tb;

public class EMWinTwoRow<T extends EMWinTwoRow> extends EMBase<T> {
	private int _width = 600;
	
	public EMWinTwoRow(Tb tb) {
		super(tb);
	}

	public static void main(String[] args) {
		Tb tb = SysOrg.TB;
		tb.resetAllVFld();
		new EMWinTwoRow(tb).backupFiles().crtFile().cmpFileIgnoreBlank();
	}
	
	public int getWidth() {
		return _width;
	}

	public void setWidth(int width) {
		_width = width;
	}

	@Override
	public String getFileName() {
		return Ext.getPathApp() + "view/" + getPack() + "/" + getClazz() + "/Win.js";
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
		add(EXTEND,"Ext.window.Window");
		add(WIDTH,getWidth());
		add(LAYOUT,"fit");
		addExp(FORM,"null");
		add(RESIZABLE,true);
		add("modal",true);
		add(ICON_CLS,"app-icon");
		add(INS_FLAG,true);
	}

	/* (non-Javadoc)
	 * @see irille.pub.html.EMBase#initForm()
	 */
	@Override
	public void initForm() {
		super.initForm();
		getForm().add(ANCHOR,"100%").add(PLAIN,true)
			.addExp(XTYPE,"Ext.create('mvc.view."+getPack()+"."+getClazz()+".Form',{insFlag : this.insFlag})");
	}
	

	@Override
	public void initFuns() {
		AddFun("setActiveRecord", EMWinTwoRow.class).addFunParas(new ExtExp("record"));
		super.initFuns();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#initActs()
	 */
	@Override
	public void initActs() {
		super.initActs();
		AddAct(new Act(getTb(), OAct.RESET),EMWinTwoRow.class);
		AddAct(new Act(getTb(), OAct.CLOSE),EMWinTwoRow.class);
		AddAct(new Act(getTb(), OAct.SAVE),EMWinTwoRow.class,getPack(),getClazz());
	}

	public void initComponent(ExtFunDefine fun) {
		fun.add("		this.items =");
		fun.add(getForm());
		
		fun.add("		this.buttonAlign = 'right',"+LN);
		fun.add("this.buttons =");
		
		fun.add(getActs());

		fun.add(loadFunCode(EMWinTwoRow.class, "initComponent2"));
//@formatter:off	
/** Begin initComponent2 ********
		this.callParent(arguments);
		this.addEvents('create');
		this.form = this.items.items[0];
*** End initComponent2 *********/
//@formatter:on
	}
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
				type : 'ajax',
				params : {
					insFlag : this.insFlag
				},
				submitEmptyText: false,
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
