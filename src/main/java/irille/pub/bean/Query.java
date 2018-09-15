package irille.pub.bean;

import java.io.Serializable;
import java.util.List;

import irille.pub.Log;
import irille.pub.bean.query.BeanQuery;
import irille.pub.bean.query.SqlQuery;
import irille.pub.bean.sql.SQL;
import irille.pub.svr.DbPool;
import irille.pub.tb.FldLanguage.Language;
import irille.pub.tb.IEnumFld;

public class Query {
	public static final Log LOG = new Log(Query.class);
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		try {
			String sql4 = "select * from hm_table";
			String sql3 = "select * from pdt_color";
			String sql2 = "select * from usr_member_level";
			String sql = "select b.name as cname,a.* from usr_consult a left join plt_country b on a.COUNTRY=b.PKEY where b.NAME='Zambia' AND TITLE like '%鞋%' order by CREATE_TIME desc LIMIT 0,2";
			System.out.println(Query.sql(sql3).debug().queryMaps());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbPool.getInstance().releaseAll();
		}
		//String sql2 = "select a.* from UsrConsult a left join UsrConsultRelation b on a.pkey=b.CONSULT where b.SUPPLIER=?";
	}

	public static SqlQuery sql(String sql, Serializable... params) {
		return new SqlQuery(sql, params);
	}
	public static SqlQuery sql(String sql, List<Serializable> params) {
		return new SqlQuery(sql, params.toArray(new Serializable[params.size()]));
	}
	public static SqlQuery sql(SQL sql) {
		return new SqlQuery(sql.toString(), sql.PARAMS().toArray(new Serializable[sql.PARAMS().size()]));
	}
	
	public static BeanQuery<?> SELECT(IEnumFld... flds) {
		BeanQuery<?> q = new BeanQuery<>();
		return q.SELECT(flds);
	}
	public static <T extends BeanMain<?, ?>> BeanQuery<T> SELECT(Class<T> beanClass) {
		BeanQuery<?> q = new BeanQuery<>();
		return q.SELECT(beanClass).FROM(beanClass);
	}
	public static <T extends BeanMain<?, ?>> T SELECT(Class<T> beanClass, Integer pkey) {
		BeanQuery<?> q = new BeanQuery<>();
		return q.SELECT(beanClass).FROM(beanClass).WHERE("pkey=?", pkey).query();
	}
	public static <T extends BeanMain<?, ?>> BeanQuery<T> UPDATE(Class<T> beanClass) {
		BeanQuery<?> q = new BeanQuery<>();
		return q.UPDATE(beanClass);
	}
	
	public static BeanQuery<?> SELECT(Language lang, IEnumFld... flds) {
		BeanQuery<?> q = new BeanQuery<>(lang);
		return q.SELECT(flds);
	}
	public static <T extends BeanMain<?, ?>> BeanQuery<T> SELECT(Class<T> beanClass, Language lang) {
		BeanQuery<?> q = new BeanQuery<>(lang);
		return q.SELECT(beanClass).FROM(beanClass);
	}
	public static <T extends BeanMain<?, ?>> T SELECT(Class<T> beanClass, Language lang, Integer pkey) {
		BeanQuery<?> q = new BeanQuery<>(lang);
		return q.SELECT(beanClass).FROM(beanClass).WHERE("pkey=?", pkey).query();
	}
}
