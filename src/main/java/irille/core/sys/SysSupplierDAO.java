package irille.core.sys;

import irille.pss.pur.PurProtApply;
import irille.pss.pur.PurProtGoodsApply;
import irille.pub.JsonTools;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.Str;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;
import irille.pub.svr.ProvDataCtrl;

import org.json.JSONArray;

public class SysSupplierDAO {
	public static final Log LOG = new Log(SysSupplierDAO.class);
	
	public static JSONArray autoComplete(int idx, int count, String code, String diy) {
		StringBuffer sql = new StringBuffer(Idu.sqlString("select pkey,code,name from {0} where {1}={2}", SysSupplier.class,
				SysSupplier.T.ENABLED, Sys.OEnabled.TRUE));
		sql.append(" AND "+ProvDataCtrl.INST.getWhere(Idu.getUser(), SysCustom.class));
		if (Str.isEmpty(code) == false)
			sql.append(" and (code like '%"+code+"%' OR name like '%"+code+"%')");
		if (Str.isEmpty(diy) == false)
			sql.append(" and " + diy);
		sql.append(" order by code");
		return JsonTools.mapToArray(BeanBase.executeQueryMap(BeanBase.getPageSql(sql.toString(), false, idx, count)));
	}


	public static class Ins extends IduIns<Ins, SysSupplier> {
		public SysCom _com = null;

		@Override
		public void before() {
			super.before();
			getB().stEnabled(true);
			if (SysSupplier.chkUniqueCode(false, getB().getCode()) != null)
				throw LOG.err("sameCode", "代码[{0}]的供应商已存在!", getB().getCode());
			getB().setRem(_com.getRem());
		}

		@Override
		public void after() {
			super.after();
			_com.setPkey(getB().gtLongPkey());
			_com.setName(getB().getName());
			_com.setShortName(getB().getShortName());
			SysComDAO.Ins cins = new SysComDAO.Ins();
			cins.setB(_com);
			cins.commit();
		}
	}

	public static class Upd extends IduUpd<Upd, SysSupplier> {
		public SysCom _com = null;

		public void before() {
			super.before();
			SysSupplier dbBean = load(getB().getPkey());
			if (dbBean.getCode().equals(getB().getCode()) == false
			    && SysSupplier.chkUniqueCode(false, getB().getCode()) != null)
				throw LOG.err("sameCode", "代码[{0}]的客户已存在!", getB().getCode());
			SysCom dbCom = BeanBase.load(SysCom.class, getB().gtLongPkey());
			PropertyUtils.copyPropertiesWithout(dbCom, _com, SysCom.T.PKEY);
			dbCom.setName(getB().getName());
			dbCom.setShortName(getB().getShortName());
			dbCom.setRowVersion(dbBean.getRowVersion());
			SysComDAO.Upd cins = new SysComDAO.Upd();
			cins.setB(dbCom);
			cins.commit();
			//
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), SysSupplier.T.PKEY, SysSupplier.T.ENABLED);
			dbBean.setRem(dbCom.getRem());
			setB(dbBean);
		}

	}

	public static class Del extends IduDel<Del, SysSupplier> {

		@Override
		public void valid() {
			super.valid();
			haveBeUsed(PurProtApply.class, PurProtApply.T.SUPPLIER, b.getPkey());
			haveBeUsed(PurProtGoodsApply.class, PurProtApply.T.SUPPLIER, b.getPkey());
			haveBeUsed(SysSupplierOrg.class, SysSupplierOrg.T.SUPPLIER, b.getPkey());
			haveBeUsed(SysPersonLink.class, SysPersonLink.T.TB_OBJ_LONG, b.gtLongPkey());
		}

		@Override
		public void after() {
			super.after();
			SysComDAO.Del cact = new SysComDAO.Del();
			cact.setBKey(getB().gtLongPkey());
			cact.commit();
		}
	}
}
