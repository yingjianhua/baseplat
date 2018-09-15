package irille.pub.validate;

import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.Bean;
import irille.pub.idu.Idu;
import irille.pub.tb.IEnumFld;

import java.math.BigDecimal;
import java.util.Date;

public class ValidNumber extends ValidBase {
	private static final Log LOG = new Log(ValidNumber.class);
	
	public enum ErrMsgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		bePositive("【{0}】必须为正数"),
		bigDecimalRange("请将【{0}】设置到{1}~{2}之间"),
		dateRange("请将【{0}】设置到{1}~{2}之间"),
		afterDate("请将【{0}】设置到{1}之后"),
		date1BeforeDate2("请将【{0}】设置到【{1}】之前"),
		bigDecimal1LessThanBigDecimal2("【{0}】必须小于【{1}】"),
        ;
        private String _msg;
        private ErrMsgs(String msg) {_msg = msg;}
        public String getMsg() {return _msg;}
    } //@formatter:on

	public ValidNumber(Idu idu) {
		super(idu);
	}
	public ValidNumber(Bean bean) {
		super(bean);
	}

	/**
	 * 字段必须为正整数
	 * 
	 * @param flds 需要校验的字段
	 * @author yingjianhua
	 */
	public void validIntegerPositive(IEnumFld... flds) {
		if (fldsNotNull(flds))
			for (IEnumFld fld : flds) {
				if(getInteger(fld)<0)
					throw LOG.err(ErrMsgs.bePositive, getName(fld));
			}
	}
	public void validBigDecimalPositive(IEnumFld... flds) {
		if (fldsNotNull(flds))
			for (IEnumFld fld : flds) {
				if(getBigDecimal(fld).compareTo(BigDecimal.ZERO)<0)
					throw LOG.err(ErrMsgs.bePositive, getName(fld));
			}
	}
	public void validBigDecimal1LessThanBigDecimal2(IEnumFld fld1, IEnumFld fld2) {
		if(getBigDecimal(fld1).compareTo(getBigDecimal(fld2))>=0)
			throw LOG.err(ErrMsgs.bigDecimal1LessThanBigDecimal2, getName(fld1), getName(fld2));
	}
	/**
	 * 字段必须在指定范围内
	 * 
	 * @param min 最小值
	 * @param max 最大值
	 * @param flds 需要校验的字段
	 * @author yingjianhua
	 */
	public void validBigDecimalRange(BigDecimal min, BigDecimal max, IEnumFld... flds) {
		if (fldsNotNull(flds))
			for (IEnumFld fld : flds) {
				BigDecimal v = getBigDecimal(fld);
				if(v.compareTo(min)<0||v.compareTo(max)>0)
					throw LOG.err(ErrMsgs.bigDecimalRange, getName(fld), min.toString(), max.toString());
				}
	}
	
	public void validAfterDate(Date date, IEnumFld...flds) {
		if (fldsNotNull(flds))
			for (IEnumFld fld : flds) {
				if(!getDate(fld).after(date))
					throw LOG.err(ErrMsgs.afterDate, getName(fld), date);
			}
	}
	public void validDate1BeforeDate2(IEnumFld fld1, IEnumFld fld2) {
		if(!getDate(fld1).before(getDate(fld2)))
			throw LOG.err(ErrMsgs.afterDate, getName(fld1), getName(fld2));
	}
	/**
	 * 字段必须在指定范围内
	 * 
	 * @param min 最小值
	 * @param max 最大值
	 * @param flds 需要校验的字段
	 * @author yingjianhua
	 */
	public void validDateRange(BigDecimal min, BigDecimal max, IEnumFld... flds) {
		if (fldsNotNull(flds))
			for (IEnumFld fld : flds) {
				BigDecimal v = getBigDecimal(fld);
				if(v.compareTo(min)<0||v.compareTo(max)>0)
					throw LOG.err(ErrMsgs.bigDecimalRange, getName(fld), min.toString(), max.toString());
				}
			}
}