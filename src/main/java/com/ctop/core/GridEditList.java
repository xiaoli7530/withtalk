package com.ctop.core;

import java.util.List;

/**
 * 用于接收界面上grid编辑的数据
 * @author wxy
 *
 * @param <E>
 */

public class GridEditList<E> {
	List<E> insertedData;
	List<E> updatedData;
	List<E> deletedData;
	
	public List<E> getInsertedData() {
		return insertedData;
	}
	public void setInsertedData(List<E> insertedData) {
		this.insertedData = insertedData;
	}
	public List<E> getUpdatedData() {
		return updatedData;
	}
	public void setUpdatedData(List<E> updatedData) {
		this.updatedData = updatedData;
	}
	public List<E> getDeletedData() {
		return deletedData;
	}
	public void setDeletedData(List<E> deletedData) {
		this.deletedData = deletedData;
	}
	
	
}
