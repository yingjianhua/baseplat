/**
 * 
 */
package irille.dep.sal;

import irille.pss.sal.SalReserve;
import irille.pss.sal.SalReserveLine;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMModel;
import irille.pub.html.EMWin;
import irille.pub.html.EMZipList;
import irille.pub.html.EMZipListForm;
import irille.pub.html.EMZipWin;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class ESalReserve extends SalReserve {
	public static void main(String[] args) {
		new ESalReserve().crtExt();
	}

	public void crtExt() {
		SalReserveLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.CUST, T.CUST_NAME, T.WAREHOUSE, T.STATUS,
		    T.OPERATOR) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.CUST_NAME, T.STATUS) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(SalReserveLine.T.PKEY) });
		ext.getVfldsList().moveLast(T.ORG, T.DEPT, T.APPR_BY, T.APPR_TIME, T.CREATED_BY, T.CREATED_TIME, T.REM);
		VFlds vs = ext.getVfldsForm();
		vs.del(T.ORG, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME);// FROM页面删除建档员等字段
		vs.moveAfter(T.REM, T.OPERATOR);
		vs.moveAfter(T.WAREHOUSE, T.STATUS);
		vs.setReadOnly("true", T.CODE, T.STATUS, T.CUST_NAME);// 只读设置
		vs.setNull(true, T.CODE, T.STATUS, T.CUST_NAME);// 为空设置
		ext.newExts().init();
		ext.crtFiles();
	}

	/**
	 * 重构LIST，加入【关闭】按钮的支持 loadTbAct是初始化控件时加入按钮及其功能代码；
	 * initFormListMain是重写主表的行选择事件，加上对【关闭】按钮的状态设置；
	 * @author whx
	 * @version 创建时间：2014年11月7日 下午5:02:28
	 */
	class MyZipList extends EMZipList<MyZipList> {

		public MyZipList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		@Override
		public void loadTbAct(Class funCodeFile, Act act) {
			super.loadTbAct(funCodeFile, act);
			IEnumOpt oact = act.getAct();
			if (act.getCode().equals("doClose") == false)
				return;
			ExtAct v = new ExtAct(this, act, ESalReserve.class);
			v.add(TEXT, act.getName()).add(ICON_CLS, "upd-icon").addExp("itemId", "this.oldId+'" + act.getCode() + "'")
			    .add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper()).addExp("disabled", "this.lock");
			getActs().add(v);
		}

		//@formatter:off	
		/** Begin onDoClose ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.MessageBox.confirm(msg_confirm_title, '保留单['+selection.get('bean.code') + '] - 确认关闭吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/sal_SalReserve_doClose?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								var bean  = Ext.create('mvc.model.sal.SalReserve',result);
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
		*** End onDoClose *********/
	//@formatter:on	

		public void initFuns() {
			AddFun("onChangeStatus", ESalSale.class).addFunParasExp("status");
			super.initFuns();
		}

		public void initFormListMain() {
			ExtList form = getFormListMain();
			ExtList l = form.AddFunCall("xtype : Ext.create", "mvc.view." + getPack() + "." + getClazz() + ".ListMain")
			    .AddFunParaList();
			l.add("title", getTb().getName()).addExp("itemId", "this.oldId+'maintable'").add(ICON_CLS, "tab-user-icon")
			    .addExp("roles", "this.roles");
			l.addExp("listeners", loadFunCode(ESalReserve.class, "initChange", getOutVFlds().get(0).getCode()));
		}

		//@formatter:off	
			/** Begin onChangeStatus ********
			if (this.roles.indexOf('upd') != -1)
				this.down('#'+this.oldId+'upd').setDisabled(status != STATUS_INIT);
			if (this.roles.indexOf('del') != -1)
				this.down('#'+this.oldId+'del').setDisabled(status != STATUS_INIT);
			if (this.roles.indexOf('doAppr') != -1)
				this.down('#'+this.oldId+'doAppr').setDisabled(status != STATUS_INIT);
			if (this.roles.indexOf('unAppr') != -1)
				this.down('#'+this.oldId+'unAppr').setDisabled(status != STATUS_CHECKED);
			if (this.roles.indexOf('doClose') != -1)
				this.down('#'+this.oldId+'doClose').setDisabled(status != STATUS_CHECKED);
				*** End onChangeStatus *********/

				/** Begin initChange ********
				{
						scope : this,
		        selectionchange: function(model, records) {
		            if (records.length === 1){
		                this.mdMain.getForm().loadRecord(records[0]);
										this.mdLineTable.store.filter([{'id':'filter', 'property':'pkey','value':records[0].get('bean.pkey')}]);
										var status = records[0].get('bean.status'); //根据单据状态判断
										this.onChangeStatus(status);
		            }else{
		            	this.mdMain.getForm().reset();
		            	this.mdLineTable.store.removeAll();
		            	this.onChangeStatus(-1);
		            }
		        }
		    }
		*** End initChange *********/
			//@formatter:on

	}

	/**
	 * 重构FORM， 自定义LABEL长度，字段长度，布局改为TABLE-3列 setFldAttr中对客户字段完全重写--更改控件类型及相关事件处理
	 * 
	 * @author whx
	 * @version 创建时间：2014年11月7日 下午5:04:57
	 */
	class MyForm extends EMFormTwoRow<MyForm> {

		public MyForm(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		public void setFieldDefaultsProperies(ExtList v) {
			v.add(LABEL_WIDTH, 110).add(WIDTH, 275).add(LABEL_STYLE, "font-weight : bold");
		}

		public void setLayoutProperies(ExtList v) {
			v.add(TYPE, "table").add(COLUMNS, 3).add(ITEM_CLS, "x-layout-table-items-form");
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(SalReserve.T.CUST.getFld().getCode())) {
				fldList.add(new ESalOrder.MyCust());
			} else if (fld.getCode().equals(SalReserve.T.WAREHOUSE.getFld().getCode())) {
				fldList.add(new ESalReserve.MyWarehouse()).setCloseStr(null);
			} else {
				super.setFldAttr(fld, fldList);
			}
		}

	}
	public static class MyWarehouse implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(SalReserve.TB).loadFunCode(ESalReserve.class, "myWarehouse"));
		}

		//@formatter:off	
  		/** Begin myWarehouse ********
		mvc.Tools.crtComboTrigger(false,'gs_GsWarehouse','',{
					name : 'bean.warehouse',
					fieldLabel : '仓库',
					listeners: {
						scope : this,
						change : function(field, newValue, oldValue, eOpts) {
							var grid = this.up('window').lineTable;
							mvc.Tools.doLoadStock(grid);
						}
					}
				})
  		*** End myWarehouse *********/
  		//@formatter:on	
	}
	/**
	 * 重构WIN 自定义窗口宽度、编辑表格的最小高度
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
		
		@Override
		public void initActs() {
		  getActs().addExp("{xtype : 'label',itemId : 'cqty'},'->'");
		  AddAct(new Act(getTb(), OAct.RESET),EMZipWin.class);
			AddAct(new Act(getTb(), OAct.CLOSE),EMZipWin.class);
			AddAct(new Act(getTb(), OAct.SAVE),EMZipWin.class);
		}

		public void initForm() {
			EMWin.initForm(getForm(), getPack(), getClazz());
			getFormList().addExp(XTYPE,
			    "Ext.create('mvc.view." + getPack() + "." + getClazz() + ".ListForm',{height : 300,border : false })");
		}
	}
	class MyListForm extends EMZipListForm {

		public MyListForm(Tb tb, VFld outfld, VFlds... vflds) {
			super(tb, outfld, vflds);
    }
		
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
		this.listeners = {
			scope : this,
			selectionchange : function(model, records) {
				mvc.Tools.doLoadStock(this);
			}
		}
		this.callParent(arguments);	
		 *** End initComponentGoods *********/
	}

	/**
	 * 重构复合界面产生器 将各种对象更改为上面的自定义对象类
	 * @author whx
	 * @version 创建时间：2014年11月7日 下午5:11:55
	 */
	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		public ExtFile newList() {
			return new MyZipList(getTb(), getMainFlds()).setOutVFlds(getOutVflds()).setSearchVFlds(getSearchVflds());
		}

		public ExtFile newForm() {
			return new MyForm(getTb(), getVfldsForm());
		}

		public ExtFile newZipWin(VFld fld) {
			return new MyWin(getTb(), fld);
		}
		@Override
		public ExtFile newZipListForm(Tb tb, VFld outfld, VFlds... vflds) {
			return new MyListForm(tb,outfld,vflds);//后来又加了取默认价、计划金额功能，暂不用产生器产生 TODO
		}
	}

}
