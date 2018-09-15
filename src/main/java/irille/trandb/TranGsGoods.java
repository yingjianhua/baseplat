package irille.trandb;

import irille.core.sys.SysUser;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsGoodsDAO;
import irille.gl.gs.GsGoodsKind;
import irille.gl.gs.GsGoodsKindDAO;
import irille.gl.gs.GsUom;
import irille.gl.gs.Gs.OBatchType;
import irille.gl.gs.Gs.OType;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

/**
 * @author LYL
 * 2015-7-15 13:55:00
 * 迁移数据: `erp_goods_clazz`、`erp_goods`a、`erp_uom`
 * 
 */
public class TranGsGoods {
	public static void run() throws Exception {
		DelAndAdd();
		runGsGoodsKind();
		runGsGoods();		
	}

	public static void runGsGoodsKind() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `erp_goods_clazz`";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("==================数据迁移开始==================");
		System.out.println("=================迁移【货物类别】===============");
		while (rs.next()) {
			String code = rs.getString("code");
			code = code.substring(2);
			String name = rs.getString("name");
			byte type = rs.getByte("type");
			Date updated_date = rs.getDate("updated_date");
			GsGoodsKind goodsKind = GsGoodsKind.chkUniqueCode(false, code);
			if (goodsKind == null) {
				goodsKind = new GsGoodsKind().init();
				goodsKind.setCode(code);
				goodsKind.setName(name);
				SysUser sysUser = SysUser.chkUniqueCode(false, "admin");
				if (code.length() != 2&&code.length()!=4) {
					if(code.length()==5){
						GsGoodsKind goodsKindPkey = GsGoodsKind.chkUniqueCode(false, code.substring(0,2));
						if(goodsKindPkey!=null){
							goodsKind.setParent(goodsKindPkey.getPkey());													
						}else{
							System.err.println("货物类别["+code.substring(0,2)+"]不存在");
						}
					}
					if(code.length()==8){
						GsGoodsKind goodsKindPkey = GsGoodsKind.chkUniqueCode(false, code.substring(0,5));
						if(goodsKindPkey!=null){
							goodsKind.setParent(goodsKindPkey.getPkey());													
						}else{
							code = code.substring(0,2);
							goodsKindPkey = GsGoodsKind.chkUniqueCode(false, code);
							goodsKind.setParent(goodsKindPkey.getPkey());													
						}					
					}
					if(code.length()==11){
						GsGoodsKind goodsKindPkey = GsGoodsKind.chkUniqueCode(false, code.substring(0,8));
						if(goodsKindPkey!=null){
							goodsKind.setParent(goodsKindPkey.getPkey());													
						}else{
							code = code.substring(0,2);
							goodsKindPkey = GsGoodsKind.chkUniqueCode(false, code);
							goodsKind.setParent(goodsKindPkey.getPkey());													
						}						
					}
					if(code.length()==14){
						GsGoodsKind goodsKindPkey = GsGoodsKind.chkUniqueCode(false, code.substring(0,11));
						if(goodsKindPkey!=null){
							goodsKind.setParent(goodsKindPkey.getPkey());													
						}else{
							code = code.substring(0,2);
							goodsKindPkey = GsGoodsKind.chkUniqueCode(false, code);
							goodsKind.setParent(goodsKindPkey.getPkey());													
						}						
					}
				} else {
					goodsKind.setParent(null);
					goodsKind.setSubjectAlias("goods");
				}
				if (type == 2||type == 3) {
					goodsKind.stType(OType.WORK);
				}else{
					goodsKind.setType(type);
					goodsKind.setCust1("品牌");
					goodsKind.setCust2("大小");
					goodsKind.setCust3("克重");
					goodsKind.setCust4("颜色");
				}
				goodsKind.setUpdateby(sysUser.getPkey());
				GsGoodsKindDAO.Ins goodsKindDAO = new GsGoodsKindDAO.Ins();
				goodsKindDAO.setB(goodsKind);
				goodsKindDAO.commit();
				goodsKind.setUpdatedTime(updated_date);
				goodsKind.upd();
			}
		}
		System.out.println("==================数据迁移完成==================");
		TranDb.INST.close(stat, rs);
	}
	public static void runGsGoods() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `erp_goods`a,`erp_uom`b where a.uom = b.code";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("==================数据迁移开始==================");
		System.out.println("===================迁移【货物】=================");
		while (rs.next()) {
			String code = rs.getString("a.code");
			code = code.substring(2);
			String kind = rs.getString("a.goods_clazz");
			String old_code = rs.getString("a.old_code");
			String name = rs.getString("a.name");
			String uomname = rs.getString("b.name");
			String spec = rs.getString("a.spec");
			String cust1 = rs.getString("a.brand");
			String cust2 = rs.getString("a.cust1");
			String cust3 = rs.getString("a.cust2");
			String cust4 = rs.getString("a.cust3");
			BigDecimal  weight_rate = rs.getBigDecimal("a.unit_weight");
			BigDecimal  valume_rate = rs.getBigDecimal("a.unit_volume");
			String descrip = rs.getString("a.describle");
			String bar_code = rs.getString("a.bar_code");
			byte enabled = rs.getByte("a.enabled");
			String where = GsUom.T.NAME.getFld().getCodeSqlField() + "=?";
			GsGoods goods = GsGoods.chkUniqueCode(false, code);
			try {
				if(goods==null){
					goods = new GsGoods().init();
					goods.setCode(code);
					GsGoodsKind goodsKind = GsGoodsKind.chkUniqueCode(false, kind.substring(2));
					if(goodsKind!=null){
						goods.setKind(goodsKind.getPkey());					
					}else{
						System.err.println("货物类别["+kind.substring(2)+"]不存在");
					}
					goods.setCodeOld(old_code);
					goods.setName(name);
					List<GsUom> gsUoms = BeanBase.list(GsUom.class, where, true, uomname);
					if(gsUoms!=null){
						goods.setUom(gsUoms.get(0).getPkey());					
					}else{
						System.err.println("计量单位["+ uomname+"]不存在");
					}
					goods.setSpec(spec);
					goods.setCust1(cust1);
					goods.setCust2(cust2);
					goods.setCust3(cust3);
					goods.setCust4(cust4);
					goods.setWeightRate(weight_rate);
					goods.setValumeRate(valume_rate);
					goods.setDescrip(descrip);
					goods.setBarCode(bar_code);
					goods.stZeroOutFlag(false);
					goods.stBatchType(OBatchType.NO);
					if(enabled==2){
						goods.stEnabled(false);					
					}else{
						goods.stEnabled(true);
					}
					GsGoodsDAO.Ins goodsins = new GsGoodsDAO.Ins();
					goodsins.setB(goods);
					goodsins.commit();
				}
			} catch (Exception e) {
				System.err.println("超出的字段内容:"+old_code);
				e.printStackTrace();
				System.exit(0);
			}
		}
		System.out.println("==================数据迁移完成==================");
		TranDb.INST.close(stat, rs);
	}
	public static void DelAndAdd() throws Exception {
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("DROP TABLE " + Bean.tb(GsGoodsKind.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GsGoodsKind.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(GsGoods.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GsGoods.class).getCodeSqlTb() + "]-->成功!");
		stmt.close();
		new DbMysql().db(GsGoodsKind.class);
		new DbMysql().db(GsGoods.class);
	}
}
