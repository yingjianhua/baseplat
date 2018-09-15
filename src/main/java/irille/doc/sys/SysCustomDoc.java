/**
 * 
 */
package irille.doc.sys;

import irille.dep.sys.ESysCom;
import irille.dep.sys.ESysCustom;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

/**
 * @author
 * 
 */
public class SysCustomDoc extends DocTran {
	public static EMCrt EXT=new ESysCustom().crtExt();
	public static DocTran DOC=new SysCustomDoc().init();

	@Override
	public void initMsg() {
		DOC.p2("客户信息:");
		ACT_ins.p("新增一条数据后，会在单位信息中对应的增加一条单位信息");
		FLD_m_comPersonFlag.p("// COM:1,单位// PERSON:2,个人");
		FLD_m_mngOrg.p("引用机构主键");
		FLD_m_mngDept.p("引用部门主键").add(getUrl(SysDeptDoc.DOC));
		FLD_m_businessMember.p("引用用户主键");
	}
	
	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_edit=DOC.getAct("edit");	//编辑
	public static DocAct ACT_doEnabled=DOC.getAct("doEnabled");	//启用
	public static DocAct ACT_unEnabled=DOC.getAct("unEnabled");	//停用
	public static DocFld FLD_m_code=DOC.getFld("m_code");	//代码
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//名称
	public static DocFld FLD_m_shortName=DOC.getFld("m_shortName");	//简称
	public static DocFld FLD_m_comPersonFlag=DOC.getFld("m_comPersonFlag");	//性质
	public static DocFld FLD_m_enabled=DOC.getFld("m_enabled");	//启用标志
	public static DocFld FLD_m_mngOrg=DOC.getFld("m_mngOrg");	//管理机构
	public static DocFld FLD_m_mngDept=DOC.getFld("m_mngDept");	//管理部门
	public static DocFld FLD_m_businessMember=DOC.getFld("m_businessMember");	//业务代表
	public static DocFld FLD_m_rowVersion=DOC.getFld("m_rowVersion");	//版本
	public static DocTb TB_m=DOC.getTb("TB_m");	//客户信息
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<	
}
