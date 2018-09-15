/**
 * 
 */
package irille.pub.priv;

import irille.core.sys.SysCell;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysUser;
import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.Bean;

/**
 * @author whx
 * 
 */
public class Privs<Obj extends Bean> {
	private static final Log LOG = new Log(Privs.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		msg("");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	//角色切换功能，
	//直接上级，直接下属，下属，上级
	/*
	 交易分配到核算单元级，可视
	 功能可以分配给角色：集团级，机构级、核算单元级
	 ？？操作权限可否跨机构？
	 各级只能设各级范围内的权限
	 职位，岗位
	 工作流
	 
	 公共角色
	 功能权限
	 数据权限定义
	 字段权限
	 连接的权限：授权有效日期
	
	
	 
	 
	 */
	//bean --
	
	/**
	 * 用户本人
	 * 
	 * @author whx
	 * 
	 */
	public class PrivUserOwner extends PrivType<SysUser> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.priv.PrivType#isOk(irille.pub.bean.Bean)
		 */
		@Override
		public boolean isOk(SysUser obj) {
			return obj.getPkey().equals(getPrivUserMsg().getUser().getPkey());
		}
	}

	/**
	 * 同部门的人员
	 * 
	 * @author whx
	 * 
	 */
	public class PrivDept extends PrivType<SysDept> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.priv.PrivType#isOk(irille.pub.bean.Bean)
		 */
		@Override
		public boolean isOk(SysDept obj) {
			return obj.getPkey()
					.equals(getPrivUserMsg().getDept().getPkey());
		}
	}
	/**
	 * 同核算单元的人员
	 * 
	 * @author whx
	 * 
	 */
	public class PrivDeptCell extends PrivType<SysDept> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.priv.PrivType#isOk(irille.pub.bean.Bean)
		 */
		@Override
		public boolean isOk(SysDept obj) {
			return obj.gtCell().getPkey()
					.equals(getPrivUserMsg().getDept().gtCell().getPkey());
		}
	}
	/**
	 * 同机构的人员
	 * 
	 * @author whx
	 * 
	 */
	public class PrivDeptOrg extends PrivType<SysDept> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.priv.PrivType#isOk(irille.pub.bean.Bean)
		 */
		@Override
		public boolean isOk(SysDept obj) {
			return obj.gtOrg().getPkey()
					.equals(getPrivUserMsg().getDept().gtOrg().getPkey());
		}
	}

	public static interface PrivUserMsgInf {
		public SysUser getUser();

		public SysDept getDept();

		public SysCell getCell();

		public SysOrg getOrg();
	}

}
