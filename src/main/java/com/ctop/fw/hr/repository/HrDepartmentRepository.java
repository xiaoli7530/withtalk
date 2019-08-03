package com.ctop.fw.hr.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.fw.hr.dto.HrDepartmentDto;
import com.ctop.fw.hr.entity.HrDepartment;


 

/** 
 */
public interface HrDepartmentRepository  extends JpaRepository<HrDepartment, String>, JpaSpecificationExecutor<HrDepartment> {
	@Query(nativeQuery = true, value = "select count(1) from HR_DEPARTMENT a where  a.DEPARTMENT_UUID != ?1 and a.DEPARTMENT_NO = ?2 and a.IS_ACTIVE='Y' ")
	public long countSameDepartmentNo(String departmentUuid, String departmentNo);
	
	
	@Query(nativeQuery = true, value = "select count(1) from HR_DEPARTMENT a where a.PARENT_UUID is null and a.DEPARTMENT_UUID != ?1 and a.SEQ_NO = ?2 and a.IS_ACTIVE='Y' ")
	public long countSameSeqNo(String departmentUuid, Long seqNo);
	
	@Query(nativeQuery = true, value = "select count(1) from HR_DEPARTMENT a where  a.PARENT_UUID = ?1 and a.DEPARTMENT_UUID != ?2 and a.SEQ_NO = ?3 and a.IS_ACTIVE='Y' ")
	public long countSameSeqNo(String parentUuid, String departmentUuid, Long seqNo);
	
	@Query(nativeQuery = true, value = "select count(1) from HR_DEPARTMENT a where a.PARENT_UUID = ?1 and a.IS_ACTIVE='Y' ")
	public long ishaveSon(String parentUuid);
	
	@Query("select b from HrDepartment b where b.hrDeptSetId = ?1 and b.isActive='Y' ")
	public HrDepartment findByHrDeptSetId(String hrDeptSetId);
	
	@Query("select b from HrDepartment b where b.hrDeptSetId = ?1 and b.isActive='Y' ")
	public HrDepartment findByHrSetId(String hrDeptSetId);
	
	@Query("select nvl(max(a.seqNo),0) from HrDepartment a where a.isActive='Y' ")
	public long queryMaxSeqNo();
	
	@Query(nativeQuery=true, value="select d.* from  hr_department d where d.department_uuid = ?1 and d.is_active='Y'")
	public HrDepartment selectdept(String deptName);
	
	@Query("select b from HrDepartment b where b.departmentNo = ?1 and b.isActive='Y' ")
	public HrDepartment findByDeptCode(String deptCode);
	
	@Query(nativeQuery=true,value="select * from HR_DEPARTMENT WHERE DEPT_LEVL = '1' and is_active = 'Y' CONNECT BY PRIOR parent_uuid=department_uuid START WITH department_uuid=?1")
	public HrDepartment findLevelOne(String deptId);
}
