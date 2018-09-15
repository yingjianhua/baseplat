package irille.trandb;

import irille.core.sys.SysCom;
import irille.core.sys.SysOrg;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysSupplier;
import irille.core.sys.SysSupplierDAO;
import irille.core.sys.SysSupplierOrg;
import irille.core.sys.SysUser;
import irille.pub.bean.Bean;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 * @author LYL
 * 2015-7-15 11:32:03
 * 迁移数据: `crm_vendor`
 * 迁移条件: `code` NOT REGEXP '^(11001|11009|11010|11011|11012)'
 */
public class TranSysSupplier {
	public static void run() throws Exception {
		DelAndAdd();
		runSupplierAndCom();
	}

	/**
	 * 供应商表(crm_vendor)迁移分析:
	 * 供应商表和个人表(base_person)是关联的,
	 * 迁移时根据供应商编号133拼接上code在base_person中查询,如"133-11001-1"
	 * 在runPersonLink()方法中查询并对应的添加到新数据库的sys_person_link中
	 * 注意!!crm_vendor中org字段有错数据所以暂定使用code中的org代码
	 * @throws Exception
	 */
	public static void runSupplierAndCom() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `crm_vendor` WHERE `code`  REGEXP '^(11004)'";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("=====================数据迁移开始====================");
		System.out.println("=============迁移【供应商表和单位信息迁移】============");
		//为了防止重复的用户名
		int num = 0;
		while (rs.next()) {
			String code = rs.getString("code");
			String name = rs.getString("name");
			//拆分原code"11001"为"11.001"
			String org = code.substring(0, 5);
			//根据code获取org的pkey
			SysOrg orgObj = SysOrg.chkUniqueCode(false, org);
			int operator = rs.getInt("operator");
			//根据operator获取user的pkey
			SysUser userpkey = SysUser.chkUniqueCode(false, operator + "");
			Date createdDateTime = rs.getDate("created_date");
			//新增前判断是否存在
			SysSupplier supplier = SysSupplier.chkUniqueCode(false, code);
			if (supplier == null) {
				supplier = new SysSupplier().init();
				supplier.setCode(code);
				supplier.setName(name);
				supplier.setMngOrg(orgObj.getPkey());
				//判断sys_com中name是否存在一样的;存在就加个num进去
				if (SysCom.chkUniqueName(false, supplier.getName()) != null) {
					System.err.println("单位信息["+supplier.getName()+"]已存在");
					supplier.setName(supplier.getName() + num);
					num++;
				}
				//user表在迁移时去除了停用账户所以在此判断用户是否存在,不存在就直接赋值为管理员pkey
				if (userpkey != null) {
					supplier.setBusinessMember(userpkey.getPkey());
				} else {
					supplier.setBusinessMember(1);
				}
				SysSupplierDAO.Ins upplierins = new SysSupplierDAO.Ins();
				upplierins.setB(supplier);
				upplierins._com = new SysCom().init();
				upplierins.commit();
				upplierins._com.setCreatedDateTime(createdDateTime);
				upplierins._com.upd();
				//对应的创建一条sys_supplier_org数据
				runSupplierOrg(supplier);
			}else{
				System.err.println("供应商["+code+"]已存在");
			}
			//根据供应商编号在base_person查找并迁移数据
			runPersonLink(supplier);
		}
		TranDb.INST.close(stat, rs);
		System.out.println("==================数据迁移完成==================");
	}

	/**
	 * 根据SysSupplier的code查找数据
	 * @param supplier
	 * @throws Exception
	 */
	public static void runPersonLink(SysSupplier supplier) throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM base_person where owner_form_numb = " + "'133-" + supplier.getCode() + "'";
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
			personLink.setTbObjLong(supplier.gtLongPkey());
			personLink.setName(name);
			personLink.setPeSex(sex);
			if (certificate_numb != null) {
				if (certificate_numb.length() <= 20) {
					personLink.setPeCardNumb(certificate_numb);
				}
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
			personLink.setOfAddr(address);
			if (zip_code != null && zip_code.length() <= 6) {
				personLink.setOfZipCode(zip_code);
			}
			personLink.setRem(remark);
			personLink.setCreatedBy(1);
			personLink.setCreatedDateTime(new Date());
			personLink.setUpdatedBy(1);
			personLink.setUpdatedDateTime(new Date());
			personLink.ins();
		}
		TranDb.INST.close(stat, rs);
	}

	/**
	 * 新增sys_supplier_org数据
	 * @param supplier
	 * @throws Exception
	 */
	public static void runSupplierOrg(SysSupplier supplier) throws Exception {
		SysSupplierOrg supplierOrg = new SysSupplierOrg();
		supplierOrg.setSupplier(supplier.getPkey());
		supplierOrg.setOrg(supplier.getMngOrg());
		supplierOrg.setDept(null);
		supplierOrg.ins();
	}

	public static void DelAndAdd() throws Exception {
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("DROP TABLE " + Bean.tb(SysSupplier.class).getCodeSqlTb());
		stmt.execute("DROP TABLE " + Bean.tb(SysSupplierOrg.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(SysSupplier.class).getCodeSqlTb() + "]-->成功!");
		System.out.println("删除表[" + Bean.tb(SysSupplierOrg.class).getCodeSqlTb() + "]-->成功!");
		stmt.close();
		new DbMysql().db(SysSupplier.class);
		new DbMysql().db(SysSupplierOrg.class);
	}
}
