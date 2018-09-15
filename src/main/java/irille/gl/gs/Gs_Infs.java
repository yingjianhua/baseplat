/**
 * 
 */
package irille.gl.gs;

import irille.core.sys.CmbMsgGoodsBy;

/**
 * @author surface1
 *
 */
public class Gs_Infs {
	/**
	 * @author surface1
	 * 需求方顶端单据接口
	 */
	public interface IGsDemandRoot {
		public void demandChecked(IGsOut out); //已审核
		public void demandUnChecked(IGsOut out); //弃审
		/**
		 * 已发货
		 * @param out 上一级的供应方单据
		 * @param shiper 运输商信息
		 * @param transportNumber 运输单号
		 */
		public void demandShipped(IGsOut out,String shiper, String transportNumber ); //已发货
		public CmbMsgGoodsBy gtGoodsby(); //收货人信息
		/**
		 * 已到货
		 * @param signInformation 签收信息
		 */
		public void goodsArrivaled(String signInformation); //已到货
		public String getPackDemand();//包装要求  STR(200)<null>
		public String getRem();//备注
		public Integer getShipMode();//运输方式 <表主键:LgtShipMode>  INT<null> 

		//XXX 以下字段也需要加入
		//取明细（明细也用接口）
	}
		
	/**
	 * @author surface1
	 * 需求的供应方接口（包括出库单）
	 */
	public interface IGsOut {
		/**
		 * 已到货， 此方法用于外部运输商的接口，到货时回馈用
		 * @param signInformation 签收信息
		 */
		public void goodsArrivaled(String signInformation); //已到货
	}
//	/**
//	 * @author surface1
//	 * 供应方接口
//	 */
//	public interface IGsInSupplier {
//		/**
//		 * 已入库
//		 * @param signInformation 签收信息
//		 */
//		public void goodsIn(); //已入库
//	}
//		
//	/**
//	 * @author surface1
//	 * 供应的需求方接口（包括入库单）
//	 */
//	public interface IGsIn {
//
//	}
}
