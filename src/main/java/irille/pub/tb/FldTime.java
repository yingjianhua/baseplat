//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.view.VFld;
import irille.pub.view.VFldTime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;


/**
 * Title: Time数据类型<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class FldTime<T extends FldTime> extends Fld<T> {
	private boolean _dispDate=true;
	/**
	 * 构造方法
	 */
	public FldTime(String code, String name, boolean null1) {
		super(code,name);
		setNull(null1);
	}
	/**
	 * 构造方法
	 */
	public FldTime(String code, String name) {
		this(code,name,false);
	}
	@SuppressWarnings("unchecked")
	@Override
  public T copyNew( String code, String name) {
		return (T)copy(new FldTime(code,name,isNull()));
  }
	@Override
	protected T copy(Fld newObj) {
	  return (T) super.copy(newObj).setDispDate(_dispDate);
	}

	@Override
  public Class getJavaType() {
	  return Date.class;
  }
	@Override
  public int getSqlType() {
		  return Types.TIME;
  }
	@Override
  public String getTypeName() {
	  return "TIME";
  }
	
	@Override
  public VFld crtVFld() {
	  return new VFldTime(this);
  }
	@Override
  public short getWidth() {
	  return (short)(_dispDate?19:8);
  }

	@Override
	public Object tran(String value) {
		if (value == null)
			return null;
		return Time.parse(value);
	}
	public boolean isDispDate() {
  	return _dispDate;
  }
	public T setDispDate(boolean dispDate) {
		assertUnlocked();
  	_dispDate = dispDate;
  	return (T)this;
  }
	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		Timestamp sqlTimestamp = rs.getTimestamp(columnName);
	    if (sqlTimestamp != null) {
	      return new Date(sqlTimestamp.getTime());
	    }
	    return null;
	}
}
