//Created on 2005-10-15
package irille.pub;

import java.io.Serializable;

/**
 * Title: 分页对象<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class Page implements Serializable {
	private transient Object _thisPageElements;
	private int _pageSize;
	private int _pageNumber;
	private int _totalElements;
	private String _actCode;
	private String _hql;

	public String getHql() {
		return _hql;
	}

	public void setHql(String hql) {
		_hql = hql;
	}

	/**
	 * 构造方法
	 * @param pageNumber 当前页编码，从1开始，如果传的值为Integer.MAX_VALUE表示获取最后一页。 
	 *                   如果你不知道最后一页编码，传Integer.MAX_VALUE即可。如果当前页超过总页数，也表示最后一页。 
	 * @param pageSize  每一页显示的条目数 
	 * @param totalElements 总记录
	 * @param thisPageElements 元素
	 */
	public Page(int pageNumber, int pageSize, int totalElements,
			Object thisPageElements) {
		this(pageNumber, pageSize, totalElements, thisPageElements, null);
	}

	public Page(int pageNumber, int pageSize, int totalElements,
			Object thisPageElements, String actCode) {
		this._pageNumber = pageNumber;
		this._pageSize = pageSize;
		this._totalElements = totalElements;
		this._thisPageElements = thisPageElements;
		this._actCode = actCode;
		recomputePageNumber();
	}

	/** 
	 * 重新计算页码 
	 */
	public void recomputePageNumber() {
		if (Integer.MAX_VALUE == this._pageNumber
				|| this._pageNumber > getLastPageNumber()) { //last page 
			this._pageNumber = getLastPageNumber();
		}
	}

	/**
	 * 是否为第1页
	 * @return 结果
	 */
	public boolean isFirstPage() {
		return getThisPageNumber() == 1;
	}

	/**
	 * 是否为最后一页
	 * @return 结果
	 */
	public boolean isLastPage() {
		return getThisPageNumber() >= getLastPageNumber();
	}

	/**
	 * 是否还有下一页
	 * @return 结果
	 */
	public boolean hasNextPage() {
		return getLastPageNumber() > getThisPageNumber();
	}

	/**
	 * 是否还有前一页
	 * @return 结果
	 */
	public boolean hasPreviousPage() {
		return getThisPageNumber() > 1;
	}

	/**
	 * 取最后一页页号
	 * @return 结果
	 */
	public int getLastPageNumber() {
		return (_totalElements + _pageSize - 1) / this._pageSize;
	}

	/**
	 * 取本页记录数
	 * @return 结果, List Or Other
	 */
	public Object getThisPageElements() {
		return _thisPageElements;
	}

	/**
	 * 取总记录数
	 * @return 结果（从1开始算起）
	 */
	public int getTotalNumberOfElements() {
		return _totalElements;
	}

	/**
	 * 取当前页的第一条记录序号
	 * @return 结果（从1开始算起）
	 */
	public int getThisPageFirstElementNumber() {
		return (getThisPageNumber() - 1) * getPageSize() + 1;
	}

	/**
	 * 取当前页的最后一条序号
	 * @return 结果（从1开始算起）
	 */
	public int getThisPageLastElementNumber() {
		int fullPage = getThisPageFirstElementNumber() + getPageSize() - 1;
		return getTotalNumberOfElements() < fullPage ? getTotalNumberOfElements()
				: fullPage;
	}

	/**
	 * 取下一页
	 * @return 结果
	 */
	public int getNextPageNumber() {
		return getThisPageNumber() + 1;
	}

	/**
	 * 取上一页
	 * @return 结果
	 */
	public int getPreviousPageNumber() {
		return getThisPageNumber() - 1;
	}

	/**
	 * 取页大小
	 * @return 结果
	 */
	public int getPageSize() {
		return _pageSize;
	}

	/**
	 * 取当前页
	 * @return 结果
	 */
	public int getThisPageNumber() {
		return _pageNumber;
	}

	/**
	 * 置thisPageElements的值
	 * @param thisPageElements 新值
	 */
	public void setThisPageElements(Object thisPageElements) {
		this._thisPageElements = thisPageElements;
	}

	/**
	 * 取actCode
	 * @return actCode
	 */
	public String getActCode() {
		return this._actCode;
	}

	/**
	 * 置actCode的值
	 * @param actCode 新值
	 */
	public void setActCode(String actCode) {
		this._actCode = actCode;
	}

	public void setPageSize(int pageSize) {
		this._pageSize = pageSize;
	}
	
	
}
