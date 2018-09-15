package irille.gl.gs;

import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.Str;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;
import irille.pub.svr.Env;

import java.util.List;
import java.util.Vector;

public class GsGoodsKindDAO {
	public static final Log LOG = new Log(GsGoodsKindDAO.class);

	/**
	 * 获取货物类型中所有科目别名
	 * @return
	 */
	public static List<String> getSubjects() {
		String sql = Idu.sqlString("select DISTINCT({1}) from {0} where {1} not like ", GsGoodsKind.class,
		    GsGoodsKind.T.SUBJECT_ALIAS) + "'@%'";
		List<Object[]> sl = BeanBase.list(sql);
		List<String> list = new Vector();
		for (Object[] objs : sl)
			list.add(objs[0].toString());
		return list;
	}

	public static class Ins extends IduIns<Ins, GsGoodsKind> {
		public void before() {
			super.before();
			initFldValue(getB());
			if (getB().getParent() != null && getB().getCode().startsWith(getB().gtParent().getCode()) == false)
				throw LOG.err("codeErr", "货物分类代码[{0}]，必须以上级分类的代码开头!", getB().getCode());
			if (GsGoodsKind.chkUniqueCode(false, getB().getCode()) != null)
				throw LOG.err("GoodsKind", "货物分类代码[{0}]不能重复!", getB().getCode());
		}
	}

	public static class Upd extends IduUpd<Upd, GsGoodsKind> {
		public void before() {
			super.before();
			GsGoodsKind kind = load(getB().getPkey());
			PropertyUtils.copyPropertiesWithout(kind, getB(), GsGoodsKind.T.PKEY, GsGoodsKind.T.CODE, GsGoodsKind.T.PARENT,
			    GsGoodsKind.T.UPDATEBY, GsGoodsKind.T.UPDATED_TIME);
			initFldValue(kind);
			setB(kind);
		}
	}

	public static class Del extends IduDel<Del, GsGoodsKind> {

		@Override
		public void before() {
			super.before();
			if (Idu.getLinesCount(GsGoods.T.KIND.getFld(), getB().getPkey()) > 0)
				throw LOG.err("errLines", "货物类别[{0}]已存在货物，不能删除", getB().getName());
		}

	}

	public static final void initFldValue(GsGoodsKind bean) {
		if (bean.getParent() == null) {
			if (Str.isEmpty(bean.getSubjectAlias()))
				throw LOG.err("err", "因为没有上级类型,所以必须填写存货科目别名");
		} else {
			GsGoodsKind parent = bean.gtParent();
			if (Str.isEmpty(bean.getSubjectAlias()))
				bean.setSubjectAlias(("@" + parent.getSubjectAlias()).replace("@@", "@"));
		}
		bean.setUpdateby(Idu.getUser().getPkey());
		bean.setUpdatedTime(Env.INST.getWorkDate());
	}

}
