package irille.trandb;

import irille.core.lg.Lg;
import irille.core.lg.LgLogin;
import irille.core.lg.LgLoginDAO;
import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysCustom;
import irille.core.sys.SysDept;
import irille.core.sys.SysEm;
import irille.core.sys.SysSupplier;
import irille.core.sys.SysUser;
import irille.core.sys.SysUserDAO;
import irille.core.sys.Sys.OBillStatus;
import irille.gl.frm.FrmPending;
import irille.gl.gl.GlDailyLedger;
import irille.gl.gl.GlGoods;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.GlSubjectMapDAO;
import irille.gl.gl.GlTransform;
import irille.gl.gl.GlTransformDAO;
import irille.gl.gs.GsStock;
import irille.gl.gs.GsUom;
import irille.gl.pya.Pya;
import irille.gl.pya.PyaNoteAccountPayable;
import irille.gl.pya.PyaNoteDepositPayable;
import irille.gl.pya.PyaNotePayable;
import irille.gl.rva.Rva;
import irille.gl.rva.RvaNoteAccount;
import irille.gl.rva.RvaNoteDeposit;
import irille.gl.rva.RvaNoteOther;
import irille.pub.Exp;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduBase;
import irille.pub.svr.DbPool;
import irille.pub.svr.Env;
import irille.pub.svr.LoginUserMsg;
import irille.pub.tb.Tb;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TranTools {
	private static final Log LOG = new Log(Idu.class);
	
	public static void main(String[] args) throws SQLException {
//		autoCreateNote();
//		DbPool.getInstance().getConn().commit();
//		checkBalance2();
//		checkStock();
//		checkDirectBalance();
//		checkBalance();
//		checkUseRate(0,2);
//		EmAssertDeptBelongOrg();
//		CustomAssertMemberBelongOrg();
//		SupplierAssertMemberBelongOrg();
		DbPool.getInstance().releaseAll();
	}
	public static class NoteAccount {
		private static Map<Integer,Class> map = new HashMap<Integer, Class>();
		private static Map<Class,String[]> map2 = new HashMap<Class, String[]>();
		
		public NoteAccount() {
			map2.put(RvaNoteAccount.class, new String[]{Rva.SubjectAlias.RA_CUST.getCode(),Rva.SubjectAlias.RA_OTHER.getCode(),Rva.SubjectAlias.RA_INNER_CELL.getCode(),Rva.SubjectAlias.RA_CELL.getCode()});
			map2.put(RvaNoteDeposit.class, new String[]{Rva.SubjectAlias.RD_CUST.getCode(),Rva.SubjectAlias.RD_OTHER.getCode(),Rva.SubjectAlias.RD_INNER_CELL.getCode(),Rva.SubjectAlias.RD_CELL.getCode()});
			map2.put(RvaNoteOther.class, new String[]{Rva.SubjectAlias.RO_USER.getCode(),Rva.SubjectAlias.RO_CELL.getCode(),Rva.SubjectAlias.RO_INNER_CELL.getCode(),Rva.SubjectAlias.RO_DEPT.getCode(),Rva.SubjectAlias.RO_ACCOUNT.getCode(),Rva.SubjectAlias.RO_OTHER.getCode()});
			map2.put(PyaNoteAccountPayable.class, new String[]{Pya.SubjectAlias.PA_SUPPLIER.getCode(),Pya.SubjectAlias.PA_OTHER.getCode(),Pya.SubjectAlias.PA_INNER_CELL.getCode(),Pya.SubjectAlias.PA_CELL.getCode()});
			map2.put(PyaNoteDepositPayable.class, new String[]{Pya.SubjectAlias.PD_SUPPLIER.getCode(),Pya.SubjectAlias.PD_OTHER.getCode(),Pya.SubjectAlias.PD_INNER_CELL.getCode(),Pya.SubjectAlias.PD_CELL.getCode()});
			map2.put(PyaNotePayable.class, new String[]{Pya.SubjectAlias.PO_USER.getCode(),Pya.SubjectAlias.PO_CELL.getCode(),Pya.SubjectAlias.PO_INNER_CELL.getCode(),Pya.SubjectAlias.PO_DEPT.getCode(),Pya.SubjectAlias.PO_ACCOUNT.getCode(),Pya.SubjectAlias.PO_OTHER.getCode()});
			for(Class cls:map2.keySet()) {
				for(String code:map2.get(cls)) {
					map.put(GlSubjectMapDAO.getByAlias(code).getSubject(), cls);
				}
			}
		}
		public static Class getNoteAccount(Integer subject) {
			return map.get(subject);
		}
	}
	public static void autoCreateNote() {
		GlJournal j = null;
		
		SysCell.TB.getCode();
		FrmPending.TB.getCode();
		GlDailyLedger.TB.getCode();
		List<GlJournal> journalAll = list(GlJournal.class, "select * from gl_journal t1 left join gl_subject t2 on t1.subject=t2.pkey where t2.writeoff_flag=1 and balance<>0");
		Map<Integer,List<GlJournal>> journalMap = new HashMap<Integer, List<GlJournal>>();
		
		for(GlJournal line:journalAll) {
			if(journalMap.containsKey(line.getOrg()) ) {
				journalMap.get(line.getOrg()).add(line);
			}else {
				journalMap.put(line.getOrg(), new ArrayList<GlJournal>());
				journalMap.get(line.getOrg()).add(line);
			}
		}
		for(Integer org:journalMap.keySet()) {
			try {
				List<GlJournal> journal = journalMap.get(org);
				simulateLogin(getAdminByOrg(org).getLoginName());
				NoteAccount fac = new NoteAccount();
				
				GlTransform bean = new GlTransform().init();
				GlTransformDAO.Ins transformIns = new GlTransformDAO.Ins();
				GlTransformDAO.Approve transfromApprove = new GlTransformDAO.Approve();
				IduBase.DoTally tally = new IduBase.DoTally();
				transformIns.setClazz(GlTransform.class);
				transformIns.setB(bean);
				transformIns.commit();
				transfromApprove.setClazz(GlTransform.class);
				transfromApprove.setB(bean);
				transfromApprove.commit();
				bean = transfromApprove.getB();
				for(GlJournal line:journal) {
					j = line;
					GlNoteDAO.Approve glNoteApprove = new GlNoteDAO.Approve();
					glNoteApprove.setClazz(GlNote.class);
					
					GlNote note = new GlNote();
					note.setAmt(line.getBalance());
					note.stDirect(line.gtDirect());//TODO借贷方向还没确定,
					note.stJournal(line);
					note.setPkey(bean.getPkey());
					note.setStatus(OBillStatus.TALLY_ABLE.getLine().getKey());
					System.out.println("subject:"+line.getSubject());
					System.out.println("Class:"+fac.getNoteAccount(line.getSubject()));
	        GlNoteDAO.insByTb(note, GlTransform.class.getName(), fac.getNoteAccount(line.getSubject()).getName(), 0, null, null, null);
	        
	      //  newBean().loadAndLock(getB().pkeyValues());
	        glNoteApprove.setB(note);
	        System.out.println("note:"+note);
	        glNoteApprove.commit();

	        GlNote note2 = new GlNote();
	        note2.setAmt(line.getBalance());
					note2.setDirect((byte)(line.getDirect()==1?2:1));//TODO借贷方向还没确定,
					note2.stJournal(line);
					note2.setPkey(bean.getPkey());
					note2.setStatus(OBillStatus.TALLY_ABLE.getLine().getKey());
	        GlNoteDAO.insByTb(note2, GlTransform.class.getName(), null, 0, null, null, null);
	        glNoteApprove.setB(note2);
	        System.out.println("note2:"+note);
	        glNoteApprove.commit();
				}
				tally.setClazz(GlTransform.class);
        tally.setB(bean);
        tally.commit();
			}
			catch (Exception e) {
	      e.printStackTrace();
	      throw LOG.err(e);
	    }
		} 
	}
	public static SysUser getAdminByOrg(Integer org) {
		SysUser.TB.getCode();
		//该方法建立在一个机构只有一个管理员的前提下，并且管理员的name中一定包含"管理员"字符；
		String where =  Idu.sqlString("{0} like ? and {1}=?", SysUser.T.NAME, SysUser.T.ORG);
		SysUser user = BeanBase.list(SysUser.class, where, false, "%管理员%",org).get(0); 
		return user;
	}
	public static void simulateLogin(String username) {
		try {
			SysUser user = SysUserDAO.loginCheckTmp(username, "123");
			user.stLoginState(Sys.OLoginState.PC); // TODO 最近登录的记录
			user.upd();
			LgLogin lg = LgLoginDAO.initLg(user, Lg.OClient.WINDOWS.getLine().getKey(),
			    "127.0.0.1", "Windows NT", "Firefox");
			LoginUserMsg umsg = new LoginUserMsg(lg);
			Env.INST.initTran(umsg, null);
			System.out.println("模拟登陆成功！---------------------==---======-------------------");
			System.out.println("当前登陆用户为：["+Env.INST.getTran().getUser().getName()+"]");
		} catch (Exp e) {
			System.out.println(e);
		}
	}
	public static List<GlDailyLedger> checkBalance2() {
		//检查日总账中余额与分户账中不一致的记录
		//将日总账按科目字典和核算单元分组并取出每一组中工作日期昨晚的记录
		String sql4dailyLedger = "select * from (select * from gl_daily_ledger order by work_date desc) t1 group by subject,cell";
		//将日总账按科目字典和核算单元分组并取出每一组中工作日期昨晚的记录2
		//String sql2 = "select * from gl_daily_ledger as t1 where not exists (select 1 from gl_daily_ledger where t1.subject=subject and t1.cell=cell and t1.work_date<work_date)";
		//将日总账按科目字典和核算单元分组并取出每一组中工作日期昨晚的记录3
		//String sql3 = "select t1.* from gl_daily_ledger t1 join (select subject,cell,max(work_date) as work_date from gl_daily_ledger group by subject,cell) t2 on t1.subject=t2.subject and t1.cell=t2.cell and t1.work_date=t2.work_date";
		//将分户账按科目字典和核算单元分组，并统计每一组中余额的和
		String sql4journal = "select direct,subject,cell,sum(balance) as balance from gl_journal group by subject,cell";
		//比较相同科目字典和核算单元的日总账和分户账中的余额，将余额不一致的日总账提取出来
		String sql = "select t1.* from ("+sql4dailyLedger+") t1 join ("+sql4journal+") t2 on t1.subject=t2.subject and t1.cell=t2.cell where case when t2.direct=1 then t1.dr_balance when t2.direct=2 then t1.cr_balance end <> t2.balance;";
		List<GlDailyLedger> list = list(GlDailyLedger.class, sql);
		System.out.println("日总账中余额与分户账中余额不同的记录数："+list.size());
		for(GlDailyLedger line:list) {
			System.out.println(line+"的余额与分户账中的余额不一致！");
		}
		return list;
	}
	public static List<GlJournal> checkBalance() {
		//检查存货账中分户账余额汇总结果是否和分户账中的余额一致
		String sql = "select t2.* from gl_goods t1 join gl_journal t2 on t1.journal=t2.pkey group by journal having sum(t1.balance)<>t2.balance;";
		List<GlJournal> list = list(GlJournal.class, sql);
		System.out.println("[存货账]中各个分户账的余额统计结果与[普通分户账]中余额不一致的有"+list.size()+"条");
		for(GlJournal journal:list) {
			System.out.println(journal+"中的余额与存货账中不一致");
		}
		return null;
	}
	
	//sql语句完全自定义，但是必须保证返回的是一个Bean的所有字段，否则会报错！！
	public static List<GsUom> checkUseRate(int min, int max) {
		//检查计量单位在货物中的使用情况，
		//通过min和max筛选出使用次数在一定范围内的计量单位
		String sql = "select t1.pkey as pkey,count(t2.name) as num from gs_uom t1 left join gs_goods t2 on t1.pkey=t2.uom group by t1.name having num>="+min+" and num<="+max ;
		Map<Integer,Integer> uomMap = new HashMap<Integer, Integer>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbPool.getInstance().getConn().prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				uomMap.put(rs.getInt("pkey"), rs.getInt("num"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbPool.close(stmt, rs);
		}
		System.out.println("计量单位使用次数在"+min+"到"+max+"之间的有"+uomMap.size()+"个");
		for(Integer uom:uomMap.keySet()) {
				System.out.println(GsUom.get(GsUom.class, uom)+"的使用次数为"+uomMap.get(uom)+"次");
		}
		return null;
	}
	public static List<SysCell> checkDirectBalance() {
		//检查分户账的借贷平衡 （分核算单元）
		String sql = "select t2.* from "
				+ "(select "
				+ "sum(case when direct=1 then balance else 0 end) as 'DR',"
				+ "sum(case when direct=2 then balance else 0 end) as 'CR',"
				+ "cell "
				+ "from gl_journal group by cell) t1 join sys_cell t2"
				+ " where t1.cell=t2.pkey and CR<>DR";
		List<SysCell> list = list(SysCell.class, sql);
		System.out.println("有"+list.size()+"个核算单元的借贷不平衡");
		for(SysCell cell:list) {
			System.out.println(cell+"借贷不平衡！");
		}
		return list;
	}
	public static void checkStock() {
		//检查存货账中货物数量是否和库存中的货物数量一致
		List<String> errorData = new ArrayList<String>();
		Map<String,BigDecimal> goodsMap = new HashMap<String, BigDecimal>();
		Map<String,BigDecimal> stockMap = new HashMap<String, BigDecimal>();

		List<GlGoods> list = BeanBase.list(GlGoods.class, "", false);
		for(GlGoods goods:list) {
			String code = goods.gtJournal().getCell()+"_"+goods.getGoods();
			if(goodsMap.containsKey(code)) {
				goodsMap.put(code, goodsMap.get(code).add(goods.getQty()));
			} else {
				goodsMap.put(code, goods.getQty());
			}
		}
		
		List<GsStock> stocks = BeanBase.list(GsStock.class, "", false);
		for(GsStock stock:stocks) {
			String code = stock.getCell()+"_"+stock.getGoods();
			if(stockMap.containsKey(code)) {
				stockMap.put(code, stockMap.get(code).add(stock.getQty()));
			}else {
				stockMap.put(code, stock.getQty());
			}
		}
		for(String code:stockMap.keySet()) {
			if(!goodsMap.containsKey(code)) {
				continue;
			}
			if(!goodsMap.get(code).equals(stockMap.get(code))) {
				errorData.add(code);
			}
		}
		System.out.println("[核算单元中的库存]有条错误数据"+errorData.size());
		for(String error:errorData) {
			System.out.println("核算单元["+error.split("_")[0]+"]中的货物["+error.split("_")[1]+"]：存货数量与存货账中数目不一致");
		}
	}
	
	public static List<SysEm> EmAssertDeptBelongOrg() {
		//检查职员所属部门是否属于其机构
		List<SysEm> list = isBelong(SysEm.class, SysDept.class, "dept", "pkey", "org", "org", "<>");
		System.out.println("["+SysEm.TB.getName()+"]"+"有"+list.size()+"条错误数据");
		for(SysEm custom:list) {
				System.out.println(custom+":[部门所属机构与机构冲突]");
		}
		return list;
	}
	public static List<SysCustom> CustomAssertMemberBelongOrg() {
		//检查客户的业务员的所属机构是否和管理机构相同
		SysCustom.TB.getCode();
		SysUser.TB.getCode();
		List<SysCustom> list = isBelong(SysCustom.class, SysUser.class, 
				SysCustom.T.BUSINESS_MEMBER.getFld().getCodeSqlField(), SysUser.T.PKEY.getFld().getCodeSqlField(), 
				SysCustom.T.MNG_ORG.getFld().getCodeSqlField(), SysUser.T.ORG.getFld().getCodeSqlField(), "<>");
		System.out.println("["+SysCustom.TB.getName()+"]"+"有"+list.size()+"条错误数据");
		for(SysCustom custom:list) {
			if(custom.gtBusinessMember().getName().equals("管理员"))
				System.out.println(custom+":[业务员不存在 - 已更改为管理员]");
			else 
				System.out.println(custom+":[业务员所属机构与管理机构冲突]");
		}
		return list;
	}
	public static List<SysSupplier> SupplierAssertMemberBelongOrg() {
		//检查供应商的业务员的所属机构是否和管理机构相同
		SysSupplier.TB.getCode();
		SysUser.TB.getCode();
		List<SysSupplier> list = isBelong(SysSupplier.class, SysUser.class, 
				SysSupplier.T.BUSINESS_MEMBER.getFld().getCodeSqlField(), SysUser.T.PKEY.getFld().getCodeSqlField(), 
				SysSupplier.T.MNG_ORG.getFld().getCodeSqlField(), SysUser.T.ORG.getFld().getCodeSqlField(), "<>");
		System.out.println("["+SysSupplier.TB.getName()+"]"+"有"+list.size()+"条错误数据");
		for(SysSupplier supplier:list) {
			if(supplier.gtBusinessMember().getName().equals("管理员"))
				System.out.println(supplier+":[业务员不存在 - 已更改为管理员]");
			else
				System.out.println(supplier+":[业务员所属机构与管理机构冲突]");
		}
		return list;
	}
	public static <T extends Bean> List<T> list(Class<T> beanClass, String sql) {
		T bean;
		Vector<T> list = new Vector<T>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbPool.getInstance().getConn().prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				bean = BeanBase.newInstance(beanClass);
				bean.fromResultSet(rs);
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbPool.close(stmt, rs);
		}
		return list;
	}
	public static <T extends Bean,E extends Bean> List<T> isBelong(Class<T> mainClass, Class<E> subClass,
			String mainFld, String subFld, String mainCon, String subCon, String sym) {
		String sql = ""
				+ "select * from "+Tb.getTBByBean(mainClass).getCodeSqlTb()+" t1 join "+Tb.getTBByBean(subClass).getCodeSqlTb()+" t2 "
				+ "where t1."+mainFld+"=t2."+subFld+" and t1."+mainCon+sym+"t2."+subCon;
		return list(mainClass,sql);
	}
}
