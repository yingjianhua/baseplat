/**
 * 
 */
package irille.doc.sys;

import irille.core.sys.Sys;
import irille.pub.doc.DocPackage;
import irille.pub.doc.DocTran;

/**
 * 包说明文档
 * @author whx
 * 
 */
public class SysDoc extends DocPackage {
	public static String NAME = Sys.TB.getName();   //必须定义，在基类中要取用
	public static SysDoc DOC = (SysDoc)new SysDoc().init();// 产生帮助文件的对象实例，在测试代码中会引用
	public static DocImg IMG = DOC.newImg("系统管理", "D:\\irille\\irillePlat\\src\\irille\\doc\\images\\sys.jpg");

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.dep.DocBase#initMsg()
	 */
	@Override
	public void initMsg() {
		super.initMsg();
		DOC.add("系统模块包括了");
		DOC.add(getUrl(SysOrgDoc.DOC));
		DOC.add("、");
		DOC.add(getUrl(SysDeptDoc.DOC));
		DOC.add("、");
		DOC.add(getUrl(SysEmDoc.DOC));
		DOC.add("、");
		DOC.add(getUrl(SysTemplatDoc.DOC));
		DOC.add("、");
		DOC.add(getUrl(SysCellDoc.DOC));
		DOC.add("、");
		DOC.add(getUrl(SysCustomDoc.DOC));
		DOC.add("等多个子菜单。");
		/*DOC.p2("系统模块主要包括了机构信息、部门信息、职员信息、用户管理、财务模板、核算单元、供应商信息、客户信息等。");*/
		DOC.br(2);
		DOC.add(IMG);
	}
}
