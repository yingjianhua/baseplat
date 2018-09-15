package irille.action;

import java.util.Vector;

public class Page {
	// 1.每页显示数量(everyPage)
	private int numPerPage;
	// 2.总记录数(totalCount)
	private int totalCount;
	// 3.总页数(totalPage)
	private int totalPage;
	// 4.当前页(currentPage)
	private int pageNum;
	// 5.起始点(beginIndex)
	private int beginIndex;
	// 6.是否有上一页(hasPrePage)
	private boolean hasPrePage;
	// 7.是否有下一页(hasNextPage)
	private boolean hasNextPage;

	private int yCount = 10;

	public Page(int everyPage, int totalCount, int totalPage, int currentPage, int beginIndex,
	    boolean hasPrePage, boolean hasNextPage) {
		this.numPerPage = everyPage;
		this.totalCount = totalCount;
		this.totalPage = totalPage;
		this.pageNum = currentPage;
		this.beginIndex = beginIndex;
		this.hasPrePage = hasPrePage;
		this.hasNextPage = hasNextPage;
	}

	// 构造函数，默认
	public Page() {
	}

	// 构造方法，对所有属性进行设置

	public int getTotalCount() {
		return totalCount;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {

		this.pageNum = pageNum;
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

	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public boolean isHasPrePage() {
		return pageNum > 1;
	}

	public void setHasPrePage(boolean hasPrePage) {
		this.hasPrePage = hasPrePage;
	}

	public boolean isHasNextPage() {
		return pageNum < totalPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public int getyCount() {
		return yCount;
	}

	public void setyCount(int yCount) {
		this.yCount = yCount;
	}

	public boolean isHasPreYe() {
		return pageNum > yCount;
	}

	public boolean isHasNextYe() {
		return ((pageNum - 1) / yCount + 1) * yCount < totalPage;
	}

	public Vector<Integer> getPageNums() {
		Vector<Integer> pageNums = new Vector<Integer>();
		for (int i = 1; i <= yCount; i++) {
			int num = ((pageNum - 1) / yCount) * yCount + i;
			if (num > totalPage)
				break;
			pageNums.add(num);
		}
		return pageNums;
	}

	public int getNextYe() {
		return ((pageNum - 1) / yCount + 1) * yCount + 1;
	}
	
	public int getPreYe() {
		return ((pageNum - 1) / yCount - 1) * yCount + 1;
	}
}
