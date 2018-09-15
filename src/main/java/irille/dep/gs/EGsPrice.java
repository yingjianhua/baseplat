package irille.dep.gs;

import irille.gl.gs.GsPrice;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMForm;
import irille.pub.html.EMModel;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EGsPrice extends GsPrice {

	public static void main(String[] args) {
		new EGsPrice().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds vflds = VFlds.newVFlds(new VFlds[] { new VFlds(TB)});
		VFlds searchVflds = new VFlds(T.NAME);
		EMCrt ext = new MyComp(TB, vflds, searchVflds);
		VFlds vl = ext.getVfldsList();
		vl.moveAfter(T.RANGE_TYPE, T.NAME);
		vl.moveAfter(T.RANGE_PKEY,T.RANGE_TYPE);
		vl.moveAfter(T.CELL,T.RANGE_PKEY);
		VFlds vf = ext.getVfldsForm();
		vf.get(T.NAME_PRICE1).setNullFalse();
		ext.newExts().init();
		return ext;	
	}
	class MyForm extends EMForm<MyForm>{

		public MyForm(Tb tb, VFlds... vFlds) {
			super(tb, vFlds);
		}
		public void setFieldDefaultsProperies(ExtList v) {
			v.add(LABEL_WIDTH, 110).add(WIDTH, 275)
					.add(LABEL_STYLE, "font-weight : bold");
		}
		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(GsPrice.T.RANGE_TYPE.getFld().getCode())) {
				fldList.add(new MyRangeType()).setCloseStr(null);
			} else if (fld.getCode().equals(
					GsPrice.T.RANGE_PKEY.getFld().getCode())) {
				fldList.add(new MyRangePkey()).setCloseStr(null);
			} else {
				super.setFldAttr(fld, fldList);
			}
		}
		@Override
		public void initComponent(ExtFunDefine fun) {
			fun.add(loadFunCode(EMForm.class, "myComponent"));
			//@formatter:off
			/** Begin myComponent ********
				if(this.checkPrice)
					this.url = this.url + 'checkPrice';
				else if (this.insFlag)
					this.url = this.url + 'ins';
				else
					this.url = this.url + 'upd';
				var formFlds = [];
				formFlds.push
			*** End myComponent *********/		
			//@formatter:on
			fun.add(getColumns());
			fun.add("	this.items = ");
			fun.AddDime(getForm()); // 是否需要"[]"有待验证 whx 20141015
			fun.add("	this.callParent(arguments);" + LN);
			fun.add(loadFunCode(EMForm.class, "myBot"));
			//@formatter:off
			/** Begin myBot ********
	var rt = this.down('[name=bean.rangeType]');
	var rp = this.down('[name=bean.rangePkey]');
	if (rt.getValue() == 1) {
		rp.setDisabled(true);
		rp.hide();
	} else if (rt.getValue() > 10 && rt.getValue() <= 20) {
		rp.setFieldLabel('可视机构');
		rp.store.proxy.url = base_path + '/sys_SysOrg_getComboTrigger';
		rp.store.load();
	} else if (rt.getValue() >20 && rt.getValue() <= 30) {
		rp.setFieldLabel('可视单元');
		rp.store.proxy.url = base_path + '/sys_SysCell_getComboTrigger';
		rp.store.load();
	}
		*** End myBot *********/		
		//@formatter:on
		}
	}
	public static class MyRangeType implements IExtOut {

		@Override
		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(GsPrice.TB).loadFunCode(EGsPrice.class, "myRangeType"));
		}

		@Override
		public String toString(int tabs) {
			return null;
		}

		//@formatter:off	
  		/** Begin myRangeType ********
			mvc.Tools.crtComboForm(false,{
					name : 'bean.rangeType',
					fieldLabel : '可视范围',
					store : Ext.create('mvc.combo.sys.SysORangeType'),
					value : 1,
					listeners : {
						scope : this,
						change : function(field,newv,oldv,opts) {
							var range = this.down('[name=bean.rangePkey]');
							if (newv <= 10) {
								range.setDisabled(true);
								range.hide();
							} else {
								if (newv > 10 && newv <= 20) {
									range.setFieldLabel('可视机构');
									range.store.proxy.url = base_path + '/sys_SysOrg_getComboTrigger';
								} else if (newv >20 && newv <= 30) {
									range.setFieldLabel('可视单元');
									range.store.proxy.url = base_path + '/sys_SysCell_getComboTrigger';
								}
								range.store.load();
								range.setDisabled(false);
								range.show();
							}
						}
					}
				})
  		*** End myRangeType *********/
  		//@formatter:on	
		
	}
	public static class MyRangePkey implements IExtOut {
		
		@Override
		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(GsPrice.TB).loadFunCode(EGsPrice.class, "myRangePkey"));
		}
		
		@Override
		public String toString(int tabs) {
			return null;
		}
		
		//@formatter:off	
		/** Begin myRangePkey ********
		 mvc.Tools.crtComboTrigger(false,'sys_SysOrg','',{
			name : 'bean.rangePkey',
			fieldLabel : '可视机构',
			hidden : this.insFlag ? true : false,
			disabled : this.insFlag ? true : false
		})
		 *** End myRangePkey *********/
		//@formatter:on	
		
	}
	class MyComp extends EMCrtSimple<MyComp> {

		public MyComp(Tb tb, VFlds vflds, VFlds searchVflds) {
			super(tb, vflds, searchVflds);
		}

		@Override
		public ExtFile newForm() {
			return new MyForm(getTb(), getVfldsForm());
		}
	}
}
