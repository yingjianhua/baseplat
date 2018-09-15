package irille.print.pur;

import irille.core.sys.SysCom;
import irille.core.sys.SysEm;
import irille.core.sys.SysOrg;
import irille.core.sys.SysPerson;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysShiping;
import irille.core.sys.SysUser;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsWarehouse;
import irille.pss.pur.PurOrder;
import irille.pss.pur.PurOrderLine;
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

public class PurOrderGrf extends GrfForm implements SheetVarsInf {
	static {
		PurOrder.TB.getCode(); PurOrderLine.TB.getCode(); SysOrg.TB.getCode(); SysCom.TB.getCode(); SysShiping.TB.getCode(); SysPersonLink.TB.getCode(); GsWarehouse.TB.getCode(); SysUser.TB.getCode(); SysEm.TB.getCode(); SysPerson.TB.getCode();
	}
	private static final PurOrder.T T = PurOrder.T.PKEY;
	private static final PurOrderLine.T L = PurOrderLine.T.PKEY;
	private static final SysOrg.T ORG = SysOrg.T.PKEY;
	private static final SysCom.T COM = SysCom.T.PKEY;
	private static final GsGoods.T GOODS = GsGoods.T.PKEY;
	private static final SysShiping.T SHIP = SysShiping.T.PKEY;
	private static final SysPersonLink.T LINK = SysPersonLink.T.PKEY;
	private static final GsWarehouse.T WAREHOUSE = GsWarehouse.T.PKEY;
	private static final SysUser.T BUYER = SysUser.T.PKEY;
	private static final SysEm.T EM = SysEm.T.PKEY;
	private static final SysPerson.T PERSON = SysPerson.T.PKEY;
	private static final Tb[] tbs = {SysPersonLink.TB};
	private static final VFlds[] personLinkName = {new VFlds(LINK.NAME)};
	private static final VFlds[] personLinkPeMobile = {new VFlds(LINK.PE_MOBILE)};
	private static final VFlds[] personLinkFax = {new VFlds(LINK.OF_FAX)};
	
	public static void main(String[] args) {
		String mainParams[] = toCodes(new VFlds(T.ORG, T.CODE, T.SUPPLIER, T.SUPNAME, T.STATUS, T.AMT, T.AMT_COST, T.REV_ADDR,
				T.WAREHOUSE, T.APPR_BY, T.APPR_TIME, T.CREATED_BY, T.CREATED_TIME, T.SHIPING_MODE, T.BUYER, T.REM),
				new VFlds(T.ORG, ORG.CODE),
				new VFlds(T.ORG, ORG.COM, COM.NAME),
				new VFlds(T.ORG, ORG.COM, COM.ADDR),
				new VFlds(T.ORG, ORG.COM, COM.TEL1),
				new VFlds(T.ORG, ORG.COM, COM.FAX),
				new VFlds(T.SHIPING, SHIP.ADDR),
				new VFlds(T.SHIPING, SHIP.NAME),
				new VFlds(T.SHIPING, SHIP.MOBILE),
				new VFlds(T.SHIPING, SHIP.TIME_SHIP_PLAN),
				new VFlds(T.BUYER, BUYER.TB_OBJ, EM.PERSON, PERSON.OF_TEL)
				);
		mainParams = toCodes(mainParams, toLinkCode(new VFlds(T.WAREHOUSE, WAREHOUSE.DEPT), tbs, personLinkName), toLinkCode(new VFlds(T.WAREHOUSE, WAREHOUSE.DEPT), tbs, personLinkPeMobile), toLinkCode(new VFlds(T.SUPPLIER), tbs, personLinkFax));
		String linesParams[] = toCodes(new VFlds(L.PKEY, L.QTY, L.UOM, L.PRICE, L.AMT),
				new VFlds(L.GOODS, GOODS.CODE),
				new VFlds(L.GOODS, GOODS.NAME),
				new VFlds(L.GOODS, GOODS.SPEC)
				);
		new CrtJsp(mainParams, linesParams, PurOrder.class, PurOrderLine.class).run();
	}

