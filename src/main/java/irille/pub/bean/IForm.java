/**
 * 
 */
package irille.pub.bean;

import irille.core.sys.SysCell;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OBillStatus;

import java.util.Date;

/**
 * @author surface1
 * 
 */
public interface IForm extends ISeq{
public Long gtLongPkey();
	
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
public String getRem();
public void setRem(String rem);
}
