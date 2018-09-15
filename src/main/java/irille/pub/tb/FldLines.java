//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.tb.Infs.IOutTbClass;
import irille.pub.view.VFld;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;


/**
 * Title: 明细行<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldLines<T extends FldLines> extends Fld<T> {
	private IEnumFld _lineTbFld;
	private IEnumFld[] _orderByFlds;

	/**
	 * Int型外键, 默认不可为空
	 * 
	 * @param tb
	 * @param keyFld
	 * @param code
	 * @param name
	 * @param orderByFlds
	 *          排序字段
	 */
	public FldLines(IEnumFld lineTbFld, String code, String name,
			IEnumFld... orderByFlds) {
		super(code, name);
		_lineTbFld = lineTbFld;
		_orderByFlds = orderByFlds;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew( String code, String name) {
		return (T) copy(new FldLines( _lineTbFld, code, name, _orderByFlds));
	}

	public Fld getLineTbFld() {
		return _lineTbFld.getFld();
	}

	@Override
	public Class getJavaType() {
		return Vector.class;
	}

	@Override
	public int getSqlType() {
		return 0;
	}

	@Override
	public String getTypeName() {
		return "";
	}

	@Override
	public String outTypeAndLen() {
		return "<" + getName() + ":" + _lineTbFld.getFld().getTb().getCode() + ">";
	}

	@Override
	public String outSrcVarDefine() {
		return "";
	}

	@Override
	public KIND getKind() {
		return KIND.LINES;
	}

	/**
	 * 输出get的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		StringBuilder buf = new StringBuilder();
		String returnType = "java.util.List<" + _lineTbFld.getFld().getTb().getCode() + ">";
		String firstUpper = getCodeFirstUpper();
		Tb lTb = (Tb) _lineTbFld.getFld().getTb();
		String lType=((IOutTbClass)_lineTbFld.getFld()).getOutTbClazz().getName();

		// 输出get对象方法
		buf.append("  public static " + returnType + " get" + firstUpper + "("+lType+" mainBean){" + LN);
		buf.append("    return list(" + lTb.getClazz().getName() + ".class," + LN + "        \" "
		    + _lineTbFld.getFld().getCodeSqlField() + "=? ORDER BY " + getOrderByFlds() + "\",false," + LN
		    + "        mainBean.getPkey());" + LN);
		buf.append("  }" + LN);
		
		buf.append("  public static " + returnType + " get" + firstUpper + "("+lType+" mainBean, int idx,int count){" + LN);
		buf.append("    return list(" + lTb.getClazz().getName() + ".class,false,\" " + 
				_lineTbFld.getFld().getCodeSqlField() + "=? ORDER BY " + getOrderByFlds() + " DESC\",idx,count,mainBean.getPkey());" + LN);
		buf.append("  }" + LN);
		
		buf.append("  public static int get" + firstUpper + "Count("+lType+" mainBean){" + LN);
		buf.append("    return ((Number) queryOneRow(\"SELECT count(*) FROM " + 
				lTb.getCodeSqlTb() + " WHERE " + _lineTbFld.getFld().getCodeSqlField() + "=? ORDER BY " + getOrderByFlds() + "\",mainBean.getPkey())[0]).intValue();" + LN);
		buf.append("  }" + LN);
		return buf.toString();
	}

	public String getOrderByFlds() {
		if (_orderByFlds.length == 0)
			return "";
		// 将orderBy的字段转换为类似：“code DESC,short_name,je”的形式
		String s = "";
		for (IEnumFld fld : _orderByFlds) {
			s += "," + fld.getFld().getCodeSqlField();
			if (!fld.getFld().isIndexAscending())
				s += " DESC";
		}
		return s.substring(1);
	}

	@Override
	public VFld crtVFld() {
		return null;
	}

	@Override
	public short getWidth() {
		return 20;
	}
	

	@Override
	public Object tran(String value) {
		return value;
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return null;
	}

}
