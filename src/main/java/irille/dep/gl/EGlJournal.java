/**
 * 
 */
package irille.dep.gl;

import irille.gl.gl.GlDaybookLine;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlJournalLine;
import irille.pub.bean.CmbGoods;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtCompSimple;
import irille.pub.html.EMCrtTrigger;
import irille.pub.html.EMList;
import irille.pub.html.EMWin;
import irille.pub.html.EMZipListMain;
import irille.pub.html.ExtFile;
import irille.pub.tb.Tb;
import irille.pub.view.VFlds;

public class EGlJournal extends GlJournal {
	public static void main(String[] args) {
		new EGlJournal().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		CmbGoods.TB.getCode();
		GlJournalLine.TB.getCode();
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.NAME, T.CELL, T.SUBJECT, T.DIRECT, T.BALANCE, T.ACC_TYPE,
		    T.STATE, T.ORG) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.NAME, T.CELL) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		MyCrt ext = new MyCrt(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(GlJournalLine.T.MAIN_PKEY) });
		VFlds vl = ext.getVfldsList().del(T.PKEY,T.OBJ_PKEY,T.EXT_TABLE); // 列表中不显示【PKEY】列
		vl.moveAfter(T.BALANCE, T.NAME);
		vl.setWidths(130,250,100,150,
				100,100,100,60,100,100,100,100,100,100,100,100,100,100);//由于list中某些字段显示不下，所以自定义了字段的宽度
		((MyCrt)ext).getNowOutKeyLineVflds();
		vl.setExpandAndHidden("true", T.REM, T.INTEREST_ACCRUAL, T.FROST_FLAG, T.ACC_TYPE, T.IN_FLAG, T.BALANCE_USE, T.ACC_JOURNAL_TYPE, T.TALLY_FLAG);
		((EMZipListMain)ext.newZipListMain()).setExtendRow();
		ext.newExts().init();
		EMCrt extTrig = new EMCrtTrigger(TB, vl, T.NAME, new VFlds(T.CODE, T.NAME));
		extTrig.newExts().init().crtFiles();
		return ext;
	}
	
	class MyWin extends EMWin {
		
		public MyWin(Tb tb) {
			super(tb);
		}
		
		@Override
		public void initFuns() {
			AddFun("setActiveRecord",EGlJournal.class).addFunParasExp("record");
			initFunsAddOtherFuns();
			initFunsAddActs();
		}
		/** Begin setActiveRecord ********
		this.form.activeRecord = record;
		if (record || this.form.activeRecord) {
			this.form.getForm().loadRecord(record);
			if(record.get('bean.accType')!='1') {
				var v = record.get('bean.objPkey');
				if(record.get('bean.accType')!='2') {
					args = v.split(bean_split);
					v = String((Number(args[0])/100000).toFixed(0))+bean_split+args[1];						
				}
				this.form.down('[itemId='+record.get('bean.accType')+']').setValue(v);	
			}
		} else {
			this.form.getForm().reset();
		}
		*** End setActiveRecord *********/
	}

	class MyCrt extends EMCrtCompSimple {

		public MyCrt(Tb tb, VFlds[] vflds,VFlds[] mainvflds, VFlds[] searchVflds,
				 VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}
		
		public MyCrt setNowOutKeyLineVflds(VFlds... nowOutKeyLineVflds) {
			VFlds ne = VFlds.newVFlds(new VFlds[] {VFlds.newVFlds(nowOutKeyLineVflds),new VFlds(GlDaybookLine.T.DIRECT, GlDaybookLine.T.AMT, GlDaybookLine.T.SUMMARY, GlDaybookLine.T.DOC_NUM)});
			ne.get(GlDaybookLine.T.SUMMARY).setWidthList(190);
			ne.get(GlDaybookLine.T.DOC_NUM).setWidthList(150);
			super.setNowOutKeyLineVflds(ne);
			return this;
		}
		
		@Override
		public EMCrtCompSimple setNowOutKeyModelVFlds(VFlds... nowOutKeyModelVflds) {
			VFlds ne = VFlds.newVFlds(new VFlds[] {VFlds.newVFlds(nowOutKeyModelVflds),new VFlds(GlDaybookLine.T.DIRECT, GlDaybookLine.T.AMT, GlDaybookLine.T.SUMMARY, GlDaybookLine.T.DOC_NUM)});
		  return super.setNowOutKeyModelVFlds(ne);
		}
		
		@Override
		public ExtFile newWin() {
			return new MyWin(getTb());
		}
		
		@Override
		public ExtFile newForm() {
			return null;
		}
	}
}
