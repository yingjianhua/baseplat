package com.irille.core.repository.orm.columns;

import com.irille.core.repository.orm.Column;
import com.irille.core.repository.orm.ColumnTypes;
import com.irille.core.repository.orm.IColumnField;

public class I18NColumn extends Column{

//	public enum Language {
//		en("English"),
//		ja("日文"),
//		de("德语"),
//		fr("法语"),
//		es("西班牙语"),
//		ru("俄语"),
//		pt("葡萄牙语"),
//		zh_CN("简体中文"),
//		zh_TW("繁体中文"),
//		hu("匈牙利语"),
//		ro("罗马尼亚语"),
//		;
//		private Language(String displayName) {
//			this.displayName = displayName;
//		}
//		private String displayName;
//		public String displayName() {
//			return this.displayName;
//		}
//	}
	
	public I18NColumn(IColumnField field, String showName, String columnName, String fieldName, ColumnTypes type, boolean unique, boolean primary, boolean autoIncrement, boolean nullable, Object defaultValue,
			String columnDefinition, int length, int precision, int scale, String comment) {
		super(field, showName, columnName, fieldName, ColumnTypes.JSONOBJECT, unique, primary, autoIncrement, nullable, defaultValue, columnDefinition, length, precision, scale, comment);
	}
	
    public String gtterMethod() {
    	return "gt"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
    }
    public String stterMethod() {
    	return "st"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
    }
    
	public String getGetterSetterComment() {
		String getterSetterComment2 = new StringBuilder()
				.append(TAB).append("public String ").append(getterMethodString()).append("(Locale locale) throws JSONException {").append(LN)
				.append(TAB+TAB).append("return ").append(gtterMethod()).append("().has(locale.getLanguage())?").append(gtterMethod()).append("().getString(locale.getLanguage()):\"\";").append(LN)
				.append(TAB).append("}").append(LN)
				.append(TAB).append("public void ").append(setterMethodString()).append("(String ").append(fieldName()).append(", Locale locale) throws JSONException {").append(LN)
				.append(TAB+TAB).append(stterMethod()).append("(").append(gtterMethod()).append("().put(locale.getLanguage(), ").append(fieldName()).append("));").append(LN)
				.append(TAB).append("}").append(LN)
				.toString();
		String gtterStterComment = new StringBuilder()
	    		.append(TAB).append("public ").append("JSONObject").append(" ").append(gtterMethod()).append("() throws JSONException {").append(LN)
	    		.append(TAB+TAB).append("return (").append(getterMethodString()).append("()==null?new JSONObject():new JSONObject(").append(getterMethodString()).append("()));").append(LN)
	    		.append(TAB).append("}").append(LN)
	    		.append(TAB).append("public void ").append(stterMethod()).append("(").append("JSONObject ").append(fieldName()).append(") {").append(LN)
	    		.append(TAB+TAB).append("this.").append(setterMethodString()).append("(").append(fieldName()).append("==null?null:").append(fieldName()).append(".toString());").append(LN)
	    		.append(TAB).append("}").append(LN)
	    		.toString();
		return super.getGetterSetterComment()+getterSetterComment2+gtterStterComment;
	}
//		  public JSONObject gtName() throws JSONException {
//		    return getName()==null?new JSONObject():new JSONObject(getName());
//		  }
//		  public void stName(JSONObject name){
//		    setName(name==null?null:name.toString());
//		  }
	
}
