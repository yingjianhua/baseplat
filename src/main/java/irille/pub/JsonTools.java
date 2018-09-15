package irille.pub;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonTools {
	public static final Log LOG = new Log(JsonTools.class);

	public static JSONArray mapToArray(Map[] maps) {
		JSONArray array = new JSONArray();
		if (maps == null || maps.length == 0)
			return array;
		try {
			Object[] keys = maps[0].keySet().toArray();
			for (Map line : maps) {
				JSONObject json = new JSONObject();
				for (Object key : keys)
					json.put(key.toString(), line.get(key));
				array.put(json);
			}
		} catch (Exception e) {
			throw LOG.err("toArray", "结果集转换JSON出错！");
		}
		return array;
	}

}
