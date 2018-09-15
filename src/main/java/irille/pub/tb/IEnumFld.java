/**
 * 
 */
package irille.pub.tb;

/**
 * @author surface1
 *
 */
public interface IEnumFld {
	public Fld getFld();
	public String name();
	
	public static Fld crtFld(IEnumFld self, Class<?> clazz, String name, boolean... isnull) {
		return new FldOutKey(clazz, Tb.getEnumFieldCode(self.name()), name).setNull(isnull.length == 0 ? false : isnull[0]);
	}
	public static Fld crtFld(IEnumFld self, IEnumFld fld, String name, boolean... null1) {
		if (name == null)
			name = fld.getFld().getName();
		return Tb.crt(fld, Tb.getEnumFieldCode(self.name()), name).setNull(null1.length == 0 ? fld.getFld().isNull() : null1[0]);
	}
	
	public static Fld crtFld(IEnumFld self, IEnumFld fld, boolean... isnull) {
		return crtFld(fld, null, isnull);
	}
	
	public static Fld crtFld(IEnumFld self, IEnumFld fld, String name, int strLen) {
		Fld f = Tb.crt(fld, Tb.getEnumFieldCode(self.name()), name);
		((FldStr) f).setLen(strLen);
		return f;
	}
	
	public static Fld crtFld(IEnumFld self, Fld<?> fld) {
		return fld.setCode(Tb.getEnumFieldCode(self.name()));
	}

}
