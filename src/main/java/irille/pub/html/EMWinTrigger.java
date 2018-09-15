package irille.pub.html;

import irille.core.sys.SysDept;
import irille.pub.FileTools;
import irille.pub.ext.Ext;
import irille.pub.tb.Tb;

/**
 * 
 * @author whx
 * 
 */
public class EMWinTrigger<T extends EMWinTrigger> extends EMBase<T> {

	/**
	 * 
	 */
	public EMWinTrigger(Tb tb) {
		super(tb);
	}

	public static void main(String[] args) {
		Tb tb = SysDept.TB;
		tb.resetAllVFld();
		new EMWinTrigger(tb).backupFiles().crtFile().cmpFileIgnoreBlank();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "view/" + getPack() + "/" + getClazz() + "/Trigger.js";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.view." + getPack() + "." + getClazz() + ".Trigger";
	}

	@Override
	public void initAttrs() {
		super.initAttrs();
		add(EXTEND, "Ext.window.Window");
		add(WIDTH, 700);
		add(HEIGHT, 400);
		add(LAYOUT, "fit");
		add("title", "选择器-"+getTb().getName());
		add(RESIZABLE, true);
		add("modal", true);
		add(BORDER, false);
	}

	/* (non-Javadoc)
	 * @see irille.pub.html.EMBase#initForm()
	 */
	@Override
	public void initForm() {
		super.initForm();
		getForm().add(ANCHOR,"100%")
			.add(PLAIN,true)
			.addExp(XTYPE,"list");	
	}
	
	@Override
	public void initFuns() {
		AddFun("onTrigger", EMWinTrigger.class).addFunParas(
				new ExtExp("data, params"));
		super.initFuns();
	}

	public void initComponent(ExtFunDefine fun) {
		fun.add(loadFunCode(EMWinTrigger.class, "initComponent1", getPack(), getClazz()));

		fun.add("		this.items =");
		fun.add(getForm());
		fun.add("		this.callParent(arguments);");
	
		//@formatter:off	
/** Begin initComponent1 ********
		var list = Ext.create('mvc.view.【0】.【1】.TriggerList');
		list.on('trigger', this.onTrigger, this);
*** End initComponent1 *********/
//@formatter:on
	}

	public void onTrigger(ExtFunDefine v) {
		v.add(loadFunCode(EMWinTrigger.class, "onTrigger"));
		//@formatter:off	
		/** Begin onTrigger ********
		this.fireEvent('trigger', data, params);
		this.close();
		*** End onTrigger *********/
		//@formatter:on		
	}
}
