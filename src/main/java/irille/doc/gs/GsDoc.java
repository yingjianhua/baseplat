/**
 * 
 */
package irille.doc.gs;

import irille.gl.gs.Gs;
import irille.pub.doc.DocPackage;

/**
 * 包说明文档
 * @author whx
 * 
 */
public class GsDoc extends DocPackage {
	public static String NAME = Gs.TB.getName();   //必须定义，在基类中要取用
	public static GsDoc DOC = (GsDoc)new GsDoc().init();// 产生帮助文件的对象实例，在测试代码中会引用

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.dep.DocBase#initMsg()
	 */
	@Override
	public void initMsg() {
		super.initMsg();
		DOC.p2("本子系统包括：商品信息、货位信息、存货、出入库单等。");
	}
}
