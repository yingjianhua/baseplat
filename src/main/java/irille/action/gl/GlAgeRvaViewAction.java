package irille.action.gl;

import irille.action.ActionBase;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysCustom;
import irille.core.sys.SysUser;
import irille.gl.gl.GlAgeRvaView;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlSubject;
import irille.gl.rva.RvaNoteAccount;
import irille.pss.sal.Sal;
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

public class GlAgeRvaViewAction extends ActionBase<GlAgeRvaView> {
	private static final int ONEDAY = 1000*60*60*24;
	private int _org;
	private int _dept;
	private int _businessMember;
	private static FilterA filter ;
	private Calendar _date = Calendar.getInstance();
	
	public GlAgeRvaView getBean() {
		return _bean;
	}

	public void setBean(GlAgeRvaView bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlAgeRvaView.class;
	}

	public void getParams() throws JSONException {
		if (!Str.isEmpty(getFilter())) {
			JSONArray ja = new JSONArray(getFilter());
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
	public class FilterA{
		public boolean filter(SysUser Member,Date startDate){
			if(!filterBelong(Member)) return false;
			return filterTime(startDate);
		}
		public boolean filterBelong(SysUser Member) {
			return true;
		};
		public boolean filterTime(Date startDate) {
			return startDate.before(_date.getTime());
		}
	}
	public class FilterM extends FilterA {
    public boolean filterBelong(SysUser Member) {
    	return Member.getPkey().equals(_businessMember);
    }
	}
	public class FilterD extends FilterA {
		 public boolean filterBelong(SysUser Member) {
	    	return Member.gtDept().getPkey().equals(_dept);
	    }
	}
	public class FilterO extends FilterA {
		 public boolean filterBelong(SysUser Member) {
	    	return Member.gtOrg().getPkey().equals(_org);
	    }
	}
	public class FilterE extends FilterA {
		public boolean filterBelong(SysUser Member) {
			return true;
		}
	}
	public Map<Integer, GlAgeRvaView> countBalance() {
		Map<Integer, GlAgeRvaView> ages = new LinkedHashMap<Integer, GlAgeRvaView>();//每个业务员的汇总记录
		GlAgeRvaView sumAge = new GlAgeRvaView().init();//所有业务员的总汇总记录
		sumAge.setName("汇总");
		GlSubject subject = GlSubject.gtByTemplatAlias(SysCellDAO.getCellByUser(Env.INST.getTran().getUser()).gtTemplat(), Sal.SubjectAlias.SAL_INCOME.getCode());
		//扩展表：应收账款凭条(4601)
		List<GlNote> notes = GlNote.list(GlNote.class, "ext_table=4601 and journal in (select pkey from gl_journal where subject=?)", false, subject.getPkey());
		for(GlNote note:notes) {
			RvaNoteAccount account = RvaNoteAccount.load(RvaNoteAccount.class, note.getPkey());//应收账款凭条
			SysCustom custom = (SysCustom)note.gtJournal().gtObjPkey();//客户
			SysUser businessMember = custom.gtBusinessMember();//业务员
			Date startDate = account.getDateStart()!=null?account.getDateStart():note.getCreatedTime();//应收账款凭条的起始时间（若没有就取建档时间）
			if(!filter.filter(businessMember, startDate)) continue;//过滤不符合用户搜索条件的记录
			
			BigDecimal balance;//余额
			if(account.getUpdatedDate().before(_date.getTime())) {//若更新时间比搜索日期早，则余额为应付账款凭条的余额
				balance = account.getBalance();
			} else {
				balance = note.getAmt();//若更新时间比搜索日期晚，则余额为应付账款凭条的金额 减去 已经核销的金额
				List<GlNote> wnotes = GlNote.list(GlNote.class, "ext_table=4602 and status=98 and created_time<=? and pkey in (select pkey from rva_note_account_line where main_note=?)", false, _date, account);
				for(GlNote wnote:wnotes) {
					balance = balance.add(wnote.getAmt().negate());
				}
			}
			int distanceDay = (int)((_date.getTimeInMillis()-startDate.getTime())/ONEDAY);//起始时间距离搜索日期的天数
			GlAgeRvaView age;
			if(ages.containsKey(businessMember.getPkey())) {
				age = ages.get(businessMember.getPkey());
			}else {
				age = new GlAgeRvaView().init();
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
			age.setCode(custom.getCode());
			age.setName(custom.getName());
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
		getParams();
		Map<Integer, GlAgeRvaView> list = countBalance();
		JSONObject lineJson = null;
		for (GlAgeRvaView line : list.values()) {
			lineJson = crtJsonByBean(line);
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, list.size());
		writerOrExport(json);
	
	}
}
