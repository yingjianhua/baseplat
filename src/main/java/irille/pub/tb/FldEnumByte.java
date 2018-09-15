//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.Str;
import irille.pub.tb.IEnumOpt.IEnumBoolean;
import irille.pub.view.VFld;
import irille.pub.view.VFldEnumByte;

import java.util.Vector;

/**
 * Title: 枚举 Byte数据类型<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldEnumByte extends FldByte<FldEnumByte> {
	private short _width = -1;
	private IEnumOpt _enum;

	/**
	 * 构造方法
	 */
	public FldEnumByte(IEnumOpt enumObj, String code, String name) {
		super( Str.tranFirstLower(code), name);
		setDefaultValue(enumObj.getLine().getKey());
		_enum = enumObj;
	}

	@Override
	public boolean isOpt() {
		return true;
	}

	@Override
	public FldEnumByte copyNew(String code, String name) {
		return (FldEnumByte) copy(new FldEnumByte(_enum, code, name));
	}

	@Override
	protected FldEnumByte copy(Fld newObj) {
		return ((FldEnumByte) super.copy(newObj)).setWidth(_width);
	}

	public IEnumOpt getEnum() {
		return _enum;
	}

	@Override
	public String outTypeAndLen() {
		return "<" + _enum.getLine().outClazzAndCode() + "> "
				+ super.outTypeAndLen();
	}

	@Override
	public String outSrcVarDefine() {
		Vector<EnumLine> lines = _enum.getLine().getLines();
		StringBuilder buf = new StringBuilder();

		for (int i = 0; i < lines.size() && i < 20; i++) {
			buf.append("	// " + lines.get(i).getVarName() + ":"
					+ lines.get(i).getKey() + ","
					+ lines.get(i).getName() + LN);
		}
		return super.outSrcVarDefine() + buf.toString();
	}

	public String outSrcMethod() {
		StringBuilder buf = new StringBuilder();
		String firstUpper = getCodeFirstUpper();
		buf.append(super.outSrcMethod());
		if (_enum instanceof IEnumBoolean) { // 布尔型
			buf.append("  public Boolean gt" + firstUpper + "(){" + LN);
			buf.append("    return byteToBoolean(_" + getCode() + ");" + LN);
			buf.append("  }" + LN);
			buf.append("  public void st" + firstUpper + "(Boolean " + getCode()
					+ "){" + LN);
			buf.append("    _" + getCode() + "=booleanToByte(" + getCode() + ");" + LN);
			buf.append("  }" + LN);
			return buf.toString();
		}
		String clazz = _enum.getLine().getClazz();
		buf.append("  public " + clazz + " gt" + firstUpper + "(){" + LN);
		buf.append("    return (" + clazz + ")(" + clazz + "."
				+ _enum.getLine().getVarName() + ".getLine().get(_" + getCode()
				+ "));" + LN);
		buf.append("  }" + LN);
		buf.append("  public void st" + firstUpper + "(" + clazz + " "
				+ getCode() + "){" + LN);
		buf.append("    _" + getCode() + "=" + getCode() + ".getLine().getKey();"
				+ LN);
		buf.append("  }" + LN);
		return buf.toString();
	}

	@Override
	public VFld crtVFld() {
		return new VFldEnumByte(this); // XXX by whx
	}

	@Override
	public short getWidth() {
		if (_width < 0) { // 未初始化
//			_width=100;
			_width = _enum.getLine().getWidth(); // 取选项明细中最宽行的宽度
		}
		return _width;
	}

	public FldEnumByte setWidth(int width) {
		assertUnlocked();
		_width = (short) width;
		return this;
	}
	
	@Override
	public String getDefaultValueSourceCode() {
		return Str.getClazzLastCode(_enum.getClass())+".DEFAULT.getLine().getKey()";
	}
}
