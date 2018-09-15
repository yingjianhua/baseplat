//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.view.Fmts;
import irille.pub.view.VFld;
import irille.pub.view.VFldNum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


/**
 * Title: Byte数据类型<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class FldByte<T extends FldByte> extends Fld<T> {
	/**
	 * 构造方法
	 */
	public FldByte(String code, String name) {
		super(code,name);
		setFmt(Fmts.FMT_NUM);
		setDefaultValue("0");
	}

	@Override
  public Class getJavaType() {
	  return Byte.class;
  }
	@Override
  public int getSqlType() {
		  return Types.TINYINT;
  }
	@Override
  public String getTypeName() {
	  return "BYTE";
  }

	@SuppressWarnings("unchecked")
	@Override
  public T copyNew(String code, String name) {
		return (T)copy(new FldByte(code,name));
  }
	@Override
  public VFld crtVFld() {
	  return new VFldNum(this);
  }

	@Override
  public short getWidth() {
	  return 4;
  }

	@Override
	public Object tran(String value) {
		if (value == null)
			return null;
		return Byte.parseByte(value);
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getByte(columnName);
	}

}
