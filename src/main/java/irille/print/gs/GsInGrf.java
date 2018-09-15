package irille.print.gs;

import irille.gl.gs.GsGoods;
import irille.gl.gs.GsIn;
import irille.gl.gs.GsInLineView;
import irille.pub.print.CrtJsp;
import irille.pub.print.GrfForm;
import irille.pub.print.Sheet;
import irille.pub.print.SheetPrint;
import irille.pub.print.SheetVarsInf;
import irille.pub.print.Size;
import irille.pub.print.GrfNode.GrfExp;
import irille.pub.print.Grfs.GrfItem;
import irille.pub.print.Grfs.GrfItemsSection;
import irille.pub.print.Grfs.GrfSectionItem;
import irille.pub.print.Sheets.SheetGap;
import irille.pub.view.VFlds;
import irille.pub.view.VFld.OAlign;

public class GsInGrf extends GrfForm implements SheetVarsInf {

	// 设置需要的参数
	static {
		GsIn.TB.getCode();
		GsInLineView.TB.getCode();
	}
	// 设置宽度和边距
	private static final double WIDTH = 21.35;
	private static final double HEIGHT = 13.95;
	private static final double TOP = 1.2;
	private static final double BOTTOM = 0;
	private static final double LEFT = 1.6;
	private static final double RIGHT = 0.8;
	private static final double BOXWIDTH = WIDTH - LEFT - RIGHT;
	private static final double BOXBOTTOM = HEIGHT - TOP - BOTTOM;
	// 设置静态全局常量
	private static final GsIn.T T = GsIn.T.PKEY;
	private static final GsInLineView.T L = GsInLineView.T.PKEY;
	private static final GsGoods.T GOODS = GsGoods.T.PKEY;

