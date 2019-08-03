package com.ctop.fw.sys.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.hr.repository.HrEmployeesRepository;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.sys.entity.SysRemindConfigEmps;
import com.ctop.fw.sys.repository.SysRemindConfigEmpsRepository;
import com.ctop.fw.sys.repository.SysRoleRepository;
import com.ctop.report.utils.StringUtil;
import com.ctop.fw.sys.dto.SysRemindConfigEmpsDto;


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
public class SysRemindConfigEmpsService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysRemindConfigEmpsRepository sysRemindConfigEmpsRepository; 
	@Autowired
	private SysRoleRepository sysRoleRepository;
	
	@Autowired
	private HrEmployeesRepository hrEmployeesRepository;
	
	@Transactional(readOnly=true)
	public SysRemindConfigEmpsDto getById(String id) {
		SysRemindConfigEmps sysRemindConfigEmps = this.sysRemindConfigEmpsRepository.findOne(id);
		return CommonAssembler.assemble(sysRemindConfigEmps, SysRemindConfigEmpsDto.class);
	}
	
	/**
	 * 批量保存，修改，删除
	 * @param emps
	 * @param 配置表头的主键
	 * @return
	 */
	@Transactional
	public List<SysRemindConfigEmpsDto> addSysRemindConfigEmps(List<SysRemindConfigEmpsDto> emps,String rcUuid) {
		if(!ListUtil.isNullOrEmpty(emps)){
			for(SysRemindConfigEmpsDto emp : emps){
				if(StringUtil.isNotEmpty(emp.getRemindUserType()) && StringUtil.isNotEmpty(emp.getEmp())){
					if(StringUtil.isEmpty(emp.getRceUuid())){//新增
						emp.setRcUuid(rcUuid);
						this.addSysRemindConfigEmps(emp);
					}else{
						if(StringUtil.equals("del", emp.get_status())){//删除
							this.deleteSysRemindConfigEmps(emp.getRceUuid());
						}else{//更新
							this.updateSysRemindConfigEmps(emp, null);
						}
					}
				}				
			}
		}
		return emps;
	}
	
	
	@Transactional
	public SysRemindConfigEmpsDto addSysRemindConfigEmps(SysRemindConfigEmpsDto sysRemindConfigEmpsDto) {
		SysRemindConfigEmps sysRemindConfigEmps = CommonAssembler.assemble(sysRemindConfigEmpsDto, SysRemindConfigEmps.class);
		sysRemindConfigEmps = this.sysRemindConfigEmpsRepository.save(sysRemindConfigEmps);
		return CommonAssembler.assemble(sysRemindConfigEmps, SysRemindConfigEmpsDto.class);
	} 

	@Transactional
	public SysRemindConfigEmpsDto updateSysRemindConfigEmps(SysRemindConfigEmpsDto sysRemindConfigEmpsDto,Set<String> updatedProperties) {
		SysRemindConfigEmps sysRemindConfigEmps = this.sysRemindConfigEmpsRepository.findOne(sysRemindConfigEmpsDto.getRceUuid());
		if(updatedProperties == null){
			CommonAssembler.assemble(sysRemindConfigEmpsDto, sysRemindConfigEmps,CommonAssembler.DEFAULT_IGNORE_PROPS);
		}else{
			CommonAssembler.assemble(sysRemindConfigEmpsDto, sysRemindConfigEmps,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		}		
		sysRemindConfigEmps = this.sysRemindConfigEmpsRepository.save(sysRemindConfigEmps);
		return CommonAssembler.assemble(sysRemindConfigEmps, SysRemindConfigEmpsDto.class);
	}
	
	@Transactional
	public void deleteSysRemindConfigEmps(String id) {
		SysRemindConfigEmps sysRemindConfigEmps = this.sysRemindConfigEmpsRepository.findOne(id);
		sysRemindConfigEmps.setIsActive("N");
		this.sysRemindConfigEmpsRepository.save(sysRemindConfigEmps);
	}
		
	@Transactional
	public void deleteSysRemindConfigEmpss(List<String> rceUuids) {
		Iterable<SysRemindConfigEmps> sysRemindConfigEmpss = this.sysRemindConfigEmpsRepository.findAll(rceUuids);
		Iterator<SysRemindConfigEmps> it = sysRemindConfigEmpss.iterator();
		while(it.hasNext()) {
			SysRemindConfigEmps sysRemindConfigEmps = it.next();
			sysRemindConfigEmps.setIsActive("N");
			this.sysRemindConfigEmpsRepository.save(sysRemindConfigEmps);
		}
	} 	
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<SysRemindConfigEmpsDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysRemindConfigEmps.class, "t");
		sql.append(" from SYS_REMIND_CONFIG_EMPS t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, SysRemindConfigEmpsDto.class);
	}
	
	@Transactional(readOnly=true)
	public List<SysRemindConfigEmpsDto> findByDto(SysRemindConfigEmpsDto dto) {
		List<SysRemindConfigEmpsDto> result = new ArrayList<SysRemindConfigEmpsDto>();
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysRemindConfigEmps.class, "t");
		sql.append(" from SYS_REMIND_CONFIG_EMPS t where t.IS_ACTIVE='Y' ");
		sql.andEqual("t.rc_uuid", dto.getRcUuid());
		result = sql.query(entityManager, SysRemindConfigEmpsDto.class);
		return result;
	}
}

