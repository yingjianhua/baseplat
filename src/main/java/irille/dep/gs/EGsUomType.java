package irille.dep.gs;

import irille.gl.gs.GsUom;
import irille.gl.gs.GsUomType;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtCompSimple;
import irille.pub.html.EMCrtEdit;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EGsUomType extends GsUomType {
	public static void main(String[] args) {
		new EGsUomType().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		GsUom.TB.getCode();
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.NAME, T.ENABLED) };
		VFlds[] mainVflds = new VFlds[] { new VFlds(T.NAME, T.SHORTKEY, T.REM) };
		EMCrt ext = new EMCrtCompSimple(TB, vflds, mainVflds, searchVflds, new VFlds[] { new VFlds(GsUom.T.UOM_TYPE) });
		ext.getVfldsForm().del(T.ENABLED);
		ext.newExts().init();
		// 下面是编辑相关JS产生
		VFlds editVflds = new VFlds().addWithout(GsUomType.TB, GsUomType.T.PKEY);
		VFlds[] editOuts = new VFlds[] { new VFlds(GsUom.TB).del(GsUom.T.ROW_VERSION) };
		VFld[] outflds = new VFld[] { GsUom.T.UOM_TYPE.getFld().getVFld() };
		EMCrtEdit extEdit = new EMCrtEdit(TB, editVflds, editOuts, outflds);
		extEdit.newExts().init();
		extEdit.crtFiles();

		return ext;
	}

//@formatter:off	
	/** Begin initFormListMain ********
	 {
		scope : this,
        selectionchange: function(model, records) {
            if (records.length === 1){
                this.mdMain.getForm().loadRecord(records[0]);
				this.mdLineTable.store.filter([{'id':'filter', 'property':'【0】','value':records[0].get('bean.pkey')}]);
				if (this.roles.indexOf('upd') != -1)
					this.down('#'+this.oldId+'upd').setDisabled(false);
				if (this.roles.indexOf('del') != -1)
					this.down('#'+this.oldId+'del').setDisabled(false);
				if (this.roles.indexOf('edit') != -1)
					this.down('#'+this.oldId+'edit').setDisabled(false);
				if (this.roles.indexOf('doEnabled') != -1)
					this.down('#'+this.oldId+'doEnabled').setDisabled(false);
				if (this.roles.indexOf('unEnabled') != -1)
					this.down('#'+this.oldId+'unEnabled').setDisabled(false);
            }else{
            	this.mdMain.getForm().reset();
            	this.mdLineTable.store.removeAll();
            	if (this.roles.indexOf('upd') != -1)
					this.down('#'+this.oldId+'upd').setDisabled(true);
				if (this.roles.indexOf('del') != -1)
					this.down('#'+this.oldId+'del').setDisabled(true);
				if (this.roles.indexOf('edit') != -1)
					this.down('#'+this.oldId+'edit').setDisabled(true);
				if (this.roles.indexOf('doEnabled') != -1)
					this.down('#'+this.oldId+'doEnabled').setDisabled(true);
				if (this.roles.indexOf('unEnabled') != -1)
					this.down('#'+this.oldId+'unEnabled').setDisabled(true);
            }
        }
    }
*** End initFormListMain *********/
//@formatter:on
}
