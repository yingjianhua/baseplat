package irille.trandb;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysCustom;
import irille.core.sys.SysDept;
import irille.core.sys.SysEm;
import irille.core.sys.SysOrg;
import irille.core.sys.SysSupplier;
import irille.gl.gl.Gl;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlSubject;
import irille.gl.gl.Gl.OAccType;
import irille.gl.gl.Gl.OInterestAccrual;
import irille.pub.bean.Bean;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author LYL
 * 2015-7-7 09:59:04
 * 注意:迁移时遇到个别职员状态为停用没迁移而不存在的数据
 * journal_type = '6' and numb = '0'修改为 journal_type = '2' and numb = '11'
 * 迁移数据: `gl_journal`、
 * 迁移条件: (balance <> '0' OR account = '1-101' OR account LIKE '1-102%') AND `code` NOT REGEXP '^(11001|11009|11010|11011|11012)';
 */
public class TranGlJournal {
	public static void run() throws Exception {
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("DROP TABLE " + Bean.tb(GlJournal.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GlJournal.class).getCodeSqlTb() + "]-->成功!");
		stmt.close();
		new DbMysql().db(GlJournal.class);
		runGlJournal();
	}

	public static void runGlJournal() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `gl_journal` WHERE (balance <> '0' OR account = '1-101' OR account LIKE '1-102%') AND `code` NOT REGEXP '^(11001|11009|11010|11011|11012)';";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("==================数据迁移开始==================");
		System.out.println("================迁移【普通分户账】==============");
		int num = 901;
		while (rs.next()) {
			String code = rs.getString("code");
			String name = rs.getString("name");
			String numb = rs.getString("numb");
			String orgcode = rs.getString("org");
			String account = rs.getString("account");
			account = account.substring(account.lastIndexOf("-") + 1);
			byte journal_type = rs.getByte("journal_type");
			BigDecimal balance = rs.getBigDecimal("balance");
			int pd = code.lastIndexOf("-" + journal_type + "-");
			GlJournal glJournal = GlJournal.chkUniqueCode(false, code.substring(0, 5));
			if (glJournal == null) {
				glJournal = new GlJournal();
				if (journal_type == 1 || journal_type == 2 || journal_type == 7) {
					if (numb.lastIndexOf("-") != -1) {
						numb = numb.substring(numb.lastIndexOf("-") + 1);
					}
					if (pd != -1) {
						code = code.replaceAll("-" + journal_type + "-", "-");
						glJournal.setCode(code);
					} else {
						glJournal.setCode(code);
					}
					if (journal_type == 1) {
						glJournal.stAccType(OAccType.ONE);
					}
					if (journal_type == 2 && pd != -1) {
						glJournal.stAccType(OAccType.MUCH);
					}else{
						glJournal.stAccType(OAccType.ONE);		
					}
					if (journal_type == 7) {
						glJournal.stAccType(OAccType.PROJECT);
					}
					GlSubject tmpSub = GlSubject.chkUniqueTempCode(false, 1, account);
					//目标科目类型是多账户、且迁移旧数据是单账户时，作调整
					if (tmpSub.gtAccType() == Gl.OAccType.MUCH && glJournal.gtAccType() == OAccType.ONE) {
						glJournal.stAccType(OAccType.MUCH);
						glJournal.setCode(code +"-" + num);
						num++;
					}
				}
				// 明细帐类型为3则查找部门pkey
				if (journal_type == 3) {
					numb = numb.replaceAll("-", "");
					if (numb.equals("10100201")) {
						numb = "1100201";
					}
					SysDept dept = SysDept.chkUniqueCode(false, numb);
					if (dept != null) {
						if (pd != -1) {
							code = code.substring(0, pd) + "-" + dept.getPkey();
							glJournal.setCode(code);
							glJournal.stAccType(OAccType.DEPT);
						} else {
							glJournal.setCode(code);
							glJournal.stAccType(OAccType.ONE);
						}
						numb = "" + dept.gtLongPkey();
					} else {
						System.err.println("部门[" + numb + "]不存在");
					}
				}
				if (journal_type == 4) {
					SysOrg org = SysOrg.chkUniqueCode(false, numb);
					if (org != null) {
						if (pd != -1) {
							code = code.substring(0, pd) + "-" + org.getPkey();
							glJournal.setCode(code);
						} else {
							code = code + "-" + org.getPkey();
							glJournal.setCode(code);
						}
						numb = "" + org.gtLongPkey();
						glJournal.stAccType(OAccType.ORG);
					}else{
						System.err.println("机构["+numb+"]不存在");
					}
				}
				if (journal_type == 6) {
					SysEm em = SysEm.chkUniqueCode(false, numb);
					if (em != null) {
						if (code.lastIndexOf("-101-6-") != -1) {
							glJournal.setCode(orgcode + "-101-" + num);
							numb = "" + num;
							num++;
							glJournal.stAccType(OAccType.MUCH);
						} else {
							if (pd != -1) {
								code = code.substring(0, pd) + "-" + em.getPkey();
								glJournal.setCode(code);
							} else {
								code = code + "-" + em.getPkey();
								glJournal.setCode(code);
							}
							numb = "" + em.gtLongPkey();
							glJournal.stAccType(OAccType.EM);
						}
						glJournal.setRem("因数据错误原数据:(" + code+")");
					} else {
						if (account.equals("12901") || account.equals("12902")) {
							glJournal.setCode(orgcode + "-12905-" + num);
							account = "12905";
							numb = "" + num;
							num++;
							glJournal.setRem("因数据错误原数据:("+code+")");
						}
						if (code.lastIndexOf("-101-6-") != -1) {
							glJournal.setCode(orgcode + "-101-" + num);
							numb = "" + num;
							num++;
							glJournal.setRem("因数据错误原数据:(" + code+")");
						}
						glJournal.stAccType(OAccType.MUCH);
					}
				}
				if (journal_type == 5) {
					if (account.substring(0, 1).equals("1")) {
						SysCustom custom = SysCustom.chkUniqueCode(false, numb);
						if (custom != null) {
							code = code.substring(0, pd) + "-" + custom.getPkey();
							if (account.equals("122")) {
								glJournal.setCode(orgcode + "-12203-" + custom.getPkey());
							} else {
								glJournal.setCode(code);
							}
							account = "12203";
							numb = "" + custom.gtLongPkey();
							glJournal.stAccType(OAccType.CUSTOM);
						} else {
							glJournal.setCode(orgcode + "-12909-" + num);
							account = "12909";
							numb = "" + num;
							num++;
							glJournal.setRem("因数据错误原数据:("+code+")");
							glJournal.stAccType(OAccType.MUCH);
						}
					} else {
						SysSupplier supplier = SysSupplier.chkUniqueCode(false, numb);
						if (supplier != null) {
							code = code.substring(0, pd) + "-" + supplier.getPkey();
							if (account.equals("204")) {
								glJournal.setCode(orgcode + "-20403-" + supplier.getPkey());
							} else {
								glJournal.setCode(code);
							}
							account = "20403";
							numb = "" + supplier.gtLongPkey();
							glJournal.stAccType(OAccType.SUPPLIER);
						} else {
							glJournal.setCode(orgcode + "-21109-" + num);
							account = "21109";
							numb = "" + num;
							num++;
							glJournal.setRem("因数据错误原数据:("+code+")");
							glJournal.stAccType(OAccType.MUCH);
						}
					}
				}
				glJournal.setObjPkey(Long.parseLong(numb));
				GlSubject glSubject = GlSubject.chkUniqueTempCode(false, 1, account);
				glJournal.setSubject(glSubject.getPkey());
				SysCell cell = SysCell.chkUniqueCode(false, orgcode);
				SysOrg orglist = SysOrg.chkUniqueCode(false, orgcode);
				glJournal.setName(name);
				glJournal.setCell(cell.getPkey());
				glJournal.stOrg(orglist);
				glJournal.setBalance(balance);
				glJournal.setBalanceUse(balance);
				glJournal.stCurrency(Sys.OCurrency.RMB);
				glJournal.stState(Gl.OJlState.NORMAL);
				glJournal.stFrostFlag(Gl.OFrostFlag.NORMAL);
				glJournal.stInFlag(glSubject.gtInFlag());
				glJournal.setAccJournalType(glSubject.getAccJournalType());
				glJournal.setTallyFlag(glSubject.getTallyFlag());
				glJournal.stInterestAccrual(OInterestAccrual.DEFAULT);
				glJournal.setDirect(glJournal.gtSubject().getDirect());
				glJournal.ins();
			}
		}
		System.out.println("==================数据迁移完成==================");
		TranDb.INST.close(stat, rs);
	}
}
