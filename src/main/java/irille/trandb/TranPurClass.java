package irille.trandb;

import irille.core.sys.SysOrg;
import irille.core.sys.SysSupplier;
import irille.core.sys.SysTemplat;
import irille.core.sys.SysTemplatCell;
import irille.core.sys.Sys.OTemplateType;
import irille.gl.gs.GsGoods;
import irille.pss.pur.PurProt;
import irille.pss.pur.PurProtDAO;
import irille.pss.pur.PurProtGoods;
import irille.pss.sal.Sal.OShipType;
import irille.pub.bean.Bean;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 * @author LYL
 * 2015-7-13 14:51:07
 * 条件: 迁移org为'11004'并且上次成交价不为null的数据
 * 迁移内容：
 * 					一、财务模板：自己定义的
 * 					二、模板可使用单元：添加上面的数据
 * 					三、供应商协议：对照pur_vendor_goods,条件:GROUP BY vendor
 * 						  1、templat：财务模板的pkey,
 * 							2、shipType：WAIT.待定，
 * 							3、dateProt：老数据的建档时间
 * 							4、dateStart：老数据的建档时间
 * 					四、供应商货物协议：
 */
public class TranPurClass {
	public static void run() throws Exception {
		DelAndAddData();
		runSysTemplat();
		runSysTemplatCell();
		runPurProt();
		runPurProtGoods();
	}

	public static void runSysTemplat() {
		System.out.println("====================【创建财务模板】================");
		SysTemplat templat = SysTemplat.chkUniqueCodeYear(false, "pur", (short) 2015, (byte) 3);
		if(templat==null){
			templat = new SysTemplat();
			templat.stType(OTemplateType.PUR);
			templat.setCode("pur");
			templat.setYear((short) 2015);
			templat.setName("采购模板");
			SysOrg org = SysOrg.chkUniqueCode(false, "11004");
			templat.setMngCell(org.getPkey());
			templat.stEnabled(true);
			templat.ins();
			System.out.println("======================【完成创建】==================");			
		}
	}

	public static void runSysTemplatCell() throws Exception {
		System.out.println("================【创建模板可使用单元】===============");
		SysTemplat sysTemplat = SysTemplat.chkUniqueCodeYear(false, "pur", (short) 2015, (byte) 3);
		SysTemplatCell sysTemplatCell = new SysTemplatCell();
		sysTemplatCell.setTemplat(sysTemplat.getPkey());
		sysTemplatCell.setCell(sysTemplat.getMngCell());
		sysTemplatCell.ins();
		System.out.println("======================【完成创建】==================");
	}

	public static void runPurProt() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `pur_vendor_goods` WHERE org = '11004' GROUP BY vendor";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("===================数据迁移开始==================");
		System.out.println("================迁移【供应商协议】===============");
		SysTemplat templat = SysTemplat.chkUniqueCodeYear(false, "pur", (short) 2015, (byte) 3);
		while (rs.next()) {
			String vendor = rs.getString("vendor");
			Date date = rs.getDate("created_date");
			SysSupplier supplier = SysSupplier.chkUniqueCode(false, vendor);
			if (supplier != null) {
				PurProt purProt = new PurProt().init();
				purProt.setTemplat(templat.getPkey());
				purProt.setSupplier(supplier.getPkey());
				purProt.setName(supplier.getName());
				purProt.stShipType(OShipType.WAIT);
				purProt.setDateProt(date);
				purProt.setDateStart(date);
				PurProtDAO.Ins ins = new PurProtDAO.Ins();
				ins.setB(purProt);
				ins.commit();
			} else {
				System.out.println("不存在的供应商: " + vendor);
			}
		}
	}

	public static void runPurProtGoods() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `pur_vendor_goods` WHERE org = '11004'";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("===================数据迁移开始==================");
		System.out.println("================迁移【供应商货物协议】===============");
		SysTemplat templat = SysTemplat.chkUniqueCodeYear(false, "pur", (short) 2015, (byte) 3);
		while (rs.next()) {
			String vendor = rs.getString("vendor");
			String goods = rs.getString("goods");
			String vgName = rs.getString("vendor_goods_name");
			String vendorNum = rs.getString("vendor_num");
			String vgSpec = rs.getString("vendor_goods_spec");
			String vendorBarcode = rs.getString("vendor_barcode");
			BigDecimal latestPrice = rs.getBigDecimal("latest_price");
			BigDecimal num = new BigDecimal(12846158181.0000);
			if(latestPrice == null || latestPrice.compareTo(num)==1)
					latestPrice = latestPrice.ZERO;
			Date latestDate = rs.getDate("latest_date");
			Date date = rs.getDate("created_date");
			SysSupplier supplier = SysSupplier.chkUniqueCode(false, vendor);
			GsGoods gsGoods = GsGoods.chkUniqueCode(false, goods.substring(2));
			if (supplier != null) {
				PurProtGoods purProtGoods = new PurProtGoods();
				purProtGoods.setTemplat(templat.getPkey());
				purProtGoods.setSupplier(supplier.getPkey());
				purProtGoods.setName(supplier.getName());
				purProtGoods.setGoods(gsGoods.getPkey());
				purProtGoods.setUom(gsGoods.getUom());
				purProtGoods.setVendorGoodsName(vgName);
				purProtGoods.setVendorNum(vendorNum);
				if (vgSpec != null || vendorBarcode != null) {
					purProtGoods.setVendorSpec(vgSpec + " | " + vendorBarcode);
				}
				if (vgSpec != null && vendorBarcode == null) {
					purProtGoods.setVendorSpec(vgSpec);
				}
				if (vgSpec == null && vendorBarcode != null) {
					purProtGoods.setVendorSpec(vendorBarcode);
				}
				purProtGoods.setPrice(latestPrice);
				purProtGoods.setPriceLast(latestPrice);
				purProtGoods.setDateLast(latestDate);
				purProtGoods.setDateStart(date);
				purProtGoods.setUpdatedBy(1);
				purProtGoods.setUpdatedTime(new Date());
				purProtGoods.ins();
			} else {
				System.out.println("不存在的供应商: " + vendor);
			}
		}
		System.out.println("==================数据迁移完成==================");
		TranDb.INST.close(stat, rs);
	}

	public static void DelAndAddData() throws Exception {
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("DROP TABLE " + Bean.tb(SysTemplatCell.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(SysTemplatCell.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(PurProt.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(PurProt.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(PurProtGoods.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(PurProtGoods.class).getCodeSqlTb() + "]-->成功!");
		stmt.close();
		new DbMysql().db(SysTemplatCell.class);
		new DbMysql().db(PurProt.class);
		new DbMysql().db(PurProtGoods.class);
	}
}
