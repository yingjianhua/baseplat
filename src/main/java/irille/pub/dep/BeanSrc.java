/**
 * 
 */
package irille.pub.dep;

import irille.pub.ClassTools;
import irille.pub.FileText;
import irille.pub.FileTools;
import irille.pub.IPubVars;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbOracle;
import irille.pub.svr.DbPool;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.FldVCmbFlds;
import irille.pub.tb.FldVInf;
import irille.pub.tb.Tb;
import irille.pub.tb.TbBase;
import irille.pub.tb.Tb.IIndexFld;
import irille.pub.tb.Tb.Index;

import java.util.Vector;

/**
 * 产生Bean自动代码的类
 * 
 * @author whx
 * 
 */
public class BeanSrc implements IPubVars {
	private static final Log LOG = new Log(BeanSrc.class);
	private StringBuilder _autoVarDef = new StringBuilder(); // 变量定义
	private StringBuilder _autoGetSet = new StringBuilder(); // get,set方法
	private StringBuilder _autoFlds = new StringBuilder(); // 自动产生的Fld
	private StringBuilder _autoInit = new StringBuilder(); // init方法
	private StringBuilder _autoIdx = new StringBuilder(); // idx方法
	private Bean _b;

	public BeanSrc(Bean bean) {
		_b = bean;
	}

	public void outSrc() {
		Class clazz = _b.getClass();
		TbBase tb = (TbBase) ClassTools.getStaticProerty(clazz, "TB");
		crSrc(); // 产生自动的代码
		updBeanJavaFile();
		try {
			if (tb.isDatabaseTb()) { // 不是真正的表,可能为包中的字段定义或一些组合字段
				DbPool.getInstance();
				Env.INST.getDB().db(clazz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final void crSrc() {

		Class clazz = _b.getClass();
		TbBase tb = (TbBase) ClassTools.getStaticProerty(clazz, "TB");
		if (tb.isDatabaseTb()) {
			TbBase tbPa = ((Tb) tb).getBaseTb();
			if (tbPa != null) {
				_autoVarDef.append("  //继承基类:" + tbPa.getClazz() + LN);
				for (Fld fld : tbPa.getFlds()) { // XXX
					_autoVarDef.append("  public static final Fld FLD_"
							+ Str.tranFieldToLineUpper(fld.getCode()) + "= TB.get(\""
							+ fld.getCode() + "\");" + LN);
				}
			}
		}
		Fld[] flds = tb.getFlds();
		_autoVarDef.append("  //实例变量定义-----------------------------------------"
				+ LN);
		_autoGetSet.append("  //方法----------------------------------------------"
				+ LN);

		// 产生唯一索引的方法
		if (tb.isDatabaseTb()) {
			Index idx;
			for (Object obj : ((Tb) _b.gtTB()).getIndexes()) {
				idx = (Index) obj;
				if (idx.isUnique()) {
					_autoGetSet.append(outSrcLoadUnique(idx, "loadUnique"));
					_autoGetSet.append(outSrcLoadUnique(idx, "chkUnique"));
				}
			}
		}
		_autoInit.append("	@Override" + LN + "  public "
				+ Str.getClazzLastCode(clazz) + " init(){" + LN + "		super.init();"
				+ LN);
		Tb baseTb = _b.tb(_b.getClass().getSuperclass());
		for (Fld fld : flds) {
			if (baseTb.chk(fld.getCode())) // 基类中已定义
				_autoVarDef.append("  //" + fld.outSrcVarDefine().substring(2));
			else {
				_autoVarDef.append(fld.outSrcVarDefine()); // 变量定义
				_autoGetSet.append(fld.outSrcMethod()); // 方法
			}
			if (fld.isDatabaseField())
				_autoInit.append(fld.outSrcInit());
			// 如有FldVFlds的组合嵌入字段，则复制索引
			if (fld instanceof FldVCmbFlds && tb.isDatabaseTb()) {
				FldVCmbFlds vfld = (FldVCmbFlds) fld;
				for (Index vidx : (Vector<Index>) vfld.getCmbTb().getIndexes()) {
					_autoIdx.append("		public static final Index IDX_"
							+ vidx.getCodeUpper() + "= TB.addIndex(\"" + vidx.getCode()
							+ "\"," + vidx.isUnique() + "," + vidx.getFldsCodeUpper() + ");"
							+ LN);
				}
			}

			if (!fld.isAutoCreate() || fld instanceof FldVInf)
				continue;
			_autoFlds.append("		" + Str.tranFieldToLineUpper(fld.getCode())
					+ "(TB.get(\"" + fld.getCode() + "\")),	//" + fld.getName() + LN);

		}
		_autoInit.append("    return this;" + LN + "  }" + LN);
		// buf.append(tb.outMsgAct("\t//"));
	}

	public String outSrcLoadUnique(Index idx, String method) {
		StringBuilder buf = new StringBuilder();
		String bean = Str.getClazzLastCode(_b.getClass());
		buf.append("  public static " + bean + " " + method
				+ Str.tranFirstUpper(idx.getCode()) + "(boolean lockFlag");
		for (IIndexFld fld : idx.getFields())
			buf.append("," + Str.getClazzLastCode(fld.getJavaType()) + " "
					+ fld.getCode());
		buf.append(") {" + LN + "    return (" + bean + ")" + method + "(T.IDX_"
				+ Str.tranFieldToLineUpper(idx.getCode()) + ",lockFlag");
		for (IIndexFld fld : idx.getFields())
			buf.append("," + fld.getCode());
		buf.append(");" + LN + "  }" + LN);
		return buf.toString();
	}

	public static final String AUTO_FIELDS = "内嵌字段定义";
	public static final String AUTO_IDX = "自动建立的索引定义";
	public static final String AUTO_SOURCE_CODE = "源代码";

	public void updBeanJavaFile() {
		// 更新文件
		FileText file = new FileText(_b.getClass());
		if (file.findCheckBegin(AUTO_FIELDS) < 0) { // 原来的格式，则进行转换
			String tab=TAB+"//";
			int b = file.find(">>>以下是自动产生的字段定义对象----");
			int e = file.find("<<<<以上是自动产生的字段定义对象");
			if (Str.trim(file.getLines().get(e + 1).toString()).equals(";"))
				e++;
			file.replace(
					b,
					e,
					TAB+tab+file.getBeginMsg(AUTO_FIELDS) + LN +TAB+ tab+file.getEndMsg(AUTO_FIELDS) + LN
							+ TAB+TAB+";" + LN +TAB+ tab+file.getBeginMsg(AUTO_IDX) + LN
							+TAB+ tab+file.getEndMsg(AUTO_IDX) + LN);
			b = file.find(">>>以下是自动产生的源代码行---");
			e = file.findJavaLastLine()-1;
			file.replace(
					b,
					e,
					tab+file.getBeginMsg(AUTO_SOURCE_CODE) + LN
							+ tab+file.getEndMsg(AUTO_SOURCE_CODE) + LN);
			file.save();
			file= new FileText(_b.getClass());
		}
		file.replace(AUTO_FIELDS, _autoFlds.toString()); // 自动产生的字段
		file.replace(AUTO_IDX, _autoIdx.toString());
		file.replace(AUTO_SOURCE_CODE, _autoVarDef.toString() + LN + _autoInit + LN
				+ _autoGetSet + LN);
		file.save();
	}

}
