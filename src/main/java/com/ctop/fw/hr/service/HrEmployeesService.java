package com.ctop.fw.hr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.HrEmployeesOrgPositionRltDto;
import com.ctop.base.service.BasePinyinService;
import com.ctop.base.service.HrEmployeesOrgPositionRltService;
import com.ctop.fw.common.model.ComboTreeDto;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilder.Condition;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.hr.dto.HrDepartmentDto;
import com.ctop.fw.hr.dto.HrEmployeesDto;
import com.ctop.fw.hr.entity.HrEmployees;
import com.ctop.fw.hr.repository.HrEmployeesRepository;
import com.ctop.fw.sys.dto.SysRemindConfigDto;
import com.ctop.fw.sys.dto.SysRemindUser;
import com.ctop.fw.sys.repository.SysRoleRepository;
import com.ctop.fw.sys.service.SysAccountService;


/**
 * <pre>
 * 功能说明：
 * 示例程序如下：
 * 创建者：${user}
 * 创建时间：${date}
 * 版本：${version}
 * </pre>
 */
@Service
@Transactional
public class HrEmployeesService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private HrEmployeesRepository hrEmployeesRepository; 
	@Autowired
	HrDepartmentService hrDepartmentService;
	@Autowired
	private BasePinyinService basePinyinService;
	@Autowired
	private SysAccountService sysAccountService;
	@Autowired
	private SysRoleRepository sysRoleRepository;
	
	@Autowired
	private HrEmployeesOrgPositionRltService hrEmployeesOrgPositionRltService;
	
	@Transactional(readOnly=true)
	public HrEmployeesDto getById(String id) {
		HrEmployees hrEmployees = this.hrEmployeesRepository.findOne(id);
		return CommonAssembler.assemble(hrEmployees, HrEmployeesDto.class);
	}
	
	
	@Transactional(readOnly=true)
	public HrEmployees getByAvicpsncode(String avicpsncode) {
		HrEmployees example = new HrEmployees();
		example.setIsActive("Y");
		//example.setAvicpsnCode(avicpsncode);
		HrEmployees emp = this.hrEmployeesRepository.findOne(Example.of(example));
		return emp;
	}
	
	@Transactional
	public HrEmployeesDto addHrEmployees(HrEmployeesDto hrEmployeesDto) {
		if(!this.isExistsEmpCode(hrEmployeesDto.getEmpCode(), "")){
			throw new BusinessException("员工编码已经存在，添加失败!");
		}
		String empEmail = hrEmployeesDto.getEmpEmail();
		if(StringUtil.isNotEmpty(empEmail)) {
			if(this.countByEmpEmail(empEmail.toLowerCase(), null) > 0) {
				throw new BusinessException("email已存在！");
			}
			hrEmployeesDto.setEmpEmail(empEmail.toLowerCase());
		}
		HrEmployees hrEmployees = CommonAssembler.assemble(hrEmployeesDto, HrEmployees.class);
		if(StringUtil.isEmpty(hrEmployees.getEmpUuid())) {
			hrEmployees.setEmpUuid(StringUtil.getUuid());
		}
		hrEmployees.setPinyin(this.basePinyinService.generatePinyin(hrEmployees.getEmpName()));
		hrEmployees = this.hrEmployeesRepository.save(hrEmployees);
		return CommonAssembler.assemble(hrEmployees, HrEmployeesDto.class);
	} 

	@Transactional
	public HrEmployeesDto updateHrEmployees(HrEmployeesDto hrEmployeesDto,Set<String> updatedProperties) {
		if(!this.isExistsEmpCode(hrEmployeesDto.getEmpCode(), hrEmployeesDto.getEmpUuid())){
			throw new BusinessException("员工编码已经存在，更新失败!");
		}
		String empEmail = hrEmployeesDto.getEmpEmail();
		if(StringUtil.isNotEmpty(empEmail)) {
			if(this.countByEmpEmail(empEmail.toLowerCase(), hrEmployeesDto.getEmpUuid()) > 0) {
				throw new BusinessException("email已存在！");
			}
			hrEmployeesDto.setEmpEmail(empEmail.toLowerCase());
		}
		
		HrEmployees hrEmployees = this.hrEmployeesRepository.findOne(hrEmployeesDto.getEmpUuid());
		CommonAssembler.assemble(hrEmployeesDto, hrEmployees,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		hrEmployees.setPinyin(this.basePinyinService.generatePinyin(hrEmployees.getEmpName()));
		hrEmployees = this.hrEmployeesRepository.save(hrEmployees);
		return CommonAssembler.assemble(hrEmployees, HrEmployeesDto.class);
	}
	
	@Transactional
	public void deleteHrEmployees(String id) {
		HrEmployees hrEmployees = this.hrEmployeesRepository.findOne(id);
		hrEmployees.setIsActive("N");
		this.hrEmployeesRepository.save(hrEmployees);
	}
	
	@Transactional(readOnly=true)
	public long countByEmpEmail(String empEmail, String empUuid) {
		StringBuffer sql = new StringBuffer();
		sql.append("  ");
		sql.append(" select count(1) ");
		sql.append("   from hr_employees t ");
		sql.append("  where t.is_active = 'Y' ");
		sql.append("    and t.emp_email =:empEmail ");
		if(StringUtil.isNotEmpty(empUuid)) {
			sql.append("    and t.emp_uuid <>:empUuid ");
		}
		
		Session session = this.entityManager.unwrap(Session.class);
		SQLQuery query = session.createSQLQuery(sql.toString());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("empEmail", empEmail);
		if(StringUtil.isNotEmpty(empUuid)) {
			params.put("empUuid", empUuid);
		}
		query.setProperties(params);
		long count = Long.valueOf(query.uniqueResult().toString());
		
		return count;
	}
		
	@Transactional
	public void deleteHrEmployeess(List<String> empUuids) {
		Iterable<HrEmployees> hrEmployeess = this.hrEmployeesRepository.findAll(empUuids);
		Iterator<HrEmployees> it = hrEmployeess.iterator();
		while(it.hasNext()) {
			HrEmployees hrEmployees = it.next();
			hrEmployees.setIsActive("N");
			this.hrEmployeesRepository.save(hrEmployees);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	@Transactional(readOnly=true)
	public PageResponseData<HrEmployees> pageQuery(PageRequestData request) {
		Specification<HrEmployees> spec = request.toSpecification(HrEmployees.class);
		Page<HrEmployees> page = hrEmployeesRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<HrEmployees>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<HrEmployeesDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");	
		sql.appendField("h.DEPARTMENT_NAME", "departmentName",",");
		sql.appendTableColumns(HrEmployees.class, "t");
		sql.append(" from HR_EMPLOYEES t ");
		sql.append(" left join hr_department h on t.DEPARTMENT_UUID = h.DEPARTMENT_UUID ");
		sql.append(" where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, HrEmployeesDto.class);
	}
	
	@Transactional(readOnly=true)
	public List<HrEmployeesDto> getHrEmployeeAll(String q,String defaultPeople) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("t.DEPARTMENT_NAME", "departmentName",",");
		sql.appendTableColumns(HrEmployees.class, "t");
		sql.append(" from ( select  h.DEPARTMENT_NAME,t.* ");
		sql.append("  from HR_EMPLOYEES t ");
		sql.append(" left join hr_department h on t.DEPARTMENT_UUID = h.DEPARTMENT_UUID");
		sql.append(" where t.IS_ACTIVE='Y' and h.is_active = 'Y' ");
		sql.append("  and not exists (select 1 from sys_account sa where sa.is_active = 'Y' and t.emp_uuid = sa.ref_id) ");
		if(StringUtil.isNotEmpty(defaultPeople)){
			sql.append("  union select h.DEPARTMENT_NAME, t.* from hr_employees t ");
			sql.append(" 	left join hr_department h on t.DEPARTMENT_UUID = h.DEPARTMENT_UUID ");
			sql.append(" 	 where t.is_active = 'Y' ");
			sql.andEqual("t.emp_uuid", defaultPeople);
		}
		sql.append("  )t where 1=1 ");
		if (StringUtil.isNotEmpty(q)) {
			sql.append(sql.or(sql.contains("t.EMP_CODE", q), sql.contains("t.EMP_NAME", q)));
		}
		return sql.query(entityManager, HrEmployeesDto.class);
	}
	public List<ComboTreeDto> selectEmployeesTree(){
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("t.parentId", "parentId",",");
		sql.appendField("t.text", "text",",");
		sql.appendField("'true'", "state",",");
		sql.appendField("t.id", "id");
		sql.append("from (select '' as parentId,d.DEPARTMENT_NAME as text,d.DEPARTMENT_UUID as id ");
		sql.append(" from HR_DEPARTMENT d ");
		sql.append(" where d.IS_ACTIVE='Y'");
		sql.append(" union  ");
		sql.append("select e.DEPARTMENT_UUID as parentId,e.EMP_NAME as text,e.EMP_UUID as id");
		sql.append(" from HR_EMPLOYEES e ");
		sql.append(" where e.IS_ACTIVE='Y') t where 1=1 ");
		if(!UserContextUtil.currentUserIsSuperAdmin()){
			sql.andEqual("t.DEPARTMENT_UUID", UserContextUtil.getCompanyUuid());
		}
		return sql.query(entityManager, ComboTreeDto.class);
	}
	@Transactional(readOnly=true)
	public List<HrEmployees> queryHrEmployeesByRoleUuidsAndDeptUuid(List<String> roleUuids,String departmentUuid) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(HrEmployees.class, "h");
		sql.append(" FROM HR_EMPLOYEES h JOIN HR_DEPARTMENT d on h.DEPARTMENT_UUID = d.DEPARTMENT_UUID                 ");
		sql.append("and d.IS_ACTIVE='Y' where h.IS_ACTIVE='Y'  and EXISTS (select 1 from SYS_ACCOUNT s                         ");
		sql.append("left join SYS_ACCOUNT_ROLE r on s.ACCOUNT_UUID = r.ACCOUNT_UUID and r.IS_ACTIVE='Y' where s.IS_ACTIVE='Y'  ");
		sql.append(sql.in("r.ROLE_UUID", roleUuids));
		sql.append(" and s.REF_ID = h.emp_uuid) ");
		sql.andEqual(" h.DEPARTMENT_UUID", departmentUuid);
		sql.append(" CONNECT BY d.parent_uuid = d.department_uuid ");
		return sql.query(entityManager, HrEmployees.class);
	}
	
	
	/**
	 * 分页查询
	 * @param combogrid的下拉项，q是查询条件
	 * @author zzy
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<HrEmployeesDto> getHrEmployee(String q,String isDept) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("h.DEPARTMENT_NAME", "departmentName",",");
		sql.appendTableColumns(HrEmployees.class, "t");
		sql.append(" from HR_EMPLOYEES t ");
		sql.append(" left join hr_department h on t.DEPARTMENT_UUID = h.DEPARTMENT_UUID");
		sql.append(" where t.IS_ACTIVE='Y' and h.is_active = 'Y' ");
		if(StringUtil.isNotEmpty(isDept) && !("GS").equals(UserContextUtil.getDepartmentType())){
			if(isDept.length() == 32){
				//sql.append(" and h.DEPARTMENT_UUID = '"+isDept+"'");
				sql.append(" and h.department_uuid in (  select t.department_uuid    from hr_department t   start with t.department_uuid = '"+isDept+"' connect by prior t.department_uuid = t.parent_uuid)");
			}else{
				//sql.append(" and h.DEPARTMENT_UUID = '"+UserContextUtil.getDepartmentUuid()+"'");
				sql.append(" and h.department_uuid in (  select t.department_uuid    from hr_department t   start with t.department_uuid = '"+UserContextUtil.getDepartmentUuid()+"' connect by prior t.department_uuid = t.parent_uuid)");
			}
		}
		if (StringUtil.isNotEmpty(q)) {
			sql.append(sql.or(sql.contains("t.AVICPSNCODE", q), sql.contains("t.EMP_NAME", q),
					sql.contains("t.EMP_SEX", q), sql.contains("h.DEPARTMENT_NAME", q)));
		}		
		return sql.query(entityManager, HrEmployeesDto.class);
	}
	
	@Transactional(readOnly=true)
	public List<HrEmployeesDto> getHrEmployee2(String q,String departmentUuid) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(HrEmployees.class, "t");
		sql.append(" from HR_EMPLOYEES t ");		
		sql.append(" left join hr_department h on t.DEPARTMENT_UUID = h.DEPARTMENT_UUID");
		sql.append(" where t.IS_ACTIVE='Y' and h.is_active = 'Y' ");
		sql.append(" and h.parent_uuid ='"+departmentUuid+"' or h.department_uuid ='"+departmentUuid+"'");
		//sql.andEqual(" t.DEPARTMENT_UUID", departmentUuid);		
		//sql.append(" and h.department_uuid in (  select t.department_uuid    from hr_department t   start with t.department_uuid = '"+UserContextUtil.getDepartmentUuid()+"' connect by prior t.department_uuid = t.parent_uuid)");
		if (StringUtil.isNotEmpty(q)) {
			sql.append(sql.or(sql.contains("t.AVICPSNCODE", q), sql.contains("t.EMP_NAME", q),
					sql.contains("t.EMP_SEX", q), sql.contains("h.DEPARTMENT_NAME", q)));
		}
		return sql.query(entityManager, HrEmployeesDto.class);
	}
	
	
	
	/**
	 * 通过部门拿员工数据。
	 * @param q
	 * @param isDept 有值(isDept = 登录用户的部门 )  isDept=32位的值   就取这个部门
	 * @author dalinpeng
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<HrEmployeesDto> getHrEmpsByDept(String q,String isDept) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("h.DEPARTMENT_NAME", "departmentName",",");
		sql.appendTableColumns(HrEmployees.class, "t");
		sql.append(" from HR_EMPLOYEES t ");
		sql.append(" left join hr_department h on t.DEPARTMENT_UUID = h.DEPARTMENT_UUID");
		sql.append(" where t.IS_ACTIVE='Y' and h.is_active = 'Y' ");
		if(StringUtil.isNotEmpty(isDept) && !("GS").equals(UserContextUtil.getDepartmentType())){
			if(isDept.length() == 32){
				sql.append(" and h.department_uuid in (  select t.department_uuid    from hr_department t   start with t.department_uuid = '"+isDept+"' connect by prior t.department_uuid = t.parent_uuid)");
			}else{
				sql.append(" and h.department_uuid in (  select t.department_uuid    from hr_department t   start with t.department_uuid = '"+UserContextUtil.getDepartmentUuid()+"' connect by prior t.department_uuid = t.parent_uuid)");
			}
		}
		if (StringUtil.isNotEmpty(q)) {
			sql.append(sql.or(sql.contains("t.AVICPSNCODE", q), sql.contains("t.EMP_NAME", q),
					sql.contains("t.EMP_PHONE", q), sql.contains("h.DEPARTMENT_NAME", q)));
		}
		return sql.query(entityManager, HrEmployeesDto.class);
	}
	
	/**
	 * 接触因素危害
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<HrEmployeesDto> sqlPageQueryForElement(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");	
		sql.appendField("h.jobname", "jobName",",");
		sql.appendField("h.pk_om_job", "pkOmJob",",");
		sql.appendField("o.touch_id", "touchId",",");
		sql.appendField("d.DEPARTMENT_NAME", "departmentName",",");
		sql.appendField("o.work_begin ", "workBegin",",");
		sql.appendField("o.work_end", "workEnd",",");
		sql.appendTableColumns(HrEmployees.class, "t");
		sql.append(" from HR_EMPLOYEES t ");
		sql.append(" left join OH_TOUCH_ELEMENT o on t.emp_uuid = o.emp_uuid and o.is_active = 'Y' ");
		sql.append(" left join hr_job h on o.pk_om_job = h.pk_om_job and h.is_active = 'Y' ");
		sql.append(" left join hr_department d on t.DEPARTMENT_UUID = d.DEPARTMENT_UUID and d.is_active = 'Y' ");
		sql.append(" where t.is_active = 'Y' ");
		return sql.pageQuery(entityManager, request, HrEmployeesDto.class);
	}
	
	@Transactional
	public HrEmployeesDto addHrEmployeesByEle(HrEmployeesDto hrEmployeesDto) {
		HrEmployees hrEmployees = this.hrEmployeesRepository.findOne(hrEmployeesDto.getEmpUuid());
		hrEmployees = this.hrEmployeesRepository.save(hrEmployees);
		return CommonAssembler.assemble(hrEmployees, HrEmployeesDto.class);
	}

	public List<HrEmployeesDto> getHrEmpsByDeptId(String q, String deptUuid) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("h.DEPARTMENT_NAME", "departmentName",",");
		sql.appendTableColumns(HrEmployees.class, "t");
		sql.append(" from HR_EMPLOYEES t ");
		sql.append(" left join hr_department h on t.DEPARTMENT_UUID = h.DEPARTMENT_UUID");
		sql.append(" where t.IS_ACTIVE='Y' and h.is_active = 'Y' ");
		if(StringUtil.isNotEmpty(deptUuid)){
			sql.append(" and h.department_uuid in (  select t.department_uuid    from hr_department t   start with t.department_uuid = '"+deptUuid+"' connect by prior t.department_uuid = t.parent_uuid)");
		}
		if (StringUtil.isNotEmpty(q)) {
			sql.append(sql.or(sql.contains("t.AVICPSNCODE", q), sql.contains("t.EMP_NAME", q),
					sql.contains("t.EMP_SEX", q), sql.contains("h.DEPARTMENT_NAME", q)));
		}
		return sql.query(entityManager, HrEmployeesDto.class);
	} 
	
	/**
	 * 当前部门下的员工
	 * 
	 * @return
	 */
	public List<HrEmployeesDto> findByUserCds(List<String> userCdLs) {
		if (userCdLs == null || userCdLs.size() == 0) {
			return null;
		}
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(HrEmployees.class, "h");
		sql.append(" from hr_employees h ");
		sql.append(" where h.is_active = 'Y' ");
		if (userCdLs != null && userCdLs.size() > 0) {
			sql.append("and h.USER_CD  in (");
			for (int i = 0; i < userCdLs.size(); i++) {
				int j = i + 1;
				if (i == 0) {
					sql.append(":userCd").append(String.valueOf(j));
					sql.addParameter("userCd" + String.valueOf(j), userCdLs.get(i));
				} else {
					sql.append(",:userCd").append(String.valueOf(j));
					sql.addParameter("userCd" + String.valueOf(j), userCdLs.get(i));
				}

			}
			sql.append(")");
		}
		return sql.query(entityManager, HrEmployeesDto.class);
	}
	
	/**
	 * 员工编码唯一性验证
	 * @author dalinpeng
	 * @param empCode
	 * @param empUuid
	 * @return
	 */
	public boolean isExistsEmpCode(String empCode,String empUuid){
		boolean flag = true;
		if(StringUtil.isEmpty(empUuid)){
			List<HrEmployees> list = this.hrEmployeesRepository.queryHrEmployeesByEmpCode(empCode);
			if((!ListUtil.isEmpty(list)) && list.size() > 0){
				flag = false;
			}
		}else{
			List<HrEmployees> list = this.hrEmployeesRepository.queryHrEmployeesByEmp(empCode,empUuid);
			if((!ListUtil.isEmpty(list)) && list.size() > 0){
				flag = false;
			}
		}
		return flag;
	}
	
	public List<HrEmployeesDto> getHrEmployeesByEmpName(String empName,String notLoginPerson,String notEmail,String defaultPeople){
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");	
		sql.appendField("s.account_uuid", "acctId",",");
		sql.appendField("h.emp_name", "empName",",");
		sql.appendField("h.emp_phone", "empPhone",",");
		sql.appendField("h.emp_email", "empEmail",",");
		sql.appendField("h.emp_code", "empCode", ",");
		sql.appendField("d.department_name", "departmentName","");
		sql.append(" from HR_EMPLOYEES h ");
		sql.append(" join sys_account s on s.ref_id=h.emp_uuid and s.status='common' and s.is_active='Y' ");
		sql.append(" left join hr_department d on d.department_uuid=h.department_uuid and d.is_active='Y' ");
		sql.append(" where h.IS_ACTIVE='Y' and  rownum <=100 ");
		if("Y".equals(notLoginPerson)){
			sql.append(sql.notEqual("s.account_uuid",UserContextUtil.getAccountUuid()));
		}
		if("Y".equals(notEmail)){
			sql.append(sql.isNotNull("h.emp_email"));
		}
		if (StringUtil.isNotEmpty(empName)) {
			
			
			/*
	    	sql.append(sql.contains("h.emp_name",empName,true));*/
			
			
			
			String[] empNames = empName.split("\\,");
			List<Condition> cdsList = new ArrayList<>();
			for(String name : empNames){
				cdsList.add(sql.contains("h.emp_name",name,true));
			}
			sql.append(sql.or(cdsList.toArray(new Condition[cdsList.size()])));
		}else{
			if(StringUtil.isNotEmpty(defaultPeople)){
				sql.append(" union select s.account_uuid,h.emp_name,h.emp_phone,h.emp_email,h.emp_code,d.department_name ");
				sql.append(" from HR_EMPLOYEES h join sys_account s on s.ref_id = h.emp_uuid  and s.status = 'common' and s.is_active = 'Y' ");
				sql.append("  left join hr_department d on d.department_uuid = h.department_uuid and d.is_active = 'Y'  where h.IS_ACTIVE = 'Y'");
				sql.andEqual("s.account_uuid", defaultPeople);
			}
		}
		return sql.query(entityManager, HrEmployeesDto.class);
	}
	
	public List<HrEmployeesDto> getHrEmployeesByCombo(String empName, String deptId){
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");	
		sql.appendField("s.account_uuid", "accountUuid",",");
		sql.appendTableColumns(HrEmployees.class, "h");
		sql.append(" from HR_EMPLOYEES h ");
		sql.append(" join sys_account s on s.ref_id=h.emp_uuid and s.status='common' and s.is_active='Y' ");
		sql.append(" where h.IS_ACTIVE='Y' ");
		
		if(StringUtil.isNotEmpty(empName)) {
			sql.append(sql.contains("h.emp_name",empName,true));
		}
		
		if(StringUtil.isNotEmpty(deptId)) {
			sql.append(sql.equal("h.department_uuid", deptId));
		}
		
		sql.append(" and rownum <=100 ");
		return sql.query(entityManager, HrEmployeesDto.class);
	}
	@Transactional(readOnly=true)
	public List<HrEmployeesDto> getEmpByEmpName(String empEmail) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");	
		sql.appendTableColumns(HrEmployees.class, "t");
		sql.append(" from HR_EMPLOYEES t ");
		sql.append(" where t.is_active='Y' ");
		sql.andEqual("t.emp_email", empEmail);
		return sql.query(entityManager, HrEmployeesDto.class);
	} 
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void synEmpByEpms(HrEmployeesDto e){
		HrEmployees hr = this.hrEmployeesRepository.findOne(e.getEmpUuid());
		if(hr != null){
			this.updateHrEmployees(e, null);
		}else{
			this.addHrEmployees(e);
		}
	}

	public List<HrEmployeesDto> getHrEmployeesByDre(String q,String groupCodes){
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select distinct ");	
		sql.appendField("he.emp_name", "empName",",");
		sql.appendField("hd.department_name", "departmentName",",");
		sql.appendField("he.emp_email", "empEmail","");
		sql.append(" from PS_BUILD_GROUP t ");
		sql.append(" join PS_BOM_BATCH_EPL_HEAD p on p.REF_UUID = t.PBG_UUID and p.is_active = 'Y' ");
		sql.append(" join ps_bom_batch_epl bp on bp.pbbeh_uuid = p.pbbeh_uuid and bp.is_active = 'Y' ");
		sql.append(" join hr_employees he on he.emp_email = bp.patac_dre_email and he.is_active = 'Y' ");
		sql.append(" join hr_department hd on hd.department_uuid = he.department_uuid ");
		sql.append(" where t.IS_ACTIVE = 'Y' ");
		if (StringUtil.isNotEmpty(q)) {
			sql.append(sql.contains("he.emp_name", q,true));
		}
		if(StringUtils.isNotBlank(groupCodes)){
			sql.append(sql.in("t.group_code", Arrays.asList(groupCodes.split("\\s*,\\s*"))));
		}
		return sql.query(entityManager, HrEmployeesDto.class);
	}
	
	/**
	 * 根据邮箱得到员工
	 * @param email
	 * @return
	 */
	public HrEmployees getByEmpEmail(String empEmail) {
		List<HrEmployees> empList = this.hrEmployeesRepository.queryByEmpEmail(empEmail);
		if(ListUtil.isEmpty(empList)) {
			throw new BusinessException(empEmail + "对应的员工不存在！");
		}
		return empList.get(0);
	}
	
	public List<HrEmployeesDto> findHrEmployees(String q,String order,String deptUuid) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("  e.emp_uuid", "empUuid", ",");
		sql.appendField("  e.emp_code", "empCode", ",");
		sql.appendField("  e.emp_name", "empName", ",");
		sql.appendField("  d.department_uuid", "departmentUuid", ",");
		sql.appendField("  d.department_name", "departmentName", ",");
		sql.appendField("  e.emp_phone", "empPhone", "");
		sql.append(" from hr_employees e");
		sql.append(" join hr_department d on d.department_uuid = e.department_uuid ");
		sql.append(" where nvl(e.is_enabled,'Y') ='Y'  and e.status = 'incumbency' ");
		if(!StringUtil.isEmpty(q)){
			sql.append(" and  (e.emp_code like '%" + q + "%' or e.emp_name like '%" + q + "%') ");
		}
		if(!StringUtil.isEmpty(deptUuid)) {
			if(!StringUtils.isEmpty(order)){
				sql.append(" and (d.department_uuid= '"+deptUuid+"' or e.emp_uuid = '"+order+"')");
			}else{
				sql.andEqual("d.department_uuid", deptUuid);
			}
		}
		if(!StringUtils.isEmpty(order)){
			sql.append(" and (rownum < 100 or e.emp_uuid = '"+order+"') order by decode(e.emp_uuid,'"+order+"',1,2)  ");
		}else{
			sql.append(" and rownum < 100");
		}
		return sql.query(entityManager, HrEmployeesDto.class);   
	}
	
	/**
	 * 查询技师员工
	 * @param q
	 * @return
	 */
	public List<HrEmployeesDto> findTechnicianEmp(String q) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append(" select  ");
		sql.appendTableColumns(HrEmployees.class, "t");
		sql.append("   from hr_employees t ");
		sql.append("  where t.is_active = 'Y' ");
		sql.contains("t.emp_name", q);
		return sql.query(entityManager, HrEmployeesDto.class);
	}
	
	/**
	 * 通过BuildBook提交人，得到班组长
	 * @param empUuid
	 * @return
	 */
	public HrEmployeesDto findTeamLeaderByBuildBook(String empUuid){
		HrEmployeesDto dto = new HrEmployeesDto();
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append(" select  ");
		sql.appendTableColumns(HrEmployees.class, "h");
		sql.append("  from hr_employees h ");
		sql.append("  join ps_pp_workshop_section w on (w.team_leader = h.emp_uuid and w.is_active='Y') ");
		sql.append("  join ps_assembly_station s on (w.ppws_uuid = s.ppws_uuid and s.is_active='Y') ");
		sql.append("  join hr_employees e on (s.pas_uuid = e.pas_uuid and e.is_active='Y') ");
		sql.append("  where h.is_active = 'Y' ");
		sql.andEqual("e.emp_uuid", empUuid);
		List<HrEmployeesDto> list = sql.query(entityManager, HrEmployeesDto.class);
		if(!ListUtil.isNullOrEmpty(list)){
			dto = list.get(0);
		}
		return dto;
	}	
	
	/**
	 * 得到邮件抄送人
	 * @author dalinpeng
	 * @param config
	 * @return
	 */
	public List<SysRemindUser> findByRemindConfig4Email(SysRemindConfigDto config) {
		List<SysRemindUser> result = new ArrayList<SysRemindUser>();
		List<SysRemindUser> unResult = new ArrayList<SysRemindUser>();
		if(!ListUtil.isNullOrEmpty(config.getEmps())){
			if(!ListUtil.isNullOrEmpty(result)){//去掉重复
				result.stream().filter(ListUtil.distinctByKey(b -> b.getEmpUuid())).forEach(b -> unResult.add(b));
			}
		}
		return unResult;
	}
	
	
	/**
	 * 通过当前用户和岗位编码得到上级岗位人员
	 * @param currUserDept
	 * @param positionNbrEpms
	 * @return
	 */
	public List<HrEmployeesDto> findByPositionNbrEpms(String currUserDept,String positionNbrEpms){
		if(StringUtil.isEmpty(currUserDept)){
			currUserDept = UserContextUtil.getDepartmentUuid();
		}
		List<HrEmployeesDto> result = new ArrayList<HrEmployeesDto>();
		HrEmployeesOrgPositionRltDto dto = new HrEmployeesOrgPositionRltDto();
		dto.setPositionNbrEpms(positionNbrEpms);
		dto.setDepartmentUuid(currUserDept);
		List<HrEmployeesOrgPositionRltDto> list = this.hrEmployeesOrgPositionRltService.findByDto(dto);
		if(ListUtil.isNullOrEmpty(list)){
			HrDepartmentDto departmentDto=hrDepartmentService.getById(currUserDept);
			if(StringUtil.isNotEmpty(departmentDto.getParentUuid())){
				return findByPositionNbrEpms(departmentDto.getParentUuid(),positionNbrEpms);
			}else{
				return result;
			}
		}else{
			for(HrEmployeesOrgPositionRltDto opr : list){
				HrEmployeesDto emp = this.getById(opr.getEmpUuid());
				if(emp != null){
					result.add(emp);
				}
			}
			return result;
		}		
	}
	
	
	/**
	 * 得到所有替办人员
	 * @param empUuid
	 * @return
	 */
	public List<HrEmployeesDto> findReplaceEmp(String  empUuid){
		List<HrEmployeesDto> result = new ArrayList<HrEmployeesDto>();
		if(StringUtil.isNotEmpty(empUuid)) {
			SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
			sql.append(" select  ");
			sql.appendTableColumns(HrEmployees.class, "e");
			sql.append("   from hr_employees e ");
			sql.append("   join bpm_replace_info  t on (t.is_active='Y' and t.status='open' and t.begin_date < sysdate ");
			sql.append("   and trunc(t.end_date) >= trunc(sysdate) and e.emp_uuid = t.replace_uuid ");
			sql.andEqual("t.emp_uuid", empUuid);
			sql.append(" ) ");
			sql.append("  where e.is_active = 'Y' ");	
			result = sql.query(entityManager, HrEmployeesDto.class);
		}		
		return result;
	}
}

