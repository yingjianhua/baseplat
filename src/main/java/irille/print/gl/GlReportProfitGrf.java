/**
 * 
 */
package irille.print.gl;

import irille.gl.gl.GlReportProfit;
import irille.gl.gl.GlReportProfitLine;
import irille.pub.print.CrtJsp;
import irille.pub.print.GrfForm;
import irille.pub.print.GrfNode;
import irille.pub.print.Sheet;
import irille.pub.print.SheetPrint;
import irille.pub.print.SheetVarsInf;
import irille.pub.print.Grfs.GrfItem;
import irille.pub.print.Grfs.GrfItemsSection;
import irille.pub.print.Grfs.GrfSectionItem;
import irille.pub.print.Sheets.SheetGap;
import irille.pub.view.VFlds;
import irille.pub.view.VFld.OAlign;

/**
 * @author whx
 * 
 */
public class GlReportProfitGrf extends GrfForm implements SheetVarsInf {
	static {
		GlReportProfit.TB.getCode();
		GlReportProfitLine.TB.getCode();
	}
	private static final float WIDTH = SheetPrint.A4_VERTICAL.width();
	private static final float HEIGHT = SheetPrint.A4_VERTICAL.height();
	private static final float TOP_BOTTOM = (float)1;
	private static final float LEFT_RIGHT = (float)1.5;
	private static final float BOXWIDTH = WIDTH-LEFT_RIGHT*2;
	private static final float BOXBOTTOM = HEIGHT-TOP_BOTTOM*2;
	private static final GlReportProfit.T T = GlReportProfit.T.PKEY;
	private static final GlReportProfitLine.T L = GlReportProfitLine.T.PKEY;

	public static void main(String[] args) {
		String mainParams[] = toCodes(new VFlds(T.ORG, T.BEGIN_DATE, T.END_DATE, T.REM, T.CREATE_BY, T.CREATE_TIME, T.ROW_VERSION));
		String linesParams[] = toCodes(new VFlds(L.KEY_NAME, L.KEY_VALUE, L.AMT_BEGIN, L.AMT_END));
		new CrtJsp(mainParams, linesParams, GlReportProfit.class, GlReportProfitLine.class) {
			public void run() {
			}
		}.run();
	}
	
