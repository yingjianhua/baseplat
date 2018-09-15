package irille.pub.svr;

import irille.pub.tb.OptCust;

import java.util.HashMap;

/**
 * 用户选项缓存
 * @author whx
 * 
 */
public class OptCustCtrl {
	private static OptCustCtrl INSTANCE = new OptCustCtrl();

	public static OptCustCtrl getInstance() {
		return INSTANCE;
	}

	private static HashMap<String, OptCust> _mapOpt = new HashMap();

	public OptCust get(String code) {
		return get(code, true);
	}

	public OptCust get(String code, boolean isGet) {
		OptCust opt = _mapOpt.get(code);
		if (opt == null) {
				opt = new OptCust(code, "", isGet);
		}
		_mapOpt.put(code, opt);
		return opt;
	}

	public void clear(String code) {
		_mapOpt.remove(code);
	}

}
