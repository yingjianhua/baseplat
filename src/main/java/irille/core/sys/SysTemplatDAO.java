package irille.core.sys;

import irille.core.sys.Sys.OTemplateType;
import irille.core.sys.SysDeptDAO.Del;
import irille.core.sys.SysDeptDAO.Upd;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanBuf;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.util.List;

public class SysTemplatDAO {
	public static final Log LOG = new Log(SysTemplatDAO.class);

	public static class Ins extends IduIns<Ins, SysTemplat> {
		@Override
		public void before() {
			super.before();
			if (SysTemplat.chkUniqueCodeYear(false, getB().getCode(), getB().getYear(), getB().getType()) != null){
				throw LOG.err("templat","代码[{0}]相同且启用年份[{1}]也相同,建议修改年份!",getB().getCode(),getB().getYear());}
			getB().stEnabled(true);
		}

	}
	public static class Upd extends IduUpd<Upd, SysTemplat> {

		public void before() {
			super.before();
			SysTemplat dbBean = load(getB().getPkey());
			if (!dbBean.getCode().equals(getB().getCode())&&!dbBean.getYear().equals(getB().getYear())) {
				if (SysTemplat.chkUniqueCodeYear(false, getB().getCode(), getB().getYear(), getB().getType()) != null){
					throw LOG.err("templat","代码[{0}]相同且启用年份[{1}]也相同,建议修改年份!",getB().getCode(),getB().getYear());}
			}
			
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), SysTemplat.T.ENABLED);
			setB(dbBean);
			BeanBuf.clear(SysTemplat.class, getB().getPkey());
		}

	}
	public static class Del extends IduDel<Del, SysTemplat> {

		@Override
		public void valid() {
			super.valid();
			haveBeUsed(SysOrg.class, SysOrg.T.TEMPLAT, b.getPkey());
			BeanBuf.clear(SysTemplat.class, getB().getPkey());
		}
	}
	//根据用户和模板类型获取模板，若没有返回null
	public static List<SysTemplat> getTemplatByUserAndType(SysUser user, byte type) {
		String where = SysTemplat.T.TYPE.getFld().getCodeSqlField() + "= ? AND " 
				+ SysTemplat.T.PKEY.getFld().getCodeSqlField() + " IN (SELECT " 
				+ SysTemplatCell.T.TEMPLAT.getFld().getCodeSqlField() + " FROM " 
				+ SysTemplatCell.TB.getCodeSqlTb() + " WHERE " 
				+ SysTemplatCell.T.CELL.getFld().getCodeSqlField() + " IN (SELECT " 
				+ SysCell.T.PKEY.getFld().getCodeSqlField() + " FROM " 
				+ SysCell.TB.getCodeSqlTb() + " WHERE " 
				+ SysCell.T.ORG.getFld().getCodeSqlField() + " = ?))";
		List<SysTemplat> list = BeanBase.list(SysTemplat.class, where, false, type, user.gtOrg().getPkey());
		return list.size() > 0 ? list : null;
	}
	
	public static List<SysTemplat> getTemplatByUserAndType(SysUser user, OTemplateType type) {
		return getTemplatByUserAndType(user, type.getLine().getKey());
	}

}
