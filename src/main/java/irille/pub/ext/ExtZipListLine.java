package irille.pub.ext;

import irille.pub.Str;
import irille.pub.tb.Fld;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.Tb;

/**
 * EXT复合表格-从表类
 */
public class ExtZipListLine {

	public static String TEMPLATE = null;

	static {
		TEMPLATE = Ext.loadTemplate("ZipListLine.txt");
	}

	public static String getContent(Tb maintb, Fld outfld) {
		Tb linetb = (Tb) outfld.getTb();
		String R_COLUMNS = "";
		for (Fld fld : linetb.getFlds()) {
			if (fld instanceof FldLines || fld instanceof FldV)
				continue;
			if (fld.getCode().equals("pkey") && linetb.isAutoIncrement())
				continue;
			if (fld.getCode().equals(outfld.getCode()))
				continue;
			if (Str.isEmpty(R_COLUMNS) == false)
				R_COLUMNS += "\t\t\t";
			R_COLUMNS += fld.crtVFld().extList() + "," + Ext.LN;
		}
		if (Str.isEmpty(R_COLUMNS) == false)
			R_COLUMNS = R_COLUMNS.substring(0, R_COLUMNS.length() - 3);
		String tabs = "";
		String content = TEMPLATE.replace("【包名】", Ext.getPag(maintb));
		content = content.replace("【类名】", Ext.getClazz(maintb));
		content = content.replace("【单元格定义】", R_COLUMNS);
		content = content.replace("【从表类名】", Ext.getClazz(linetb));
		content = content.replace("【从表包名】", Ext.getPag(linetb));
		return content;
	}

	private static String getJsPath(Tb tb, Fld outfld) {
		return Ext.getPathApp() + "view/" + Ext.getPag(tb) + "/" + Ext.getClazz(tb) + "/ListLine"
		    + Str.tranFirstUpper(outfld.getTb().getCode()) + ".js";
	}

	public static void crtJs(Tb mainTb, Fld outfld, boolean del) {
		Ext.crtJs(getJsPath(mainTb, outfld), getContent(mainTb, outfld), del);
	}

	public static void main(String[] args) {
//		System.out.println(getContent(SysB2.FLD_KEY));
	}

}
