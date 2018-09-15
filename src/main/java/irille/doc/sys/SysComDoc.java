/**
 * 
 */
package irille.doc.sys;

import irille.dep.sys.ESysCom;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

/**
 * @author
 * 
 */
public class SysComDoc extends DocTran {
	public static EMCrt EXT=new ESysCom().crtExt();
	public static DocTran DOC=new SysComDoc().init();

	@Override
	public void initMsg() {
		DOC.p2("单位信息：添加客户信息、机构信息和供应商信息时会自动创建对应的单位信息");
		FLD_m_name.p("企业客户的名称必须唯一\t");
	}
	
	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//机构名称
	public static DocFld FLD_m_shortName=DOC.getFld("m_shortName");	//机构简称
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
	public static DocFld FLD_m_rowVersion=DOC.getFld("m_rowVersion");	//版本
	public static DocTb TB_m=DOC.getTb("TB_m");	//单位信息
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<	
}
