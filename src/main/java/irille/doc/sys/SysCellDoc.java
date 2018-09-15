/**
 * 
 */
package irille.doc.sys;

import irille.dep.sys.ESysCell;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

/**
 * @author
 * 
 */
public class SysCellDoc extends DocTran {
	public static EMCrt EXT=new ESysCell().crtExt();
	public static DocTran DOC=new SysCellDoc().init();
	
	//自定义信息，包括明细表，名词解释等
	public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
	public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	public static  DocImg IMG=DOC.newImg("测试", ""); //名词定义

	@Override
	public void initMsg() {
		DOC.p2("核算单元信息的管理。");
		NOUN_cell.p2("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。");
//		FLD_m_org.p("核算单元的所属机构。");
	}
	
	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_del=DOC.getAct("del");	//删除
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_code=DOC.getFld("m_code");	//代码
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//名称
	public static DocFld FLD_m_year=DOC.getFld("m_year");	//年份
	public static DocFld FLD_m_org=DOC.getFld("m_org");	//机构
	public static DocFld FLD_m_dept=DOC.getFld("m_dept");	//部门
	public static DocFld FLD_m_templat=DOC.getFld("m_templat");	//财务模板
	public static DocFld FLD_m_rowVersion=DOC.getFld("m_rowVersion");	//版本
	public static DocTb TB_m=DOC.getTb("TB_m");	//核算单元
	public static DocFld FLD_line_pkey=DOC.getFld("line_pkey");	//编号
	public static DocFld FLD_line_code=DOC.getFld("line_code");	//代码
	public static DocFld FLD_line_name=DOC.getFld("line_name");	//名称
	public static DocFld FLD_line_year=DOC.getFld("line_year");	//年份
	public static DocFld FLD_line_org=DOC.getFld("line_org");	//机构
	public static DocFld FLD_line_dept=DOC.getFld("line_dept");	//部门
	public static DocFld FLD_line_templat=DOC.getFld("line_templat");	//财务模板
	public static DocFld FLD_line_rowVersion=DOC.getFld("line_rowVersion");	//版本
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<	
}
