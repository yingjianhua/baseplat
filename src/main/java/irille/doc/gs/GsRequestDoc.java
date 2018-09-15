package irille.doc.gs;

import irille.dep.gs.EGsRequest;
import irille.doc.sys.SysCellDoc;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsRequestDoc extends DocTran {
	public static EMCrt EXT=new EGsRequest().crtExt();
	public static DocTran DOC=new GsRequestDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		DOC.p2("当销售人员收到客户定货时，发现仓库存货不够的话就可以开请购单向采购部发起请购要求。采购部同意采购审核后仓库就会增加所请购存货的在途数量，销售员就可以开先销售单了。");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
		FLD_m_status.p("初始、审核、关闭");
		FLD_m_warehouse.add(getUrl(GsWarehouseDoc.DOC));
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_del=DOC.getAct("del");	//删除
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
	public static DocTb TB_m=DOC.getTb("TB_m");	//请购单
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<

}
