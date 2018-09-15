package irille.pub.html;

import irille.pub.Log;
import irille.pub.ext.Ext;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.Exts.ExtBase;
import irille.pub.svr.Env;

import java.util.Vector;

/**
 * 表达式或变量（不包括JAVA基本类型）, 输出格式为：“[aaa,'bbb',this]”
 * 
 * @author whx
 * 
 */
public class ExtDime<T extends ExtDime> extends ExtBase {
	private static final Log LOG = new Log(ExtDime.class);
	private Vector _list = new Vector();
	private boolean _tabs = false, _close=true;
	private String[] _closeStr=new String[]{"[","]"};

	/**
	 * 构造方法
	 * @param exps 数据元素列表
	 */
	public ExtDime(Object... exps) {
		add(exps);
	}
	
	/**
	 * 置缩进标志，为false则不缩进
	 * @param tabs
	 * @return
	 */
	public ExtDime setTabs(boolean tabs) {
		_tabs = tabs;
		return this;
	}

	/**
	 * 取缩进标志
	 * @return
	 */
	public boolean isTabs() {
		return _tabs;
	}

	/**
	 * 增加数组元素
	 * @param exps 要增加的元素列表
	 * @return
	 */
	public T add(Object... exps) {
		for(Object obj:exps)
			_list.add(obj);
		return (T)this;
	}

	/**
	 * 增加IExtOut对象到数组中
	 * @param exts
	 * @return
	 */
	public T add(IExtOut exts) {
		_list.add(exts);
		return (T)this;
	}
	

	/**
	 * 新增List对象
	 * @param parent 父对象节点
	 * @return
	 */
	public ExtList AddList(ExtList parent) {
		ExtList list=new ExtList(parent);
		_list.add(list);
		return list;
	}
	
	/**
	 * 新增List对象
	 * @return 新增的List对象
	 */
	public ExtList AddList() {
		return AddList(null);
	}
	
	/**
	 * 置表达式，在输出时两端不带“‘”
	 * @param value
	 * @return
	 */
	public T addExp(String value) {
		_list.add(new ExtExp(value));
		return (T)this;
	}

	/* (non-Javadoc)
	 * @see irille.pub.ext.Ext.IExtOut#out(int, java.lang.StringBuilder)
	 */
	@Override
	public void out(int tabs, StringBuilder buf) {
		tabs++;
		buf.append(outBefore());
		String s = "";
		String separator = outSeparator() + (isTabs() ? Env.LN + T[tabs] : "");
		for (Object ext : _list) {
			buf.append(s);
			s = separator;
			Ext.outObject(ext, tabs, buf);
		}
		buf.append(outAfter());
	}

	public String outSeparator() {
		return ",";
	}

	public final String outBefore() {
		return _closeStr[0];
	}

	public final String outAfter() {
		return _closeStr[1];
	}

	/**
	 * @return the close
	 */
	public boolean isClose() {
		return _close;
	}

	/**
	 * @param close the close to set
	 */
	public T setClose(boolean close) {
		if(!close)
			_closeStr=new String[]{"",""};
		_close = close;
		return (T)this;
	}
	
	public Vector getNodes() {
		return _list;
	}

	/**
	 * @return the closeStr
	 */
	public String[] getCloseStr() {
		return _closeStr;
	}

	/**
	 * @param closeStr the closeStr to set
	 */
	public void setCloseStr(String closeStrBefore,String closeStrAfter) {
		_closeStr = new String[]{closeStrBefore,closeStrAfter};
	}
	
	public void setCloseStrToParas(){
		setCloseStr("(", ")");
	}
}
