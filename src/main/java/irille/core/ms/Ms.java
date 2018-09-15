/**
 * 
 */
package irille.core.ms;

import irille.core.sys.SysModule;
import irille.pub.Log;
import irille.pub.bean.PackageBase;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.tb.TbBase;

/**
 * 产品与服务信息包
 * @author surface1
 *
 */
public class Ms extends PackageBase {
	private static final Log LOG = new Log(Ms.class);
	public static final Ms INST = new Ms();
	public static TbBase TB = new TbBase<Tb>(Ms.class, "消息模块"); //定义公共的Fld对象用

	private Ms() {
	}

	@Override
	public void initTbMsg() { // 初始化表信息
	}

	@Override
	public void initTranData() {
	}

	@Override
	public SysModule initModule() {
		return iuModule(Ms.TB, null);
	}

	/**
	 * 初始化，在运行期间仅执行一次
	 */
	public void initOnlyOne() { // 初始化方法，在每次启动时执行一次
		super.initOnlyOne();
	}

	public enum T implements IEnumFld {
		;
		private Fld _fld;

		private T(Class clazz, String name, boolean... isnull) {
			_fld = TB.addOutKey(clazz, this, name, isnull);
		}

		private T(IEnumFld fld, boolean... isnull) {
			this(fld, null, isnull);
		}

		private T(IEnumFld fld, String name, boolean... null1) {
			_fld = TB.add(fld, this, name, null1);
		}

		private T(IEnumFld fld, String name, int strLen) {
			_fld = TB.add(fld, this, name, strLen);
		}

		private T(Fld fld) {
			_fld = TB.add(fld);
		}

		public Fld getFld() {
			return _fld;
		}
	}

	public enum ODoType implements IEnumOpt {//@formatter:off
		COO(1,"协作执行"),COM(2,"竞争执行");
		public static final String NAME="处理类型";
		public static final ODoType DEFAULT = COO; // 定义缺省值
		private EnumLine _line;
		private ODoType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	static { //在此可以加一些对FLD进行特殊设定的代码
	//		T.LOGIN.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改 XXX
	}//@formatter:on
}
