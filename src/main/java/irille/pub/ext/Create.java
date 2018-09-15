package irille.pub.ext;

import irille.core.lg.LgLogin;
import irille.core.lg.LgTran;
import irille.core.sys.SysCell;
import irille.core.sys.SysCom;
import irille.core.sys.SysCtype;
import irille.core.sys.SysCtypeCode;
import irille.core.sys.SysCustom;
import irille.core.sys.SysDept;
import irille.core.sys.SysEm;
import irille.core.sys.SysOrg;
import irille.core.sys.SysPerson;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysSeq;
import irille.core.sys.SysSupplier;
import irille.core.sys.SysSupplierOrg;
import irille.core.sys.SysTemplat;
import irille.core.sys.SysUser;
import irille.gl.gl.GlEntryDefLine;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlJournalLine;
import irille.gl.gl.GlSubject;
import irille.gl.gl.GlSubjectMap;
import irille.gl.gs.GsDemand;
import irille.gl.gs.GsGain;
import irille.gl.gs.GsGainLine;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsGoodsCmb;
import irille.gl.gs.GsGoodsKind;
import irille.gl.gs.GsLocation;
import irille.gl.gs.GsLoss;
import irille.gl.gs.GsLossLine;
import irille.gl.gs.GsRequest;
import irille.gl.gs.GsRequestLine;
import irille.gl.gs.GsStock;
import irille.gl.gs.GsStockLine;
import irille.gl.gs.GsStockStimate;
import irille.gl.gs.GsUom;
import irille.gl.gs.GsUomType;
import irille.gl.gs.GsWarehouse;
import irille.pss.lgt.LgtShipMode;
import irille.pss.pur.PurProt;
import irille.pss.pur.PurProtApply;
import irille.pss.pur.PurProtGoods;
import irille.pss.pur.PurProtGoodsApply;
import irille.pss.pur.PurProtGoodsApplyLine;
import irille.pss.sal.SalOrder;
import irille.pss.sal.SalOrderLine;
import irille.pss.sal.SalSale;
import irille.pss.sal.SalSaleLine;
import irille.pub.svr.StartInitServlet;
import irille.pub.tb.Fld;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

/**
 * EXT-JS页面产生 各模块的页面都集中在这里
 */
public class Create {
	public static final String LIST = "list";
	public static final String INS = "ins";
	public static final String UPD = "upd";

	/**
	 * 最基础的菜单功能
	 * 
	 * @param tb
	 *            表模型的TB
	 * @param del
	 *            JS已存在是否允许覆盖
	 * @param flds
	 *            列表界面上的查询字段
	 */
	static void extSimple(Tb tb, boolean del, IEnumFld... eflds) {
		ExtModel.crtJs(tb, del);
		ExtStore.crtJs(tb, del);
		ExtList.crtJs(tb, del, enumToFld(eflds));
		if (tb.checkAct(INS) == null && tb.checkAct(UPD) == null)
			return;
		ExtWin.crtJs(tb, del);
		ExtForm.crtJs(tb, del);
	}

	/**
	 * 最基础的菜单功能--FORM表单为两列情况 以解决字段过多的排版问题
	 */
	static void extSimpleTwo(Tb tb, boolean del, IEnumFld... eflds) {
		ExtModel.crtJs(tb, del);
		ExtStore.crtJs(tb, del);
		ExtList.crtJs(tb, del, enumToFld(eflds));
		if (tb.checkAct(INS) == null && tb.checkAct(UPD) == null)
			return;
		ExtWinTwoRow.crtJs(tb, del);
		ExtFormTwoRow.crtJs(tb, del);
	}

	/**
	 * 主界面为复合界面，多个从表 但FORM操作是普通的单表方式
	 */
	static void extCompSimple(Tb tb, boolean del, Fld[] outFlds,
			Fld[] mainFlds, Fld... flds) {
		ExtModel.crtJs(tb, del);
		ExtStore.crtJs(tb, del);
		ExtZipList.crtJs(tb, del, outFlds, flds, mainFlds);
		ExtZipListMain.crtJs(tb, del);
		for (Fld out : outFlds) {
			ExtZipListLine.crtJs(tb, out, del);
			ExtModel.crtJs((Tb) out.getTb(), del);
			ExtStore.crtJs((Tb) out.getTb(), del);
		}
		if (tb.checkAct(INS) == null && tb.checkAct(UPD) == null)
			return;
		ExtWin.crtJs(tb, del);
		ExtForm.crtJs(tb, del);
	}

