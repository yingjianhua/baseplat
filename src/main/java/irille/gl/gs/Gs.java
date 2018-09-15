/**
 * 
 */
package irille.gl.gs;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysModule;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.PackageBase;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.tb.TbBase;

/**
 * 产品与服务信息包
 * @author surface1
 *
 */
public class Gs extends PackageBase{
	private static final Log LOG = new Log(Gs.class);
	public static final Gs INST = new Gs();
	public static TbBase TB = new TbBase<Tb>(Gs.class, "库存模块"); //定义公共的Fld对象用
	private Gs() {
	}

	@Override
	public void initTbMsg() { // 初始化表信息
		addTb(1, GsGoodsKind.class); //13
		addTb(2, GsGoods.class); //14
		addTb(3, GsGoodsCmb.class);  //908
		addTb(5, GsUomType.class); //910
		addTb(6, GsWarehouse.class); //911
		addTb(7, GsLocation.class);//912
		addTb(8, GsStock.class);//913
		addTb(9, GsStockStimate.class);//931
		addTb(10, GsStockBatch.class); //932
		addTb(11, GsPrice.class);//942
		addTb(12, GsPriceCtl.class); //943
		addTb(13, GsPriceGoods.class); //944
		addTb(14, GsPriceGoodsCell.class); //945
		addTb(15, GsPriceGoodsKind.class); //946
		
		addTb(51, GsIn.class,  Sys.OTableType.FORM); //30001
		addTb(52, GsOut.class,  Sys.OTableType.FORM);//30002
		addTb(53, GsGain.class,  Sys.OTableType.FORM);//30003
		addTb(54, GsLoss.class,  Sys.OTableType.FORM);//30004
		addTb(55, GsRequest.class,  Sys.OTableType.FORM);//30005
		addTb(56, GsDemand.class,  Sys.OTableType.FORM);//30006
		addTb(57, GsDemandDirect.class,  Sys.OTableType.FORM);//30007
		addTb(58, GsMovement.class,  Sys.OTableType.FORM);//30008
		addTb(59, GsUnite.class,  Sys.OTableType.FORM);//30009
		addTb(60, GsSplit.class,  Sys.OTableType.FORM);//30010
		addTb(61, GsPhyinv.class,  Sys.OTableType.FORM);//30010
		addTb(62, GsReportSalOut.class);
		addTb(63, GsReportMvOut.class);
		addTb(64, GsReportPurIn.class);
	}

	public void initTranData() { //初始化PrvTranData表数据
		addTD(new TranDataMsg(GsGoodsKind.TB));
		addTD(new TranDataMsg(GsGoods.TB));
		addTD(new TranDataMsg(GsGoodsCmb.TB));
		addTD(new TranDataMsg(GsUomType.TB));
		addTD(new TranDataMsg(GsWarehouse.TB).d(GsWarehouse.T.PKEY).c(GsWarehouse.T.CELL).o(GsWarehouse.T.ORG));
		addTD(new TranDataMsg(GsLocation.TB).d(GsLocation.T.WAREHOUSE).c(GsLocation.T.WAREHOUSE,GsWarehouse.T.CELL).o(GsLocation.T.WAREHOUSE,GsWarehouse.T.ORG));
		addTD(new TranDataMsg(GsStock.TB).d(GsStock.T.WAREHOUSE).c(GsStock.T.WAREHOUSE,GsWarehouse.T.CELL).o(GsStock.T.WAREHOUSE,GsWarehouse.T.ORG));
		addTD(new TranDataMsg(GsStockStimate.TB).d(GsStockStimate.T.WAREHOUSE).c(GsStockStimate.T.WAREHOUSE,GsWarehouse.T.CELL).o(GsStockStimate.T.WAREHOUSE,GsWarehouse.T.ORG));
		addTD(new TranDataMsg(GsStockBatch.TB).d(GsStockBatch.T.STOCK,GsStock.T.WAREHOUSE).c(GsStockBatch.T.STOCK,GsStock.T.WAREHOUSE,GsWarehouse.T.CELL).o(GsStockBatch.T.STOCK,GsStock.T.WAREHOUSE,GsWarehouse.T.ORG));
		addTD(new TranDataMsg(GsPrice.TB));
		addTD(new TranDataMsg(GsPriceCtl.TB));
		addTD(new TranDataMsg(GsPriceGoods.TB));
		addTD(new TranDataMsg(GsPriceGoodsCell.TB).c(GsPriceGoodsCell.T.CELL).o(GsPriceGoodsCell.T.CELL,SysCell.T.ORG));
		addTD(new TranDataMsg(GsPriceGoodsKind.TB));
		
		addTD(new TranDataMsg(GsIn.TB).d(GsIn.T.WAREHOUSE).o(GsIn.T.WAREHOUSE, GsWarehouse.T.ORG));
		addTD(new TranDataMsg(GsOut.TB).d(GsOut.T.WAREHOUSE).o(GsOut.T.WAREHOUSE, GsWarehouse.T.ORG));
		addTD(new TranDataMsg(GsGain.TB).d(GsGain.T.WAREHOUSE).o(GsGain.T.WAREHOUSE, GsWarehouse.T.ORG));
		addTD(new TranDataMsg(GsLoss.TB).d(GsLoss.T.WAREHOUSE).o(GsLoss.T.WAREHOUSE, GsWarehouse.T.ORG));
		addTD(new TranDataMsg(GsRequest.TB).u(GsRequest.T.CREATED_BY).d(GsRequest.T.DEPT).o(GsRequest.T.ORG));
		addTD(new TranDataMsg(GsDemand.TB).d(GsDemand.T.WAREHOUSE).c(GsDemand.T.CELL).o(GsDemand.T.ORG));
		addTD(new TranDataMsg(GsDemandDirect.TB).c(GsDemandDirect.T.CELL).o(GsDemandDirect.T.ORG));
		addTD(new TranDataMsg(GsMovement.TB).d(GsMovement.T.WAREHOUSE_IN).o(GsMovement.T.WAREHOUSE_IN,GsWarehouse.T.ORG));
		addTD(new TranDataMsg(GsMovement.TB).d(GsMovement.T.WAREHOUSE_OUT).o(GsMovement.T.WAREHOUSE_OUT,GsWarehouse.T.ORG));
		addTD(new TranDataMsg(GsUnite.TB).d(GsUnite.T.WAREHOUSE).o(GsUnite.T.WAREHOUSE, GsWarehouse.T.ORG));
		addTD(new TranDataMsg(GsSplit.TB).d(GsSplit.T.WAREHOUSE).o(GsSplit.T.WAREHOUSE, GsWarehouse.T.ORG));
		addTD(new TranDataMsg(GsPhyinv.TB).d(GsPhyinv.T.WAREHOUSE).o(GsPhyinv.T.WAREHOUSE, GsWarehouse.T.ORG));
	}
	
