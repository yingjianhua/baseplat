package irille.gl.gl;

import irille.core.sys.ConfPackage;
import irille.core.sys.Sys;
import irille.core.sys.SysTemplat;
import irille.gl.gs.GsGoodsKindDAO;
import irille.pub.ClassTools;
import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.bean.PackageBase;
import irille.pub.bean.PackageBase.ISubjectAlias;
import irille.pub.idu.Idu;
import irille.pub.idu.IduUpd;
import irille.pub.svr.DbPool;
import irille.pub.svr.Env;

import java.util.List;

public class GlSubjectMapDAO {
	private static final Log LOG = new Log(GlSubjectMapDAO.class);
	private static List<GlSubjectMap> _listAll = null;

	public static GlSubjectMap getByAlias(SysTemplat tmp, String code) {
		if (code.startsWith("@"))
			code = code.substring(1);
		return BeanBase.chk(GlSubjectMap.class, tmp.getCode() + "-" + code);
	}

	public static GlSubjectMap getByAlias(SysTemplat tmp, String code, String target) {
		if (code.startsWith("@"))
			code = code.substring(1);
		return BeanBase.chk(GlSubjectMap.class, tmp.getCode() + "-" + code + "-" + target);
	}

	public static GlSubjectMap getByAlias(String code) {
		return getByAlias(Idu.getOrg().gtTemplat(), code);
	}

	/**
	 * 单账户科目的账号=“核算代码-科号”，其它的为“核算代码-科目-代码”
	 */
	public static String toCode(GlSubjectMap map) {
		if (map.getTemplat() == null)
			return null;
		String code = map.gtTemplat().getCode();
		if (map.getAliasSource() != null)
			code += "-" + map.getAliasSource();
		if (map.getAliasTarget() != null)
			code += "-" + map.getAliasTarget();
		return code;
	}

	/**
	 * 刷新所有财务模板对应的科目别名
	 */
	public synchronized static void refreshBase() {
		if (Env.INST.getDB().hasTable(GlSubjectMap.class) == false)
			return;
		_listAll = BeanBase.list(GlSubjectMap.class, "1=1", false);
		String where = Idu.sqlString("{0}={1} and {2}={3}", SysTemplat.T.ENABLED, Sys.OEnabled.TRUE,
				SysTemplat.T.TYPE, Sys.OTemplateType.GL);
		List<SysTemplat> list = BeanBase.list(SysTemplat.class, where, false);
		List<String> subjects = GsGoodsKindDAO.getSubjects();
		for (Class clazz : ConfPackage.INST.getPacks().keySet()) {
			PackageBase pack = (PackageBase) ClassTools.getStaticProerty(clazz.getName(), "INST");
			for (SysTemplat line : list) {
				insAlias(line, pack.getSubjectAliases()); //刷新基础科目别名
				insAlias(line, pack.getSubjectAliasesCst(), subjects); //刷新货物-成本核算--目标别名
			}
		}
		//刷新货物类型科目别名
		for (SysTemplat line : list)
			for (String kind : subjects)
				insGoodsKind(line, kind, "库存商品");
		clearAll();
	}

	/**
	 * 清除多余的别名
	 */
	public static void clearAll() {
		for (GlSubjectMap line : _listAll)
			line.del();
		_listAll.clear();
	}

	private static void insGoodsKind(SysTemplat tmp, String source, String name) {
		GlSubjectMap map = GlSubjectMapDAO.getByAlias(tmp, source);
		if (map != null) {
			map.setName(name);
			map.upd();
		} else {
			map = new GlSubjectMap();
			map.stTemplat(tmp);
			map.setAliasSource(source);
			map.setName(name);
			map.setPkey(toCode(map));
			map.ins();
			System.err.println("新增科目别名：" + map.getPkey() + "/t" + map.getName());
		}
		_listAll.remove(map);
	}

	private static void insAlias(SysTemplat tmp, ISubjectAlias[] alias) {
		if (alias == null || alias.length == 0 || tmp.gtType() != Sys.OTemplateType.GL)
			return;
		for (ISubjectAlias line : alias) {
			GlSubjectMap map = GlSubjectMapDAO.getByAlias(tmp, line.getCode());
			if (map != null) {
				map.setName(line.getName());
				map.upd();
			} else {
				map = new GlSubjectMap();
				map.stTemplat(tmp);
				map.setAliasSource(line.getCode());
				map.setName(line.getName());
				map.setPkey(toCode(map));
				map.ins();
				System.err.println("新增科目别名：" + map.getPkey() + "/t" + map.getName());
			}
			_listAll.remove(map);
		}
	}

	private static void insAlias(SysTemplat tmp, ISubjectAlias[] alias, List<String> subjects) {
		if (alias == null || alias.length == 0 || tmp.gtType() != Sys.OTemplateType.GL || subjects.size() == 0)
			return;
		for (ISubjectAlias line : alias) {
			for (String subject : subjects) {
				GlSubjectMap map = GlSubjectMapDAO.getByAlias(tmp, subject, line.getCode());
				if (map != null) {
					map.setName(line.getName());
					map.upd();
				} else {
					map = new GlSubjectMap();
					map.stTemplat(tmp);
					map.setAliasSource(subject);
					map.setAliasTarget(line.getCode());
					map.setName(line.getName());
					map.setPkey(toCode(map));
					map.ins();
					System.err.println("新增科目别名：" + map.getPkey() + "\t" + map.getName());
				}
				_listAll.remove(map);
			}
		}
	}

	public static class Upd extends IduUpd<Upd, GlSubjectMap> {

		public void before() {
			super.before();
			GlSubjectMap dbBean = load(getB().getPkey());
			dbBean.setName(getB().getName());
			dbBean.setSubject(getB().getSubject());
			setB(dbBean);
		}

	}

}
