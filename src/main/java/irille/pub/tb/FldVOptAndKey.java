//Created on 2005-9-27
package irille.pub.tb;

import java.sql.ResultSet;
import java.sql.SQLException;

import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.view.VFld;

/**
 * Title: 外表与主键字段<br>
 * Description: 该字段会产生后缀为Table与Pkey的真实的字段，还会产生GET与SET方法，同时产生一个虚拟的OBJ方法<br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldVOptAndKey<T extends FldVOptAndKey> extends FldV<T> {
	private IEnumOptObj<Class> _opt;

	public FldVOptAndKey(String code, String name, IEnumOptObj<Class> opt) {
		super(code, name);
		_opt=opt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew(String code, String name) {
		return (T) copy(new FldVOptAndKey(code, name,_opt));
	}

	@Override
	public Fld setTb(TbBase tb) {
		super.setTb(tb);
		  tb.add(tb.crt(_opt)).setAutoCreate();
		  tb.add(new FldIntTbObj(getCode()+"Pkey", getName() + "主键值").setNull())
				.setAutoCreate();
//		  tb.add(tb.crtInt(getCode() + "Pkey", getName() + "主键值"))
//		  .setAutoCreate();
		return this;
	}

	@Override
	public Class getJavaType() {
		return Bean.class;
	}

	/**
	 * 输出get与set的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		StringBuilder buf = new StringBuilder();
		String returnType = "Bean";
		String firstUpper = getCodeFirstUpper();
		String optFirstUpper = Str.tranFirstUpper(_opt.getLine().getCodeEnum());

		// 输出get对象方法
		buf.append("  //根据表类型选项字段及主键字段的值取对象" + LN);
		buf.append("  public " + returnType + " gt" + firstUpper + "(){" + LN);
		buf.append("    IEnumOptObj<Class> opt=(IEnumOptObj)gt" +_opt.getLine().getCodeEnum()+"();"+ LN);
		buf.append("    if(opt.getObj()==null)" + LN);
		buf.append("    	return null;" + LN);
		buf.append("    return get(opt.getObj(),_"+ getCode()+"Pkey);" + LN);
		buf.append("  }" + LN);
		return buf.toString();
	}

	@Override
	public VFld crtVFld() {
		return null;
	}

	@Override
	public Object tran(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return null;
	}
}
