package com.ctop.base.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.SqlBuilder;

import com.ctop.base.entity.HrEmployeesOrgPositionRlt;
import com.ctop.base.repository.HrEmployeesOrgPositionRltRepository;
import com.ctop.base.dto.HrEmployeesOrgPositionRltDto;
import com.ctop.base.entity.HrEmployeesOrgPositionRlt;


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
public class HrEmployeesOrgPositionRltService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private HrEmployeesOrgPositionRltRepository hrEmployeesOrgPositionRltRepository; 
	
	@Transactional(readOnly=true)
	public HrEmployeesOrgPositionRltDto getById(String id) {
		HrEmployeesOrgPositionRlt hrEmployeesOrgPositionRlt = this.hrEmployeesOrgPositionRltRepository.findOne(id);
		return CommonAssembler.assemble(hrEmployeesOrgPositionRlt, HrEmployeesOrgPositionRltDto.class);
	}
	
	@Transactional
	public HrEmployeesOrgPositionRltDto addHrEmployeesOrgPositionRlt(HrEmployeesOrgPositionRltDto hrEmployeesOrgPositionRltDto) {
		HrEmployeesOrgPositionRlt hrEmployeesOrgPositionRlt = CommonAssembler.assemble(hrEmployeesOrgPositionRltDto, HrEmployeesOrgPositionRlt.class);
		if(StringUtil.isEmpty(hrEmployeesOrgPositionRlt.getRltUuid())) {
			hrEmployeesOrgPositionRlt.setRltUuid(StringUtil.getUuid());
		}
		hrEmployeesOrgPositionRlt = this.hrEmployeesOrgPositionRltRepository.save(hrEmployeesOrgPositionRlt);
		return CommonAssembler.assemble(hrEmployeesOrgPositionRlt, HrEmployeesOrgPositionRltDto.class);
	} 

	@Transactional
	public HrEmployeesOrgPositionRltDto updateHrEmployeesOrgPositionRlt(HrEmployeesOrgPositionRltDto hrEmployeesOrgPositionRltDto,Set<String> updatedProperties) {
		HrEmployeesOrgPositionRlt hrEmployeesOrgPositionRlt = this.hrEmployeesOrgPositionRltRepository.findOne(hrEmployeesOrgPositionRltDto.getRltUuid());
		CommonAssembler.assemble(hrEmployeesOrgPositionRltDto, hrEmployeesOrgPositionRlt,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		hrEmployeesOrgPositionRlt = this.hrEmployeesOrgPositionRltRepository.save(hrEmployeesOrgPositionRlt);
		return CommonAssembler.assemble(hrEmployeesOrgPositionRlt, HrEmployeesOrgPositionRltDto.class);
	}
	
	@Transactional
	public void deleteHrEmployeesOrgPositionRlt(String id) {
		HrEmployeesOrgPositionRlt hrEmployeesOrgPositionRlt = this.hrEmployeesOrgPositionRltRepository.findOne(id);
		hrEmployeesOrgPositionRlt.setIsActive("N");
		this.hrEmployeesOrgPositionRltRepository.save(hrEmployeesOrgPositionRlt);
	}
		
	@Transactional
	public void deleteHrEmployeesOrgPositionRlts(List<String> rltUuids) {
		Iterable<HrEmployeesOrgPositionRlt> hrEmployeesOrgPositionRlts = this.hrEmployeesOrgPositionRltRepository.findAll(rltUuids);
		Iterator<HrEmployeesOrgPositionRlt> it = hrEmployeesOrgPositionRlts.iterator();
		while(it.hasNext()) {
			HrEmployeesOrgPositionRlt hrEmployeesOrgPositionRlt = it.next();
			hrEmployeesOrgPositionRlt.setIsActive("N");
			this.hrEmployeesOrgPositionRltRepository.save(hrEmployeesOrgPositionRlt);
		}
	} 

 
	/**
	 * 查询
	 * @param dto
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<HrEmployeesOrgPositionRltDto> findByDto(HrEmployeesOrgPositionRltDto dto) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(HrEmployeesOrgPositionRlt.class, "t");
		sql.append(" from HR_EMPLOYEES_ORG_POSITION_RLT t where t.IS_ACTIVE='Y' ");
		sql.andEqual("t.DEPARTMENT_UUID", dto.getDepartmentUuid());
		sql.andEqual("t.POSITION_NBR_EPMS", dto.getPositionNbrEpms());
		return sql.query(entityManager,HrEmployeesOrgPositionRltDto.class);
	}
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<HrEmployeesOrgPositionRltDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(HrEmployeesOrgPositionRlt.class, "t");
		sql.append(" from HR_EMPLOYEES_ORG_POSITION_RLT t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, HrEmployeesOrgPositionRltDto.class);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void synHeoprByEpms(HrEmployeesOrgPositionRltDto e) {
		HrEmployeesOrgPositionRlt s = this.hrEmployeesOrgPositionRltRepository.findOne(e.getRltUuid());
		if(s != null){
			this.updateHrEmployeesOrgPositionRlt(e, null);
		}else{
			this.addHrEmployeesOrgPositionRlt(e);
		}
		
	}
	
}

