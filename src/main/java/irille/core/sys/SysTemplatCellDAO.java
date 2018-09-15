package irille.core.sys;

import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduIns;

import java.util.List;

public class SysTemplatCellDAO {
	public static final Log LOG = new Log(SysTemplatCellDAO.class);
	
	public static SysTemplat getPurTmpl() {
		return getPurTmpl(Idu.getCell());
	}
	
	public static SysTemplat getPurTmpl(SysCell cell) {
		String where = Idu.sqlString("{0}=?", SysTemplatCell.T.CELL);
		List<SysTemplatCell> list = BeanBase.list(SysTemplatCell.class, where, false, cell.getPkey());
		for (SysTemplatCell line : list) {
			if (line.gtTemplat().gtType() == Sys.OTemplateType.PUR)
				return line.gtTemplat();
		}
		throw LOG.err("nodefine", "核算单元[{0}]的可使用模板未定义", cell.getName());
	}

	public static class Ins extends IduIns<Ins, SysTemplatCell> {

		@Override
		public void before() {
			super.before();
			if (getB().chkUniqueTemplatCell(false, getB().getTemplat(), getB().getCell()) != null)
				throw LOG.err("isExist", "模板[{0}]已存在单元[{1}]！", getB().gtTemplat().getName(), getB().gtCell().getName());
			//获取和该模板类型相同的模板,同一单元对于同一模板类型只能有一个
			List<SysTemplat> list = BeanBase.list(SysTemplat.class, SysTemplat.T.TYPE.getFld().getCodeSqlField() + "=?",
			    false, getB().gtTemplat().getType());
			for (SysTemplat line : list) {
				if (getB().chkUniqueTemplatCell(false, line.getPkey(), getB().getCell()) != null)
					throw LOG.err("isExist", "核算单元[{0}]已启用模板[{1}]，一个核算单元只能使用一个采购模板！", getB().gtCell().getName(), line.getName());
			}
		}
	}

}
