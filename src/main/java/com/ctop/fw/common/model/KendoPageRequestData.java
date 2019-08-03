package com.ctop.fw.common.model;

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

public class KendoPageRequestData implements PageRequestData{
	private Filter filter = new Filter();
	private int top;
	private int skip;
	private int page;
	private int pageSize;
	
	private Map<String,Object> otherFilter;

	private List<SortField> sort = new ArrayList<SortField>();
	
	public int getPageIndex() {
		return this.page;
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
		if (this.filter != null) {
			Predicate p1 = this.filter.toPredicate(root, query, cb);
			if (p1 != null) {
				query.where(p1);
			}
		}
		if (this.sort != null) {
			for(SortField sort: this.sort) {
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
				return KendoPageRequestData.this.buildPridicate(root, query, cb);
			}

		};
	}

	public PageRequest buildPageRequest() {
		int page = this.page - 1;
		if(this.sort != null && this.sort.size() > 0) {
			List<Order> orders = new ArrayList<Order>();
			for(SortField item: this.sort) {
				Direction dir = "asc".equals(item.getDir() ) ? Direction.DESC : Direction.ASC; 
				orders.add(new Order(dir, item.getField()));
			}
			org.springframework.data.domain.Sort sort = new org.springframework.data.domain.Sort(orders);
			return new PageRequest(page, this.pageSize, sort);
		} else {
			return new PageRequest(page, this.pageSize);
		}
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getSkip() {
		return skip;
	}

	public void setSkip(int skip) {
		this.skip = skip;
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

	public List<SortField> getSort() {
		return sort;
	}

	public void setSort(List<SortField> sort) {
		this.sort = sort;
	}

	@Override
	public void setSortField(String sortField) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSortOrder(String sortOrder) {
		// TODO Auto-generated method stub
		
	}

	public Map<String, Object> getOtherFilter() {
		return otherFilter;
	}

	public void setOtherFilter(Map<String, Object> otherFilter) {
		this.otherFilter = otherFilter;
	}

	@Override
	public Filter getFilterByName(String filterName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeFilterByName(String filterName) {
		// TODO Auto-generated method stub
		
	}

}
