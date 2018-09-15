package irille.action.pur;

import irille.action.ActionLineGoods;
import irille.core.sys.Sys;
import irille.core.sys.SysTable;
import irille.core.sys.SysTemplatCellDAO;
import irille.pss.pur.PurOrderDirect;
import irille.pss.pur.PurOrderDirectLine;
import irille.pss.pur.PurProtGoods;
import irille.pub.bean.BeanBase;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PurOrderDirectLineAction extends ActionLineGoods<PurOrderDirectLine> {

	public PurOrderDirectLine getBean() {
		return _bean;
	}

	public void setBean(PurOrderDirectLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return PurOrderDirectLine.class;
	}
	
	public void listForPrice() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		Long pk = (Long) getPkey();
		long key1 = pk * SysTable.NUM_BASE;
		long key2 = key1 + SysTable.NUM_BASE;
		String where = " pkey > " + key1 + " AND pkey < " + key2;
		List<PurOrderDirectLine> list = BeanBase.list(PurOrderDirectLine.class, where, false);
		JSONObject lineJson = null;
		for (PurOrderDirectLine line : list) {
			PurProtGoods pg = PurProtGoods.chkUniqueTempCustObj(false,
			    SysTemplatCellDAO.getPurTmpl().getPkey(), BeanBase.load(PurOrderDirect.class, pk)
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
