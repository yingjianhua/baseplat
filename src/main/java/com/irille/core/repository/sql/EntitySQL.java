package com.irille.core.repository.sql;

import java.io.Serializable;
import java.util.List;

import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.orm.IColumnField;

import irille.pub.bean.BeanBase;
import irille.pub.tb.IEnumOpt;


/**
 * from mybatis version 3.4.6
 * @author yingjianhua
 *
 */
public class EntitySQL {

  private MybatisSQL mybatisSQL;
  
  public EntitySQL() {
	  mybatisSQL = new MybatisSQL(); 
  }
  public <T extends Entity> EntitySQL DELETE_FROM(Class<T> beanClass) {
	  return mybatisSQL.DELETE_FROM(beanClass);
  }
  public <T extends Entity> EntitySQL UPDATE(Class<T> beanClass) {
	  return mybatisSQL.UPDATE(beanClass);
  }
  public <T extends Entity> EntitySQL SET(IColumnField fld, Serializable param) {
	  mybatisSQL.SET(fld);
	  if(param instanceof IEnumOpt) {
		  IEnumOpt opt = (IEnumOpt)param;
		  mybatisSQL.PARAM(opt.getLine().getKey());
	  } else {
		  mybatisSQL.PARAM(param);
	  }
	  return mybatisSQL.getSelf();
  }
  public <T extends Entity> EntitySQL SELECT(Class<T> beanClass) {
	  return mybatisSQL.SELECT(beanClass);
  }
  public EntitySQL SELECT(String... columns) {
	  return mybatisSQL.SELECT(columns);
  }
  public EntitySQL SELECT(IColumnField fld, String alias) {
	  return mybatisSQL.SELECT(fld, alias);
  }
  
  public EntitySQL SELECT(IColumnField... flds) {
	  return mybatisSQL.SELECT(flds);
  }
  
  public <T extends Entity> EntitySQL FROM(Class<T> beanClass) {
	  return mybatisSQL.FROM(beanClass);
  }
  
  public <T extends Entity> EntitySQL LEFT_JOIN(Class<T> beanClass, IColumnField fld1, IColumnField fld2) {
	  return mybatisSQL.LEFT_OUTER_JOIN(beanClass, fld1, fld2);
  }
  public <T extends Entity> EntitySQL INNER_JOIN(Class<T> beanClass, IColumnField fld1, IColumnField fld2) {
	  return mybatisSQL.INNER_JOIN(beanClass, fld1, fld2);
  }
  
  public <T extends Entity> EntitySQL WHERE(IColumnField fld, String conditions) {
	  return mybatisSQL.WHERE(fld, conditions);
  }
  
  public <T extends Entity> EntitySQL WHERE(IColumnField fld, String conditions, Serializable... params) {
	  mybatisSQL.WHERE(fld, conditions);
	  for(Serializable param:params) {
		  if(param instanceof IEnumOpt) {
			  IEnumOpt opt = (IEnumOpt)param;
			  mybatisSQL.PARAM(opt.getLine().getKey());
		  } else {
			  mybatisSQL.PARAM(param);
		  }
	  }
	  return mybatisSQL.getSelf();
  }
  
  public <T extends Entity> EntitySQL WHERE(String conditions, Serializable... params) {
	  mybatisSQL.WHERE(conditions);
	  for(Serializable param:params) {
		  if(param instanceof IEnumOpt) {
			  IEnumOpt opt = (IEnumOpt)param;
			  mybatisSQL.PARAM(opt.getLine().getKey());
		  } else {
			  mybatisSQL.PARAM(param);
		  }
	  }
	  return mybatisSQL.getSelf();
  }
  
  public <T extends Entity> EntitySQL ORDER_BY(IColumnField fld, String type) {
	  return mybatisSQL.ORDER_BY(fld, type);
  }
  
  public <T extends Entity> EntitySQL GROUP_BY(IColumnField fld) {
	  return mybatisSQL.GROUP_BY(fld);
  }
  
  public <T extends Entity> EntitySQL LIMIT(int start, int limit) {
	  return mybatisSQL.LIMIT(start, limit);
  }
  
  public <T extends Entity> EntitySQL LOCK(boolean lock) {
	  return mybatisSQL.LOCK(lock);
  }
  
  public <T extends Entity> EntitySQL PARAM(Serializable param) {
	  return mybatisSQL.PARAM(param);
  }
  
  public List<Serializable> PARAMS() {
	  return mybatisSQL.PARAMS();
  }
  
