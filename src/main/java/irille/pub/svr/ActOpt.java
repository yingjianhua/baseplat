package irille.pub.svr;

import irille.pub.tb.Tb;

public class ActOpt<T extends ActOpt> extends Act<T> {
	private String _code;
	private String _name;

	public ActOpt(Tb tb, String code, String name,String icon) {
		super(tb,OAct.OPT,icon);
		_code = code;
		_name = name;
		setNeedSel(false);
	}
	
	public ActOpt(Tb tb, String code, String name,String icon,boolean sel) {
		super(tb,OAct.OPT,icon);
		_code = code;
		_name = name;
		setNeedSel(sel);
	}

	@Override
	public String getCode() {
		return _code;
	}

	@Override
	public String getName() {
		return _name;
	}
}
