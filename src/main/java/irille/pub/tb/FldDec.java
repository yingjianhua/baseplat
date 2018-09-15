//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.view.Fmts;
import irille.pub.view.VFld;
import irille.pub.view.VFldDec;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Title: 浮点数数据类型<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldDec extends Fld<FldDec> {
	private byte _len; // 长度
	private byte _scale; // 小数位数

	/**
	 * 构造方法
	 */
	public FldDec(String code, String name, int len, int scale) {
		super(code, name);
		_len = (byte) len;
		_scale = (byte) scale;
		setFmt(Fmts.FMT_DEC);
		setDefaultValue("ZERO");
	}
	
	public FldDec(String code, String name, int len, int scale, boolean null1) {
		super(code, name);
		_len = (byte) len;
		_scale = (byte) scale;
		setFmt(Fmts.FMT_DEC);
		setNull(null1);
	}

	@Override
	public FldDec copyNew(String code, String name) {
		return (FldDec) copy(new FldDec(code, name, getLen(), getScale()));
	}

	@Override
	public Class getJavaType() {
		return BigDecimal.class;
	}

	@Override
	public int getSqlType() {
		return Types.DECIMAL;
	}

	@Override
	public String getTypeName() {
		return "DEC";
	}

	public int getLen() {
		return _len;
	}

	public int getScale() {
		return _scale;
	}

	@Override
	public VFld crtVFld() {
		VFld vfld = new VFldDec(this);
		vfld.setType(getType());
		return vfld;
	}

	@Override
	public short getWidth() {
		return (short) (getLen() + (getScale() == 0 ? 1 : 2));
	}

	@Override
	public Object tran(String value) {
		if (value == null)
			return null;
		return new BigDecimal(value);
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getBigDecimal(columnName);
	}
	
}
