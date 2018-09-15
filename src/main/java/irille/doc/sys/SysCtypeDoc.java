/**
 * 
 */
package irille.doc.sys;

import irille.dep.sys.ESysCtype;
import irille.dep.sys.ESysOrg;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

/**
 * @author
 * 
 */
public class SysCtypeDoc extends DocTran {
	public static EMCrt EXT=new ESysCtype().crtExt();
	public static DocTran DOC=new SysCtypeDoc().init();

	@Override
	public void initMsg() {
		DOC.p2("单位信息：添加客户信息、机构信息和供应商信息时会自动创建对应的单位信息");
		
	}
	
	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_edit=DOC.getAct("edit");	//编辑
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_ctypeName=DOC.getFld("m_ctypeName");	//名称
	public static DocFld FLD_m_ctypeDes=DOC.getFld("m_ctypeDes");	//描述
	public static DocFld FLD_m_ctypeLen=DOC.getFld("m_ctypeLen");	//代码长度
	public static DocFld FLD_m_rowVersion=DOC.getFld("m_rowVersion");	//版本
	public static DocTb TB_m=DOC.getTb("TB_m");	//系统参数分类
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<	
}
