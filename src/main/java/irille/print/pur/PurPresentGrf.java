package irille.print.pur;

import irille.core.sys.SysCom;
import irille.core.sys.SysOrg;
import irille.core.sys.SysPersonLink;
import irille.gl.gs.GsGoods;
import irille.pss.pur.PurPresent;
import irille.pss.pur.PurPresentLine;
import irille.pub.print.CrtJsp;
import irille.pub.print.GrfForm;
import irille.pub.print.Sheet;
import irille.pub.print.SheetPrint;
import irille.pub.print.SheetVarsInf;
import irille.pub.print.Grfs.GrfItem;
import irille.pub.print.Grfs.GrfItemsSection;
import irille.pub.print.Grfs.GrfSectionItem;
import irille.pub.print.Sheets.SheetGap;
import irille.pub.tb.Tb;
import irille.pub.view.VFlds;
import irille.pub.view.VFld.OAlign;

public class PurPresentGrf extends GrfForm implements SheetVarsInf {
	static {
		PurPresent.TB.getCode(); PurPresentLine.TB.getCode(); SysOrg.TB.getCode(); SysCom.TB.getCode(); SysPersonLink.TB.getCode(); 
	}
	private static final PurPresent.T T = PurPresent.T.PKEY;
	private static final PurPresentLine.T L = PurPresentLine.T.PKEY;
	private static final SysOrg.T ORG = SysOrg.T.PKEY;
	private static final SysCom.T COM = SysCom.T.PKEY;
	private static final GsGoods.T GOODS = GsGoods.T.PKEY;
	private static final SysPersonLink.T LINK = SysPersonLink.T.PKEY;
	private static final Tb[] tbs = {SysPersonLink.TB};
	private static final VFlds[] personLinkName = {new VFlds(LINK.NAME)};
	private static final VFlds[] personLinkPeMobile = {new VFlds(LINK.PE_MOBILE)};
	
	public static void main(String[] args) {
		String mainParams[] = toCodes(new VFlds(T.ORG, T.CODE, T.SUPPLIER, T.SUPNAME, T.CREATED_TIME, T.REM),
				new VFlds(T.ORG, ORG.CODE),
				new VFlds(T.ORG, ORG.COM, COM.NAME),
				new VFlds(T.ORG, ORG.COM, COM.ADDR),
				new VFlds(T.ORG, ORG.COM, COM.TEL1),
				new VFlds(T.ORG, ORG.COM, COM.FAX)
				);
		mainParams = toCodes(mainParams, toLinkCode(new VFlds(T.SUPPLIER), tbs, personLinkName), toLinkCode(new VFlds(T.SUPPLIER), tbs, personLinkPeMobile));
		String linesParams[] = toCodes(new VFlds(L.PKEY, L.QTY, L.UOM, L.PRICE, L.AMT),
				new VFlds(L.GOODS, GOODS.CODE),
				new VFlds(L.GOODS, GOODS.NAME),
				new VFlds(L.GOODS, GOODS.SPEC)
				);
		new CrtJsp(mainParams, linesParams, PurPresent.class, PurPresentLine.class).run();
	}

