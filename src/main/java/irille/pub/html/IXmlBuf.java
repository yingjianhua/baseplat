package irille.pub.html;

import irille.pub.html.XmlNode.TabStruct;


/**
 *  静态Html代码产生器的接口
 * @author whx
 *
 * @param <T>
 */
public interface IXmlBuf<T extends IXmlBuf> {
		public T appendView(ViewBase view, TabStruct tab);

		public T appendVirtualNode(NodeVirtual dataNode);

		public T appendStr(String str);
		
		public String toString();
		
		/**
		 * 是否toString方法调用的直接Html代码输出，即没有与数据节点的代码合并
		 * @return
		 */
		public boolean isToStringOut();
}
