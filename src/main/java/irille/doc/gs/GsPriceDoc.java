package irille.doc.gs;

import irille.dep.gs.EGsPrice;
import irille.doc.sys.SysCellDoc;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsPriceDoc extends DocTran {
	public static EMCrt EXT=new EGsPrice().crtExt();
	public static DocTran DOC=new GsPriceDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		DOC.p2("定价名称");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
		FLD_m_cell.add(getUrl(SysCellDoc.DOC));
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_del=DOC.getAct("del");	//删除
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//名称
	public static DocFld FLD_m_namePrice1=DOC.getFld("m_namePrice1");	//价格名称1
	public static DocFld FLD_m_namePrice2=DOC.getFld("m_namePrice2");	//价格名称2
	public static DocFld FLD_m_namePrice3=DOC.getFld("m_namePrice3");	//价格名称3
	public static DocFld FLD_m_namePrice4=DOC.getFld("m_namePrice4");	//价格名称4
	public static DocFld FLD_m_namePrice5=DOC.getFld("m_namePrice5");	//价格名称5
	public static DocFld FLD_m_namePrice6=DOC.getFld("m_namePrice6");	//价格名称6
	public static DocFld FLD_m_namePrice7=DOC.getFld("m_namePrice7");	//价格名称7
	public static DocFld FLD_m_namePrice8=DOC.getFld("m_namePrice8");	//价格名称8
	public static DocFld FLD_m_namePrice9=DOC.getFld("m_namePrice9");	//价格名称9
	public static DocFld FLD_m_namePrice10=DOC.getFld("m_namePrice10");	//价格名称10
	public static DocFld FLD_m_namePrice11=DOC.getFld("m_namePrice11");	//价格名称11
	public static DocFld FLD_m_namePrice12=DOC.getFld("m_namePrice12");	//价格名称12
	public static DocFld FLD_m_rangeType=DOC.getFld("m_rangeType");	//可视范围
	public static DocFld FLD_m_rangePkey=DOC.getFld("m_rangePkey");	//可视对象主键值
	public static DocFld FLD_m_cell=DOC.getFld("m_cell");	//管理核算单元
	public static DocTb TB_m=DOC.getTb("TB_m");	//定价名称
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<

}
