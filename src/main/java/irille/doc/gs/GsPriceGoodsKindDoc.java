package irille.doc.gs;

import irille.dep.gs.EGsPriceGoodsKind;
import irille.doc.sys.SysCellDoc;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsPriceGoodsKindDoc extends DocTran {
	public static EMCrt EXT=new EGsPriceGoodsKind().crtExt();
	public static DocTran DOC=new GsPriceGoodsKindDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		DOC.p2("基础价格分类。");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
		FLD_m_price.add(getUrl(GsPriceDoc.DOC));
		FLD_m_cell.add(getUrl(SysCellDoc.DOC));
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_edit=DOC.getAct("edit");	//编辑
	public static DocAct ACT_del=DOC.getAct("del");	//删除
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocAct ACT_doEnabled=DOC.getAct("doEnabled");	//启用
	public static DocAct ACT_unEnabled=DOC.getAct("unEnabled");	//停用
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_code=DOC.getFld("m_code");	//代码
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//名称
	public static DocFld FLD_m_price=DOC.getFld("m_price");	//定价名称
	public static DocFld FLD_m_priceOrig=DOC.getFld("m_priceOrig");	//定价基数来源
	public static DocFld FLD_m_enabled=DOC.getFld("m_enabled");	//启用标志
	public static DocFld FLD_m_rate1=DOC.getFld("m_rate1");	//利润率1(%)
	public static DocFld FLD_m_rate2=DOC.getFld("m_rate2");	//利润率2(%)
	public static DocFld FLD_m_rate3=DOC.getFld("m_rate3");	//利润率3(%)
	public static DocFld FLD_m_rate4=DOC.getFld("m_rate4");	//利润率4(%)
	public static DocFld FLD_m_rate5=DOC.getFld("m_rate5");	//利润率5(%)
	public static DocFld FLD_m_rate6=DOC.getFld("m_rate6");	//利润率6(%)
	public static DocFld FLD_m_rate7=DOC.getFld("m_rate7");	//利润率7(%)
	public static DocFld FLD_m_rate8=DOC.getFld("m_rate8");	//利润率8(%)
	public static DocFld FLD_m_rate9=DOC.getFld("m_rate9");	//利润率9(%)
	public static DocFld FLD_m_rate10=DOC.getFld("m_rate10");	//利润率10(%)
	public static DocFld FLD_m_rate11=DOC.getFld("m_rate11");	//利润率11(%)
	public static DocFld FLD_m_rate12=DOC.getFld("m_rate12");	//利润率12(%)
	public static DocFld FLD_m_rangeType=DOC.getFld("m_rangeType");	//可视范围
	public static DocFld FLD_m_rangePkey=DOC.getFld("m_rangePkey");	//可视对象主键值
	public static DocFld FLD_m_cell=DOC.getFld("m_cell");	//管理核算单元
	public static DocTb TB_m=DOC.getTb("TB_m");	//基础价格分类
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<

}
