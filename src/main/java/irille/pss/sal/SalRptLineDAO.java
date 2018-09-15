package irille.pss.sal;

import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanForm;
import irille.pub.bean.IGoods;
import irille.pub.idu.Idu;

import java.util.List;

public class SalRptLineDAO {
	public static final Log LOG = new Log(SalRptLineDAO.class);

	public static void insBySalForm(BeanForm form, List<IGoods> list) {
		for (IGoods line : list) {
			SalRptLine model = new SalRptLine();
			model.init();
			if (form instanceof SalSale) {
				model.setCust(((SalSale) form).getCust());
				model.setAmt(((SalSale) form).getAmt());
				model.setWarehouse(((SalSale) form).getWarehouse());
				model.setQty(line.getQty());
			} else if (form instanceof SalSaleDirect) {
				model.setCust(((SalSaleDirect) form).getCust());
				model.setAmt(((SalSaleDirect) form).getAmt());
				model.setWarehouse(null);
				model.setQty(line.getQty());
			} else if (form instanceof SalRtn) {
				model.setCust(((SalRtn) form).getCust());
				model.setAmt(((SalRtn) form).getAmt().negate());
				model.setWarehouse(((SalRtn) form).getWarehouse());
				model.setQty(line.getQty().negate());
			} else
				throw LOG.err("notSalForm", "生成流水的源单据必须为销售单、销售直销单或销售退货单！");
			model.setGoodsKind(line.gtGoods().getKind());
			model.setGoods(line.getGoods());
			model.setDate(form.getApprTime());
			model.setOperator(form.getApprBy());
			model.setDept(form.getDept());
			model.setOrg(form.getOrg());
			model.stForm(form);
			model.setFormNum(form.getCode());
			model.ins();
		}
	}

	public static void delBySalForm(BeanForm form) {
		String sql = Idu.sqlString("DELETE FROM {0} WHERE {1}=?", SalRptLine.class, SalRptLine.T.FORM.getFld());
		BeanBase.executeUpdate(sql, form.gtLongPkey());
	}

}
