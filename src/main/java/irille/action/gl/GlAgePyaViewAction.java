package irille.action.gl;

import irille.action.ActionBase;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysCustom;
import irille.core.sys.SysSupplier;
import irille.core.sys.SysUser;
import irille.gl.gl.GlAgePyaView;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlSubject;
import irille.gl.pya.PyaNoteAccountPayable;
import irille.pss.pur.Pur;
import irille.pub.DateTools;
import irille.pub.Str;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GlAgePyaViewAction extends ActionBase<GlAgePyaView> {
	private static final int ONEDAY = 1000*60*60*24;
	private int _org;
	private int _dept;
	private int _businessMember;
	private static FilterA filter;
	private Calendar _date = Calendar.getInstance();
	public GlAgePyaView getBean() {
		return _bean;
	}

	public void setBean(GlAgePyaView bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlAgePyaView.class;
	}

	public static class FilterA {
		private static final int ONEDAY = 1000*60*60*24;
		public static FilterA filter = new FilterA();
		protected static int _org;
		protected static int _dept;
		protected static int _businessMember;
		private static Calendar _date = Calendar.getInstance();
		public FilterA() {}
		public static FilterA getInstance() {
			if(filter==null) {
				if(_businessMember!=0) {
					filter = new FilterM();
				} else if(_dept!=0) {
					filter = new FilterD();
				} else if(_org!=0) {
					filter = new FilterO();
				} else {
					filter = new FilterE();
				}
			}
			return filter;
		}
		public boolean filter(SysUser Member,Date startDate) {
			if(!filterBelong(Member)) return false;
			return filterTime(startDate);
		}
		public static boolean filterBelong(SysUser Member) {
			return true;
		}
		public boolean filterTime(Date startDate) {
			return startDate.before(_date.getTime());
		}
		public static void getParams(String filter) throws JSONException {
			if (!Str.isEmpty(filter)) {
				JSONArray ja = new JSONArray(filter);
				for (int i = 0; i < ja.length(); i++) {
					JSONObject json = ja.getJSONObject(i);
					String fldName = json.getString(QUERY_PROPERTY);
					String param = json.getString(QUERY_VALUE);
					if(fldName.equals("org")) {
						_org = Integer.parseInt(param);
					}else if(fldName.equals("dept")) {
						_dept = Integer.parseInt(param);
					}else if(fldName.equals("businessMember")) {
						_businessMember = Integer.parseInt(param);
					} else if(fldName.equals("date")) {
						try {
			        _date.setTime(DateTools.string2Date(param));
		        } catch (Exception e) {
			        e.printStackTrace();
		        }
					}
				}
			}
			_date.set(Calendar.HOUR_OF_DAY, 23);
			_date.set(Calendar.MINUTE, 59);
			_date.set(Calendar.SECOND, 59);
		}
	}
	public static class FilterM extends FilterA {
    public static boolean filterBelong(SysUser Member) {
    	return Member.getPkey().equals(_businessMember);
    }
	}
	public static class FilterD extends FilterA {
		 public static boolean filterBelong(SysUser Member) {
	    	return Member.gtDept().getPkey().equals(_dept);
	    }
	}
	public static class FilterO extends FilterA {
		 public static boolean filterBelong(SysUser Member) {
	    	return Member.gtOrg().getPkey().equals(_org);
	    }
	}
	public static class FilterE extends FilterA {
		public static boolean filterBelong(SysUser Member) {
			return true;
		}
	}
	public Map<Integer, GlAgePyaView> countBalance() throws Exception{
		//List<GlAgePyaView> ages = new ArrayList<GlAgePyaView>();
		filter = FilterA.getInstance();
		filter.getParams(getFilter());
		Map<Integer, GlAgePyaView> ages = new LinkedHashMap<Integer, GlAgePyaView>();//根据业务员的汇总记录
		GlAgePyaView sumAge = new GlAgePyaView().init();//所有业务员的汇总记录
		sumAge.setName("汇总");
		GlSubject subject = GlSubject.gtByTemplatAlias(SysCellDAO.getCellByUser(Env.INST.getTran().getUser()).gtTemplat(), Pur.SubjectAlias.PUR_INCOME.getCode());
		//扩展表：应收账款凭条(4601)
		String where = "ext_table=4403 and journal in (select pkey from gl_journal where subject=?)";
		List<GlNote> notes = GlNote.list(GlNote.class, where, false, subject.getPkey());
		Date startDate;
		for(GlNote note:notes) {//遍历经过第一次筛选的所有GlNote
			PyaNoteAccountPayable account = PyaNoteAccountPayable.load(PyaNoteAccountPayable.class, note.getPkey());
			SysSupplier supplier = (SysSupplier)note.gtJournal().gtObjPkey();
			SysUser businessMember = supplier.gtBusinessMember();
			startDate = account.getDateStart()!=null?account.getDateStart():note.getCreatedTime();
			if(!filter.filter(businessMember, startDate)) continue;//不符合用户搜索条件的记录过滤
			
			BigDecimal balance;
			if(account.getUpdatedDate().before(_date.getTime())) {//获取余额
				balance = account.getBalance();
			} else {
				balance = note.getAmt();
				//扩展表：应付账款核销凭条（4404） 状态为完成（98） 核销当前凭条的核销凭条
				List<GlNote> wnotes = GlNote.list(GlNote.class, "ext_table=4404 and status=98 and created_time<=? and pkey in (select pkey from pya_note_account_payable where main_note=?)", false, _date, account);
				for(GlNote wnote:wnotes) {
					balance = balance.add(wnote.getAmt().negate());
				}
			}
			//判断账龄
			int distanceDay = (int)((_date.getTimeInMillis()-startDate.getTime())/ONEDAY);
			GlAgePyaView age;
			if(ages.containsKey(businessMember.getPkey())) {
				age = ages.get(businessMember.getPkey());
			}else {
				age = new GlAgePyaView().init();
				ages.put(businessMember.getPkey(), age);
			}
			if(distanceDay>=0&&distanceDay<=30) {
				age.setBalanceA(age.getBalanceA().add(balance));
				sumAge.setBalanceA(sumAge.getBalanceA().add(balance));
			} else if(distanceDay<=60) {
				age.setBalanceB(age.getBalanceB().add(balance));
				sumAge.setBalanceB(sumAge.getBalanceB().add(balance));
			} else if(distanceDay<=90) {
				age.setBalanceC(age.getBalanceC().add(balance));
				sumAge.setBalanceC(sumAge.getBalanceC().add(balance));
			} else {
				age.setBalanceD(age.getBalanceD().add(balance));
				sumAge.setBalanceD(sumAge.getBalanceD().add(balance));
			}
			age.stDept(businessMember.gtDept());
			age.stBusinessMember(businessMember);
			age.setCode(supplier.getCode());
			age.setName(supplier.getName());
			age.setBalance(age.getBalance().add(balance)); 
		}
		sumAge.setBalance(sumAge.getBalanceA().add(sumAge.getBalanceB()).add(sumAge.getBalanceC()).add(sumAge.getBalanceD()));
		ages.put(0, sumAge);
		return ages;
	}
	public void list() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		// 目前过滤器的搜索，是肯定会带初始条件的
		Map<Integer, GlAgePyaView> list = countBalance();
		JSONObject lineJson = null;
		for (GlAgePyaView line : list.values()) {
			lineJson = crtJsonByBean(line);
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, list.size());
		writerOrExport(json);
	
	}
}
