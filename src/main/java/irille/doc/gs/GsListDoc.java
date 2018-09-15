/**
 * 
 */
package irille.doc.gs;

import irille.gl.gs.GsUnite;
import irille.pub.doc.DocClassList;

/**
  * 包中所有交易的目录清单。
 * 每个类都要在此引用，以便生成相关索引文件。
 * 以应用方式运行此类，将重新产生本包下所有的帮助文档。
* @author whx
 * 
 */
public class GsListDoc extends DocClassList {
	public static GsDoc PACKAGE=GsDoc.DOC;   //必须定义，在基类中要取用
	public static DocClassList DOC = new GsListDoc().init();// 产生帮助文件的对象实例，在测试代码中会引用

	/**
	 * 重新产生本模块的所有帮助文件
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
		//在此列入所有的类 
		//@formatter:off	
		add(GsInDoc.DOC);
		add(GsOutDoc.DOC);
		add(GsGainDoc.DOC);
//		add(GsLossDoc.DOC);
		add(GsStockDoc.DOC);
		add(GsStockStimateDoc.DOC);
		add(GsStockBatchDoc.DOC);
		add(GsWarehouseDoc.DOC);
		add(GsLocationDoc.DOC);
		add(GsRequestDoc.DOC);
		add(GsDemandDoc.DOC);
		add(GsDemandDirectDoc.DOC);
		add(GsMovementDoc.DOC);
		add(GsUniteDoc.DOC);
		add(GsSplitDoc.DOC);
		add(GsPhyinvDoc.DOC);
		
		add(GsGoodsDoc.DOC);
		add(GsGoodsKindDoc.DOC);
		add(GsGoodsCmbDoc.DOC);
		add(GsUomTypeDoc.DOC);
//		add(GsPriceDoc.DOC);
//		add(GsPriceCtlDoc.DOC);
//		add(GsPriceGoodsDoc.DOC);
//		add(GsPriceGoodsCellDoc.DOC);
//		add(GsPriceGoodsKindDoc.DOC);
//		add(GsPriceGoodsKindCellDoc.DOC);
		//@formatter:off	
	}
}
