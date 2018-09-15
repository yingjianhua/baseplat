package irille.pub.ext;

import irille.gl.gs.GsUom;
import irille.pub.Str;
import irille.pub.tb.Fld;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.FldV;
import irille.pub.tb.Tb;

/**
 * EXT编辑-TAB表格类
 */
public class ExtListEdit {

	public static String TEMPLATE = null;

	static {
		TEMPLATE = Ext.loadTemplate("ListEdit.txt");
	}

	public static String getContent(FldOutKey outfld) {
		Tb tb = (Tb)outfld.getTb();
		String R_COLUMNS = "";
		for (Fld fld : tb.getFlds()) {
			if (fld instanceof FldLines || fld instanceof FldV)
				continue;
			if (fld.getCode().equals("pkey")) {
				if (tb.isAutoIncrement())
					continue;
				if (tb.isAutoLocal() && fld.getJavaType().equals(String.class) == false)
					continue;
			}
			//从表中作为外键的，界面上不必要显示，在ACTION中自动负值
			if (fld.getCode().equals(outfld.getCode()))
				continue;
			//TODO 处理-各VFLD实现单元格编辑
			if (Str.isEmpty(R_COLUMNS) == false)
				R_COLUMNS += "\t\t\t";
			R_COLUMNS += fld.crtVFld().extListEdit(Ext.LNT4) + "," + Ext.LN;
			
		}
		if (Str.isEmpty(R_COLUMNS) == false)
			R_COLUMNS = R_COLUMNS.substring(0,R_COLUMNS.length()-3);
		String tabs = "";
		String content = TEMPLATE.replace("【包名】", Ext.getPag(tb));
		content = content.replace("【类名】", Ext.getClazz(tb));
		content = content.replace("【单元格定义】", R_COLUMNS);
		content = content.replace("【外键】", outfld.getCode());
		return content;
	}

	private static String getJsPath(FldOutKey outfld) {
		Tb tb = (Tb)outfld.getTb();
		return Ext.getPathApp() + "view/" + Ext.getPag(tb) + "/" + Ext.getClazz(tb) + "/ListEdit.js";
	}

	public static void crtJs(FldOutKey outfld, boolean del) {
		Ext.crtJs(getJsPath(outfld), getContent(outfld), del);
	}
	
	public static void main(String[] args) {
		GsUom.TB.getName();
	  System.err.println(getContent((FldOutKey)GsUom.T.UOM_TYPE.getFld()));
  }

}
