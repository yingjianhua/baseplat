//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.view.VFld;
import irille.pub.view.VFldText;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
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
public class FldBlob<T extends FldBlob> extends Fld<T> {
	private short _width=40;

	/**
	 * 构造方法
	 */
	public FldBlob(String code, String name,boolean null1) {
		super(code,name);
		setNull(null1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
  public T copyNew(String code, String name) {
		return (T)copy(new FldBlob(code,name,isNull()));
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
		  return Types.BLOB;
  }
	@Override
  public String getTypeName() {
	  return "BLOB";
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
		Blob blob = rs.getBlob(columnName);
	    byte[] returnValue = null;
	    if (null != blob) {
	      returnValue = blob.getBytes(1, (int) blob.length());
	    }
	    String r;
		try {
			r = new String(returnValue, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	    return r;
	}
}
