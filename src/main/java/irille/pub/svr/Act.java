package irille.pub.svr;

import irille.pub.Log;
import irille.pub.Str;
import irille.pub.tb.EnumLine;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;

public class Act<T extends Act> {
	private static final Log LOG = new Log(Act.class);

	public interface IEnumAct extends IEnumOpt {
		public String getIcon();
	}

	public enum OAct implements IEnumAct {//@formatter:off
		INS(1,"新增","ins-icon"),//
		EDIT(2, "编辑","edit-icon"),  //
		UPD(3, "修改","upd-icon"),  //
		LIST(4,"查询","list-icon"),//
		DEL(5,"删除","del-icon"),  //
		DO_APPR(11,"审核","doAppr-icon"),//
		UN_APPR(12,"弃审","unAppr-icon"),  //
		DO_NOTE(14,"便签","doNote-icon"),  //
		DO_TALLY(15,"记账","doTally-icon"),//
		UN_TALLY(16,"记账取消","unTally-icon"),  //
		DO_ENABLED(21,"启用","doEnabled-icon"), //
		UN_ENABLED(22,"停用","unEnabled-icon"), //
		OPT(31,"操作","opt-icon"),//
		UNDO(32,"撤销","undo-icon"),//
		REPORT(70,"报表","report-icon"),//
		PRINT(71,"打印","print-icon"), //
		UPD_ROW(81,"修改","upd-icon-table"),//行修改
		DEL_ROW(82,"删除","del-icon-table"), //行删除
		SEARCH(96,"搜索","win-ok-icon"),
		SEARCH_ADV(101,"搜索","win-ok-icon"),
		RESET(97,"重置","win-refresh-icon"),//
		CLOSE(98,"关闭","win-close-icon"),//
		SAVE(99,"保存","win-save-icon");
		public static final String NAME="操作功能";
		public static final OAct DEFAULT = INS; // 定义缺省值
		private EnumLine _line;
		private String _icon;
		private OAct(int key, String name,String icon) {_line=new EnumLine(this,key,name);_icon=icon;	}
		public EnumLine getLine(){return _line;	}
		public String getIcon() { return _icon;}
	}		//@formatter:on

	//
	//
	// public static final byte TYPE_INS = 1;
	// public static final byte TYPE_UPD = 2;
	// public static final byte TYPE_LIST = 3;
	// public static final byte TYPE_DEL = 4;
	// public static final byte TYPE_OPT = 5;
	// public static final byte TYPE_UNDO = 6;
	// public static final byte TYPE_PRINT = 7;
	// public static final byte TYPE_ENABLED = 8;
	// public static final byte TYPE_REPORT = 9;
	// public static final Opt TYPE = new Opt(LOG, "type", "功能类型").add(TYPE_INS,
	// "新增")
	// .add(TYPE_UPD, "修改").add(TYPE_LIST, "查询").add(TYPE_DEL, "删除").add(TYPE_OPT,
	// "操作")
	// .add(TYPE_UNDO, "撤销").add(TYPE_PRINT, "打印").add(TYPE_ENABLED,
	// "启停").add(TYPE_REPORT, "报表");
	//
	// public static final String LIST = "list";
	// public static final String INS = "ins";
	// public static final String UPD = "upd";

	private Tb _tb;
	private IEnumAct _act;
	private String _icon;
	private boolean _needSel; //是否需要选中
	private String _name;

	public Act(Tb tb, IEnumAct act, String icon) {
		_tb = tb;
		if (icon == null)
			_icon = act.getIcon();
		else
			_icon = icon;
		_act = act;
		_name = _act.getLine().getName();
		setNeedSel(true);
		if (act == OAct.INS)
			setNeedSel(false);
	}

	public Act(Tb tb, IEnumAct act) {
		this(tb, act, null);
	}

	public String getCode() {
		return _act.getLine().getCode();
	}

	public String getCodeFirstUpper() {
		return Str.tranFirstUpper(getCode());
	}

	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}

	public final Tb getTb() {
		return _tb;
	}

	public final String getIcon() {
		return _icon;
	}

	/**
	 * @return the act
	 */
	public IEnumOpt getAct() {
		return _act;
	}

	public String getFunName() {
		return "on" + Str.tranFirstUpper(getCode());
	}

	public static String funName(IEnumOpt act) {
		return "on" + Str.tranFirstUpper(act.getLine().getCode());
	}

	public void setIcon(String icon) {
		_icon = icon;
	}

	public boolean isNeedSel() {
		return _needSel;
	}

	public void setNeedSel(boolean needSel) {
		_needSel = needSel;
	}

}
