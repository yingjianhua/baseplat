package irille.pss.sal;

import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.svr.ProvDataCtrl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class SalCollectDAO {
	private static String ROLE_SQL = "#role#";
	private static String TABLE_SQL = "#table#";
	private Date startDate;
	private Date expireDate;
	
	public static void main(String[] args) {
  }
	public Date getStartDate() {
  	return startDate;
  }
	public void setStartDate(Date startDate) {
  	this.startDate = startDate;
  }
	public Date getExpireDate() {
  	return expireDate;
  }
	public void setExpireDate(Date expireDate) {
  	this.expireDate = expireDate;
  }
	private static String getRoleWhere(Class clazz) {
		String role = ProvDataCtrl.INST.getWhere(Idu.getUser(), clazz);
		StringBuilder where =  new StringBuilder();
		if(!role.equals("1=1"))	role = role.substring(1,role.length()-1);
		for(String line: role.split(" OR ") ) {
			if(!line.equals("1=1")&&!line.equals("1=2"))	line = "t1."+line;
			where.append(" OR "+line);
		}
		return "("+where.substring(4)+")";
	}
	public Map<String,SalCollect> aa() {
		Map<String,SalCollect> list = new LinkedHashMap<String,SalCollect>();
		String role = getRoleWhere(SalSale.class);
		String sql = "select t2.login_name as 'code',t2.name as 'name',sum(t1.amt) as 'amt',sum(t1.amt_pay) as 'amt_pay',sum(t1.amt_rec) as 'amt_rec',sum(t1.amt_rec_back) as 'amt_rec_back',sum(t1.amt_ord) as 'amt_ord' from "+TABLE_SQL+" t1,sys_user t2 where (t1.operator=t2.pkey and created_time between ? and ? and "+ROLE_SQL+" and t1.status=98) group by t1.operator";
		
		Map[] maps = BeanBase.executeQueryMap(sql.replace(TABLE_SQL, "sal_sale").replace(ROLE_SQL, getRoleWhere(SalSale.class)), startDate, expireDate);
		list = Map2Line(maps, list, 1, false);
		
		maps = BeanBase.executeQueryMap(sql.replace(TABLE_SQL, "sal_sale_direct").replace(ROLE_SQL, getRoleWhere(SalSaleDirect.class)), startDate, expireDate);
		list = Map2Line(maps, list, 1, false);
		
		maps = BeanBase.executeQueryMap(sql.replace("sum(t1.amt_ord) as 'amt_ord'", "0").replace(TABLE_SQL, "sal_rtn").replace(ROLE_SQL, getRoleWhere(SalRtn.class)), startDate, expireDate);
		list = Map2Line(maps, list, 1, true);

		sql = "select t2.code as 'code',t2.short_name as 'name',sum(t1.amt) as 'amt',sum(t1.amt_pay) as 'amt_pay',sum(t1.amt_rec) as 'amt_rec',sum(t1.amt_rec_back) as 'amt_rec_back',sum(t1.amt_ord) as 'amt_ord' from "+TABLE_SQL+" t1,sys_org t2 where (t1.org=t2.pkey and created_time between ? and ? and "+ROLE_SQL+" and t1.status=98) group by t1.org";
		
		maps = BeanBase.executeQueryMap(sql.replace(TABLE_SQL, "sal_sale").replace(ROLE_SQL, getRoleWhere(SalSale.class)), startDate, expireDate); 
		list = Map2Line(maps, list, 2, false);
		
		maps = BeanBase.executeQueryMap(sql.replace(TABLE_SQL, "sal_sale_direct").replace(ROLE_SQL, getRoleWhere(SalSaleDirect.class)), startDate, expireDate);
		list = Map2Line(maps, list, 2, false);

		maps = BeanBase.executeQueryMap(sql.replace("sum(t1.amt_ord) as 'amt_ord'","0").replace(TABLE_SQL, "sal_rtn").replace(ROLE_SQL, getRoleWhere(SalRtn.class)), startDate, expireDate);
		list = Map2Line(maps, list, 2, true);
		/*Integer pkey = new Integer(2);
		SysUser user = BeanBase.load(SysUser.class, pkey);
		user.getName()
		user.gtOrg().getName()
		*/
		return list;
	}
	private static Map<String,SalCollect> Map2Line(Map[] maps, Map<String,SalCollect> list, int type , boolean negate) {
		String name = "";
		if(type==1) {
			name = "operator:";
		} else if(type==2) {
			name = "org:";
		} else {
			name = "total:";
		}
		for(Map map:maps) {
			SalCollect line;
			if(list.containsKey(name+map.get("code")+"")){
				line = list.get(name+map.get("code")+"");
			}else {
				line = new SalCollect().init();
			}
			line.setCode(map.get("code")+"");
			line.setName((String)map.get("name"));
			if(negate) {
				line.setAmtRtn(line.getAmtRtn().add(((BigDecimal)map.get("amt"))));
				line.setAmtCash(line.getAmtCash().add(((BigDecimal)map.get("amt_pay")).negate()));
				line.setAmtRec(line.getAmtRec().add(((BigDecimal)map.get("amt_rec")).negate()));
				line.setAmtRecback(line.getAmtRecback().add(((BigDecimal)map.get("amt_rec_back")).negate()));
			//	line.setAmtOrder(line.getAmtOrder().add(((BigDecimal)map.get("amt_ord")).negate()));
			} else {
				line.setAmtSal(line.getAmtSal().add((BigDecimal)map.get("amt")));
				line.setAmtCash(line.getAmtCash().add((BigDecimal)map.get("amt_pay")));
				line.setAmtRec(line.getAmtRec().add((BigDecimal)map.get("amt_rec")));
				line.setAmtRecback(line.getAmtRecback().add((BigDecimal)map.get("amt_rec_back"	)));
				line.setAmtOrder(line.getAmtOrder().add((BigDecimal)map.get("amt_ord")));
			}
			list.put(name+map.get("code"), line);
		}
		return list;
	}
		//BeanBase.executeQueryMap(sql, paras)
		//select operator,sum(amt),.... from sal_sale where date<> group by operator
		//linkedhashmap //带顺序的Map
		
	
	/*public static class List extends IduPage<List, SalCollect> {
		
	}*/
}
