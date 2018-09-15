//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.Str;
import irille.pub.view.VFld;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * Title: 组合字段<br>
 * Description: 该字段会添加组合TB对象中的所有字段到当前TB中, 并会有组合字段的对象操作<br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldVCmbFlds<T extends FldVCmbFlds> extends FldV<T> {
	private Tb _cmbTb;
	private HashSet<Fld> _without=null;
	private Boolean _cmbNull = null; //null 保持不变 ; false 统一设置成不可为空 ；true统一设置成可为空

	public FldVCmbFlds(String code, Tb cmbTb, IEnumFld... withoutFlds) {
		super(code, "字段组合"+code);
		_cmbTb = cmbTb;
		setWithout(withoutFlds);
	}

	public T setWithout(IEnumFld... flds) {
		_without=new HashSet();
		for(IEnumFld fld: flds )
			_without.add(fld.getFld());
		return (T)this;
	}
	public FldVCmbFlds setNullCmb(boolean isnull) {
		_cmbNull = isnull;
		return (T)this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		return (T) copy(new FldVCmbFlds(code,_cmbTb));
	}

	@Override
	public Fld setTb(TbBase tb) {
		super.setTb(tb);
		for (Fld fld : _cmbTb.getFlds()) {
			if (!fld.isAutoCreate() && !_without.contains(fld))
				if (_cmbNull == null)
					tb.add(fld.copyNew(fld.getCode(),fld.getName())).setAutoCreate();
				else
					tb.add(fld.copyNew(fld.getCode(),fld.getName())).setNull(_cmbNull).setAutoCreate();
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
		return "";
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
	public Tb getCmbTb() {
		return _cmbTb;
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return null;
	}
}
