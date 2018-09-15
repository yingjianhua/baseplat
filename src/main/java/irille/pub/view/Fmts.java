package irille.pub.view;

import irille.pub.Log;
import irille.pub.NumTools;
import irille.pub.Str;
import irille.pub.tb.Infs.IFld;
import irille.pub.tb.Infs.IOptLine;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Fmts {
	private static final Log LOG = new Log(Fmts.class);
	public static final byte TYPE_NORMAL = 1;
	public static final byte TYPE_CH_LOWER = 2; // 中文小写：一二三
	public static final byte TYPE_CH_UPPER = 3; // 中文大写：壹元整
	public static final byte TYPE_COMMA = 4; // 逗号分隔
	
	private static final String YYYY_MM_DD_TIME = "yyyy-MM-dd hh:mm:ss";
	private static final String TIME = "hh:mm:ss";
	public static final FmtTime FMT_TIME_DATE = new FmtTime(YYYY_MM_DD_TIME);
	public static final FmtTime FMT_TIME = new FmtTime(TIME);
	public static final FmtStr FMT_Str = new FmtStr();
	
	public static class FmtStr extends FmtBase {

	}
	
	public static class FmtTime extends FmtBase {
		private SimpleDateFormat _fmt;


		private FmtTime(String fmt) {
			_fmt = new SimpleDateFormat(fmt);
		}

		@Override
		public String out(IFld fld, Object value) {
			return _fmt.format((Date) value);
		}
	}

	private static final String YYYY_MM_DD = "yyyy-MM-dd";
	private static final String YYYY_MM_DD_CH = "yyyy年MM月dd日";
	public static final FmtDate FMT_YYYY_MM_DD = new FmtDate(YYYY_MM_DD);
	public static final FmtDate FMT_YYYY_MM_DD_CH = new FmtDate(YYYY_MM_DD_CH);
	public static final FmtDate FMT_YYYYMMDD = new FmtDate("yyyyMMdd");
	public static final FmtDate FMT_YYYY_MM_DD_CH_LOWER = new FmtDate(YYYY_MM_DD_CH, TYPE_CH_LOWER);
	public static final FmtDate FMT_YYYY_MM_DD_CH_UPPER = new FmtDate(YYYY_MM_DD_CH, TYPE_CH_UPPER);
	public static final FmtDate FMT_YYYY_MM_CH = new FmtDate("yyyy年MM月");

	/**
	 * 日期格式
	 * 
	 * @author whx
	 * 
	 */
	public static class FmtDate extends FmtBase {

		private SimpleDateFormat _fmt;
		private byte _type;

		private FmtDate(String fmt, byte type) {
			_fmt = new SimpleDateFormat(fmt);
			_type = type;
		}

		private FmtDate(String fmt) {
			_fmt = new SimpleDateFormat(fmt);
			_type = TYPE_NORMAL;
		}

		@Override
		public String out(IFld fld, Object value) {
			if (value == null || value.toString().equals(""))
				return "";
			String s = _fmt.format((Date) value);
			if (_type == TYPE_NORMAL)
				return _fmt.format((Date) value);
			switch (_type) {
			case TYPE_CH_LOWER: // 二ＯＯ五年一月一日
				return NumTools.toChLower(s);
			case TYPE_CH_UPPER: // 贰零零五年壹月壹日
				return NumTools.toChUpper(s);
			}
			throw LOG.err();
		}
	}
	
	public static final FmtNum FMT_NUM = new FmtNum();

	/**
	 * 整数的格式类，包括：byte,short,long,int
	 * @author whx
	 * 
	 */
	public static class FmtNum extends FmtBase {

		private FmtNum() {
			setAlign(ALIGN_RIGHT);
		}
		private FmtNum(byte align) {
			setAlign(align);
		}
	}

	public static final FmtOpt FMT_OPT = new FmtOpt();
	public static class FmtOpt extends FmtBase {
		/**
		 * 显示选项的值及名称，如："1:正常"
		 * 
		 * @param beans
		 * @return
		 */
		@Override
		public String out(IFld fld, Object value) {
			if (value == null || Str.isEmpty(value.toString()))
				return "";
			if (!fld.isOpt())
				LOG.err("notOpt", "字段[{0}]不是选项，不能引用FmtOpts对象", fld.getName());
			IOptLine line = fld.getOpt().get(value.toString());
			return line.getKey()+":"+line.getName();
		}
	}
	public static final FmtDec FMT_DEC = new FmtDec(0);
	public static final FmtDec FMT_DEC2 = new FmtDec(2);
	public static final FmtDec FMT_DEC2_COMMA = new FmtDec(2, TYPE_COMMA);
	public static final FmtDec FMT_DEC0_COMMA = new FmtDec(0, TYPE_COMMA);
	public static final FmtDec FMT_DEC_CH_UPPER = new FmtDec(0, TYPE_CH_UPPER);
	public static final FmtDec FMT_DEC2_CH_UPPER = new FmtDec(2, TYPE_CH_UPPER);

	public static class FmtDec extends FmtBase {

		private byte _scale;// 小数位数，用于显示对齐
		private byte _type;

		public FmtDec(int scale) {
			this(scale, TYPE_NORMAL);
		}

		public FmtDec(int scale, byte type) {
			setAlign(ALIGN_RIGHT);
			_scale = (byte) scale;
			_type = type;
		}

		private static final String FORMAT_COMMA = "#,##0.0000000000000000000000";
		private static final String FORMAT = "#0.0000000000000000000000";

		@Override
		public String out(IFld fld, Object value) {
			switch (_type) {
			case TYPE_NORMAL: // 1233.22
				if (value == null)
					return "";
				return new DecimalFormat(FORMAT.substring(0, 3 + _scale)).format(value);
			case TYPE_COMMA:// 10,222.00
				if (value == null)
					return "";
				return new DecimalFormat(FORMAT_COMMA.substring(0, 6 + _scale)).format(value);
			case TYPE_CH_UPPER: // 金额大写，如：壹佰元整
				if (value == null)
					return "";
				return NumTools.jeToCh(Double.parseDouble(value.toString()));
			}
			throw LOG.err();
		}
	}
	
	public static final FmtOutKey FMT_OUTKEY = new FmtOutKey();
	
	public static class FmtOutKey extends FmtBase {
	}
}
