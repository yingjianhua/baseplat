package irille.gl.gl;

import irille.core.sys.SysTable;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanLong;
import irille.pub.idu.Idu;

import java.util.List;

public class GlReportLineDAO {
	public static final Log LOG = new Log(GlReportLineDAO.class);

	public static void update(List<GlReportLine> lineList, int reportPkey) {
		valid(lineList);
		String where = Idu.sqlString("{0}=?", GlReportLine.T.REPORT);
		List<GlReportLine> list = BeanBase.list(GlReportLine.class, where, false, reportPkey);
		Idu.delLine(list);
		long mainPkey = (long)reportPkey * SysTable.NUM_BASE;
		int num = 1;
		if(lineList==null) return;
		for(GlReportLine line : lineList) {
			line.setPkey(mainPkey + num);
			line.ins();
			num++;
		}
	/*	long mainpkey = lineFirstPkey(main.getPkey());
		long mainpkey2 = mainpkey + SysTable.NUM_BASE;
		int num = 0;
		String wheresql = "pkey > " + mainpkey + " and pkey < " + mainpkey2;
		List<Bean> listOld = BeanBase.list(lineClazz, wheresql, true); // 数据库旧数据
		for (BeanLong formBean : (List<BeanLong>) list) {
			if (formBean.getPkey() == null)
				continue;
			for (Bean bean : listOld) {
				if (bean.equals(formBean)) {
					PropertyUtils.copyWithoutCollection(bean, formBean);
					bean.upd();
					if (formBean.getPkey() % SysTable.NUM_BASE > num)
						num = (int) (formBean.getPkey() % SysTable.NUM_BASE);
					break;
				}
			}
		}
		// 删除不存的数据
		for (Bean bean : listOld) {
			if (list.contains(bean))
				continue;
			bean.del();
		}
		// 新增
		for (BeanLong formBean : (List<BeanLong>) list) {
			if (formBean.getPkey() != null)
				continue;
			num++;
			formBean.setPkey(mainpkey + num);
			formBean.setRowVersion((short) 0);
			validFromOut(formBean);
			formBean.ins();
		}
		*/
	}
	public static void valid(List<GlReportLine> lineList) {
		if(lineList==null) return;
		for(GlReportLine line : lineList) {
			if(line.getSubject()==null) {
				throw LOG.err("validate","科目不可为空，请输入");
			} else if(line.getDirect()==null) {
				throw LOG.err("validate","借贷标志不可为空，请输入");
			} else if(line.getSymbolType()==null) {
				throw LOG.err("validate","加减类型不可为空，请输入");
			}
		}
	}
		
}
