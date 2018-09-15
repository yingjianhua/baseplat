//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.view.VFld;
import irille.pub.view.VFldText;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


/**
 * Title: Text数据类型<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class FldClob<T extends FldClob> extends Fld<T> {
	private short _width=40;

	/**
	 * 构造方法
	 */
	public FldClob(String code, String name,boolean null1) {
		super(code,name);
		setNull(null1);
	}

	@SuppressWarnings("unchecked")
	@Override
  public T copyNew(String code, String name) {
		return (T)copy(new FldClob(code,name,isNull()));
  }
	@Override
	protected T copy(Fld newObj) {
	  return (T) super.copy(newObj).setWidth(_width);
	}

	@Override
  public Class getJavaType() {
	  return String.class;
  }
	@Override
  public int getSqlType() {
		  return Types.CLOB;
  }
	@Override
  public String getTypeName() {
	  return "CLOB";
  }
	@Override
  public VFld crtVFld() {
	  return new VFldText(this);
  }
	@Override
  public short getWidth() {
	  return _width;
  }
	public T setWidth( int width) {
		assertUnlocked();
		_width=(short)width;
		return (T)this;
	}
	@Override
	public Object tran(String value) {
		return value;
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		 String value = "";
		 Clob clob = rs.getClob(columnName);
		 if (clob != null) {
			 int size = (int) clob.length();
			 value = clob.getSubString(1, size);
		 }
		 return value;
	}
}
