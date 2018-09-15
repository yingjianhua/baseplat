package irille.doc.gs;

import irille.dep.gs.EGsStock;
import irille.doc.sys.SysCellDoc;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsStockDoc extends DocTran {
	public static EMCrt EXT=new EGsStock().crtExt();
	public static DocTran DOC=new GsStockDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		DOC.p2("本功能完成对存货目录的设立和管理。");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
		FLD_m_warehouse.add(getUrl(GsWarehouseDoc.DOC));
		FLD_m_goods.add(getUrl(GsGoodsDoc.DOC));
		FLD_m_location.add(getUrl(GsLocationDoc.DOC));
		FLD_m_cell.add(getUrl(SysCellDoc.DOC));
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_warehouse=DOC.getFld("m_warehouse");	//仓库
	public static DocFld FLD_m_goods=DOC.getFld("m_goods");	//货物
	public static DocFld FLD_m_qty=DOC.getFld("m_qty");	//库存数量
	public static DocFld FLD_m_location=DOC.getFld("m_location");	//货位
	public static DocFld FLD_m_enrouteQty=DOC.getFld("m_enrouteQty");	//在途数量
	public static DocFld FLD_m_lockedQty=DOC.getFld("m_lockedQty");	//存货锁定数量
	public static DocFld FLD_m_lowestQty=DOC.getFld("m_lowestQty");	//最低库存
	public static DocFld FLD_m_safetyQty=DOC.getFld("m_safetyQty");	//安全库存
	public static DocFld FLD_m_limitQty=DOC.getFld("m_limitQty");	//上限库存
	public static DocFld FLD_m_purLeadDays=DOC.getFld("m_purLeadDays");	//采购提前天数
	public static DocFld FLD_m_enabled=DOC.getFld("m_enabled");	//启用标志
	public static DocFld FLD_m_cell=DOC.getFld("m_cell");	//核算单元
	public static DocTb TB_m=DOC.getTb("TB_m");	//存货
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<

}
