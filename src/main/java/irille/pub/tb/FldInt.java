//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.view.Fmts;
import irille.pub.view.VFld;
import irille.pub.view.VFldKeyInt;
import irille.pub.view.VFldNum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Title: Int数据类型<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class FldInt<T extends FldInt> extends Fld<T> {
	private short _width = 8;

	/**
	 * 构造方法
	 */
	public FldInt( String code, String name) {
		super(code, name);
		setFmt(Fmts.FMT_NUM);
		setDefaultValue(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		return (T) copy(new FldInt(code, name));
	}

	@Override
	protected T copy(Fld newObj) {
		return (T) super.copy(newObj).setWidth(_width);
	}

	@Override
	public Class getJavaType() {
		return Integer.class;
	}

	@Override
	public int getSqlType() {
		return Types.INTEGER;
	}

	@Override
	public String getTypeName() {
		return "INT";
	}

	@Override
	public VFld crtVFld() {
		if (getCode().equals("pkey"))
			return new VFldKeyInt(this);
		return new VFldNum(this);
	}

	@Override
	public short getWidth() {
		return _width;
	}

	public T setWidth(int width) {
		assertUnlocked();
		_width = (short) width;
		return (T) this;
	}

	@Override
	public Object tran(String value) {
		if (value == null)
			return null;
		return Integer.parseInt(value);
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getInt(columnName);
	}
}
