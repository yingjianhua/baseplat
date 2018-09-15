package irille.gl.gs;

import irille.core.sys.SysCell;
import irille.core.sys.SysOrg;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.BeanBase;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

public class GsPriceCtlDAO {
	public static final Log LOG = new Log(GsPriceCtlDAO.class);

	//取核算单元的定价控制；如果不存在取对应机构的定价控制
	public static GsPriceCtl getPriceCtrl(SysCell cell) {
		GsPriceCtl ctl = GsPriceCtl.chkUniqueObj(false, cell.gtLongPkey());
		if (ctl != null)
			return ctl;
		ctl = GsPriceCtl.chkUniqueObj(false, cell.gtOrg().gtLongPkey());
		if (ctl == null)
			throw LOG.err("noCtl", "请先配置机构的定价控制!");
		return ctl;
	}

	public static class Ins extends IduIns<Ins, GsPriceCtl> {
		public Integer _org;
		public Integer _cell;

		@Override
		public void before() {
			super.before();
			if (_cell != null)
				getB().setTbObj(BeanBase.get(SysCell.class, _cell).gtLongPkey());
			if (_org != null)
				getB().setTbObj(BeanBase.get(SysOrg.class, _org).gtLongPkey());
			if (GsPriceCtl.chkUniqueObj(false, getB().getTbObj()) != null)
				throw LOG.err("onlyOne", "定价控制已经存在！");
		}

	}

	public static class Upd extends IduUpd<Upd, GsPriceCtl> {

		public void before() {
			super.before();
			GsPriceCtl dbBean = load(getB().getPkey());
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), GsPriceCtl.T.PKEY, GsPriceCtl.T.TB_OBJ);
			setB(dbBean);
		}

	}

	public static class Del extends IduDel<Del, GsPriceCtl> {

	}

}
