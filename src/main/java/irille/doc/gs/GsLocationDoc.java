package irille.doc.gs;

import irille.dep.gs.EGsLocation;
import irille.doc.sys.SysCellDoc;
import irille.pub.doc.DExample;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsLocationDoc  extends DocTran {
	public static EMCrt EXT=new EGsLocation().crtExt();
	public static DocTran DOC=new GsLocationDoc().init();
	
	//自定义信息，包括明细表，名词解释等
	public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
	public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义

	@Override
	public void initMsg() {
		DOC.p2("货位是仓库中存放商品的位置。一种商品可以存放在多个货位，一个货位也可以存放多种商品。");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
		FLD_m_warehouse.add(getUrl(GsWarehouseDoc.DOC));
	}
	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_del=DOC.getAct("del");	//删除
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_warehouse=DOC.getFld("m_warehouse");	//仓库
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//名称
	public static DocFld FLD_m_enabled=DOC.getFld("m_enabled");	//启用标志
	public static DocFld FLD_m_weight=DOC.getFld("m_weight");	//总可用重量
	public static DocFld FLD_m_weightUsed=DOC.getFld("m_weightUsed");	//已用重量
	public static DocFld FLD_m_weightAvail=DOC.getFld("m_weightAvail");	//可用重量
	public static DocFld FLD_m_valume=DOC.getFld("m_valume");	//总可用体积
	public static DocFld FLD_m_valumeUsed=DOC.getFld("m_valumeUsed");	//已用体积
	public static DocFld FLD_m_valumeAvail=DOC.getFld("m_valumeAvail");	//可用体积
	public static DocFld FLD_m_rem=DOC.getFld("m_rem");	//备注
	public static DocTb TB_m=DOC.getTb("TB_m");	//货位
	public static DocFld FLD_line_pkey=DOC.getFld("line_pkey");	//编号
	public static DocFld FLD_line_warehouse=DOC.getFld("line_warehouse");	//仓库
	public static DocFld FLD_line_name=DOC.getFld("line_name");	//名称
	public static DocFld FLD_line_enabled=DOC.getFld("line_enabled");	//启用标志
	public static DocFld FLD_line_weight=DOC.getFld("line_weight");	//总可用重量
	public static DocFld FLD_line_weightUsed=DOC.getFld("line_weightUsed");	//已用重量
	public static DocFld FLD_line_weightAvail=DOC.getFld("line_weightAvail");	//可用重量
	public static DocFld FLD_line_valume=DOC.getFld("line_valume");	//总可用体积
	public static DocFld FLD_line_valumeUsed=DOC.getFld("line_valumeUsed");	//已用体积
	public static DocFld FLD_line_valumeAvail=DOC.getFld("line_valumeAvail");	//可用体积
	public static DocFld FLD_line_rem=DOC.getFld("line_rem");	//备注
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<	
}
