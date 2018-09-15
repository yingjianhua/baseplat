package irille.action.dataimport.util.variable;

import java.math.BigDecimal;

public class BooleanVariable {
	public static final int TRUE=1;
	public static final int FALSE=0;
	public static final String TRUE_STRING="true";
	public static final String FALSE_STRING="false";
	
	public static boolean getName(int value){
		if(TRUE==value){
			return true;
		}else if(FALSE==value){
			return false;
		}
		return false;
	}
	
	public static boolean getName(Object o){
		if(TRUE_STRING.equals(o)){
			return true;
		}else if(FALSE_STRING.equals(o)){
			return false;
		}
		if(o instanceof String){
			int value=Integer.parseInt((String)o);
			if(value==TRUE){
				return true;
			}else if(value==FALSE){
				return false;
			}
		}else if(o instanceof Integer){
			int value=(Integer)o;
			if(value==TRUE){
				return true;
			}else if(value==FALSE){
				return false;
			}
		}else if( o instanceof BigDecimal){
			BigDecimal b=(BigDecimal)o;
			if(b.intValue()==TRUE){
				return true;
			}else if(b.intValue()==FALSE){
				return false;
			}
		}
		return false;
	}
	
	public static int getValue(boolean name){
		if(getName(TRUE)==name){
			return TRUE;
		}else if(getName(FALSE)==name){
			return FALSE;
		}
		return 0;
	}
}
