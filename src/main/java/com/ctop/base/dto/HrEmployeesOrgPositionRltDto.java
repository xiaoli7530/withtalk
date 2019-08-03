package com.ctop.base.dto;

import com.ctop.fw.common.model.BaseDto;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：HrEmployeesOrgPositionRlt实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class HrEmployeesOrgPositionRltDto extends BaseDto implements Serializable {
	private String rltUuid;
	private String empUuid;
	private String hrEmplId;
	private String positionNbr;
	private String positionInfoUuid;
	private String departmentUuid;
	private String hrDeptSetId;
	private String positionNbrEpms;

	
	public String getRltUuid() {
		return rltUuid;
	}

	public void setRltUuid(String rltUuid) {
		this.rltUuid = rltUuid;
	}
	
	public String getEmpUuid() {
		return empUuid;
	}

	public void setEmpUuid(String empUuid) {
		this.empUuid = empUuid;
	}
	
	public String getHrEmplId() {
		return hrEmplId;
	}

	public void setHrEmplId(String hrEmplId) {
		this.hrEmplId = hrEmplId;
	}
	
	public String getPositionNbr() {
		return positionNbr;
	}

	public void setPositionNbr(String positionNbr) {
		this.positionNbr = positionNbr;
	}
	
	public String getPositionInfoUuid() {
		return positionInfoUuid;
	}

	public void setPositionInfoUuid(String positionInfoUuid) {
		this.positionInfoUuid = positionInfoUuid;
	}
	
	public String getDepartmentUuid() {
		return departmentUuid;
	}

	public void setDepartmentUuid(String departmentUuid) {
		this.departmentUuid = departmentUuid;
	}
	
	public String getHrDeptSetId() {
		return hrDeptSetId;
	}

	public void setHrDeptSetId(String hrDeptSetId) {
		this.hrDeptSetId = hrDeptSetId;
	}
	
	public String getPositionNbrEpms() {
		return positionNbrEpms;
	}

	public void setPositionNbrEpms(String positionNbrEpms) {
		this.positionNbrEpms = positionNbrEpms;
	}

}

