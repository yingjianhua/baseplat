//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.view.VFld;
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
public class FldChar<T extends FldChar> extends Fld<T> {
	private short _width;
	private int _len; // 长度
	/**
	 * 构造方法
	 */
	public FldChar(String code, String name,int len,boolean null1) {
		super(code,name);
		_len=len;
		_width=(short)len;
		setNull(null1);
	}

	@SuppressWarnings("unchecked")
	@Override
  public T copyNew( String code, String name) {
		return (T)copy(new FldChar(code,name,_len,isNull()));
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
		  return Types.CHAR;
  }
	@Override
  public String getTypeName() {
	  return "CHAR";
  }
	public int getLen() {
  	return _len;
  }
	@Override
  public VFld crtVFld() {
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

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		 return rs.getString(columnName);
	}

}
