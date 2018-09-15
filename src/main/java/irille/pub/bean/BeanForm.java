//Created on 2005-12-29
package irille.pub.bean;

import irille.core.sys.Sys;
import irille.core.sys.SysSeq;
import irille.pub.Str;

/**
 * Title: 表单基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public abstract class BeanForm<T extends BeanForm> extends BeanLong<T> implements IForm {
	/*
	 * (non-Javadoc)
	 * @see irille.pub.bean.ISeq#initSeq(irille.core.sys.SysSeq)
	 */
	@Override
	public void initSeq(SysSeq s) {
		s.setPkey(gtTable().getPkey());
		s.stOrgFlag(true);
		s.stType(Sys.OType.YYMM);
	}

	public String getCodePrintShort() {
		if (Str.isEmpty(getCode()))
			return "";
		return getCode().substring(getCode().indexOf("-") + 1);
	}

	// public static final Tb TB = new Tb(BeanBill.class, "凭证", BeanForm.TB);
	// public static final Fld FLD_ACCOUNT_DATE = TB.crtDate("accountDate",
	// "记账日期").setNull();
	//
	//
	// // whx方法-----------------------------------------------------
	// public OBillStatus getAccPre() {
	// return OBillStatus.INIT;
	// }
	//
	// public OBillStatus getAccAft() {
	// return OBillStatus.VERIFIED;
	// }
	//
	// public List<LINE> getLines() {
	// return null;
	// }
	//
	// //继承基类:class irille.pub.bean.BeanForm
	// public static final Fld FLD_PKEY = TB.get("pkey");
	// public static final Fld FLD_FORM_NUMBER = TB.get("formNumber");
	// public static final Fld FLD_STATUS = TB.get("status");
	// public static final Fld FLD_DEPT = TB.get("dept");
	// public static final Fld FLD_ORG = TB.get("org");
	// public static final Fld FLD_CREATED_BY = TB.get("createdBy");
	// public static final Fld FLD_CREATED_TIME = TB.get("createdTime");
	// public static final Fld FLD_REMARK = TB.get("remark");
	// //TABLE:凭证:BeanBill
	// //实例变量定义--------------------------------------------------------
	// //private Long _pkey; // 编号 LONG
	// //private String _formNumber; // 单据号 STR(40)
	// //private Byte _status; // 状态 <Sys.STATUS> BYTE
	// // 11:初始
	// // 53:复核中
	// // 58:已复核
	// // 63:审核中
	// // 68:已审核
	// // 73:审批中
	// // 78:已审批
	// // 83:可记账
	// // 98:完成
	// // 99:作废
	// //private Integer _dept; // 部门 <表主键:SysDept> INT
	// //private Integer _org; // 机构 <表主键:SysOrg> INT
	// //private Integer _createdBy; // 建档员 <表主键:SysUser> INT
	// //private Date _createdTime; // 建档时间 TIME
	// //private String _remark; // 备注 STR(250)<null>
	// private Date _accountDate; // 记账日期 DATE<null>
	//
	// //方法----------------------------------------------------------------
	// public Date getAccountDate() {
	// return _accountDate;
	// }
	//
	// public void setAccountDate(Date accountDate) {
	// _accountDate = accountDate;
	// }
}
