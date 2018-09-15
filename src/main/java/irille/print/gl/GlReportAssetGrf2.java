/**
 * 
 */
package irille.print.gl;

import irille.gl.gl.GlReportAsset;
import irille.gl.gl.GlReportAssetLine;
import irille.pub.print.CrtJsp;
import irille.pub.print.GrfDetailGrid;
import irille.pub.print.GrfForm;
import irille.pub.print.Sheet;
import irille.pub.print.SheetPrint;
import irille.pub.print.SheetVarsInf;
import irille.pub.print.GrfNode.GrfExp;
import irille.pub.print.Grfs.GrfColumnContent;
import irille.pub.print.Grfs.GrfGroupFooter;
import irille.pub.print.Grfs.GrfGroupHeader;
import irille.pub.print.Grfs.GrfItem;
import irille.pub.print.Grfs.GrfItemsGroup;
import irille.pub.print.Grfs.GrfItemsSection;
import irille.pub.print.Grfs.GrfSectionItem;
import irille.pub.print.Sheets.SheetGap;
import irille.pub.view.VFlds;
import irille.pub.view.VFld.OAlign;

/**
 * @author whx
 * 
 */
public class GlReportAssetGrf2 extends GrfForm implements SheetVarsInf {
	static {
		GlReportAsset.TB.getCode();
		GlReportAssetLine.TB.getCode();
	}
	private static final float WIDTH = SheetPrint.A4_VERTICAL.width();
	private static final float HEIGHT = SheetPrint.A4_VERTICAL.height();
	private static final float TOP_BOTTOM = (float)1;
	private static final float LEFT_RIGHT = (float)1.5;
	private static final float BOXWIDTH = WIDTH-LEFT_RIGHT*2;
	private static final float BOXBOTTOM = HEIGHT-TOP_BOTTOM*2;
	private static final GlReportAsset.T T = GlReportAsset.T.PKEY;
	private static final GlReportAssetLine.T L = GlReportAssetLine.T.PKEY;

	public static void main(String[] args) {
		String mainParams[] = toCodes(new VFlds(T.ORG, T.BEGIN_DATE, T.END_DATE, T.REM, T.CREATE_BY, T.CREATE_TIME, T.ROW_VERSION));
		String linesParams[] = toCodes(new VFlds(L.KEY_NAME, L.KEY_VALUE, L.AMT_BEGIN, L.AMT_END));
		new CrtJsp(mainParams, linesParams, GlReportAsset.class, GlReportAssetLine.class) {
			public void run() {
			}
		}.run();
	}
	
