package irille.pub.ext;

import irille.gl.gs.GsGainLine;
import irille.pub.Str;
import irille.pub.tb.Fld;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.Tb;

/**
 * EXT复合表格-表格编辑类
 */
public class ExtZipListForm {

	public static String TEMPLATE = null;

	static {
		TEMPLATE = Ext.loadTemplate("ZipListForm.txt");
	}

	public static String getContent(Tb tb, Fld outfld) {
		//==========单元格定义
		String R_COLUMNS = "";
		for (Fld fld : outfld.getTb().getFlds()) {
			if (fld instanceof FldLines || fld instanceof FldV)
				continue;
			if (fld.getCode().equals("pkey") && tb.isAutoIncrement())
				continue;
			if (fld.getCode().equals(outfld.getCode()))
				continue;
			if (Str.isEmpty(R_COLUMNS) == false)
				R_COLUMNS += "\t\t\t";
			R_COLUMNS += fld.crtVFld().extListEdit(Ext.LNT4) + "," + Ext.LN;
		}
		if (Str.isEmpty(R_COLUMNS) == false)
			R_COLUMNS = R_COLUMNS.substring(0,R_COLUMNS.length()-3);
		String content = TEMPLATE.replace("【包名】", Ext.getPag(tb));
		content = content.replace("【类名】", Ext.getClazz(tb));
		content = content.replace("【单元格编辑器定义】", R_COLUMNS);
		content = content.replace("【从表类名】", Ext.getClazz((Tb)outfld.getTb()));
		content = content.replace("【从表包名】", Ext.getPag((Tb)outfld.getTb()));
		return content;
	}

	private static String getJsPath(Tb tb) {
		return Ext.getPathApp() + "view/" + Ext.getPag(tb) + "/" + Ext.getClazz(tb) + "/ListForm.js";
	}

	public static void crtJs(Tb mainTb, Fld outfld, boolean del) {
		Ext.crtJs(getJsPath(mainTb), getContent(mainTb, outfld), del);
	}
	
	public static void main(String[] args) {
		System.out.println(getContent(GsGainLine.TB, GsGainLine.T.PKEY.getFld()));
	}

}
