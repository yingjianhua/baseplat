package irille.doc.gs;

import irille.dep.gs.EGsGoods;
import irille.doc.sys.SysCellDoc;
import irille.pub.doc.DocTran;
import irille.pub.html.EMCrt;

public class GsGoodsDoc extends DocTran {
	public static EMCrt EXT=new EGsGoods().crtExt();
	public static DocTran DOC=new GsGoodsDoc().init();
	
	//自定义信息，包括明细表，名词解释等
//		public static DocTb TB_line=DOC.addTb(EXT.getTb(),"line",EXT.getVflds());  //明细表
//		public static  DocNoun NOUN_cell=DOC.newNoun("NOUN_cell", "核算单元"); //名词定义
	
	@Override
	public void initMsg() {
		DOC.p2("货物。");
		
//		NOUN_cell.add("核算单元是系统中独立核算的单位，机构是核算单元，要独立核算的部门也可以" +
//				"设置核算单元。核算单元相对于其它财务系统就是一本独立的账套。").add(getUrl(DExample.ACT_del)).add(getUrl(DExample.DOC));
//		FLD_m_org.p("核算单元的所属机构。");
	}

	//>>>>>>以下是自动产生的源代码行--Doc文档--请保留此行用于识别>>>>>>
	public static DocAct ACT_list=DOC.getAct("list");	//查询
	public static DocAct ACT_ins=DOC.getAct("ins");	//新增
	public static DocAct ACT_upd=DOC.getAct("upd");	//修改
	public static DocAct ACT_del=DOC.getAct("del");	//删除
	public static DocAct ACT_doEnabled=DOC.getAct("doEnabled");	//启用
	public static DocAct ACT_unEnabled=DOC.getAct("unEnabled");	//停用
	public static DocFld FLD_m_pkey=DOC.getFld("m_pkey");	//编号
	public static DocFld FLD_m_code=DOC.getFld("m_code");	//代码
	public static DocFld FLD_m_kind=DOC.getFld("m_kind");	//货物类别
	public static DocFld FLD_m_codeOld=DOC.getFld("m_codeOld");	//原代码
	public static DocFld FLD_m_name=DOC.getFld("m_name");	//名称
	public static DocFld FLD_m_shortkey=DOC.getFld("m_shortkey");	//快捷键
	public static DocFld FLD_m_uom=DOC.getFld("m_uom");	//计量单位
	public static DocFld FLD_m_spec=DOC.getFld("m_spec");	//规格
	public static DocFld FLD_m_cust1=DOC.getFld("m_cust1");	//属性名称1
	public static DocFld FLD_m_cust2=DOC.getFld("m_cust2");	//属性名称2
	public static DocFld FLD_m_cust3=DOC.getFld("m_cust3");	//属性名称3
	public static DocFld FLD_m_cust4=DOC.getFld("m_cust4");	//属性名称4
	public static DocFld FLD_m_cust5=DOC.getFld("m_cust5");	//属性名称5
	public static DocFld FLD_m_weightRate=DOC.getFld("m_weightRate");	//单位重量
	public static DocFld FLD_m_valumeRate=DOC.getFld("m_valumeRate");	//单位体积
	public static DocFld FLD_m_inFlag=DOC.getFld("m_inFlag");	//入库标识
	public static DocFld FLD_m_outFlag=DOC.getFld("m_outFlag");	//出库标识
	public static DocFld FLD_m_descrip=DOC.getFld("m_descrip");	//描述
	public static DocFld FLD_m_barCode=DOC.getFld("m_barCode");	//条型码
	public static DocFld FLD_m_zeroOutFlag=DOC.getFld("m_zeroOutFlag");	//可否零库存出库
	public static DocFld FLD_m_batchType=DOC.getFld("m_batchType");	//批次管理类型
	public static DocFld FLD_m_economicQty=DOC.getFld("m_economicQty");	//经济批量
	public static DocFld FLD_m_purLeadDays=DOC.getFld("m_purLeadDays");	//采购提前天数
	public static DocFld FLD_m_enabled=DOC.getFld("m_enabled");	//启用标志
	public static DocFld FLD_m_org=DOC.getFld("m_org");	//机构
	public static DocFld FLD_m_rowVersion=DOC.getFld("m_rowVersion");	//版本
	public static DocTb TB_m=DOC.getTb("TB_m");	//货物
	//<<<<<<以上是自动产生的源代码行--Doc文档--请保留此行用于识别<<<<<<

}
