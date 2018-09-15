package irille.pub.inf;

import irille.pub.bean.IGoodsPrice;

import java.util.List;

public interface ICstInvoice {
	public List<IGoodsPrice> getInvoiceLines();
}
