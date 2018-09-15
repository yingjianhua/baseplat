package irille.dep.sal;

import irille.pss.sal.SalCollect;
import irille.pss.sal.SalSaleLine;
import irille.pub.bean.CmbGoods;
import irille.pub.ext.Ext;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMForm;
import irille.pub.html.EMList;
import irille.pub.html.EMWin;
import irille.pub.html.ExtExp;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFlds;

public class ESalCollect extends SalCollect {
	public static void main(String[] args) {
		new ESalCollect().crtExt();
	}

	public void crtExt() {
		CmbGoods.TB.getCode();
		SalSaleLine.TB.getCode();
		VFlds[] vflds = new VFlds[] { new VFlds(TB)};
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		MySimple ext = new MySimple(TB,vflds,null); 
		ext.newExts().init();
		ext.crtFiles();
	}
	class MyList extends EMList<MyList> {

		public MyList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		@Override
		public void loadTbAct(Class funCodeFile, Act act) {
			IEnumOpt oact = act.getAct();
			if(act.getCode().equals("fdToday")) {
				ExtAct v = new ExtAct(this, act, ESalCollect.class);
				v.add(TEXT, act.getName()).add(ICON_CLS, "search-icon").addExp("itemId", "this.oldId+'" + act.getCode() + "'")
				    .add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper())/*.addExp("disabled", "this.lock")*/;
				getActs().add(v);
			} else if(act.getCode().equals("fdMonth")) {
				ExtAct v = new ExtAct(this, act, ESalCollect.class);
				v.add(TEXT, act.getName()).add(ICON_CLS, "search-icon").addExp("itemId", "this.oldId+'" + act.getCode() + "'")
				    .add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper())/*.addExp("disabled", "this.lock")*/;
				getActs().add(v);
			} else if(act.getCode().equals("fdTotal")) {
				ExtAct v = new ExtAct(this, act, ESalCollect.class);
				v.add(TEXT, act.getName()).add(ICON_CLS, "search-icon").addExp("itemId", "this.oldId+'" + act.getCode() + "'")
				    .add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper())/*.addExp("disabled", "this.lock")*/;
				getActs().add(v);
			} else if(act.getCode().equals("fdLimit")) {
				ExtAct v = new ExtAct(this, act, ESalCollect.class);
				v.add(TEXT, act.getName()).add(ICON_CLS, "search-icon").addExp("itemId", "this.oldId+'" + act.getCode() + "'")
				    .add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper())/*.addExp("disabled", "this.lock")*/;
				getActs().add(v);
			} else{
				super.loadTbAct(funCodeFile, act);
			}
		}

		@Override
		public void initFormSearch() { //不要搜索栏
		  // TODO Auto-generated method stub
		 // super.initFormSearch();
		}
		
		@Override
		public void initMainAct(ExtFunDefine fun, ExtAct act) {//不要 if (this.roles.indexOf 这句话
		  // TODO Auto-generated method stub
		  //super.initMainAct(fun, act);
		  fun.addParas("mainActs.push", act);
		}
		@Override
		public void initForm() {
		  // TODO Auto-generated method stub
		 // super.initForm();
		}
		
		@Override
		public void initFuns() {
			AddFun("onLimitRecord", ESalCollect.class).addFunParasExp("form,data");
			initFunsAddOtherFuns();
			initFunsAddActs();
			//super.initFuns();
			//add(AddFun("onLimitRecord", ESalCollect.class).addFunParas(new ExtExp("data,items")));
			add(AddFun("onSaveRecord", ESalCollect.class).addFunParas(new ExtExp("param")));
		}
		//@formatter:off	
		/** Begin onLimitRecord ********
			this.getStore().loadData(data.items);
		 *** End onLimitRecord *********/
		
		/** Begin onSaveRecord ********
			var me = this;
				Ext.Ajax.request({
					url : base_path+'/sal_SalCollect_list?'+param,
					success : function (response, options) {
						var result = Ext.decode(response.responseText);
						if (result.success){
							me.getStore().loadData(result.items);
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
	 *** End onSaveRecord *********/
		
	
		/** Begin onFdToday ********
		this.onSaveRecord("date=today");
		 *** End onFdToday *********/
		
		/** Begin onFdMonth ********
		this.onSaveRecord("date=month");
		 *** End onFdMonth *********/
		
		/** Begin onFdTotal ********
		this.onSaveRecord("date=total");
		 *** End onFdTotal *********/
		
		/** Begin onFdLimit ********
		var win = Ext.create('mvc.view.sal.SalCollect.Win',{
			title : this.title+'>查询	'
		});
		win.on('create',this.onLimitRecord,this);
		win.show();
		 *** End onFdLimit *********/
		
		//@formatter:on	

	}
	class MyWin extends EMWin<MyWin> {

		public MyWin(Tb tb) {
			super(tb);
		}

		@Override
		public void initActs() {
			AddActWin(new Act(getTb(), OAct.RESET),EMWin.class);
			AddActWin(new Act(getTb(), OAct.CLOSE),EMWin.class);
			AddActWin(new Act(getTb(), OAct.SEARCH),ESalCollect.class);
		}
	}

	//@formatter:off	
	/** Begin onSearch ********
	var form = this.form.getForm();
	if (form.isValid()) {
		form.submit({
			url : this.form.url,
			submitEmptyText: false,
			type : 'ajax',
			//params : {insFlag : this.insFlag},
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
	*** End onSearch *********/
	
	class MyForm extends EMForm<MyForm> {

		@Override
		public void initColumns() {
		  // TODO Auto-generated method stub
		 // super.initColumns();
		}
		public MyForm(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		@Override
		public void initAttrs() {
			add(EXTEND, "Ext.form.Panel");
			AddDime(REQUIRES, "Ext.ux.DataTip");
			add(LAYOUT, "form");
			add(BORDER, false);
			add(FRAME, false);
			add(INS_FLAG, true);
			add(BODY_PADDING, "5 5 5 5");
			addExp(URL, Ext.url(Ext.getPag(getTb()), Ext.getClazz(getTb()), "list"));//重构后修改了这个方法
			setFieldDefaultsProperies(AddList(FIELD_DEFAULTS));
		}
		
		@Override
		public void initComponent(ExtFunDefine fun) {
			fun.add(loadFunCode(ESalCollect.class, "initComponentFormMy"));
			//@formatter:off
			/** Begin initComponentFormMy ********
				var formFlds = [];
				formFlds.push({
					xtype : 'datefield',
					name : 'startDate',
					afterLabelTextTpl : required,
					allowBlank : false,
					fieldLabel : '开始时间',
					format : 'Y-m-d'
				},{
					xtype : 'datefield',
					name : 'expireDate',
					afterLabelTextTpl : required,
					allowBlank : false,
					fieldLabel : '截止时间',
					format : 'Y-m-d'
				});
			*** End initComponentFormMy *********/		
			//@formatter:on
			//fun.add(getColumns());
			fun.add("	this.items = ");
			fun.AddDime(getForm()); // 是否需要"[]"有待验证 whx 20141015
			fun.add("	this.callParent(arguments);" + LN);
		  
		}
	}
	private class MySimple extends EMCrtSimple {
		
		public MySimple(Tb tb, VFlds[] vflds, VFlds[] searchVflds) {
			super(tb, vflds, searchVflds);
		}
		
		@Override
		public ExtFile newList() {
			if (_list == null) 
				_list = new MyList(getTb(), getVfldsList());
			return _list;
		}
		
		@Override
		public ExtFile newWin() {
			if(_win == null) 
				_win = new MyWin(getTb());
			return _win;
		}
		
		@Override
		public ExtFile newForm() {
			if(_form == null) 
				_form = new MyForm(getTb());
			return _form;
		}
		
		@Override
		public MySimple newExts() {
			addExt(newModel());
			addExt(newStore());
			addExt(newList());
			addExt(new MyWin(getTb()));
			addExt(new MyForm(getTb()));
			return (MySimple) this;
		}
	}
}
