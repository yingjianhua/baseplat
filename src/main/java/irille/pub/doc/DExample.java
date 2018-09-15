/**
 * 
 */
package irille.pub.doc;

import irille.core.sys.SysCell;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFldOutKey;
import irille.pub.view.VFlds;

/**
 * @author
 * 
 */
public class DExample extends DocTran {
	public static EMCrt EXT=new MyExt().crtExt();  //产生Ext的对象实例，在测试代码中会引用
	public static DocTran DOC=new DExample().init();//产生帮助文件的对象实例，在测试代码中会引用
	
	//自定义信息，包括明细表，名词解释等
	public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
	public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元明细"); //名词定义
	public static  DocImg IMG_order=DOC.newImg("销售订单流程图", "../image/SalOrder销售订单.gif"); //图片定义

	public static void main(String[] args) {
		EXT.backupFiles().crtFilesAndCompBackup();  //产生Ext文件
	}

	/**
	 * 对象说明内容的设定
	 * 常用方法:
	 *     p:换行
	 *     p2:前空2格并换行
	 *     add:增加对象，包括字符串
	 *     add2:前空2格，增加字符串
	 *     getUrl(节点)：取节点的连接，如add(getUrl(ESysCell.ACT_del))
	 */
	@Override
	public void initMsg() {
		DOC.p2("核算单元信息的管理。");  //交易的备注
		DOC.add(IMG_order);
		
		NOUN_cell.p2("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。"); //名词解释的备注
		NOUN_cell.add("引用图片：").add(getUrl(IMG_order));
//		FLD_m_org.p("核算单元的所属机构。");
	}

	private static class MyExt extends SysCell{
		public EMCrt crtExt() {
			VFlds[] vflds = new VFlds[] { new VFlds(TB) };
			VFlds[] searchVflds = new VFlds[] { new VFlds(T.NAME, T.YEAR) };
			EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
			((VFldOutKey)ext.getVfldsForm().get(T.TEMPLAT)).setDiySql("type=1");
			ext.newExts().init();
			return ext;
		}
	}
	
	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_del=DOC.getAct("del");	//删除
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_code=DOC.getFld("m_code");	//代码
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//名称
	public static DocFld FLD_m_year=DOC.getFld("m_year");	//年份
	public static DocFld FLD_m_org=DOC.getFld("m_org");	//机构
	public static DocFld FLD_m_dept=DOC.getFld("m_dept");	//部门
	public static DocFld FLD_m_templat=DOC.getFld("m_templat");	//财务模板
	public static DocTb TB_m=DOC.getTb("TB_m");	//核算单元
	public static DocFld FLD_line_pkey=DOC.getFld("line_pkey");	//编号
	public static DocFld FLD_line_code=DOC.getFld("line_code");	//代码
	public static DocFld FLD_line_name=DOC.getFld("line_name");	//名称
	public static DocFld FLD_line_year=DOC.getFld("line_year");	//年份
	public static DocFld FLD_line_org=DOC.getFld("line_org");	//机构
	public static DocFld FLD_line_dept=DOC.getFld("line_dept");	//部门
	public static DocFld FLD_line_templat=DOC.getFld("line_templat");	//财务模板
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<	
}
