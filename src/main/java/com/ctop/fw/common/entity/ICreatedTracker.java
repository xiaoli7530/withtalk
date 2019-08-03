package com.ctop.fw.common.entity;

import java.util.Date;

/**
 * 
 * @author 龚建军
 *
 */
public interface ICreatedTracker {

	public Date getCreatedDate();

	public void setCreatedDate(Date createdDate);

	public String getCreatedBy();

	public void setCreatedBy(String createdBy);

	public String getIsActive();

	public void setIsActive(String isActive);
}