	// 产生jsp文件：数据的传递
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		String mainParams[] = toCodes(new VFlds(T.CODE,T.ORIG_FORM,T.ORIG_FORM_NUM,T.WAREHOUSE,T.CREATED_TIME,T.REM,T.CREATED_BY,T.APPR_BY),
				new VFlds(T.ORIG_FORM));
		mainParams = toCodes(mainParams, "origForm.tb.name");
		String linesParmas[] = toCodes(new VFlds(L.QTY, L.UOM,L.LOCATION),
				new VFlds(L.GOODS, GOODS.CODE),
				new VFlds(L.GOODS, GOODS.NAME), 
				new VFlds(L.GOODS, GOODS.SPEC));
		new CrtJsp(mainParams,linesParmas, GsIn.class, GsInLineView.class).run();
	}

	// 设置报表头的参数
	public GsInGrf() {
		super(GsIn.class, null, 8, new VFlds(T.CODE,T.WAREHOUSE,T.ORIG_FORM,T.ORIG_FORM_NUM,T.CREATED_TIME,T.REM,T.CREATED_BY,T.APPR_BY));
		getItemsParameter().AddItem_Name("origForm.tb.name");
		getItemsParameter().AddItem_Name("x1y1");
		getItemsParameter().AddItem_Name("x2y1");
		getItemsParameter().AddItem_Name("x3y1");
		getItemsParameter().AddItem_Name("x1y2");
		getItemsParameter().AddItem_Name("x2y2");
		getItemsParameter().AddItem_Name("x3y2");
		getItemsParameter().AddItem_Name("x1y3");
		getItemsParameter().AddItem_Name("x2y3");
		getItemsParameter().AddItem_Name("x3y3");
		getItemsParameter().AddItem_Name("x12y1");
		getItemsParameter().AddItem_Name("x12y2");
		setDetailGrid(new MyDetailGrid(this).init()); // 设置明细行
		
	}

	// 初始化页眉页脚和报表头报表尾
	public void initItemsSection(GrfItemsSection is, GrfSectionItem ph, GrfSectionItem pf, GrfSectionItem rh, GrfSectionItem rf) {
		super.initItemsSection(is, ph, pf, rh, rf);
		rh.addExp("RepeatOnPage", "T");
		rf.addExp("RepeatOnPage", "T");
		// 设置报表尾在底部
		rf.addExp("PrintAtBottom", "T");
		// 合并第2行和第3行并设置每个插件的位置大小
		Sheet line2 = rh.getSheet().newInnerSheet(2, 1, new double[] { 0.62, 0.62 }, BOXWIDTH/4, BOXWIDTH/4*2, 0.9, -1);
		// 设置静态框并给予值
		rh.AddItem("MemoBox", "orderName","[#{origForm.tb.name}#]入 库 单", line2.cellLocationSizeAbs(1, 2, 2, 1)).setTextAlignToMiddleCenter().add(FONT_REPORT_HEAD);
		Sheet line4 = rh.getSheet().newInnerSheet(4, 1, new double[] {0.7}, 1.8, 2.85,3, 1.6, 5.5, 1.5, 1.5, -1);
		rh.AddValue(T.CODE, line4.cellLocationSizeAbs(1, 2));
		rh.AddValue(T.WAREHOUSE, line4.cellLocationSizeAbs(1, 3)).setTextAlignToMiddleLeft();
		rh.AddItem("MemoBox", "createdTime","[#{createdTime}#]", line4.cellLocationSizeAbs(1, 5));
		rh.AddItem("MemoBox",  "pageCount", GrfItem.pageCount(), line4.cellLocationSizeAbs(1, 6)).setTextAlignToMiddleCenter();
		rh.AddItem("MemoBox",  "pageNumber", GrfItem.pageNumber(), line4.cellLocationSizeAbs(1, 7)).setTextAlignToMiddleCenter();
		Sheet line6 = rh.getSheet().newInnerSheet(6, 1, new double[] {0.62},0.3, BOXWIDTH/3-0.3,BOXWIDTH/3,BOXWIDTH/3);
		rh.AddItem("MemoBox", "x1y1", "[#{x1y1}#]", line6.cellLocationSizeAbs(1,2));
		rh.AddItem("MemoBox", "x2y1", "[#{x2y1}#]", line6.cellLocationSizeAbs(1,3));
		rh.AddItem("MemoBox", "x3y1", "[#{x3y1}#]", line6.cellLocationSizeAbs(1,4));
		rh.AddItem("MemoBox", "x12y1", "[#{x12y1}#]", line6.cellLocationSizeAbs(1,2)).setWidth(BOXWIDTH/3*2-0.3);
		Sheet line7 = rh.getSheet().newInnerSheet(7, 1, new double[] {0.62},0.3, BOXWIDTH/3-0.3,BOXWIDTH/3,BOXWIDTH/3);
		rh.AddItem("MemoBox", "x1y2", "[#{x1y2}#]", line7.cellLocationSizeAbs(1,2));
		rh.AddItem("MemoBox", "x2y2", "[#{x2y2}#]", line7.cellLocationSizeAbs(1,3));
		rh.AddItem("MemoBox", "x3y2", "[#{x3y2}#]", line7.cellLocationSizeAbs(1,4));
		rh.AddItem("MemoBox", "x12y2", "[#{x12y2}#]", line7.cellLocationSizeAbs(1,2)).setWidth(BOXWIDTH/3*2-0.3);
		Sheet line8 = rh.getSheet().newInnerSheet(8, 1, new double[] {0.62},0.3, BOXWIDTH/3-0.3,BOXWIDTH/3,BOXWIDTH/3);
		rh.AddItem("MemoBox", "x1y3", "[#{x1y3}#]", line8.cellLocationSizeAbs(1,2));
		rh.AddItem("MemoBox", "x2y3", "[#{x2y3}#]", line8.cellLocationSizeAbs(1,3));
		rh.AddItem("MemoBox", "x3y3", "[#{x3y3}#]", line8.cellLocationSizeAbs(1,4));
		//报表尾相关配置
		Sheet fline1 = rf.getSheet().newInnerSheet(1, 1, new double[] {0.5}, 1.6, -1);
		rf.AddValue(T.REM, fline1.cellLocationSizeAbs(1, 2));
		Sheet fline2 = rf.getSheet().newInnerSheet(2, 1, new double[] {0.5}, 1.6, 1.5, 1.2, 2);
		rf.AddValue(T.CREATED_BY, fline2.cellLocationSizeAbs(1, 2));
		rf.AddValue(T.APPR_BY, fline2.cellLocationSizeAbs(1, 4));
	}

	

	// 初始化整张单据
	public void initSheetPrint(SheetPrint sp) {
		double[] header = { 0.62, 0.62, 0.62, 0.62, 0.7, 0.62, 0.62, 0.62 };
		sp.setOriention(false);// 设置方向
		sp.setPageSize(new Size(HEIGHT, WIDTH));// 设置打印纸的大小
		sp.setPageGap(new SheetGap(TOP, BOTTOM, LEFT, RIGHT));
		sp.setPageHeader(new double[] { 0 }, 2, -1, 2);
		sp.setPageFooter(new double[] { 0 }, 2, -1, 2);
		sp.setReportHeader(header, BOXWIDTH);
		sp.setReportFooter(new double[] { 0.5, 1.3 }, BOXWIDTH);
		sp.setGroupHeader(new double[] { 0 }, BOXWIDTH);
		sp.setGroupFooter(new double[] { 0 }, BOXWIDTH);
		//sp.setGroupFooter(new double[] { 1.2 }, 3.5, 5.9, 3, 2.6, 3.6, -1);
	}

	private static class MyDetailGrid extends GrfFormLine {
		public MyDetailGrid(GrfForm parent) {
			super(parent, //
					new VFlds(GOODS.CODE, GOODS.SPEC, L.UOM, L.QTY,L.LOCATION), 
					new double[] { 1.7, 9.7, 1, 2.5, 4.5 }, 
					new VFlds(L.GOODS, GOODS.CODE), 
					new VFlds(L.GOODS, GOODS.SPEC),
					null, null,null);
			getVFlds().setAlignTitle(OAlign.MIDDLE_RIGHT, L.QTY, L.UOM);
			getVFld(L.UOM).setListName("单位").setAlign(OAlign.MIDDLE_CENTER);;
			getVFld(GOODS.CODE).setListName("商品编号").setAlignTitle(OAlign.MIDDLE_RIGHT);
			getVFlds().setAlign(OAlign.MIDDLE_RIGHT,GOODS.CODE, L.QTY);
			getVFld(GOODS.SPEC).setListName(" 商品描述");
			showTitileCellUnderLine(true);//给明细标题下面加下划线
			getColumnTitle().addAttr("RepeatStyle", new GrfExp("OnPage"));//标题部分每页重复
		}
	}
}
