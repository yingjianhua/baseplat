//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.view.VFld;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Vector;

/**
 * Title: 字段列表<br>
 * Description: 该字段会添加组合TB对象中的所有字段到当前TB中<br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldVFlds<T extends FldVFlds> extends FldV<T> {
	private Vector<Fld> _flds = new Vector();

	public FldVFlds(String code, IEnumFld... flds) {
		super(code, "字段列表" + code);
		add(flds);
	}

	public T add(IEnumFld... flds) {
		for (IEnumFld fld : flds) {
			_flds.add(fld.getFld());
		}
		return (T) this;
	}

	public T add(Tb tb, IEnumFld... flds) {
		HashSet<Fld> _without = new HashSet();
		for (IEnumFld fld : flds)
			_without.add(fld.getFld());
		for (Fld fld : tb.getFlds()) {
			if (!_without.contains(fld))
				_flds.add(fld);
		}
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		FldVFlds vfld = new FldVFlds(code);
		for (Fld fld : _flds) {
			vfld._flds.add(fld);
		}
		return (T) copy(vfld);
	}

	@Override
	public Fld setTb(TbBase tb) {
		super.setTb(tb);
		for (Fld fld : _flds) {
			tb.add(fld.copyNew(fld.getCode(), fld.getName())).setAutoCreate();
		}
		return this;
	}

	@Override
	public Class getJavaType() {
		return null;
	}

	/**
	 * 输出get与set的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		return "";
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
