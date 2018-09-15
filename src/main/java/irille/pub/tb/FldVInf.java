//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.Str;
import irille.pub.view.VFld;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Title: 数组字段<br>
 * Description: 该字段会产生若干真实的字段，还会产生GET与SET方法，同时产生一个虚拟的数组字段<br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FldVInf<T extends FldVInf> extends FldV<T> {

	public FldVInf() {
		super("inf", "产生当前类的接口类");
	}
	
	@Override
	public Fld setTb(TbBase tb) {
		super.setTb(tb);
		setAutoCreate(); //设置此值，当本TB的Fld补加到其他TB时不会被复制
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T copyNew( String code, String name) {
		return (T) copy(new FldVInf());
	}

	@Override
	public Class getJavaType() {
		return getTb().getClazz();
	}

	/**
	 * 输出get与set的方法源代码
	 */
	@Override
	public String outSrcMethod() {
		Vector<String> lns=new Vector();
		StringBuilder buf = new StringBuilder();
		String code;
		int i,begin,ipublic;
		for(Fld fld: getTb().getFlds()) {
			if(fld instanceof FldVInf) 
				continue;
			//拆分行
			code=fld.outSrcMethod();
		  i=code.indexOf(LN);
		  lns.clear();
		  begin=0;
		  while(i>=0) {
		  	lns.add(code.substring(begin,i));
		  	begin=i+LN.length();
		  	if(begin>=code.length())
		  		break;
		  	i=code.indexOf(LN, begin);
		  }
	  	if(begin<code.length())
		  	lns.add(code.substring(begin));
	  	//取方法的定义代码
	  	for(String ln:lns){
	  		if(ln.indexOf("public")<0 || ln.indexOf("{")<10)
	  			continue;
	  		buf.append("	"+ln.substring(0,ln.indexOf("{"))+";"+LN);
	  	}
		}
		System.err.println("！！！请将以下代码复制到 包名的类中，便于给其它类引用！");
		System.err.println( "	public static interface I"+Str.getClazzLastCode(getTb().getClazz())+"{"+LN
		     +buf.toString()+"	}");
		 return "";
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
	  return KIND.UNION;
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return null;
	}

}
