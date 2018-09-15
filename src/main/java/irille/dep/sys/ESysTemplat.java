/**
 * 
 */
package irille.dep.sys;

import irille.core.sys.Sys;
import irille.core.sys.SysTemplat;
import irille.core.sys.SysTemplatCell;
import irille.gl.gs.GsStockBatch.T;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtEdit;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMCrtTrigger;
import irille.pub.html.EMList;
import irille.pub.html.EMListEdit;
import irille.pub.html.EMModel;
import irille.pub.html.EMStore;
import irille.pub.html.EMWinSearch;
import irille.pub.html.ExtDime;
import irille.pub.html.ExtExp;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.ExtList;
import irille.pub.html.Exts;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class ESysTemplat extends SysTemplat {
	public static void main(String[] args) {
		new ESysTemplat().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds vflds = new VFlds(TB);
		VFlds searchVflds = new VFlds(T.CODE);
		EMCrt ext = new MyCrt(TB, vflds, searchVflds);
		ext.getVfldsForm().del(T.ENABLED);
		ext.getVfldsForm().get(T.TYPE).setHidden("true").setDefaultValue(Sys.OTemplateType.GL.getLine().getKey());
		((EMWinSearch)ext.newWinSearch()).getVFlds().del(T.TYPE);
		ext.newExts().init();

		EMCrt extTrigger = new EMCrtTrigger(TB, vflds, T.NAME, new VFlds(T.CODE));
		extTrigger.newExts().init().crtFiles();

		VFlds editVflds = new VFlds().addWithout(SysTemplat.TB, SysTemplat.T.PKEY, SysTemplat.T.REM);
		VFlds[] editOuts = new VFlds[] { new VFlds().add(SysTemplatCell.TB, SysTemplatCell.T.PKEY,
		    SysTemplatCell.T.TEMPLAT, SysTemplatCell.T.CELL) };
		VFld[] outflds = new VFld[] { SysTemplatCell.T.TEMPLAT.getFld().getVFld() };
		EMCrt extEdit = new MyEdit(TB, editVflds, editOuts, outflds);
		extEdit.newExts().init().crtFiles();
		
		return ext;
	}

	class MyList extends EMList<MyList> {

		public MyList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		public void initComponent(ExtFunDefine fun) {
			// 明细行功能定义
			initLineActs(fun);
			// 主界面功能定义
			initMainActs(fun);
			// 单元格定义 //表格列定义：包括名称、对应模型名、排序及渲染器
			fun.add("this.columns = ");
			fun.add(getColumns());
			if (getIndexGoods() > 0)
				fun.add("		mvc.Tools.doGoodsLine(this.columns, " + getIndexGoods() + ");");
			fun.add(loadFunCode(MyList.class, "initComponentMy", getPack(), getClazz()));
			//@formatter:off	
	/** Begin initComponentMy ********
			if (mainActs.length > 0)
				this.tbar=mainActs;
			this.store=Ext.create('mvc.store.【0】.【1】'); 
			this.store.proxy.url = base_path + '/sys_SysTemplat_listGl';
			this.store.remoteFilter = true;
			this.store.proxy.filterParam = 'filter';
			this.on({cellclick:mvc.Tools.onCellclick});
	*** End initComponentMy *********/
	//@formatter:on

			fun.add("this.dockedItems=");
			fun.AddDime().add(getFormDocked()).add(getFormTable());
			fun.add("		this.callParent(arguments);");
			fun.add("		mvc.Tools.onENTER2SearchBar(this.down('[dock=top]'),this);");
		}

	}

	class MyCrt extends EMCrtSimple<MyCrt> {

		public MyCrt(Tb tb, VFlds vflds, VFlds searchVflds) {
			super(tb, vflds, searchVflds);
		}

		public ExtFile newList() {
			if (_list == null)
				_list = new MyList(getTb(), getVfldsList()).setSearchVFlds(getSearchVflds());
			return _list;
		}

	}

	public static class MyEdit extends EMCrtEdit {

		public MyEdit(Tb tb, VFlds vflds, VFlds[] outVflds, VFld[] outs) {
			super(tb, vflds, outVflds, outs);
		}

		public void crtOutFld(VFld out, VFlds flds) {
			EMListEdit eml = new EMListEdit((Tb) out.getTb(), out, flds);
			eml.getVfld(SysTemplatCell.T.CELL).setWidthList(200);
			addExt(eml);
			addExt(new EMModel((Tb) out.getFld().getTb(), new VFlds().addAll(out.getFld().getTb())));
			addExt(new EMStore((Tb) out.getFld().getTb()));
			crtOpt((Tb) out.getFld().getTb());
		}

	}
}
