package com.ctop.base.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.PmProjectDto;
import com.ctop.base.entity.PmProject;
import com.ctop.base.repository.PmProjectRepository;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;


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
public class PmProjectService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private PmProjectRepository pmProjectRepository; 
	
	@Transactional(readOnly=true)
	public PmProjectDto getById(String id) {
		PmProject pmProject = this.pmProjectRepository.findOne(id);
		return CommonAssembler.assemble(pmProject, PmProjectDto.class);
	}
	
	@Transactional
	public PmProjectDto addPmProject(PmProjectDto pmProjectDto) {
		PmProject pmProject = CommonAssembler.assemble(pmProjectDto, PmProject.class);
		pmProject = this.pmProjectRepository.save(pmProject);
		if(StringUtil.isEmpty(pmProject.getProjectUuid())){
			pmProject.setProjectUuid(StringUtil.getUuid());
		}
		return CommonAssembler.assemble(pmProject, PmProjectDto.class);
	} 

	@Transactional
	public PmProjectDto updatePmProject(PmProjectDto pmProjectDto,Set<String> updatedProperties) {
		PmProject pmProject = this.pmProjectRepository.findOne(pmProjectDto.getProjectUuid());
		CommonAssembler.assembleIgnoreNull(pmProjectDto, pmProject,CommonAssembler.DEFAULT_IGNORE_PROPS);
		pmProject = this.pmProjectRepository.save(pmProject);
		return CommonAssembler.assemble(pmProject, PmProjectDto.class);
	}
	
	@Transactional
	public void deletePmProject(String id) {
		PmProject pmProject = this.pmProjectRepository.findOne(id);
		pmProject.setIsActive("N");
		this.pmProjectRepository.save(pmProject);
	}
		
	@Transactional
	public void deletePmProjects(List<String> projectUuids) {
		Iterable<PmProject> pmProjects = this.pmProjectRepository.findAll(projectUuids);
		Iterator<PmProject> it = pmProjects.iterator();
		while(it.hasNext()) {
			PmProject pmProject = it.next();
			pmProject.setIsActive("N");
			this.pmProjectRepository.save(pmProject);
		}
	} 

	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<PmProjectDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("h.emp_code", "empCode", ",");
		sql.appendField("h.emp_name", "empName", ",");
		sql.appendTableColumns(PmProject.class, "t");
		sql.append(" from PM_PROJECT t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, PmProjectDto.class);
	}
	
	public List<PmProjectDto> findAllProject(String q,String order) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append(" select ");
		sql.appendTableColumns(PmProject.class, "t");
		sql.append(" from pm_project t ");
		sql.append(" left join hr_employees h on t.project_manager_uuid = h.emp_uuid and h.is_active = 'Y' ");
		sql.append(" where t.is_active = 'Y' and (t.project_type ='engineering' or (t.project_type='edv' and t.project_level='2'))");
		if(!StringUtil.isEmpty(q)) {
			sql.append(" and  (Lower(t.project_Code) like Lower('%" + q + "%') or t.project_Name like '%" + q + "%') ");
		}	
		if(StringUtils.isNotBlank(order)){
			sql.append(" and (rownum < 100 or t.project_uuid = '"+order+"') order by decode(t.project_uuid,'"+order+"',1,2)  ");
			sql.append(" ,t.project_Code desc ");
		}else{
			sql.append(" and rownum < 100");
			sql.append(" order by t.project_Code desc ");
		}
		
		return sql.query(entityManager, PmProjectDto.class);   
	}
	
	public List<PmProjectDto> findEdvProject(String q,String order) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append(" select ");
		sql.appendTableColumns(PmProject.class, "t");
		sql.append(" from pm_project t ");
		sql.append(" left join hr_employees h on t.project_manager_uuid = h.emp_uuid and h.is_active = 'Y' ");
		sql.append(" where t.is_active = 'Y' and t.project_level='2' and t.project_type='edv'");
		if(!StringUtil.isEmpty(q)) {
			sql.append(" and  (Lower(t.project_Code) like Lower('%" + q + "%') or t.project_Name like '%" + q + "%') ");
		}	
		if(StringUtils.isNotBlank(order)){
			sql.append(" and (rownum < 100 or t.project_uuid = '"+order+"') order by decode(t.project_uuid,'"+order+"',1,2)  ");
		}else{
			sql.append(" and rownum < 100");
			sql.append(" order by t.project_Code ");
		}
		return sql.query(entityManager, PmProjectDto.class);   
	}
	
	public List<PmProjectDto> findAllProjectWithPppr(String q) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append(" select ");
		sql.appendField("p.PHASE", "phase", ",");
		sql.appendField("p.PPPR_NAME", "ppprName", ",");
		sql.appendField("p.PPPR_UUID", "ppprUuid", ",");
		sql.appendField("p.BIW_BOM_STATUS", "biwBomStatus", ",");
		sql.appendField("p.GA_BOM_STATUS", "gaBomStatus", ",");
		sql.appendField("p.PPPR_STATUS", "ppprStatus", ",");
		sql.appendTableColumns(PmProject.class, "t");
		sql.append(" from pm_project t ");
		sql.append(" inner join EDV_PPPR p on t.PROJECT_UUID = p.PROJECT_UUID and p.is_active = 'Y' ");
		sql.append(" where t.is_active = 'Y' ");
		if(!StringUtil.isEmpty(q)) {
			sql.append(" and  (Lower(t.project_Code) like Lower('%" + q + "%') or t.project_Name like '%" + q + "%') ");
		}
		sql.append(" order by t.project_Code ");
		return sql.query(entityManager, PmProjectDto.class);   
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void synProjectByEpms(PmProjectDto p) {
		PmProjectDto s = this.getById(p.getProjectUuid());
		if("N".equals(p.getIsActive())){
			s.setIsActive("N");
			this.pmProjectRepository.save(CommonAssembler.assemble(s, PmProject.class));
		}else if(s != null){
			this.updatePmProject(p, null);
		}else{
			this.addPmProject(p);
		}
	}
	
}

