//Created on 2005-11-15
package irille.pub;

import irille.pub.tb.IEnumOpt;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Title: 基于java 1.5的字符处理增强类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class Str implements IPubVars {
	private static final Log LOG = new Log(Str.class);
	public static final String ENCODING_GB = "GB2312";
	public static final String ENCODING_ISO = "ISO-8859-1";
	public static final String ENCODING_UTF8 = "UTF-8";
	public static final String ENCODING_GBK = "GBK";
	public final static String BLANK_CHARS = " \t\n\r";
	public final static SimpleDateFormat SDF_MONTH = new SimpleDateFormat("yyyy-MM");
	public final static SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat SDF_TIME = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

	public static final String[] TMP_STRS = {};

	public static final String trim(String str) {
		return trim(str,BLANK_CHARS);
	}
	
	public static final String trim(String str, String blankChars ) {
		StringBuilder buf=new StringBuilder();
		String s;
		int begin;
		int i;
		for(i=0;i<str.length();i++) {
			s=str.substring(i, i+1);
			if(blankChars.indexOf(s)<0)
				break;
		}
		begin=i;
		for(i=str.length()-1;i>=0;i--) {
			s=str.substring(i, i+1);
			if(blankChars.indexOf(s)<0)
				break;
		}		
		if(i>=begin)
			return str.substring(begin, i+1);
		return "";
	}
	
	public static final String value(IEnumOpt opt) {
		return opt.getLine().getKey() + "";
	}

	/**
	 * 取字符串的显示长度，英文字符占一格，中文占2格
	 * 
	 * @param str
	 * @return
	 */
	public static final int len(String str) {
		return str.getBytes().length;
	}

	public static final boolean isEmpty(String str) {
		if (str == null || str.trim().isEmpty())
			return true;
		return false;
	}

	public static boolean isNum(String str) {
		if (isEmpty(str))
			return false;
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	public static boolean isContain(String sub, String[] ps) {
		if (ps == null || ps.length == 0)
			return false;
		for (String line : ps)
			if (line.equals(sub))
				return true;
		return false;
	}

	/**
	 * 判断字符串是上下级关系，分隔符为“.”
	 * @param str 上级串（主串）
	 * @param subString 下级串（子串）
	 * @return 如：“xj”与“j.aaa”为上下级关系
	 */
	public static boolean isUpDown(String str, String subString) {
		if (str == null)
			return true;
		if (subString == null)
			return false;
		if (str.equals(subString))
			return true;
		if (!str.substring(str.length() - 1).equals("."))
			str += ".";
		int l = str.length();
		if (l > subString.length()) //主串比子串要长
			return false;
		//	System.out.println(str+","+subString+"!"+subString.substring(0,l));
		return str.equals(subString.substring(0, l));
	}

	public static String getFileExtend(String fn) {
		if (isEmpty(fn))
			return null;
		int idx = fn.lastIndexOf('.') + 1;
		if (idx == 0 || idx >= fn.length())
			return null;
		return fn.substring(idx);
	}

	public static boolean isIPAddr(String addr) {
		if (isEmpty(addr))
			return false;
		String[] ips = StringUtils.split(addr, '.');
		if (ips.length != 4)
			return false;
		try {
			int ipa = Integer.parseInt(ips[0]);
			int ipb = Integer.parseInt(ips[1]);
			int ipc = Integer.parseInt(ips[2]);
			int ipd = Integer.parseInt(ips[3]);
			return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0 && ipc <= 255 && ipd >= 0 && ipd <= 255;
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean isEmail(String email) {
		if (email == null)
			return false;
		email = email.trim();
		if (email.indexOf(' ') != -1)
			return false;
		int idx = email.indexOf('@');
		if (idx == -1 || idx == 0 || (idx + 1) == email.length())
			return false;
		if (email.indexOf('@', idx + 1) != -1)
			return false;
		if (email.indexOf('.') == -1)
			return false;
		return true;
	}

	public static boolean isMobile(String mobile) {
		String regEx = "(^1(3|5|8)\\d{9}$)|(^0\\d{10,11}$)";
		Pattern p1 = Pattern.compile(regEx);
		Matcher m1 = p1.matcher(mobile);
		return m1.matches();
	}

	public static boolean isCard(String idcard) {
		if (idcard == null || StringUtils.isEmpty(idcard.trim()))
			return false;
		idcard = idcard.trim();
		if (idcard.length() != 15 && idcard.length() != 18)
			return false;
		return true;
	}

	/**
	 * 返回格式化的文本串，如: msg="账号[{0}]，户名[{1}]", paras={"111","张三"}, 则结果为账号[111]，
	 * 户名[张三]
	 * 
	 * @param msg
	 *          格式账，参数以"{位置}"表示
	 * @param paras
	 *          参数
	 * @return 结果
	 */
	public static String format(String msg, Object... paras) {
		return new MessageFormat(msg).format(paras);
	}

	/**
	 * 将参数转换成复合字段的字符串，如将”11“，”22“，”33“转换成“11:22:33"
	 * 
	 * @param paras
	 * @return
	 */
	public static String createComplex(Object... paras) {
		StringBuilder bud = new StringBuilder();
		String split = "";
		for (Object obj : paras) {
			bud.append(split + obj);
			split = ":";
		}
		return bud.toString();
	}

	public static String getComplexPara(String complex, int id) {
		return split(complex, ":", id);
	}

	/**
	 * 在对象之间加入换行符
	 * 
	 * @param lines
	 *          行
	 * @return 结果
	 */
	public static String lines(Object... lines) {
		StringBuilder bud = new StringBuilder();
		for (Object obj : lines)
			bud.append(obj + LN);
		return bud.toString();
	}

	/**
	 * 输出对象，并以换行符结尾
	 * 
	 * @param objs
	 *          对象列表
	 */
	public static void LN(Object... objs) {
		for (Object obj : objs) {
			System.out.print(obj.toString());
		}
		System.out.println("");
	}

	/**
	 * 取类的最后一段名称
	 * 
	 * @param clazz
	 *          类
	 * @return 结果
	 */
	public static String getClazzLastCode(Class clazz) {
		return getClazzLastCode(clazz.getName());
	}

	/**
	 * 取类的最后一段名称
	 * 
	 * @param clazzCode
	 *          类
	 * @return 结果
	 */
	public static String getClazzLastCode(String clazzCode) {
		String[] strs = split(clazzCode, ".");
		strs = split(strs[strs.length - 1], "$");
		return strs[strs.length - 1];
	}

	/**
	 * 取类的包名
	 * 
	 * @param clazz
	 *          类
	 * @return 结果
	 */
	public static String getClazzPackage(Class clazz) {
		return getClazzPackage(clazz.getName());
	}

	/**
	 * 取类的包名
	 * 
	 * @param clazzCode
	 *          类
	 * @return 结果
	 */
	public static String getClazzPackage(String clazzCode) {
		String[] strs = split(clazzCode, ".");
		String s = strs[0];
		for (int i = 1; i < strs.length - 1; i++)
			s += "." + strs[i];
		return s;
	}

	public static String getSplitLastCode(String code, String split) {
		String[] strs = split(code, split);
		return strs[strs.length - 1];
	}

	public static String formNumberToCode(String number) {
		int index = number.indexOf("-");
		return number.substring(index + 1);
	}

	/**
	 * 判断字符串的第1个字符是否为小写
	 * 
	 * @param str
	 *          字符串
	 * @return 结果
	 */
	public static boolean isFirstLower(String str) {
		return Character.isLowerCase(str.charAt(0));
	}

	/**
	 * 判断字符串的第1个字符是否为大写
	 * 
	 * @param str
	 *          字符串
	 * @return 结果
	 */
	public static boolean isFirstUpper(String str) {
		return Character.isUpperCase(str.charAt(0));
	}

	/**
	 * 将以指定分隔符分隔的字符串转化为字符串数组，并去掉前后空格
	 * 
	 * @param fieldList
	 *          String 字符串
	 * @param separate
	 *          String 分隔符，为null表示以空白字符分隔
	 * @return String[] 结果
	 */
	public static String[] split(String fieldList, String separate) {
		if (fieldList == null)
			return TMP_STRS;
		StringTokenizer st;
		if (separate == null)
			st = new StringTokenizer(fieldList);
		else
			st = new StringTokenizer(fieldList, separate);
		String[] _fields = new String[st.countTokens()];
		for (int i = 0; st.hasMoreTokens(); i++) {
			_fields[i] = st.nextToken().trim();
		}
		return _fields;
	}

	/**
	 * 取以指定分隔符分隔的字符串的第ID段内容，
	 * 
	 * @param fieldList
	 *          String 字符串
	 * @param separate
	 *          String 分隔符，为null表示以空白字符分隔
	 * @param id
	 *          取第几段,值从0开始，0表示第1段
	 * @return 结果，如ID太大则返回“”
	 */
	public static String split(String fieldList, String separate, int id) {
		if (fieldList == null)
			return "";
		String[] _fields = split(fieldList, separate);
		if (id >= _fields.length)
			return "";
		return _fields[id];
	}

	/**
	 * 取最后一段内容，如aa.bb.cc, 以"."为分隔符的结果为"cc"
	 * 
	 * @param str
	 *          字符串
	 * @param separate
	 *          分隔符
	 * @return 结果
	 */
	public static String lastStr(String str, String separate) {
		String[] strs = split(str, separate);
		return strs[strs.length - 1];
	}

	/**
	 * 取除最后一段的内容，如aa.bb.cc, 以"."为分隔符的结果为"aa.bb."
	 * 
	 * @param str
	 *          字符串
	 * @param separate
	 *          分隔符
	 * @return 结果
	 */
	public static String lastStrWithout(String str, String separate) {
		String[] strs = split(str, separate);
		return str.substring(0, str.length() - strs[strs.length - 1].length());
	}

	/**
	 * 取字符串左边指定长度的字符
	 * 
	 * @param str
	 *          String 字符串
	 * @param len
	 *          int 长度
	 * @return String 结果
	 */
	public static String left(String str, int len) {
		int l = str.length();
		if (len > l) {
			return str;
		}
		return str.substring(0, len);
	}

	/**
	 * 取字符串左边“总长度-指定长度”个数的字符
	 * 
	 * @param str
	 *          String 字符串
	 * @param subLen
	 *          要减去的长度
	 * @return String 结果
	 */
	public static String leftSub(String str, int subLen) {
		int len = str.length();
		if (subLen > len)
			return "";
		return left(str, len - subLen);
	}

	/**
	 * 取字符串右边指定长度的字符
	 * 
	 * @param str
	 *          String 字符串
	 * @param len
	 *          int 长度
	 * @return String 结果
	 */
	public static String right(String str, int len) {
		int l = str.length();
		if (len > l) {
			return str;
		}
		return str.substring(l - len);
	}

	/**
	 * 将带下划线的字符串转化为首字均小写，其它字的首字符大写的字符串， 如：tran_code 将转成 tranCode, TRAN_CODE 转换成
	 * tranCode
	 * 
	 * @param lineCode
	 *          带“_”的字符
	 * @return 结果
	 */
	public static String tranLineToField(String lineCode) {
		String[] st = split(lineCode.toLowerCase(), "_");
		StringBuffer buf = new StringBuffer();
		buf.append(st[0].toLowerCase());
		for (int i = 1; i < st.length; i++) {
			buf.append(tranFirstUpper(st[i]));
		}
		return buf.toString();
	}

	/**
	 * 将字符串的首字符转为大写
	 * 
	 * @param lineCode
	 *          字符串
	 * @return 结果
	 */
	public static String tranFirstUpper(String lineCode) {
		if (lineCode.length() == 0)
			return lineCode;
		return left(lineCode, 1).toUpperCase() + lineCode.substring(1);
	}

	/**
	 * 将字符串的首字符转为小写
	 * 
	 * @param lineCode
	 *          字符串
	 * @return 结果
	 */
	public static String tranFirstLower(String lineCode) {
		if (lineCode.length() == 0)
			return lineCode;
		return left(lineCode, 1).toLowerCase() + lineCode.substring(1);
	}

	/**
	 * 将首字均小写，其它字的首字符大写的字符串转化成带下划线全部小写的字符串， 如：tranCode 将转成 tran_code
	 * 
	 * @param fieldCode
	 *          要转换的字符串
	 * @return 结果
	 */
	public static String tranFieldToLineLower(String fieldCode) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < fieldCode.length(); i++) {
			if (Character.isUpperCase(fieldCode.charAt(i)) && i != 0)
				buf.append("_");
			// if (Character.isDigit(fieldCode.charAt(i)) && i != 0)
			// buf.append("_");
			buf.append(fieldCode.charAt(i));
		}
		return buf.toString().toLowerCase();
	}

	/**
	 * 将首字均小写，其它字的首字符大写的字符串转化成带下划线全部大写的字符串， 如：tranCode 将转成 TRAN_CODE
	 * 
	 * @param fieldCode
	 *          要转换的字符串
	 * @return 结果
	 */
	public static String tranFieldToLineUpper(String fieldCode) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < fieldCode.length(); i++) {
			if (Character.isUpperCase(fieldCode.charAt(i)))
				buf.append("_");
			buf.append(fieldCode.charAt(i));
		}
		return buf.toString().toUpperCase();
	}

	/**
	 * 将大写与下划线的字符串转成Feild类型，如： TRAN_CODE 将转成 tranCode
	 * 
	 * @param fieldCode
	 *          要转换的字符串
	 * @return 结果
	 */
	public static String tranLineUpperToField(String fieldCode) {
		StringBuffer buf = new StringBuffer();
		boolean up = false;
		char c;
		for (int i = 0; i < fieldCode.length(); i++) {
			c = fieldCode.charAt(i);
			if (c == '_') {
				up = true;
				continue;
			}

			if (!up) {
				buf.append(Character.toLowerCase(c));
				continue;
			}
			buf.append(c);
			up = false;
		}
		return buf.toString();
	}

	/**
	 * 将Model类代码转为对应的MI实现类代码
	 * 
	 * @param modelClass
	 *          模型类
	 * @return String MI实现类
	 */
	public static String tranModelClassToDAOClass(String modelClass) {
		return modelClass + "DAO";
	}

	public static String tranActionClassToDAOClass(String actionClass) {
		actionClass = actionClass.replaceFirst(".action.", ".bean.");
		actionClass = actionClass.replaceFirst("Action", "DAO");
		return actionClass;
	}

	public static final String replaceFirst(String rec, String old, String newS) {
		StringBuffer buf = new StringBuffer();
		int idx = rec.indexOf(old);
		if (idx < 0)
			return rec;
		buf.append(rec.substring(0, idx));
		buf.append(newS);
		buf.append(rec.substring(idx + old.length()));
		return buf.toString();
	}

	public static final Date toDate(String date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateFormat.parse(date);
		} catch (Exception e) {
			return null;
		}
	}

	public static String fillRight(String str, int len) {
		StringBuffer buf = new StringBuffer();
		int strlen = 0;
		byte[] bytes = str.getBytes();
		for (int i = 0; i < bytes.length; i++)
			strlen += bytes[i] > 127 || bytes[i] < -128 ? 2 : 1;
		for (int i = len - strlen; i > 0; i--)
			buf.append(" ");
		return buf.toString() + str;
	}

	public static String fillLeft(String str, int len) {
		StringBuffer buf = new StringBuffer();
		int strlen = 0;
		byte[] bytes = str.getBytes();
		for (int i = 0; i < bytes.length; i++)
			strlen += bytes[i] > 127 || bytes[i] < -128 ? 2 : 1;
		for (int i = len - strlen; i > 0; i--)
			buf.append(" ");
		return str + buf.toString();
	}

	public static String getModuleCode(String clazzCode) {
		return clazzCode.split("\\.")[2];
	}

	public static int getCount(String code, String serparate) {
		int i = 0;
		while (code.indexOf(serparate) != -1) {
			code = code.substring(code.indexOf(serparate) + 1);
			i++;
		}
		return i;
	}

	public static int getWordCount(String s) {
		s = s.replaceAll("[^\\x00-\\xff]", "**");
		int length = s.length();
		return length;
	}

	public static final String[] zodiacArr = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };

	public static final String[] constellationArr = { "水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
	    "天蝎座", "射手座", "魔羯座" };

	public static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };

	/**
	 * 根据日期获取生肖
	 * 
	 * @return
	 */
	public static String dateToZodica(Date date) {
		int index = (date.getYear() + 1900) % 12;
		return zodiacArr[index];
	}

	/**
	 * 根据日期获取星座
	 * 
	 * @param time
	 * @return
	 */
	public static String dateToStar(Date date) {
		int month = date.getMonth();
		int day = date.getDate();
		if (day < constellationEdgeDay[month]) {
			month = month - 1;
		}
		if (month >= 0) {
			return constellationArr[month];
		}
		// default to return 魔羯
		return constellationArr[11];
	}

	public static String addSpile(int num, String spil, String str) {
		String ren = "";
		for (int i = 0; i <= str.length() / num; i++) {
			int big = num * i;
			int end = num * (i + 1);
			if (end > str.length())
				end = str.length();
			ren = ren + spil + str.substring(big, end);
		}
		return ren.equals("") ? ren : ren.substring(spil.length());
	}

	// 将文章内容分成数组
	public static String acticlePager(int size, String spil, String content) {
		if (isEmpty(content))
			return "";
		ArrayList<String> list = new ArrayList<String>();
		boolean end = true;
		int length = content.length();
		while (end) {
			String temp = "";
			/**
			 * 如果内容已经少于默认数，就直接作为结尾返回
			 */
			if (size >= length) {
				temp = content;
				list.add(temp);
				break;
			}
			/**
			 * temp为本次截取内容段 temp2为余下的内容段
			 */
			temp = content.substring(0, size);
			String temp2 = content.substring(size + 1, length);
			String session = temp;
			int a = 0;
			int b = 0;
			/**
			 * 首先计算 <和>是否相等
			 */
			while (temp.indexOf(" <") > -1) {
				a++;
				temp = temp.substring(temp.indexOf(" <") + 1, temp.length());
			}
			temp = session;
			while (temp.indexOf(">") > -1) {
				b++;
				temp = temp.substring(temp.indexOf(">") + 1, temp.length());
			}
			if (a != b) {
				int p = temp2.indexOf(">");
				temp = content.substring(0, size + p + 2);
				temp2 = content.substring(size + p + 2, length);
				session = temp;
			}
			/**
			 * 如果相等就再计算 <P和 </p> 是否吻合
			 */
			if (a == b) {
				a = 0;
				b = 0;
				temp = session;
				while (temp.indexOf(" <P") > -1) {
					a++;
					temp = temp.substring(temp.indexOf(" <") + 1, temp.length());
				}
				temp = session;
				while (temp.indexOf(" </P") > -1) {
					b++;
					temp = temp.substring(temp.indexOf(">") + 1, temp.length());
				}
				if (a == b) {
					break;
				}
				if (a != b) {
					int p = temp2.indexOf(" </P>");
					temp = content.substring(0, size + p + 5);
					try {
						if ((size + p + 5) < length)
							temp2 = content.substring(size + p + 5, length);
					} catch (Exception e) {
						temp2 = "";
					}
				}
			}
			/**
			 * 余下内容更新
			 */
			content = temp2;
			length = content.length();
			// System.out.println("cut after:"+content);
			System.out.println("cut after:" + temp);
			list.add(temp);
			/**
			 * 如果不存在余下内容了就结束本次操作
			 */
			if (temp2.equals("") || temp2.length() < 1)
				end = false;
		}
		StringBuffer res = new StringBuffer();
		for (String str : list) {
			res.append(spil + str);
		}
		return res.length() == 0 ? "" : res.substring(spil.length());
	}

	public static final String randomString(int length) {
		Random randGen = null;
		char[] numbersAndLetters = null;
		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			randGen = new Random();
			numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
			    .toCharArray();
			// numbersAndLetters =
			// ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
			// randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
		}
		return new String(randBuffer);
	}

	//	public static void main(String[] args) {
	//		// System.out.println("run this is 1===" +
	//		// mumChangeNCR2GB("&#37038;&#20214;&#27169;&#29256;&#27979;&#35797;&#39033;&#30446;1833");
	//		System.out.println(mumChangeNCR2GB("济南吉宇&#183;勋业代理销售有限公司"));
	//		System.out
	//		    .println(mumChangeNCR2GB("&#37038;&#20214;&#27169;&#29256;&#27979;&#35797;&#39033;&#30446;1833"));
	//		System.out
	//		    .println(mumChangeNCR2GB("whx   &#20214;&#27169;&#29256;&#27979;&#35797;&#39033;&#30446;1833"));
	//		System.out.println(mumChangeGB2NCR("whx123中国_-%，"));
	//		// System.out.println("change ncr==="+singChangeGB2NCR("mm"));
	//	}

	/**
	 * Methods Descrip:这是一个将单个的NCR编码的字符转换成GBK编码的汉字
	 * 
	 * @param ncrStr
	 *          :NCR编码的字符
	 * @return: String GBK编码的汉字
	 */
	private static String singChangeNCR2GB(String ncrStr) {
		if (ncrStr.indexOf("&") == 0) {
			String ls = ncrStr.substring(2);
			int lnum = Integer.parseInt(ls);
			String res = Integer.toHexString(lnum);
			byte[] neByte = new byte[2];
			if (res.length() > 3) {
				neByte[0] = (byte) Integer.parseInt(res.substring(0, 2), 16);
				neByte[1] = (byte) Integer.parseInt(res.substring(2), 16);
			} else if (res.length() == 3) {
				return "";
			} else {
				if (lnum > 128) {
					return "";
				} else {
					neByte[0] = 0;
					neByte[1] = (byte) Integer.parseInt(res, 16);
				}
			}
			String lss = "";
			try {
				lss = new String(neByte, "utf-16be");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return lss;
		} else if (ncrStr.indexOf("&") > 0) {
			int bgs = ncrStr.indexOf("&");
			String festr = ncrStr.substring(0, bgs);
			String chstr = ncrStr.substring(bgs + 2);
			int lnum = Integer.parseInt(chstr);
			String res = Integer.toHexString(lnum);
			byte neByte[] = new byte[2];
			if (res.length() > 3) {
				neByte[0] = (byte) Integer.parseInt(res.substring(0, 2), 16);
				neByte[1] = (byte) Integer.parseInt(res.substring(2), 16);
			} else if (res.length() == 3) {
				return festr + " ";
			} else {
				if (lnum > 128) {
					return festr + " ";
				} else {
					neByte[0] = 0;
					neByte[1] = (byte) Integer.parseInt(res, 16);
				}
			}
			String lss = "";
			try {
				lss = new String(neByte, "utf-16be");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return festr + lss;
		} else {
			return ncrStr;
		}
	}

	/**
	 * Methods Descrip:这是多个将单个的NCR编码的字符转换成GBK编码的汉字
	 * 
	 * @param ncrStr
	 *          :多个将单个的NCR编码的字符
	 * @return:String GBK编码的汉字
	 * 
	 */
	public static String mumChangeNCR2GB(String ncrStr) {
		if (!ncrStr.contains(";")) {
			return singChangeNCR2GB(ncrStr);
		} else {
			String[] strScr = toArray(ncrStr, ";");
			String[] strRstr = new String[strScr.length];
			for (int i = 0; i < strRstr.length; i++) {
				strRstr[i] = singChangeNCR2GB(strScr[i]);
			}
			String res = arrayToString(strRstr, "");
			return res;
		}
	}

	public static String mumChangeGB2NCR(String str) {
		if (Str.isEmpty(str))
			return null;
		String rst = "";
		for (int i = 0; i < str.length(); i++) {
			String line = String.valueOf(str.charAt(i));
			if (line.equals("&") || line.equals("#") || line.equals(";"))
				continue;
			if (line.getBytes().length == 1) {
				rst += line;
				continue;
			}
			rst += singChangeGB2NCR(line);
		}
		return rst;
	}

	/**
	 * Methods Descrip:这是一个将单个汉字转换成NCR编码的字符
	 * 
	 * @param str
	 *          :以GBK编吗的汉字
	 * @return:NCR编码的字符串
	 */
	public static String singChangeGB2NCR(String str) {
		String res = "";
		try {
			res = new String(str.getBytes("GB2312"), "utf-16");
			byte[] utf_16E = str.getBytes("utf-16be");
			String Str16k = byteTo16String(utf_16E);
			System.out.println("utf_16E===" + Str16k);
			int ncrNum = Integer.parseInt(Str16k, 16);
			res = "&#" + ncrNum + ";";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Methods Descrip:将byte数组转换成以utf-16be的String字符
	 * 
	 * @param bt
	 *          :byte类型的数组
	 * @return:String 以utf-16be编码的字符
	 * 
	 */
	public static String byteTo16String(byte[] bt) {
		String res = "";
		for (int i = 0; i < bt.length; i++) {
			int hex = (int) bt[i] & 0xff;
			System.out.print(Integer.toHexString(hex) + " ");
			res = res + Integer.toHexString(hex);
		}
		return res;
	}

	/**
	 * Methods Descrip:将一个字符串按照分隔符分解成一个字符串数组
	 * 
	 * @param: s 需要分解的字符串
	 * @param: delimiter 字符串分隔符
	 * @return:String[] 分解后的字符串数组
	 */
	public static String[] toArray(String s, String delimiter) {
		if (s == null) {
			return null;
		}
		if (s.equals("")) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(s, delimiter);
		ArrayList vec = new ArrayList();
		for (int i = 0; st.hasMoreTokens(); i++) {
			String t = st.nextToken();
			if ((t != null) && (t.length() > 0)) {
				vec.add(t.trim());
			}
		}
		if (vec.size() == 0) {
			return null;
		}
		String[] kw = (String[]) vec.toArray(new String[vec.size()]);
		return kw;
	}

	/**
	 * Methods Descrip:将一个字符串数组按照指定的分隔组合成一个字符串
	 * 
	 * @param s
	 *          :需要转换的字符串数组
	 * @param delimiter
	 *          :指定的分隔符
	 * @return String:组合后的字符串
	 * 
	 */
	public static String arrayToString(String[] s, String delimiter) {
		String res = "";
		for (int i = 0; i < s.length; i++) {
			res = res + s[i];
			if (i < (s.length - 1)) {
				res = res + delimiter;
			}
		}
		return res;
	}

	public static String getEncoding(String str) {
		try {
			if (str.equals(new String(str.getBytes(ENCODING_GB), ENCODING_GB)))
				return ENCODING_GB;
		} catch (Exception exception) {
		}
		try {
			if (str.equals(new String(str.getBytes(ENCODING_ISO), ENCODING_ISO)))
				return ENCODING_ISO;
		} catch (Exception exception1) {
		}
		try {
			if (str.equals(new String(str.getBytes(ENCODING_UTF8), ENCODING_UTF8)))
				return ENCODING_UTF8;
		} catch (Exception exception2) {
		}
		try {
			if (str.equals(new String(str.getBytes(ENCODING_GBK), ENCODING_GBK)))
				return ENCODING_GBK;
		} catch (Exception exception3) {
		}
		return "";
	}

}
