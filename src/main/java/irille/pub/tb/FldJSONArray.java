//Created on 2005-9-27
package irille.pub.tb;

import java.sql.ResultSet;
import java.sql.SQLException;

import irille.pub.view.Fmts;
import irille.pub.view.VFld;
import irille.pub.view.VFldNum;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * 存放jsonArray格式字符串的字段
 * @author yingjianhua
 */
public class FldJSONArray<T extends FldJSONArray> extends Fld<T> {
	/**
	 * 不能与java.sql.Types中的值重复 
	 */
	public static final int JSONARRAY = 2101;
	private short _width = 8;

	/**
	 * 构造方法
	 */
	public FldJSONArray( String code, String name) {
		super(code, name);
		setFmt(Fmts.FMT_NUM);
		setDefaultValue(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		return (T) copy(new FldJSONArray(code, name));
	}

	@Override
	protected T copy(Fld newObj) {
		return (T) super.copy(newObj).setWidth(_width);
	}

	/**
	 * 输出get与set的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		StringBuilder buf = new StringBuilder();
		String returnType = "JSONArray";
		String firstUpper = getCodeFirstUpper();

		// 输出gt对象方法
		buf.append("  public " + returnType + " gt" + firstUpper + "(){" + LN);
		buf.append("    if(get" + getCodeFirstUpper() + "()==null)" + LN);
		buf.append("      return null;" + LN);
		buf.append("	try {" + LN);
		buf.append("		return new " + returnType + "(get" + getCodeFirstUpper() + "());" + LN);
		buf.append("	} catch (JSONException e) {" + LN);
		buf.append("		e.printStackTrace();" + LN);
		buf.append("	}" + LN);
		buf.append("	return null;" + LN);
		buf.append("  }" + LN);
		// 输出st对象方法
		buf.append("  public void st" + firstUpper + "(" + returnType + " " + getCode() + "){" + LN);
		buf.append("    if(" + getCode() + "==null)" + LN);
		buf.append("      set" + firstUpper + "(null);" + LN);
		buf.append("    else" + LN);
		buf.append("      set" + firstUpper + "(" + getCode() + ".toString());" + LN);
		buf.append("  }" + LN);
		return super.outSrcMethod() + buf.toString();
	}
	
	@Override
	public Class getJavaType() {
		return String.class;
	}

	@Override
	public int getSqlType() {
		return JSONARRAY;
	}

	@Override
	public String getTypeName() {
		return "JSONARRAY";
	}

	@Override
	public VFld crtVFld() {
		return new VFldNum(this);
	}

	@Override
	public short getWidth() {
		return _width;
	}

	public T setWidth(int width) {
		assertUnlocked();
		_width = (short) width;
		return (T) this;
	}

	@Override
	public Object tran(String value) {
		if (value == null)
			return null;
		JSONArray json = null;
		try {
			json = new JSONArray(value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getString(columnName);
	}
}
