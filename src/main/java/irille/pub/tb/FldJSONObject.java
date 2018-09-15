//Created on 2005-9-27
package irille.pub.tb;

import java.sql.ResultSet;
import java.sql.SQLException;

import irille.pub.view.Fmts;
import irille.pub.view.VFld;
import irille.pub.view.VFldNum;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 存放jsonObject的格式字符串的字段
 * @author yingjianhua
 */
public class FldJSONObject<T extends FldJSONObject> extends Fld<T> {
	/**
	 * 不能与java.sql.Types中的值重复 
	 */
	public static final int JSONOBJECT = 2100;
	private short _width = 8;

	/**
	 * 构造方法
	 */
	public FldJSONObject( String code, String name) {
		super(code, name);
		setFmt(Fmts.FMT_NUM);
		setDefaultValue(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		return (T) copy(new FldJSONObject(code, name));
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
		String returnType = "JSONObject";
		String firstUpper = getCodeFirstUpper();

		// 输出gt对象方法
		buf.append("  public " + returnType + " gt" + firstUpper + "() throws JSONException {" + LN);
		buf.append("    return get" + getCodeFirstUpper() + "()==null?new JSONObject():new JSONObject(get" + getCodeFirstUpper() + "());" + LN);
		buf.append("  }" + LN);
		// 输出st对象方法
		buf.append("  public void st" + firstUpper + "(" + returnType + " " + getCode() + "){" + LN);
		buf.append("    set" + getCodeFirstUpper() + "(" + getCode() + "==null?null:" + getCode() + ".toString());" + LN);
		buf.append("  }" + LN);
		return super.outSrcMethod() + buf.toString();
	}
	 
	@Override
	public Class getJavaType() {
		return String.class;
	}

	@Override
	public int getSqlType() {
		return JSONOBJECT;
	}

	@Override
	public String getTypeName() {
		return "JSONOBJECT";
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
		JSONObject json = null;
		try {
			json = new JSONObject(value);
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
