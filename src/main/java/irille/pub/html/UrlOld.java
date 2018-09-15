package irille.pub.html;


/**
 * 所有与URL有关的属性都通过URL类的方法产生，以规范操作，确保代码可靠！！！ Url类，用于产生URL的字符串
 * 
 * @author whx
 * 
 */
public class UrlOld {
	private String _url;

	public UrlOld(String url) {
		_url = tranUrl(url);
	}

	// public Url(Node fmt) {
	// _url = fmt.getAnchorHref();
	// }
	public AttrBase createUrl() {
		return new AttrCust("url", _url);
	}

	public AttrBase create(String attributeName) {
		return new AttrCust(attributeName, _url);
	}

	public AttrBase createAction() {
		return new AttrCust("action", _url);
	}

	// 背景图片
	public AttrBase createBackgroudImage() {
		return new AttrCust("background-image", _url);
	}

	// list-style-image设置列表前的图片
	public AttrBase createListStyleImage() {
		return new AttrCust("list-style-image", "url(" + _url + ")");
	}

	// 引用
	public AttrBase createHref() {
		return new AttrCust("href", _url);
	}

	// 引用
	public AttrBase createSrc() {
		return new AttrCust("src", _url);
	}

	public static String tranUrl(String url) {
		return url; // TODO
	}
}
