package irille.doc.gs;

import irille.dep.gs.EGsIn;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsInDoc extends DocTran {
	public static EMCrt EXT=new EGsIn().crtExt();
	public static DocTran DOC=new GsInDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		DOC.p2("入库单由采购入库、调拨入库、盘盈入库、拆分合并入库等业务形成。系统中的入库单都是由其它单据自动产生的，库管员不用手工录入，只有审核与弃审操作。");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
		FLD_m_status.p("初始、审核、关闭");
		FLD_m_warehouse.add(getUrl(GsWarehouseDoc.DOC));
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocAct ACT_doAppr=DOC.getAct("doAppr");	//审核
	public static DocAct ACT_unAppr=DOC.getAct("unAppr");	//弃审
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
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//名称
	public static DocFld FLD_m_origForm=DOC.getFld("m_origForm");	//源单据
	public static DocFld FLD_m_origFormNum=DOC.getFld("m_origFormNum");	//源单据号
	public static DocFld FLD_m_operator=DOC.getFld("m_operator");	//理货员
	public static DocFld FLD_m_checker=DOC.getFld("m_checker");	//检验员
	public static DocFld FLD_m_inTime=DOC.getFld("m_inTime");	//实际入库时间
	public static DocTb TB_m=DOC.getTb("TB_m");	//入库单
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<

}
