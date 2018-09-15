//Created on 2005-9-27
package irille.pub.tb;

import java.sql.ResultSet;
import java.sql.SQLException;

import irille.pub.Log;
import irille.pub.Str;
import irille.pub.tb.Fld.KIND;
import irille.pub.view.FmtBase;
import irille.pub.view.VFld;

/**
 * Title: 计算字段，不能SET值<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldVCal<T extends FldVCal> extends FldV<T> {
	private static final Log LOG = new Log(FldVCal.class);
	private Fld _baseFld; //数据类型的参照字段
	private String _exp;

	public FldVCal(Fld baseFld,String exp, String code, String name) {
		super(code, name);
		_baseFld = baseFld;
		_exp=exp;
	}
	/* (non-Javadoc)
	 * @see irille.pub.tb.Fld#setTb(irille.pub.tb.TbBase)
	 */
	@Override
	public Fld setTb(TbBase tb) {
		return super.setTb(tb);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		return (T) copy(new FldVCal(_baseFld,_exp, code, name));
	}

	@Override
	public Class getJavaType() {
		return _baseFld.getJavaType();
	}
	@Override
  public short getWidth() {
	  return _baseFld.getWidth();
  }
	public final T setWidth( int width) {
		throw LOG.err();
	}
	@Override
	public KIND getKind() {
	  return KIND.CAL;
	}

	/* (non-Javadoc)
	 * @see irille.pub.tb.Fld#isOpt()
	 */
	@Override
	public boolean isOpt() {
		return _baseFld.isOpt();
	}
	
	/* (non-Javadoc)
	 * @see irille.pub.tb.Fld#getLen()
	 */
	@Override
	public int getLen() {
		return _baseFld.getLen();
	}
	
	/* (non-Javadoc)
	 * @see irille.pub.tb.Fld#getScale()
	 */
	@Override
	public int getScale() {
		return _baseFld.getScale();
	}
	/* (non-Javadoc)
	 * @see irille.pub.tb.Fld#getFmt()
	 */
	@Override
	public FmtBase getFmt() {
		return _baseFld.getFmt();
	}
	
	/* (non-Javadoc)
	 * @see irille.pub.tb.Fld#getType()
	 */
	@Override
	public irille.pub.tb.Fld.OOutType getType() {
		return _baseFld.getType();
	}
	
	/**
	 * 输出get与set的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		StringBuilder buf = new StringBuilder();
		String returnType = Str.getClazzLastCode(getJavaType());
		String firstUpper = getCodeFirstUpper();

		// 输出get方法
		buf.append("  //计算字段 " + returnType + LN);
		buf.append("  public " + returnType + " get" + firstUpper + "(){" + LN);
		buf.append("    return "+_exp+";" + LN);
		buf.append("  }" + LN);
		return buf.toString();
	}

	@Override
	public VFld crtVFld() {
		return _baseFld.crtVFld();
	}

	@Override
	public Object tran(String value) {
		return _baseFld.tran(value);
	}
	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return null;
	}
}
