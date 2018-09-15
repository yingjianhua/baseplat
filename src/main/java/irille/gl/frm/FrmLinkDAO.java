package irille.gl.frm;

import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.IForm;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.util.ArrayList;
import java.util.List;

public class FrmLinkDAO {
	public static final Log LOG = new Log(FrmLinkDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		notFoundLink("主单据[{0}]不存在关联单据，不能进行当前操作"),
		notFoundMain("关联单据[{0}]不存在主单据，不能进行当前操作");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public final static String SQL_MAIN_FORM = Idu.sqlString("{0} = ?", FrmLink.T.MAIN_FORM);
	public final static String SQL_LINK_FORM = Idu.sqlString("{0} = ?", FrmLink.T.LINK_FORM);

	/**
	 * 取关联单据记录
	 * @param mainForm 主单据对象
	 * @return
	 */
	public static List queryLinkForm(IForm mainForm, boolean lock) {
		List list = new ArrayList();
		List<FrmLink> linkForms = BeanBase.list(FrmLink.class, SQL_MAIN_FORM, lock, mainForm.gtLongPkey());
		for (FrmLink link : linkForms)
			list.add(link.gtLinkForm());
		if (list == null || list.isEmpty())
			throw LOG.err(Msgs.notFoundLink, mainForm.getCode());
		return list;
	}

	/**
	 * 取主单据记录
	 * @param linkForm 源单据对象
	 * @return
	 */
	public static List queryMainForm(IForm linkForm, boolean lock) {
		List list = new ArrayList();
		List<FrmLink> mainForms = BeanBase.list(FrmLink.class, SQL_LINK_FORM, lock, linkForm.gtLongPkey());
		for (FrmLink link : mainForms)
			list.add(link.gtLinkForm());
		if (list == null || list.isEmpty())
			throw LOG.err(Msgs.notFoundMain, linkForm.getCode());
		return list;
	}

	/**
	 * 新增关联记录
	 * @param mainForm 主单据对象
	 * @param linkForms 关联单据对象
	 * @return
	 */
	public static void ins(IForm mainForm, List<IForm> linkForms) {
		FrmLink link;
		Ins act = new Ins();
		for (IForm linkForm : linkForms) {
			link = new FrmLink();
			link.stType(Frm.OLinkType.SOURCE_DESC);
			link.setMainForm(mainForm.gtLongPkey());
			link.setMainFormNum(mainForm.getCode());
			link.setLinkForm(linkForm.gtLongPkey());
			link.setLinkFormNum(linkForm.getCode());
			act.setB(link);
			act.commit();
		}
	}

	/**
	 * 删除关联记录
	 * @param mainForm 主单据对象
	 * @return
	 */
	public static void del(IForm mainForm) {
		String sql = Idu.sqlString("delete from {0} where {1}=?", FrmLink.class, FrmLink.T.MAIN_FORM);
		Bean.executeUpdate(sql, mainForm.gtLongPkey());
	}

	public static class Ins extends IduIns<Ins, FrmLink> {

	}

	public static class Upd extends IduUpd<Upd, FrmLink> {

	}

	public static class Del extends IduDel<Del, FrmLink> {

	}
}
