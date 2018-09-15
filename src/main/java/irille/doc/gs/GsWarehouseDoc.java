package irille.doc.gs;

import irille.dep.gs.EGsWarehouse;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsWarehouseDoc extends DocTran {
	public static EMCrt EXT=new EGsWarehouse().crtExt();
	public static DocTran DOC=new GsWarehouseDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		//说明本类的作用和功能
		DOC.p2("核算单元是系统中独立核算的单位，机构是核算单元，要需独立核算的部门也可以" +
				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。");
		ACT_list.p("aaaaaa");
		ACT_list.addToInput("1");
		ACT_list.addToCheck("2");
		ACT_list.addToProc("3");
		ACT_list.addToOutput("4");
		FLD_m_consignees.p("dbbbbbbbbbbb");
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_del=DOC.getAct("del");	//删除
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//部门
	public static DocFld FLD_m_org=DOC.getFld("m_org");	//机构
	public static DocFld FLD_m_cell=DOC.getFld("m_cell");	//核算单元
	public static DocFld FLD_m_enabled=DOC.getFld("m_enabled");	//启用标志
	public static DocFld FLD_m_locationFlag=DOC.getFld("m_locationFlag");	//货位管理标志
	public static DocFld FLD_m_outOrder=DOC.getFld("m_outOrder");	//存货出库顺序
	public static DocFld FLD_m_consignees=DOC.getFld("m_consignees");	//收货人
	public static DocFld FLD_m_invented=DOC.getFld("m_invented");	//是否为虚拟仓库
	public static DocFld FLD_m_rem=DOC.getFld("m_rem");	//备注
	public static DocFld FLD_m_rowVersion=DOC.getFld("m_rowVersion");	//版本
	public static DocTb TB_m=DOC.getTb("TB_m");	//仓库
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<	
}
