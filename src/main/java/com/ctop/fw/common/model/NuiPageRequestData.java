package com.ctop.fw.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.common.utils.StringUtil;

public class NuiPageRequestData implements PageRequestData, Serializable{
	private static final long serialVersionUID = 9210652516468320194L;
	private Filter filter = new Filter();
	private PageNum page = new PageNum();
	/**page , pageIndex*/
	private int pageIndex;
	private int pageSize;
	private String sortField;
	private String sortOrder;
	
	private Map<String,Object> otherFilter;
	
	private List<SortField> sorts = new ArrayList<SortField>();
	private boolean isAddSort = false; 
	private List<Filter> filterRules = new ArrayList<Filter>();
	
	public List<SortField> getSort() {
		if(!isAddSort 
				&& StringUtil.isNotEmpty(this.sortField) 
				&& StringUtil.isNotEmpty(this.sortOrder)){
			sorts.add(new SortField(this.sortField,this.sortOrder));
			isAddSort = true;
		}
		return sorts;
	}
	
	public Filter buildFilter4FilterRules() {
		Filter filter = new Filter();
		filter.setLogic("and");
		filter.setFilters(this.filterRules);
		return filter;
	}
		
	/**
	 * 将kendo的从前台传过来的参数转换成对象
	 * 
	 * @param flatMap
	 * @return
	 */
	public static KendoPageRequestData buildFromFlatMap(Map<String, Object> flatMap) {
		KendoPageRequestData pageRequest = new KendoPageRequestData();
		BeanWrapper bw = new BeanWrapperImpl(pageRequest);
		bw.setAutoGrowCollectionLimit(10);
		bw.setAutoGrowNestedPaths(true);
		for (Map.Entry<String, Object> entry : flatMap.entrySet()) {
			setPageRequestProperty(bw, entry.getKey(), entry.getValue());
		}
		return pageRequest;
	}

	private static void setPageRequestProperty(BeanWrapper bw, String flatKey, Object value) {
		String key = convertFlatKey(flatKey);
		if (bw.isWritableProperty(key)) {
			bw.setPropertyValue(key, value);
		}
	}

	private static String convertFlatKey(String key) {
		StringBuilder builder = new StringBuilder();
		boolean isObjectProperty = true;
		for (int i = 0; i < key.length() - 1; i++) {
			char c = key.charAt(i);
			char next = key.charAt(i + 1);
			if (c == '[') {
				isObjectProperty = !Character.isDigit(next);
			}
			if (isObjectProperty && (c == '[')) {
				builder.append('.');
				continue;
			}
			if (isObjectProperty && (c == ']')) {
				continue;
			}
			builder.append(c);
			if ((i + 1) == key.length() - 1 && (!isObjectProperty || next != ']')) {
				builder.append(next);
			}
		}
		return builder.toString();
	}

	public <T> Predicate buildPridicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		Predicate isActive = cb.equal(root.get("isActive"), "Y");
		if (this.filter != null) {
			Predicate p1 = this.filter.toPredicate(root, query, cb);
			if (p1 != null) {
				isActive = cb.and(isActive, p1);
			} 
		}
		query.where(isActive);
		List<SortField> sortFields = this.getSort();
		if (sortFields != null) {
			for(SortField sort: sortFields) {
				if ("desc".equals(sort.getDir())) {
					query.orderBy(cb.desc(root.get(sort.getField())));
				}
				if ("asc".equals(sort.getDir())) {
					query.orderBy(cb.asc(root.get(sort.getField())));
				}
			}
		}
		return query.getRestriction(); 
	}
 
	public <T> Specification<T> toSpecification(Class<T> domainClass) {
		return (Specification<T>) new Specification<T>() {

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return NuiPageRequestData.this.buildPridicate(root, query, cb);
			}

		};
	}

	public PageRequest buildPageRequest() {
		int pageIndex = this.pageIndex;
		List<SortField> sortFields = this.getSort();
		if(sortFields != null && sortFields.size() > 0) {
			List<Order> orders = new ArrayList<Order>();
			for(SortField item: sortFields) {
				Direction dir = "asc".equals(item.getDir() ) ? Direction.DESC : Direction.ASC; 
				orders.add(new Order(dir, item.getField()));
			}
			org.springframework.data.domain.Sort sort = new org.springframework.data.domain.Sort(orders);
			return new PageRequest(pageIndex, this.pageSize, sort);
		} else {
			return new PageRequest(pageIndex, this.pageSize);
		}
	}

	@Override
	public Filter getFilterByName(String filterName) {
		List<Filter> list = this.getFilter().getFilters();
		if (ListUtil.isEmpty(list)) {
			return null;
		}
		for (Filter filter : list) {
			if (StringUtil.equals(filterName, filter.getField())) {
				return filter;
			}
		}
		return null;
	}
	
	public void removeFilterByName(String filterName){
		List<Filter> list = this.getFilter().getFilters();
		if (ListUtil.isEmpty(list)) {
			return ;
		}
		for(int i=0;i<list.size();i++) {
			if (StringUtil.equals(filterName, list.get(i).getField())) {
				list.remove(i);
				return ;
			}
		}
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public PageNum getPage() {
		return page;
	}

	public void setPage(PageNum page) {
		this.page = page;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public class PageNum implements Serializable{
		private static final long serialVersionUID = 2831453840325914604L;
		public PageNum() {
			
		}
		
		private int begin;
		private int length;
		public int getBegin() {
			return begin;
		}
		public void setBegin(int begin) {
			this.begin = begin;
		}
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
	}

	@Override
	public int getSkip() {
		return this.page.begin;
	}

	public List<Filter> getFilterRules() {
		return filterRules;
	}

	public void setFilterRules(List<Filter> filterRules) {
		this.filterRules = filterRules;
	}

	public Map<String, Object> getOtherFilter() {
		return otherFilter;
	}

	public void setOtherFilter(Map<String, Object> otherFilter) {
		this.otherFilter = otherFilter;
	}
	
	
}
