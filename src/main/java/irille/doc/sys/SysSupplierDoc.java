/**
 * 
 */
package irille.doc.sys;

import irille.dep.sys.ESysOrg;
import irille.dep.sys.ESysSupplier;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

/**
 * @author
 * 
 */
public class SysSupplierDoc extends DocTran {
	public static EMCrt EXT=new ESysSupplier().crtExt();
	public static DocTran DOC=new SysSupplierDoc().init();
	@Override
	public void initMsg() {
		DOC.p2("单位信息：添加客户信息、机构信息和供应商信息时会自动创建对应的单位信息");
		
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
	public static DocFld FLD_m_pyaAmt=DOC.getFld("m_pyaAmt");	//应付款
	public static DocFld FLD_m_code=DOC.getFld("m_code");	//代码
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//名称
	public static DocFld FLD_m_shortName=DOC.getFld("m_shortName");	//简称
	public static DocFld FLD_m_comPersonFlag=DOC.getFld("m_comPersonFlag");	//性质
	public static DocFld FLD_m_enabled=DOC.getFld("m_enabled");	//启用标志
	public static DocFld FLD_m_mngOrg=DOC.getFld("m_mngOrg");	//管理机构
	public static DocFld FLD_m_mngDept=DOC.getFld("m_mngDept");	//管理部门
	public static DocFld FLD_m_businessMember=DOC.getFld("m_businessMember");	//业务代表
	public static DocFld FLD_m_rowVersion=DOC.getFld("m_rowVersion");	//版本
	public static DocFld FLD_m_tel1=DOC.getFld("m_tel1");	//电话1
	public static DocFld FLD_m_tel2=DOC.getFld("m_tel2");	//电话2
	public static DocFld FLD_m_fax=DOC.getFld("m_fax");	//传真
	public static DocFld FLD_m_website=DOC.getFld("m_website");	//网址
	public static DocFld FLD_m_addr=DOC.getFld("m_addr");	//地址
	public static DocFld FLD_m_zipCode=DOC.getFld("m_zipCode");	//邮编
	public static DocFld FLD_m_rem=DOC.getFld("m_rem");	//备注
	public static DocFld FLD_m_updatedBy=DOC.getFld("m_updatedBy");	//更新员
	public static DocFld FLD_m_updatedDateTime=DOC.getFld("m_updatedDateTime");	//更新时间
	public static DocFld FLD_m_createdBy=DOC.getFld("m_createdBy");	//建档员
	public static DocFld FLD_m_createdDateTime=DOC.getFld("m_createdDateTime");	//建档时间
	public static DocTb TB_m=DOC.getTb("TB_m");	//供应商信息
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<	
}
