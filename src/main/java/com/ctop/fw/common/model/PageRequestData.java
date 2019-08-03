package com.ctop.fw.common.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

public interface PageRequestData {
	/**JPA DATA的分页用*/
	public <T> Specification<T> toSpecification(Class<T> domainClass);
	/**JPA DATA的分页用*/
	public PageRequest buildPageRequest();

	public Filter getFilter();
	public int getSkip();
	public int getPageIndex();
	public int getPageSize();
	public List<SortField> getSort();
	public void setSortField(String sortField);
	public void setSortOrder(String sortOrder);
	
	public Map<String,Object> getOtherFilter();
	/**
	 * 根据查询的field 查找对应的filter，如果不存在则返回null
	 * @param filterName
	 * @return
	 */
	public Filter getFilterByName(String filterName);
	/**
	 * 移除查询条件
	 * @param filterName
	 */
	public void removeFilterByName(String filterName);
}
