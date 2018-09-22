/**
 * 
 */
package com.irille.core.repository.orm;

import irille.pub.ClassTools;
import irille.pub.FileText;
import irille.pub.IPubVars;
import irille.pub.Str;

/**
 * 产生Bean自动代码的类
 * 
 */
public class EntitySrc implements IPubVars {
	private StringBuilder _autoVarDef = new StringBuilder(); // 变量定义
	private StringBuilder _autoGetSet = new StringBuilder(); // get,set方法
	private StringBuilder _autoFlds = new StringBuilder(); // 自动产生的Fld
	private StringBuilder _autoInit = new StringBuilder(); // init方法
	private StringBuilder _autoIdx = new StringBuilder(); // idx方法
	private Entity entity;
	
	public EntitySrc(Entity entity) {
		this.entity = entity;
	}

	public void outSrc() {
		crSrc(); // 产生自动的代码
		updBeanJavaFile();
	}

	public final void crSrc() {
		Class<?> clazz = entity.getClass();
		Table<?> tb = (Table<?>) ClassTools.getStaticProerty(clazz, "table");
		Column[] columns = tb.columns();
		_autoVarDef
			.append(LN)
			.append("	// 实例变量定义-----------------------------------------").append(LN);
		_autoGetSet
			.append("	// 方法------------------------------------------------").append(LN);
		_autoInit
			.append("	@Override").append(LN)
			.append("	public " + Str.getClazzLastCode(clazz) + " init() {").append(LN)
			.append("		super.init();").append(LN);
		for(Column column:columns) {
			_autoVarDef.append(column.getFieldComment()); // 变量定义
			_autoGetSet.append(column.getGetterSetterComment()); // 方法
			_autoInit.append(column.getInitComment());
		}
		_autoInit.append("		return this;" + LN + "	}").append(LN);
	}

	public static final String AUTO_FIELDS = "内嵌字段定义";
	public static final String AUTO_IDX = "自动建立的索引定义";
	public static final String AUTO_SOURCE_CODE = "源代码";

	public void updBeanJavaFile() {
		// 更新文件
		FileText file = new FileText(entity.getClass());
//		if (file.findCheckBegin(AUTO_FIELDS) < 0) { // 原来的格式，则进行转换
//			String tab=TAB+"//";
//			int b = file.find(">>>以下是自动产生的字段定义对象----");
//			int e = file.find("<<<<以上是自动产生的字段定义对象");
//			if (Str.trim(file.getLines().get(e + 1).toString()).equals(";"))
//				e++;
//			file.replace(
//					b,
//					e,
//					TAB+tab+file.getBeginMsg(AUTO_FIELDS) + LN +TAB+ tab+file.getEndMsg(AUTO_FIELDS) + LN
//							+ TAB+TAB+";" + LN +TAB+ tab+file.getBeginMsg(AUTO_IDX) + LN
//							+TAB+ tab+file.getEndMsg(AUTO_IDX) + LN);
//			b = file.find(">>>以下是自动产生的源代码行---");
//			e = file.findJavaLastLine()-1;
//			file.replace(
//					b,
//					e,
//					tab+file.getBeginMsg(AUTO_SOURCE_CODE) + LN
//							+ tab+file.getEndMsg(AUTO_SOURCE_CODE) + LN);
//			file.save();
//			file= new FileText(entity.getClass());
//		}
//		file.replace(AUTO_FIELDS, _autoFlds.toString()); // 自动产生的字段
		file.replace(AUTO_SOURCE_CODE, _autoVarDef.toString() + LN + _autoInit + LN + _autoGetSet + LN);
		file.save();
	}

}
