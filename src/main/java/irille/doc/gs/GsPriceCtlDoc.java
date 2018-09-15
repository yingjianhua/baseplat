package irille.doc.gs;

import irille.dep.gs.EGsPriceCtl;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsPriceCtlDoc extends DocTran {
	public static EMCrt EXT=new EGsPriceCtl().crtExt();
	public static DocTran DOC=new GsPriceCtlDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		DOC.p2("定价控制。");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
		FLD_m_price.add(getUrl(GsPriceDoc.DOC));
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_del=DOC.getAct("del");	//删除
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_price=DOC.getFld("m_price");	//定价名称
	public static DocFld FLD_m_retailLevel=DOC.getFld("m_retailLevel");	//默认零售价格级别
	public static DocFld FLD_m_lowestLevel=DOC.getFld("m_lowestLevel");	//默认最低零售价格级别
	public static DocFld FLD_m_tradeLevel=DOC.getFld("m_tradeLevel");	//默认批发价格级别
	public static DocFld FLD_m_mvLevel=DOC.getFld("m_mvLevel");	//默认调拨价格级别
	public static DocTb TB_m=DOC.getTb("TB_m");	//定价控制
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<

}
