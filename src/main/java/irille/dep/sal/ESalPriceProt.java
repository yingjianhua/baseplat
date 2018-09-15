package irille.dep.sal;

import irille.pss.sal.SalOrder;
import irille.pss.sal.SalPriceProt;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMForm;
import irille.pub.html.EMModel;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class ESalPriceProt extends SalPriceProt {
	
	public static void main(String[] args) {
		new ESalPriceProt().crtExt().crtFiles();
	}
	
	public EMCrt crtExt() {
		VFlds vflds = new VFlds(TB);

		VFlds searchVflds = new VFlds(T.CELL, T.CUST);
		VFlds listFlds = new VFlds().addAll(TB);
		EMCrt ext = new MySimple(TB, vflds, searchVflds);
		VFlds vs = ext.getVfldsForm();
		vs.setReadOnly("true",  T.NAME);// 只读设置
		vs.setNull(true,T.NAME);// 为空设置
		ext.setVfldsList(listFlds);
		ext.newExts().init();
		return ext;
	}
	/**
	 * 重构IEXTOUT，用于FORM中客户的控件实现
	 * 
	 * @author whx
	 * @version 创建时间：2015年7月28日09:12:18
	 */
	public static class MyCust implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(SalPriceProt.TB).loadFunCode(ESalPriceProt.class, "myCust"));
		}

		//@formatter:off	
  		/** Begin myCust ********
 		xtype : 'comboauto',
		fieldLabel : '客户',
		name : 'bean.cust',
		listConfig : {minWidth:250},
		fields : ['pkey','code', 'name'],//查询返回信息model
		valueField : ['pkey'],//提交值
		textField : 'code', //显示信息
		queryParam : 'code',//搜索使用
		url : base_path + '/sys_SysCustom_autoComplete',
		urlExt : 'sys.SysCustom',
		hasBlur : false,
		afterLabelTextTpl : required,
		allowBlank : false,
		listeners : {
			scope : this,
			blur : function(field){
				var me = this;
				if (!field.getRawValue()){
					me.down('[name=bean.name]').setValue(null);
	    		return;
	    	}
				var urlCust = base_path+ '/sys_SysCustom_loadInfoDetail?sarg1=' + field.getRawValue();
	    		Ext.Ajax.request({
	    			//async : false, //加上同步限制后，单元格之间切换会中断
	    			url : urlCust,
	    			method : 'GET',
	    			success : function(response) {
	    				rtn = Ext.JSON.decode(response.responseText, true);
	    				me.down('[name=bean.cust]').setValue(rtn.cust);
	    				me.down('[name=bean.name]').setValue(rtn.custName);
	    			},
	    			failure : function(response) {
	    				Ext.example.msg(msg_title, msg_ajax);
	    			}
	    		});
			}
		} 	
  		
  		*** End myCust *********/
  		//@formatter:on	
	}
	public static class MyForm extends EMForm<MyForm> {

		public MyForm(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(SalPriceProt.T.CUST.getFld().getCode())) {
				fldList.add(new MyCust());
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
