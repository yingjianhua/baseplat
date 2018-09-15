//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.view.VFld;
import irille.pub.view.VFldKeyStr;
import irille.pub.view.VFldStr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


/**
 * Title: Str数据类型<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class FldStr<T extends FldStr> extends Fld<T> {
	private short _width;
	private int _len; // 长度
	/**
	 * 构造方法
	 */
	public FldStr(String code, String name,int len,boolean null1) {
		super(code,name);
		_len=len;
		_width=(short)len;
		setNull(null1);
	}
	@SuppressWarnings("unchecked")
	@Override
  public T copyNew( String code, String name) {
		return (T)copy(new FldStr(code,name,_len,isNull()));
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
		  return Types.VARCHAR;
  }
	@Override
  public String getTypeName() {
	  return "STR";
  }
	public int getLen() {
  	return _len;
  }
	@Override
  public VFld crtVFld() {
		if (getCode().equals("pkey"))
			return new VFldKeyStr(this);
	  return new VFldStr(this);
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

	public Fld setLen(int len) {
		assertUnlocked();
		_len=len;
		return this;
	}
	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getString(columnName);
	}
}
