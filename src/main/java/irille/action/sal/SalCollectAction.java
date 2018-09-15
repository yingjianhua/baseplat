package irille.action.sal;

import irille.action.ActionBase;
import irille.pss.sal.SalCollect;
import irille.pss.sal.SalCollectDAO;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class SalCollectAction extends ActionBase<SalCollect>{
	private String date;
	private Date startDate;
	private Date expireDate;
	
	public Date getStartDate() {
  	return startDate;
  }
	public void setStartDate(Date startDate) {
  	this.startDate = startDate;
  }
	public Date getExpireDate() {
  	return expireDate;
  }
	public void setExpireDate(Date expireDate) {
  	this.expireDate = expireDate;
  }
	public String getDate() {
  	return date;
  }
	public void setDate(String date) {
  	this.date = date;
  }
	public static void main(String[] args){
		try {
	    new SalCollectAction().test();
    } catch (Exception e) {
	    e.printStackTrace();
    }
	}
	public void test() throws Exception{
		System.out.println(new Date(0));
		System.out.println(new Date(9223372036854L));
	}
	@Override
	public Class beanClazz() {
	  return SalCollect.class;
	}
	
	public SalCollect getBean() {
		return _bean;
	}

	public void setBean(SalCollect bean) {
		this._bean = bean;
	}
	@Override
	public void list() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		Calendar cal = Calendar.getInstance();
		SalCollectDAO dao = new SalCollectDAO();
		if(date==null) {
			cal.setTime(getExpireDate());
			cal.add(Calendar.DAY_OF_YEAR, 1);
			setExpireDate(cal.getTime());
		} else if(date.equals("today")) {
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			setStartDate(cal.getTime());
			cal.add(Calendar.DAY_OF_YEAR,1);
			setExpireDate(cal.getTime());
		} else if(date.equals("month")){
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			setStartDate(cal.getTime());
			cal.add(Calendar.MONTH, 1);
			setExpireDate(cal.getTime());
		} else if(date.equals("total")) {
			setStartDate(new Date(0));
			setExpireDate(new Date(9223372036854L));//TODO 这里不能设置日期的time为long类型的最大值？？
		}
		dao.setStartDate(getStartDate());
		dao.setExpireDate(getExpireDate());
		Map<String,SalCollect> list = dao.aa();
		JSONObject lineJson = null;
		for (String key : list.keySet()) {
			SalCollect line = list.get(key);
			lineJson = crtJsonByBean(line, "bean.");
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, ja.length());
		json.put("success", true);
		writerOrExport(json);
	}
}
