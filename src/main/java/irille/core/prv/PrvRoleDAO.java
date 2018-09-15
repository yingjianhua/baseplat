package irille.core.prv;

import irille.core.prv.Prv.OPrvType;
import irille.core.sys.SysCom;
import irille.core.sys.SysUserRole;
import irille.pub.Cn;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduOther;
import irille.pub.idu.IduUpd;
import irille.pub.svr.DbPool;
import irille.pub.svr.Env;
import irille.pub.svr.ProvCtrl;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class PrvRoleDAO {
	public static final Log LOG = new Log(PrvRoleDAO.class);
	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		admin("系统管理员不能删除");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on
	
	//取包含的所有角色
	public static void getAllRoles(Integer pkey, Vector<Integer> vec) {
		if (vec.contains(pkey))
			return;
		vec.add(pkey);
		String where = Idu.sqlString("{0}=?", PrvRoleLine.T.ROLE);
		List<PrvRoleLine> list = BeanBase.list(PrvRoleLine.class, where, false, pkey);
		for (PrvRoleLine line : list) {
			getAllRoles(line.getChild(), vec);
		}
	}
	
	public static class Ins extends IduIns<Ins, PrvRole> {
		@Override
		public void before() {
			super.before();		
			if (PrvRole.chkUniqueCode(false, getB().getCode()) != null) {
				throw LOG.err("notFound",  "角色代码[{0}]已存在,不可重名!", getB()
						.getCode());
			}
			getB().stEnabled(true);
			getB().stUpdatedBy(getUser());
			getB().setUpdatedTime(Env.getTranBeginTime());
		}
		@Override
		public void after() {
			List<PrvTranGrp> grps = PrvTranGrp.list(PrvTranGrp.class, "", false);
			for(PrvTranGrp grp:grps) {
				PrvRoleTran roleTran = new PrvRoleTran();
				roleTran.stRole(getB());
				roleTran.stGrp(grp);
				roleTran.stType(OPrvType.DEFAULT);
				roleTran.setDay(0);
				roleTran.ins();
			}
		}

	}
	
	public static void main(String[] args) throws SQLException {
		List<PrvTranGrp> grps = PrvTranGrp.list(PrvTranGrp.class, "", false);
		List<PrvRole> roles = PrvRole.list(PrvRole.class, "", false);
		
		for (PrvRole role : roles) {
			for(PrvTranGrp grp:grps) {
				PrvRoleTran roleTran = new PrvRoleTran();
				roleTran.stRole(role);
				roleTran.stGrp(grp);
				roleTran.stType(OPrvType.DEFAULT);
				roleTran.setDay(0);
				roleTran.ins();
			}
		}
		
		DbPool.getInstance().getConn().commit();
		DbPool.getInstance().releaseAll();
		
  }

	public static class Upd extends IduUpd<Upd, PrvRole> {

		public void before() {
			super.before();
			PrvRole dbBean = load(getB().getPkey());
			if (!dbBean.getCode().equals(getB().getCode())) {
				if (PrvRole.chkUniqueCode(false, getB().getCode()) != null) {
					throw LOG.err("notFound",  "角色代码[{0}]已存在,不可重名!", getB()
							.getCode());
				}
			}
			getB().stUpdatedBy(getUser());
			getB().setUpdatedTime(Env.INST.getSystemTime());
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), PrvRole.T.PKEY, PrvRole.T.ENABLED, PrvRole.T.UPDATED_BY,
			    PrvRole.T.UPDATED_TIME);
			setB(dbBean);
		}

	}

	public static class Del extends IduDel<Del, PrvRole> {
		@Override
		public void before() {
		  // TODO Auto-generated method stub
		  super.before();
		  String where = Idu.sqlString("{0}=?", PrvRoleAct.T.ROLE);
		  List<PrvRoleAct> acts = PrvRoleAct.list(PrvRoleAct.class, where, true, getB().getPkey());
		  for(PrvRoleAct line:acts) {
		  	line.del();
		  }
		  where = Idu.sqlString("{0}=? or {1}=?", PrvRoleLine.T.ROLE, PrvRoleLine.T.CHILD);
		  List<PrvRoleLine> lines = PrvRoleLine.list(PrvRoleLine.class, where, true, getB().getPkey(), getB().getPkey());
		  for(PrvRoleLine line:lines) {
		  	line.del();
		  }
		  where = Idu.sqlString("{0}=?", PrvRoleTran.T.ROLE);
		  List<PrvRoleTran> trans = PrvRoleTran.list(PrvRoleTran.class, where, true, getB().getPkey());
		  for(PrvRoleTran line:trans) {
		  	line.del();
		  }
		  where = Idu.sqlString("{0}=?", SysUserRole.T.ROLE);
		  List<SysUserRole> roles = SysUserRole.list(SysUserRole.class, where, true, getB().getPkey());
		  for(SysUserRole line:roles) {
		  	line.del();
		  }
		}
		@Override
		public void valid() {
		  super.valid();
		  if(getB().getCode().equals("admin")) {
		  	throw LOG.err(Msgs.admin);
		  }
		}

		@Override
		public void run() {
			getB().stEnabled(false);
			getB().upd();
			ProvCtrl.getInstance().clearAll();
		}
	}
	
	public static class UpdCtrl extends IduOther<UpdCtrl, PrvRole> {
		public static Cn CN = new Cn("updCtrl", "权限控制");
		private String _lines;
		private String _type;

		@Override
		public void run() {
			PrvRoleActDAO.iud(getB(), getType(), getLines());
			ProvCtrl.getInstance().clearAll();
		}

		public String getLines() {
			return _lines;
		}

		public void setLines(String lines) {
			_lines = lines;
		}

		public String getType() {
			return _type;
		}

		public void setType(String type) {
			_type = type;
		}
	}

}
