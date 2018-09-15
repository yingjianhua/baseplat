//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.Str;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Infs.IOutTbClass;
import irille.pub.view.Fmts;
import irille.pub.view.VFld;
import irille.pub.view.VFldTbObj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Title: 外部主键对象，根据算法分析出所属表及主键！<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class FldLongTbObj<T extends FldLongTbObj> extends Fld<T> implements IOutTbClass {
	private Class _returnType;
	private short _width = 10;

	/**
	 * 构造方法
	 */
	public FldLongTbObj(String code, String name, Class returnType) {
		super(code, name);
		setFmt(Fmts.FMT_NUM);
		if (returnType != null)
			_returnType = returnType;
		else
			_returnType = BeanLong.class;
	}

	public FldLongTbObj(String code, String name) {
		this(code, name, BeanLong.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		return (T) copy(new FldLongTbObj(code, name, _returnType));
	}

	public Class getReturnType() {
		return _returnType;
	}

	@Override
	protected T copy(Fld newObj) {
		return (T) super.copy(newObj).setWidth(_width);
	}

	/**
	 * 输出get与set的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		StringBuilder buf = new StringBuilder();
		String firstUpper = getCodeFirstUpper();
		String returnType = Str.getClazzLastCode(_returnType);

		// 输出gt对象方法
		buf.append("  //外部主键对象: " + returnType + LN);
		buf.append("  public Bean gt" + firstUpper + "(){" + LN);
		buf.append("    return (Bean)gtLongTbObj(get" + firstUpper + "());" + LN);
		buf.append("  }" + LN);

		// 输出st对象方法  setTbObjLong(Long.parseLong(tbObjLong.getPkey().toString()));
		buf.append("  public void st" + firstUpper + "(Bean " + getCode() + "){" + LN);
		buf.append("      set" + firstUpper + "(" + getCode() + ".gtLongPkey());" + LN);
		buf.append("  }" + LN);
		return super.outSrcMethod() + buf.toString();
	}

	@Override
	public Class getJavaType() {
		return Long.class;
	}

	@Override
	public int getSqlType() {
		return Types.BIGINT;
	}

	@Override
	public String getTypeName() {
		return "LONG";
	}

	@Override
	public VFld crtVFld() {
		VFld vfld = new VFldTbObj(this);
		vfld.setType(OOutType.COMBO);
		return vfld;
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
		return Long.parseLong(value);
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.tb.Infs.IFldOutKey#getOutKeyClazz()
	 */
	@Override
	public Class getOutTbClazz() {
		return _returnType;
	}

	@Override
	public String getDefaultValueSourceCode() {
		return null;
	}
	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getLong(columnName);
	}
}
