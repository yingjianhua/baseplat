package irille.view;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page<T> {
	
	@JsonIgnore
	private int start;
	@JsonIgnore
	private int limit = 15;
	
	private Integer total;
	private Integer curPage;
	private List<T> items;
	
	public Page(List<T> items, Integer total) {
		super();
		this.total = total;
		this.items = items;
	}
	
	public Page(List<T> items, Integer total, Integer curPage) {
		super();
		this.total = total;
		this.curPage = curPage;
		this.items = items;
	}
	
}
