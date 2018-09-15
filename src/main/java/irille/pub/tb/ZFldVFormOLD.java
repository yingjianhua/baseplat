//Created on 2005-9-27

//!!!!!! 此对象用FldVTbAndKey来代替!!!
package irille.pub.tb;

import java.sql.ResultSet;
import java.sql.SQLException;

import irille.pub.Str;
import irille.pub.bean.BeanForm;
import irille.pub.view.VFld;

/**
 * Title: 组合的单据虚拟字段<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class ZFldVFormOLD<T extends ZFldVFormOLD> extends FldV<T> {
	private Fld _tbIdFld;  //表代码的字段
	private Fld _tbPkeyFld;  //表主键的字段

	public ZFldVFormOLD(String code, String name,Fld tbIdFld,Fld tbPkeyFld) {
		super( code, name);
		_tbIdFld=tbIdFld;
		_tbPkeyFld=tbPkeyFld;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		return (T)copy( new ZFldVFormOLD(code, name,_tbIdFld,_tbPkeyFld ));
	}
	
	@Override
	public Class getJavaType() {
		return BeanForm.class;
	}

	/**
	 * 输出get与set的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		StringBuilder buf = new StringBuilder();
		String returnType = Str.getClazzLastCode(getJavaType());
		String firstUpper = getCodeFirstUpper();

		// 输出get对象方法,在方法名后加"OBJ"
		buf.append("  //根据表字段【"+_tbIdFld.getCode()+"】及主键字段【"+_tbPkeyFld.getCode()+"】的值取对象"+ LN);
		buf.append("  public " + returnType + " get" + firstUpper + "OBJ(){"
		    + LN);
		buf.append("    return ("+returnType+")get(get"
				+_tbIdFld.getCodeFirstUpper()+"(),get"+_tbPkeyFld.getCodeFirstUpper()+"());" + LN);
		buf.append("  }" + LN);
		buf.append("  public void set" + firstUpper + "OBJ("+returnType+" "+getCode()+"){"
		    + LN);
		buf.append("    set"+_tbPkeyFld.getCodeFirstUpper()+"("+getCode()+".getPkey());"+LN);
		buf.append("    set"+_tbIdFld.getCodeFirstUpper()+"("+getCode()+".getID());"+LN);
		buf.append("  }" + LN); 
	return buf.toString();
	}
	
//	public Bean getFormNumbOBJ() {
//		return (Bean) get(getFormId(), getFormPkey());
//	}
//
//	public void setFormNumbOBJ(BeanMain formNumb) {
//		setFormPkey(formNumb.getPkey().toString());
//		setFormId(formNumb.getID());
//	}
	
	
	@Override
  public VFld crtVFld() {
	  return null;
  }

	public Fld getTbIdFld() {
  	return _tbIdFld;
  }

	public Fld getTbPkeyFld() {
  	return _tbPkeyFld;
  }

	@Override
  public Object tran(String value) {
	  return null;  //XXX
  }

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return null;
	}
}
