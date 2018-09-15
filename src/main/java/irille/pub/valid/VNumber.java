//Created on 2005-10-22
package irille.pub.valid;

import irille.pub.Log;
import irille.pub.tb.Infs.IFld;

/**
 * Title: Byte,Short,Int型数据校验器<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class VNumber {
	
	private static final Log LOG = new Log(VNumber.class);
	public static final VBase PLUS=new VPlus();
	public static final VBase PLUS_OR_ZERO=new VPlusOrZero();
	public static final VBase NEGATIVE=new VNegative();
	public static final VBase NEGATIVE_OR_ZERO= new VNegativeOrZero();
	

	private static class VPlus extends VBase<VPlus> {
		@Override
		public void valid(IFld fld,Object value) {
			if (toDec(value).signum()>0)
				return;
			throw LOG.err("Plus", errString(fld,value));
		}

		
		@Override
		public String help() {
			return "正数";
		}

		@Override
		public VPlus copyNew() {
			return this;
		}

		@Override
    public String getJS() {
	    // TODO Auto-generated method stub
	    return null;
    }
	}

	private static class VPlusOrZero extends VBase<VPlusOrZero> {

		@Override
		public void valid(IFld fld,Object value) {
			if (toDec(value).signum() >= 0)
				return;
			throw LOG.err("PositiveOrZero", errString(fld,value));
		}

		@Override
		public String help() {
			return "正数或零";
		}

		@Override
		public VPlusOrZero copyNew() {
			return this;
		}

		@Override
    public String getJS() {
	    // TODO Auto-generated method stub
	    return null;
    }
	}

	private static class VNegative extends VBase<VNegative> {

		@Override
		public void valid(IFld fld,Object value) {
			if (toDec(value).signum() < 0)
				return;
			throw LOG.err("Negative", errString(fld,value));
		}

		@Override
		public String help() {
			return "负数";
		}

		@Override
		public VNegative copyNew() {
			return this;
		}

		@Override
    public String getJS() {
	    // TODO Auto-generated method stub
	    return null;
    }
	}

	private static class VNegativeOrZero extends VBase<VNegativeOrZero> {

		@Override
		public void valid(IFld fld,Object value) {
			if (toDec(value).signum() <= 0)
				return;
			throw LOG.err("NegativeOrZero", errString(fld,value));
		}

		@Override
		public String help() {
			return "负数或零";
		}

		@Override
		public VNegativeOrZero copyNew() {
			return this;
		}

		@Override
    public String getJS() {
	    // TODO Auto-generated method stub
	    return null;
    }
	}

	/**
	 * 有效值为指定列表
	 * 
	 * @author whx
	 * 
	 */
	public static class VList extends VBase<VList> {
		private Object[] _values;
		private String _listStr;

		public VList( Object... values) {
			_values = values;

			// 将列表转化成字符串，在出错时显示用
			StringBuilder buf = new StringBuilder();
			for (Object v : values) {
				buf.append("," + v);
			}
			_listStr = buf.toString().substring(1);
		}

		@Override
		public void valid(IFld fld,Object value) {
			for (Object v : _values) {
				if (value.equals( v))
					return;
			}
			throw LOG.err("List", errString(fld,value));
		}

		@Override
		public String help() {
			return "其中之一[" + _listStr + "]";
		}

		@Override
		public VList copyNew() {
			return this;
		}

		@Override
    public String getJS() {
	    // TODO Auto-generated method stub
	    return null;
    }
	}

	/**
	 * 有效值为指定范围
	 * 
	 * @author whx
	 * 
	 */
	public static class VScope extends VBase<VScope> {
		private Long _min;
		private Long _max;

		/**
		 * 构造方法
		 * 
			 * @param min
		 *          最小值，如为null表示最小有效值
		 * @param max
		 *          最大值，如为null表示最大有效值
		 */
		public VScope(Long min, Long max) {
			_min = min;
			_max = max;
		}

		@Override
		public void valid(IFld fld,Object value) {
			Long v=Long.parseLong(value.toString());
			if (_min != null && v < _min || _max != null && v > _max)
				throw LOG.err("VIntScope", errString(fld,value));
		}

		@Override
		public String help() {
			if (_min == null)
				return "必须小于或等于" + _max;
			if (_max == null)
				return "必须大于或等于" + _min;
			return "必须在[" + _min + "-" + _max + "]之间";
		}

		@Override
		public VScope copyNew() {
			return this;
		}

		@Override
    public String getJS() {
	    // TODO Auto-generated method stub
	    return null;
    }
	}
}
