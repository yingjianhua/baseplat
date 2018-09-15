package irille.doc.gs;

import irille.dep.gs.EGsPhyinv;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsPhyinvDoc extends DocTran {
	public static EMCrt EXT = new EGsPhyinv().crtExt();
	public static DocTran DOC = new GsPhyinvDoc().init();

	// 自定义信息，包括明细表，名词解释等
	// public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());
	// //明细表
	// public static DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义

	@Override
	public void initMsg() {
		DOC.p2("企业的存货品种多、收发频繁，在日常存货收发、保管过程中，由于计量错误、检验疏忽、管理不善、自然损耗、核算错误以及偷窃、贪污等原因，有时会发生存货的盘盈、盘亏和毁损现象，从而造成存货账实不相符。为了保护企业流动资产的安全和完整，做到账实相符，企业必须对存货进行定期或不定期的清查。确定企业各种存货的实际库存量，并与账面记录相核对，查明存货盘盈、盘亏和毁损的数量以及造成的原因，并据以编制存货盘点报告表，按规定程序，报有关部门审批。存货盘盈、盘亏和毁损，在查明原因、分清责任、按规定程序报经有关部门批准后，应进行相应的账务处理，调整存货账的实存数，使存货的账面记录与库存实物核对相符。");

		// NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
		// "设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
		// FLD_m_org.p("核算单元的所属机构。");
		 FLD_m_status.p("初始、审核、关闭");
		 FLD_m_warehouse.add(getUrl(GsWarehouseDoc.DOC));
	}

	// >>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_del=DOC.getAct("del");	//删除
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocAct ACT_doAppr=DOC.getAct("doAppr");	//审核
	public static DocAct ACT_unAppr=DOC.getAct("unAppr");	//弃审
	public static DocAct ACT_inv=DOC.getAct("inv");	//盘点
	public static DocAct ACT_produce=DOC.getAct("produce");	//产生
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_code=DOC.getFld("m_code");	//单据号
	public static DocFld FLD_m_status=DOC.getFld("m_status");	//状态
	public static DocFld FLD_m_org=DOC.getFld("m_org");	//机构
	public static DocFld FLD_m_dept=DOC.getFld("m_dept");	//部门
	public static DocFld FLD_m_createdBy=DOC.getFld("m_createdBy");	//建档员
	public static DocFld FLD_m_createdTime=DOC.getFld("m_createdTime");	//建档时间
	public static DocFld FLD_m_apprBy=DOC.getFld("m_apprBy");	//审核
	public static DocFld FLD_m_apprTime=DOC.getFld("m_apprTime");	//审核时间
	public static DocFld FLD_m_rem=DOC.getFld("m_rem");	//备注
	public static DocFld FLD_m_warehouse=DOC.getFld("m_warehouse");	//仓库
	public static DocFld FLD_m_planFiniDate=DOC.getFld("m_planFiniDate");	//预计完成日期
	public static DocFld FLD_m_finiTime=DOC.getFld("m_finiTime");	//完成时间
	public static DocFld FLD_m_unmatchNum=DOC.getFld("m_unmatchNum");	//不符品种数
	public static DocFld FLD_m_unmatchAmt=DOC.getFld("m_unmatchAmt");	//金额差额
	public static DocFld FLD_m_countedBy=DOC.getFld("m_countedBy");	//盘点员
	public static DocTb TB_m=DOC.getTb("TB_m");	//盘点单
	// <<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<
}
