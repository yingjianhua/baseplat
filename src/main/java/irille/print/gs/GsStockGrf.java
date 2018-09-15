package irille.print.gs;

import irille.gl.gs.GsGoods;
import irille.gl.gs.GsStock;
import irille.gl.gs.GsStockLine;
import irille.pub.print.CrtJsp;
import irille.pub.print.GrfForm;
import irille.pub.print.Sheet;
import irille.pub.print.SheetPrint;
import irille.pub.print.SheetVarsInf;
import irille.pub.print.Size;
import irille.pub.print.GrfForm.GrfFormLine;
import irille.pub.print.GrfNode.GrfExp;
import irille.pub.print.Grfs.GrfItem;
import irille.pub.print.Grfs.GrfItemsSection;
import irille.pub.print.Grfs.GrfSectionItem;
import irille.pub.print.Sheets.SheetGap;
import irille.pub.view.VFlds;
import irille.pub.view.VFld.OAlign;

/**
 * @author whx
 * 注意不要用junit生成前端的打印模板
 * 原因：明细需要
 */
public class GsStockGrf extends GrfForm implements SheetVarsInf {
	static {
		GsStock.TB.getCode();
		GsStockLine.TB.getCode();
	}
	// 设置静态全局常量
	private static final GsStock.T T = GsStock.T.PKEY;
	private static final GsStockLine.T L = GsStockLine.T.PKEY;
	private static final GsGoods.T GOODS = GsGoods.T.PKEY;

	// 设置jsp需要的参数
	public static void main(String[] args) {
		String mainParams[] = toCodes(new VFlds(T.WAREHOUSE), new VFlds(T.GOODS, GOODS.SPEC));
		String linesParmas[] = toCodes(new VFlds(L.GS_FORM, L.NAME, L.GS_FORM_NUM, L.ORIG_FORM,
				L.ORIG_FORM_NUM, L.GS_QTY, L.GS_TIME, L.QTY));
		// 生成jsp文件run()
		new CrtJsp(mainParams, linesParmas, GsStock.class, GsStockLine.class).run();
	}