	public PurPresentGrf() {
		super(PurPresent.class, "采 购 收 货 单", 7, 
				new VFlds(T.ORG, T.CODE, T.SUPPLIER, T.SUPNAME, T.CREATED_TIME, T.REM, COM.TEL1, COM.ADDR, COM.FAX,  LINK.NAME, LINK.PE_MOBILE, LINK.OF_FAX));
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.ORG, ORG.CODE)));//组合的外键
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.ORG, ORG.COM, COM.ADDR)));//组合的外键
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.ORG, ORG.COM, COM.TEL1)));//组合的外键
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.ORG, ORG.COM, COM.FAX)));//组合的外键
		getItemsParameter().AddItem_Name(toLinkCode(new VFlds(T.SUPPLIER), tbs, personLinkName));//组合的外键
		getItemsParameter().AddItem_Name(toLinkCode(new VFlds(T.SUPPLIER), tbs, personLinkPeMobile));//组合的外键
		getVFld(T.CODE).setName("单号");
		getVFld(COM.TEL1).setName("电话");
		getVFld(T.CREATED_TIME).setName("开单时间");
		getVFld(LINK.NAME).setName("联系人");
		getVFld(LINK.PE_MOBILE).setName("手机");
		getVFld(LINK.OF_FAX).setName("传真");
		setDetailGrid(new MyDetailGrid(this).init()); // 设置明细行
	}
	
	// ph:PageHeader, pf:PageFooter, rh:ReportHeader, rf:ReportFooter
		@Override
		public void initItemsSection(GrfItemsSection is, GrfSectionItem ph,
				GrfSectionItem pf, GrfSectionItem rh, GrfSectionItem rf) {
			super.initItemsSection(is, ph, pf, rh, rf);
			rh.addAttr("RepeatOnPage", new GrfExp("T"));//每页重复
			rf.addAttr("RepeatOnPage", new GrfExp("T"));//每页重复
			rf.addAttr("PrintAtBottom", new GrfExp("T"));//打印在页底
			getVFlds().setAlignToMiddleCenter(T.ORG);
			
			rh.AddValue(T.ORG, 1, 1).setTextAlignToMiddleCenter().add(FONT_14_BOLD);
//			rh.AddItem("StaticBox", "orderName", "采 购 收 货 单", 2,1).setTextAlignToMiddleCenter().add(FONT_REPORT_HEAD);
			Sheet line2 = rh.getSheet().newInnerSheet(2, 1, new double[] {0.65, 0.65}, 5, 8.3, 1.3, -1);
			rh.AddItem("StaticBox", "orderName", "采 购 收 货 单", line2.cellLocationSizeAbs(1, 2, 2, 1)).setTextAlignToMiddleCenter().add(FONT_REPORT_HEAD);
			rh.AddName(COM.TEL1, line2.cellLocationSizeAbs(2, 3));
			rh.AddValue(COM.TEL1, line2.cellLocationSizeAbs(2, 4), new VFlds(T.ORG, ORG.COM, COM.TEL1));
			Sheet line4 = rh.getSheet().newInnerSheet(4, 1, new double[] {0.65}, 1, 21, 1.3, -1);
			rh.AddName(COM.ADDR, line4.cellLocationSizeAbs(1, 1)).setTextAlignToMiddleRight(); 
			rh.AddValue(COM.ADDR, line4.cellLocationSizeAbs(1, 2), new VFlds(T.ORG, ORG.COM, COM.ADDR));
			rh.AddName(COM.FAX, line4.cellLocationSizeAbs(1, 3));
			rh.AddValue(COM.FAX, line4.cellLocationSizeAbs(1, 4), new VFlds(T.ORG, ORG.COM, COM.FAX));
			Sheet line5 = rh.getSheet().newInnerSheet(5, 1, new double[] {0.65}, 1, 6.3, 2, 4.7, 1.6, 1.6, -1);
			rh.AddName(T.CODE, line5.cellLocationSizeAbs(1, 1));
			rh.AddValue(T.CODE, line5.cellLocationSizeAbs(1, 2));
			rh.AddName(T.CREATED_TIME, line5.cellLocationSizeAbs(1, 3));
			rh.AddValue(T.CREATED_TIME, line5.cellLocationSizeAbs(1, 4));
			rh.AddItem("MemoBox",  "pageCount", "共 " + GrfItem.pageCount() + "页", line5.cellLocationSizeAbs(1, 5)).setTextAlignToMiddleCenter();
			rh.AddItem("MemoBox",  "pageNumber", "第 " + GrfItem.pageNumber() + " 页", line5.cellLocationSizeAbs(1, 6)).setTextAlignToMiddleCenter();
			Sheet line6 = rh.getSheet().newInnerSheet(6, 1, new double[] {0.65}, 2, 7, 2.6, 6.4);
			rh.AddName(T.SUPNAME, line6.cellLocationSizeAbs(1, 1)).addAttr("BorderStyles", new GrfExp("[DrawLeft|DrawTop|DrawBottom]"));
			rh.AddValue(T.SUPNAME, line6.cellLocationSizeAbs(1, 2)).addAttr("BorderStyles", new GrfExp("[DrawTop|DrawBottom]"));
			rh.AddNameMulti(line6.cellLocationSizeAbs(1, 3), LINK.NAME, LINK.PE_MOBILE).addAttr("BorderStyles", new GrfExp("[DrawTop|DrawBottom]"));
			rh.AddValueMulti(LINK.NAME, line6.cellLocationSizeAbs(1, 4), toLinkCode(new VFlds(T.SUPPLIER), tbs, personLinkName), toLinkCode(new VFlds(T.SUPPLIER), tbs, personLinkPeMobile)).addAttr("BorderStyles", new GrfExp("[DrawTop|DrawRight|DrawBottom]"));
			rh.AddItem("StaticBox", "orderlist", "受赠清单:", 7, 1).add(FONT_14_BOLD).addAttr("BorderStyles", new GrfExp("[DrawBottom]"));
			
			Sheet fline1 = rf.getSheet().newInnerSheet(1, 1, new double[] {0.65}, 1.2, -1);
			rf.AddName(T.REM, fline1.cellLocationSizeAbs(1, 1)).addAttr("BorderStyles", new GrfExp("[DrawLeft|DrawTop|DrawBottom]"));
			rf.AddValue(T.REM, fline1.cellLocationSizeAbs(1, 2)).addAttr("BorderStyles", new GrfExp("[DrawTop|DrawRight|DrawBottom]"));
			
			
		}
		
		
		@Override
		public void initSheetPrint(SheetPrint sp) {
			double[] header = new double[getReportHeaderLines()];
			double[] footer = new double[12];
			for(int i=0;i<header.length;i++)
				header[i]=_mainLineHeight;
			for(int i = 0; i < footer.length; i++)
				footer[i] = 0.65;
			sp.setPageSize(SheetPrint.A4_VERTICAL);
			sp.setOriention(false);
			sp.setPageGap(new SheetGap(1, 1.5));
			sp.setPageHeader(new double[] { 0.5 }, 2, -1, 2);
			sp.setPageFooter(new double[] { 0.2 }, 2, -1, 2);
			sp.setReportHeader(header, 18);
			sp.setReportFooter(footer, 18);
			sp.setGroupHeader(new double[] {0.1}, 18);
			sp.setGroupFooter(new double[] {0.1}, 18);
		}

	private static class MyDetailGrid extends GrfFormLine {
		public MyDetailGrid(GrfForm parent) {
			super(
					parent, //
					new VFlds(L.PKEY, GOODS.CODE, GOODS.NAME, GOODS.SPEC, L.UOM, L.QTY),
					new double[] { 1, 3, 3, 7.7, 1, 2.3},
					null,
					new VFlds(L.GOODS, GOODS.CODE),
					new VFlds(L.GOODS, GOODS.NAME),
					new VFlds(L.GOODS, GOODS.SPEC),
					null,null
					);
			getVFlds().setAlignToMiddleRight(L.QTY, L.UOM);
			getVFlds().setAlignTitle(OAlign.MIDDLE_RIGHT, L.QTY, L.UOM);
			getVFld(L.PKEY).setListName("序号");
			getVFld(L.UOM).setListName("单位");
			showTitileCellUnderLine(true);//给明细标题下面加下划线
			getColumnTitle().addAttr("RepeatStyle", new GrfExp("OnPage"));//标题部分每页重复
		}
	}
}
