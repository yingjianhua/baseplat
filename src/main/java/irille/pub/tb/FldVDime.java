//Created on 2005-9-27
package irille.pub.tb;

import java.sql.ResultSet;
import java.sql.SQLException;

import irille.core.sys.SysOrg;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.view.VFld;

/**
 * Title: 数组字段<br>
 * Description: 该字段会产生若干真实的字段，还会产生GET与SET方法，同时产生一个虚拟的数组字段<br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldVDime<T extends FldVDime> extends FldV<T> {
	private int _numbs[];
	private String _names[];
	private Fld _baseFld;

	public FldVDime( String code, String name, Fld baseFld,
			int numbs[], String... names) {
		super(code, name);
		_baseFld = baseFld;
		_numbs = numbs;
		_names = names;
	}
	
	@Override
	public Fld setTb(TbBase tb) {
		super.setTb(tb);
		for(int i=0;i<_numbs.length;i++) { //在TB对象中自动添加各数组无素的Fld
		  tb.add(tb.crt(_baseFld,getCode()+_numbs[i],_names[i] ).setNull(_baseFld.isNull())).setAutoCreate();	
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew( String code, String name) {
		return (T) copy(new FldVDime( code, name, _baseFld, _numbs, _names));
	}

	@Override
	public Class getJavaType() {
		return _baseFld.getJavaType();
	}
public static void main(String[] args) {
	System.err.println(SysOrg.class.isAssignableFrom(Bean.class));
	System.out.println(Bean.class.isAssignableFrom(SysOrg.class));
}
	/**
	 * 输出get与set的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		StringBuilder buf = new StringBuilder();
		String returnType = Str.getClazzLastCode(getJavaType());
		String firstUpper = getCodeFirstUpper();
		boolean isBean=Bean.class.isAssignableFrom(_baseFld.getJavaType());

		// 输出gt对象方法
		buf.append("  //数组对象: " + returnType + LN);
		buf.append("  public " + returnType + " gt" + firstUpper
				+ "(int i){" + LN);
		buf.append("    switch(i) {" + LN);
		for (int i = 0; i < _numbs.length; i++) {
			buf.append("    case " + _numbs[i] + ":" + LN);
			buf.append("    	return "+(isBean?"gt":"get")+firstUpper+ _numbs[i] + "();" + LN);
		}
		buf.append("  	default: throw LOG.err(\"dimeErr\",\"Dime numb[{0}] invalid.\",i);"+LN+
				"		}" + LN);
		buf.append("  }" + LN);
		//输出St方法
		buf.append("  public void st" + firstUpper + "( int i, " + returnType + " " + getCode() + "){" + LN);
		buf.append("    switch(i) {" + LN);
		for (int i = 0; i < _numbs.length; i++) {
			buf.append("    case " + _numbs[i] + ":" + LN);
			buf.append("    	"+(isBean?"st":"set")+firstUpper+ _numbs[i]+"("+getCode()+ ");" + LN);
			buf.append("    	return;"+ LN);
		}
		buf.append("  	default: throw LOG.err(\"dimeErr\",\"Dime numb[{0}] invalid.\",i);"+LN+
				"		}" + LN);
		buf.append("  }" + LN);
		return buf.toString();
	}

	@Override
	public VFld crtVFld() {
		return null;
	}

	@Override
	public Object tran(String value) {
		return null;
	}
	
	@Override
	public KIND getKind() {
	  return KIND.DIME;
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return null;
	}

}
