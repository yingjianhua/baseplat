package irille.doc.gs;

import irille.dep.gs.EGsDemandDirect;
import irille.doc.sys.SysCellDoc;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsDemandDirectDoc extends DocTran {
	public static EMCrt EXT=new EGsDemandDirect().crtExt();
	public static DocTran DOC=new GsDemandDirectDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		DOC.p2("直销需求");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
		FLD_m_status.p("初始、审核、关闭");
		FLD_m_cell.add(getUrl(SysCellDoc.DOC));
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_origForm=DOC.getFld("m_origForm");	//源单据
	public static DocFld FLD_m_origFormNum=DOC.getFld("m_origFormNum");	//源单据号
	public static DocFld FLD_m_poForm=DOC.getFld("m_poForm");	//供应单
	public static DocFld FLD_m_poFormNum=DOC.getFld("m_poFormNum");	//供应单号
	public static DocFld FLD_m_status=DOC.getFld("m_status");	//状态
	public static DocFld FLD_m_org=DOC.getFld("m_org");	//机构
	public static DocFld FLD_m_cell=DOC.getFld("m_cell");	//核算单元
	public static DocFld FLD_m_createdBy=DOC.getFld("m_createdBy");	//建档员
	public static DocFld FLD_m_createdTime=DOC.getFld("m_createdTime");	//建档时间
	public static DocTb TB_m=DOC.getTb("TB_m");	//直销需求
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<

}
