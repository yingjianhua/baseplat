package irille.gl.gs;

import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.math.BigDecimal;

public class GsLocationDAO {
	
	public static final Log LOG = new Log(GsLocationDAO.class);
	
	public static void validate(GsLocation location) {
		if (GsLocation.chkUniqueWarehouseName(false, location.getWarehouse(), location.getName()) != null)
			throw LOG.err("hasLocation", "仓库[{0}]已存在货位[{1}]，请重新输入!", location.gtWarehouse().getExtName(), location.getName());
	}

	public static class Ins extends IduIns<Ins, GsLocation> {
		public void before() {
			super.before();
//			BeanBase.chkUnique(idx, lockFlag, values);
			validate(getB());
			getB().setWeightUsed(BigDecimal.ZERO);
			getB().setWeightAvail(getB().getWeight());
			getB().setValumeUsed(BigDecimal.ZERO);
			getB().setValumeAvail(getB().getValume());
		}
//		//测试推送的代码
//		@Override
//		public void after() {
//			// TODO Auto-generated method stub
//			super.after();
//			Event event = Event.createDataEvent(getUser().getLoginName());//发送字段
//            event.setField("msgcont", String.valueOf(getB().getName()));
//            event.setField("unreadMsg", String.valueOf(count()));
//            Dispatcher.getInstance().multicast(event);
//		}
	}
	
	public static class Upd extends IduUpd<Upd, GsLocation> {
		public void before() {
			super.before();
			if (getB().getWeight().compareTo(getB().getWeightUsed()) == -1)
				throw LOG.err("isFull", "已用重量不得大于总可用总量，请重新输入!");
			if (getB().getValume().compareTo(getB().getValumeUsed()) == -1)
				throw LOG.err("isFull", "已用体积不得大于总可用体积，请重新输入!");
			GsLocation location = BeanBase.get(GsLocation.class, getB().getPkey());
			location.setName(getB().getName());
			location.setEnabled(getB().getEnabled());
			location.setWeight(getB().getWeight());
			location.setWeightUsed(getB().getWeightUsed());
			location.setWeightAvail(location.getWeight().subtract(location.getWeightUsed()));
			location.setValume(getB().getValume());
			location.setValumeUsed(getB().getValumeUsed());
			location.setValumeAvail(location.getValume().subtract(location.getValumeUsed()));
			location.setRem(getB().getRem());
			setB(location);
		}
	}

}