	/**
	 * @param mainClazz
	 * @param vflds
	 */
	public GlReportAssetGrf2() {
		super(GlReportAsset.class, null, 8,//int参数表示外围主表行数
				new VFlds(T.ORG, T.BEGIN_DATE, T.END_DATE, T.REM, T.CREATE_BY, T.CREATE_TIME, T.ROW_VERSION));
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
		Sheet line1 = rh.getSheet().newInnerSheet(1, 1, new double[] { 1.0 });
		Sheet line2 = rh.getSheet().newInnerSheet(2, 1, new double[] { 0.9 }, -1);
		rh.AddItem("StaticBox", "orderName", "资 产 负 债 表", line2.cellLocationSizeAbs(1, 1)).setTextAlignToMiddleCenter().add(FONT_REPORT_HEAD);
		Sheet line3 = rh.getSheet().newInnerSheet(3, 1, new double[] {0.7},-1);
		Sheet line4 = rh.getSheet().newInnerSheet(4, 1, new double[] {0.4}, -1);
		rh.AddItem("StaticBox", "table2", "会企01表", line4.cellLocationSizeAbs(1,1)).setTextAlignToMiddleRight();
		Sheet line5 = rh.getSheet().newInnerSheet(5, 1, new double[] {0.4}, 1.6, 5.9, 1, 0.4, 1, 0.4, 6.3, -1);
		rh.AddItem("StaticBox", "table3", "编制单位:", line5.cellLocationSizeAbs(1,1)).setTextAlignToTopLeft();
		rh.AddValue(T.ORG, line5.cellLocationSizeAbs(1, 2)).setTextAlignToTopLeft();
		GrfItem year = rh.AddValue(T.END_DATE, line5.cellLocationSizeAbs(1, 3));
		year.setTextAlignToTopCenter().addAttr("GetDisplayTextScript", getYear());
		year.addExp("BorderStyles", "[DrawBottom]");
		rh.AddItem("StaticBox", "table4", "年", line5.cellLocationSizeAbs(1, 4)).setTextAlignToTopLeft();
		GrfItem month = rh.AddValue(T.END_DATE, line5.cellLocationSizeAbs(1, 5));
		month.setTextAlignToTopCenter().addAttr("GetDisplayTextScript", getMonth());
		month.addExp("BorderStyles", "[DrawBottom]");
		rh.AddItem("StaticBox", "table5", "月", line5.cellLocationSizeAbs(1, 6)).setTextAlignToTopLeft();
		rh.AddItem("StaticBox", "table6", "单位:元", line5.cellLocationSizeAbs(1,8)).setTextAlignToTopRight();
		
		Sheet line6 = rh.getSheet().newInnerSheet(7, 1, new double[] { 1.2 }, 4.48, 0.625, 1.825, 2.075, 4.48, 0.625, 1.825, 2.075);
		rh.AddItem("StaticBox", "asset", "资  产", line6.cellLocationSizeAbs(1,1), OAlign.MIDDLE_CENTER).addExp("BorderStyles", "[DrawLeft|DrawTop]");
		rh.AddItem("StaticBox", "value1", "行次", line6.cellLocationSizeAbs(1,2), OAlign.MIDDLE_CENTER).addExp("BorderStyles", "[DrawLeft|DrawTop]").addExp("WordWrap", "T");
		rh.AddItem("StaticBox", "amt_begin1", "年 初 数", line6.cellLocationSizeAbs(1,3), OAlign.MIDDLE_CENTER).addExp("BorderStyles", "[DrawLeft|DrawTop]");
		rh.AddItem("StaticBox", "amt_end1", "期 末 数", line6.cellLocationSizeAbs(1,4), OAlign.MIDDLE_CENTER).addExp("BorderStyles", "[DrawLeft|DrawTop]");
		rh.AddItem("StaticBox", "asset", "负债和所有者权益（或股东权益）", line6.cellLocationSizeAbs(1,5), OAlign.MIDDLE_CENTER).addExp("BorderStyles", "[DrawLeft|DrawTop]");
		rh.AddItem("StaticBox", "value2", "行次", line6.cellLocationSizeAbs(1,6), OAlign.MIDDLE_CENTER).addExp("BorderStyles", "[DrawLeft|DrawTop]").addExp("WordWrap", "T");
		rh.AddItem("StaticBox", "amt_begin2", "年 初 数", line6.cellLocationSizeAbs(1,7), OAlign.MIDDLE_CENTER).addExp("BorderStyles", "[DrawLeft|DrawTop]");
		rh.AddItem("StaticBox", "amt_end2", "期 末 数", line6.cellLocationSizeAbs(1,8), OAlign.MIDDLE_CENTER).addExp("BorderStyles", "[DrawLeft|DrawTop|Drawright]");
		//报表脚
		
		Sheet fline1 = rf.getSheet().newInnerSheet(1, 1, new double[] {1.5}, 4.2, 4, 3.4, 3.3, -1);
		rf.AddItem("StaticBox", "charge", "企业负责人：", fline1.cellLocationSizeAbs(1, 1));
		rf.AddItem("StaticBox", "accounting", "主管会计：", fline1.cellLocationSizeAbs(1, 2));
		rf.AddItem("StaticBox", "tab", "制表：", fline1.cellLocationSizeAbs(1, 3));
		rf.AddItem("StaticBox", "reportDate", "报出日期：", fline1.cellLocationSizeAbs(1, 4));
		rf.AddItem("StaticBox", "date", "年     月     日", fline1.cellLocationSizeAbs(1, 5));
	}
	
