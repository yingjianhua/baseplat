//Created on 2005-9-17
package irille.pub;

import irille.pub.valid.ValidDate;
import irille.pub.valid.ValidNumber;
import irille.pub.view.Fmts;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

/**
 * Title: 对象处理类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class Obj {
	private static final Log LOG = new Log(Obj.class);

	/**
	 * 比较对象的值，同时为null返回true, 一者为null返回false，都不是null调用equals方法比较
	 * 
	 * @param obj1
	 *            对象1
	 * @param obj2
	 *            对象2
	 * @return 结果
	 */
	public static boolean equals(Object obj1, Object obj2) {
		if (obj1 == obj2)
			return true;
		if (obj1 == null || obj2 == null)
			return false;
		if (obj1 instanceof Date && obj2 instanceof Date)
			return ValidDate.equals((Date) obj1, (Date) obj2);
		if (obj1 instanceof BigDecimal && obj2 instanceof BigDecimal) {
			return ((BigDecimal) obj1).compareTo((BigDecimal) obj2) == 0;
		}
		return obj1.equals(obj2);
	}

	public static BigDecimal getRate100(BigDecimal dec1, BigDecimal dec2) {
		if (dec2.signum() == 0)
			return BigDecimal.ZERO;
		return dec1.divide(dec2, dec1.scale(), BigDecimal.ROUND_UP).multiply(
				new BigDecimal("100"));
	}

	/**
	 * Blob转为Byte数组
	 * 
	 * @param blob
	 * @return
	 */
	public static byte[] tranToBytes(Blob blob) {
		try {
			return blob.getBytes(1, (int) blob.length());
		} catch (Exception e) {
			throw LOG.err("io", "IO流输出出错!");
		}
	}

	public static Object getSmall(Object obj1, Object obj2) {
		int rst = compare(obj1, obj2);
		if (rst > 1)
			return obj1;
		return obj2;
	}

	public static Date getStringToDate(String localDate) {
		String[] obs = localDate.split("\\ ")[0].split("\\-");
		if (obs.length < 3)
			throw LOG.err("tranDate", "日期转换出错!");
		return new Date(Integer.parseInt(obs[0]) - 1900,
				Integer.parseInt(obs[1]) - 1, Integer.parseInt(obs[2]));
	}

	/**
	 * 字符串输出
	 * 
	 * @param obj
	 *            对象
	 * @return 结果
	 */
	public static String toString(Object obj) {
		if (obj instanceof Date)
			return Fmts.FMT_YYYY_MM_DD.out(null, obj);
		else if (obj instanceof String)
			return "" + '"' + obj + '"';
		return obj.toString();
	}

	/**
	 * 比较对象大小
	 * 
	 * @param obj1
	 *            对象1
	 * @param obj2
	 *            对象2
	 * @return 结果，obj1>obj2返回1，obj1==obj2返回0，否则为-1
	 */
	public static int compare(Object obj1, Object obj2) {
		if (obj1 == null || obj2 == null)
			throw LOG.err("nullCompare", "空值不能进行比较!");
		if (!obj1.getClass().equals(obj2.getClass())
				|| obj1.getClass().equals(Boolean.class))
			throw LOG.err("typeErr",
					"[{0}]类型的数据值[{1}]不与同[{2}]类型的数据值[{3}]进行比较!", obj1.getClass()
							.getName(), obj1, obj2.getClass().getName(), obj2);
		if (obj1 instanceof Date)
			return ValidDate.DEFAULT.compare(obj1, obj2);
		if (obj1 instanceof BigDecimal)
			return ValidNumber.DEFAULT.compare(obj1, obj2);
		return ((java.lang.Comparable) obj1).compareTo(obj2);
	}

	/**
	 * 对象序列化后重新读入
	 * 
	 * @param obj
	 *            Object 要序列化的对象
	 * @throws Exception
	 *             出错
	 * @return Object 重读的对象
	 */
	public static Object readSerial(Object obj) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(obj);
			os.close();
			byte[] buf = out.toByteArray();
			// System.out.println("["+obj.getClass().getName()+"]Serial size="+buf.length);
			ObjectInputStream is = new ObjectInputStream(
					new ByteArrayInputStream(buf));
			return is.readObject();
		} catch (Exception e) {
			throw LOG.errSerial(e, obj.getClass());
		}
	}

	public static Dimension getPFSize(Component com, Font font, String code) {
		FontMetrics fm = com.getFontMetrics(font);
		return new Dimension(fm.stringWidth(code), fm.getHeight());
	}
	
}
