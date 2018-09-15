package irille.doc.gs;

import irille.dep.gs.EGsStockStimate;
import irille.gl.gs.GsUom;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsStockStimateDoc extends DocTran {
	public static EMCrt EXT=new EGsStockStimate().crtExt();
	public static DocTran DOC=new GsStockStimateDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		DOC.p2("库存锁定的产生。主要的信息来源：采购退货单、调拨单、保留单、销售单、赠送单等单据。");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
		FLD_m_warehouse.add(getUrl(GsWarehouseDoc.DOC));
		FLD_m_goods.add(getUrl(GsGoodsDoc.DOC));
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_warehouse=DOC.getFld("m_warehouse");	//仓库
	public static DocFld FLD_m_goods=DOC.getFld("m_goods");	//货物
	public static DocFld FLD_m_enrouteType=DOC.getFld("m_enrouteType");	//预计出入库货物类别
	public static DocFld FLD_m_uom=DOC.getFld("m_uom");	//计量单位
	public static DocFld FLD_m_qty=DOC.getFld("m_qty");	//数量
	public static DocFld FLD_m_planDate=DOC.getFld("m_planDate");	//预计到货出货日期
	public static DocFld FLD_m_origForm=DOC.getFld("m_origForm");	//源单据
	public static DocFld FLD_m_origFormNum=DOC.getFld("m_origFormNum");	//源单据号
	public static DocFld FLD_m_createdDate=DOC.getFld("m_createdDate");	//建档日期
	public static DocTb TB_m=DOC.getTb("TB_m");	//预计出入库量登记表
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<

}
