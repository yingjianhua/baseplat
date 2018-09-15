package irille.print.gs;

import irille.core.sys.SysCom;
import irille.core.sys.SysEm;
import irille.core.sys.SysOrg;
import irille.core.sys.SysPerson;
import irille.core.sys.SysUser;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsLoss;
import irille.gl.gs.GsLossLine;
import irille.pub.print.CrtJsp;
import irille.pub.print.GrfForm;
import irille.pub.print.Sheet;
import irille.pub.print.SheetPrint;
import irille.pub.print.SheetVarsInf;
import irille.pub.print.Size;
import irille.pub.print.Grfs.GrfItem;
import irille.pub.print.Grfs.GrfItemsSection;
import irille.pub.print.Grfs.GrfSectionItem;
import irille.pub.print.Sheets.SheetGap;
import irille.pub.view.VFlds;
import irille.pub.view.VFld.OAlign;

public class GsLossGrf extends GrfForm implements SheetVarsInf {
	static {
		GsLoss.TB.getCode();
		GsLossLine.TB.getCode();
		SysOrg.TB.getCode();
		SysCom.TB.getCode();
		SysUser.TB.getCode();
		SysEm.TB.getCode();
		SysPerson.TB.getCode();
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
	private static final GsLoss.T T = GsLoss.T.PKEY;
	private static final GsLossLine.T L = GsLossLine.T.PKEY;
	private static final SysOrg.T ORG = SysOrg.T.PKEY;
	private static final SysCom.T COM = SysCom.T.PKEY;
	private static final SysUser.T OPT = SysUser.T.PKEY;
	private static final SysEm.T EM = SysEm.T.PKEY;
	private static final SysPerson.T P = SysPerson.T.PKEY;
	private static final GsGoods.T GOODS = GsGoods.T.PKEY;

	// 设置jsp需要的参数
	public static void main(String[] args) {
		String mainParams[] = toCodes(new VFlds(T.ORG, T.CODE, T.STATUS, T.WAREHOUSE, T.APPR_BY, T.APPR_TIME, T.CREATED_BY, T.CREATED_TIME, T.REM), new VFlds(T.ORG, ORG.COM, COM.NAME), new VFlds(T.ORG, ORG.COM, COM.ADDR), new VFlds(T.ORG, ORG.COM, COM.TEL1), new VFlds(T.ORG, ORG.COM, COM.FAX));
		String linesParmas[] = toCodes(new VFlds(L.QTY, L.UOM), new VFlds(L.GOODS, GOODS.CODE), new VFlds(L.GOODS, GOODS.NAME), new VFlds(L.GOODS, GOODS.SPEC));
		// 生成jsp文件run()
		new CrtJsp(mainParams, linesParmas, GsLoss.class, GsLossLine.class).run();
	}

	public void initItemsSection(GrfItemsSection is, GrfSectionItem ph, GrfSectionItem pf, GrfSectionItem rh, GrfSectionItem rf) {
		super.initItemsSection(is, ph, pf, rh, rf);
		rh.addExp("RepeatOnPage", "T");
		rf.addExp("RepeatOnPage", "T");
		// 设置报表尾在底部
		rf.addExp("PrintAtBottom", "T");
		Sheet line2 = rh.getSheet().newInnerSheet(2, 1, new double[] { 0.62, 0.62 }, BOXWIDTH/4, BOXWIDTH/4*2, 0.9, -1);
		// 设置静态框并给予值
		rh.AddItem("StaticBox", "orderName", "盘 亏 单", line2.cellLocationSizeAbs(1, 2, 2, 1)).setTextAlignToMiddleCenter().add(FONT_REPORT_HEAD);
		Sheet line5 = rh.getSheet().newInnerSheet(5, 1, new double[] { 0.7 }, 1.8, 2.1, 0.9, 3, 1.6, 5.5, 1.5, 1.5, -1);
		rh.AddValue(T.CODE, line5.cellLocationSizeAbs(1, 2)).addAttr("GetDisplayTextScript", spliteCode());
		rh.AddValue(T.WAREHOUSE, line5.cellLocationSizeAbs(1, 4));
		rh.AddItem("MemoBox", "createdtime", "[#{createdTime}#]", line5.cellLocationSizeAbs(1, 6));
		rh.AddItem("MemoBox", "pageCount", GrfItem.pageCount(), line5.cellLocationSizeAbs(1, 7)).setTextAlignToMiddleCenter();
		rh.AddItem("MemoBox", "pageNumber", GrfItem.pageNumber(), line5.cellLocationSizeAbs(1, 8)).setTextAlignToMiddleCenter();
		Sheet fline1 = rf.getSheet().newInnerSheet(1, 1, new double[] { 0.5 }, 1.6, -1, 2, 2);
		rf.AddValue(T.CREATED_BY, fline1.cellLocationSizeAbs(1, 2));
		Sheet fline2 = rf.getSheet().newInnerSheet(2, 1, new double[] { 0.5 }, 0.9, -1);
		rf.AddValue(T.REM, fline2.cellLocationSizeAbs(1, 2));
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
		sp.setGroupFooter(new double[] { 1.2 }, 3.5, 5.9, 3, 2.6, 3.6, -1);
	}

	// 设置报表头的参数
	public GsLossGrf() {
		super(GsLoss.class, null, 8, new VFlds(T.ORG, T.CODE, T.STATUS, T.WAREHOUSE, T.APPR_BY, T.APPR_TIME, T.CREATED_BY, T.REM, COM.TEL1, COM.ADDR, COM.FAX));
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.CREATED_TIME))).addExp("DataType", "DateTime").addAttr("Format", "yyyy-MM-dd HH:mm:ss");// 组合的外键
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.ORG, ORG.COM, COM.ADDR)));// 组合的外键
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.ORG, ORG.COM, COM.TEL1)));// 组合的外键
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.ORG, ORG.COM, COM.FAX)));// 组合的外键
		getVFld(COM.TEL1).setName("电话");
		setDetailGrid(new MyDetailGrid(this).init()); // 设置明细行
	}

	private static class MyDetailGrid extends GrfFormLine {
		public MyDetailGrid(GrfForm parent) {
			super(parent, //
					new VFlds(GOODS.CODE, GOODS.SPEC, L.UOM, L.QTY), new double[] { 1.7, 13.85, 1, 2.4 }, new VFlds(L.GOODS, GOODS.CODE), new VFlds(L.GOODS, GOODS.SPEC), null, null);
			getVFlds().setAlignTitle(OAlign.MIDDLE_RIGHT, L.QTY, L.UOM);
			getVFld(GOODS.CODE).setListName("商品编号").setAlignTitle(OAlign.MIDDLE_RIGHT);
			getVFlds().setAlign(OAlign.MIDDLE_RIGHT, GOODS.CODE, L.QTY);
			getVFld(GOODS.SPEC).setListName(" 商品描述");
			getVFld(L.UOM).setListName("单位").setAlign(OAlign.MIDDLE_CENTER);;
			showTitileCellUnderLine(true);// 给明细标题下面加下划线
			getColumnTitle().addAttr("RepeatStyle", new GrfExp("OnPage"));// 标题部分每页重复
		}

	}

}
