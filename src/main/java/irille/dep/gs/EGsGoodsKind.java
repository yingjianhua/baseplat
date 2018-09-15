package irille.dep.gs;

import irille.gl.gs.GsGoodsKind;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMCrtTrigger;
import irille.pub.html.EMForm;
import irille.pub.html.EMModel;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EGsGoodsKind extends GsGoodsKind {

	public static void main(String[] args) {
		new EGsGoodsKind().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds vflds = new VFlds(TB);
		VFlds searchVflds = new VFlds(T.CODE, T.NAME);
		EMCrt ext = new MySimple(TB, vflds, searchVflds);
		ext.getVfldsList().del(T.SHORTKEY);
		ext.getVfldsForm().del(T.UPDATEBY, T.UPDATED_TIME, T.SHORTKEY);
		ext.getVfldsForm().get(T.CODE).setReadOnly("!this.insFlag");
		ext.getVfldsForm().get(T.PARENT).setReadOnly("!this.insFlag");
		ext.newExts().init();
		//选择器
		EMCrtTrigger trigger = new EMCrtTrigger(TB, vflds, T.NAME, new VFlds(T.CODE, T.NAME));
		trigger.newExts().init().crtFiles();
		return ext;
	}

	public static class MyKind implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(GsGoodsKind.TB).loadFunCode(EGsGoodsKind.class, "myKind"));
		}

		//@formatter:off	
  		/** Begin myKind ********
 		xtype : 'beantrigger',
		name : 'bean.parent',
		fieldLabel : '上级类别',
		bean : 'GsGoodsKind',
		beanType : 'gs',
		emptyText : form_empty_text,
		readOnly : !this.insFlag,
		listeners : {
			scope : this,
			change : function(field,newv,oldv,opts) {
				var me = this;
				if (!newv){
					me.down('[name=bean.cust1]').setValue(null);
					me.down('[name=bean.cust2]').setValue(null);
					me.down('[name=bean.cust3]').setValue(null);
					me.down('[name=bean.cust4]').setValue(null);
					me.down('[name=bean.cust5]').setValue(null);
					return;
				}
				if(me.insFlag){ 
				var urlCust = base_path+ '/gs_GsGoodsKind_loadCust?sarg1=' + newv;
    		Ext.Ajax.request({
    			url : urlCust,
    			method : 'GET',
    			success : function(response) {
    				rtn = Ext.JSON.decode(response.responseText, true);
    				if (rtn.success){
	    				me.down('[name=bean.cust1]').setValue(rtn.cust1);
	    				me.down('[name=bean.cust2]').setValue(rtn.cust2);
	    				me.down('[name=bean.cust3]').setValue(rtn.cust3);
	    				me.down('[name=bean.cust4]').setValue(rtn.cust4);
	    				me.down('[name=bean.cust5]').setValue(rtn.cust5);
    				}else{
    					Ext.MessageBox.show({
								title : msg_title, 
								msg : rtn.msg,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
    				}
    			}
    		})
    		};
			}
		}
  		*** End myKind *********/
  		//@formatter:on	
	}

	public static class MyForm extends EMForm<MyForm> {

		public MyForm(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(GsGoodsKind.T.PARENT.getFld().getCode())) {
				fldList.add(new MyKind());
			} else {
				super.setFldAttr(fld, fldList);
			}
		}

	}

	public static class MySimple extends EMCrtSimple<MySimple> {

		public MySimple(Tb tb, VFlds vflds, VFlds searchVflds) {
			super(tb, vflds, searchVflds);
		}

		public ExtFile newForm() {
			if (_form == null)
				_form = new MyForm(getTb(), getVfldsForm());
			return _form;
		}

	}
}