	public PurOrderGrf() {
		super(PurOrder.class, "采 购 订 单", 8, 
				new VFlds(T.ORG, T.CODE, T.SUPPLIER, T.SUPNAME, T.STATUS, T.AMT, T.AMT_COST, T.WAREHOUSE, T.REV_ADDR,
						T.APPR_BY, T.APPR_TIME, T.CREATED_BY, T.CREATED_TIME, T.SHIPING_MODE, T.BUYER, T.REM, COM.TEL1, COM.ADDR, COM.FAX, SHIP.MOBILE, SHIP.TIME_SHIP_PLAN, LINK.NAME, LINK.PE_MOBILE, LINK.OF_FAX, PERSON.OF_TEL));
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.ORG, ORG.CODE)));//组合的外键
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.ORG, ORG.COM, COM.ADDR)));//组合的外键
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.ORG, ORG.COM, COM.TEL1)));//组合的外键
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.ORG, ORG.COM, COM.FAX)));//组合的外键
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.SHIPING, SHIP.ADDR)));//组合的外键
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.SHIPING, SHIP.NAME)));//组合的外键
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.SHIPING, SHIP.MOBILE)));//组合的外键
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.SHIPING, SHIP.TIME_SHIP_PLAN)));//组合的外键
		getItemsParameter().AddItem_Name(toCode(new VFlds(T.BUYER, BUYER.TB_OBJ, EM.PERSON, PERSON.OF_TEL)));
		getItemsParameter().AddItem_Name(toLinkCode(new VFlds(T.WAREHOUSE, WAREHOUSE.DEPT), tbs, personLinkName));//组合的外键
		getItemsParameter().AddItem_Name(toLinkCode(new VFlds(T.WAREHOUSE, WAREHOUSE.DEPT), tbs, personLinkPeMobile));//组合的外键
		getItemsParameter().AddItem_Name(toLinkCode(new VFlds(T.SUPPLIER), tbs, personLinkFax));//组合的外键
		getVFld(COM.TEL1).setName("电话");
		getVFld(T.CREATED_TIME).setName("订货日期");
		getVFld(SHIP.NAME).setName("联系人");
		getVFld(LINK.NAME).setName("联系人");
		getVFld(LINK.PE_MOBILE).setName("手机");
		getVFld(LINK.OF_FAX).setName("传真");
		getVFld(PERSON.OF_TEL).setName("电话");
		getVFld(SHIP.TIME_SHIP_PLAN).setName("发货时间");
		getVFld(T.REV_ADDR).setName("交货地址");
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
			getVFlds().setAlignToMiddleRight(T.AMT,T.AMT_COST);
			getVFlds().setAlignToMiddleCenter(T.ORG);
			
			rh.AddValue(T.ORG, 1, 1).setTextAlignToMiddleCenter().add(FONT_REPORT_HEAD);
			Sheet line2 = rh.getSheet().newInnerSheet(2, 1, new double[] {0.65, 0.65}, 5, 17, 1.3, -1);
			rh.AddItem("StaticBox", "orderName", "采 购 订 单", line2.cellLocationSizeAbs(1, 2, 2, 1)).setTextAlignToMiddleCenter().add(FONT_REPORT_HEAD);
			rh.AddName(COM.TEL1, line2.cellLocationSizeAbs(2, 3));
			rh.AddValue(COM.TEL1, line2.cellLocationSizeAbs(2, 4), new VFlds(T.ORG, ORG.COM, COM.TEL1));
			Sheet line4 = rh.getSheet().newInnerSheet(4, 1, new double[] {0.65}, 1, 21, 1.3, -1);
			rh.AddName(COM.ADDR, line4.cellLocationSizeAbs(1, 1)).setTextAlignToMiddleRight(); 
			rh.AddValue(COM.ADDR, line4.cellLocationSizeAbs(1, 2), new VFlds(T.ORG, ORG.COM, COM.ADDR));
			rh.AddName(COM.FAX, line4.cellLocationSizeAbs(1, 3));
			rh.AddValue(COM.FAX, line4.cellLocationSizeAbs(1, 4), new VFlds(T.ORG, ORG.COM, COM.FAX));
			Sheet line5 = rh.getSheet().newInnerSheet(5, 1, new double[] {0.65}, 1.5, 8.3, 2, 10, 2, 2, -1);
			rh.AddName(T.CODE, line5.cellLocationSizeAbs(1, 1));
			rh.AddValue(T.CODE, line5.cellLocationSizeAbs(1, 2));
			rh.AddName(T.CREATED_TIME, line5.cellLocationSizeAbs(1, 3));
			rh.AddValue(T.CREATED_TIME, line5.cellLocationSizeAbs(1, 4));
			rh.AddItem("MemoBox",  "pageCount", "共 " + GrfItem.pageCount() + "页", line5.cellLocationSizeAbs(1, 5)).setTextAlignToMiddleCenter();
			rh.AddItem("MemoBox",  "pageNumber", "第 " + GrfItem.pageNumber() + " 页", line5.cellLocationSizeAbs(1, 6)).setTextAlignToMiddleCenter();
			Sheet line6 = rh.getSheet().newInnerSheet(6, 1, new double[] {0.65}, 2, 8, 1.5, 5.8, 1.1, -1);
			rh.AddName(T.SUPNAME, line6.cellLocationSizeAbs(1, 1)).addAttr("BorderStyles", new GrfExp("[DrawLeft|DrawTop]"));
			rh.AddValue(T.SUPNAME, line6.cellLocationSizeAbs(1, 2)).addAttr("BorderStyles", new GrfExp("[DrawTop]"));
			rh.AddName(SHIP.NAME, line6.cellLocationSizeAbs(1, 3)).addAttr("BorderStyles", new GrfExp("[DrawTop]"));
			rh.AddValue(SHIP.NAME, line6.cellLocationSizeAbs(1, 4), new VFlds(T.SHIPING, SHIP.NAME)).addAttr("BorderStyles", new GrfExp("[DrawTop]"));
			rh.AddName(LINK.OF_FAX, line6.cellLocationSizeAbs(1, 5)).addAttr("BorderStyles", new GrfExp("[DrawTop]"));
			rh.AddValue(LINK.OF_FAX, line6.cellLocationSizeAbs(1, 6), new VFlds(T.SUPPLIER), tbs, personLinkFax).addAttr("BorderStyles", new GrfExp("[DrawTop|DrawRight]"));
			Sheet line7 = rh.getSheet().newInnerSheet(7, 1, new double[] {0.65}, 2.65, 7.2, 2.5, -1);
			rh.AddNameMulti(line7.cellLocationSizeAbs(1, 1), LINK.NAME, LINK.PE_MOBILE).addAttr("BorderStyles", new GrfExp("[DrawLeft|DrawBottom]"));
			rh.AddValueMulti(LINK.NAME, line7.cellLocationSizeAbs(1, 2), toLinkCode(new VFlds(T.WAREHOUSE, WAREHOUSE.DEPT), tbs, personLinkName), toLinkCode(new VFlds(T.WAREHOUSE, WAREHOUSE.DEPT), tbs, personLinkPeMobile)).addAttr("BorderStyles", new GrfExp("[DrawBottom]"));
			rh.AddNameMulti(line7.cellLocationSizeAbs(1, 3), T.BUYER, PERSON.OF_TEL).addAttr("BorderStyles", new GrfExp("[DrawBottom]"));
			rh.AddValueMulti(T.BUYER, line7.cellLocationSizeAbs(1, 4), new VFlds(T.BUYER), new VFlds(T.BUYER, BUYER.TB_OBJ, EM.PERSON, PERSON.OF_TEL)).addAttr("BorderStyles", new GrfExp("[DrawRight|DrawBottom]"));
			rh.AddItem("StaticBox", "orderlist", "订货清单:", 8, 1).add(FONT_14_BOLD).addAttr("BorderStyles", new GrfExp("[DrawBottom]"));
			
			double[] footer = new double[8];
			for(int i = 0; i < footer.length; i++)
				footer[i] = 0.65;
			Sheet fline1 = rf.getSheet().newInnerSheet(1, 1, new double[] {0.65}, 1.2, -1);
			rf.AddName(T.REM, fline1.cellLocationSizeAbs(1, 1)).addAttr("BorderStyles", new GrfExp("[DrawLeft|DrawTop|DrawBottom]"));
