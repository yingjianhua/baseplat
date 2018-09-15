package irille.gl.gs;

import irille.core.sys.Sys;
import irille.core.sys.SysDept;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.BeanBuf;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.util.List;

public class GsUomTypeDAO {
	public static final Log LOG = new Log(GsUomTypeDAO.class);
	
	public static class Ins extends IduIns<Ins, GsUomType> {
		public void before(){
			super.before();
			if(getB().chkUniqueName(false, getB().getName())!=null)
				throw LOG.err("uomType","名称[{0}]不能重复!",getB().getName());
			getB().stEnabled(true);
		}

	}
	
	public static class Upd extends IduUpd<Upd, GsUomType> {
		
		public void before() {
			super.before();
			GsUomType dbBean = load(getB().getPkey());
			if(!dbBean.getName().equals(getB().getName())){
				if(getB().chkUniqueName(false, getB().getName())!=null)
					throw LOG.err("uomType","名称[{0}]不能重复!",getB().getName());
			}
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), GsUomType.T.PKEY,GsUomType.T.ENABLED);
			setB(dbBean);
			BeanBuf.clear(GsUomType.class, getB().getPkey());
		}
	}
	public static class Del extends IduDel<Del, GsUomType> {

		@Override
		public void valid() {
			super.valid();
			haveBeUsed(GsUom.class, GsUom.T.UOM_TYPE, b.getPkey());
		}
		@Override
		public void before() {
			super.before();
			BeanBuf.clear(GsUomType.class, getB().getPkey());
		}
		
	}
	
	/*public static final void updAndUnableLine(BeanMain main, List list, Fld field) {
		String wheresql = field.getCodeSqlField() + "=?";
		List<GsUom> listOld = BeanBase.list(field.getTb().getClazz(), wheresql, false, main.pkeyValues()); // 数据库旧数据
		boolean insFlag;
		for (Bean formBean : (List<Bean>) list) {
			formBean.propertySet(field, main.getPkey());
			insFlag = true; // 默认新增标志
			for (GsUom uom : listOld) {
				if (uom.equals(formBean)) {
					insFlag = false; // 修改标志
					PropertyUtils.copyWithoutCollection(uom, formBean);
					uom.upd();
					break;
				}
			}
			if (insFlag)
				formBean.ins();
		}
		// 禁用不存的数据
		for (GsUom uom : listOld) {
			if (list.contains(uom))
				continue;
			uom.stEnabled(false);
			uom.upd();
		}
	}*/
	
	public static final void unableLine(List set) {
		for (GsUom bean : (List<GsUom>) set) {
			bean.stEnabled(false);
			bean.upd();
		}
	}
	
	public static void setDefaultValue(List<GsUom> list) {
		if (list == null)
			return;
		for (GsUom line : list) {
			if (line.getEnabled() == null)
				line.setEnabled(Sys.OEnabled.TRUE.getLine().getKey());
		}
	}

}
