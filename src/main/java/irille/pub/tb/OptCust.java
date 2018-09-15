//Created on 2005-9-27
package irille.pub.tb;

import irille.core.sys.SysCtype;
import irille.core.sys.SysCtypeCode;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;

import java.util.List;

/**
 * Title: 选项类型基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class OptCust extends OptBase {

	public OptCust(String code, String name) {
		this(code, name, false);
	}

	public OptCust(String code, String name, boolean isGet) {
		super(code, name);
		//类之间相互关联，在运行测试时，读用户选项数据造成循环引用出错
		//仅在启动项目后，通过缓存取用户选项时会取值
		if (isGet == false)
			return;
		SysCtype opt = (SysCtype) Bean.get(SysCtype.class, code);
		setName(opt.getCtypeName());
		List<SysCtypeCode> list = BeanBase.list(SysCtypeCode.class, " ctype_type=? ORDER BY code_value", false, opt.getPkey());
		for (SysCtypeCode line : list) {
			add(line.getCodeValue(), line.getCodeName(), line.getCodeDes(), null, true);
		}
	}

	public TYPE getType() {
		return TYPE.OPT_CUST;
	}
}
