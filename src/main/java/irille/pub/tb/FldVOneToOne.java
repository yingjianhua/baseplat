//Created on 2005-9-27
package irille.pub.tb;

import java.sql.ResultSet;
import java.sql.SQLException;

import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.view.VFld;
import irille.pub.view.VFldOutKey;
import irille.pub.view.VFldStr;

/**
 * Title: 一对一表虚拟字段<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldVOneToOne<T extends FldVOneToOne> extends FldV<T> {
	private Class<Bean> _otherBeanClass; // 一对一其他表的主键
	private boolean _isSp = false; //表示为特殊的一对一字段，针对机构与部门等与SYSCOM

	public FldVOneToOne(Class otherBeanClass, String code, String name) {
		super(code, name);
		_otherBeanClass = otherBeanClass;
	}
	public FldVOneToOne(Class otherBeanClass, String code, String name, boolean isSp) {
		super(code, name);
		_otherBeanClass = otherBeanClass;
		_isSp = isSp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		return (T) copy(new FldVOneToOne(_otherBeanClass, code, name, _isSp));
	}

	@Override
	public Class getJavaType() {
		return _otherBeanClass;
	}

	@Override
	public Fld setTb(TbBase tb) {
		super.setTb(tb);
		if (_isSp)
			return this;
		Fld pkey = Tb.getTBByBean(_otherBeanClass).get(PKEY).copyNew();
		tb.add(pkey).setAutoCreate();
		return this;
	}

	/**
	 * 输出get与set的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		if (_isSp)
			return "";
		StringBuilder buf = new StringBuilder();
		String returnType = Str.getClazzLastCode(getJavaType());
		String firstUpper = getCodeFirstUpper();

		// 输出gt对象方法
		buf.append("  //取一对一表对象: " + returnType + LN);
		buf.append("  public " + returnType + " gt" + firstUpper + "(){" + LN);
		buf.append("    return get(" + returnType + ".class,getPkey());" + LN);
		buf.append("  }" + LN);

		// 输出st对象方法
		buf.append("  public void st" + firstUpper + "(" + returnType + " "
				+ getCode() + "){" + LN);
		buf.append("      setPkey(" + getCode() + ".getPkey());" + LN);
		buf.append("  }" + LN);
		return buf.toString();
	}

	@Override
	public VFld crtVFld() {
		VFld vfld = new VFldStr(this);
		return vfld;
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
