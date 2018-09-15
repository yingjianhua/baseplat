package irille.doc.gs;

import irille.dep.gs.EGsUomType;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsUomTypeDoc extends DocTran {
	public static EMCrt EXT=new EGsUomType().crtExt();
	public static DocTran DOC=new GsUomTypeDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		DOC.p2("通过不同的模板可以定义多套同名的计量单位类型。相同计量单位类型的计量单位之间可以通过指定的转换率进行转换;单位类型明细实质上与交易计量单位是相同的，而交易计量单位只是独立于计量单位类型操作而已");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//名称
	public static DocFld FLD_m_shortkey=DOC.getFld("m_shortkey");	//快捷键
	public static DocFld FLD_m_enabled=DOC.getFld("m_enabled");	//启用标志
	public static DocFld FLD_m_rem=DOC.getFld("m_rem");	//备注
	public static DocTb TB_m=DOC.getTb("TB_m");	//计量单位类型
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<

}
