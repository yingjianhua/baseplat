package com.irille.core.repository.query;

import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ResultMapper {
	public static final Log LOG = new Log(ResultMapper.class);

	private static final void propertySet(Bean<?, ?> bean, Fld<?> fld, Object val) {
		try {
			fld.getSetMethod().invoke(bean, val);
		} catch (Exception e) {
			throw LOG.err(e, "setField", "对Bean的字段【{0}】赋值出错!", fld.getName());
		}
	}

	@SuppressWarnings("rawtypes")
	private static final <T extends Bean> T fromResultSet(ResultSet rs, Class<T> beanClass) {
		T bean = BeanBase.newInstance(beanClass);
		Fld[] flds = Tb.getTBByBean(beanClass).getFlds(); 
		try {
			for (Fld fld : flds) {
				try {
					if (rs.getObject(fld.getCodeSqlField()) != null)
						propertySet(bean, fld, fld.getResult(rs, fld.getCodeSqlField()));
				} catch (SQLException e) {
				}
			}
			return bean;
		} catch (Exception e) {
			throw LOG.err(e, "setBeanfromResultSet", "数据库记录->对象【{0}】时出错!", beanClass);
		}
	}
	
	public static <T extends Bean<?, ?>> T asBean(ResultSet rs, Class<T> beanClass) {
		try {
			if(rs.next())
				return fromResultSet(rs, beanClass);
			return null;
		} catch (Exception e) {
			throw LOG.err("asBeanFromResultSet", "数据库记录->[{0}]对象出错", beanClass.getName());
		}
	}
	
	public static <T extends Bean<?, ?>> List<T> asBeanList(ResultSet rs, Class<T> beanClass) {
		try {
			Vector<T> list = new Vector<T>();
			while (rs.next()) {
				list.add(fromResultSet(rs, beanClass));
			}
			return list;
		} catch (SQLException e) {
			throw LOG.err("asBeanListFromResultSet", "数据库记录->List<{0}>对象出错", beanClass.getName());
		}
	}
	public static <T extends Object> List<T> asObjects(ResultSet rs, Class<T> resultClass) {
		try {
			Vector<T> list = new Vector<T>();
			while (rs.next()) {
				list.add((T)rs.getObject(1));
			}
			return list;
		} catch (Exception e) {
			throw LOG.err("asObjectsFromResultSet", "数据库记录->List<{0}>对象出错", resultClass.getName());
		}
	}
	public static <T extends Object> T asObject(ResultSet rs, Class<T> resultClass) {
		try {
			if(rs.next()) {
				switch (resultClass.getName()) {
				case "java.lang.Integer":
					return (T)(Integer)(rs.getInt(1));
				default:
					return (T)rs.getObject(1);
				}
			}
		} catch (Exception e) {
			throw LOG.err("asObjectFromResultSet", "数据库记录->[{0}]对象出错", resultClass.getName());
		}
		return null;
	}
	
	public static Map<String, Object> asMap(ResultSet rs) {
		try {
			Map<String, Object> map = new HashMap<>();
//		System.out.println("column length:"+l);
//		System.out.println("  |catalogName|tableName|schemaName|columnClassName|columnLabel|scale|columnDisplaySize|precision|columnType|columnTypeName|columnName|columnValue|"
//				+ "javaType|");
//		java.sql.Types
			ResultSetMetaData md = rs.getMetaData();
			int l = md.getColumnCount();
			if(rs.next()) {
				for(int i=0;i<l;i++){
					String key = md.getColumnLabel(i+1);
					Object value = null;
					/**
					 * 没有根据数据库里的实际类型来取值,会导致某些类型的出现偏差,如表里为short类型,数据库为tinyint,但取出来的值为integer
					 * 还有一些类型没有做转换
					 */
					switch (md.getColumnType(i+1)) {
					case Types.TINYINT:
						value = rs.getByte(i+1);
						break;
					case Types.SMALLINT:
						value = rs.getShort(i+1);
						break;
					default:
						value = rs.getObject(i+1);
					}
//				System.out.println((i+1)+""
//						+" | "+md.getCatalogName(i+1)
//						+" | "+md.getTableName(i+1) 
//						+" | "+md.getSchemaName(i+1) 
//						+" | "+md.getColumnClassName(i+1)
//						+" | "+md.getColumnLabel(i+1)
//						+" | "+md.getScale(i+1)
//						+" | "+md.getColumnDisplaySize(i+1)
//						+" | "+md.getPrecision(i+1)
//						+" | "+md.getColumnType(i+1)
//						+" | "+md.getColumnTypeName(i+1) 
//						+" | "+md.getColumnName(i+1)
//						+" | "+rs.getObject(i+1)
//						+" | "+rs.getObject(i+1).getClass().getName()
//						+" |");
					map.put(key, value);
				}
			}
			return map;
		} catch (Exception e) {
			throw LOG.err("asMapFromResultSet", "数据库记录->Map对象出错");
		}
	}
	
	public static List<Map<String, Object>> asMaps(ResultSet rs)  {
		try {
			List<Map<String, Object>> list = new ArrayList<>();
			ResultSetMetaData md = rs.getMetaData();
			int l = md.getColumnCount();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<>();
				for(int i=0;i<l;i++){
					String key = md.getColumnLabel(i+1);
					Object value = null;
					switch (md.getColumnType(i+1)) {
					case Types.TINYINT:
						value = rs.getByte(i+1);
						break;
					case Types.SMALLINT:
						value = rs.getShort(i+1);
						break;
					default:
						value = rs.getObject(i+1);
					}
					map.put(key, value);
				}
				list.add(map);
			}
			return list;
		} catch (SQLException e) {
			throw LOG.err("asMapsFromResultSet", "数据库记录->List<Map>对象出错");
		}
	}
	
}
