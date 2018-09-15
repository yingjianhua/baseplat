package irille.dep.gs;

import irille.gl.gs.GsGoods;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimpleTwo;
import irille.pub.html.EMCrtTrigger;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMList;
import irille.pub.html.EMListTrigger;
import irille.pub.html.ExtFile;
import irille.pub.tb.Tb;
import irille.pub.view.VFlds;

public class EGsGoods extends GsGoods {
	public static void main(String[] args) {
		new EGsGoods().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds vflds = new VFlds(TB).del(T.PHOTO);
		VFlds searchVflds = new VFlds(T.CODE, T.NAME, T.SPEC);
		EMCrt ext = new MyCrt(TB, vflds, searchVflds);
		ext.getVfldsForm().del(T.IN_FLAG, T.OUT_FLAG, T.BAR_CODE, T.SHORTKEY, T.ENABLED);
		ext.getVfldsForm().get(T.CODE).setNullTrue();
		VFlds vl = ext.getVfldsList();
		vl.del(T.IN_FLAG, T.OUT_FLAG);
		vl.get(T.CODE).setWidthList(100);
		vl.get(T.KIND).setWidthList(130);
		vl.moveAfter(T.SPEC, T.CUST5);
		vl.setExpand("true", T.CODE, T.NAME);
		vl.setExpandAndHidden("true", T.SHORTKEY, T.WEIGHT_RATE, T.VALUME_RATE, T.DESCRIP, T.BAR_CODE, T.ZERO_OUT_FLAG,
		    T.BATCH_TYPE, T.ECONOMIC_QTY, T.PUR_LEAD_DAYS);
		((EMList) ext.newList()).setExtendRow();
		//((EMFormTwoRow) ext.newForm()).setWidthLabel(110);
		ext.newExts().init();
		//选择器
		EMCrtTrigger trigger = new EMCrtTrigger(TB, vl, T.CODE, new VFlds(T.CODE, T.NAME, T.SPEC));
		VFlds vlTrig = trigger.getVfldsList();
		vlTrig.get(T.CODE).setWidthList(80);
		vlTrig.get(T.KIND).setWidthList(80);
		vlTrig.get(T.CODE_OLD).setWidthList(80);
		vlTrig.get(T.NAME).setWidthList(80);
		vlTrig.get(T.UOM).setWidthList(50);
		vlTrig.get(T.UOM).setName("单位");
		vlTrig.get(T.CUST1).setWidthList(80);
		vlTrig.get(T.CUST2).setWidthList(80);
		vlTrig.get(T.CUST3).setWidthList(80);
		vlTrig.get(T.CUST4).setWidthList(80);
		vlTrig.get(T.CUST5).setWidthList(80);
		vlTrig.get(T.SPEC).setWidthList(160);
		((EMListTrigger) trigger.newList()).setExtendRow();
		((EMListTrigger) trigger.newList()).setTdCount(4);
		trigger.newExts().init().crtFiles();
		return ext;
	}

	class MyCrt extends EMCrtSimpleTwo<MyCrt> {

		public MyCrt(Tb tb, VFlds vflds, VFlds searchVflds) {
			super(tb, vflds, searchVflds);
		}

		public ExtFile newForm() {
			return null;
		}

	}

}
