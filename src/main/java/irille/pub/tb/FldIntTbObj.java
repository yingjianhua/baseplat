//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.bean.BeanInt;
import irille.pub.tb.Infs.IOutTbClass;
import irille.pub.view.Fmts;
import irille.pub.view.VFld;
import irille.pub.view.VFldTbObj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

//注意 存的是主键值，没有附加表ID
public class FldIntTbObj<T extends FldIntTbObj> extends Fld<T> implements IOutTbClass {
	private Class _returnType;
	private short _width = 10;

	/**
	 * 构造方法
	 */
	public FldIntTbObj(String code, String name, Class returnType) {
		super(code, name);
		setFmt(Fmts.FMT_NUM);
		if (returnType != null)
			_returnType = returnType;
		else
			_returnType = BeanInt.class;
	}

	public FldIntTbObj(String code, String name) {
		this(code, name, BeanInt.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		return (T) copy(new FldIntTbObj(code, name, _returnType));
	}

	public Class getReturnType() {
		return _returnType;
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
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getInt(columnName);
	}
	
}
