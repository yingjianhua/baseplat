//Created on 2005-10-10
package irille.pub;

import irille.pub.bean.Bean;
import irille.pub.tb.Fld;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.Tb;

import java.util.ArrayList;

import org.apache.commons.lang3.ObjectUtils;

/**
 * Title: 属性集<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class Properties<T extends Properties> extends LockBase<T> {
	private static final Log LOG = new Log(Properties.class);
	private ArrayList _list = new ArrayList();
	private Fld _head = null;

	/**
	 * 构造方法
	 */
	public Properties() {
	}

	/**
	 * 构造方法
	 * @param headField
	 */
	public Properties(Fld headField) {
		_head = headField;
	}

	/**
	 * 增加字段
	 * @param fields 字段清单
	 * @return 当前对象
	 */
	public T add(Fld... fields) {
		assertUnlocked();
		for (int i = 0; i < fields.length; i++) {
			_list.add(fields[i]);
		}
		return (T) this;
	}

	/**
	 * 增加表的所有字段
	 * @param tableMsg 表信息
	 * @return 当前对象
	 */
	public T addAll(Tb tableMsg) {
		for (int i = 0; i < tableMsg.size(); i++) {
			add(tableMsg.get(i));
		}
		return (T) this;
	}

	/**
	 * 增加除指定字段列表外的所有字段
	 * @param withoutFields
	 * @return
	 */
	public T addWithout(Fld... withoutFields) {
		Tb tb = (Tb) withoutFields[0].getTb();
		Fld fld;
		for (int i = 0; i < tb.size(); i++) {
			fld = tb.get(i);
			if (fld instanceof FldLines || fld instanceof FldV)
				continue;
			boolean flag = false;
			for (Fld wfld : withoutFields)
				if (wfld.getCode().equals(fld.getCode()))
					flag = true;
			if (flag == false)
				add(tb.get(i));
		}
		return (T) this;
	}

	/**
	 * 增加子属性
	 * @param properties 属性集
	 * @return 当前对象
	 */
	public T add(Properties properties) {
		assertUnlocked();
		_list.add(properties);
		return (T) this;
	}

	/**
	 * 比较对象属性是否相等
	 * @param model1 对象1
	 * @param model2 对象2
	 */
	public boolean equals(Bean model1, Bean model2) {
		try {
			assertEquals(model1, model2);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 确认对象属性相等
	 * @param model1 对象1
	 * @param model2 对象2
	 */
	public void assertEquals(Bean model1, Bean model2) {
		ArrayList<String> list = getPropertyCodes();
		for (String p : list) {
			if (ObjectUtils.equals(PropertyUtils.getProperty(model1, p),
			    PropertyUtils.getProperty(model2, p)))
				continue;
			throw LOG.err("assertEquals", "类[{5}对象[{0}]与[{1}]的属性[{2}]值[{3}]与[{4}]不相等!",
			    model1.toString(), model2.toString(), p, PropertyUtils.getProperty(model1, p),
			    PropertyUtils.getProperty(model2, p), model1.getClass().getName());
		}
		return;
	}

	/**
	 * 将from对象的指定属性复制到to对象中
	 * @param from 来自
	 * @param to 到
	 */
	public void copy(Bean to, Bean from) {
		ArrayList<String> list = getPropertyCodes();
		for (String p : list) {
			PropertyUtils.setProperty(to, p, PropertyUtils.getProperty(from, p));
		}
	}

	/**
	 * 取属性字段列表
	 * @param head 头字符，为null表示没有
	 * @return 结果
	 */
	public ArrayList<String> getPropertyCodes() {
		ArrayList<String> list = new ArrayList();
		addPropertyCodesToList(list, null);
		return list;
	}

	/**
	 * 取属性字段列表
	 * @param head 头字符，为null表示没有, 最后要加"."
	 * @return 结果
	 */
	public void addPropertyCodesToList(ArrayList<String> list, String head) {
		if (head == null || head.equals(""))
			head = "";
		if (_head != null)
			head += _head.getCode() + ".";
		for (Object obj : _list) {
			if (obj instanceof Fld)
				list.add(head + ((Fld) obj).getCode());
			else
				((Properties) obj).addPropertyCodesToList(list, head);
		}
	}

	/**
	 * 取属性字段列表
	 * @param head 头字符，为null表示没有, 最后要加"."
	 * @return 结果
	 */
	public void addPropertyNamesToList(ArrayList<String> list, String head) {
		if (head == null || head.equals(""))
			head = "";
		if (_head != null)
			head += _head.getName() + ".";
		for (Object obj : _list) {
			if (obj instanceof Fld)
				list.add(head + ((Fld) obj).getName());
			else
				((Properties) obj).addPropertyNamesToList(list, head);
		}
	}

	public T setHeadField(Fld field) {
		_head = field;
		return (T) this;
	}
}
