package irille.pub.html;

import irille.pub.bean.Bean;

/**
 * Html 文本输出的缓冲类, 将不是DataNode类的节点转化为静态文本，DataNode的节点在输出前填入数据即可，以提高效率
 * 
 * @author whx
 * 
 */
public class ViewDynamic<T extends ViewDynamic> extends ViewBase<T> {
	private StaticCodeBuf _staticCodeBuf = null;

	public ViewDynamic(String key) {
		super(key);
	}

	@Override
	public void outHtmlCode(StringBuilder buf, Xhtml html, Bean bean, ViewBase view,
	    TabStruct tab) {
		createXml(view, tab);
		// 输出所有节点的代码到BUF中
		getStaticCodeBuf().outHtmlCode(buf, html, bean, null, tab);
	};

	@Override
	public void createXml(IXmlBuf buf, TabStruct tab) {
		if (_staticCodeBuf == null) // 如果是第一次，则添加到上级节点中
			buf.appendView(this,tab);
		_staticCodeBuf = new StaticCodeBuf();
		clearChildNodesAndAttribute();
		initNodes();
		super.createXml(this, tab);
	}

	// 初始化数据节点, 每次显示时都调用一次本方法
	public void initNodes() {

	}

	@Override
	protected StaticCodeBuf getStaticCodeBuf() {
		return _staticCodeBuf;
	}

	private static final Xhtml tmpXhtml=new Xhtml("临时文件");
	@Override
  public Xhtml getLinkCssXhtml() {
	  return tmpXhtml;
  }
}
