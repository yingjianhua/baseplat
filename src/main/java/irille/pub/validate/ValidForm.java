package irille.pub.validate;

import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanMain;
import irille.pub.idu.Idu;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.io.Serializable;

public class ValidForm extends ValidBase {
	private static final Log LOG = new Log(ValidForm.class);
	
	public enum ErrMsgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		wrongErr("【{0}】输入有误,请重新输入"),
		notNull("【{0}】字段不能为空"),
        notEmpty("【{0}】不能为空！"),
        existsErr("【{0}】中【{1}】为【{2}】的记录不存在")
        ;
        private String _msg;
        private ErrMsgs(String msg) {_msg = msg;}
        public String getMsg() {return _msg;}
    } //@formatter:on

	public ValidForm(Idu idu) {
		super(idu);
	}
	public ValidForm(Bean bean) {
		super(bean);
	}
	
	/**
	 * 根据表的某个字段判断是否存在记录
	 * @param fld
	 * @param params
	 */
	public <T extends BeanMain> void validExists(Tb tb, IEnumFld fld, Serializable param) {
		if(((Number)BeanBase.queryOneRow("SELECT count(*) FROM " + tb.getCodeSqlTb() + " WHERE " + fld.getFld().getCodeSqlField() + "=?", param)[0]).intValue() <= 0) {
			throw LOG.err(ErrMsgs.existsErr, tb.getName(), getName(fld), param);
		}
	}

	
	/**
	 * 校验设置的对象字段不为null
	 * 
	 * @param 需要校验的字段
	 * @author yingjianhua
	 */
	public void validNotNull(IEnumFld... flds) {
		if (fldsNotNull(flds))
			for (IEnumFld fld : flds) {
				if (getValue(fld) != null)
					throw LOG.err(ErrMsgs.notNull, getName(fld));
			}
	}
	/**
	 * 校验设置的对象字段不为空
	 * 
	 * @param 需要校验的字段
	 * @author yingjianhua
	 */
	public void validNotEmpty(IEnumFld... flds) {
		if (fldsNotNull(flds))
			for (IEnumFld fld : flds) {
				if (getValue(fld) == null || Str.isEmpty(getValue(fld).toString()))
					throw LOG.err(ErrMsgs.notEmpty, getName(fld));
			}
	}
	

	/**
	 * 校验空字符串
	 * 
	 * @param str
	 *            校验字符串
	 * @param paras
	 *            异常提示字段替换值
	 */
	public static void validEmpty(String str, Object... paras) {
		if (Str.isEmpty(str))
			throw LOG.err(ErrMsgs.notEmpty, paras);
	}

	/**
	 * 校验空字符串
	 * 
	 * @param obj
	 *            校验字符串字段对象
	 * @param paras
	 *            异常提示字段替换值
	 */
	private static void validEmpty(Object obj, Object... paras) {
		validEmpty(obj.toString(), paras);
	}

}