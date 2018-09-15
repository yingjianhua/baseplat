package irille.doc.gs;

import irille.dep.gs.EGsGoodsCmb;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsGoodsCmbDoc extends DocTran {
	public static EMCrt EXT=new EGsGoodsCmb().crtExt();
	public static DocTran DOC=new GsGoodsCmbDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		DOC.p2("组合套件定义。");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
		FLD_m_goods.add(getUrl(GsGoodsDoc.DOC));
		FLD_m_innerGoods.add(getUrl(GsGoodsDoc.DOC));
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_del=DOC.getAct("del");	//删除
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_goods=DOC.getFld("m_goods");	//货物
	public static DocFld FLD_m_sort=DOC.getFld("m_sort");	//排序号
	public static DocFld FLD_m_innerGoods=DOC.getFld("m_innerGoods");	//内部货物
	public static DocFld FLD_m_innerCount=DOC.getFld("m_innerCount");	//内部货物数量
	public static DocTb TB_m=DOC.getTb("TB_m");	//组合套件定义
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<

}
