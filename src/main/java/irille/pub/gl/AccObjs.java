/**
 * 
 */
package irille.pub.gl;

import irille.core.sys.SysCell;
import irille.core.sys.SysCustom;
import irille.core.sys.SysDept;
import irille.core.sys.SysEm;
import irille.core.sys.SysOrg;
import irille.core.sys.SysProject;
import irille.core.sys.SysSupplier;
import irille.core.sys.SysUser;
import irille.gl.gl.Gl;
import irille.gl.gl.GlJournalDAO;
import irille.gl.gl.GlSubject;
import irille.pub.Log;
import irille.pub.bean.BeanBill;

/**
 * 记账账户的对象信息
 * @author whx
 *
 */
public class AccObjs {
	private static final Log LOG = new Log(AccObjs.class);
	private SysOrg _org=null;
	private SysDept _dept=null;
	private SysCell _cell=null;
	private SysUser _user=null;
	private SysCustom _custom=null;
	private SysProject _project=null;
	private SysSupplier _supplier=null;
	
	/**
	 * 根据Bill置用户、核算单元、部门与机构
	 * @param bill
	 * @return
	 */
	public AccObjs initBase(BeanBill bill) {
		_org=bill.gtOrg();
		_dept=bill.gtDept();
		_cell=bill.gtCell();
		_user=bill.gtCreatedBy();
		return this;
	}
	/**
	 * 根据用户置核算单元、部门与机构
	 * @param user
	 * @return
	 */
	public AccObjs initBase(SysUser user) {
		_org=user.gtOrg();
		_dept=user.gtDept();
		_cell=_dept.gtCell();
		_user=user;
		return this;
	}

	public AccObjs init() {
		initBase();
		return initOther();
	}
	/**
	 * 初始化基本对象，用户、核算单元、部门与机构
	 * @return
	 */
	public AccObjs initBase() {
		_org=null;
		_dept=null;
		_cell=null;
		_user=null;
		return this;
	}
	
	/**
	 * 初始化扩展对象，客户、项目、供应商
	 * @return
	 */
	public AccObjs initOther() {
		_custom=null;
		_project=null;
		_supplier=null;
		return this;
	}
	
	/**
	 * @return the org
	 */
	public SysOrg getOrg() {
		return _org;
	}
	/**
	 * @param org the org to set
	 */
	public AccObjs setOrg(SysOrg org) {
		_org = org;
		return this;
	}
	/**
	 * @return the dept
	 */
	public SysDept getDept() {
		return _dept;
	}
	/**
	 * @param dept the dept to set
	 */
	public AccObjs setDept(SysDept dept) {
		_dept = dept;
		return this;
	}
	/**
	 * @return the cell
	 */
	public SysCell getCell() {
		return _cell;
	}
	/**
	 * @param cell the cell to set
	 */
	public AccObjs setCell(SysCell cell) {
		_cell = cell;
		return this;
	}
	/**
	 * @return the user
	 */
	public SysUser getUser() {
		return _user;
	}
	/**
	 * @param user the user to set
	 */
	public AccObjs setUser(SysUser user) {
		_user = user;
		return this;
	}
	/**
	 * @return the custom
	 */
	public SysCustom getCustom() {
		return _custom;
	}
	/**
	 * @param custom the custom to set
	 */
	public AccObjs setCustom(SysCustom custom) {
		_custom = custom;
		return this;
	}
	/**
	 * @return the project
	 */
	public SysProject getProject() {
		return _project;
	}
	/**
	 * @param project the project to set
	 */
	public AccObjs setProject(SysProject project) {
		_project = project;
		return this;
	}
	/**
	 * @return the supplier
	 */
	public SysSupplier getSupplier() {
		return _supplier;
	}
	/**
	 * @param supplier the supplier to set
	 */
	public AccObjs setSupplier(SysSupplier supplier) {
		_supplier = supplier;
		return this;
	}
	
	public Long getObjLongPkey(GlSubject subject) {
		if (subject.gtAccType() == Gl.OAccType.ONE)
			return null;
		else if (subject.gtAccType() == Gl.OAccType.ORG)
			return getOrg().gtLongPkey();
		else if (subject.gtAccType() == Gl.OAccType.CELL)
			return getCell().gtLongPkey();
		else if (subject.gtAccType() == Gl.OAccType.DEPT)
			return getDept().gtLongPkey();
		else if (subject.gtAccType() == Gl.OAccType.PROJECT)
			return getProject().gtLongPkey();
		else if (subject.gtAccType() == Gl.OAccType.EM)
			return getProject().gtLongPkey();
		else if (subject.gtAccType() == Gl.OAccType.USER)
			return getUser().gtLongPkey();
		else if (subject.gtAccType() == Gl.OAccType.CUSTOM)
			return getCustom().gtLongPkey();
		else if (subject.gtAccType() == Gl.OAccType.SUPPLIER)
			return getSupplier().gtLongPkey();
		throw LOG.err("科目[{0}]的账户类型未制定", subject.getName());
	}
	
	public Integer getObjPkey(GlSubject subject) {
		if (subject.gtAccType() == Gl.OAccType.ONE)
			return null;
		else if (subject.gtAccType() == Gl.OAccType.ORG)
			return getOrg().getPkey();
		else if (subject.gtAccType() == Gl.OAccType.CELL)
			return getCell().getPkey();
		else if (subject.gtAccType() == Gl.OAccType.DEPT)
			return getDept().getPkey();
		else if (subject.gtAccType() == Gl.OAccType.PROJECT)
			return getProject().getPkey();
		else if (subject.gtAccType() == Gl.OAccType.EM)
			return getProject().getPkey();
		else if (subject.gtAccType() == Gl.OAccType.USER)
			return getUser().getPkey();
		else if (subject.gtAccType() == Gl.OAccType.CUSTOM)
			return getCustom().getPkey();
		else if (subject.gtAccType() == Gl.OAccType.SUPPLIER)
			return getSupplier().getPkey();
		throw LOG.err("科目[{0}]的账户类型未制定", subject.getName());
	}
	
	public String getObjName(GlSubject subject) {
		if (subject.gtAccType() == Gl.OAccType.ONE)
			return null;
		else if (subject.gtAccType() == Gl.OAccType.ORG)
			return getOrg().getName();
		else if (subject.gtAccType() == Gl.OAccType.CELL)
			return getCell().getName();
		else if (subject.gtAccType() == Gl.OAccType.DEPT)
			return getDept().getName();
		else if (subject.gtAccType() == Gl.OAccType.PROJECT)
			return getProject().getName();
		else if (subject.gtAccType() == Gl.OAccType.EM)
			return getProject().getName();
		else if (subject.gtAccType() == Gl.OAccType.USER)
			return getUser().getName();
		else if (subject.gtAccType() == Gl.OAccType.CUSTOM)
			return getCustom().getName();
		else if (subject.gtAccType() == Gl.OAccType.SUPPLIER)
			return getSupplier().getName();
		throw LOG.err("科目[{0}]的账户类型未制定", subject.getName());
	}
}
