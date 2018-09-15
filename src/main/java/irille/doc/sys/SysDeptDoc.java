/**
 * 
 */
package irille.doc.sys;


import irille.dep.sys.ESysDept;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

/**
 * @author
 * 
 */
public class SysDeptDoc extends DocTran {
	public static EMCrt EXT=new ESysDept().crtExt();
	public static DocTran DOC=new SysDeptDoc().init();

	@Override
	public void initMsg() {
		DOC.p2("部门信息");
		
	}
	
	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_del=DOC.getAct("del");	//删除
	public static DocAct ACT_edit=DOC.getAct("edit");	//联系人
	public static DocAct ACT_doEnabled=DOC.getAct("doEnabled");	//启用
	public static DocAct ACT_unEnabled=DOC.getAct("unEnabled");	//停用
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_code=DOC.getFld("m_code");	//代码
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//部门名称
	public static DocFld FLD_m_enabled=DOC.getFld("m_enabled");	//启用标志
	public static DocFld FLD_m_org=DOC.getFld("m_org");	//所属机构
	public static DocFld FLD_m_manager=DOC.getFld("m_manager");	//部门负责人
	public static DocFld FLD_m_deptUp=DOC.getFld("m_deptUp");	//上级部门
	public static DocFld FLD_m_cell=DOC.getFld("m_cell");	//核算单元
	public static DocFld FLD_m_rem=DOC.getFld("m_rem");	//备注
	public static DocFld FLD_m_rowVersion=DOC.getFld("m_rowVersion");	//版本
	public static DocTb TB_m=DOC.getTb("TB_m");	//部门信息
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<	
}
