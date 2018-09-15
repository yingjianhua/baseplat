//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.Log;
import irille.pub.PubInfs.IMsg;

/**
 * Title: 选项转换相关操作类<br>
 * Description: <br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class OptFldDo {
	private static final Log LOG = new Log(OptFldDo.class);
	
	
	
//	
//	public static class OptTrunEnabled extends OptTurn{
//		
//		public OptTrunEnabled(IEnumFld fld ) {
//			super(fld, OEnabled.TRUE, OEnabled.FALSE);
//		}
//		
//		public IEnumOpt assertEnable(IEnabled bean) {
//			assertEquals(bean.gtEnabled(), getFrom());	
//			return getTo();
//		}
//		
//		public IEnumOpt assertDisable(IEnumOpt val) {
//			assertEquals(val, getTo());	
//			return getFrom();
//		}	
//	}
	
	/**
	 * 选项转换类
	 * @author whx
	 *
	 */
	public static class OptTurn {
		private IEnumFld _fld;
		private IEnumOpt _from;
		private IEnumOpt _to;

		public OptTurn(IEnumFld fld, IEnumOpt from, IEnumOpt to) {
			_fld = fld;
			_from = from;
			_to = to;
		}

		public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		clazz("有关字段[{0}]的两个选项值进行比较时，数据类型[{1}][{2}]不一致，不可比较！"),
		assertErr("字段[{0}]的内容为[{1}]，不可进行本操作！"),
		assertEquals("字段[{0}]的内容为[{1}]，必须为[{2}]才可进行本操作！"),
		;
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

		public boolean isIn(IEnumOpt val, IEnumOpt... optList) {
			for (IEnumOpt opt : optList)
				if (isEquals(val, opt))
					return true;
			return false;
		}

		public void assertIn(IEnumOpt val, IEnumOpt... optList) {
			for (IEnumOpt opt : optList)
				if (isEquals(val, opt))
					return;
			throw LOG.err(Msgs.assertErr, getFldName(), val.getLine().getName());
		}

		public boolean isNotIn(IEnumOpt val, IEnumOpt... optList) {
			for (IEnumOpt opt : optList)
				if (isEquals(val, opt))
					return false;
			return true;
		}

		public void assertNotIn(IEnumOpt val, IEnumOpt... optList) {
			for (IEnumOpt opt : optList)
				if (isEquals(val, opt))
					throw LOG.err(Msgs.assertErr, getFldName(), val.getLine().getName());
		}

		public void assertEquals(IEnumOpt val, IEnumOpt opt) {
			if (isEquals(val, opt))
				return;
			throw LOG.err(Msgs.assertEquals, getFldName(), val.getLine().getName(),
					opt.getLine().getName());
		}

		/**
		 * 比较2个选项的值是否一致，如类型不一致将出错
		 * 
		 * @param val
		 * @param opt
		 * @return
		 */
		public boolean isEquals(IEnumOpt val, IEnumOpt opt) {
			if (!val.getClass().equals(opt.getClass()))
				throw LOG.err(Msgs.clazz, getFldName(), val.getClass().getName(), opt
						.getClass().getName());
			return val == opt;
		}

		public String getFldName() {
			return _fld.getFld().getName();
		}

		/**
		 * 状态转换
		 * 
		 * @param nowVal
		 *          现值，必须为源值
		 * @return 目的值
		 */
		public IEnumOpt turn(IEnumOpt nowVal) {
			assertFrom(nowVal);
			return _to;
		}

		/**
		 * 状态转换取消
		 * 
		 * @param nowVal
		 *          现值，必须为目的值
		 * @return 源值
		 */
		public IEnumOpt turnCancel(IEnumOpt nowVal) {
			assertTo(nowVal);
			return _from;
		}

		/**
		 * 检查是否为源选项值
		 * 
		 * @param val
		 *          要检查的值
		 * @return
		 */
		public boolean isEqualsFrom(IEnumOpt val) {
			return isEquals(val, _from);
		}

		public void assertFrom(IEnumOpt val) {
			assertEquals(val, _from);
		}

		public void assertTo(IEnumOpt val) {
			assertEquals(val, _to);
		}

		/**
		 * 检查是否为目标选项值
		 * 
		 * @param val
		 *          要检查的值
		 * @return
		 */
		public boolean isEqualsTo(IEnumOpt val) {
			return isEquals(val, _to);
		}

		public IEnumFld getEnumFld() {
			return _fld;
		}

		public Fld getFld() {
			return _fld.getFld();
		}

		/**
		 * @return the from
		 */
		public IEnumOpt getFrom() {
			return _from;
		}

		/**
		 * @return the to
		 */
		public IEnumOpt getTo() {
			return _to;
		}
	}
}
