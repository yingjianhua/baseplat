package irille.view;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Page<T> {
	public static final String PAGE_TOTALCOUNT = "totalCount";
	public static final String PAGE_TOTALPAGE = "totalPage";
	public static final String PAGE_CURRENTPAGE = "currentPage";
	public static final String PAGE_START = "start";
	public static final String PAGE_LIMIT = "limit";
	public static final String PAGE_ITEMS = "items";
    
	private int totalCount;
	private int totalPage;
	private int currentPage;
	private int start;
	private int limit;
	private List<T> items;
	
	public Page(int totalCount, int totalPage, int currentPage, int start, int limit, List<T> items) {
		super();
		this.totalCount = totalCount;
		this.totalPage = totalPage;
		this.currentPage = currentPage;
		this.start = start;
		this.limit = limit;
		this.items = items;
	}
	public Page(List<T> items, int start, int limit, int totalCount) {
		this.items = items;
		this.start = start;
		this.limit = limit;
		this.totalCount = totalCount;
		if(limit == 0) {
			this.totalPage = 1;
			this.currentPage = 1;
		} else {
			this.totalPage = totalCount/limit+(totalCount%limit>0?1:0);
			this.currentPage = start/limit+1;
		}
	}
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray(items, false);
		json.put(PAGE_TOTALCOUNT, totalCount);
		json.put(PAGE_TOTALPAGE, totalPage);
		json.put(PAGE_CURRENTPAGE, currentPage);
		json.put(PAGE_START, start);
		json.put(PAGE_LIMIT, limit);
		json.put(PAGE_ITEMS, ja);
		return json;
	}
	public String toJSONString() throws JSONException {
		return toJSON().toString();
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}
}
