package irille.gl.gs;

import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;

import java.util.List;

public class GsStockLineDAO {
	public static final Log LOG = new Log(GsStockLineDAO.class);
	public static String GET_LINES = Idu.sqlString("{0}=?", GsStockLine.T.GS_FORM);
	public static String COUNT_LINES = Idu.sqlString("select count(*) from {0} where {1}=?", GsStockLine.class,
	    GsStockLine.T.GS_FORM);

	public static List<GsStockLine> getStockLines(Bean bean, boolean lock, int idx, int count) {
		return BeanBase.list(GsStockLine.class, lock, GET_LINES, idx, count, bean.gtLongPkey());
	}

	public static int getStockLinesCount(Bean bean) {
		return ((Number) BeanBase.queryOneRow(COUNT_LINES, bean.gtLongPkey())[0]).intValue();
	}
}
