package irille.trandb;

import irille.core.sys.SysCell;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysCom;
import irille.core.sys.SysDept;
import irille.core.sys.SysDeptDAO;
import irille.core.sys.SysOrg;
import irille.core.sys.SysOrgDAO;
import irille.core.sys.SysPersonLink;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 * @author LYL
 *         2015-7-9 14:13:45
 *         迁移:base_org、base_dept
 *         机构迁移条件: `code` NOT IN ('11001','11009','11010','11011','11012');
 *         部门迁移条件: `org` NOT IN ('11001','11009','11010','11011','11012') AND `enabled` <> 2
 *         相关的表:SysOrg、SysDept、SysCom、SysCell、SysPersonLink
 */
public class TranSysOrgDept {

	public static void run() throws Exception {
		DelAndAddData();
		runOrg();
		runDrpt();
	}

	/**
	 * @throws Exception
	 * 机构表的转移
	 */
	public static void runOrg() throws Exception {
		Connection conn = TranDb.INST.getConn();
		//String sql = "SELECT * FROM `base_org` WHERE `code` NOT IN ('11001','11009','11010','11011','11012')";
		String sql = "SELECT * FROM `base_org`";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("================开始迁移================");
		System.out.println("============迁移【机构信息】============");
		while (rs.next()) {
			String code = rs.getString("code");
			String name = rs.getString("name");
			String parent = rs.getString("parent");
			Byte enabled = rs.getByte("enabled");
			int template = rs.getInt("template");
			Byte state = rs.getByte("state");
			String shortName = rs.getString("short_name");
			String website = rs.getString("http");
			String tel1 = rs.getString("tel1");
			String tel2 = rs.getString("tel2");
			String fax = rs.getString("fax");
			String address = rs.getString("address");
			String zipCode = rs.getString("zip_code");
			int updated_by = rs.getInt("updated_by");
			Date updated_date = rs.getDate("updated_date");
			String remark = rs.getString("remark");
			//判断是否已存在
			SysOrg org = SysOrg.chkUniqueCode(false, code);
			if (org == null) {
				org = new SysOrg().init();
				org.setName(name);
				org.setShortName(shortName);
				org.setEnabled(enabled);
				if ("11".equals(code)) {
					org.setOrgUp(null);
					org.setCode(code);
				} else {
					org.setOrgUp(1);
					org.setCode(code);
				}
				org.setTemplat(template);
				org.setWorkDate(new Date());
				SysOrgDAO.Ins orgins = new SysOrgDAO.Ins();
				orgins.setB(org);
				orgins._com = new SysCom().init();
				orgins._com.setName(name);
				orgins._com.setShortName(shortName);
				orgins._com.setWebsite(website);
				orgins._com.setTel1(tel1);
				orgins._com.setTel2(tel2);
				orgins._com.setFax(fax);
				orgins._com.setAddr(address);
				orgins._com.setZipCode(zipCode);
				orgins._com.setUpdatedBy(1);
				orgins._com.setUpdatedDateTime(updated_date);
				orgins._com.setRem(remark);
				orgins.commit();
				SysCell cell = SysCell.chkUniqueCode(false, org.getCode());
				if (cell == null) {
					cell = new SysCell().init();
					cell.setCode(org.getCode());
					cell.setName(org.getName());
					cell.setOrg(org.getPkey());
					cell.setYear(new Short("2015"));
					cell.setTemplat(1);
					SysCellDAO.Ins cellins = new SysCellDAO.Ins();
					cellins.setB(cell);
					cellins.commit();
				}else{					
					System.out.println("核算单元代码: [" + org.getCode() + "]已存在");
				}
			}else{
				System.out.println("机构代码: [" + code + "]已存在");
			}
		}
		System.out.println("==================结束===================");
		TranDb.INST.close(stat, rs);
	}

	/**
	 * @throws Exception
	 *           部门表的转移
	 */
	public static void runDrpt() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `base_dept` WHERE `org` NOT IN ('11001','11009','11010','11011','11012') AND `enabled` <> 2;";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("================开始迁移================");
		System.out.println("============迁移【部门信息】============");
		while (rs.next()) {
			String code = rs.getString("code");
			code = code.replaceAll("--", "-").replaceAll("-", "");
			String name = rs.getString("name");
			Byte enabled = rs.getByte("enabled");
			String org = rs.getString("org");
			String remark = rs.getString("remark");
			String address = rs.getString("address");
			String fax = rs.getString("fax");
			String tel1 = rs.getString("tel1");
			SysDept dept = SysDept.chkUniqueCode(false, code);
			if (dept == null) {
				dept = new SysDept().init();
				dept.setName(name);
				dept.setEnabled(enabled);
				dept.setRem(remark);
				SysOrg orgObj = SysOrg.chkUniqueCode(false, org);
				if(orgObj!=null){
					dept.setOrg(orgObj.getPkey());					
				}else{
					System.out.println("不存在的机构代码: ["+org+"]");
				}
				if (!"11".equals(code) && code.length() != 5) {
					dept.setCode(code);
					SysDept deptUp = SysDept.chkUniqueCode(false, code.substring(0, code.length() - 2));
					if(deptUp!=null){						
						dept.setDeptUp(deptUp.getPkey());
					}else{
						System.out.println("不存在的部门代码: ["+code.substring(0, code.length() - 2)+"]");
					}
				} else {
					dept.setCode(code);
					dept.setDeptUp(null);
				}
				SysDeptDAO.Ins deptins = new SysDeptDAO.Ins();
				deptins.setB(dept);
				deptins.commit();
				if (Str.isEmpty(address) == false) {
					runPersonLink(dept, name, address, fax, tel1);
				}
			}else{
				System.out.println("部门代码: ["+code+"]已存在");
				if (Str.isEmpty(address) == false) {
					runPersonLink(dept, name, address, fax, tel1);
				}
			}
		}
		System.out.println("==================结束===================");
		TranDb.INST.close(stat, rs);
	}

	/**
	 * 根据部门中的地址、传真、电话  建对应的联系人信息 TODO 目前缺少名称
	 */
	public static void runPersonLink(SysDept dept, String name, String addr, String fax, String tel) throws Exception {
		SysPersonLink person = new SysPersonLink();
		try {
			person.setTbObjLong(dept.gtLongPkey());
			person.setName(name);
			if(tel!=null){
				if(tel.length()<=20){				
					person.setOfTel(tel);				
				}
			}
			if(fax!=null){
				if(fax.length()<=20){				
					person.setOfFax(fax);
				}
			}
			person.setOfAddr(addr);
			person.setCreatedBy(1);
			person.setCreatedDateTime(new Date());
			person.setUpdatedBy(1);
			person.setUpdatedDateTime(new Date());
			person.ins();
		} catch (Exception e) {
			System.err.println("添加联系人信息出错代码如下:");
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * @throws Exception
	 *           在迁移前删除表再重建
	 */
	public static void DelAndAddData() throws Exception {
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("DROP TABLE " + Bean.tb(SysOrg.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(SysOrg.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(SysCom.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(SysCom.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(SysDept.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(SysDept.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(SysCell.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(SysCell.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(SysPersonLink.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(SysPersonLink.class).getCodeSqlTb() + "]-->成功!");
		stmt.close();
		new DbMysql().db(SysOrg.class);
		new DbMysql().db(SysCom.class);
		new DbMysql().db(SysDept.class);
		new DbMysql().db(SysCell.class);
		new DbMysql().db(SysPersonLink.class);
	}
}