	static void extCompSimple(Tb tb, boolean del, IEnumFld out,
			IEnumFld[] mainFlds, IEnumFld... flds) {
		extCompSimple(tb, del, new Fld[] { out.getFld() }, enumToFld(mainFlds),
				enumToFld(flds));
	}

	/**
	 * 产生外键选择器
	 */
	static void extTrigger(Tb tb, boolean del, IEnumFld vfld, IEnumFld... flds) {
		ExtModel.crtJs(tb, del);
		ExtStore.crtJs(tb, del);
		ExtWinTrigger.crtJs(tb, del);
		ExtListTrigger.crtJs(tb, del, vfld.getFld(), enumToFld(flds));
	}

	/**
	 * 主界面为复合界面，多个从表 新增、修改同时操作从表记录
	 */
	static void extComp(Tb tb, boolean del, IEnumFld out, IEnumFld[] mainFlds,
			IEnumFld... flds) {
		Fld outfld = out.getFld();
		ExtModel.crtJs(tb, del);
		ExtStore.crtJs(tb, del);
		ExtZipList.crtJs(tb, del, new Fld[] { outfld }, enumToFld(flds),
				enumToFld(mainFlds));
		ExtZipListMain.crtJs(tb, del);
		ExtZipListLine.crtJs(tb, outfld, del);
		ExtModel.crtJs((Tb) outfld.getTb(), del);
		ExtStore.crtJs((Tb) outfld.getTb(), del);
		if (tb.checkAct(INS) == null && tb.checkAct(UPD) == null)
			return;
		ExtZipWin.crtJs(tb, outfld, del);
		ExtZipListForm.crtJs(tb, outfld, del);
		ExtForm.crtJs(tb, del);
	}

	/**
	 * 产生编辑窗口及表格页
	 */
	static void extEditor(Tb tb, boolean del, IEnumFld[] mflds,
			IEnumFld... outflds) {
		FldOutKey[] outkeys = new FldOutKey[outflds.length];
		int i = 0;
		for (IEnumFld line : outflds) {
			outkeys[i] = (FldOutKey) line.getFld();
			i++;
		}
		ExtWinEdit.crtJs(tb, del, enumToFld(mflds), outkeys);
		for (FldOutKey line : outkeys) {
			ExtListEdit.crtJs(line, del);
			ExtModel.crtJs((Tb) line.getTb(), del);
			ExtStore.crtJs((Tb) line.getTb(), del);
		}
	}

	// EnumFld转Fld
	private static Fld[] enumToFld(IEnumFld... eflds) {
		if (eflds == null)
			return null;
		Fld[] flds = new Fld[eflds.length];
		int i = 0;
		for (IEnumFld line : eflds) {
			flds[i] = line.getFld();
			i++;
		}
		return flds;
	}

	/**
	 * ====================基础/参数模块页面产生
	 */
	static void crtSys() {
		extSimpleTwo(SysOrg.TB, false, SysOrg.T.CODE, SysOrg.T.NAME);
		extSimple(SysCom.TB, false, SysCom.T.NAME);
		extSimple(SysDept.TB, false, SysDept.T.CODE, SysDept.T.NAME);
		extSimple(SysCustom.TB, false, SysCustom.T.NAME);
		extSimple(SysPerson.TB, false, SysPerson.T.NAME);
		extSimpleTwo(SysEm.TB, false, SysEm.T.CODE, SysEm.T.NAME, SysEm.T.ORG);
		extSimple(SysUser.TB, false, SysUser.T.LOGIN_NAME, SysUser.T.NAME,
				SysUser.T.ORG);
		extSimple(SysTemplat.TB, false, SysTemplat.T.CODE, SysTemplat.T.NAME);
		extSimple(SysCell.TB, false, SysCell.T.NAME, SysCell.T.YEAR);
		extSimple(SysSeq.TB, false);
		ExtModel.crtJs(SysCtype.TB, false);
		ExtModel.crtJs(SysCtypeCode.TB, false);
		ExtStore.crtJs(SysCtype.TB, false);
		ExtStore.crtJs(SysCtypeCode.TB, false);

		extTrigger(SysUser.TB, false, SysUser.T.NAME, SysUser.T.LOGIN_NAME,
				SysUser.T.NAME, SysUser.T.ORG, SysUser.T.DEPT);
		extTrigger(SysPerson.TB, false, SysPerson.T.NAME, SysPerson.T.NAME,
				SysPerson.T.HO_TEL);
		extTrigger(SysPersonLink.TB, false, SysPersonLink.T.NAME, SysPersonLink.T.NAME);
		
		extSimpleTwo(SysSupplier.TB, false, SysSupplier.T.CODE,
				SysSupplier.T.NAME);
		extSimple(SysSupplierOrg.TB, false, SysSupplierOrg.T.SUPPLIER,
				SysSupplierOrg.T.ORG);
		extSimple(SysPersonLink.TB, false, SysPersonLink.T.NAME);

		extTrigger(SysSupplier.TB, false, SysSupplier.T.NAME,
				SysSupplier.T.CODE);
	}

