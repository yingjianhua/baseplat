package irille.pub.ext;

import irille.core.sys.SysOrg;
import irille.pub.tb.Fld;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.Tb;

/**
 * EXT模型类
 */
public class ExtModel {

	public static String TEMPLATE = null;

	static {
		TEMPLATE = Ext.loadTemplate("Model.txt");
	}

	//{name : 'bean.pkey' , mapping : 'pkey' , type : 'int', useNull : true},
	//{name : 'bean.code' , mapping : 'code' , type : 'string'},
	//{name : 'bean.optSys' , mapping : 'optSys' , type : 'string'},
	//{name : 'bean.optCust' , mapping : 'optCust' , type : 'string', optCust : true},
	//{name : 'bean.age' , mapping : 'age' , type : 'int', useNull : true},
	//{name : 'bean.amt' , mapping : 'amt' , type : 'float', useNull : true},
	//{name : 'bean.date1' , mapping : 'date1' , type : 'date'},
	//{name : 'bean.time1' , mapping : 'time1' , type : 'date' ,dateFormat : 'Y-m-d H:i:s'},
	//{name : 'bean.key1' , mapping : 'key1' , type : 'string', outkey : true}
	//{name : 'bean.amt2' , mapping : 'amt2' , type : 'float', useNull : true,
	//	convert:mvc.Tools.pctConvert(0.0001),isPct:0.0001
	//},
	//{name : 'bean.amt3' , mapping : 'amt3' , type : 'float', useNull : true,
	//	convert:mvc.Tools.pctConvert(0.00000001),isPct:0.00000001
	//},
	//{name : 'bean.amt4' , mapping : 'amt4' , type : 'float', useNull : true},
	//{name : 'bean.rate' , mapping : 'rate' , type : 'float', useNull : true,
	//	convert:mvc.Tools.pctConvert(100),isPct:100
	//},
	//{name : 'bean.rate2' , mapping : 'rate2' , type : 'float', useNull : true,
	//	convert:mvc.Tools.pctConvert(1000),isPct:1000
	//},

	public static String getContent(Tb tb) {
		String fields = "";
		for (Fld fld : tb.getFlds()) {
			if (fld instanceof FldLines || fld instanceof FldV)
				continue;
			fields += fld.crtVFld().extModel() + "," + Ext.LNT2;
		}
		fields = fields.substring(0, fields.length() - 5);
		String content = TEMPLATE.replace("【包名】", Ext.getPag(tb));
		content = content.replace("【类名】", Ext.getClazz(tb));
		content = content.replace("【字段集】", fields);
		return content;
	}

	private static String getJsPath(Tb tb) {
		return Ext.getPathApp() + "model/" + Ext.getPag(tb) + "/" + Ext.getClazz(tb) + ".js";
	}

	public static void crtJs(Tb tb, boolean del) {
		Ext.crtJs(getJsPath(tb), getContent(tb), del);
	}

	public static void main(String[] args) {
		System.err.println(getContent(SysOrg.TB));
	}

}
