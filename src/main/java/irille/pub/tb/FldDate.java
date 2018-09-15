//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.view.Fmts;
import irille.pub.view.VFld;
import irille.pub.view.VFldDate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;


/**
 * Title: Date数据类型<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldDate<T extends FldDate> extends Fld<T> {

	/**
	 * 构造方法
	 */
	public FldDate(String code, String name, boolean null1) {
		super(code, name);
		setNull(null1);
		setFmt(Fmts.FMT_YYYY_MM_DD);
	}
	public FldDate(String code, String name) {
		this(code, name, false);
	}
	@SuppressWarnings("unchecked")
	@Override
  public T copyNew(String code, String name) {
		return (T)copy(new FldDate(code,name,isNull()));
  }

	@Override
	public Class getJavaType() {
		return Date.class;
	}

	@Override
	public int getSqlType() {
		return Types.DATE;
	}

	@Override
	public String getTypeName() {
		return "DATE";
	}
	@Override
  public VFld crtVFld() {
	  return new VFldDate(this);
  }

	@Override
  public short getWidth() {
	  return 10;
  }

	@Override
	public Object tran(String value) {
		if (value == null)
			return null;
		return Date.parse(value);
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