	/**
	 * ====================系统日志页面产生
	 */
	static void crtLg() {
		extSimple(LgLogin.TB, false, LgLogin.T.USER_SYS, LgLogin.T.LOGIN_TIME);
		extSimple(LgTran.TB, false, LgTran.T.B_TIME, LgTran.T.REM);
	}

	static void crtTest() {
		// extSimple(SysA.TB, false, SysA.FLD_CODE, SysA.FLD_OPT_SYS,
		// SysA.FLD_OPT_CUST);
		// extSimple(SysA1.TB, false, SysA1.FLD_AMT, SysA1.FLD_DATE1,
		// SysA1.FLD_KEY1);
		// extSimple(SysA2.TB, false, SysA2.FLD_CODE);
		// extEditor(SysA2.TB, false, new Fld[] { SysA2.FLD_CODE, SysA2.FLD_DES
		// },
		// new FldOutKey[] { (FldOutKey) SysA1.FLD_KEY1 });
		// extSimple(SysB.TB, false, SysB.FLD_AMT, SysB.FLD_RATE,
		// SysB.FLD_KEY1);
		// extSimple(SysB2.TB, false, SysB2.FLD_NAME, SysB2.FLD_KEY1,
		// SysB2.FLD_KEY2);
		// extSimple(SysB3.TB, false, SysB3.FLD_NAME);
		// extEditor(SysB3.TB, false, new Fld[] { SysB3.FLD_NAME,
		// SysB3.FLD_REMARK
		// }, new FldOutKey[] { SysB2.FLD_PARENT });
		//
		// extComp(SysB4.TB, false, SysB2.FLD_PARENT2, new Fld[] {
		// SysB4.FLD_PKEY,
		// SysB4.FLD_NAME, SysB4.FLD_REMARK,
		// SysB4.FLD_NAME }, SysB4.FLD_PKEY, SysB4.FLD_NAME);
		//
		// extTrigger(SysA2.TB, false, SysA2.FLD_CODE, SysA2.FLD_CODE,
		// SysA2.FLD_DES);
		// extTrigger(SysA.TB, false, SysA.FLD_CODE, SysA.FLD_CODE,
		// SysA.FLD_OPT_SYS);
		// extTrigger(SysB2.TB, false, SysB2.FLD_NAME, SysB2.FLD_NAME,
		// SysB2.FLD_AMT);
	}

	/**
	 * ====================财务模块页面产生
	 */
	static void crtGl() {
		extSimpleTwo(GlSubject.TB, false, GlSubject.T.CODE, GlSubject.T.NAME,
				GlSubject.T.ENABLED);
		extSimple(GlSubjectMap.TB, false, GlSubjectMap.T.NAME,
				GlSubjectMap.T.ALIAS_SOURCE, GlSubjectMap.T.ALIAS_TARGET);
		// extComp(GlEntryDef.TB, false, GlEntryDefLine.T.PKEY, new IEnumFld[] {
		// GlEntryDef.T.TEMPLAT,
		// GlEntryDef.T.TABLE_ID, GlEntryDef.T.CODE, GlEntryDef.T.NAME },
		// GlEntryDef.T.CODE,
		// GlEntryDef.T.NAME);
		extCompSimple(GlJournal.TB, false, GlJournalLine.T.MAIN_PKEY,
				new IEnumFld[] { GlJournal.T.CODE, GlJournal.T.NAME,
						GlJournal.T.CELL, GlJournal.T.SUBJECT, GlJournal.T.DIRECT,
						GlJournal.T.BALANCE, GlJournal.T.BALANCE_USE },
				GlJournal.T.CODE, GlJournal.T.NAME, GlJournal.T.SUBJECT);
	}

