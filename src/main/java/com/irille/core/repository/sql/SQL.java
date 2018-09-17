package com.irille.core.repository.sql;

import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanMain;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;

import java.io.Serializable;
import java.util.List;


/**
 * from mybatis version 3.4.6
 * @author yingjianhua
 *
 */
public class SQL {

  private MybatisSQL mybatisSQL;
  
  public SQL() {
	  mybatisSQL = new MybatisSQL(); 
  }
  public <T extends BeanMain<?, ?>> SQL DELETE_FROM(Class<T> beanClass) {
	  return mybatisSQL.DELETE_FROM(beanClass);
  }
  public <T extends BeanMain<?, ?>> SQL UPDATE(Class<T> beanClass) {
	  return mybatisSQL.UPDATE(beanClass);
  }
  public <T extends BeanMain<?, ?>> SQL SET(IEnumFld fld, Serializable param) {
	  mybatisSQL.SET(fld);
	  if(param instanceof IEnumOpt) {
		  IEnumOpt opt = (IEnumOpt)param;
		  mybatisSQL.PARAM(opt.getLine().getKey());
	  } else {
		  mybatisSQL.PARAM(param);
	  }
	  return mybatisSQL.getSelf();
  }
  public <T extends BeanMain<?, ?>> SQL SELECT(Class<T> beanClass) {
	  return mybatisSQL.SELECT(beanClass);
  }
  public SQL SELECT(String... columns) {
	  return mybatisSQL.SELECT(columns);
  }
  public SQL SELECT(IEnumFld fld, String alias) {
	  return mybatisSQL.SELECT(fld, alias);
  }
  
  public SQL SELECT(IEnumFld... flds) {
	  return mybatisSQL.SELECT(flds);
  }
  
  public <T extends BeanMain<?, ?>> SQL FROM(Class<T> beanClass) {
	  return mybatisSQL.FROM(beanClass);
  }
  
  public <T extends BeanMain<?, ?>> SQL LEFT_JOIN(Class<T> beanClass, IEnumFld fld1, IEnumFld fld2) {
	  return mybatisSQL.LEFT_OUTER_JOIN(beanClass, fld1, fld2);
  }
  
  public <T extends BeanMain<?, ?>> SQL WHERE(IEnumFld fld, String conditions) {
	  return mybatisSQL.WHERE(fld, conditions);
  }
  
