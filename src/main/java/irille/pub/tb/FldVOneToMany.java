//Created on 2005-9-27
package irille.pub.tb;

import java.sql.ResultSet;
import java.sql.SQLException;

import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.view.VFld;

/**
 * Title: 一对多表虚拟字段<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldVOneToMany<T extends FldVOneToMany> extends FldV<T> {
	private Class<Bean> _beanBaseClass; // 一对多其他表的主键

	public FldVOneToMany(Class beanBaseClass, String code, String name) {
		super(code, name);
		_beanBaseClass = beanBaseClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		return (T) copy(new FldVOneToMany(_beanBaseClass, code, name));
	}

	@Override
	public Class getJavaType() {
		return _beanBaseClass;
	}

	@Override
	public Fld setTb(TbBase tb) {
		super.setTb(tb);
		tb.add(Tb.crtLongPkey()).setAutoCreate();
		return this;
	}

	/**
	 * 输出get与set的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		StringBuilder buf = new StringBuilder();
		String returnType = Str.getClazzLastCode(getJavaType());
		String firstUpper = getCodeFirstUpper();

		// 输出gt对象方法
		buf.append("  //取一对多表对象: " + returnType + LN);
		buf.append("  public " + returnType + " gt" + firstUpper + "(){" + LN);
		buf.append("    return ("+returnType+")gtLongTbObj(getPkey());" + LN);
		buf.append("  }" + LN);

		// 输出st对象方法
		buf.append("  public void st" + firstUpper + "(" + returnType + " "
				+ getCode() + "){" + LN);
		buf.append("      setPkey(" + getCode() + ".gtLongPkey());" + LN);
		buf.append("  }" + LN);
		return buf.toString();
	}

	@Override
	public VFld crtVFld() {
		return null;
	}

	@Override
	public Object tran(String value) {
		return null;
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return null;
	}
}
