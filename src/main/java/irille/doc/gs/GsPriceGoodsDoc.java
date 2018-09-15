package irille.doc.gs;

import irille.dep.gs.EGsPriceGoods;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsPriceGoodsDoc extends DocTran {
	public static EMCrt EXT=new EGsPriceGoods().crtExt();
	public static DocTran DOC=new GsPriceGoodsDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		DOC.p2("基础价格信息。");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
		FLD_m_goods.add(getUrl(GsGoodsDoc.DOC));
		FLD_m_priceKind.add(getUrl(GsPriceGoodsKindDoc.DOC));
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_del=DOC.getAct("del");	//删除
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocAct ACT_doEnabled=DOC.getAct("doEnabled");	//启用
	public static DocAct ACT_unEnabled=DOC.getAct("unEnabled");	//停用
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_goods=DOC.getFld("m_goods");	//货物
	public static DocFld FLD_m_priceKind=DOC.getFld("m_priceKind");	//基础价格分类
	public static DocFld FLD_m_priceCost=DOC.getFld("m_priceCost");	//定价基数
	public static DocFld FLD_m_price1=DOC.getFld("m_price1");	//价格1
	public static DocFld FLD_m_price2=DOC.getFld("m_price2");	//价格2
	public static DocFld FLD_m_price3=DOC.getFld("m_price3");	//价格3
	public static DocFld FLD_m_price4=DOC.getFld("m_price4");	//价格4
	public static DocFld FLD_m_price5=DOC.getFld("m_price5");	//价格5
	public static DocFld FLD_m_price6=DOC.getFld("m_price6");	//价格6
	public static DocFld FLD_m_price7=DOC.getFld("m_price7");	//价格7
	public static DocFld FLD_m_price8=DOC.getFld("m_price8");	//价格8
	public static DocFld FLD_m_price9=DOC.getFld("m_price9");	//价格9
	public static DocFld FLD_m_price10=DOC.getFld("m_price10");	//价格10
	public static DocFld FLD_m_price11=DOC.getFld("m_price11");	//价格11
	public static DocFld FLD_m_price12=DOC.getFld("m_price12");	//价格12
	public static DocFld FLD_m_enabled=DOC.getFld("m_enabled");	//启用标志
	public static DocTb TB_m=DOC.getTb("TB_m");	//基础价格信息
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<

}