//			rf.AddValue(T.REM, fline1.cellLocationSizeAbs(1, 2)).addAttr("BorderStyles", new GrfExp("[DrawTop|DrawRight|DrawBottom]"));
			GrfItem rem = rf.AddValue(ORG.CODE, fline1.cellLocationSizeAbs(1, 2), new VFlds(T.ORG, ORG.CODE));
			rem.addAttr("GetDisplayTextScript", getPurRem());
			rem.addAttr("BorderStyles", new GrfExp("[DrawTop|DrawBottom]"));
			Sheet fline2t9 = rf.getSheet().newInnerSheet(2, 1, footer, 0.65, 2.15, -1);
			rf.AddItem("StaticBox", "rf1", "一、", fline2t9.cellLocationSizeAbs(1, 1));
			rf.AddItem("StaticBox", "bzyq", "包装要求:", fline2t9.cellLocationSizeAbs(1, 2));
			rf.AddItem("StaticBox", "bzyq2", " ", fline2t9.cellLocationSizeAbs(1, 3)).addAttr("BorderStyles", new GrfExp("[DrawBottom]"));
			rf.AddItem("StaticBox", "rf2", "二、", fline2t9.cellLocationSizeAbs(2, 1));
			rf.AddName(SHIP.TIME_SHIP_PLAN, fline2t9.cellLocationSizeAbs(2, 2));
			rf.AddValue(SHIP.TIME_SHIP_PLAN, fline2t9.cellLocationSizeAbs(2, 3), new VFlds(T.SHIPING, SHIP.TIME_SHIP_PLAN)).addAttr("BorderStyles", new GrfExp("[DrawBottom]"));
			rf.AddItem("StaticBox", "rf3", "三、", fline2t9.cellLocationSizeAbs(3, 1));
			rf.AddName(T.SHIPING_MODE, fline2t9.cellLocationSizeAbs(3, 2));
			rf.AddValue(T.SHIPING_MODE, fline2t9.cellLocationSizeAbs(3, 3)).addAttr("BorderStyles", new GrfExp("[DrawBottom]"));
			rf.AddItem("StaticBox", "rf4", "四、", fline2t9.cellLocationSizeAbs(4, 1));
			rf.AddName(T.REV_ADDR, fline2t9.cellLocationSizeAbs(4, 2));
			rf.AddValue(T.REV_ADDR, fline2t9.cellLocationSizeAbs(4, 3)).addAttr("BorderStyles", new GrfExp("[DrawBottom]"));
			rf.AddItem("StaticBox", "rf5", "五、", fline2t9.cellLocationSizeAbs(5, 1));
			rf.AddItem("StaticBox", "qtsx", "其他事项:", fline2t9.cellLocationSizeAbs(5, 2));
			rf.AddItem("StaticBox", "qtsx1", "1、如不能如期发货，请尽快来电告知；", fline2t9.cellLocationSizeAbs(5, 3));
			rf.AddItem("StaticBox", "qtsx2", "2、如无合约，本“订单”即作为正式合约，具有法律效力；如有合约，合约经双方签字盖章确认后即予生效，", fline2t9.cellLocationSizeAbs(6, 3));
			rf.AddItem("StaticBox", "qtsx3", "本“订单作为合约附件”；", fline2t9.cellLocationSizeAbs(7, 3));
			rf.AddItem("StaticBox", "qtsx4", "3、采购订单经双方确认后即予生效；", fline2t9.cellLocationSizeAbs(8, 3));
			Sheet fline10t12 = rf.getSheet().newInnerSheet(10, 1, new double[] {0.65, 0.65, 0.65}, 13.5, -1);
			rf.AddItem("StaticBox", "xfgz", "需方（盖章）", fline10t12.cellLocationSizeAbs(1, 1));
			rf.AddItem("StaticBox", "gfgz", "供方（盖章）", fline10t12.cellLocationSizeAbs(1, 2));
			rf.AddItem("StaticBox", "xfjbr", "经办人", fline10t12.cellLocationSizeAbs(2, 1));
			rf.AddItem("StaticBox", "gfjbr", "经办人", fline10t12.cellLocationSizeAbs(2, 2));
			rf.AddItem("StaticBox", "xfrq", "日期", fline10t12.cellLocationSizeAbs(3, 1));
			rf.AddItem("StaticBox", "gfrq", "日期", fline10t12.cellLocationSizeAbs(3, 2));
			
			
		}
		