	/*// gh：分组头，gf：分组脚，分组默认隐藏
	@Override
	public void initItemsGroup(GrfItemsGroup ig, GrfGroupHeader gh, GrfGroupFooter gf) {
		super.initItemsGroup(ig, gh, gf);
		gf.setVisible(new GrfExp("T"));
		gf.addAttr("PrintAtBottom", new GrfExp("T"));//打印在页底
		gf.AddItem("StaticBox", "stc", "金额合计（人民币）：", 1, 1).setTextAlignToMiddleRight();
		gf.AddItem("MemoBox", "quoteSummaryTotalCh", "[#summaryTotalCh#]", 1, 2);
		gf.AddItem("MemoBox", "quoteSummaryTotal", "[#summaryTotal#]", 1, 3).setTextAlignToMiddleRight();
		gf.AddItem("StaticBox", "sp", "本页小计", 1, 4);
	}*/
	
	@Override
	public void initSheetPrint(SheetPrint sp) {
		sp.setPageSize(SheetPrint.A4_VERTICAL);//纸张大小为A4
		sp.setOriention(false);//纸张纵向
		sp.setPageGap(new SheetGap(TOP_BOTTOM, LEFT_RIGHT));//间隙 上下留1cm，左右留1.5cm
		sp.setPageHeader(new double[] { 1 });//页眉
		sp.setPageFooter(new double[] { 0 });//页脚
		sp.setReportHeader(new double[] { 1, 0.9, 0.7, 0.4, 0.4, 0.4, 1.2}, BOXWIDTH);
		sp.setReportFooter(new double[] { 2.0 }, BOXWIDTH);
		sp.setGroupHeader(new double[] {0}, 4.5, 0.625, 1.825, 2.075, 4.5, 0.625, 1.825, 2.075);
		sp.setGroupFooter(new double[] {1.2}, 3.5, 5.9, 3, 2.6,3.6, -1);
	}
	
	private static class MyDetailGrid extends GrfFormLine {
		public MyDetailGrid(GrfForm parent) {
			super(
					parent, 
					new VFlds(L.KEY_NAME, L.KEY_VALUE, L.AMT_BEGIN, L.AMT_END),
					new double[] { 4.5, 0.625, 1.825, 2.075, 4.5, 0.625, 1.825, 2.075},
					null,null,null,null
					);
			getVFlds().setAlignTitle(OAlign.MIDDLE_CENTER, L.KEY_NAME, L.KEY_VALUE, L.AMT_BEGIN, L.AMT_END);
			getVFld(L.KEY_NAME).setListName("项    目");
			getVFld(L.KEY_VALUE).setListName("行次").setAlign(OAlign.MIDDLE_CENTER);
			getVFld(L.AMT_BEGIN).setListName("本 月 数").setAlign(OAlign.MIDDLE_RIGHT);
			getVFld(L.AMT_END).setListName("本年累计数");//.setAlign(OAlign.MIDDLE_LEFT);
			getColumnTitle().setHeight(0);
			getColumnContent().setHeight(0.543);
			setShowColLine(new GrfExp("T"));
			setShowRowLine(new GrfExp("T"));
			setPageColumnCount(2);
			//setPageColumnDirection(new GrfExp(GrfDetailGrid.DOWN_ACROSS_EQUAL));
			setBorderStyles(new GrfExp("[DrawLeft|DrawTop|DrawRight]"));
			//showTitileCellUnderLine(true);//给明细标题下面加下划线
			getColumnTitle().addExp("RepeatStyle", "OnPage");//标题部分每页重复
	//		GrfColumnContent content = getColumnContent();
		}
	}
	
}
