package irille.dep.gl;

import irille.gl.gl.GlReportProfit;
import irille.gl.gl.GlReportProfitLine;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtCompSimple;
import irille.pub.html.EMWin;
import irille.pub.html.ExtFile;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EGlReportProfit extends GlReportProfit{
	public static void main(String[] args) {
		//new EGlReportProfit().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		//CmbGoods.TB.getCode();
		GlReportProfitLine.TB.getCode();
		VFlds[] vflds =  new VFlds[]{new VFlds(TB)};
		VFlds[] searchVflds = new VFlds[] {new VFlds(T.ORG,T.BEGIN_DATE,T.END_DATE)}; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		MyComp ext = new MyComp(TB, vflds, vflds, searchVflds, new VFlds[]{new VFlds(GlReportProfitLine.TB)});
		ext.newExts().init();
		return ext;
	}
	class MyComp extends EMCrtCompSimple {

		public MyComp(Tb tb, VFlds[] vflds,VFlds[] mainvflds, VFlds[] searchVflds,
				 VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}
		
		@Override
		public ExtFile newWin() {
			return new EMWin(getTb());
		}
		
		@Override
		public EMCrtCompSimple newExts() {
			addExt(newModel());
			addExt(newStore());
			addExt(newList());
			addExt(newWinSearch());
			addExt(newWin());
			addExt(newForm());
			VFlds vflds,vflds_link;
			addExt(newZipListMain());
				for (VFld fld : getOutVflds().getVFlds()) {
					vflds=new VFlds().addAll(fld.getFld().getTb());
					//EXT上的技术问题待解决，暂只能把单位移动到数量之后，避免货物编辑时"TAB"造成的单位数据冲突
					setNowOutKeyModelVFlds(vflds);
					setNowOutKeyLineVflds(vflds);
					crtOutFld(fld);
				}
			return this;
		}
	}
}
