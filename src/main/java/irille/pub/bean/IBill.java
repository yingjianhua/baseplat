/**
 * 
 */
package irille.pub.bean;

import irille.core.sys.SysCell;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OBillStatus;
import irille.pub.gl.ITallyBean;

import java.util.Date;

/**
 * 凭证接口
 * 实现凭证接口的Bean必须继承自BeanBill类，它是有会计分录的单据，可以附件若干张INote的凭条。
 * @author surface1
 * 
 */
public interface IBill extends IForm,IGetCell,ITallyBean {
	public Long getPkey();

	public void setPkey(Long pkey);

	public String getCode();

	public void setCode(String code);

	public Byte getStatus();

	public void setStatus(Byte status);

	public OBillStatus gtStatus();

	public void stStatus(OBillStatus status);

	public Integer getOrg();

	public void setOrg(Integer org);

	public SysOrg gtOrg();

	public void stOrg(SysOrg org);

	public Integer getCell();

	public void setCell(Integer cell);

	public SysCell gtCell();

	public void stCell(SysCell cell);

	public Integer getDept();

	public void setDept(Integer dept);

	public SysDept gtDept();

	public void stDept(SysDept dept);

	public Integer getCreatedBy();

	public void setCreatedBy(Integer createdBy);

	public SysUser gtCreatedBy();

	public void stCreatedBy(SysUser createdBy);

	public Date getCreatedTime();

	public void setCreatedTime(Date createdTime);

	public Integer getApprBy();

	public void setApprBy(Integer apprBy);

	public SysUser gtApprBy();

	public void stApprBy(SysUser apprBy);

	public Date getApprTime();

	public void setApprTime(Date apprTime);

	public Integer getTallyBy();

	public void setTallyBy(Integer tallyBy);

	public SysUser gtTallyBy();

	public void stTallyBy(SysUser tallyBy);

	public Date getTallyTime();

	public void setTallyTime(Date tallyTime);
	 public String getRem();
	  public void setRem(String rem);
}
