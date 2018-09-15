/**
 * 
 */
package irille.pub.html;

import irille.pub.Log;
import irille.pub.Str;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.ext.Ext.IExtVars;
import irille.pub.svr.Act;
import irille.pub.tb.IEnumOpt;

import java.util.Vector;

/**
 * 
 * @author whx
 * 
 */
public class Exts implements IExtVars {
	private static final Log LOG = new Log(Exts.class);
	
//	public static final void addAct(ExtList v, String text, Object scope, String iconCls, String expHandler) {
//		v.add(TEXT,text).add(ICON_CLS, iconCls).add(SCOPE,scope).addExp(HANDLER, expHandler);		
//	}
//	public static final void addActWin(ExtList v, String text, Object scope, String iconCls, String expHandler) {
//		v.add(TEXT,text).add(SCOPE,scope).add(ICON_CLS, iconCls).addExp(HANDLER, expHandler);		
//	}
	
	public static final void addButton(ExtList v, String text, Object scope, String iconCls, String expHandler) {
		v.add(XTYPE,"button").add(TEXT,text).add(SCOPE,scope).add(ICON_CLS, iconCls).addExp(HANDLER, expHandler);		
	}
	public static final void addButtonSplit(ExtList v, String text, Object scope, String iconCls, String expHandler) {
		v.add(XTYPE,"splitbutton").add(TEXT,text).add(SCOPE,scope).add(ICON_CLS, iconCls).addExp(HANDLER, expHandler);		
	}

/**
 * 功能数组类，可以添加其它对象，但在查找时仅找类型为"ExtAct.class"的对象, 用于界面功能按钮的属性定制
 * @author whx
 *
 */
	public static class ExtActs extends ExtDime<ExtActs> {

		/**
		 * 增加Act对象
		 * @param file JS文件对象
		 * @param act 功能
		 * @param funCodeFile 代码所在的Java类
		 * @param funCodeParas 取代码时调用的参数
		 * @return ExtAct对象
		 */
		public ExtAct Add(EMBase file, Act act,Class funCodeFile, Object... funCodeParas) {
			ExtAct v = new ExtAct(file,act,funCodeFile,funCodeParas);
			v.add(TEXT, act.getName()).add(ICON_CLS, act.getIcon())
					.add(SCOPE, EXP_THIS)
					.addExp(HANDLER, "this.on" + act.getCodeFirstUpper());
			add(v);
			return v;
		}

		/**
		 * 增加Act对象
		 * @param file JS文件对象
		 * @param act 功能
		 * @param funCodeFile 代码所在的Java类
		 * @param funCodeParas 取代码时调用的参数
		 * @return ExtAct对象
		 */
		public ExtAct AddActWin(EMBase file,Act act,Class funCodeFile, Object... funCodeParas) {
			ExtAct v = new ExtAct(file,act,funCodeFile,funCodeParas);
			v.add(TEXT, act.getName()).add(SCOPE, EXP_THIS)
					.add(ICON_CLS, act.getIcon())
					.addExp(HANDLER, "this.on" + act.getCodeFirstUpper());
			add(v);
			return v;
		}
		/**
		 * 增加列表行的Act对象
		 * @param file JS文件对象
		 * @param act 功能
		 * @param funCodeFile 代码所在的Java类
		 * @param funCodeParas 取代码时调用的参数
		 * @return ExtAct对象
		 */
		public ExtAct AddActRow(EMBase file,Act act,Class funCodeFile, Object... funCodeParas) {
			ExtAct v = new ExtAct(file,act,funCodeFile,funCodeParas);
			v
			.add(ICON_CLS, act.getIcon())
			.add("tooltip", act.getName())
			.add(SCOPE, EXP_THIS)
			.addExp(HANDLER, "this.on" + act.getCodeFirstUpper());
			add(v);
			return v;
		}
	
		/**
		 * 取指定的对象
		 * @param act 功能
		 * @return
		 */
		public ExtAct getAct(IEnumOpt act) {
			return getAct(act.getLine().getCode());
		}

