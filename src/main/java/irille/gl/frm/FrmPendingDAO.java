package irille.gl.frm;

import irille.core.sys.SysCell;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysOrg;
import irille.core.sys.SysTable;
import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.IForm;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.tb.Tb;

import java.util.List;
import java.util.Vector;

public class FrmPendingDAO {
	public static final Log LOG = new Log(FrmPendingDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		notFound("{0}[{1}]的对应的[{2}]已处理，不能进行当前操作");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	/**
	 * 取待处理记录
	 * @param origForm 源单据对象
	 * @param descTb 目标单据TB
	 * @return
	 */
	public static FrmPending get(IForm origForm, Tb descTb) {
		int descType = descTb.getID();
		Bean ob = (Bean) origForm;
		FrmPending pend = FrmPending.chkUniqueFormDesc(false, ob.gtLongPkey(), descType);
		if (pend == null)
			throw LOG.err(Msgs.notFound, ob.gtTB().getName(), origForm.getCode(), descTb.getName());
		return pend;
	}

	/**
	 * 增加待处理记录
	 * @param origForm 源单据
	 * @param descTbs 目标单据
	 */
	public static void ins(IForm origForm, Tb... descTbs) {
		FrmPending pend;
		Bean ob = (Bean) origForm;
		SysCell cell = SysCellDAO.getCellByDept(origForm.getDept());
		for (Tb linetb : descTbs) {
			pend = new FrmPending();
			pend.setOrigForm(ob.gtLongPkey());
			pend.setOrigFormNum(origForm.getCode());
			pend.setOrg(origForm.getOrg());
			pend.setCell(cell.getPkey());
			pend.setUserSys(origForm.getCreatedBy());
			pend.setUserCrt(origForm.getCreatedBy());
			pend.setDescType(linetb.getID());
			pend.setCreatedTime(Env.INST.getWorkDate());
			pend.ins();
		}
	}

	public static void ins(IForm origForm, Tb descTb, String rem) {
		FrmPending pend;
		Bean ob = (Bean) origForm;
		SysCell cell = SysCellDAO.getCellByDept(origForm.getDept());
		pend = new FrmPending();
		pend.setOrigForm(ob.gtLongPkey());
		pend.setOrigFormNum(origForm.getCode());
		pend.setOrg(origForm.getOrg());
		pend.setCell(cell.getPkey());
		pend.setUserSys(origForm.getCreatedBy());
		pend.setUserCrt(origForm.getCreatedBy());
		pend.setDescType(descTb.getID());
		pend.setCreatedTime(Env.INST.getWorkDate());
		pend.setRem(rem);
		pend.ins();
	}

	/**
	 * 增加待处理记录 - 汇总单据删除时反向操作
	 * @param origForm 源单据
	 * @param descTbs 目标单据
	 */
	public static void insByCst(IForm origForm, Tb descTb, Integer userId) {
		FrmPending pend;
		Bean ob = (Bean) origForm;
		SysCell cell = SysCellDAO.getCellByDept(origForm.getDept());
		pend = new FrmPending();
		pend.setOrigForm(ob.gtLongPkey());
		pend.setOrigFormNum(origForm.getCode());
		pend.setOrg(origForm.getOrg());
		pend.setCell(cell.getPkey());
		pend.setUserSys(userId);
		pend.setUserCrt(origForm.getCreatedBy());
		pend.setDescType(descTb.getID());
		pend.setCreatedTime(Env.INST.getWorkDate());
		pend.ins();
	}

	/**
	 * 删除待处理记录 -- 由源单据反操作时调用
	 * @param origForm 源单据
	 * @param descTbs 目标单据
	 */
	public static void del(IForm origForm, Tb... descTbs) {
		for (Tb linetb : descTbs) {
			get(origForm, linetb).del();
		}
	}

	/**
	 * 删除待处理记录 -- 由目标单据反操作时调用
	 */
	public static void del(List<IForm> lines, Tb descTb) {
		for (IForm line : lines) {
			FrmPending mode = get(line, descTb);
			mode.del();
		}
	}

	public final static String SQL_DESC_ORG = Idu.sqlString("{0} = ? AND {1} = ?", FrmPending.T.ORG,
	    FrmPending.T.DESC_TYPE);

	/**
	 * 取指定目标类型的待处理单据
	 */
	public List<IForm> queryPendingForm(SysOrg org, Tb descTb, boolean lock) {
		List<FrmPending> pends = BeanBase.list(FrmPending.class, SQL_DESC_ORG, lock, org.getPkey(), descTb.getID());
		Vector rtn = new Vector();
		for (FrmPending pend : pends)
			rtn.add(pend.gtOrigForm());
		return rtn;
	}

	public final static String SQL_SRC_DESC_ORG = Idu.sqlString("{0}%" + SysTable.NUM_BASE
	    + " = ? AND {1} = ? AND {2} = ?", FrmPending.T.ORIG_FORM, FrmPending.T.ORG, FrmPending.T.DESC_TYPE);

	/**
	 * 取指定源单据与目标单据类型的待处理单据
	 */
	public List<IForm> queryPendingForm(SysOrg org, Tb src, Tb desc, boolean lock) {
		List<FrmPending> pends = BeanBase.list(FrmPending.class, SQL_SRC_DESC_ORG, false, src.getID(), org.getPkey(),
		    desc.getID());
		Vector rtn = new Vector();
		for (FrmPending pend : pends)
			rtn.add(pend.gtOrigForm());
		return rtn;
	}
}
