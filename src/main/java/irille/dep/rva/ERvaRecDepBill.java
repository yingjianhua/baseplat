/**
 * 
 */
package irille.dep.rva;

import irille.dep.pya.EPyaPayDepBill;
import irille.gl.rva.RvaRecDepBill;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMForm;
import irille.pub.html.EMList;
import irille.pub.html.ExtExp;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFlds;

public class ERvaRecDepBill extends RvaRecDepBill {
	public static void main(String[] args) {
		new ERvaRecDepBill().crtExt().crtFiles();
		;
	}

	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.AMT, T.JOURNAL) };
		EMCrt ext = new MyComp(TB, vflds, searchVflds);
		VFlds vl = ext.getVfldsList();
		vl.get(T.JOURNAL).setWidthList(250);
		vl.get(T.SUMMARY).setWidthList(190);
		vl.moveLast(T.ORG);
		vl.setExpandAndHidden("true", T.DEPT, T.CELL, T.APPR_BY, T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME, T.CREATED_BY,
		    T.CREATED_TIME, T.REM);

		VFlds vf = ext.getVfldsForm();
		vf.get(T.JOURNAL).attrs().addExp("diySql", "subject");
		vf.get(T.DATE_START).attrs().addExp("value", "new Date()");
		vf.del(T.CREATED_BY, T.CREATED_TIME, T.CELL, T.TALLY_BY, T.TALLY_TIME, T.APPR_BY, T.APPR_TIME, T.ORG, T.DEPT,
		    T.CODE, T.STATUS);
		vf.moveLast(T.REM);
		((EMList) ext.newList()).setLineActs(false);
		((EMList) ext.newList()).setExtendRow();
		ext.newExts().init();
		return ext;
	}

	class MyComp extends EMCrtSimple<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] searchVflds) {
			super(tb, vflds, searchVflds);
		}

		public ExtFile newList() {
			if (_list == null)
				_list = new MyList(getTb(), getVfldsList()).setSearchVFlds(getSearchVflds());
			return _list;
		}
		public ExtFile newForm() {
			if(_form == null) 
				_form = new MyForm(getTb(), getVfldsForm());
			return _form;
		}
	}

	class MyForm extends EMForm<MyForm> {

		public MyForm(Tb tb, VFlds... vflds) {
	    super(tb, vflds);
    }
		
		@Override
		public void initComponent(ExtFunDefine fun) {
			fun.add(loadFunCode(ERvaRecDepBill.class, "initComponentMy",getPack(),getClazz()));
			//@formatter:off
			/** Begin initComponentMy ********
				if (this.insFlag)
					this.url = this.url + 'ins';
				else
					this.url = this.url + 'upd';
				var formFlds = [];
				var subject = "";
				Ext.Ajax.request({
					async : false,
					url : base_path+'/【0】_【1】_getSubjects',
					success : function (response, options) {
						subject =  response.responseText;
					}
				});
				formFlds.push
			*** End initComponentMy *********/		
			//@formatter:on
			fun.add(getColumns());
			fun.add("	this.items = ");
			fun.AddDime(getForm()); // 是否需要"[]"有待验证 whx 20141015
			fun.add("	this.callParent(arguments);" + LN);
		}
	}


	class MyList extends EMList<MyList> {

		public MyList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		public void loadTbAct(Class funCodeFile, Act act) {
			IEnumOpt oact = act.getAct();
			ExtAct v = null;
			if (oact == OAct.DO_APPR || oact == OAct.UN_APPR || oact == OAct.DO_TALLY || oact == OAct.UN_TALLY)
				v = new ExtAct(this, act, EPyaPayDepBill.class, getPack(), getClazz(), getTb().getName());
			else if (oact == OAct.DO_NOTE)
				v = new ExtAct(this, act, EPyaPayDepBill.class, getTb().getClazz().getName());
			else {
				super.loadTbAct(funCodeFile, act);
				return;
			}
			v.add(TEXT, act.getName()).add(ICON_CLS, act.getIcon()).addExp("itemId", "this.oldId+'" + act.getCode() + "'")
			    .add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper()).addExp("disabled", "this.lock");
			getActs().add(v);
		}

		public void initFuns() {
			AddFun("onChangeStatus", EPyaPayDepBill.class).addFunParasExp("status");
			ExtFunDefine fun = AddList("listeners") // 表格行选择事件，主要设置按钮是否可用
			    .AddFunDefine("selectionchange", new ExtExp("selModel, selected"));
			fun.add(loadFunCode(EPyaPayDepBill.class, "initChange"));
			initFunsAddOtherFuns();
			initFunsAddActs();
			add(AddFun("onSearchCancel", EMList.class));
			add(AddFun("onSearch", EMList.class));
			add(AddFun("onSearchAdv", EMList.class, getPack(), getClazz()));
			add(AddFunD("onSearchDo", EMList.class, new ExtExp("array")));
		}

	}

}
