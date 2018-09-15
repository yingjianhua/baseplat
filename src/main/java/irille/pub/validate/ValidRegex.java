package irille.pub.validate;

import irille.pub.Log;
import irille.pub.PubInfs.IRegMsg;
import irille.pub.bean.Bean;
import irille.pub.idu.Idu;
import irille.pub.tb.IEnumFld;

import java.util.regex.Pattern;

public class ValidRegex extends ValidBase {
	private static final Log LOG = new Log(ValidRegex.class);
	private Idu idu;

	public enum RegErrMsgs implements IRegMsg {//@formatter:off
		aZLenErr("[a-zA-Z]'{'0,{0}}", "须为小于{0}位的英文字母"),
		isEmail("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$", "【{0}】输入有误,请重新输入"),
		isPhoneNumber("^[0-9]*$", "【{0}】输入有误,请重新输入"),
		withoutNumber(".*\\d+.","【{0}】不能有数字"),
		numFirstErr("[0-9]{1,}.*", "【{0}】不能以数字开头")
		;
		public static final String NAME="正则表达式验证";
		private String _reg;
		private String _msg;
		private RegErrMsgs(String reg, String msg) {
			_reg = reg;
			_msg = msg;
        }
		public String getReg() {return _reg;}
        public String getMsg() {return _msg;}
	}		//@formatter:on

	public ValidRegex(Idu idu) {
		super(idu);
	}
	public ValidRegex(Bean bean) {
		super(bean);
	}

	/**
	 * 匹配正则表达式
	 * 
	 * @param reg
	 *            正则表达式
	 * @param str
	 *            匹配字符串
	 * @return boolean
	 */
	public static boolean regMarch(String reg, String str) {
		return (Pattern.compile(reg).matcher(str)).matches();
	}

	
	/**
	 * 校验是否匹配正常表达式
	 * 
	 * @param isMach
	 *            true:不匹配抛出异常，false:匹配抛出异常
	 * @param rem
	 *            对应的正则枚举对象
	 * @param str
	 *            校验字符串
	 * @param paras
	 *            异常提示字段替换值
	 */
	private void regMarch(boolean isMarch, IRegMsg msg, IEnumFld... flds) {
		regMarch(isMarch, msg.getReg(), msg, flds);
	}
	private void regMarch(boolean isMarch, String reg, IRegMsg msg, IEnumFld... flds) {
		if(fldsNotNull(flds))
			for(IEnumFld fld:flds) {
				if (isMarch){
					if(!regMarch(reg, getString(fld)))
						throw LOG.err(msg, getName(fld));	
				}else{
					if(regMarch(reg, getString(fld)))
						throw LOG.err(msg, getName(fld));	
				}
			}
	}
	
	/**
	 * 字段需要符合reg的正则表达式，否则抛出异常
	 * 
	 * @param regex 正则表达式
	 * @param str 匹配字符串
	 * @return 
	 * @author yingjianhua
	 */
	public void validRegexMatched(IRegMsg regex, IEnumFld... flds) {
		regMarch(true, regex, flds);
	}
	public void validRegexMatched(String regex, IRegMsg msg, IEnumFld... flds) {
		regMarch(true, regex, msg, flds);
	}
	
	/**
	 * 若字段符合reg的正则表达式 则抛出异常
	 * 
	 * @param regex 正则表达式
	 * @param str 匹配字符串
	 * @return 
	 * @author yingjianhua
	 */
	public void validRegexNotMatched(IRegMsg regex, IEnumFld... flds) {
		regMarch(false, regex, flds);
	}
	public void validRegexNotMatched(String regex, IRegMsg msg, IEnumFld... flds) {
		regMarch(false, regex, msg, flds);
	}
	
	/**
	 * 校验Email
	 * @param flds
	 */
	public void validEmail(IEnumFld... flds) {
		validRegexMatched(RegErrMsgs.isEmail, flds);
	}
	/**
	 * 校验手机号码
	 * @param flds
	 */
	public void validPhone(IEnumFld... flds) {
		validRegexMatched(RegErrMsgs.isPhoneNumber, flds);
	}
	
	/**
	 * 校验小于指定位数英文字符串
	 * @param len 位数
	 * @param flds 校验字段
	 * @author yingjianhua
	 */
	public void validAZLen(Integer len, IEnumFld... flds) {
		validRegexMatched(Log.messageToString(RegErrMsgs.aZLenErr.getReg(), String.valueOf(len)), RegErrMsgs.aZLenErr, flds);
	}

	public void validWithoutNumber(IEnumFld... flds) {
		validRegexNotMatched(RegErrMsgs.withoutNumber, flds);
	}
	public void validNotStartWithNumber(IEnumFld... flds) {
		validRegexNotMatched(RegErrMsgs.numFirstErr, flds);
	}
}