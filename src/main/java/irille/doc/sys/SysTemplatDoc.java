/**
 * 
 */
package irille.doc.sys;

import irille.dep.sys.ESysOrg;
import irille.dep.sys.ESysTemplat;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

/**
 * @author
 * 
 */
public class SysTemplatDoc extends DocTran {
	public static EMCrt EXT=new ESysTemplat().crtExt();
	public static DocTran DOC=new SysTemplatDoc().init();

	@Override
	public void initMsg() {
		DOC.p2("单位信息：添加客户信息、机构信息和供应商信息时会自动创建对应的单位信息");
		
	}
	
	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_doEnabled=DOC.getAct("doEnabled");	//启用
	public static DocAct ACT_unEnabled=DOC.getAct("unEnabled");	//停用
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_type=DOC.getFld("m_type");	//模板类型
	public static DocFld FLD_m_code=DOC.getFld("m_code");	//代码
	public static DocFld FLD_m_year=DOC.getFld("m_year");	//启用年份
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//模板名称
	public static DocFld FLD_m_mngCell=DOC.getFld("m_mngCell");	//管理单元
	public static DocFld FLD_m_enabled=DOC.getFld("m_enabled");	//启用标志
	public static DocFld FLD_m_rem=DOC.getFld("m_rem");	//备注
	public static DocFld FLD_m_rowVersion=DOC.getFld("m_rowVersion");	//版本
	public static DocTb TB_m=DOC.getTb("TB_m");	//财务模板
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<	
}
