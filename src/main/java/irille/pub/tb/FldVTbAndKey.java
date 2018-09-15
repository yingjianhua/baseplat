//Created on 2005-9-27
package irille.pub.tb;

import java.sql.ResultSet;
import java.sql.SQLException;

import irille.core.sys.Sys;
import irille.pub.bean.Bean;
import irille.pub.view.VFld;

/**
 * Title: 外表与主键字段<br>
 * Description: 该字段会产生后缀为Table与Pkey的真实的字段，还会产生GET与SET方法，同时产生一个虚拟的OBJ方法<br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldVTbAndKey<T extends FldVTbAndKey> extends FldV<T> {
	private Fld _table; // 表代码的字段
	private Fld _pkey; // 表主键的字段

	public FldVTbAndKey(String code, String name) {
		super(code, name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		return (T) copy(new FldVTbAndKey(code, name));
	}

	@Override
	public Fld setTb(TbBase tb) {
		super.setTb(tb);
		_table = tb.add(tb.crt(Sys.T.TABLE_ID, getCode() + "Table", getName()))
				.setAutoCreate();
		_pkey = tb.add(tb.crtInt(getCode() + "Pkey", getName() + "主键值"))
				.setAutoCreate();
		return this;
	}

	@Override
	public Class getJavaType() {
		return Bean.class;
	}

	/**
	 * 输出get与set的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		StringBuilder buf = new StringBuilder();
		String returnType = "Bean";
		String firstUpper = getCodeFirstUpper();

		// 输出get对象方法
		buf.append("  //根据表字段及主键字段的值取对象" + LN);
		buf.append("  public " + returnType + " gt" + firstUpper + "(){" + LN);
		buf.append("    if(get" + _pkey.getCodeFirstUpper() + "()==null)" + LN);
		buf.append("    	return null;" + LN);
		buf.append("    return gtTbAndPkeyObj(get" + _table.getCodeFirstUpper()
				+ "(),get" + _pkey.getCodeFirstUpper() + "());" + LN);
		buf.append("  }" + LN);
		buf.append("  public void st" + firstUpper + "(" + returnType + " "
				+ getCode() + "){" + LN);
		buf.append("    set" + _pkey.getCodeFirstUpper() + "((Integer)" + getCode()
				+ ".pkeyValues()[0]);" + LN);
		buf.append("    set" + _table.getCodeFirstUpper() + "(" + getCode()
				+ ".gtTbId());" + LN);
		buf.append("  }" + LN);
		return buf.toString();
	}

	// public Bean getFormNumbOBJ() {
	// return (Bean) get(getFormId(), getFormPkey());
	// }
	//
	// public void setFormNumbOBJ(BeanMain formNumb) {
	// setFormPkey(formNumb.getPkey().toString());
	// setFormId(formNumb.getID());
	// }

	/**
	 * @return the table
	 */
	public Fld getTable() {
		return _table;
	}

	/**
	 * @return the pkey
	 */
	public Fld getPkey() {
		return _pkey;
	}

	@Override
	public VFld crtVFld() {
		return null;
	}

	@Override
	public Object tran(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return null;
	}
}