  public String JSON_EXTRACT(IColumnField fld, String key) {
	  //return mybatisSQL.columnLabelWithAlias(fld)+"->"+key+" as "+mybatisSQL.fld.column().columnName();//老版本mysql不支持的语法
	  return "JSON_EXTRACT("+mybatisSQL.columnLabelWithAlias(fld)+", \"$."+key+"\") as "+fld.column().columnName();
  }
  public String JSON_UNQUOTE(IColumnField fld, String key) {
	  return "JSON_UNQUOTE(JSON_EXTRACT("+mybatisSQL.columnLabelWithAlias(fld)+", \"$."+key+"\")) as "+fld.column().columnName();
  }
  
  
  @Override
  public String toString() {
	  return mybatisSQL.toString();
  }
  
  class MybatisSQL extends AbstractSQL<EntitySQL> {

	private int start = 0;
	private int limit = 0;
	private boolean lock  = false;
	
	@Override
	public EntitySQL getSelf() {
		return EntitySQL.this;
	}
	public <T extends Entity> EntitySQL SELECT(Class<T> beanClass) {
		return super.SELECT(alias(beanClass)+".*");
	}
	
	public EntitySQL SELECT(String... columns) {
		return super.SELECT(columns);
	}

	public EntitySQL SELECT(IColumnField... flds) {
		String[] columns = new String[flds.length];
		for(int i=0;i<columns.length;i++) {
			columns[i] = columnLabelWithAlias(flds[i]);
		}
		return super.SELECT(columns);
	}
	
	public EntitySQL SELECT(IColumnField fld, String alias) {
		return super.SELECT(fld.column().columnName()+" as "+alias);
	}
	
	public <T extends Entity> EntitySQL FROM(Class<T> beanClass) {
		return super.FROM(tableNameWithAlias(beanClass));
	}
	
	public <T extends Entity> EntitySQL LEFT_OUTER_JOIN(Class<T> beanClass, IColumnField fld1, IColumnField fld2) {
		return super.LEFT_OUTER_JOIN(tableNameWithAlias(beanClass)+" ON "+columnLabelWithAlias(fld1)+"="+columnLabelWithAlias(fld2));
	}
	public <T extends Entity> EntitySQL INNER_JOIN(Class<T> beanClass, IColumnField fld1, IColumnField fld2) {
		return super.INNER_JOIN(tableNameWithAlias(beanClass)+" ON "+columnLabelWithAlias(fld1)+"="+columnLabelWithAlias(fld2));
	}
	
	public <T extends Entity> EntitySQL WHERE(IColumnField fld, String conditions) {
		if(isSelect())
			return super.WHERE(columnLabelWithAlias(fld)+" "+conditions);
		else
			return super.WHERE(fld.column().columnName()+" "+conditions);
	}
	
	public  <T extends Entity> EntitySQL ORDER_BY(IColumnField fld, String type) {
	  return super.ORDER_BY(columnLabelWithAlias(fld)+" "+type);
	}
	
	public  <T extends Entity> EntitySQL GROUP_BY(IColumnField fld) {
	  return super.GROUP_BY(columnLabelWithAlias(fld));
	}
	
	public <T extends Entity> EntitySQL DELETE_FROM(Class<T> beanClass) {
		return super.DELETE_FROM(tableName(beanClass));
	}
	public <T extends Entity> EntitySQL UPDATE(Class<T> beanClass) {
		return super.UPDATE(tableName(beanClass));
	}
	public EntitySQL SET(IColumnField fld) {
		return super.SET(fld.column().columnName()+"=?");
	}
	public <T extends Entity> EntitySQL LIMIT(int start, int limit) {
		this.start = start;
		this.limit = limit;
		return getSelf();
	}
	public <T extends Entity> EntitySQL LOCK(boolean lock) {
		this.lock = lock;
		return getSelf();
	}
	@Override
	public String toString() {
		String sql = super.toString();
		if(isSelect() && limit > 0)
			sql += " LIMIT " + start + "," + limit;
		if(lock)
			sql += " FOR UPDATE";
		return sql;
	}
	private <T extends Entity> String alias(Class<T> beanClass) {
		return beanClass.getSimpleName();
	}
	
	private <T extends Entity> String tableName(Class<T> beanClass) {
		return BeanBase.tb(beanClass).getCodeSqlTb();
	}
	
	private <T extends Entity> String tableNameWithAlias(Class<T> beanClass) {
		return tableName(beanClass)+" "+alias(beanClass);
	}
	
	private <T extends Entity> String alias(IColumnField fld) {
		Class<?> a = ((Enum<?>) fld).getDeclaringClass().getDeclaringClass();
		return a.getSimpleName();
	}
	
	private String columnLabelWithAlias(IColumnField fld) {
		return alias(fld)+"."+fld.column().columnName();
	}
  }
}
