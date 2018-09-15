package irille.action.pur;

import irille.action.ActionLineGoods;
import irille.core.sys.Sys;
import irille.core.sys.SysTable;
import irille.core.sys.SysTemplatCellDAO;
import irille.pss.pur.Pur;
import irille.pss.pur.PurOrder;
import irille.pss.pur.PurOrderLine;
import irille.pss.pur.PurProtGoods;
import irille.pub.bean.BeanBase;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PurOrderLineAction extends ActionLineGoods<PurOrderLine> {

	public PurOrderLine getBean() {
		return _bean;
	}

	public void setBean(PurOrderLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return PurOrderLine.class;
	}

	@Override
	public void list() throws Exception {
		// TODO Auto-generated method stub
		super.list();
	}

	public void listForPrice() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		Long pk = (Long) getPkey();
		long key1 = pk * SysTable.NUM_BASE;
		long key2 = key1 + SysTable.NUM_BASE;
		String where = " pkey > " + key1 + " AND pkey < " + key2;
		List<PurOrderLine> list = BeanBase.list(PurOrderLine.class, where, false);
		JSONObject lineJson = null;
		for (PurOrderLine line : list) {
			PurProtGoods pg = PurProtGoods.chkUniqueTempCustObj(false,
			    SysTemplatCellDAO.getPurTmpl().getPkey(), BeanBase.load(PurOrder.class, pk)
			        .getSupplier(), line.getGoods());
			if (line.getPrice().compareTo(BigDecimal.ZERO) == 0) {
				if (pg.getPriceLast() != null)
					line.setPrice(pg.getPriceLast());
				else
					line.setPrice(pg.getPrice());
				line.setAmt(line.getPrice().multiply(line.getQty()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
			}
			lineJson = crtJsonByBean(line);
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		writerOrExport(json);
	}
}
