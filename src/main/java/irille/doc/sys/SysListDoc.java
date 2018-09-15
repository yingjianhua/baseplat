/**
 * 
 */
package irille.doc.sys;

import irille.pub.doc.DocClassList;

/**
 * 包中所有交易的目录清单。
 * 每个类都要在此引用，以便生成相关索引文件。
 * 以应用方式运行此类，将重新产生本包下所有的帮助文档。
 * @author whx
 * 
 */
public class SysListDoc extends DocClassList {
	public static SysDoc PACKAGE = SysDoc.DOC; // 必须定义，在基类中要取用
	public static DocClassList DOC = new SysListDoc().init();// 产生帮助文件的对象实例，在测试代码中会引用

	/**
	 * 重新产生本模块的所有帮助文件
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		DOC.saveAllToFile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.dep.DocBase#initMsg()
	 */
	@Override
	public void initMsg() {
		super.initMsg();
		addPackage(PACKAGE);
		// 在此列入所有的类
		//@formatter:off	
		add(SysCellDoc.DOC);
		add(SysComDoc.DOC);
		add(SysCtypeDoc.DOC);
		add(SysCustomDoc.DOC);
		add(SysDeptDoc.DOC);
		add(SysEmDoc.DOC);
		add(SysOrgDoc.DOC);
//		add(SysSupplierDoc.DOC);
		add(SysTemplatDoc.DOC);
		//@formatter:off	
	}
}
