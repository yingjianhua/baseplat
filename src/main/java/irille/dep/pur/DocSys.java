/**
 * 
 */
package irille.dep.pur;

import irille.pub.doc.DExample;
import irille.pub.doc.DocBase;
import irille.pub.doc.DocClassList;

/**
 * @author whx
 *
 */
public class DocSys extends DocClassList{
	public static DocBase DOC=new DocSys().init();//产生帮助文件的对象实例，在测试代码中会引用

	/* (non-Javadoc)
	 * @see irille.pub.dep.DocBase#initMsg()
	 */
	@Override
	public void initMsg() {
		super.initMsg();
		addPackage(DExample.DOC);
		add(DExample.DOC);
	}
}
