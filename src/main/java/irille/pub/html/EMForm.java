package irille.pub.html;

import irille.core.sys.SysUser;
import irille.dep.sal.ESalOrder;
import irille.gl.gl.GlNoteView;
import irille.gl.gs.GsGoods;
import irille.pss.sal.SalOrder;
import irille.pub.Str;
import irille.pub.ext.Ext;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.FldV;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFldOutKey;
import irille.pub.view.VFlds;

/**
 * 
 * @author whx
 * 
 */
public class EMForm<T extends EMForm> extends EMBase<T> {
	private int _widthLabel = 100;
	private boolean _isGoodsLink = false;

	public EMForm(Tb tb, VFlds... vflds) {
		super(tb, vflds);
	}

	public static void main(String[] args) {
		Tb tb = GlNoteView.TB;
		VFlds flds = new VFlds(tb);
		System.err.println(new EMForm(tb, flds).init().out());
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "view/" + getTbPackage(getTb()) + "/" + getTbClazz(getTb()) + "/Form.js";
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.view." + getTbPackage(getTb()) + "." + getTbClazz(getTb()) + ".Form";
	}

	@Override
	public void initAttrs() {
		super.initAttrs();
		add(EXTEND, "Ext.form.Panel");
		AddDime(REQUIRES, "Ext.ux.DataTip");
		add(LAYOUT, "form");
		add(BORDER, false);
		add(FRAME, false);
		add(INS_FLAG, true);
		add(BODY_PADDING, "5 5 5 5");
		addExp(URL, Ext.url(Ext.getPag(getTb()), Ext.getClazz(getTb()), ""));
		setFieldDefaultsProperies(AddList(FIELD_DEFAULTS));
		//AddList(PLUGINS).add(PTYPE, "datatip");
	}

	@Override
	public void initForm() {
		super.initForm();
		setLayoutProperies(getForm().AddList(LAYOUT));
		getForm().add(BORDER, false).addExp(ITEMS, "formFlds");
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#initColumns()
	 */
	@Override
	public void initColumns() {
		super.initColumns();
		getColumns().setCloseStrToParas(); //改为输出“()”
		boolean showKey = false; // 列表是否显示主键 | 系统自增与逻辑自增不显示

		for (VFld vfld : getVFlds().getVFlds()) {
			if (vfld.getFld() instanceof FldLines || vfld.getFld() instanceof FldV)
				continue;
			//ONE2ONE类型的主键
			if (vfld.getCode().equals("pkey")) {
				if (vfld.getFld() instanceof FldOutKey) {
					setFldAttr(vfld, getColumns().AddList());
					showKey = true;
					continue;
				}
				if (getTb().isAutoIncrement())
					continue;
				if (getTb().isAutoLocal() && vfld.getFld().getJavaType().equals(String.class) == false)
					continue;
				showKey = true;
			}
			if (vfld.getCode().equals("rowVersion")) { //版本号在FORM中默认隐藏
				vfld.setHidden("true");
			}
			setFldAttr(vfld, getColumns().AddList());
		}
		// 自增长主键放在最后
		if (showKey == false)
			getTb().get("pkey").getVFld().form(getColumns().AddList(null), false);
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.html.EMBase#setFldAttr(irille.pub.view.VFld,
	 * irille.pub.html.ExtList)
	 */
	@Override
	public void setFldAttr(VFld fld, ExtList fldList) {
		if (_isGoodsLink && fld instanceof VFldOutKey && ((FldOutKey) fld.getFld()).getOutTbClazz().equals(GsGoods.class))
			fldList.add(new EMForm.MyGoods(fld.getCode(), fld.getReadOnly()));
		else
			fld.form(fldList, false);
		super.setFldAttr(fld, fldList);
	}

	@Override
	public void initComponent(ExtFunDefine fun) {
		fun.add(loadFunCode(EMForm.class, "initComponent"));
		//@formatter:off
		/** Begin initComponent ********
			if (this.insFlag)
				this.url = this.url + 'ins';
			else
				this.url = this.url + 'upd';
			var formFlds = [];
			formFlds.push
		*** End initComponent *********/		
		//@formatter:on
		fun.add(getColumns());
		fun.add("	this.items = ");
		fun.AddDime(getForm()); // 是否需要"[]"有待验证 whx 20141015
		fun.add("	this.callParent(arguments);" + LN);
	}

	/**
	 * 
	 * @param v
	 */
	public void setFieldDefaultsProperies(ExtList v) { // 以下方法在子类EMFormTwoRow类中有重构！
		v.add(LABEL_WIDTH, getWidthLabel()).add(LABEL_STYLE, "font-weight : bold");
	}

	/**
	 * 
	 * @param v
	 */
	public void setLayoutProperies(ExtList v) { // 以下方法在子类EMFormTwoRow类中有重构！
		v.add(TYPE, "vbox").add(ALIGN, "stretch");
	}

	public int getWidthLabel() {
		return _widthLabel;
	}

	public void setWidthLabel(int widthLabel) {
		_widthLabel = widthLabel;
	}

	public void setGoodsLink(boolean isGoodsLink) {
		_isGoodsLink = isGoodsLink;
	}

	public static class MyGoods implements IExtOut {
		String _code;
		String _readOnly;

		public MyGoods(String code, String readOnly) {
			_code = code;
			_readOnly = readOnly;
		}
		
		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			if (Str.isEmpty(_readOnly))
			buf.append(new EMModel(SysUser.TB).loadFunCode(EMForm.class, "myGoods", _code));
			else
			buf.append(new EMModel(SysUser.TB).loadFunCode(EMForm.class, "myGoodsOnly", _code));
		}

		//@formatter:off	
  	/** Begin myGoods ********
		xtype : 'comboauto',
		listConfig : {minWidth:250},
		fieldLabel : '货物',
		fields : ['pkey','code','name','spec'],//查询返回信息model
		valueField : ['pkey'],//提交值
		textField : 'code', //显示信息
		queryParam : 'code',//搜索使用
		name : 'bean.【0】', //提交使用
		url : base_path + '/gs_GsGoods_autoComplete',
		urlExt : 'gs.GsGoods',
		hasBlur : false,
		afterLabelTextTpl : required,
		allowBlank : false,
		listeners : {
			scope : this,
			blur : function(field){
				mvc.Tools.onLoadGoodsForm(field.getRawValue(),this,'【0】');
			}
		}
  		*** End myGoods *********/
		
		/** Begin myGoodsOnly ********
		xtype : 'comboauto',
		listConfig : {minWidth:250},
		fieldLabel : '货物',
		fields : ['pkey','code','name','spec'],//查询返回信息model
		valueField : ['pkey'],//提交值
		textField : 'code', //显示信息
		queryParam : 'code',//搜索使用
		name : 'bean.【0】', //提交使用
		url : base_path + '/gs_GsGoods_autoComplete',
		urlExt : 'gs.GsGoods',
		hasBlur : false,
		afterLabelTextTpl : required,
		allowBlank : false,
		readOnly : !this.insFlag,
		listeners : {
			scope : this,
			blur : function(field){
				mvc.Tools.onLoadGoodsForm(field.getRawValue(),this,'【0】');
			}
		}
  		*** End myGoodsOnly *********/
  		//@formatter:on	
	}

}