//		@Override
//		public void initItemsGroup(GrfItemsGroup ig, GrfGroupHeader gh, GrfGroupFooter gf) {
//			super.initItemsGroup(ig, gh, gf);
//		}
		
		@Override
		public void initSheetPrint(SheetPrint sp) {
			double[] header = new double[getReportHeaderLines()];
			double[] footer = new double[12];
			for(int i=0;i<header.length;i++)
				header[i]=_mainLineHeight;
			for(int i = 0; i < footer.length; i++)
				footer[i] = 0.65;
			sp.setPageSize(SheetPrint.A4_VERTICAL);
			sp.setOriention(true);
			sp.setPageGap(new SheetGap(1, 1.5));
			sp.setPageHeader(new double[] { 0.5 }, 2, -1, 2);
			sp.setPageFooter(new double[] { 0.2 }, 2, -1, 2);
			sp.setReportHeader(header, 26.7);
			sp.setReportFooter(footer, 26.7);
			sp.setGroupHeader(new double[] {0.1}, 26.7);
			sp.setGroupFooter(new double[] {0.1}, 26.7);
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
//			getVFlds().setAlignToMiddleCenter( L.QTY);
			getVFlds().setAlignTitle(OAlign.MIDDLE_RIGHT, L.QTY, L.UOM, L.PRICE, L.AMT);
			getVFld(L.PKEY).setListName("序号");
			getVFld(L.UOM).setListName("单位");
//			getItemsColumn().AddItem_Name_Width("Sequence", 1);
//			getColumnContent().getItemsColumnContentCell().AddItem_Column_DataField("Sequence", null).addExp("FreeCell", "T")
//			.AddNode("Items Control").AddNode("Item").addExp("Type", "SystemVarBox")
//			.addAttr("Name", "SystemVarBox1").addExp("Width", "1").addExp("Height", "0.62").addExp("TextAlign", "topCenter").addExp("SystemVar", "RecordNo");
//			getColumnTitle().getItemsColumnTitleCell().AddItem_Column_Text_FreeCell("F", "Sequence", "序号", "T", OAlign.MIDDLE_CENTER);
			showTitileCellUnderLine(true);//给明细标题下面加下划线
			getColumnTitle().addAttr("RepeatStyle", new GrfExp("OnPage"));//标题部分每页重复
		}
	}
}
