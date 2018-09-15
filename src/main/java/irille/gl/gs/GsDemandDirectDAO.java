package irille.gl.gs;

import irille.core.sys.Sys;
import irille.core.sys.SysCellDAO;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanForm;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;
import irille.pub.inf.IDirect;
import irille.pub.svr.Env;

public class GsDemandDirectDAO {
	public static final Log LOG = new Log(GsDemandDirectDAO.class);

	/**
	 * 产生直销需求
	 * @param form 来源单据
	 */
	public static void insertByForm(BeanForm form) {
		GsDemandDirect mode = new GsDemandDirect();
		mode.setOrigForm(form.gtLongPkey());
		mode.setOrigFormNum(form.getCode());
		mode.setPoForm(null);
		mode.setPoFormNum(null);
		mode.setStatus(Sys.OBillStatus.INIT.getLine().getKey());
		mode.setOrg(form.getOrg());
		mode.setCell(SysCellDAO.getCellByDept(form.getDept()).getPkey());
		mode.setCreatedBy(form.getCreatedBy());
		mode.setCreatedTime(Env.INST.getWorkDate());
		Ins act = new Ins();
		act.setB(mode);
		act.commit();
	}

	// 根据源单据删除直销需求
	public static void deleteByForm(BeanForm form) {
		GsDemandDirect mode = GsDemandDirect.chkUniqueOrig(true, form.gtLongPkey());
		if (mode == null)
			throw LOG.err("noDemandDirect", "找不到产生的直销需求!");
		if (mode.gtStatus() != Sys.OBillStatus.INIT)
			throw LOG.err("errStatus", "产生的直销需求已被处理!");
		Del act = new Del();
		act.setB(mode);
		act.commit();
	}

	// 根据源单据处理直销需求
	public static void doByForm(BeanForm origForm, BeanForm poFrom) {
		GsDemandDirect mode = GsDemandDirect.chkUniqueOrig(true, origForm.gtLongPkey());
		if (mode == null)
			throw LOG.err("noDemandDirect", "找不到产生的直销需求!");
		if (mode.gtStatus() != Sys.OBillStatus.INIT)
			throw LOG.err("errStatus", "产生的直销需求已被处理!");
		mode.setPoForm(poFrom.gtLongPkey());
		mode.setPoFormNum(poFrom.getCode());
		mode.stStatus(Sys.OBillStatus.DONE);
		Upd act = new Upd();
		act.setB(mode);
		act.commit();
		try {
			Class dc = Class.forName(mode.gtOrigForm().getClass().getName() + "DAO");
			IDirect inf = (IDirect) dc.newInstance();
			inf.directOk((Bean) mode.gtOrigForm());
		} catch (Exception e) {
			throw LOG.err(e, "directCancel", "直销处理出错");
		}

	}

	// 根据源单据取消处理直销需求
	public static void clearByForm(BeanForm origForm) {
		GsDemandDirect mode = GsDemandDirect.chkUniqueOrig(true, origForm.gtLongPkey());
		if (mode == null)
			throw LOG.err("noDemandDirect", "找不到产生的直销需求!");
		if (mode.gtStatus() != Sys.OBillStatus.DONE)
			throw LOG.err("errStatus", "产生的直销需求状态不是已完成!");
		mode.setPoForm(null);
		mode.setPoFormNum(null);
		mode.stStatus(Sys.OBillStatus.INIT);
		Upd act = new Upd();
		act.setB(mode);
		act.commit();
		try {
			Class dc = Class.forName(mode.gtOrigForm().getClass().getName() + "DAO");
			IDirect inf = (IDirect) dc.newInstance();
			inf.directCancel((Bean) mode.gtOrigForm());
		} catch (Exception e) {
			throw LOG.err(e, "directCancel", "直销取消处理出错");
		}
	}

	public static class Ins extends IduIns<Ins, GsDemandDirect> {

	}

	public static class Upd extends IduUpd<Upd, GsDemandDirect> {

	}

	public static class Del extends IduDel<Del, GsDemandDirect> {

	}

}