	@Override
	public SysModule initModule() {
		return iuModule(Gs.TB, "goods-货物管理-30,gs-库存管理-40");
	}
	
	/**
	 * 初始化，在运行期间仅执行一次
	 */
	public void initOnlyOne() { // 初始化方法，在每次启动时执行一次
		super.initOnlyOne();
	}
	
	public static enum SubjectAliasCst implements ISubjectAlias {//@formatter:off
		CST_GS_GAIN("成本核算.盘盈单"),
		CST_GS_LOSS("成本核算.盘亏单"),
		CST_GS_UNIT("成本核算.合并单"),
		CST_GS_SPLIT("成本核算.拆分单"),
		;
		private String _name;
		private SubjectAliasCst(String name) { _name=name;}
		public String getName(){return _name;}
		public String getSubName(int i){return Str.split(_name,".")[i]; }
		public String getCode(){return Str.tranLineUpperToField(name());}
	}//@formatter:on

	@Override
	public ISubjectAlias[] getSubjectAliasesCst() {
		return SubjectAliasCst.values();
	}

	public enum T implements IEnumFld {
		;
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
		{_fld=TB.addOutKey(clazz,this,name,isnull);	}
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld); }
		public Fld getFld(){return _fld;}
	}		
	
	public enum OOutOrder implements IEnumOpt {//@formatter:off
		FREE(1,"随意"),FIFO(2,"先入先出");
		public static final String NAME="存货出库顺序";
		public static final OOutOrder DEFAULT = FREE; // 定义缺省值
		private EnumLine _line;
		private OOutOrder(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on
	
	public enum OPriceOrig implements IEnumOpt {//@formatter:off
		COST(1,"成本价自动产生（不可维护）"),COST_UPD(2,"成本价自动产生（可维护）");
		public static final String NAME="定价基数来源";
		public static final OPriceOrig DEFAULT = COST; // 定义缺省值
		private EnumLine _line;
		private OPriceOrig(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on
	
	public enum OType implements IEnumOpt{
		//@formatter:off 、、、
			COMMODITY(1,"标准产品"),
			RAW_MATERIAL(11,"自有产品"),
			SERVICE(12,"服务"),
			WORK(99,"人工");
			public static final String NAME="类型";
			public static final OType DEFAULT = COMMODITY; // 定义缺省值
			private EnumLine _line;
			private OType(int key, String name) {_line=new EnumLine(this,key,name);	}
			public EnumLine getLine(){return _line;	}
			//@formatter:on
		}

		public enum OBatchType implements IEnumOpt {//@formatter:off
			NO(0,"不用分批次管理"),BATCH(1,"分批次管理"),EXP_DATE(2,"有效(保质)期管理"),SERIAL(3,"一物一序列号管理");
			public static final String NAME="批次管理类型";
			public static final OBatchType DEFAULT = NO; // 定义缺省值
			private EnumLine _line;
			private OBatchType(int key, String name) {_line=new EnumLine(this,key,name);	}
			public EnumLine getLine(){return _line;	}
		}		//@formatter:on
		
		public enum OEnrouteType implements IEnumOpt {//@formatter:off
			YQG(11,"已请购"),DBZT(12,"调拔在途"),CGZT(13,"采购在途"),SCDD(14,"生产订单"),DHZJ(15,"到货/在检"),WWDD(16,"委外订单"),QTZT(49,"其他在途"),
			YDQ(51,"已订购"),DBDF(52,"调拔待发"),DFH(53,"待发货"),SCWL(54,"生产未领"),BLJH(55,"备料计划"),WWWL(56,"委外未领"),QTSD(99,"其他锁定"),
			;
			public static final String NAME="预计出入库货物类别";
			public static final OEnrouteType DEFAULT = CGZT; // 定义缺省值
			private EnumLine _line;
			private OEnrouteType(int key, String name) {_line=new EnumLine(this,key,name);	}
			public EnumLine getLine(){return _line;	}
		}		//@formatter:on
		
		public enum OKindType implements IEnumOpt {//@formatter:off 
			GOODS_STD(1,"标准产品"),
			GOODS_MY(2,"自由产品"),
			SERVE(6,"服务"),
			WORK(7,"人工"),
			;
			public static final String NAME="类别";
			public static final OKindType DEFAULT = GOODS_STD;
			private EnumLine _line;
			private OKindType(int key, String name) {_line=new EnumLine(this,key,name);	}
			public EnumLine getLine(){return _line;	}
		}		//@formatter:on
		
}
