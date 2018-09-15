package irille.pub.idu;

import irille.core.sys.SysSeqDAO;
import irille.pss.sal.Sal;
import irille.pss.sal.SalSale;
import irille.pub.bean.Bean;
import irille.pub.bean.IBill;
import irille.pub.bean.IForm;
import irille.pub.svr.Env;
import irille.pub.tb.Tb;

import java.math.BigDecimal;
import java.util.List;

public class IduInsLines<T extends IduInsLines, BEAN extends Bean, LINES extends Bean>
		extends IduIns<T, BEAN> {
	private List<LINES> _lines;

	public List<LINES> getLines() {
		return _lines;
	}

	public void setLines(List<LINES> lines) {
		_lines = lines;
	}

}
