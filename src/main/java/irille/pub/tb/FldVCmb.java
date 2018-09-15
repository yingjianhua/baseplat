//Created on 2005-9-27
package irille.pub.tb;

import java.sql.ResultSet;
import java.sql.SQLException;

import irille.pub.Str;
import irille.pub.view.VFld;

/**
 * Title: 组合字段<br>
 * Description: 该字段会添加组合TB对象中的所有字段到当前TB中, 各字段名加指定前缀,还会产生gt与st对象的方法<br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldVCmb<T extends FldVCmb> extends FldV<T> {
	private TbBase _cmbTb;
	private Boolean _cmbNull = null; //null 保持不变 ; false 统一设置成不可为空 ；true统一设置成可为空

	public FldVCmb(String code, String name, TbBase cmbTb) {
		super(code, name);
		_cmbTb = cmbTb;
	}

	public FldVCmb setNullCmb(boolean isnull) {
		_cmbNull = isnull;
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		return (T) copy(new FldVCmb(code, name, _cmbTb));
	}

	@Override
	public Fld setTb(TbBase tb) {
		super.setTb(tb);
		for (Fld fld : _cmbTb.getFlds()) {
			if (!fld.isAutoCreate()) {
				if (_cmbNull == null)
					tb.add(fld.copyNew(getCode() + Str.tranFirstUpper(fld.getCode()), getName() + fld.getName())).setAutoCreate();
				else
					tb.add(
					    fld.copyNew(getCode() + Str.tranFirstUpper(fld.getCode()), getName() + fld.getName()).setNull(_cmbNull))
					    .setAutoCreate();
			}
		}
		return this;
	}

	@Override
	public Class getJavaType() {
		return _cmbTb.getClazz();
	}

	/**
	 * 输出get与set的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		StringBuilder buf = new StringBuilder();
		String returnType = Str.getClazzLastCode(getJavaType());
		String firstUpper = getCodeFirstUpper();

		// 输出get对象方法
		buf.append("  //组合对象的操作" + LN);
		buf.append("  public " + returnType + " gt" + firstUpper + "(){" + LN);
		buf.append("    " + returnType + " b=new " + returnType + "();" + LN);
		for (Fld fld : _cmbTb._flds) {
			String code = Str.tranFirstUpper(fld.getCode());
			if (fld.isDatabaseField())
				buf.append("    	b.set" + code + "(_" + getCode() + code + ");" + LN);
		}
		buf.append("    return b;" + LN);
		buf.append("  }" + LN);
		buf.append("  public void st" + firstUpper + "(" + returnType + " " + getCode() + "){" + LN);
		for (Fld fld : _cmbTb._flds) {
			String code = Str.tranFirstUpper(fld.getCode());
			if (fld.isDatabaseField())
				buf.append("    _" + getCode() + code + "=" + getCode() + ".get" + code + "();" + LN);
		}
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

	@Override
	public VFld crtVFld() {
		return null;
	}

	@Override
	public Object tran(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the cmbTb
	 */
	public TbBase getCmbTb() {
		return _cmbTb;
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return null;
	}
}
