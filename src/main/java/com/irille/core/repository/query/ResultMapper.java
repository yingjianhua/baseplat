package com.irille.core.repository.query;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.ColumnTypes;
import com.irille.core.repository.orm.Entity;

import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

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
	private static final <T extends Bean> T fromResultSet2(ResultSet rs, Class<T> beanClass) {
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
	private static final Object fromResultSet(ResultSet rs, ColumnTypes type, String columnLabel) throws SQLException {
		Object value = null;
		switch (type) {
		case BLOB:
			value = rs.getBlob(columnLabel);
			break;
		case BOOLEAN:
			value = rs.getBoolean(columnLabel);
			break;
		case BYTE:
			value = rs.getByte(columnLabel);
			break;
		case CHAR:
			value = rs.getString(columnLabel);
			break;
		case CLOB:
			value = rs.getClob(columnLabel);
			break;
		case DATE:
			value = rs.getDate(columnLabel);
			break;
		case DEC:
			value = rs.getBigDecimal(columnLabel);
			break;
		case DOUBLE:
			value = rs.getDouble(columnLabel);
			break;
		case INT:
			value = rs.getInt(columnLabel);
			break;
		case JSONARRAY:
			value = rs.getString(columnLabel);
			break;
		case JSONOBJECT:
			value = rs.getString(columnLabel);
			break;
		case LONG:
			value = rs.getLong(columnLabel);
			break;
		case OPTLINE:
			value = rs.getByte(columnLabel);
			break;
		case SHORT:
			value = rs.getShort(columnLabel);
			break;
		case STR:
			value = rs.getString(columnLabel);
			break;
		case STRINGTEXT:
			value = rs.getString(columnLabel);
			break;
		case STROPT:
			value = rs.getString(columnLabel);
			break;
		case TEXT:
			value = rs.getString(columnLabel);
			break;
		case TIME:
			value = rs.getDate(columnLabel);
			break;
		default:
			value = rs.getObject(columnLabel);
			break;
		}
		return value;
	}
	
	private static final <T extends Entity> T fromResultSet(ResultSet rs, Class<T> entityClass) {
		Column[] columns = Entity.table(entityClass).columns();
		try {
			T entity = entityClass.newInstance();
			for (Column column : columns) {
				try {
					Object value = fromResultSet(rs, column.type(), column.fieldName());
					try {
						if(value != null) {
							column.setterMethod().invoke(entity, value);
						}
					} catch (Exception e1) {
						throw LOG.err(e1, "setField", "对Bean的字段【{0}】赋值出错!", column.fieldName());
					}
				} catch (SQLException e) {
				}
			}
			return entity;
		} catch (InstantiationException | IllegalAccessException e) {
			throw LOG.err(e, "newInstanceErr", "初始化[{0}]对象出错", entityClass);
		} catch (Exception e) {
			throw LOG.err(e, "setBeanfromResultSet", "数据库记录->对象【{0}】时出错!", entityClass);
		}
	}
	
	public static <T extends Bean<?, ?>> T asBean(ResultSet rs, Class<T> beanClass) {
		try {
			if(rs.next())
				return fromResultSet2(rs, beanClass);
			return null;
		} catch (Exception e) {
			throw LOG.err("asBeanFromResultSet", "数据库记录->[{0}]对象出错", beanClass.getName());
		}
	}
	
	public static <T extends Bean<?, ?>> List<T> asBeanList(ResultSet rs, Class<T> beanClass) {
		try {
			Vector<T> list = new Vector<T>();
			while (rs.next()) {
				list.add(fromResultSet2(rs, beanClass));
			}
			return list;
		} catch (SQLException e) {
			throw LOG.err("asBeanListFromResultSet", "数据库记录->List<{0}>对象出错", beanClass.getName());
		}
	}
	
	public static Object asColumn(ResultSet rs, ColumnTypes type, String columnLabel) {
		try {
			if(rs.next())
				return fromResultSet(rs, type, columnLabel);
			return null;
		} catch (Exception e) {
			throw LOG.err("asColumnFromResultSet", "数据库记录->[{0}]字段出错", columnLabel);
		}
	}
	
	public static <T extends Entity> T asEntity(ResultSet rs, Class<T> entityClass) {
		try {
			if(rs.next())
				return fromResultSet(rs, entityClass);
			return null;
		} catch (Exception e) {
			throw LOG.err("asEntityFromResultSet", "数据库记录->[{0}]对象出错", entityClass.getName());
		}
	}
	public static <T extends Entity> List<T> asEntityList(ResultSet rs, Class<T> entityClass) {
		try {
			Vector<T> list = new Vector<T>();
			while (rs.next()) {
				list.add(fromResultSet(rs, entityClass));
			}
			return list;
		} catch (SQLException e) {
			throw LOG.err("asEntityListFromResultSet", "数据库记录->List<{0}>对象出错", entityClass.getName());
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
	
	public static Object fromResultSet(ResultSet rs, int types, int index) throws SQLException {
		Object value = null;
		/**
		 * 没有根据数据库里的实际类型来取值,会导致某些类型的出现偏差,如表里为short类型,数据库为tinyint,但取出来的值为integer
		 * 还有一些类型没有做转换
		 */
		switch (types) {
		case Types.TINYINT:
			value = rs.getByte(index);
			break;
		case Types.SMALLINT:
			value = rs.getShort(index);
			break;
		case Types.INTEGER:
			value = rs.getInt(index);
			break;
		case Types.DATE:
			value = rs.getDate(index);
			break;
		case Types.TIME:
			value = rs.getTime(index);
			break;
		case Types.TIMESTAMP:
			value = rs.getTimestamp(index);
			break;
		default:
			value = rs.getObject(index);
		}
		return value;
	}
	
	public static Map<String, Object> asMap(ResultSet rs) {
		try {
			Map<String, Object> map = new HashMap<>();
			boolean f = false;
			if(f)
				System.out.println("  |catalogName|tableName  |schemaName |columnClassName     |columnLabel  |scale|columnDisplaySize|precision|columnType |columnTypeName  |"
						+ "javaType            |"
						+"columnName |columnValue         |"
						);
			ResultSetMetaData md = rs.getMetaData();
			int l = md.getColumnCount();
			if(rs.next()) {
				for(int i=1;i<=l;i++){
					String key = md.getColumnLabel(i);
					Object value = fromResultSet(rs, md.getColumnType(i), i);
					if(f) {
						print((i)+"", 2);
						print(md.getCatalogName(i), 11);
						print(md.getTableName(i), 11);
						print(md.getSchemaName(i), 11);
						print(md.getColumnClassName(i), 20);
						print(md.getColumnLabel(i), 13);
						print(md.getScale(i)+"", 5);
						print(md.getColumnDisplaySize(i)+"", 17);
						print(md.getPrecision(i)+"", 9);
						print(md.getColumnType(i)+"", 11);
						print(rs.getObject(i).getClass().getName(), 20);
						print(md.getColumnTypeName(i), 16);
						print(md.getColumnName(i), 11);
						print(rs.getObject(i)+"", 20);
						System.out.println();
					}
					map.put(key, value);
				}
			}
			return map;
		} catch (Exception e) {
			throw LOG.err("asMapFromResultSet", "数据库记录->Map对象出错");
		}
	}
	public static void print(String str, int length) {
		if(str.length()>length)
			System.out.print(str.substring(0, length)+"|");
		else {
			StringBuilder b = new StringBuilder();
			for(int i=0;i<length-str.length();i++) {
				b.append(" ");
			}
			System.out.print(str+b.toString()+"|");
		}
	}
	
	public static List<Map<String, Object>> asMaps(ResultSet rs)  {
		try {
			List<Map<String, Object>> list = new ArrayList<>();
			ResultSetMetaData md = rs.getMetaData();
			int l = md.getColumnCount();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<>();
				for(int i=1;i<=l;i++){
					String key = md.getColumnLabel(i);
					Object value = fromResultSet(rs, md.getColumnType(i), i);
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
