/**
 * 
 */
package irille.pub.print;

import irille.pub.print.Grfs.GrfItemsSection;
import irille.pub.print.Grfs.GrfSectionItem;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * @author whx
 * 
 */
public class GrfForm<T extends GrfForm> extends GrfMain<T> implements SheetVarsInf {
	public double _mainLineHeight = 0.62;
	public double _columnTitleHeight = 0.7;
	public double _columnContentHeight = 0.6;
	private String _name;

	private int _reportHeaderLines;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public int getReportHeaderLines() {
		return _reportHeaderLines;
	}

	public void setReportHeaderLines(int reportHeaderLines) {
		this._reportHeaderLines = reportHeaderLines;
	}

	/**
	 * 
	 * @param mainClazz Bean类
	 * @param name 表单名称
	 * @param reportHeaderLines 表头内容行数，不包括前3行
	 * @param vflds 字段
	 */
	public GrfForm(Class mainClazz, String name, int reportHeaderLines, VFlds vflds) {
		super(mainClazz, vflds);
		_name = name;
		_reportHeaderLines = reportHeaderLines;
	}

	/*
	 * (non-Javadoc)
	 * 初始化各个组件
	 */
	@Override
	public void initSheetPrint(SheetPrint sp) {
		super.initSheetPrint(sp);
		double[] header = new double[_reportHeaderLines + 3];
		header[0] = _mainLineHeight;
		header[1] = _mainLineHeight + 0.2; //标题行
		header[2] = _mainLineHeight;
		//循环行数的高度
		for (int i = 3; i < header.length; i++)
			header[i] = _mainLineHeight;
		sp.setPageHeader(new double[] { 1.05833 }, 4, -1, 4);
		//		sp.setPageFooter(new double[] { 1.05833 }, 6, -1, 6);
		sp.setPageFooter(new double[] { 0.8, 0.8 }, 2, -1, 2);
		sp.setReportHeader(header, 2, -1, 2, -1, 2, -1);
		sp.setReportFooter(new double[] { 0.8 }, 1, 2, 8, 1, 2, 8);
	}

	// ph:PageHeader, pf:PageFooter, rh:ReportHeader, rf:ReportFooter
	@Override
	public void initItemsSection(GrfItemsSection is, GrfSectionItem ph, GrfSectionItem pf, GrfSectionItem rh,
	    GrfSectionItem rf) {
		super.initItemsSection(is, ph, pf, rh, rf);
		//		ph.AddItem("StaticBox", "pageHeader", "表头", 1, 2).setTextAlignToTopCenter();
		//
		//		pf.AddItem("MemoBox", "page",
		//				"第[#SystemVar(PageNumber)#]页 共[#SystemVar(PageCount)#]页", 1, 2).setTextAlignToTopCenter();
		//
		//		rh.AddItem("StaticBox", "billName", _name, 2, 2, 1, 5).setTextAlignToTopCenter()
		//				.add(FONT_REPORT_HEAD);
	}

	public static class GrfFormLine<T extends GrfFormLine> extends GrfDetailGrid<T> {
		/**
		 * 
		 * @param parent
		 * @param vflds
		 * @param colWidths
		 * @param cvflds 若传组合字段值，必须与vflds顺序一一对应，不进行组合的字段传null
		 */
		public GrfFormLine(GrfForm parent, VFlds vflds, double colWidths[], VFlds... cvflds) {
			super(parent, vflds, parent._columnTitleHeight, parent._columnContentHeight, colWidths, cvflds);
		}
	}

	protected static String toCode(VFlds vflds) {
		String result = "";
		int i = 0;
		for (VFld fld : vflds.getVFlds()) {
			result += fld.getCode();
			if (i < vflds.size() - 1)
				result += ".";
			i++;
		}
		return result;
	}
	/**
	 * 生成无外键的外表关联字段字符串，后两个数组参数长度必须相同
	 * @param vflds 当前相关外表的字段
	 * @param tbs 关联的外表
	 * @param multiVfld 对应tbs顺序的关联外表的字段
	 * @return 无外键的外表关联字段字符串
	 */
	protected static String toLinkCode(VFlds vflds, Tb[] tbs, VFlds[] multiVflds) {
		String result = toCode(vflds);
		for (int i = 0; i < tbs.length; i++) {
			result += "_" + tbs[i].getCode() + "." + toCode(multiVflds[i]);
		}
		return result;
	}

	/**
	 * 
	 * @param mainvflds 无需进行组合的字段
	 * @param vflds 需进行组合的字段
	 * @return 组合后的字符串数组
	 */
	protected static String[] toCodes(VFlds mainvflds, VFlds... vflds) {
		String result[] = new String[mainvflds.size() + vflds.length];
		int i = 0;
		for (VFld fld : mainvflds.getVFlds()) {
			result[i] = fld.getCode();
			i++;
		}
		if (result.length > mainvflds.size())
			for (VFlds vfld : vflds) {
				result[i] = toCode(vfld);
				i++;
			}
		return result;
	}

	/**
	 * 合并新的字段
	 * @param codes 原有字段数组
	 * @param newCodes 新字段字符串
	 * @return 合并后的字段数组
	 */
	protected static String[] toCodes(String[] codes, String... newCodes) {
		String[] result = new String[codes.length + newCodes.length];
		System.arraycopy(codes, 0, result, 0, codes.length);
		System.arraycopy(newCodes, 0, result, codes.length, newCodes.length);
		return result;
	}
	
	protected static String getPurRem() {
		return "if (Sender.IsNull) Sender.DisplayText = \" \";else {var tmp = \"请确认以上货物品名、编号、规格、单位、数量，如有疑问请速致电\";  if (Sender.DisplayText == \"11004\") tmp += \"021-57734559\"; else tmp += \"0577-88605872\"; Sender.DisplayText = tmp;}";
	}

	protected static String spliteCode() {
		return "if (Sender.IsNull) Sender.DisplayText = \" \";else {   var arr = Sender.DisplayText.split(\"-\");    var tmp = \"\";    for (var i = 1; i  < arr.length; i++) {         tmp += arr[i];          if (i < arr.length - 1)             tmp += \"-\";     }   Sender.DisplayText = tmp; }";
	}

	protected static String getYear() {//2015-6-15 格式的日期中 取的年
		return "if (Sender.IsNull) Sender.DisplayText = \" \"; else { var arr = Sender.DisplayText.split(\"-\"); var tmp = \"\"; Sender.DisplayText = arr[0];}";
	}

	protected static String getMonth() {//2015-6-15 的事的日期中 取得月
		return "if (Sender.IsNull) Sender.DisplayText = \" \"; else { var arr = Sender.DisplayText.split(\"-\"); var tmp = \"\"; Sender.DisplayText = arr[1];}";
	}
}
