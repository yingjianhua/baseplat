package com.irille.core.repository;

import java.io.Serializable;
import java.util.List;

import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.query.EntityQuery;
import com.irille.core.repository.query.IPredicate;
import com.irille.core.repository.query.SqlQuery;
import com.irille.core.repository.sql.SQL;

import irille.pub.Log;
import irille.pub.svr.DbPool;
import irille.pub.tb.FldLanguage.Language;

public class Query2 {
	public static final Log LOG = new Log(Query2.class);
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		try {
			String sql4 = "select * from hm_table";
			String sql3 = "select * from pdt_color";
			String sql2 = "select * from usr_member_level";
			String sql = "select b.name as cname,a.* from usr_consult a left join plt_country b on a.COUNTRY=b.PKEY where b.NAME='Zambia' AND TITLE like '%鞋%' order by CREATE_TIME desc LIMIT 0,2";
			System.out.println(Query2.sql(sql3).debug().queryMaps());
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
	public static EntityQuery<?> SELECT(IPredicate... predicates) {
		EntityQuery<?> q = new EntityQuery<>();
		return q.SELECT(predicates);
	}
	public static <T extends Entity> EntityQuery<T> SELECT(Class<T> beanClass) {
		EntityQuery<?> q = new EntityQuery<>();
		return q.SELECT(beanClass).FROM(beanClass);
	}
	public static <T extends Entity> T SELECT(Class<T> beanClass, Serializable pkey) {
		EntityQuery<?> q = new EntityQuery<>();
		return q.SELECT(beanClass).FROM(beanClass).WHERE("pkey=?", pkey).query();
	}
	
	public static <T extends Entity> EntityQuery<T> UPDATE(Class<T> beanClass) {
		return new EntityQuery<>().UPDATE(beanClass);
	}
	public static <T extends Entity> EntityQuery<T> DELETE(Class<T> beanClass) {
		return new EntityQuery<>().DELETE(beanClass);
	}
	public static <T extends Entity> EntityQuery<T> INSERT(Class<T> beanClass) {
		return new EntityQuery<>().INSERT(beanClass);
	}
	
	public static EntityQuery<?> SELECT(Language lang, IPredicate... predicates) {
		EntityQuery<?> q = new EntityQuery<>(lang);
		return q.SELECT(predicates);
	}
	public static <T extends Entity> EntityQuery<T> SELECT(Class<T> beanClass, Language lang) {
		EntityQuery<?> q = new EntityQuery<>(lang);
		return q.SELECT(beanClass).FROM(beanClass);
	}
	public static <T extends Entity> T SELECT(Class<T> beanClass, Language lang, Integer pkey) {
		EntityQuery<?> q = new EntityQuery<>(lang);
		return q.SELECT(beanClass).FROM(beanClass).WHERE("pkey=?", pkey).query();
	}
	
}
