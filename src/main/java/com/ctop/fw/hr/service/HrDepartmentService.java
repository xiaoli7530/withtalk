package com.ctop.fw.hr.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.service.BasePinyinService;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.hr.dto.HrDepartmentDto;
import com.ctop.fw.hr.entity.HrDepartment;
import com.ctop.fw.hr.entity.HrEmployees;
import com.ctop.fw.hr.repository.HrDepartmentRepository;
import com.ctop.fw.hr.repository.HrEmployeesRepository;

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
public class HrDepartmentService {

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private HrDepartmentRepository hrDepartmentRepository;
	@Autowired
	private HrEmployeesRepository hrEmployeesRepository;
	@Autowired
	private BasePinyinService basePinyinService;

	public HrDepartmentDto getById(String id) {
		HrDepartment hrDepartment = this.hrDepartmentRepository.findOne(id);
		return CommonAssembler.assemble(hrDepartment, HrDepartmentDto.class);
	}

	@Transactional
	public HrDepartmentDto addHrDepartment(HrDepartmentDto hrDepartmentDto) {
		validateDepartmentNo(hrDepartmentDto);
		validateSeqNo(hrDepartmentDto);
		HrDepartment hrDepartment = CommonAssembler.assemble(hrDepartmentDto, HrDepartment.class);
		if(StringUtil.isEmpty(hrDepartment.getDepartmentUuid())) {
			hrDepartment.setDepartmentUuid(StringUtil.getUuid());
		}
		//hrDepartment.setPinyin(this.basePinyinService.generatePinyin(hrDepartment.getDepartmentName()));
		hrDepartment = this.hrDepartmentRepository.save(hrDepartment);
		return CommonAssembler.assemble(hrDepartment, HrDepartmentDto.class);
	}

