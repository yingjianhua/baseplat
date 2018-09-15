/**
 * 
 */
package irille.dep.gl;

import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlDaybookLine;
import irille.pub.bean.CmbGoods;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtCompSimple;
import irille.pub.html.EMZipList;
import irille.pub.html.ExtFile;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFldOutKey;
import irille.pub.view.VFlds;

public class EGlDaybook extends GlDaybook {
	public static void main(String[] args) {
		new EGlDaybook().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		CmbGoods.TB.getCode();
		GlDaybookLine.TB.getCode();
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE,T.BILL, T.WORK_DATE, T.CREATE_TIME, T.TALLY_BY,T.ORG) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.ORG, T.WORK_DATE) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		EMCrt ext = new MyComp(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(GlDaybookLine.T.PKEY) });
		ext.getVfldsList().get(T.CODE).setWidthList(140);
		ext.newExts();//.init();
		VFlds vl = ((EMCrtCompSimple)ext).getNowOutKeyLineVflds();
		((EMCrtCompSimple)ext).setNowOutKeyLineVflds(vl);
		ext.init();
		//子表的选项需要单独调用
		EMCrt.crtOpt(GlDaybookLine.TB);
		return ext;
	}
	class MyComp extends EMCrtCompSimple {
		public MyComp(Tb tb, VFlds[] vflds,VFlds[] mainvflds, VFlds[] searchVflds,
				 VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}
		@Override
		public EMCrtCompSimple setNowOutKeyLineVflds(VFlds... nowOutKeyLineVflds) {
			VFlds vl = nowOutKeyLineVflds[0];
			//设置明细表的字段宽度
			vl.get(GlDaybookLine.T.JOURNAL).setWidthList(250);
			vl.get(GlDaybookLine.T.SUMMARY).setWidthList(210);
			vl.get(GlDaybookLine.T.CURRENCY).setWidthList(75);
			vl.get(GlDaybookLine.T.IN_FLAG).setWidthList(75);
			vl.get(GlDaybookLine.T.DOC_NUM).setWidthList(150);
			if (vl.chk(GlDaybookLine.T.AGENT_CELL.getFld().getCode()))
				vl.del(GlDaybookLine.T.AGENT_CELL);
			return super.setNowOutKeyLineVflds(nowOutKeyLineVflds);
		}
		@Override
		public ExtFile newList() {
			return new MyList(getTb(), getMainFlds()).setOutVFlds(getOutVflds())
					.setSearchVFlds(getSearchVflds());
		}
		
		@Override
		public ExtFile newWin() {
			return null;
		}
		@Override
		public ExtFile newForm() {
			return null;
		}
	}
	class MyList extends EMZipList {
		public MyList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}
		
		@Override
		public void loadTbAct(Class funCodeFile, Act act) {//让doTally按钮 常亮
			IEnumOpt oact = act.getAct();
			if (oact != OAct.INS && oact != OAct.EDIT && oact != OAct.UPD && oact != OAct.DEL && oact != OAct.DO_APPR
			    && oact != OAct.UN_APPR && oact != OAct.DO_TALLY && oact != OAct.UN_TALLY && oact != OAct.DO_ENABLED
			    && oact != OAct.UN_ENABLED && oact != OAct.DO_NOTE && oact != OAct.PRINT)
				return;
			ExtAct v = null;
			if (oact == OAct.DO_APPR || oact == OAct.UN_APPR ||
					oact == OAct.DO_TALLY || oact == OAct.UN_TALLY)
				v = new ExtAct(this, act, funCodeFile, getPack(), getClazz(), getTb().getName());
			else if (oact == OAct.DO_NOTE)
				v = new ExtAct(this, act, funCodeFile, getTb().getClazz().getName());
			else if (oact == OAct.PRINT)
				v = new ExtAct(this, act, funCodeFile, getTb().getClazz().getSimpleName());
			else
				v = new ExtAct(this, act, funCodeFile);
			v.add(TEXT, act.getName()).add(ICON_CLS, act.getIcon()).addExp("itemId", "this.oldId+'" + act.getCode() + "'")
			    .add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper());
			getActs().add(v);
			if (oact == OAct.EDIT || oact == OAct.UPD || oact == OAct.DEL || oact == OAct.DO_APPR || oact == OAct.UN_APPR
					|| oact == OAct.UN_TALLY || oact == OAct.DO_NOTE
			    || oact == OAct.DO_ENABLED || oact == OAct.UN_ENABLED || oact == OAct.PRINT) {
				v.addExp("disabled", "this.lock");
			}
		}
		
		public void initFuns() {
			AddFun("onSaveRecord", EGlDaybook.class).addFunParasExp("form, data");
			super.initFuns();
		}
	}
}

//@formatter:off

/** Begin onSaveRecord ********
	console.log(data);
	this.mdMainTable.store.load();
	//this.mdMainTable.store.insert(0,data);
	//this.mdMainTable.getView().select(0);
	//Ext.example.msg(msg_title, msg_text);
*** End onSaveRecord *********/

/** Begin onDoTally ********
	var win = Ext.create('mvc.view.gl.GlDaybook.Win',{
		title : this.title+'>记账统一入口',
		descUrl : 'gl_GlDaybook_tally',
		descType : 2203, //目标单据类型
	});
	win.on('create',this.onSaveRecord,this);
	win.show();
*** End onDoTally *********/

/** Begin onUnTally ********
var selections = this.mdMainTable.getView().getSelectionModel().getSelection();
var me = this;
if (selections.length > 0){
	Ext.MessageBox.confirm(msg_confirm_title, '确定取消记账吗？',
		function(btn) {
			if (btn != 'yes')
				return;
			var arr = new Array(),formv;
			for (var i=0; i<selections.length; i++){
				formv = selections[i].get('bean.pkey').toString();
				console.log(formv);
				arr.push(formv.split(bean_split)[0]);
			}
			console.log(arr);
			Ext.Ajax.request({
				url : base_path+'/【0】_【1】_untally?pkeys='+arr.toString(),
				success : function (response, options) {
					var result = Ext.decode(response.responseText);
					if (result.success){
						me.mdMainTable.getStore().remove(selections);
						Ext.example.msg(msg_title, '记账取消--成功');
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
*** End onUnTally *********/

/** Begin initFormListMain ********
{
			 scope : this,
			 selectionchange: function(model, records) {
				 if (records.length === 1){
				    this.mdMain.getForm().loadRecord(records[0]);
						this.mdLineTable.store.filter([{'id':'filter', 'property':'【0】','value':records[0].get('bean.pkey')}]);
						if (this.roles.indexOf('unTally') != -1)
							this.down('#'+this.oldId+'unTally').setDisabled(false);
				 }else {
				   	this.mdMain.getForm().reset();
				   	this.mdLineTable.store.removeAll();
				   	if(records.length === 0) {
				   		if(this.roles.indexOf('unTally') != -1)
				   			this.down('#'+this.oldId+'unTally').setDisabled(true);
				   	}else {
							if (this.roles.indexOf('del') != -1)
								this.down('#'+this.oldId+'unTally').setDisabled(false);
						}
				 }
			 }
}
*** End initFormListMain *********/
//@formatter:on
