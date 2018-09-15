//Created on 2005-9-27
package irille.core.sys;

import irille.pub.Log;
import irille.pub.idu.IduUpd;
import irille.pub.svr.OptCustCtrl;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class SysCtypeDAO {
	public static final Log LOG = new Log(SysCtypeDAO.class);
	
	// 取用户选项
	public static List<SysCtypeCode> getOptLines(String code) {
		return SysCtypeCode.getLines(new SysCtype().load(code));
	}

	public static class Upd extends IduUpd<Upd, SysCtype> {
		private List<SysCtypeCode> _lines;

		public void before() {
			super.before();
			SysCtype dbBean = load(getB().getPkey());
			setB(dbBean);
			for (SysCtypeCode line : getLines())
				if (line.getCodeValue().length() != dbBean.getCtypeLen())
					throw LOG.err("codeLen", "参数配置的代码长度为{0}位", dbBean.getCtypeLen());
			updLine(dbBean, getLines(), SysCtypeCode.T.CTYPE_TYPE.getFld());
			//清空参数配置的缓存
			OptCustCtrl.getInstance().clear(dbBean.getPkey());
		}
 
		// 仅更新选项明细
		public void run() {
		}

		public List<SysCtypeCode> getLines() {
			return _lines;
		}

		public void setLines(List<SysCtypeCode> lines) {
			_lines = lines;
		}

	}

	//查询ctype所有数据及所有对应的字表数据,此方法页面初始化是数据加入到js缓存
	public JSONObject ctypeNode() throws Exception{
		JSONObject obj = new JSONObject();
		String ctypeSql = "select * from sys_ctype";
		Map[] map = SysCtype.executeQueryMap(ctypeSql);
		for(Map m : map){
			String PKEY = (String)m.get("pkey");
			StringBuffer codeSql = new StringBuffer("select * from sys_ctype_code where ctype_type = '")
					.append(PKEY).append("' order by code_value");

			Map[] code = SysCtypeCode.executeQueryMap(codeSql.toString());
			JSONObject o = new JSONObject();
			JSONArray list = new JSONArray();
			for(Map c : code){
				String CODE_NAME = (String)c.get("code_name");
				String CODE_VALUE = (String)c.get("code_value");
				o.put(CODE_VALUE, CODE_NAME);
				
				JSONObject codeObj = new JSONObject();
				codeObj.put("text", CODE_NAME);
				codeObj.put("value", CODE_VALUE);
				list.put(codeObj);
			}
			o.put("list", list);
			obj.put(PKEY, o);
		}
		return obj;
	}
}