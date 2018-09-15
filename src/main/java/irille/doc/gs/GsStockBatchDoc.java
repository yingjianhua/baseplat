package irille.doc.gs;

import irille.dep.gs.EGsStockBatch;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsStockBatchDoc extends DocTran {
	public static EMCrt EXT=new EGsStockBatch().crtExt();
	public static DocTran DOC=new GsStockBatchDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		DOC.p2("用户通过存货的批次代码，可以对存货的收发存情况进行批次管理,可统计某一批次所有存货的收发存情况或某一存货所有批次的收发存情况。");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
		FLD_m_stock.add(getUrl(GsStockDoc.DOC));
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_stock=DOC.getFld("m_stock");	//存货
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//批次
	public static DocFld FLD_m_cleared=DOC.getFld("m_cleared");	//结清标志
	public static DocFld FLD_m_expDate=DOC.getFld("m_expDate");	//有效(保质)期
	public static DocFld FLD_m_entryTime=DOC.getFld("m_entryTime");	//入库日期
	public static DocFld FLD_m_qty=DOC.getFld("m_qty");	//库存数量
	public static DocTb TB_m=DOC.getTb("TB_m");	//存货批次信息
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<

}
