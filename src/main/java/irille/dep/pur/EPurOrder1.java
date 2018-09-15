/**
 * 
 */
package irille.dep.pur;

import irille.core.sys.SysShiping;
import irille.gl.gs.GsIn.T;
import irille.pss.pur.PurOrder;
import irille.pss.pur.PurOrderLine;
import irille.pub.bean.CmbGoods;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMModel;
import irille.pub.html.EMWin;
import irille.pub.html.EMZipList;
import irille.pub.html.EMZipWin;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EPurOrder1 extends PurOrder {
	public static void main(String[] args) {
		new EPurOrder1().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		CmbGoods.TB.getCode();
		PurOrderLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		VFlds ship = new VFlds("ship").addWithout(SysShiping.TB, SysShiping.T.PKEY, SysShiping.T.REM,
			    SysShiping.T.SHIPING_FORM, SysShiping.T.TIME_SHIP, SysShiping.T.TIME_ARR, SysShiping.T.ROW_VERSION);
		ship.get(SysShiping.T.NAME).setName("发货人名称");
		ship.get(SysShiping.T.ADDR).setName("发货地址");
		ship.get(SysShiping.T.MOBILE).setName("发货人手机");
		ship.get(SysShiping.T.TEL).setName("发货人电话");
		VFlds[] vflds = new VFlds[] { new VFlds().addWithout(TB, T.SHIPING), ship };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.SUPPLIER, T.SUPNAME,
				T.AMT, T.AMT_COST) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.SUPNAME, T.STATUS) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds,
				new VFlds[] { new VFlds(PurOrderLine.T.PKEY) });

		ext.newExts().init();
		ext.crtFiles();
