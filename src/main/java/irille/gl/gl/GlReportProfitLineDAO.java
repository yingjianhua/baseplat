package irille.gl.gl;

import irille.core.sys.SysOrg;
import irille.gl.gl.Gl.OValueType;
import irille.gl.gl.GlReportAssetLineDAO.AmtStruct;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GlReportProfitLineDAO {
	
	public static List<GlReportProfitLine> getListLine(SysOrg org,Date beginDate,Date endDate) {
		boolean flag = false;
		List<GlReportProfitLine> listLine = new ArrayList<GlReportProfitLine>();
		List<GlReportProfitLine> olistLine = new ArrayList<GlReportProfitLine>();
		Map<Integer,GlReportProfitLine> map = new HashMap<Integer, GlReportProfitLine>();
		AmtStruct amt = new AmtStruct();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		if(cal.MONTH!=Calendar.JANUARY) {
			cal.set(Calendar.DATE, -1);
			String where = Idu.sqlString("{0}=? and {1}=?", GlReportProfit.T.END_DATE,GlReportProfit.T.ORG);
			List<GlReportProfit> profits = BeanBase.list(GlReportProfit.class, where, false, cal.getTime(),org);
			if(profits.size()!=0) {
				olistLine = BeanBase.listTid(GlReportProfitLine.class, profits.get(0), false);
				flag = true;
//				System.out.println(flag);
				for(GlReportProfitLine line:olistLine) {
					map.put(line.getKeyValue(), line);
				}
			}	
		}
		
		String where = "table_type=?";
		for(GlReport list:BeanBase.list(GlReport.class, where, false, 4)) {
			GlReportProfitLine line = initLine(list);
			line.setAmtBegin(amt.getAmtEnd(list, endDate, org));
//			System.out.println("amtBegin:"+line.getAmtBegin());
			if(flag == true) {
				line.setAmtEnd(map.get(line.getKeyValue()).getAmtEnd().add(line.getAmtBegin()));
//				System.out.println("keyValue:"+line.getKeyValue());
			} else {
				line.setAmtEnd(line.getAmtBegin());
			}
//			System.out.println("amtEnd:"+line.getAmtEnd());
			listLine.add(line);
		}
		return listLine;
	}
//产生初始化后的利润表明细
	public static GlReportProfitLine initLine(GlReport list) {
		GlReportProfitLine line = Bean.newInstance(GlReportProfitLine.class);
		if(list.gtValueType()==OValueType.ZJ||list.getKeyValue()==1) {
			line.setKeyName(list.getName());
		} else {
			line.setKeyName("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+list.getName());	
		}
		line.setKeyValue(list.getKeyValue());
		line.setOrderId(list.getOrderId());
		line.setTableType(list.getTableType());
		line.setRowVersion((short)0);
		return line;
	}
	
}
