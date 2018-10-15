//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.view.VFld;
import irille.pub.view.VFldLanguage;


/**
 * 通过json支持多语言字符的字段<br>
 * @author yingjianhua
 */
public class FldLanguage<T extends FldLanguage> extends FldJSONObject<T> {
	
	public enum Language {
		ar("阿拉伯语"),
		en("English"),
		ja("日文"),
		de("德语"),
		fr("法语"),
		es("西班牙语"),
		ru("俄语"),
		pt("葡萄牙语"),
		zh_CN("简体中文"),
		zh_TW("繁体中文"),
		hu("匈牙利语"),
		ro("罗马尼亚语"),
		;
		private Language(String displayName) {
			this.displayName = displayName;
		}
		private String displayName;
		public String displayName() {
			return this.displayName;
		}
	}
	/**
	 * 构造方法
	 */
	public FldLanguage( String code, String name) {
		super(code, name);
	}

	/**
	 * 输出get与set的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		StringBuilder buf = new StringBuilder();
		String returnType = getReturnTypeShort();
		String firstUpper = getCodeFirstUpper();

		// 输出gt对象方法
		buf.append("  public " + returnType + " get" + firstUpper + "(FldLanguage.Language l) throws JSONException {" + LN);
		buf.append("    return gt" + firstUpper + "().has(l.name())?gt" + firstUpper + "().getString(l.name()):\"\";" + LN);
		buf.append("  }" + LN);
		// 输出st对象方法
		buf.append("  public void set" + firstUpper + "(" + returnType + " " + getCode() + ", FldLanguage.Language l) throws JSONException {" + LN);
		buf.append("    st" + firstUpper + "(gt" + firstUpper + "().put(l.name(), " + getCode() + "));" + LN);
		buf.append("  }" + LN);
		return super.outSrcMethod() + buf.toString();
	}
	
	@Override
	  public VFld crtVFld() {
		  return new VFldLanguage(this);
	  }
	
	@Override
	public T copyNew(String code, String name) {
		return (T) copy(new FldLanguage(code, name));
	}
}
