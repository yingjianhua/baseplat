package irille.pub.inf;

import irille.pss.sal.SalSale;
import irille.pub.bean.Bean;

import java.math.BigDecimal;

/**
 * 销账计划核销时，更新原单据的回款金额
 * @author whx
 * @version 创建时间：2015年1月23日 上午10:05:00
 * @param <E>
 */
public interface IRecBack<E extends Bean> {

	public void updRecBack(E model, BigDecimal amt);

}