	/**
	 * @param mainClazz
	 * @param vflds
	 */
	public GlReportProfitGrf() {
		super(GlReportProfit.class, null, 8,//int参数表示外围主表行数
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
		rh.AddItem("StaticBox", "orderName", "利 润 表", line2.cellLocationSizeAbs(1, 1)).setTextAlignToMiddleCenter().add(FONT_REPORT_HEAD);
		Sheet line3 = rh.getSheet().newInnerSheet(3, 1, new double[] {0.7},-1);
		Sheet line4 = rh.getSheet().newInnerSheet(4, 1, new double[] {0.4}, -1);
		rh.AddItem("StaticBox", "table2", "会企02表", line4.cellLocationSizeAbs(1,1)).setTextAlignToMiddleRight();
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
		//报表脚
		Sheet fline1 = rf.getSheet().newInnerSheet(1, 1, new double[] { 1.0 }, -1);
		Sheet fline2 = rf.getSheet().newInnerSheet(2, 1, new double[] { 0.85 }, 0.7, -1);
		rf.AddItem("StaticBox", "extData", "补充资料:", fline2.cellLocationSizeAbs(1, 2));
		/*Sheet fline3 = rf.getSheet().newInnerSheet(3, 1, new double[] { 5.95 }, -1);
		GrfFreeGrid freeGrid = rf.AddFreeGrid("FreeGrid1", fline3.cellLocationSizeAbs(1, 1), new double[] { 0.85, 0.85, 0.85, 0.85, 0.85, 0.85, 0.85}, 9, 4.9, 4);
		String [][] fd = new String[][]{
				{"项    目","本年累计数","上年实际数"},
				{" 1、出售、处置部门或被投资单位所得收益",null,null},
				{" 2、自然灾害发生的损失",null,null},
				{" 3、会计政策变更增加(或减少)利润总额",null,null},
				{" 4、会计估计变更增加(或减少)利润总额",null,null},
				{" 5、债务重组损失",null,null},
				{" 6、其他",null,null}};
		freeGrid.setData(fd);
		freeGrid.setAlign(1, 1, OAlign.MIDDLE_CENTER).setAlign(1, 2, OAlign.MIDDLE_CENTER).setAlign(1, 3, OAlign.MIDDLE_CENTER);*/
		Sheet fline3 = rf.getSheet().newInnerSheet(3, 1, new double[] {0.85}, 9, 4.9, 4.15);
		rf.AddItem("StaticBox", "table1-1", "项    目", fline3.cellLocationSizeAbs(1, 1), OAlign.MIDDLE_CENTER).addAttr("BorderStyles", "[Drawleft|DrawTop]");
		rf.AddItem("StaticBox", "table1-2", "本年累计数", fline3.cellLocationSizeAbs(1, 2), OAlign.MIDDLE_CENTER).addAttr("BorderStyles", "[Drawleft|DrawTop]");
		rf.AddItem("StaticBox", "table1-3", "上年实际数", fline3.cellLocationSizeAbs(1, 3), OAlign.MIDDLE_CENTER).addAttr("BorderStyles", "[Drawleft|DrawTop|Drawright]");
		Sheet fline4 = rf.getSheet().newInnerSheet(4, 1, new double[] {0.85}, 9, 4.9, 4.15);
		rf.AddItem("StaticBox", "table2-1", " 1、出售、处置部门或被投资单位所得收益", fline4.cellLocationSizeAbs(1, 1)).addAttr("BorderStyles", "[Drawleft|DrawTop]");
		rf.AddItem("StaticBox", "table2-2", "", fline4.cellLocationSizeAbs(1, 2)).addAttr("BorderStyles", "[Drawleft|DrawTop]");
		rf.AddItem("StaticBox", "table2-3", "", fline4.cellLocationSizeAbs(1, 3)).addAttr("BorderStyles", "[Drawleft|DrawTop|Drawright]");
		Sheet fline5 = rf.getSheet().newInnerSheet(5, 1, new double[] {0.85}, 9, 4.9, 4.15);
		rf.AddItem("StaticBox", "table3-1", " 2、自然灾害发生的损失", fline5.cellLocationSizeAbs(1, 1)).addAttr("BorderStyles", "[Drawleft|DrawTop]");
		rf.AddItem("StaticBox", "table3-2", "", fline5.cellLocationSizeAbs(1, 2)).addAttr("BorderStyles", "[Drawleft|DrawTop]");
		rf.AddItem("StaticBox", "table3-3", "", fline5.cellLocationSizeAbs(1, 3)).addAttr("BorderStyles", "[Drawleft|DrawTop|Drawright]");
		Sheet fline6 = rf.getSheet().newInnerSheet(6, 1, new double[] {0.85}, 9, 4.9, 4.15);
		rf.AddItem("StaticBox", "table4-1", " 3、会计政策变更增加(或减少)利润总额", fline6.cellLocationSizeAbs(1, 1)).addAttr("BorderStyles", "[Drawleft|DrawTop]");
		rf.AddItem("StaticBox", "table4-2", "", fline6.cellLocationSizeAbs(1, 2)).addAttr("BorderStyles", "[Drawleft|DrawTop]");
		rf.AddItem("StaticBox", "table4-3", "", fline6.cellLocationSizeAbs(1, 3)).addAttr("BorderStyles", "[Drawleft|DrawTop|Drawright]");
		Sheet fline7 = rf.getSheet().newInnerSheet(7, 1, new double[] {0.85}, 9, 4.9, 4.15);
		rf.AddItem("StaticBox", "table5-1", " 4、会计估计变更增加(或减少)利润总额", fline7.cellLocationSizeAbs(1, 1)).addAttr("BorderStyles", "[Drawleft|DrawTop]");
		rf.AddItem("StaticBox", "table5-2", "", fline7.cellLocationSizeAbs(1, 2)).addAttr("BorderStyles", "[Drawleft|DrawTop]");
		rf.AddItem("StaticBox", "table5-3", "", fline7.cellLocationSizeAbs(1, 3)).addAttr("BorderStyles", "[Drawleft|DrawTop|Drawright]");
		Sheet fline8 = rf.getSheet().newInnerSheet(8, 1, new double[] {0.85}, 9, 4.9, 4.15);
		rf.AddItem("StaticBox", "table6-1", " 5、债务重组损失", fline8.cellLocationSizeAbs(1, 1)).addAttr("BorderStyles", "[Drawleft|DrawTop]");
		rf.AddItem("StaticBox", "table6-2", "", fline8.cellLocationSizeAbs(1, 2)).addAttr("BorderStyles", "[Drawleft|DrawTop]");
		rf.AddItem("StaticBox", "table6-3", "", fline8.cellLocationSizeAbs(1, 3)).addAttr("BorderStyles", "[Drawleft|DrawTop|Drawright]");
		Sheet fline9 = rf.getSheet().newInnerSheet(9, 1, new double[] {0.85}, 9, 4.9, 4.15);
		rf.AddItem("StaticBox", "table7-1", " 6、其他", fline9.cellLocationSizeAbs(1, 1)).addAttr("BorderStyles", "[Drawleft|DrawTop|DrawBottom]");
		rf.AddItem("StaticBox", "table7-2", "", fline9.cellLocationSizeAbs(1, 2)).addAttr("BorderStyles", "[Drawleft|DrawTop|DrawBottom]");
		rf.AddItem("StaticBox", "table7-3", "", fline9.cellLocationSizeAbs(1, 3)).addAttr("BorderStyles", "[Drawleft|DrawTop|Drawright|DrawBottom]");
		
		Sheet fline10 = rf.getSheet().newInnerSheet(10, 1, new double[] {1.5}, 4.2, 4, 3.4, 3.3, -1);
		rf.AddItem("StaticBox", "charge", "企业负责人：", fline10.cellLocationSizeAbs(1, 1));
		rf.AddItem("StaticBox", "accounting", "主管会计：", fline10.cellLocationSizeAbs(1, 2));
		rf.AddItem("StaticBox", "tab", "制表：", fline10.cellLocationSizeAbs(1, 3));
		rf.AddItem("StaticBox", "reportDate", "报出日期：", fline10.cellLocationSizeAbs(1, 4));
		rf.AddItem("StaticBox", "date", "年     月     日", fline10.cellLocationSizeAbs(1, 5));
	}
	
	// gh：分组头，gf：分组脚，分组默认隐藏
	/*@Override
	public void initItemsGroup(GrfItemsGroup ig, GrfGroupHeader gh, GrfGroupFooter gf) {
		super.initItemsGroup(ig, gh, gf);
		gf.setVisible(new GrfExp("T"));
		gf.addAttr("PrintAtBottom", new GrfExp("T"));//打印在页底
		gf.AddItem("StaticBox", "stc", "金额合计（人民币）", 1, 1);
		//gf.AddItem("MemoBox", "quoteSummaryTotalCh", "[#summaryTotalCh#]", 1, 2);
		//gf.AddItem("MemoBox", "quoteSummaryTotal", "[#summaryTotal#]", 1, 3).setTextAlignToMiddleRight();
		gf.AddItem("StaticBox", "sp", "本页小计", 1, 4);
	}*/
	
	@Override
	public void initSheetPrint(SheetPrint sp) {
		sp.setPageSize(SheetPrint.A4_VERTICAL);//纸张大小为A4
		sp.setOriention(false);//纸张纵向
		sp.setPageGap(new SheetGap(TOP_BOTTOM, LEFT_RIGHT));//间隙 上下留1cm，左右留1.5cm
		sp.setPageHeader(new double[] { 1 });//页眉
		sp.setPageFooter(new double[] { 0 });//页脚
		sp.setReportHeader(new double[] { 1, 0.9, 0.7, 0.4, 0.4, 0.4}, BOXWIDTH);
		sp.setReportFooter(new double[] { 0.1, 0.85, 0.85, 0.85, 0.85, 0.85, 0.85, 0.85, 0.85, 1.5}, BOXWIDTH);
		sp.setGroupHeader(new double[] {0.1}, 18);
		sp.setGroupFooter(new double[] {0.05}, 3.5, 5.9, 3, 2.6, 3);
	}

	public static class GrfFreeGrid{
		private GrfItem _parent;
		private String _name;
		private int _rows;
		private int _columns;
		private double[] _heights;
		private double[] _widths;
		private String [][] _text;
		private GrfNode [][] _nodes;
		
		public GrfFreeGrid(GrfItem parent, String name) {
			_parent = parent;
			_name = name;
		}
		
		public GrfFreeGrid init(double[] heights, double...widths) {
			_rows = heights.length;
			_columns = widths.length;
			_heights = heights;
			_widths = widths;
			_text = new String[_rows][_columns];
			_nodes = new GrfNode[_rows][_columns];
			_parent.AddNode("Object Border").addExp("Styles", "[DrawLeft|DrawTop|DrawRight|DrawBottom]");
			initColumn();
			initRow();
			return this;
		}
		private void initColumn() {
			_parent.addAttr("ColumnCount", _columns);
			GrfNode node = _parent.AddNode("Items FreeGridColumn");
			for(int i=0;i<_columns;i++) {
				node.AddNode("Item["+(i+1)+"]").addAttr("Width", _widths[i]);
			}
		}
		private void initRow() {
			_parent.addAttr("RowCount", _rows);
			GrfNode node = _parent.AddNode("Items FreeGridRow");
			for(int i=0;i<_rows;i++) {
				node.AddNode("Item["+(i+1)+"]").addAttr("Height", _heights[i]);
			}
		}
		public void setData(String[][] text) {
			_text = text;
			GrfNode node = _parent.AddNode("Items FreeGridCell");
			for(int i=0;i<_rows;i++) {
				for(int j=0;j<_columns;j++) {
					if(_text[i][j]!=null) {
						_nodes[i][j] = node.AddNode("Item["+(i+1)+","+(j+1)+"]").addAttr("Text", _text[i][j]);
					}
				}
			}
		}
		public GrfFreeGrid setAlign(int row, int column, OAlign align) {
			_nodes[row-1][column-1].addExp("TextAlign", align.getLine().getCode());
			return this;
		}
		
		public String getText(int row, int column) {
			return _text[row-1][column-1];
		}
		
	}
	private static class MyDetailGrid extends GrfFormLine {
		public MyDetailGrid(GrfForm parent) {
			super(
					parent, 
					new VFlds(L.KEY_NAME, L.KEY_VALUE, L.AMT_BEGIN, L.AMT_END),
					new double[] { 9, 1.25, 3.65, 4.15},
					null,null,null,null
					);
			getVFlds().setAlignTitle(OAlign.MIDDLE_CENTER, L.KEY_NAME, L.KEY_VALUE, L.AMT_BEGIN, L.AMT_END);
			getVFld(L.KEY_NAME).setListName("项    目");
			getVFld(L.KEY_VALUE).setListName("行次").setAlign(OAlign.MIDDLE_CENTER);
			getVFld(L.AMT_BEGIN).setListName("本 月 数").setAlign(OAlign.MIDDLE_RIGHT);
			getVFld(L.AMT_END).setListName("本年累计数");//.setAlign(OAlign.MIDDLE_LEFT);
			getColumnTitle().setHeight(0.882);
			getColumnContent().setHeight(0.882);
			setShowColLine(new GrfExp("T"));
			setShowRowLine(new GrfExp("T"));
			setBorderStyles(new GrfExp("[DrawLeft|DrawTop|DrawRight]"));
			//showTitileCellUnderLine(true);//给明细标题下面加下划线
			getColumnTitle().addExp("RepeatStyle", "OnPage");//标题部分每页重复
		}
	}
	
}