	/**
	 * ====================分摊模块页面产生
	 */
	static void crtSh() {

	}

	/**
	 * ====================货物模块页面产生
	 */
	static void crtGs() {
		extComp(GsUomType.TB, false, GsUom.T.UOM_TYPE, new IEnumFld[] {
				GsUomType.T.NAME, GsUomType.T.SHORTKEY, GsUomType.T.ENABLED,
				GsUomType.T.REM }, GsUomType.T.NAME, GsUomType.T.SHORTKEY);
		extSimple(GsGoodsKind.TB, false, GsGoodsKind.T.CODE,
				GsGoodsKind.T.NAME, GsGoodsKind.T.SHORTKEY);
		extSimpleTwo(GsGoods.TB, false, GsGoods.T.CODE, GsGoods.T.NAME,
				GsGoods.T.ENABLED, GsGoods.T.KIND);
		extSimple(GsGoodsCmb.TB, false, GsGoodsCmb.T.GOODS, GsGoodsCmb.T.SORT);
		extSimple(GsWarehouse.TB, false, GsWarehouse.T.ORG,
				GsWarehouse.T.ENABLED);
		extSimple(GsLocation.TB, false, GsLocation.T.WAREHOUSE,
				GsLocation.T.NAME, GsLocation.T.ENABLED);
		extCompSimple(GsStock.TB, false, GsStockLine.T.STOCK, new IEnumFld[] {
				GsStock.T.WAREHOUSE, GsStock.T.GOODS, GsStock.T.LOCATION,
				GsStock.T.ENROUTE_QTY, GsStock.T.LOCKED_QTY },
				GsStock.T.WAREHOUSE, GsStock.T.GOODS, GsStock.T.LOCATION);
		extComp(GsGain.TB, false, GsGainLine.T.PKEY, new IEnumFld[] {
				GsGain.T.CODE, GsGain.T.ORIG_FORM_NUM, GsGain.T.WAREHOUSE,
				GsGain.T.STATUS }, GsGain.T.CODE, GsGain.T.WAREHOUSE,
				GsGain.T.STATUS);
		extComp(GsLoss.TB, false, GsLossLine.T.PKEY, new IEnumFld[] {
				GsLoss.T.CODE, GsLoss.T.WAREHOUSE, GsLoss.T.STATUS,
				GsLoss.T.ORG, GsLoss.T.DEPT, GsLoss.T.ORIG_FORM,
				GsLoss.T.APPR_BY, GsLoss.T.APPR_TIME, GsLoss.T.CREATED_BY,
				GsLoss.T.CREATED_TIME, GsLoss.T.REM }, GsLoss.T.CODE,
				GsLoss.T.WAREHOUSE, GsLoss.T.STATUS);
		extComp(GsRequest.TB, false, GsRequestLine.T.PKEY, new IEnumFld[] {
				GsRequest.T.CODE, GsRequest.T.STATUS, GsRequest.T.ORG, GsRequest.T.DEPT
				}, GsRequest.T.CODE, GsRequest.T.STATUS);
		extSimple(GsStockStimate.TB, false, GsStockStimate.T.WAREHOUSE,
				GsStockStimate.T.GOODS);
		extSimple(GsDemand.TB, false, GsDemand.T.WAREHOUSE, GsDemand.T.GOODS);

		extTrigger(GsUom.TB, false, GsUom.T.NAME, GsUom.T.SHORTKEY);
		extTrigger(GsGoods.TB, false, GsGoods.T.CODE, GsGoods.T.NAME,
				GsGoods.T.SPEC);
	}