  public <T extends BeanMain<?, ?>> SQL WHERE(IEnumFld fld, String conditions, Serializable... params) {
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
  
  public <T extends BeanMain<?, ?>> SQL WHERE(String conditions, Serializable... params) {
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
  
  public <T extends BeanMain<?, ?>> SQL ORDER_BY(IEnumFld fld, String type) {
	  return mybatisSQL.ORDER_BY(fld, type);
  }
  
  public <T extends BeanMain<?, ?>> SQL GROUP_BY(IEnumFld fld) {
	  return mybatisSQL.GROUP_BY(fld);
  }
  
  public <T extends BeanMain<?, ?>> SQL LIMIT(int start, int limit) {
	  return mybatisSQL.LIMIT(start, limit);
  }
  
  public <T extends BeanMain<?, ?>> SQL LOCK(boolean lock) {
	  return mybatisSQL.LOCK(lock);
  }
  
  public <T extends BeanMain<?, ?>> SQL PARAM(Serializable param) {
	  return mybatisSQL.PARAM(param);
  }
  
  public List<Serializable> PARAMS() {
	  return mybatisSQL.PARAMS();
  }
  
  public String JSON_EXTRACT(IEnumFld fld, String key) {
	  //return mybatisSQL.columnLabelWithAlias(fld)+"->"+key+" as "+mybatisSQL.columnLabel(fld);//老版本mysql不支持的语法
	  return "JSON_EXTRACT("+mybatisSQL.columnLabelWithAlias(fld)+", \"$."+key+"\") as "+mybatisSQL.columnLabel(fld);
  }
  public String JSON_UNQUOTE(IEnumFld fld, String key) {
	  return "JSON_UNQUOTE(JSON_EXTRACT("+mybatisSQL.columnLabelWithAlias(fld)+", \"$."+key+"\")) as "+mybatisSQL.columnLabel(fld);
  }
  
  
  @Override
  public String toString() {
	  return mybatisSQL.toString();
  }
  
  class MybatisSQL extends AbstractSQL<SQL> {

	private int start = 0;
	private int limit = 0;
	private boolean lock  = false;
	
	@Override
	public SQL getSelf() {
		return SQL.this;
	}
	public <T extends BeanMain<?, ?>> SQL SELECT(Class<T> beanClass) {
		return super.SELECT(alias(beanClass)+".*");
	}
	
	public SQL SELECT(String... columns) {
		return super.SELECT(columns);
	}

	public SQL SELECT(IEnumFld... flds) {
		String[] columns = new String[flds.length];
		for(int i=0;i<columns.length;i++) {
			columns[i] = columnLabelWithAlias(flds[i]);
		}
		return super.SELECT(columns);
	}
	
	public SQL SELECT(IEnumFld fld, String alias) {
		return super.SELECT(columnLabelWithAlias(fld)+" as "+alias);
	}
	
	public <T extends BeanMain<?, ?>> SQL FROM(Class<T> beanClass) {
		return super.FROM(tableNameWithAlias(beanClass));
	}
	
	public <T extends BeanMain<?, ?>> SQL LEFT_OUTER_JOIN(Class<T> beanClass, IEnumFld fld1, IEnumFld fld2) {
		return super.LEFT_OUTER_JOIN(tableNameWithAlias(beanClass)+" ON "+columnLabelWithAlias(fld1)+"="+columnLabelWithAlias(fld2));
	}
	
	public <T extends BeanMain<?, ?>> SQL WHERE(IEnumFld fld, String conditions) {
		if(isSelect())
			return super.WHERE(columnLabelWithAlias(fld)+" "+conditions);
		else
			return super.WHERE(columnLabel(fld)+" "+conditions);
	}
	
	public  <T extends BeanMain<?, ?>> SQL ORDER_BY(IEnumFld fld, String type) {
	  return super.ORDER_BY(columnLabelWithAlias(fld)+" "+type);
	}
	
	public  <T extends BeanMain<?, ?>> SQL GROUP_BY(IEnumFld fld) {
	  return super.GROUP_BY(columnLabelWithAlias(fld));
	}
	
	public <T extends BeanMain<?, ?>> SQL DELETE_FROM(Class<T> beanClass) {
		return super.DELETE_FROM(tableName(beanClass));
	}
	public <T extends BeanMain<?, ?>> SQL UPDATE(Class<T> beanClass) {
		return super.UPDATE(tableName(beanClass));
	}
	public SQL SET(IEnumFld fld) {
		return super.SET(columnLabel(fld)+"=?");
	}
	public <T extends BeanMain<?, ?>> SQL LIMIT(int start, int limit) {
		this.start = start;
		this.limit = limit;
		return getSelf();
	}
	public <T extends BeanMain<?, ?>> SQL LOCK(boolean lock) {
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
	private <T extends BeanMain<?, ?>> String alias(Class<T> beanClass) {
		return beanClass.getSimpleName();
	}
	
	private <T extends BeanMain<?, ?>> String tableName(Class<T> beanClass) {
		return BeanBase.tb(beanClass).getCodeSqlTb();
	}
	
	private <T extends BeanMain<?, ?>> String tableNameWithAlias(Class<T> beanClass) {
		return tableName(beanClass)+" "+alias(beanClass);
	}
	
	private <T extends BeanMain<?, ?>> String alias(IEnumFld fld) {
		Class<?> a = ((Enum<?>) fld).getDeclaringClass().getDeclaringClass();
		return a.getSimpleName();
	}
	
	private String columnLabel(IEnumFld fld) {
		return fld.name().toLowerCase();
	}
	
	private String columnLabelWithAlias(IEnumFld fld) {
		return alias(fld)+"."+columnLabel(fld);
	}
  }
}
