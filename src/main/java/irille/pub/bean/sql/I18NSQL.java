package irille.pub.bean.sql;

import irille.pub.bean.BeanMain;
import irille.pub.tb.FldLanguage;
import irille.pub.tb.FldLanguage.Language;
import irille.pub.tb.IEnumFld;


/**
 * from mybatis version 3.4.6
 * @author yingjianhua
 *
 */
public class I18NSQL extends SQL{

  private Language lang;
  
  public I18NSQL(Language lang) {
	  super();
	  this.lang = lang;
  }
  
  @Override
  public <T extends BeanMain<?, ?>> I18NSQL SELECT(Class<T> beanClass) {
	  IEnumFld[] flds = null;
	  for(Class<?> innerClass:beanClass.getDeclaredClasses()) {
		  if(innerClass.getSimpleName().equals("T") && innerClass.isEnum() && IEnumFld.class.isAssignableFrom(innerClass)) {
			  flds = (IEnumFld[])innerClass.getEnumConstants();
			  break;
		  }
	  }
	  if(flds != null) {
		  for(IEnumFld fld:flds) {
			  if(fld.getFld() instanceof FldLanguage)
				  super.SELECT(super.JSON_UNQUOTE(fld, lang.name()));
			  else
				  super.SELECT(fld);
		  }
	  } else {
		  super.SELECT(beanClass);
	  }
	  
	/*  try {
		  System.out.println(((Tb<?>)beanClass.getDeclaredField("TB").get(null)).getFlds());;
		for(IEnumFld fld:(IEnumFld[])((Tb<?>)beanClass.getDeclaredField("TB").get(null)).getFlds()) {
			if(fld instanceof FldLanguage)
				super.SELECT(super.JSON_UNQUOTE(fld, lang.name()));
			else
				super.SELECT(fld);
		  }
	} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
		super.SELECT(beanClass);
	}*/
	  return this;
  }
  
  @Override
  public SQL SELECT(IEnumFld... flds) {
	  for(IEnumFld fld:flds) {
		if(fld.getFld() instanceof FldLanguage)
			super.SELECT(super.JSON_UNQUOTE(fld, lang.name()));
		else
			super.SELECT(fld);
	  }
	  return this;
  }
}
