package irille.pub.validate;

import irille.pub.bean.Bean;
import irille.pub.idu.Idu;
import irille.pub.tb.IEnumFld;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Title: 表单校验器基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public abstract class ValidBase<T extends ValidBase> {
	//private static final Log LOG = new Log(ValidBase.class);
	
	private Idu idu;
	private Bean bean;
	
	@SuppressWarnings("unused")
	private ValidBase() {
	}
	
	public ValidBase(Bean bean) {
		this.bean = bean;
	}
	public ValidBase(Idu idu) {
		this.idu = idu;
	}
	public void setB(Bean bean) {
		this.bean = bean;
	}
	protected Bean getB() {
		if(bean != null) return bean;
		return idu.getB();
	}

	protected Object getValue(IEnumFld fld) {
		return getB().propertyValue(fld.getFld());
	}
	protected String getString(IEnumFld fld) {
		return getB().propertyValue(fld.getFld()).toString();
	}
	protected Integer getInteger(IEnumFld fld) {
		return (Integer)getB().propertyValue(fld.getFld());
	}
	protected BigDecimal getBigDecimal(IEnumFld fld) {
		return (BigDecimal)getB().propertyValue(fld.getFld());
	}
	protected Date getDate(IEnumFld fld) {
		return (Date)getB().propertyValue(fld.getFld());
	}
	protected String getName(IEnumFld fld) {
		return fld.getFld().getName();
	}
	protected boolean fldsNotNull(IEnumFld... flds) {
		if (flds != null && flds.length > 0) return true;
		else return false;
	}
}
