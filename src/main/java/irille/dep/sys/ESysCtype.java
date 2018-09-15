/**
 * 
 */
package irille.dep.sys;

import irille.core.sys.SysCtype;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class ESysCtype extends SysCtype {

	public static void main(String[] args) {
		new ESysCtype().crtExt().crtFiles();
	}
	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CTYPE_NAME) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.newExts().init();
		
		return ext;
	}	
	//@formatter:off	
			/** Begin onEdit ********
	var selection = this.getView().getSelectionModel().getSelection()[0];
		if (selection){
			var win = Ext.create('mvc.view.sys.SysCtype.WinEdit',{
				title : this.title+'['+selection.get('bean.ctypeName')+']>编辑',
				record : selection
			});
			win.show();
			win.loadData();
		}	
		 *** End onEdit *********/
			//@formatter:on	
}
