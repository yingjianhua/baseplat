package irille.gl.gs;


public class GsGain1DAO {
//public static final Log LOG = new Log(GsGain1DAO.class);
//	
//	public static class Ins extends IduIns<Ins, GsGain> {
//		
//		public List<GsGainLine> _lines;
//
//		public List<GsGainLine> getLines() {
//			return _lines;
//		}
//
//		public void setLines(List<GsGainLine> lines) {
//			_lines = lines;
//		}
//		
//		@Override
//		public void before() {
//			// TODO Auto-generated method stub
//			super.before();
//			getB().setPkeyNumAndTbID();
//			getB().setCode(SysSeqDAO.getSeqnumForm(GsGain.TB));
//			getB().setStatus(Sys.OBillStatus.INIT.getLine().getKey());
//			getB().setOrg(getUser().getOrg());
//			getB().setDept(getUser().getDept());
//			getB().setCreatedBy(getUser().getPkey());
//			getB().setCreatedTime(Env.INST.getWorkDate());
//		}
//
//		@Override
//		public void after() {
//			super.after();
//			insLineTid(getB(), getLines());
//		}
//
//	}
//	
//	public static class Upd extends IduUpd<Upd, GsGain> {
//		
//		public List<GsGainLine> _lines;
//
//		public List<GsGainLine> getLines() {
//			return _lines;
//		}
//
//		public void setLines(List<GsGainLine> lines) {
//			_lines = lines;
//		}
//		
//		@Override
//		public void valid() {
//			super.valid();
//			if (getB().gtStatus() != Sys.OBillStatus.INIT)
//				throw LOG.err("err", "盘盈单非初始状态不可修改");
//		}
//		
//		@Override
//		public void before() {
//			super.before();
//			GsGain gain = BeanBase.get(GsGain.class, getB().getPkey());
//			gain.setWarehouse(getB().getWarehouse());
//			gain.setRem(getB().getRem());
//			setB(gain);
//			updLineTid(getB(), getLines(), GsGainLine.class);
//		}
//	}
//	
//	public static class Del extends IduDel<Del, GsGain> {
//
//		@Override
//		public void valid() {
//			super.valid();
//			if (getB().gtStatus() != Sys.OBillStatus.INIT)
//				throw LOG.err("err", "盘盈单非初始状态不可删除");
//		}
//		
//		@Override
//		public void before() {
//			super.before();
//			delLineTid(getB(), GsGainLine.class);
//		}
//
//	}
//	
//	public static class Approve extends IduOther<Approve, GsGain> {
//		public static Cn CN = new Cn("approve", "审核");
//
//		@Override
//		public void run() {
//			super.run();
//			if (getB().gtStatus() != Sys.OBillStatus.INIT)
//				throw LOG.err("approve", "单据[" + getB().getCode() + "]非初始状态，审核出错");
//			GsGain gain = BeanBase.get(GsGain.class, getB().getPkey());
//			gain.setStatus(Sys.OBillStatus.CHECKED.getLine().getKey());
//			gain.setApprBy(getUser().getPkey());
//			gain.setApprTime(Env.INST.getWorkDate());
//			gain.upd();
//			new GsInDAO().autoIns(getB(), Idu.getLinesTid(gain, GsGainLine.class));
//		}
//	}
//
//	public static class Unapprove extends IduOther<Approve, GsGain> {
//		public static Cn CN = new Cn("unapprove", "弃审");
//
//		@Override
//		public void run() {
//			super.run();
//			if (getB().gtStatus() != Sys.OBillStatus.CHECKED)
//				throw LOG.err("err", "盘盈单[" + getB().getCode() + "]状态出错，不可弃审");
//			GsIn in = GsIn.loadUniqueOrig(false, getB().getPkey());
//			if (in.gtStatus() != Sys.OBillStatus.INIT)
//				throw LOG.err("err", "入库单[" + in.getCode() + "]状态出错，不可弃审");
//			GsGain gain = BeanBase.get(GsGain.class, getB().getPkey());
//			gain.setStatus(Sys.OBillStatus.INIT.getLine().getKey());
//			gain.setApprBy(null);
//			gain.setApprTime(null);
//			gain.upd();
//			new GsInDAO().autoDel(in);
//		}
//	}

}
