/**
 * 
 */
package irille.doc.sys;

import irille.dep.sys.ESysOrg;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

/**
 * @author
 * 
 */
public class SysOrgDoc extends DocTran {
	public static EMCrt EXT=new ESysOrg().crtExt();
	public static DocTran DOC=new SysOrgDoc().init();

	@Override
	public void initMsg() {
		DOC.p2("单位信息：添加客户信息、机构信息和供应商信息时会自动创建对应的单位信息");
		
	}
	
	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_del=DOC.getAct("del");	//删除
	public static DocAct ACT_doEnabled=DOC.getAct("doEnabled");	//启用
	public static DocAct ACT_unEnabled=DOC.getAct("unEnabled");	//停用
	public static DocAct ACT_ded=DOC.getAct("ded");	//日终处理
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_code=DOC.getFld("m_code");	//代码
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//机构名称
	public static DocFld FLD_m_shortName=DOC.getFld("m_shortName");	//机构简称
	public static DocFld FLD_m_enabled=DOC.getFld("m_enabled");	//启用标志
	public static DocFld FLD_m_orgUp=DOC.getFld("m_orgUp");	//上级机构
	public static DocFld FLD_m_workDate=DOC.getFld("m_workDate");	//工作日期
	public static DocFld FLD_m_state=DOC.getFld("m_state");	//机构状态
	public static DocFld FLD_m_templat=DOC.getFld("m_templat");	//科目模板
	public static DocFld FLD_m_valuationMethods=DOC.getFld("m_valuationMethods");	//存货计价方式
	public static DocFld FLD_m_internationTrade=DOC.getFld("m_internationTrade");	//是否有国际贸易
	public static DocFld FLD_m_currency=DOC.getFld("m_currency");	//币种
	public static DocFld FLD_m_rowVersion=DOC.getFld("m_rowVersion");	//版本
	public static DocFld FLD_m_com=DOC.getFld("m_com");	//单位信息
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
	public static DocTb TB_m=DOC.getTb("TB_m");	//机构信息
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<	
}
