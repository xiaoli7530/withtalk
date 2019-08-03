package com.ctop.fw.common.model;

import java.io.Serializable;

/**
 * sort: { field: "age", dir: "desc" }
 * @author 龚建军
 *
 */
public class SortField implements Serializable{
	private static final long serialVersionUID = -8545622873198824925L;
	private String field;
	private String dir;
	
	public SortField() {}
	
	public SortField(String field, String dir) {
		super();
		this.field = field;
		this.dir = dir;
	}



	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
}
