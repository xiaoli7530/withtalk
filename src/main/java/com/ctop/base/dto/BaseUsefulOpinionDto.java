package com.ctop.base.dto;

import com.ctop.fw.common.model.BaseDto;
import java.io.Serializable;

/**
 * <pre>
 * 功能说明：BaseUsefulOpinion实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class BaseUsefulOpinionDto extends BaseDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6399861550023029594L;
	private String uoId;
	private String remark;
	private String accountUuid;

	
	public String getUoId() {
		return uoId;
	}

	public void setUoId(String uoId) {
		this.uoId = uoId;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getAccountUuid() {
		return accountUuid;
	}

	public void setAccountUuid(String accountUuid) {
		this.accountUuid = accountUuid;
	}

}

