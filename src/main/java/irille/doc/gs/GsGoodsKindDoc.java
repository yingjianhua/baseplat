package irille.doc.gs;

import irille.dep.gs.EGsGoodsKind;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsGoodsKindDoc extends DocTran {
	public static EMCrt EXT=new EGsGoodsKind().crtExt();
	public static DocTran DOC=new GsGoodsKindDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		DOC.p2("商品分类决定该类商品的基本属性、扩展属性的类型跟数量。");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_del=DOC.getAct("del");	//删除
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_code=DOC.getFld("m_code");	//代码
	public static DocFld FLD_m_parent=DOC.getFld("m_parent");	//上级类别
	public static DocFld FLD_m_type=DOC.getFld("m_type");	//类型
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//名称
	public static DocFld FLD_m_shortkey=DOC.getFld("m_shortkey");	//快捷键
	public static DocFld FLD_m_cust1=DOC.getFld("m_cust1");	//属性名称1
	public static DocFld FLD_m_cust2=DOC.getFld("m_cust2");	//属性名称2
	public static DocFld FLD_m_cust3=DOC.getFld("m_cust3");	//属性名称3
	public static DocFld FLD_m_cust4=DOC.getFld("m_cust4");	//属性名称4
	public static DocFld FLD_m_cust5=DOC.getFld("m_cust5");	//属性名称5
	public static DocFld FLD_m_subjectAlias=DOC.getFld("m_subjectAlias");	//存货科目别名
	public static DocFld FLD_m_updateby=DOC.getFld("m_updateby");	//更新员
	public static DocFld FLD_m_updatedTime=DOC.getFld("m_updatedTime");	//更新时间
	public static DocFld FLD_m_rowVersion=DOC.getFld("m_rowVersion");	//版本
	public static DocTb TB_m=DOC.getTb("TB_m");	//货物类别
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<

}
