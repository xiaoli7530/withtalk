package com.ctop.base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.springframework.format.annotation.DateTimeFormat;

import com.ctop.fw.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * <pre>
 * 功能说明：${table.className}实体类
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
@SuppressWarnings("serial") 
@Entity
@Table(name = "PM_PROJECT")
@BatchSize(size = 20)
public class PmProject extends BaseEntity implements Serializable {

	@Id
	@Column(name = "PROJECT_UUID")
	private String projectUuid;//主键

	@Column(name = "PROJECT_CODE")
	private String projectCode;//项目代码

	@Column(name = "PROJECT_NAME")
	private String projectName;//项目名称

	@Column(name = "PROJECT_TYPE")
	private String projectType;//项目类型（PS/EDV）

	@Column(name = "PROJECT_MANAGER")
	private String projectManager;//项目经理

	@Column(name = "PROJECT_DESC")
	private String projectDesc;//项目描述

	@Column(name = "VTC_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date vtcDate;//VTC时间

	@Column(name = "PROJECT_STATUS")
	private String projectStatus;//状态

	@Column(name = "EXT1")
	private String ext1;//扩展字段1

	@Column(name = "EXT2")
	private String ext2;//扩展字段2

	@Column(name = "EXT3")
	private String ext3;//扩展字段3

	@Column(name = "EXT4")
	private String ext4;//扩展字段4

	@Column(name = "EXT5")
	private String ext5;//扩展字段5

	@Column(name = "PROJECT_CATEGORY")
	private String projectCategory;//产品分类（发动机/变速箱/电池包）

	@Column(name = "BEGIN_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date beginDate;//开始时间

	@Column(name = "OWNER_UUID")
	private String ownerUuid;//物料工程师UUID

	@Column(name = "OWNER_NAME")
	private String ownerName;//物料工程师

	@Column(name = "PROJECT_MANAGER_UUID")
	private String projectManagerUuid;//项目经理UUID

	@Column(name = "PROJECT_CHIEF_ENGINEER_UUID")
	private String projectChiefEngineerUuid;//项目总工UUID

	@Column(name = "PROJECT_CHIEF_ENGINEER")
	private String projectChiefEngineer;//项目总工

	@Column(name = "END_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date endDate;//结束时间

	@Column(name = "IS_TIMS_TBO")
	private String isTimsTbo;//是否TiMS-TBO

	@Column(name = "WAREHOUSE_UUID")
	private String warehouseUuid;//实验室对应仓库

	@Column(name = "PROJECT_LEVEL")
	private String projectLevel;//项目级别

	@Column(name = "PARENT_UUID")
	private String parentUuid;//主项目UUID

	@Column(name = "COORDINATION_ENGINEER_UUID")
	private String coordinationEngineerUuid;//协调工程师UUID

	@Column(name = "COORDINATION_ENGINEER")
	private String coordinationEngineer;//协调工程师

	@Column(name = "DSE_NAME")
	private String dseName;//系统工程师（DSE）

	@Column(name = "DSE_UUID")
	private String dseUuid;//系统工程师uuid（DSE）

	@Column(name = "VISIBLE")
	private String visible;//是否显示

	@Column(name = "CAR_TYPE")
	private String carType;//车辆类型

	@Column(name = "CAR_BRAND")
	private String carBrand;//车辆品牌

	@Column(name = "CAR_ROOF")
	private String carRoof;//车型平台

	@Column(name = "CAR_YEAR")
	private String carYear;//车型年

	@Column(name = "PROJECT_PEM_ID")
	private String projectPemId;//项目负责人

	@Column(name = "PROJECT_PLANT")
	private String projectPlant;//制造工厂

	@Column(name = "PROJECT_GUID")
	private String projectGuid;//项目唯一标识符

	@Column(name = "KVDP_PROJECT_LEVEL")
	private String kvdpProjectLevel;//更改级别（后续需要添加的内容）

	@Column(name = "AFI_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date afiDate;//AFI时间

	@Column(name = "VPI_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date vpiDate;//VPI时间

	@Column(name = "EDV_MODE")
	private String edvMode;//造车动作

	@Column(name = "VEHICLE_TYPE")
	private String vehicleType;//车辆类型（suv/mpv/bridge）

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
	
}