	// 设置报表头的参数
	public GsStockGrf() {
		super(GsStock.class, null, 7, new VFlds(T.WAREHOUSE, T.GOODS, GOODS.SPEC));
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.GOODS, GOODS.SPEC)));// 组合的外键
		setDetailGrid(new MyDetailGrid(this).init()); // 设置明细行
		//getDetailGrid().addExp("BorderStyles", "[DrawTop]");
		getDetailGrid().setBorderStyles(new GrfExp("[DrawTop]"));
	}

	@Override
	public void initItemsSection(GrfItemsSection is, GrfSectionItem ph, GrfSectionItem pf,
			GrfSectionItem rh, GrfSectionItem rf) {
		super.initItemsSection(is, ph, pf, rh, rf);
		// 设置每一页重复打印报表头和报表尾
		rh.addExp("RepeatOnPage", "T");
		rf.addExp("RepeatOnPage", "T");
		// 设置报表尾在底部
		rf.addExp("PrintAtBottom", "T");
		Sheet line2 = rh.getSheet().newInnerSheet(2, 1, new double[] { 0.62, 0.62 }, 19);
		// 设置静态框并给予值
		rh.AddItem("StaticBox", "orderName", "存货出入库明细单", line2.cellLocationSizeAbs(1,1,2,1))
				.setTextAlignToMiddleCenter().add(FONT_REPORT_HEAD);
		Sheet line4 = rh.getSheet().newInnerSheet(4, 1, new double[] {0.62}, 0.8,5,5,-1,2,2);
		rh.AddItem("SystemVarBox",  "time", null,line4.cellLocationSizeAbs(1, 2)).setTextAlignToMiddleCenter()
		.addAttr("Format", "日期:yyyy/MM/dd HH:mm").addExp("SystemVar", "CurrentDateTime");
		rh.AddItem("SystemVarBox",  "pageCount", null, line4.cellLocationSizeAbs(1, 5)).setTextAlignToMiddleCenter()
		.addAttr("Format", "共0页").addExp("SystemVar", "PageCount");
		rh.AddItem("SystemVarBox",  "pageNumber", null, line4.cellLocationSizeAbs(1, 6)).setTextAlignToMiddleCenter()
		.addAttr("Format", "第0页");
		Sheet line5 = rh.getSheet().newInnerSheet(5, 1, new double[] {0.62}, 0.5,1,-1);
		rh.AddItem("StaticBox", "warehouse", "仓库:", line5.cellLocationSizeAbs(1,2)).setTextAlignToMiddleCenter();
		rh.AddValue(T.WAREHOUSE, line5.cellLocationSizeAbs(1, 3));
		Sheet line6 = rh.getSheet().newInnerSheet(6, 1, new double[] {0.62}, 0.5,1.7,10);
		rh.AddItem("StaticBox", "spec", "货物描述:", line6.cellLocationSizeAbs(1,2)).setTextAlignToMiddleCenter();
		rh.AddValue( GOODS.SPEC, line6.cellLocationSizeAbs(1, 3),new VFlds(T.GOODS, GOODS.SPEC)).setTextAlignToMiddleLeft();
	}

	public void initSheetPrint(SheetPrint sp) {
		// double[] header = new double[getReportHeaderLines()];
		double[] header = { 1, 0.62, 0.62, 0.62, 0.62, 0.62,0.62};
		sp.setOriention(false);
		sp.setPageSize(SheetPrint.A4_VERTICAL);
		sp.setPageGap(new SheetGap(0,1));
		sp.setPageHeader(new double[] { 0 }, 2, -1, 2);
		sp.setPageFooter(new double[] { 0 }, 2, -1, 2);
		sp.setReportHeader(header, 19);
		sp.setReportFooter(new double[] { 0.5, 1.3 }, 19);
		sp.setGroupHeader(new double[] { 0 }, 19);
		sp.setGroupFooter(new double[] { 0 }, 19);
	}

	// 设置明细内容和配置
	private static class MyDetailGrid extends GrfFormLine {
		public MyDetailGrid(GrfForm parent) {
			super(parent, //
					new VFlds(L.GS_FORM, L.NAME, L.GS_FORM_NUM, L.ORIG_FORM, L.ORIG_FORM_NUM,
							L.GS_QTY, L.GS_TIME, L.QTY), 
							new double[] {1,4.5,4,1.2,4,3.5,2.8,3 }, 
							null,null, null, null, null, null, null, null,null);
			getVFlds().setAlign(OAlign.TOP_LEFT, L.GS_FORM, L.NAME, L.GS_FORM_NUM, L.ORIG_FORM, L.ORIG_FORM_NUM,
							L.GS_QTY, L.GS_TIME, L.QTY);
			getVFlds().setAlignTitle(OAlign.MIDDLE_LEFT, L.GS_FORM, L.NAME, L.GS_FORM_NUM, L.ORIG_FORM, L.ORIG_FORM_NUM,
					L.GS_QTY, L.GS_TIME, L.QTY);
			getColumnContent().getItemsColumnContentCell();
			getVFld(L.GS_FORM).setListName("类型");
			getVFld(L.GS_FORM_NUM).setListName("出入库单号");
			getVFld(L.ORIG_FORM).setListName("源类");
			getVFld(L.ORIG_FORM_NUM).setListName("原单据号");
			getVFld(L.GS_QTY).setListName("出入数量");
			getItemsColumn().AddItem_Name_Width("Column1", 1);
			getColumnContent().getItemsColumnContentCell().AddItem_Column_DataField("Column1", null).addExp("FreeCell", "T")
			.AddNode("Items Control").AddNode("Item").addExp("Type", "SystemVarBox")
			.addAttr("Name", "SystemVarBox1").addExp("Width", "1").addExp("Height", "0.61").addExp("TextAlign", "topCenter").addExp("SystemVar", "RowNo");
			getColumnTitle().getItemsColumnTitleCell().AddItem_Column_Text_FreeCell("F", "Column1", "序号", "T", OAlign.MIDDLE_CENTER);
			showTitileCellUnderLine(true);// 给明细标题下面加下划线
			getColumnTitle().addAttr("RepeatStyle", new GrfExp("OnPage"));// 标题部分每页重复
		}
	}
}
