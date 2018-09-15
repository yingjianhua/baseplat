package irille.pub.tb;

import irille.pub.ClassTools;
import irille.pub.Log;
import irille.pub.Str;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Title: 枚举明细行<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class EnumLine  {
	private static final Log LOG = new Log(EnumLine.class);
	private static Hashtable<Class, Vector<EnumLine>>  _lines = new Hashtable();

	private Enum _enum;
	private byte _key;
	private String _name;

	public EnumLine(Enum enumObj,int key, String name) {
		_enum=enumObj;
		_key = (byte)key;
		_name = name;
		getLines().add(this);
	}
	public EnumLine(Enum enumObj,IEnumOpt e) {
		this(enumObj,e.getLine()._key,e.getLine()._name);
	}
	
	public Vector<EnumLine> getLines() {
		 Vector<EnumLine> lines=_lines.get(_enum.getClass());
		if(lines==null) {
			lines=new Vector();
			_lines.put(_enum.getClass(), lines);
		}
		return lines;
	}
	
	
	public Byte getDefaultValue() {
		IEnumOpt e=null;
		try {
			e=(IEnumOpt)ClassTools.getStaticProerty(_enum.getClass(), "DEFAULT");
		} catch (Exception e2) {
		}
		if(e==null)
			return null;
		return e.getLine().getKey();
	}
	
	/**
	 * @return the key
	 */
	public byte getKey() {
		return _key;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return _name;
	}

	public Enum getEnum() {
		return _enum;
	}
	public Enum get(int i) {
		for(EnumLine e: getLines())
			if(e.getKey()==i)
				return e._enum;
		throw LOG.err("EnumLineNotFound", "选项[" + getNameEnum() + "]值为[{0}]的选项行没找到", i);
	}
	public Enum chk(int i) {
		for(EnumLine e: getLines())
			if(e.getKey()==i)
				return e._enum;
		throw null;
	}
	
	public IEnumOpt[] getValues() {
		Method method=ClassTools.getMethod(_enum.getClass(), "values");
		return (IEnumOpt[])ClassTools.run(method,_enum);		
	}
	
	public String getNameEnum() {
		return (String)ClassTools.getStaticProerty(_enum.getClass(),"NAME");
	}
	/**
	 * 返回field类型的名称
	 * @return
	 */
	public String getCodeEnum() {
		String s= Str.getClazzLastCode(_enum.getClass()).substring(1);
				return s;
	}
	
	/**
	 * 返回枚举的变量名，如：DR
	 * @return
	 */
	public String getVarName() {
		return _enum.name();
	}
	/**
	 * 返回枚举类名的最后一段，如：DIRECT
	 * @return
	 */
	public String getClazz() {
		return Str.getClazzLastCode(_enum.getClass());
	}
	/**
	 * 返回数据类型，用于打印实体变量定义的说明用 格式如："DIRECT.DR"
	 * 
	 * @return 数据类型与长度的文本
	 */
	public String outClazzAndCode() {
		return Str.getClazzLastCode(_enum.getClass());
	}
	
	public String getCode() {
		return Str.tranLineToField(_enum.name());
	}
	/**
	 * 返回显示的宽度，取最长的明细行的长度
	 * @return
	 */
	public short getWidth() {
		int width=0;
		int w;
		for (EnumLine e: getLines()) {
			w=Str.len(e.getName());
			if(width<w)
				width=w;
		}
		return (short)width;
	}

}
