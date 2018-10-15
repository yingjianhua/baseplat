package com.irille.core.repository.sql;

import com.irille.core.repository.orm.Entity;
import com.irille.core.repository.orm.IColumnField;
import com.irille.core.repository.orm.Table;
import com.irille.core.repository.orm.columns.I18NColumn;
import com.irille.core.repository.query.IPredicate;

import irille.pub.ClassTools;
import irille.pub.tb.FldLanguage.Language;


/**
 * from mybatis version 3.4.6
 * @author yingjianhua
 *
 */
public class I18NEntitySQL extends EntitySQL{

  private Language lang;
  
  public I18NEntitySQL(Language lang) {
	  super();
	  this.lang = lang;
  }
  
//  @Override
//  public <T extends Entity> I18NEntitySQL SELECT(Class<T> entityClass) {
//	  IColumnField[] fields = null;
//	  Table table = (Table)ClassTools.getStaticProerty(entityClass, "table");
//	  for(Class<?> innerClass:entityClass.getDeclaredClasses()) {
//		  if(innerClass.getSimpleName().equals("field") && innerClass.isEnum() && IColumnField.class.isAssignableFrom(innerClass)) {
//			  fields = (IColumnField[])innerClass.getEnumConstants();
//			  break;
//		  }
//	  }
//	  if(fields != null) {
//		  for(IColumnField fld:fields) {
//			  if(fld.column() instanceof I18NColumn)
//				  super.SELECT(super.JSON_UNQUOTE(fld, lang.name()));
//			  else
//				  super.SELECT(fld);
//		  }
//	  } else {
//		  super.SELECT(entityClass);
//	  }
//	  
//	/*  try {
//		  System.out.println(((Tb<?>)beanClass.getDeclaredField("TB").get(null)).getFlds());;
//		for(IColumnField fld:(IColumnField[])((Tb<?>)beanClass.getDeclaredField("TB").get(null)).getFlds()) {
//			if(fld instanceof FldLanguage)
//				super.SELECT(super.JSON_UNQUOTE(fld, lang.name()));
//			else
//				super.SELECT(fld);
//		  }
//	} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
//		super.SELECT(beanClass);
//	}*/
//	  return this;
//  }
//  
//  @Override
//  public EntitySQL SELECT(IPredicate... predicates) {
//	  for(IPredicate predicate:predicates) {
//		if(fld.column() instanceof I18NColumn)
//			super.SELECT(super.JSON_UNQUOTE(fld, lang.name()));
//		else
//			super.SELECT(fld);
//	  }
//	  return this;
//  }
}
