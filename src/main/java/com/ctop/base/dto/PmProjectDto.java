package com.ctop.base.dto;

import com.ctop.fw.common.model.BaseDto;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 功能说明：PmProject实体的数据传输对像
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
public class PmProjectDto extends BaseDto implements Serializable {
	private String projectUuid;
	private String projectCode;
	private String projectName;
	private String projectType;
	private String projectManager;
	private String projectDesc;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date vtcDate;
	private String projectStatus;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private String projectCategory;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date beginDate;
	private String ownerUuid;
	private String ownerName;
	private String projectManagerUuid;
	private String projectChiefEngineerUuid;
	private String projectChiefEngineer;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date endDate;
	private String isTimsTbo;
	private String warehouseUuid;
	private String projectLevel;
	private String parentUuid;
	private String coordinationEngineerUuid;
	private String coordinationEngineer;
	private String dseName;
	private String dseUuid;
	private String visible;
	private String carType;
	private String carBrand;
	private String carRoof;
	private String carYear;
	private String projectPemId;
	private String projectPlant;
	private String projectGuid;
	private String kvdpProjectLevel;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date afiDate;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8") 
	private Date vpiDate;
	private String edvMode;
	private String vehicleType;

	private String empCode;
	private String empName;
	
	private String ppprName;
	private String phase;
	private String biwBomStatus;//BIW状态
	private String gaBomStatus;//GA状态
	private String ppprStatus;//PPPR状态
	private String ppprUuid;
	
	public String getProjectUuid() {
		return projectUuid;
	}

	public void setProjectUuid(String projectUuid) {
		this.projectUuid = projectUuid;
	}
	
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	
	public String getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}
	
	public String getProjectDesc() {
		return projectDesc;
	}

	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}
	
	public Date getVtcDate() {
		return vtcDate;
	}

	public void setVtcDate(Date vtcDate) {
		this.vtcDate = vtcDate;
	}
	
	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	
	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	
	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	
	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
	
	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}
	
	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}
	
	public String getProjectCategory() {
		return projectCategory;
	}

	public void setProjectCategory(String projectCategory) {
		this.projectCategory = projectCategory;
	}
	
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	public String getOwnerUuid() {
		return ownerUuid;
	}

	public void setOwnerUuid(String ownerUuid) {
		this.ownerUuid = ownerUuid;
	}
	
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public String getProjectManagerUuid() {
		return projectManagerUuid;
	}

	public void setProjectManagerUuid(String projectManagerUuid) {
		this.projectManagerUuid = projectManagerUuid;
	}
	
	public String getProjectChiefEngineerUuid() {
		return projectChiefEngineerUuid;
	}

	public void setProjectChiefEngineerUuid(String projectChiefEngineerUuid) {
		this.projectChiefEngineerUuid = projectChiefEngineerUuid;
	}
	
	public String getProjectChiefEngineer() {
		return projectChiefEngineer;
	}

	public void setProjectChiefEngineer(String projectChiefEngineer) {
		this.projectChiefEngineer = projectChiefEngineer;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getIsTimsTbo() {
		return isTimsTbo;
	}

	public void setIsTimsTbo(String isTimsTbo) {
		this.isTimsTbo = isTimsTbo;
	}
	
	public String getWarehouseUuid() {
		return warehouseUuid;
	}

	public void setWarehouseUuid(String warehouseUuid) {
		this.warehouseUuid = warehouseUuid;
	}
	
	public String getProjectLevel() {
		return projectLevel;
	}

	public void setProjectLevel(String projectLevel) {
		this.projectLevel = projectLevel;
	}
	
	public String getParentUuid() {
		return parentUuid;
	}

	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}
	
	public String getCoordinationEngineerUuid() {
		return coordinationEngineerUuid;
	}

	public void setCoordinationEngineerUuid(String coordinationEngineerUuid) {
		this.coordinationEngineerUuid = coordinationEngineerUuid;
	}
	
	public String getCoordinationEngineer() {
		return coordinationEngineer;
	}

	public void setCoordinationEngineer(String coordinationEngineer) {
		this.coordinationEngineer = coordinationEngineer;
	}
	
	public String getDseName() {
		return dseName;
	}

	public void setDseName(String dseName) {
		this.dseName = dseName;
	}
	
	public String getDseUuid() {
		return dseUuid;
	}

	public void setDseUuid(String dseUuid) {
		this.dseUuid = dseUuid;
	}
	
	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}
	
	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}
	
	public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	
	public String getCarRoof() {
		return carRoof;
	}

	public void setCarRoof(String carRoof) {
		this.carRoof = carRoof;
	}
	
	public String getCarYear() {
		return carYear;
	}

	public void setCarYear(String carYear) {
		this.carYear = carYear;
	}
	
	public String getProjectPemId() {
		return projectPemId;
	}

	public void setProjectPemId(String projectPemId) {
		this.projectPemId = projectPemId;
	}
	
	public String getProjectPlant() {
		return projectPlant;
	}

	public void setProjectPlant(String projectPlant) {
		this.projectPlant = projectPlant;
	}
	
	public String getProjectGuid() {
		return projectGuid;
	}

	public void setProjectGuid(String projectGuid) {
		this.projectGuid = projectGuid;
	}
	
	public String getKvdpProjectLevel() {
		return kvdpProjectLevel;
	}

	public void setKvdpProjectLevel(String kvdpProjectLevel) {
		this.kvdpProjectLevel = kvdpProjectLevel;
	}
	
	public Date getAfiDate() {
		return afiDate;
	}

	public void setAfiDate(Date afiDate) {
		this.afiDate = afiDate;
	}
	
	public Date getVpiDate() {
		return vpiDate;
	}

	public void setVpiDate(Date vpiDate) {
		this.vpiDate = vpiDate;
	}
	
	public String getEdvMode() {
		return edvMode;
	}

	public void setEdvMode(String edvMode) {
		this.edvMode = edvMode;
	}
	
	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getBiwBomStatus() {
		return biwBomStatus;
	}

	public void setBiwBomStatus(String biwBomStatus) {
		this.biwBomStatus = biwBomStatus;
	}

	public String getGaBomStatus() {
		return gaBomStatus;
	}

	public void setGaBomStatus(String gaBomStatus) {
		this.gaBomStatus = gaBomStatus;
	}

	public String getPpprStatus() {
		return ppprStatus;
	}

	public void setPpprStatus(String ppprStatus) {
		this.ppprStatus = ppprStatus;
	}

	public String getPpprName() {
		return ppprName;
	}

	public void setPpprName(String ppprName) {
		this.ppprName = ppprName;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getPpprUuid() {
		return ppprUuid;
	}

	public void setPpprUuid(String ppprUuid) {
		this.ppprUuid = ppprUuid;
	}

}