	@Transactional
	public HrDepartmentDto updateHrDepartment(HrDepartmentDto hrDepartmentDto,Set<String> updatedProperties) {
		validateDepartmentNo(hrDepartmentDto);
		validateSeqNo(hrDepartmentDto);
		HrDepartment hrDepartment = this.hrDepartmentRepository.findOne(hrDepartmentDto.getDepartmentUuid());
		if (StringUtil.isNotEmpty(hrDepartmentDto.getParentUuid())
				&& StringUtil.isNotEmpty(hrDepartment.getDepartmentUuid())
				&& hrDepartmentDto.getParentUuid().equals(hrDepartment.getDepartmentUuid())) {
			throw new BusinessException("hr.hrDepartment.duplicateParentUuid");
		} else {
			CommonAssembler.assemble(hrDepartmentDto, hrDepartment,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
			//hrDepartment.setPinyin(this.basePinyinService.generatePinyin(hrDepartment.getDepartmentName()));
			hrDepartment = this.hrDepartmentRepository.save(hrDepartment);
		}
		return CommonAssembler.assemble(hrDepartment, HrDepartmentDto.class);
	}

	/** 校验不能相同部门编号 */
	private void validateDepartmentNo(HrDepartmentDto hrDepartmentDto) {
		if (StringUtil.isNotEmpty(hrDepartmentDto.getDepartmentNo())) {
			String exDeparUuid = StringUtil.isEmpty(hrDepartmentDto.getDepartmentUuid()) ? "_"
					: hrDepartmentDto.getDepartmentUuid();
			long sameNoCount = this.hrDepartmentRepository.countSameDepartmentNo(exDeparUuid,
					hrDepartmentDto.getDepartmentNo());
			if (sameNoCount > 0) {
				// hr.hrDepartment.duplicateDepartmentNo=部门编号已存在!
				throw new BusinessException("hr.hrDepartment.duplicateDepartmentNo");
			}
		}
	}

	/** 校验同级节点的序号必须不同 */
	private void validateSeqNo(HrDepartmentDto hrDepartmentDto) {
		// 校验同级节点的序号必须不同
		long sameSeqNoCount = 0L;
		String exDeparUuid = StringUtil.isEmpty(hrDepartmentDto.getDepartmentUuid()) ? "_"
				: hrDepartmentDto.getDepartmentUuid();
		if (StringUtil.isEmpty(hrDepartmentDto.getParentUuid())) {
			sameSeqNoCount = this.hrDepartmentRepository.countSameSeqNo(exDeparUuid, hrDepartmentDto.getSeqNo());
		} else {
			sameSeqNoCount = this.hrDepartmentRepository.countSameSeqNo(hrDepartmentDto.getParentUuid(), exDeparUuid,
					hrDepartmentDto.getSeqNo());
		}
		if (sameSeqNoCount > 0) {
			// hr.hrDepartment.duplicateSeqNo=同节点不能存在相同序号!
			throw new BusinessException("hr.hrDepartment.duplicateSeqNo");
		}
	}

	/** 校验编辑时 */
	private void validateSameTree(HrDepartmentDto hrDepartmentDto) {
		// 校验同级节点的序号必须不同
		long sameSeqNoCount = 0L;
		String exDeparUuid = StringUtil.isEmpty(hrDepartmentDto.getDepartmentUuid()) ? "_"
				: hrDepartmentDto.getDepartmentUuid();
		if (StringUtil.isEmpty(hrDepartmentDto.getParentUuid())) {
			sameSeqNoCount = this.hrDepartmentRepository.countSameSeqNo(exDeparUuid, hrDepartmentDto.getSeqNo());
		} else {
			sameSeqNoCount = this.hrDepartmentRepository.countSameSeqNo(hrDepartmentDto.getParentUuid(), exDeparUuid,
					hrDepartmentDto.getSeqNo());
		}
		if (sameSeqNoCount > 0) {
			// hr.hrDepartment.duplicateSeqNo=同节点不能存在相同序号!
			throw new BusinessException("hr.hrDepartment.duplicateSeqNo");
		}
	}

	@Transactional
	public void deleteHrDepartment(String id) {
		HrDepartment hrDepartment = this.hrDepartmentRepository.findOne(id);
		long ishavaSon = hrDepartmentRepository.ishaveSon(id);
		if (ishavaSon > 0) {
			throw new BusinessException("请先删除子节点!");
		} else {
			hrDepartment.setIsActive("N");
		}
		this.hrDepartmentRepository.save(hrDepartment);
	}

	@Transactional
	public void deleteHrDepartments(List<String> departmentUuids) {
		Iterable<HrDepartment> hrDepartments = this.hrDepartmentRepository.findAll(departmentUuids);
		Iterator<HrDepartment> it = hrDepartments.iterator();
		while (it.hasNext()) {
			HrDepartment hrDepartment = it.next();
			long ishavaSon = hrDepartmentRepository.ishaveSon(hrDepartment.getDepartmentUuid());
			if (ishavaSon > 0) {
				throw new BusinessException("请先删除子节点!");
			} else {
//				List<WmWarehouse> wmWarehouse = wmWarehouseRepository
//						.findByDepartUuids(hrDepartment.getDepartmentUuid());
				List<HrEmployees> hrEmployees = hrEmployeesRepository
						.findbyDepartmentUuid(hrDepartment.getDepartmentUuid());
//				if (wmWarehouse.size() > 0) {
//					throw new BusinessException(wmWarehouse.get(0).getWarehouseName()+"被仓库信息引用,不能删除!");
//				} else 
				if (hrEmployees.size() > 0) {
					throw new BusinessException(hrDepartment.getDepartmentName()+"被员工信息引用,不能删除!");
				} else {
					hrDepartment.setIsActive("N");
				}
			}
			this.hrDepartmentRepository.save(hrDepartment);
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 */
	/*
	 * public PageResponseData<HrDepartment> pageQuery(PageRequestData request)
	 * { Specification<HrDepartment> spec =
	 * request.toSpecification(HrDepartment.class); Page<HrDepartment> page =
	 * hrDepartmentRepository.findAll(spec, request.buildPageRequest()); return
	 * new PageResponseData<HrDepartment>(page); }
	 */

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 */
	public PageResponseData<HrDepartmentDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		// sql.appendField("t.DEPARTMENT_NAME", "dName", ",");
		sql.appendField("t.DEPARTMENT_NAME", "text",",");
		sql.appendTableColumns(HrDepartment.class, "t");
		sql.append(" from HR_DEPARTMENT t ");
		// sql.append("left join HR_DEPARTMENT h on h.DEPARTMENT_UUID =
		// t.PARENT_UUID ");
		sql.append(" where t.IS_ACTIVE='Y'");
		return sql.pageQuery(entityManager, request, HrDepartmentDto.class);
	}
	
	public List<HrDepartmentDto> selectDepartmentTree(){
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("((select count(1) from hr_employees o where o.department_uuid=t.department_uuid and o.is_active='Y'))", "empNum", ",");
		sql.appendTableColumns(HrDepartment.class, "t");
		sql.append(" from HR_DEPARTMENT t ");
		sql.append(" where t.IS_ACTIVE='Y'");
		return sql.query(entityManager, HrDepartmentDto.class);
	}

	/**
	 * 通过组织唯一识别号查询
	 * @param hrDeptSetId
	 * @return
	 */
	public HrDepartmentDto findByHrDeptSetId(String hrDeptSetId){
		HrDepartmentDto hrDepartmentDto = null;
		HrDepartment hrDepartment = this.hrDepartmentRepository.findByHrDeptSetId(hrDeptSetId);
		if (hrDepartment != null){
			hrDepartmentDto = CommonAssembler.assemble(hrDepartment, HrDepartmentDto.class);
		}
		return hrDepartmentDto;
	}
	
	/**
	 * 通过组织唯一识别号查询
	 * @param hrDeptSetId
	 * @return
	 */
	public HrDepartmentDto findByHrSetId(String hrDeptSetId){
		HrDepartmentDto hrDepartmentDto = null;
		HrDepartment hrDepartment = this.hrDepartmentRepository.findByHrSetId(hrDeptSetId);
		if (hrDepartment != null){
			hrDepartmentDto = CommonAssembler.assemble(hrDepartment, HrDepartmentDto.class);
		}
		return hrDepartmentDto;
	}
	
	public Long queryMaxSeqNo(){
		 long seqNo = hrDepartmentRepository.queryMaxSeqNo() + 1;
		 return seqNo;
	}
	
	/**
	 * 通过部门ID寻找一级部门
	 * @param deptId
	 * @return
	 */
	public HrDepartmentDto findLevelOne(String deptId) {
		HrDepartmentDto hrDepartmentDto = null;
		HrDepartment hrDepartment = this.hrDepartmentRepository.findLevelOne(deptId);
		if (hrDepartment != null){
			hrDepartmentDto = CommonAssembler.assemble(hrDepartment, HrDepartmentDto.class);
		}
		return hrDepartmentDto;
	}
	public List<HrDepartmentDto> getByDepartmentCode(String code){
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(HrDepartment.class, "t");
		sql.append(" from HR_DEPARTMENT t ");
		sql.append(" where t.IS_ACTIVE='Y'");
		sql.andEqual("t.DEPARTMENT_NO", code);
		return sql.query(entityManager, HrDepartmentDto.class);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void synDeptByEpms(HrDepartmentDto e) {
		HrDepartment d = this.hrDepartmentRepository.findOne(e.getDepartmentUuid());
		if(d != null){
			this.updateHrDepartment(e, null);
		}else{
			this.addHrDepartment(e);
		}
	}
	
	@Transactional(readOnly=true)
	public List<HrDepartmentDto> getDeptMap() {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("t.DEPARTMENT_NAME", "departmentName",",");
		sql.appendField("t.DEPARTMENT_UUID", "departmentUuid","");
		//sql.appendTableColumns(HrDepartment.class, "t");
		sql.append(" from HR_DEPARTMENT t ");				
		sql.append(" where t.IS_ACTIVE='Y' ");			
		return sql.query(entityManager, HrDepartmentDto.class);
	}
}