//		ext.backupFiles();
//		ext.crtFilesAndCompBackup();
		return ext;
	}

	/**
	 * 重构LIST，加入【关闭】按钮的支持 loadTbAct是初始化控件时加入按钮及其功能代码；
	 * initFormListMain是重写主表的行选择事件，加上对【关闭】按钮的状态设置；
	 * 
	 * @author whx
	 * @version 创建时间：2014年11月7日 下午5:02:28
	 */
	class MyZipList extends EMZipList<MyZipList> {

		public MyZipList(Tb tb, VFlds[] vflds) {
			super(tb, vflds);
		}

		@Override
		public void loadTbAct(Class funCodeFile, Act act) {
			super.loadTbAct(funCodeFile, act);
			IEnumOpt oact = act.getAct();
			if (act.getCode().equals("close") == false)
				return;
			ExtAct v = new ExtAct(this, act, EPurOrder1.class);
			v.add(TEXT, act.getName()).add(ICON_CLS, "upd-icon")
					.addExp("itemId", "this.oldId+'" + act.getCode() + "'")
					.add(SCOPE, EXP_THIS)
					.addExp(HANDLER, "this.on" + act.getCodeFirstUpper())
					.addExp("disabled", "this.lock");
			getActs().add(v);
		}

		//@formatter:off	
		/** Begin onClose ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.MessageBox.confirm(msg_confirm_title, '销售订单['+selection.get('bean.code') + '] - 确认关闭吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/pur_PurOrder_close?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								var bean  = Ext.create('mvc.model.pur.PurOrder',result);
								Ext.apply(selection.data, bean.data);
								selection.commit();
								me.mdMainTable.getSelectionModel().deselectAll();
								me.mdMainTable.getView().select(selection);
								Ext.example.msg(msg_title, '关闭--成功');
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
		}
		*** End onClose *********/
	//@formatter:on	

		public void initFormListMain() {
			ExtList form = getFormListMain();
			ExtList l = form.AddFunCall("xtype : Ext.create",
					"mvc.view." + getPack() + "." + getClazz() + ".ListMain")
					.AddFunParaList();
			l.add("title", getTb().getName())
					.addExp("itemId", "this.oldId+'maintable'")
					.add(ICON_CLS, "tab-user-icon")
					.addExp("roles", "this.roles");
			l.addExp(
					"listeners",
					loadFunCode(EPurOrder1.class, "initFormListMainStatus",
							getOutVFlds().get(0).getCode()));
			//@formatter:off	
			/** Begin initFormListMainStatus ********
{
			 								scope : this,
				                selectionchange: function(model, records) {
				                    if (records.length === 1){
				                        this.mdMain.getForm().loadRecord(records[0]);
        								this.mdLineTable.store.filter([{'id':'filter', 'property':'pkey','value':records[0].get('bean.pkey')}]);
        								var status = records[0].get('bean.status'); //根据单据状态判断
        								//初始状态
        								if (status==STATUS_INIT){
	    									if (this.roles.indexOf('upd') != -1)
												this.down('#'+this.oldId+'upd').setDisabled(false);
											if (this.roles.indexOf('del') != -1)
												this.down('#'+this.oldId+'del').setDisabled(false);
											if (this.roles.indexOf('doAppr') != -1)
												this.down('#'+this.oldId+'doAppr').setDisabled(false);
											if (this.roles.indexOf('unAppr') != -1)
												this.down('#'+this.oldId+'unAppr').setDisabled(true);
											if (this.roles.indexOf('close') != -1)
												this.down('#'+this.oldId+'close').setDisabled(true);
        								}else if (status==STATUS_APPROVE){
        									if (this.roles.indexOf('upd') != -1)
												this.down('#'+this.oldId+'upd').setDisabled(true);
											if (this.roles.indexOf('del') != -1)
												this.down('#'+this.oldId+'del').setDisabled(true);
											if (this.roles.indexOf('doAppr') != -1)
												this.down('#'+this.oldId+'doAppr').setDisabled(true);
											if (this.roles.indexOf('unAppr') != -1)
												this.down('#'+this.oldId+'unAppr').setDisabled(false);
											if (this.roles.indexOf('close') != -1)
												this.down('#'+this.oldId+'close').setDisabled(false);
        								} else if (status==STATUS_CLOSE){
        									if (this.roles.indexOf('upd') != -1)
												this.down('#'+this.oldId+'upd').setDisabled(true);
											if (this.roles.indexOf('del') != -1)
												this.down('#'+this.oldId+'del').setDisabled(true);
											if (this.roles.indexOf('doAppr') != -1)
												this.down('#'+this.oldId+'doAppr').setDisabled(true);
											if (this.roles.indexOf('unAppr') != -1)
												this.down('#'+this.oldId+'unAppr').setDisabled(true);
											if (this.roles.indexOf('close') != -1)
												this.down('#'+this.oldId+'close').setDisabled(true);
        								}
				                    }else{
				                    	this.mdMain.getForm().reset();
				                    	this.mdLineTable.store.removeAll();
				                    	if (this.roles.indexOf('upd') != -1)
											this.down('#'+this.oldId+'upd').setDisabled(true);
										if (this.roles.indexOf('del') != -1)
											this.down('#'+this.oldId+'del').setDisabled(true);
										if (this.roles.indexOf('doAppr') != -1)
											this.down('#'+this.oldId+'doAppr').setDisabled(true);
										if (this.roles.indexOf('unAppr') != -1)
											this.down('#'+this.oldId+'unAppr').setDisabled(true);
										if (this.roles.indexOf('close') != -1)
											this.down('#'+this.oldId+'close').setDisabled(true);
				                    }
				                }
			                }
			 *** End initFormListMainStatus *********/
			//@formatter:on
		}

	}

	/**
	 * 重构FORM， 自定义LABEL长度，字段长度，布局改为TABLE-3列 setFldAttr中对客户字段完全重写--更改控件类型及相关事件处理
	 * 
	 * @author whx
	 * @version 创建时间：2014年11月7日 下午5:04:57
	 */
	class MyForm extends EMFormTwoRow<MyForm> {

		public MyForm(Tb tb, VFlds[] vflds) {
			super(tb, vflds);
		}

		public void setFieldDefaultsProperies(ExtList v) {
			v.add(LABEL_WIDTH, 110).add(WIDTH, 275)
					.add(LABEL_STYLE, "font-weight : bold");
		}

		public void setLayoutProperies(ExtList v) {
			v.add(TYPE, "table").add(COLUMNS, 3)
					.add(ITEM_CLS, "x-layout-table-items-form");
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(PurOrder.T.SUPPLIER.getFld().getCode())) {
				fldList.add(new MySupplier());
			} else {
				super.setFldAttr(fld, fldList);
			}
		}

	}

	/**
	 * 重构IEXTOUT，用于FORM中客户的控件实现
	 * 
	 * @author whx
	 * @version 创建时间：2014年11月7日 下午5:09:31
	 */
	class MySupplier implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(PurOrder.TB).loadFunCode(EPurOrder1.class,
					"MySupplier"));
		}

		//@formatter:off	
  		/** Begin MySupplier ********
 		xtype : 'comboauto',
		fieldLabel : '供应商',
		name : 'bean.supplier',
		listConfig : {minWidth:250},
		fields : ['pkey','code','name'],//查询返回信息model
		valueField : ['pkey'],//提交值
		textField : 'code', //显示信息
		queryParam : 'code',//搜索使用
		url : base_path + '/sys_SysSuppliser_autoComplete',
		urlExt : 'sys.SysCustom',
		hasBlur : false,
		afterLabelTextTpl : required,
		allowBlank : false,
  		
  		*** End MySupplier *********/
  		//@formatter:on	
	}

	/**
	 * 重构WIN 自定义窗口宽度、编辑表格的最小高度
	 * 
	 * @author whx
	 * @version 创建时间：2014年11月7日 下午5:10:39
	 */
	class MyWin extends EMZipWin<MyWin> {

		public MyWin(Tb tb, VFld outFld) {
			super(tb, outFld);
		}

		public void initAttrs() {
			add(EXTEND, "Ext.window.Window");
			add(WIDTH, 880);
			add(RESIZABLE, false);
			add("modal", true);
			add(ICON_CLS, "app-icon");
			add("pkeyFlag", true);
			add(INS_FLAG, true);
		}

		public void initForm() {
			EMWin.initForm(getForm(), getPack(), getClazz());
			getFormList().addExp(
					XTYPE,
					"Ext.create('mvc.view." + getPack() + "." + getClazz()
							+ ".ListForm',{minHeight : 200,border : false })");
		}
	}

	/**
	 * 重构复合界面产生器 将各种对象更改为上面的自定义对象类
	 * 
	 * @author whx
	 * @version 创建时间：2014年11月7日 下午5:11:55
	 */
	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds,
				VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		public ExtFile newList() {
			return null;
		}

		public ExtFile newForm() {
			return null;
		}

		public ExtFile newZipWin(VFld fld) {
			return null;
		}

		@Override
		public ExtFile newWin() {
		  // TODO Auto-generated method stub
		  return null;
		}
		
		@Override
		public ExtFile newZipListForm(Tb tb, VFld outfld, VFlds... vflds) {
		  // TODO Auto-generated method stub
		  return null;
		}
		
		@Override
		public ExtFile newZipListMain() {
		  // TODO Auto-generated method stub
		  return null;
		}
	}

}
