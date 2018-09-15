/**
 * 
 */
package irille.doc.sys;

import irille.dep.sys.ESysEm;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

/**
 * @author
 * 
 */
public class SysEmDoc extends DocTran {
	public static EMCrt EXT=new ESysEm().crtExt();
	public static DocTran DOC=new SysEmDoc().init();

	@Override
	public void initMsg() {
		DOC.p2("单位信息：添加客户信息、机构信息和供应商信息时会自动创建对应的单位信息");
		
	}
	
	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_code=DOC.getFld("m_code");	//工号
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//名称
	public static DocFld FLD_m_nickname=DOC.getFld("m_nickname");	//昵称
	public static DocFld FLD_m_state=DOC.getFld("m_state");	//职员状态
	public static DocFld FLD_m_org=DOC.getFld("m_org");	//机构
	public static DocFld FLD_m_dept=DOC.getFld("m_dept");	//部门
	public static DocFld FLD_m_person=DOC.getFld("m_person");	//个人信息
	public static DocFld FLD_m_peCardType=DOC.getFld("m_peCardType");	//个人证件类型
	public static DocFld FLD_m_peCardNumb=DOC.getFld("m_peCardNumb");	//个人证件号码
	public static DocFld FLD_m_peMobile=DOC.getFld("m_peMobile");	//个人常用手机
	public static DocFld FLD_m_peEmail=DOC.getFld("m_peEmail");	//个人邮箱
	public static DocFld FLD_m_peWx=DOC.getFld("m_peWx");	//个人微信
	public static DocFld FLD_m_peQq=DOC.getFld("m_peQq");	//个人QQ
	public static DocFld FLD_m_peSex=DOC.getFld("m_peSex");	//个人性别
	public static DocFld FLD_m_peBirthday=DOC.getFld("m_peBirthday");	//个人出生日期
	public static DocFld FLD_m_peMerry=DOC.getFld("m_peMerry");	//个人婚姻状况
	public static DocFld FLD_m_hoTel=DOC.getFld("m_hoTel");	//家庭电话
	public static DocFld FLD_m_hoAddr=DOC.getFld("m_hoAddr");	//家庭地址
	public static DocFld FLD_m_hoZipCode=DOC.getFld("m_hoZipCode");	//家庭邮编
	public static DocFld FLD_m_rem=DOC.getFld("m_rem");	//备注
	public static DocFld FLD_m_updatedBy=DOC.getFld("m_updatedBy");	//更新员
	public static DocFld FLD_m_updatedDateTime=DOC.getFld("m_updatedDateTime");	//更新时间
	public static DocFld FLD_m_createdBy=DOC.getFld("m_createdBy");	//建档员
	public static DocFld FLD_m_createdDateTime=DOC.getFld("m_createdDateTime");	//建档时间
	public static DocFld FLD_m_rowVersion=DOC.getFld("m_rowVersion");	//版本
	public static DocFld FLD_m_loginName=DOC.getFld("m_loginName");	//登录账号
	public static DocTb TB_m=DOC.getTb("TB_m");	//职员信息
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<	
}
