package com.ctop.fw.common.model;

import java.util.List;

import org.springframework.data.domain.Page;

public class PageResponseData<T> {
	
	private List<T> data;
	private int totalPages;
	private long total;
	private int page;
	private int pageSize;
	
	public PageResponseData() {
		
	}
	
	public PageResponseData(Page<T> pagable) {
		this.data = pagable.getContent();
		this.totalPages = pagable.getTotalPages();
		this.total = pagable.getTotalElements();
		this.page = pagable.getNumber() + 1; 
		this.pageSize = pagable.getSize();
	}
	
	public PageResponseData(PageRequestData request, int totalCount, List<T> data) {
		this.data = data;
		this.total = totalCount;
		this.page = request.getPageIndex();
		this.pageSize = request.getPageSize();
		this.totalPages = (int) Math.ceil( ((double)totalCount)/this.pageSize);
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
