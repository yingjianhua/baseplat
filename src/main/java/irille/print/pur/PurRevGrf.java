package irille.print.pur;

import irille.gl.gs.GsGoods;
import irille.pss.pur.PurRev;
import irille.pss.pur.PurRevLine;
import irille.pub.print.CrtJsp;
import irille.pub.print.GrfForm;
import irille.pub.print.Sheet;
import irille.pub.print.SheetPrint;
import irille.pub.print.SheetVarsInf;
import irille.pub.print.Grfs.GrfItem;
import irille.pub.print.Grfs.GrfItemsSection;
import irille.pub.print.Grfs.GrfSectionItem;
import irille.pub.print.Sheets.SheetGap;
import irille.pub.view.VFlds;
import irille.pub.view.VFld.OAlign;

public class PurRevGrf extends GrfForm implements SheetVarsInf {
	static {
		PurRev.TB.getCode(); PurRevLine.TB.getCode();
	}
	private static final PurRev.T T = PurRev.T.PKEY;
	private static final PurRevLine.T L = PurRevLine.T.PKEY;
	private static final GsGoods.T GOODS = GsGoods.T.PKEY;
	
	public static void main(String[] args) {
		String mainParams[] = toCodes(new VFlds(T.ORG, T.CODE,T.SUPNAME, T.AMT, T.ORD, T.WAREHOUSE, T.CREATED_TIME, T.BUYER, T.REM));
		String linesParams[] = toCodes(new VFlds(L.PKEY, L.QTY, L.UOM, L.PRICE, L.AMT),
				new VFlds(L.GOODS, GOODS.CODE),
				new VFlds(L.GOODS, GOODS.NAME),
				new VFlds(L.GOODS, GOODS.SPEC)
				);
		new CrtJsp(mainParams, linesParams, PurRev.class, PurRevLine.class).run();
	}

	public PurRevGrf() {
		super(PurRev.class, "采 购 收 货 单", 8, 
				new VFlds(T.ORG, T.CODE, T.SUPNAME, T.AMT, T.WAREHOUSE,T.ORD, T.CREATED_TIME, T.BUYER, T.REM));
		getVFld(T.CODE).setName("单号");
		getVFld(T.CREATED_TIME).setName("开单时间");
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
			rh.AddItem("StaticBox", "orderName", "采 购 收 货 单", 2,1).setTextAlignToMiddleCenter().add(FONT_REPORT_HEAD);
			Sheet line4 = rh.getSheet().newInnerSheet(4, 1, new double[] {0.65}, 1, 6.3, 2, 4.7, 1.6, 1.6, -1);
			rh.AddName(T.CODE, line4.cellLocationSizeAbs(1, 1));
			rh.AddValue(T.CODE, line4.cellLocationSizeAbs(1, 2));
			rh.AddName(T.CREATED_TIME, line4.cellLocationSizeAbs(1, 3));
			rh.AddValue(T.CREATED_TIME, line4.cellLocationSizeAbs(1, 4));
			rh.AddItem("MemoBox",  "pageCount", "共 " + GrfItem.pageCount() + "页", line4.cellLocationSizeAbs(1, 5)).setTextAlignToMiddleCenter();
			rh.AddItem("MemoBox",  "pageNumber", "第 " + GrfItem.pageNumber() + " 页", line4.cellLocationSizeAbs(1, 6)).setTextAlignToMiddleCenter();
			Sheet line5to7 = rh.getSheet().newInnerSheet(5, 1, new double[] {0.65, 0.65, 0.65}, 2, 8, 1.6, 6.4);
			rh.AddName(T.SUPNAME, line5to7.cellLocationSizeAbs(1, 1)).addAttr("BorderStyles", new GrfExp("[DrawLeft|DrawTop]"));
			rh.AddValue(T.SUPNAME, line5to7.cellLocationSizeAbs(1, 2)).addAttr("BorderStyles", new GrfExp("[DrawTop]"));
			rh.AddName(T.BUYER, line5to7.cellLocationSizeAbs(1, 3)).addAttr("BorderStyles", new GrfExp("[DrawTop]"));
			rh.AddValue(T.BUYER, line5to7.cellLocationSizeAbs(1, 4)).addAttr("BorderStyles", new GrfExp("[DrawTop|DrawRight]"));
			rh.AddName(T.WAREHOUSE, line5to7.cellLocationSizeAbs(2, 1)).addAttr("BorderStyles", new GrfExp("[DrawLeft]"));
			rh.AddValue(T.WAREHOUSE, line5to7.cellLocationSizeAbs(2, 2));
			rh.AddName(T.AMT, line5to7.cellLocationSizeAbs(2, 3));
			rh.AddItem("MemoBox", T.AMT.getFld().getCode(), "[#{"+ T.AMT.getFld().getCode() + "}:$$#]", line5to7.cellLocationSizeAbs(2, 4),OAlign.MIDDLE_LEFT).addAttr("BorderStyles", new GrfExp("[DrawRight]"));
			rh.AddName(T.ORD, line5to7.cellLocationSizeAbs(3, 1)).addAttr("BorderStyles", new GrfExp("[DrawLeft|DrawBottom]"));
			rh.AddValue(T.ORD, line5to7.cellLocationSizeAbs(3, 2)).addAttr("BorderStyles", new GrfExp("[DrawBottom]"));
			rh.AddItem("StaticBox", "gldj","关联单据:",line5to7.cellLocationSizeAbs(3, 3)).addAttr("BorderStyles", new GrfExp("[DrawBottom]"));
			rh.AddItem("StaticBox", "ysyf","应收预付",line5to7.cellLocationSizeAbs(3, 4)).addAttr("BorderStyles", new GrfExp("[DrawRight|DrawBottom]"));
			
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
					new VFlds(L.PKEY, GOODS.CODE, GOODS.NAME, GOODS.SPEC, L.UOM, L.QTY, L.PRICE, L.AMT),
					new double[] { 1, 3, 3.9, 7.1, 1, 3.7, 3.5, 3.5 },
					null,
					new VFlds(L.GOODS, GOODS.CODE),
					new VFlds(L.GOODS, GOODS.NAME),
					new VFlds(L.GOODS, GOODS.SPEC),
					null,null,null,null
					);
			getVFlds().setAlignToMiddleRight(L.QTY, L.UOM, L.PRICE, L.AMT);
			getVFlds().setAlignTitle(OAlign.MIDDLE_RIGHT, L.QTY, L.UOM, L.PRICE, L.AMT);
			getVFld(L.PKEY).setListName("序号");
			getVFld(L.UOM).setListName("单位");
			showTitileCellUnderLine(true);//给明细标题下面加下划线
			getColumnTitle().addAttr("RepeatStyle", new GrfExp("OnPage"));//标题部分每页重复
		}
	}
}
