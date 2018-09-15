package irille.pub.html;

import irille.pub.ClassTools;
import irille.pub.Str;
import irille.pub.ext.Ext;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.FldEnumByte;
import irille.pub.tb.FldOptCust;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;

import java.util.Vector;

/**
 * 属性类的基类
 * 
 * @author whx
 * 
 */
public class EMOpt<T extends EMOpt> extends ExtFile<T> {
	private Class _clazz;

	/**
	 * @param fileName
	 */
	public EMOpt(Class optClazz) {
		_clazz = optClazz;
	}

	public Class getClazz() {
		return _clazz;
	}

	public static void main(String[] args) {
		 new EMOptCust("sysPost").backupFiles().init().crtFile().cmpFileIgnoreBlank();;
	}

	public static void crtOpts(Tb tb) {
		for (Fld fld : tb.getFlds())
			if (fld instanceof FldEnumByte) { // 选项
				FldEnumByte efld = (FldEnumByte) fld;
				EMOpt opt = new EMOpt(efld.getEnum().getClass());
				if (Str.getClazzPackage(opt._clazz).equals(
						Str.getClazzPackage(tb.getClazz()))) {
					opt.init();
					opt.crtFile();
				} else
					System.out.println("选项：[" + opt._clazz.getName() + "]与类["
							+ tb.getClazz().getName() + "]不属于同一个包，不再新建选项的JS文件。");
			} else {
				if (fld instanceof FldOptCust) { // 用户选项
					EMOptCust opt = new EMOptCust(((FldOptCust) fld).getOptCode());
					opt.init();
					opt.crtFile();
				}
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getFileName()
	 */
	@Override
	public String getFileName() {
		return Ext.getPathApp() + "combo/" + Ext.getPag(_clazz) + "/"
				+ Ext.getClassVarName(_clazz) + ".js";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.html.ExtFile#getDefineName()
	 */
	@Override
	public String getDefineName() {
		return "mvc.combo." + Ext.getPag(_clazz) + "."
				+ Ext.getClassVarName(_clazz);
	}

	@Override
	public void initAttrs() {
		//@formatter:off
		add(EXTEND,"Ext.data.Store");
		AddDime("fields","value","text");
		initOptLines(AddDime("data"));
	}
	public void initOptLines(ExtDime dime) {
		Vector<EnumLine> lines=((IEnumOpt)ClassTools.getStaticProerty(_clazz, "DEFAULT")).getLine().getLines();
		for(EnumLine l:lines) {
			dime.AddList(null).add(VALUE,l.getKey()).add(TEXT, l.getName()).setTabs(false);
		}
	}
}