		/**
		 * 取指定的对象
		 * @param code 功能代码
		 * @return
		 */
		public ExtAct getAct(String code) {
			for (Object obj : getNodes()) {
				if (obj instanceof ExtAct && ((ExtAct)obj).getCode().equals(code))
					return (ExtAct) obj;
			}
			throw LOG.err("getObj", "代码为[{0}]的对象没找到！", code);
		}
		
		/**
		 * 取功能数组
		 * @return
		 */
		public Vector<ExtAct> getActs() {
			Vector<ExtAct> acts=new Vector();
			for (Object obj : getNodes()) {
				if (obj instanceof ExtAct)
					acts.add((ExtAct) obj);
			}			
			return acts;
		}
	}

/**
 * 界面功能包装的类，用于对功能按钮产生前台代码的增强操作。
 * @author whx
 *
 * @param <T>
 */
	public static class ExtAct<T extends ExtAct> extends ExtObj<T, Act> {
		private String _funName;
		private String _funCallName;
		private ExtFunDefine _fun;
		/**
		 * 构造方法
		 * @param file JS文件对象
		 * @param act 功能
		 * @param funCodeFile 代码所在的Java类
		 * @param funCodeParas 取代码时调用的参数
		 */
		public ExtAct(EMBase file, Act act,Class funCodeFile, Object... funCodeParas) {
			super(act);
			setFunName(file,act.getFunName(),funCodeFile,funCodeParas);
		}
		
		//by whx
		public ExtAct(Act act, String funContent) {
			super(act);
			_funName = act.getFunName();
			setFunCallName("this." + _funName);
			_fun=new ExtFunDefine(null,_funName, new Object[0]);
			_fun.add(funContent);
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.html.ExtClasses.ExtBase#getCode()
		 */
		@Override
		public String getCode() {
			return getObj().getCode();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.html.ExtClasses.ExtBase#getName()
		 */
		@Override
		public String getName() {
			return getObj().getName();
		}

		/**
		 * 取Act
		 * @return
		 */
		public Act getAct() {
			return getObj();
		}

		/**
		 * 取函数名
		 * @return
		 */
		public String getFunName() {
			return _funName;			
		}

		/**
		 * 置函数名
		 * @param file JS文件对象
		 * @param act 功能
		 * @param funCodeFile 代码所在的Java类
		 * @param funCodeParas 取代码时调用的参数
		 * @return 当前对象
		 */
		public ExtAct setFunName(EMBase file, String funName,Class funCodeFile, Object... funCodeParas) {
			_funName = funName;
			setFunCallName("this."+_funName);
			_fun=new ExtFunDefine(null,_funName, new Object[0]);
			_fun.add(file.loadFunCode(funCodeFile,_funName,funCodeParas));
			return this;
		}

		/**
		 * @return the funCallName
		 */
		public String getFunCallName() {
			return _funCallName;
		}

		/**
		 * @param funCallName the funCallName to set
		 */
		public void setFunCallName(String funCallName) {
			_funCallName = funCallName;
		}

		/**
		 * @return
		 */
		public ExtFunDefine getFun() {
			return _fun;
		}
	}

	abstract static class ExtObj<T extends ExtObj, OBJ extends Object>
			extends ExtList<T> {
		private OBJ _obj;

		public ExtObj(OBJ obj) {
			super();
			_obj = obj;
		}

		/**
		 * @return the obj
		 */
		public OBJ getObj() {
			return _obj;
		}

		public abstract String getCode();

		public abstract String getName();
	}

	public static abstract class ExtBase implements IExtOut {

		/* (non-Javadoc)
		 * @see irille.pub.ext.Ext.IExtOut#toString(int)
		 */
		@Override
		public final String toString(int tabs) {
			StringBuilder buf=new StringBuilder();
			out(tabs,buf);
			return buf.toString();
		}		
	}
}
