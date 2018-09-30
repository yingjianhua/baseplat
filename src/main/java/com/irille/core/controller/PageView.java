package com.irille.core.controller;

import java.util.List;

import irille.view.BaseView;

public class PageView<T extends BaseView> implements BaseView {

	private Integer total;
	private List<T> items;

	public PageView(Integer total, List<T> items) {
		super();
		this.total = total;
		this.items = items;
	}
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}
	
}
