package irille.gl.gl;

import irille.core.sys.SysOrg;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.util.List;
import java.util.Set;

public class GlSubjectDAO {
	public static final Log LOG = new Log(GlSubjectDAO.class);

	public final static String WHERE_CHILD= Idu.sqlString("{0} = ?", GlSubject.T.SUBJECT_UP);

	/**
	 * 取所有科目与下级科目(所有可开户的科目)
	 * @param set
	 * @param subject
	 * @return
	 */
	public static Set getAccount(Set set, GlSubject subject) {
		if (subject.gtTotalFlag() == false) {
			set.add(subject);
			return set;
		}
		List<GlSubject> list = BeanBase.list(GlSubject.class, WHERE_CHILD, false, subject.getPkey());
		for (GlSubject account : list)
			getAccount(set, account);
		return set;
	}

	public static class Ins extends IduIns<Ins, GlSubject> {
		@Override
		public void before() {
			super.before();
			if (getB().getSubjectUp() != null)
				if(!getB().getCode().startsWith(getB().gtSubjectUp().getCode()))
					throw LOG.err("codeError","科目代码[{0}]需要以上级科目代码[{1}]开头",getB().getCode(),getB().gtSubjectUp().getCode());
			if(GlSubject.chkUniqueTempCode(false, getB().getTemplat(), getB().getCode())!=null)
				throw LOG.err("notFound", "在{0}下已存在科目号为[{1}]的科目字典,不可重复新增!", getB().gtTemplat().getName(), getB().getCode());
		}
	}

	public static class Upd extends IduUpd<Upd, GlSubject> {

		public void before() {
			super.before();
			GlSubject dbBean = load(getB().getPkey());
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), GlSubject.T.PKEY, GlSubject.T.CODE, GlSubject.T.TEMPLAT,
			    GlSubject.T.SUBJECT_UP);
			setB(dbBean);
		}

	}
	public static class Del extends IduDel<Del, GlSubject> {

		@Override
		public void valid() {
			super.valid();
			haveBeUsed(GlSubject.class, GlSubject.T.SUBJECT_UP, b.getPkey());
			haveBeUsed(GlJournal.class, GlJournal.T.SUBJECT, b.getPkey());
			haveBeUsed(GlSubjectMap.class, GlSubjectMap.T.SUBJECT, b.getPkey());
		}
	}

}
