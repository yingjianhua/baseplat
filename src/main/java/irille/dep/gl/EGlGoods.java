/**
 * 
 */
package irille.dep.gl;

import irille.core.sys.SysCell;
import irille.gl.gl.GlGoods;
import irille.gl.gl.GlGoodsLine;
import irille.pub.bean.CmbGoods;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtCompSimple;
import irille.pub.html.EMCrtModelAndStore;
import irille.pub.html.EMForm;
import irille.pub.html.EMWinSearch;
import irille.pub.html.EMZipList;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.tb.Fld;
import irille.pub.tb.FldStr;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EGlGoods extends GlGoods {
	public static void main(String[] args) {
		new EGlGoods().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		CmbGoods.TB.getCode();
		GlGoodsLine.TB.getCode();
		
		VFlds vflds = VFlds.newVFlds(new VFlds[] { new VFlds(TB), EMCrtModelAndStore.getGoodsVflds(T.GOODS.getFld()), 
				new VFlds("NULL").add(SysCell.fldOutKey())});
		vflds.moveAfter("goodsSpec", "goods").moveAfter("goodsName", "goods").moveBefore("cell", "goods");
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.JOURNAL, T.GOODS) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		Fld fldName = new FldStr("goodsName", "货物名称", 100, true);
		Fld fldSpec = new FldStr("goodsSpec", "货物规格", 100, true);
		VFlds vflds_link;
		vflds_link = new VFlds("link").add(fldName.getVFld()).add(fldSpec.getVFld());
		VFlds mainVflds = VFlds.newVFlds(new VFlds(T.GOODS), vflds_link,new VFlds(T.QTY,T.PRICE,T.BALANCE));
		EMCrt ext = new MyCrt(TB, vflds, new VFlds[] {mainVflds}, searchVflds, new VFlds[] { new VFlds(GlGoodsLine.T.GOODS) });
		VFlds vl = ext.getVfldsList();
		vl.get(T.JOURNAL).setWidthList(120);
		vl.get(T.BATCH).setHidden("true");
		vl.moveLast(T.ENABLED);
		VFlds vf = ext.getVfldsForm();
		vf.setReadOnly("!this.insFlag", T.GOODS);
		vf.get("cell").setReadOnly("!this.insFlag");
		vf.del(T.UOM,T.JOURNAL);
		vf.setGoodsLink(T.GOODS);
		vf.get(T.QTY).setReadOnly("true");
		vf.get(T.BALANCE).setReadOnly("true");
		vf.get(T.BATCH).setHidden("true");
		((EMWinSearch)ext.newWinSearch()).getVFlds().del("cell");
		ext.newExts().init();
		return ext;
	}
  class MyList extends EMZipList {
  	public MyList(Tb tb, VFlds... vflds) {
  		super(tb, vflds);
  	}
  	@Override
  	public void initForm() {
  		initFormDocked();
  		ExtList form = getForm();
  		form.add(XTYPE,"form")
  			.addExp("itemId"," this.oldId+'main'")
  			.add("bodyPadding","5 5 0 5")
  			.AddList("fieldDefaults")
  				.add(ANCHOR,"100%")
  				.add(LABEL_WIDTH,100)
  				.add(WIDTH,275)
  				.add("labelAlign","right")
  				.add(READ_ONLY,true);
  		
  		initFormTabpanel();
  		initFormMainTable();
  		form.AddDime(ITEMS, getFormMainTable());
  		
  	//@formatter:on
  	}
  }
	class MyCrt extends EMCrtCompSimple {

		public MyCrt(Tb tb, VFlds vflds,VFlds[] mainvflds, VFlds[] searchVflds,
				 VFlds[] outVflds) {
			super(tb, new VFlds[] {vflds}, mainvflds, searchVflds, outVflds);
		}
		
		@Override
		public ExtFile newList() {
			return new MyList(getTb(), getMainFlds()).setOutVFlds(getOutVflds())
					.setSearchVFlds(getSearchVflds());
		}
		
		@Override
		public ExtFile newForm() {
			if (_form == null)
				_form = new EMForm(getTb(), getVfldsForm());
			_form.setGoodsLink(true);
			return _form;
		}
		
		@Override
		public MyCrt newExts() {
			addExt(newModel());
			addExt(newStore());
			addExt(newList());
			addExt(newWinSearch());
			if (isCrtWinAndForm()) {
				addExt(newWin());
				addExt(newForm());
			}
			VFlds vflds,vflds_link;
			addExt(newZipListMain());
				for (VFld fld : getOutVflds().getVFlds()) {
					vflds=new VFlds().addAll(fld.getFld().getTb());
					//EXT上的技术问题待解决，暂只能把单位移动到数量之后，避免货物编辑时"TAB"造成的单位数据冲突
					if (vflds.chk("goods")) {
						Fld fldName = new FldStr("goodsName", "货物名称", 100, true);
						Fld fldSpec = new FldStr("goodsSpec", "货物规格", 100, true);
						vflds_link = new VFlds("link").add(fldName.getVFld()).add(fldSpec.getVFld());
						setNowOutKeyModelVFlds(vflds, vflds_link);
					}else {
						setNowOutKeyModelVFlds(vflds);
					}
					setNowOutKeyLineVflds(vflds);
					getNowOutKeyLineVflds().get(GlGoodsLine.T.DAYBOOK_LINE).setName("记账单据");
					crtOutFld(fld);
				}
			return this;
		}
	}
}
