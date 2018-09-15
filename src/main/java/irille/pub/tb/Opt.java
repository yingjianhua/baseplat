//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.Log;
import irille.pub.Str;

/**
 * Title: 选项类型基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class Opt extends OptBase<Opt> {
	private static final Log LOG = new Log(Opt.class);
	private String _clazzName;

	public Opt(Log log, String code, String name) {
		super(code, name);
		_clazzName = log.getClazzName();
	}

	public String getClazzName() {
		return _clazzName;
	}

	public TYPE getType() {
		return TYPE.OPT;
	}

	/**
	 * 返回数据类型，用于打印实体变量定义的说明用 格式如："BaseOpt.ENABLE"
	 * 
	 * @return 数据类型与长度的文本
	 */
	public String outClazzAndCode() {
		return Str.getClazzLastCode(_clazzName) + "."
		    + Str.tranFieldToLineUpper(getCode());
	}
}
