//Created on 2005-9-27
package irille.pub.tb;

import java.sql.ResultSet;
import java.sql.SQLException;

import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.tb.Infs.IOutTbClass;
import irille.pub.view.VFld;
import irille.pub.view.VFldOutKey;

/**
 * Title: Int数据类型<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldOutKey extends Fld<FldOutKey> implements IOutTbClass{
	private static final Log LOG = new Log(FldOutKey.class);
	private short _width = 20;
	private Class _outTbClass;

	/**
	 * Int型外键, 默认不可为空
	 * 
	 * @param tb
	 * @param outTbClass
	 * @param code
	 * @param name
	 */
	public FldOutKey(Class outTbClass, String code, String name) {
		this(outTbClass, code, name, false);
	}

	public FldOutKey(Tb tb, String code, String name) {
		this(tb.getClazz(), code, name, false);
	}

	/**
	 * Int型外键
	 * 
	 * @param tb
	 * @param keyFld
	 * @param code
	 * @param name
	 */
	public FldOutKey(Class outTbClass, String code, String name,
			boolean null1) {
		super(code, name);
		_outTbClass = outTbClass;
		setNull(null1);
		setViewOutCombo();
	}

	@Override
	public FldOutKey copyNew(String code, String name) {
		return (FldOutKey) copy(new FldOutKey( _outTbClass, code, name, isNull()));
	}

	@Override
	protected FldOutKey copy(Fld newObj) {
		return (FldOutKey) super.copy(newObj).setWidth(_width);
	}

	public Fld getOutKeyFld() {
		return Bean.tb(_outTbClass).get("pkey");
	}

	public Class getOutTbClazz() {
		return _outTbClass;
	}

	@Override
	public boolean isOutKey() {
		return true;
	}

	@Override
	public Class getJavaType() {
		return getOutKeyFld().getJavaType();
	}

	@Override
	public int getSqlType() {
		return getOutKeyFld().getSqlType();
	}

	@Override
	public String getTypeName() {
		return getOutKeyFld().getTypeName();
	}

	@Override
	public int getLen() {
		return getOutKeyFld().getLen();
	}

	@Override
	public int getScale() {
		return getOutKeyFld().getScale();
	}

	@Override
	public String outTypeAndLen() {
		return "<表主键:" + getOutKeyFld().getTb().getCode() + "> "
				+ super.outTypeAndLen();
	}

	/**
	 * 输出get与set的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		StringBuilder buf = new StringBuilder();
		String returnType = getOutKeyFld().getTb().getCode();
		String firstUpper = getCodeFirstUpper();

		// 输出gt对象方法
		buf.append("  public " + returnType + " gt" + firstUpper + "(){" + LN);
		buf.append("    if(get" + getCodeFirstUpper() + "()==null)" + LN);
		buf.append("      return null;" + LN);
		buf.append("    return (" + returnType + ")get(" + returnType
				+ ".class,get" + getCodeFirstUpper() + "());" + LN);
		buf.append("  }" + LN);
		// 输出st对象方法
		buf.append("  public void st" + firstUpper + "(" + returnType + " "
				+ getCode() + "){" + LN);
		buf.append("    if(" + getCode() + "==null)" + LN);
		buf.append("      set" + firstUpper + "(null);" + LN);
		buf.append("    else" + LN);
		buf.append("      set" + firstUpper + "(" + getCode() + ".getPkey());"
				+ LN);
		buf.append("  }" + LN);
		return super.outSrcMethod() + buf.toString();
	}

	@Override
	public VFld crtVFld() {
		VFld vfld = new VFldOutKey(this);
		vfld.setType(getType());
		return vfld;
	}

	@Override
	public short getWidth() {
		return _width;
	}

	public FldOutKey setWidth(int width) {
		assertUnlocked();
		_width = (short) width;
		return this;
	}

	@Override
	public Object tran(String value) {
		return value; // XXX
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return getOutKeyFld().getResult(rs, columnName);
	}
}
