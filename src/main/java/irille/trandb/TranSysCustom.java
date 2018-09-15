package irille.trandb;

import irille.core.sys.SysCom;
import irille.core.sys.SysCustom;
import irille.core.sys.SysCustomDAO;
import irille.core.sys.SysCustomOrg;
import irille.core.sys.SysOrg;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OLinkType;
import irille.pub.bean.Bean;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 * @author LYL
 *         2015-7-15 10:04:07
 *         迁移数据: `crm_cust`、`base_person`
 *         迁移条件: `code` NOT REGEXP '^(11001|11009|11010|11011|11012)'
 */
public class TranSysCustom {
	public static void run() throws Exception {
		DelAndAdd();
		runCustAndPerson();
	}

	/**
	 * 客户表(crm_cust)迁移分析:
	 * 客户表和个人表(base_person)是关联的,
	 * 迁移时根据客户编号132拼接上code在base_person中查询,如"132-11001-1"
	 * 在runPersonLink()方法中查询并对应的添加到新数据库的sys_person_link中
	 * 注意!!crm_cust中org字段有错数据所以暂定使用code中的org代码
	 * @throws Exception
	 */
	public static void runCustAndPerson() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `crm_cust` WHERE `code` NOT REGEXP '^(11001|11009|11010|11011|11012)'";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("=====================数据迁移开始===================");
		System.out.println("=============迁移【客户表和单位信息迁移】============");
		int num = 0;
		while (rs.next()) {
			String code = rs.getString("code");
			String name = rs.getString("name");
			String org = code.substring(0, 5);
			SysOrg orgObj = SysOrg.chkUniqueCode(false, org);
			int operator = rs.getInt("operator");
			SysUser operatorObj = SysUser.chkUniqueCode(false, operator + "");
			Date createdDateTime = rs.getDate("created_date");
			SysCustom custom = SysCustom.chkUniqueCode(false, code);
			if (custom == null) {
				custom = new SysCustom().init();
				custom.setCode(code);
				custom.setName(name);
				if (orgObj != null) {
					custom.setMngOrg(orgObj.getPkey());
				} else {
					System.err.println("不存在的机构[" + org + "]");
				}
				if (SysCom.chkUniqueName(false, custom.getName()) != null) {
					System.err.println("单位信息[" + custom.getName() + "]已存在");
					custom.setName(custom.getName() + num);
					num++;
				}
				if (operatorObj != null) {
					custom.setBusinessMember(operatorObj.getPkey());
				} else {
					custom.setBusinessMember(1);
				}
				SysCustomDAO.Ins customins = new SysCustomDAO.Ins();
				customins.setB(custom);
				customins._com = new SysCom().init();
				customins.commit();
				runPersonLink(custom);
				runCustomOrg(custom);
			} else {
				System.err.println("客户信息[" + code + "]已存在");
			}
		}

		System.out.println("==================数据迁移完成==================");
		TranDb.INST.close(stat, rs);
	}

	public static void runPersonLink(SysCustom custom) throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM base_person where owner_form_numb = " + "'132-" + custom.getCode() + "'";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		while (rs.next()) {
			String name = rs.getString("name");
			byte sex = rs.getByte("gender");
			byte certificate_type = rs.getByte("certificate_type");
			String certificate_numb = rs.getString("certificate_numb");
			String mobile = rs.getString("mobile");
			String http = rs.getString("http");
			String email = rs.getString("email");
			String tel1 = rs.getString("tel1");
			String fax = rs.getString("fax");
			String address = rs.getString("address");
			String zip_code = rs.getString("zip_code");
			String remark = rs.getString("remark");
			SysPersonLink personLink = new SysPersonLink();
			personLink.setTbObjLong(custom.gtLongPkey());
			personLink.setName(name);
			personLink.setPeSex(sex);
			personLink.setOfAddr(address);
			if (certificate_numb != null && certificate_numb.length() <= 20) {
				personLink.setPeCardNumb(certificate_numb);
			}
			personLink.setPeCardType(certificate_type);
			if (mobile != null && mobile.length() <= 20) {
				personLink.setPeMobile(mobile);
			}
			personLink.setPeEmail(email);
			if (tel1 != null && tel1.length() <= 20) {
				personLink.setOfTel(tel1);
			}
			if (fax != null && fax.length() <= 20) {
				personLink.setOfFax(fax);
			}
			if (zip_code != null && zip_code.length() <= 6) {
				personLink.setOfZipCode(zip_code);
			}
			personLink.setRem(remark);
			personLink.stType(OLinkType.SAL);
			personLink.setCreatedBy(1);
			personLink.setCreatedDateTime(new Date());
			personLink.setUpdatedBy(1);
			personLink.setUpdatedDateTime(new Date());
			personLink.ins();
		}
		TranDb.INST.close(stat, rs);
	}

	public static void runCustomOrg(SysCustom custom) throws Exception {
		SysCustomOrg customOrg = new SysCustomOrg();
		customOrg.setCustom(custom.getPkey());
		customOrg.setOrg(custom.getMngOrg());
		if (custom.getBusinessMember().intValue() != 1 && custom.gtBusinessMember().getOrg().equals(custom.getMngOrg()))
			customOrg.setDept(custom.gtBusinessMember().getDept());
		customOrg.ins();
	}

	public static void DelAndAdd() throws Exception {
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("DROP TABLE " + Bean.tb(SysCustom.class).getCodeSqlTb());
		stmt.execute("DROP TABLE " + Bean.tb(SysCustomOrg.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(SysCustom.class).getCodeSqlTb() + "]-->成功!");
		System.out.println("删除表[" + Bean.tb(SysCustomOrg.class).getCodeSqlTb() + "]-->成功!");
		stmt.close();
		new DbMysql().db(SysCustom.class);
		new DbMysql().db(SysCustomOrg.class);
	}
}
