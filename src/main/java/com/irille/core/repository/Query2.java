package com.irille.core.repository;

import java.io.Serializable;
import java.util.List;

import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.query.EntityQuery;
import com.irille.core.repository.query.IPredicate;
import com.irille.core.repository.query.SqlQuery;
import com.irille.core.repository.sql.EntitySQL;

import irille.pub.Log;
import irille.pub.svr.DbPool;

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
	public static SqlQuery sql(EntitySQL sql) {
		return new SqlQuery(sql.toString(), sql.params().toArray(new Serializable[sql.params().size()]));
	}
	public static EntityQuery<?> select(IPredicate... predicates) {
		EntityQuery<?> q = new EntityQuery<>();
		return q.select(predicates);
	}
	public static <T extends Entity> EntityQuery<T> selectFrom(Class<T> entityClass) {
		EntityQuery<?> q = new EntityQuery<>();
		return q.select(entityClass).FROM(entityClass);
	}
	public static <T extends Entity> T selectFrom(Class<T> entityClass, Serializable pkey) {
		EntityQuery<?> q = new EntityQuery<>();
		return q.select(entityClass).FROM(entityClass).where("pkey=?", pkey).query();
	}
	
	public static <T extends Entity> EntityQuery<T> update(Class<T> entityClass) {
		return new EntityQuery<>().update(entityClass);
	}
	public static <T extends Entity> EntityQuery<T> delete(Class<T> entityClass) {
		return new EntityQuery<>().delete(entityClass);
	}
	public static <T extends Entity> EntityQuery<T> insert(Class<T> entityClass) {
		return new EntityQuery<>().insert(entityClass);
	}
	
//	public static EntityQuery<?> SELECT(Language lang, IPredicate... predicates) {
//		EntityQuery<?> q = new EntityQuery<>(lang);
//		return q.select(predicates);
//	}
//	public static <T extends Entity> EntityQuery<T> SELECT(Class<T> entityClass, Language lang) {
//		EntityQuery<?> q = new EntityQuery<>(lang);
//		return q.select(entityClass).FROM(entityClass);
//	}
//	public static <T extends Entity> T SELECT(Class<T> entityClass, Language lang, Integer pkey) {
//		EntityQuery<?> q = new EntityQuery<>(lang);
//		return q.select(entityClass).FROM(entityClass).where("pkey=?", pkey).query();
//	}
	
}
