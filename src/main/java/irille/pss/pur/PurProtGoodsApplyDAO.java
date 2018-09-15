package irille.pss.pur;

import irille.core.sys.SysSeqDAO;
import irille.pub.Log;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduBase;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.List;

public class PurProtGoodsApplyDAO {
	public static final Log LOG = new Log(PurProtGoodsApplyDAO.class);

	public static class Ins extends
			IduInsLines<Ins, PurProtGoodsApply, PurProtGoodsApplyLine> {
		@Override
		public void before() {
			// TODO Auto-generated method stub
			super.before();
			if (getLines() == null || getLines().size() == 0)
				throw LOG.err("noLines", "货物信息不可为空");
			PurProt prot = PurProt.chkUniqueTempCust(false,
					getB().getTemplat(), getB().getSupplier());
			if (prot == null)
				throw LOG.err("Ins", "供应商【" + getB().getName() + "】还未签定协议！");
			validateRepeatLines(getLines());//检查明细中货物是否重复
			initForm(getB());
			
		}

		@Override
		public void after() {
			super.after();
			for (PurProtGoodsApplyLine line : getLines()) {
				line.setAftDateStart(Env.getWorkDate());
			}
			insLineTid(getB(), getLines());
		}
	}

	public static class Upd extends
			IduUpdLines<Upd, PurProtGoodsApply, PurProtGoodsApplyLine> {

		@Override
		public void before() {
			super.before();
			if (getLines() == null || getLines().size() == 0)
				throw LOG.err("noLines", "货物信息不可为空");
			validateRepeatLines(getLines());//检查明细中货物是否重复
			PurProtGoodsApply bean = loadThisBeanAndLock();
			assertStatusIsInit(bean);
			bean.setTemplat(getB().getTemplat());
			bean.setSupplier(getB().getSupplier());
			bean.setName(getB().getName());
			bean.setRem(getB().getRem());
			setB(bean);
			for (PurProtGoodsApplyLine line : getLines())
				line.setAftDateStart(Env.getWorkDate());
			updLineTid(getB(), getLines(), PurProtGoodsApplyLine.class);
		}
	}

	public static class Del extends IduDel<Del, PurProtGoodsApply> {

		@Override
		public void before() {
			super.before();
			PurProtGoodsApply bean = loadThisBeanAndLock();
			assertStatusIsInit(bean);
			delLineTid(getB(), PurProtGoodsApplyLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, PurProtGoodsApply> {

		@Override
		public void run() {
			super.run();
			PurProtGoodsApply bean = loadThisBeanAndLock();
			assertStatusIsInit(bean);
			bean.stStatus(STATUS.CHECKED);
			bean.setApprBy(getUser().getPkey());
			bean.setApprTime(Env.INST.getWorkDate());
			bean.upd();
			setB(bean);
			List<PurProtGoodsApplyLine> lines = getLinesTid(bean,
					PurProtGoodsApplyLine.class);
			IduBase.Ins insdao = new IduBase.Ins();
			insdao.setClazz(PurProtGoods.class);
			IduBase.Upd upddao = new IduBase.Upd();
			upddao.setClazz(PurProtGoods.class);
			for (PurProtGoodsApplyLine line : lines) {
				PurProtGoods pg = PurProtGoods.chkUniqueTempCustObj(true,
						bean.getTemplat(), bean.getSupplier(), line.getGoods());
				if (pg == null) {
					pg = new PurProtGoods();
					pg.setTemplat(bean.getTemplat());
					pg.setSupplier(bean.getSupplier());
					pg.setName(bean.getName());
					pg.setGoods(line.getGoods());
					pg.setUom(line.getUom());
					pg.setVendorGoodsName(line.getAftVendorGoodsName());
					pg.setVendorNum(line.getAftVendorNum());
					pg.setVendorSpec(line.getAftVendorSpec());
					pg.setPrice(line.getAftPrice());
					//TODO 启动日期不用
					pg.setDateStart(line.getAftDateStart());
					pg.setDateEnd(line.getAftDateEnd());
					pg.setUpdatedTime(Env.getTranBeginTime());
					pg.setUpdatedBy(getUser().getPkey());
					insdao.setB(pg);
					insdao.commit();
				} else {
					pg.setVendorGoodsName(line.getAftVendorGoodsName());
					pg.setVendorNum(line.getAftVendorNum());
					pg.setVendorSpec(line.getAftVendorSpec());
					pg.setPrice(line.getAftPrice());
					pg.setDateStart(line.getAftDateStart());
					pg.setDateEnd(line.getAftDateEnd());
					pg.setUpdatedTime(Env.getTranBeginTime());
					pg.setUpdatedBy(getUser().getPkey());
					upddao.setB(pg);
					upddao.commit();
				}
			}
		}
	}
	
	
	protected static void validateRepeatLines(List<PurProtGoodsApplyLine> lines) {
		for (int i = 0; i < lines.size(); i++)
			for (int j = i+1; j < lines.size(); j++)
				if (lines.get(i).getGoods().equals(lines.get(j).getGoods()))
					throw LOG.err("ins", "明细中货物【{0}】重复，不可新增！", lines.get(i).gtGoods().getName());
	}
}
