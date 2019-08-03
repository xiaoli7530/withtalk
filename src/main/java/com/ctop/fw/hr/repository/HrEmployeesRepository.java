package com.ctop.fw.hr.repository;
 
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.fw.hr.entity.HrEmployees;
 

/** 
 */
public interface HrEmployeesRepository  extends JpaRepository<HrEmployees, String>, JpaSpecificationExecutor<HrEmployees> {
	
	/*
	 * 根据empCode查询
	 */
	@Query("select b from HrEmployees b where b.empCode=?1 and b.isActive='Y'")
	public List<HrEmployees> queryHrEmployeesByEmpCode(String empCode);
	
	@Query("select b from HrEmployees b where b.empCode=?1 and b.isActive='Y' and b.empUuid <> ?2")	
	public List<HrEmployees> queryHrEmployeesByEmp(String empCode,String empUuid);
	
	/*
	 * 根据empCode查询
	 */
	@Query("select b from HrEmployees b where b.departmentUuid = ?1 and b.isActive='Y'")
	public List<HrEmployees> findbyDepartmentUuid(String departmentUuid);
	
	@Query(nativeQuery = true, value = "select b.* from sys_account a,hr_employees b "
			+ "where a.ref_id=b.emp_uuid and a.account_uuid=?1 and a.IS_ACTIVE='Y' ")
	public List<HrEmployees> findByAccountUuid(String accountUuid);
	
	
	@Query(nativeQuery = true, value = "select s.* "
			+ "from hr_employees s "
			+ "where s.is_active = 'Y' "
			+ " and exists "
			+ "(select 1"
			+ " from sys_account a "
			+ "  where a.is_active = 'Y'"
			+ " and a.ref_id = s.emp_uuid"
			+ " and exists"
			+ " (select 1"
			+ "      from sys_account_role r"
			+ "    where r.is_active = 'Y'"
			+ "      and r.account_uuid = a.account_uuid"
			+ "      and exists (select 1"
			+ "              from sys_role sr"
			+ "             where sr.is_active = 'Y'"
			+ "               and sr.role_uuid = r.role_uuid"
			+ "               and sr.role_code = ?1)))")
	public List<HrEmployees> findHrEmpByRoleCode(String roleCode);
	
	@Query(nativeQuery=true, value="select t.* from HR_EMPLOYEES t left join hr_department d on d.department_no = t.ou  where t.emp_name= ?1 and t.is_active='Y' and d.is_active='Y'")
	public HrEmployees selectdeptcode(String login_name);
	
	
	@Query(nativeQuery=true, value="select he.* from hr_employees he join sys_account sa on (he.emp_uuid = sa.ref_id and sa.is_active = 'Y') where he.is_active = 'Y' and sa.account_uuid=?1")
	public HrEmployees selectByAccountUuid(String accountUuid);
	
	
	@Query(nativeQuery=true, value="select count(1) from hr_employees t where lower(t.emp_email) =?1 and t.is_active = 'Y'")
	public Long countByEmpEmail(String empEmail);
	
	
	@Query(nativeQuery=true, value="select t.* from hr_employees t where lower(t.emp_email) =?1 and t.is_active = 'Y'")
	public List<HrEmployees> queryHrEmployeesByEmail(String empEmail);
	
	
	@Query(nativeQuery=true, value="select t.* from hr_employees t where lower(t.emp_email) =?1 and t.is_active = 'Y'")
	public List<HrEmployees> queryByEmpEmail(String empEmail);
	
}