	/**
	 * ====================合同模块页面产生
	 */
	static void crtCont() {
		// extComp(ContContract.TB, false, (FldOutKey)
		// ContContractLine.FLD_CONTRACT, new Fld[] { ContContract.FLD_CODE,
		// ContContract.FLD_CUST_NAME, ContContract.FLD_STATUS,
		// ContContract.FLD_AMT, ContContract.FLD_AMT_REAL,
		// ContContract.FLD_AMT_REC, ContContract.FLD_ORG,
		// ContContract.FLD_DEPT_SALE, ContContract.FLD_EM_SALE },
		// ContContract.FLD_CODE, ContContract.FLD_CUST_NAME,
		// ContContract.FLD_DEPT_SALE);
		// //表外应收
		// extSimple(ContAccountBook.TB, false, ContAccountBook.FLD_ORG,
		// ContAccountBook.FLD_STATUS,
		// ContAccountBook.FLD_OBJECT_PKEY);
		// //销售单
		// extComp(ContSale.TB, false, (FldOutKey) ContSaleLine.FLD_SALE, new
		// Fld[]
		// { ContSale.FLD_FORM_NUMBER,
		// ContSale.FLD_CUST_NAME, ContSale.FLD_STATUS, ContSale.FLD_AMT,
		// ContSale.FLD_FORM_NUMBERS },
		// ContSale.FLD_FORM_NUMBER, ContSale.FLD_CUST_NAME,
		// ContSale.FLD_STATUS);
		// //发票汇总
		// extComp(ContInvoice.TB, false, (FldOutKey)
		// ContInvoiceLine.FLD_INVOICE,
		// new Fld[] { ContInvoice.FLD_FORM_NUMBER,
		// ContInvoice.FLD_AMT, ContInvoice.FLD_STATUS, ContInvoice.FLD_TYPE,
		// ContInvoice.FLD_DOC_NUMBER },
		// ContInvoice.FLD_FORM_NUMBER, ContInvoice.FLD_DOC_NUMBER,
		// ContInvoice.FLD_STATUS);
		//
		// extTrigger(SysObjectRel.TB, false, SysObjectRel.FLD_DESCRIPTION,
		// SysObjectRel.FLD_DESCRIPTION);
		// extTrigger(ContContract.TB, false, ContContract.FLD_CODE,
		// ContContract.FLD_CODE, ContContract.FLD_CUST_NAME);
	}

	static void crtSal() {
		extComp(SalOrder.TB, false, SalOrderLine.T.PKEY, new IEnumFld[] {
				SalOrder.T.CODE, SalOrder.T.ORG, SalOrder.T.DEPT,
				SalOrder.T.STATUS }, SalOrder.T.CODE, SalOrder.T.STATUS);
		extComp(SalSale.TB, false, SalSaleLine.T.PKEY, new IEnumFld[] {
				SalSale.T.CODE, SalSale.T.ORG, SalSale.T.DEPT,
				SalSale.T.WAREHOUSE, SalSale.T.STATUS }, SalSale.T.CODE,
				SalSale.T.STATUS);

		extTrigger(SalOrder.TB, false, SalOrder.T.CODE, SalOrder.T.CODE,
				SalOrder.T.CUST_NAME);
	}

	static void crtLgt() {
		extSimple(LgtShipMode.TB, false, LgtShipMode.T.CODE, LgtShipMode.T.NAME);
	}

	static void crtPur() {
		extSimpleTwo(PurProt.TB, false, PurProt.T.SUPPLIER, PurProt.T.NAME);
		extSimpleTwo(PurProtApply.TB, false, PurProtApply.T.SUPPLIER,
				PurProtApply.T.STATUS);
		extSimpleTwo(PurProtGoods.TB, false, PurProtGoods.T.SUPPLIER,
				PurProtGoods.T.NAME, PurProtGoods.T.GOODS,
				PurProtGoods.T.VENDOR_GOODS_NAME, PurProtGoods.T.VENDOR_NUM,
				PurProtGoods.T.VENDOR_SPEC);
		extComp(PurProtGoodsApply.TB,
				false,
				PurProtGoodsApplyLine.T.PKEY,
				new IEnumFld[] { PurProtGoodsApply.T.CODE,
						PurProtGoodsApply.T.SUPPLIER, PurProtGoodsApply.T.NAME },
				PurProtGoodsApply.T.CODE, PurProtGoodsApply.T.SUPPLIER,
				PurProtGoodsApply.T.NAME, PurProtGoodsApply.T.STATUS);
		
		extSimpleTwo(PurProtGoodsApplyLine.TB, false);
	}

	public static void main(String[] args) {
		// 各种循环引用
		GlEntryDefLine.TB.getName();
		SalOrderLine.TB.getName();
		SalSaleLine.TB.getName();
		StartInitServlet.initBeanLoad();
		GsRequestLine.TB.getName();
		// =========
		ExtOpt.crtOptAll(false); // 系统选项及用户选项的产生
		crtSys();
		crtGs();
		crtSal();
		crtPur();
		crtLgt();
	}

}
