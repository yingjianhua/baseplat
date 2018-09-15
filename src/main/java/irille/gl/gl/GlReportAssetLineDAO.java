package irille.gl.gl;

import irille.core.sys.SysOrg;
import irille.gl.gl.Gl.ODirect;
import irille.gl.gl.Gl.OSymbolType;
import irille.gl.gl.Gl.OTableType;
import irille.gl.gl.Gl.OValueType;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.svr.DbPool;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class GlReportAssetLineDAO {
	private static final Log LOG = new Log(GlReportAssetLineDAO.class);
	
	public static List<GlReportAssetLine> getListLine(SysOrg org,Date beginDate,Date endDate) {
		//System.out.println("GlReportAssetLineDAO.getListLine.method.startTime:"+new Date().getTime());
		List<GlReportAssetLine> listLine = new ArrayList<GlReportAssetLine>();
		String where = "table_type in (?,?,?)";
		AmtStruct amt = new AmtStruct();
		for(GlReport list:BeanBase.list(GlReport.class, where, false, 1,2,3)) {
			GlReportAssetLine line = initLine(list);
			line.setAmtBegin(amt.getAmtBegin(list, beginDate, org));
			line.setAmtEnd(amt.getAmtEnd(list, endDate, org));
			listLine.add(line);
		}
		//System.out.println("GlReportAssetLineDAO.getListLine.method.endTime:"+new Date().getTime());
		return listLine;
	}
	
	//产生初始化后的资产负债表明细
	public static GlReportAssetLine initLine(GlReport list) {
		GlReportAssetLine line = Bean.newInstance(GlReportAssetLine.class);
		if(list.gtValueType()==OValueType.W) {
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
	
	static class AmtStruct {
		HashMap<Long,BigDecimal> amtBeginMap = new HashMap<Long, BigDecimal>();
		HashMap<Long,BigDecimal> amtEndMap = new HashMap<Long, BigDecimal>();
		HashMap<OTableType,BigDecimal> amtTotalBegin = new HashMap<OTableType, BigDecimal>();
		HashMap<OTableType,BigDecimal> amtTotalEnd = new HashMap<OTableType, BigDecimal>();
		
		public void addAmt(GlReport list,BigDecimal amtBegin,BigDecimal amtEnd) {
			Long parent = list.getParent();
			if(list.gtValueType()==OValueType.YE) {
				amtBeginMap.put(parent, getBeginMap(parent).add(amtBegin));
				amtEndMap.put(parent, getEndMap(parent).add(amtEnd));	
			} else if(list.gtValueType()==OValueType.XJ) {
				
			}
		}
		public BigDecimal getAmtBegin(GlReport report, Date date, SysOrg org) {
			if(report.getName().equals("负债和所有者权益总计")) {
				return getTotalBeginMap(OTableType.FZ).add(getTotalBeginMap(OTableType.QY));
			}
			BigDecimal amt = BigDecimal.ZERO;
			if(report.gtValueType()==OValueType.YE) {
				amt = getBalance(report, date, org);
				if(report.gtSymbolType()==OSymbolType.ADD) {
					amtBeginMap.put(report.getParent(), getBeginMap(report.getParent()).add(amt));
					amtTotalBegin.put(report.gtTableType(), getTotalBeginMap(report.gtTableType()).add(amt));
				} else if (report.gtSymbolType()==OSymbolType.SUB) {
					amtBeginMap.put(report.getParent(), getBeginMap(report.getParent()).subtract(amt));
					amtTotalBegin.put(report.gtTableType(), getTotalBeginMap(report.gtTableType()).subtract(amt));
				}
			} else if (report.gtValueType()==OValueType.XJ) {
				amt = getBeginMap(report.getParent());
			} else if (report.gtValueType()==OValueType.ZJ) {
				amt = getTotalBeginMap(report.gtTableType());
			} else if (report.gtValueType()==OValueType.W) {
				return BigDecimal.ZERO;
			}
			return amt;
		}
		
		public BigDecimal getAmtEnd(GlReport report, Date date, SysOrg org) {
			if(report.getName().equals("负债和所有者权益总计")) {
				return getTotalEndMap(OTableType.FZ).add(getTotalEndMap(OTableType.QY));
			}	
			BigDecimal amt = BigDecimal.ZERO;
			if(report.gtValueType()==OValueType.YE) {
				amt = getBalance(report, date, org);
				if(report.gtSymbolType()==OSymbolType.ADD) {
					amtEndMap.put(report.getParent(), getEndMap(report.getParent()).add(amt));
					amtTotalEnd.put(report.gtTableType(), getTotalEndMap(report.gtTableType()).add(amt));
				} else if (report.gtSymbolType()==OSymbolType.SUB) {
					amtEndMap.put(report.getParent(), getEndMap(report.getParent()).subtract(amt));
					amtTotalEnd.put(report.gtTableType(), getTotalEndMap(report.gtTableType()).subtract(amt));
				}
			} else if (report.gtValueType()==OValueType.XJ) {
				amt = getEndMap(report.getParent());
			} else if (report.gtValueType()==OValueType.ZJ) {
				amt = getTotalEndMap(report.gtTableType());
			} else if (report.gtValueType()==OValueType.W) {
				return BigDecimal.ZERO;
			}
			return amt;
		}
		public BigDecimal getBeginMap(Long parent) {
			BigDecimal amt = amtBeginMap.get(parent);
			if(amt==null) {
				amt = BigDecimal.ZERO;
				amtBeginMap.put(parent, amt);
			}
			return amt;
		}
		public BigDecimal getEndMap(Long parent) {
			BigDecimal amt = amtEndMap.get(parent);
			if(amt==null) {
				amt = BigDecimal.ZERO;
				amtEndMap.put(parent, amt);
			}
			return amt;
		}
		public BigDecimal getTotalBeginMap(OTableType tableType) {
			BigDecimal amt = amtTotalBegin.get(tableType);
			if(amt==null) {
				amt = BigDecimal.ZERO;
				amtTotalBegin.put(tableType, amt);
			}
			return amt;
		}
		public BigDecimal getTotalEndMap(OTableType tableType) {
			BigDecimal amt = amtTotalEnd.get(tableType);
			if(amt==null) {
				amt = BigDecimal.ZERO;
				amtTotalEnd.put(tableType, amt);
			}
			return amt;
		}
	//通过报表设置和工作日期，得到余额
		public static BigDecimal getBalance(GlReport report, Date date, SysOrg org) {
			//List<GlDailyLedger> dailys = BeanBase.list(GlDailyLedger.class, "work_date=? and org=?", false, date, org.getPkey());
			//两种方式去取DailyLedger，上面这种是考虑每天都会有DailyLedger的情况，
			//下面这种是并不是每天会有日总账，在生成报表时，根据所选的日期，去取所选日期之前最接近的某天的记录做统计；
			String sql = Idu.sqlString("select * from (select * from {0} where {1}<=? and {2}=? order by {1} desc) as daily group by {3},{4}",
					GlDailyLedger.TB.getCodeSqlTb(),GlDailyLedger.T.WORK_DATE, GlDailyLedger.T.ORG,GlDailyLedger.T.SUBJECT,GlDailyLedger.T.CELL);
			GlDailyLedger bean;
			Vector<GlDailyLedger> dailys = new Vector<GlDailyLedger>();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				stmt = DbPool.getInstance().getConn().prepareStatement(sql);
				BeanBase.toPreparedStatementData(stmt, 1, date, org.getPkey());
				rs = stmt.executeQuery();
				while (rs.next()) {
					bean = new GlDailyLedger();
					bean.fromResultSet(rs);
					dailys.add(bean);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw LOG.err("query", "查询数据出错!");
			} finally {
				DbPool.close(stmt, rs);
			}
			
			List<GlReportLine> reportLines = BeanBase.listTid(GlReportLine.class, report, false);
			BigDecimal balance = BigDecimal.ZERO;
			for(GlReportLine reportLine:reportLines) {
				String subjectCode = reportLine.gtSubject().getCode();
				OSymbolType symbolType = reportLine.gtSymbolType();
				BigDecimal amt = BigDecimal.ZERO;
				for(GlDailyLedger daily:dailys) {
					if(daily.gtSubject().getCode().startsWith(subjectCode)) {
						if(daily.gtSubject().gtDirect()==ODirect.DR) {
							amt = amt.add(daily.getDrBalance());
						} else if (daily.gtSubject().gtDirect()==ODirect.CR) {
							amt = amt.add(daily.getCrBalance());
						}
					}
				}
				if(symbolType==OSymbolType.ADD) 
					balance = balance.add(amt);
				else 
					balance = balance.subtract(amt);
			}
			return balance;
		}
	}
}
