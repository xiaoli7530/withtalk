package com.ctop.fw.sys.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.sys.dto.SysPermissionDto;
import com.ctop.fw.sys.dto.SysPermissionSimpleDto;
import com.ctop.fw.sys.entity.SysPermission;
import com.ctop.fw.sys.entity.SysResource;
import com.ctop.fw.sys.repository.SysPermissionRepository;
import com.ctop.fw.sys.repository.SysResourceRepository;
import com.ctop.fw.sys.repository.SysRolePermissionRepository;


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
public class SysPermissionService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysPermissionRepository sysPermissionRepository;
	@Autowired
	private SysResourceRepository sysResourceRepository;
	@Autowired
	private SysRolePermissionRepository sysRolePermissionRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	public SysPermissionDto getById(String id) {
		SysPermission sysPermission = this.sysPermissionRepository.findOne(id);
		return modelMapper.map(sysPermission, SysPermissionDto.class);
	}
	
	@Transactional
	public SysPermissionDto addSysPermission(String companyUuid, SysPermissionDto sysPermissionDto) {
		SysPermission sysPermission = modelMapper.map(sysPermissionDto, SysPermission.class);
		sysPermission = this.sysPermissionRepository.save(sysPermission);		
		return CommonAssembler.assemble(sysPermission, SysPermissionDto.class);
	} 

	@Transactional
	public SysPermissionDto updateSysPermission(String companyUuid, SysPermissionDto sysPermissionDto) {
		SysPermission sysPermission = this.sysPermissionRepository.findOne(sysPermissionDto.getPermissionUuid());
		modelMapper.map(sysPermissionDto, sysPermission);
		sysPermission = this.sysPermissionRepository.save(sysPermission);
		return modelMapper.map(sysPermission, SysPermissionDto.class);
	}
	
	/**
	 * 先统一删除，再新增
	 * @param resourceUuid
	 * @param sysPermissionDtos
	 */
	@Transactional
	public void saveSysPermissions(String companyUuid, String resourceUuid, List<SysPermissionDto> sysPermissionDtos) { 		
		for(SysPermissionDto spn : sysPermissionDtos) {
			if(StringUtil.isNotEmpty(spn.getPermissionUuid())) {
				this.sysRolePermissionRepository.deleteByPermissionUuid(spn.getPermissionUuid());
			}
		}
		this.sysPermissionRepository.deleteByResourceUuid(resourceUuid);
		SysResource sysResource=this.sysResourceRepository.findOne(resourceUuid);
		for(SysPermissionDto syd:sysPermissionDtos){
			syd.setResourceUuid(resourceUuid);
			syd.setResourceName(sysResource.getResourceName());
			syd.setOperationName(syd.getName());
			SysPermission spn = modelMapper.map(syd, SysPermission.class);
			this.sysPermissionRepository.save(spn);
		}
	}
	
	 
	
	@Transactional
	public void deleteSysPermission(String companyUuid, String id) {
		SysPermission sysPermission = this.sysPermissionRepository.findOne(id);
		sysPermission.setIsActive("N");
		this.sysPermissionRepository.save(sysPermission);
	}
		
	@Transactional
	public void deleteSysPermissions(String companyUuid, List<String> permissionUuids) {
		//根据permissionUuids查询sys_role_permission表有多少与之关联，表示不能直接删除sys_permission表中的记录
		//需要先取消sys_role_permission中的记录
		Iterable<SysPermission> nDSysPer =  sysPermissionRepository.findSysPermissionByPermissionIds(permissionUuids);
		Iterator<SysPermission> nDit = nDSysPer.iterator();
		List<String> lst = new ArrayList<String>();
		while(nDit.hasNext()) {
			SysPermission sp = nDit.next();
			lst.add(sp.getResourceName() + ":" + sp.getOperationName());
		}
		if(lst != null && lst.size() > 0) {
			String[] strarg = new String[]{};
			//功能权限 {} 已经使用，请先取消角色管理->功能权限配置，再进行删除！
			throw new BusinessException("sys.sysPermission.delete.used",lst.toArray());
		}
		
		Iterable<SysPermission> sysPermissions = this.sysPermissionRepository.findAll(permissionUuids);
		Iterator<SysPermission> it = sysPermissions.iterator();
		while(it.hasNext()) {
			SysPermission sysPermission = it.next();
			sysPermission.setIsActive("N");
			this.sysPermissionRepository.save(sysPermission);
		}
	} 

	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<SysPermission> pageQuery(PageRequestData request) {
		Specification<SysPermission> spec = request.toSpecification(SysPermission.class);
		Page<SysPermission> page = sysPermissionRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<SysPermission>(page);
	}
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<SysPermissionDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysPermission.class, "t");
		sql.append(" from SYS_PERMISSION t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, SysPermissionDto.class);
	}
	
	/**
	 * 取菜单下关联的功能按钮列表
	 * @param resourceUuid
	 * @return
	 */
	public List<SysPermissionDto> findSysPermissionByResourceUuid(String resourceUuid) {		
		 List<SysPermission> list = this.sysPermissionRepository.findByResourceUuid(resourceUuid);
		 return list.stream().map(item -> modelMapper.map(item, SysPermissionDto.class)).collect(Collectors.toList());
	}
	
	/**
	 * 根据按钮查询功能数据
	 * @param operationUuid
	 * @return
	 */
	public List<SysPermission> findSysPermissionByOperationUuid(String operationUuid) {
		 List<SysPermission> sysPermissions = this.sysPermissionRepository.findSysPermissionsByOperationUuid(operationUuid);
		 return sysPermissions;
	}
	
	/**
	 * 
	 * @param companyUuid
	 * @return
	 */
	public List<SysPermissionSimpleDto> findSysPermissionByCompanyUuid(String companyUuid) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select "); 
		sql.appendField("t.PERMISSION_UUID", "permissionUuid", ",");
		sql.appendField("t.RESOURCE_UUID", "resourceUuid", ",");
		sql.appendField("t.OPERATION_UUID", "operationUuid", ",");
		sql.appendField("t.OPERATION_CODE", "operationCode", ",");
		sql.appendField("t.INTERFACE_URL", "interfaceUrl"); 
//		sql.appendTableColumns(SysPermission.class, "t");
		sql.append(" from SYS_PERMISSION t join SYS_RESOURCE r on t.RESOURCE_UUID = r.RESOURCE_UUID where r.IS_ACTIVE='Y' and t.IS_ACTIVE='Y' ");
		if(!StringUtil.isEmpty(companyUuid)) {
			sql.andEqual("r.COMPANY_UUID", companyUuid);
		}
		return sql.query(entityManager, SysPermissionSimpleDto.class);
	}
	
	/**
	 * 查询帐号可用的权限
	 * @param accountUuid
	 * @return
	 */
	public List<SysPermissionSimpleDto> findSysPermissionAvailableToAccountUuid(String tenantUuid, String accountUuid) { 
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select distinct "); 
		sql.appendField("p.PERMISSION_UUID", "permissionUuid", ",");
		sql.appendField("p.RESOURCE_UUID", "resourceUuid", ",");
		sql.appendField("p.OPERATION_UUID", "operationUuid", ",");
		sql.appendField("p.OPERATION_CODE", "operationCode", ",");
		sql.appendField("p.INTERFACE_URL", "interfaceUrl"); 
		sql.append(" from SYS_ACCOUNT_ROLE sar, SYS_ROLE_PERMISSION o, SYS_ROLE sr, SYS_PERMISSION p, SYS_RESOURCE r "); 
		sql.append(" where sar.ROLE_UUID = o.ROLE_UUID and sar.ROLE_UUID = sr.ROLE_UUID and sr.IS_ACTIVE = 'Y' ");
		sql.append(" and p.PERMISSION_UUID = o.PERMISSION_UUID and p.IS_ACTIVE = 'Y' ");
		sql.append(" and p.RESOURCE_UUID = r.RESOURCE_UUID and r.IS_ACTIVE = 'Y' and sar.IS_ACTIVE='Y'");
		//sql.andEqual("r.COMPANY_UUID", tenantUuid);
		sql.andEqual("sar.ACCOUNT_UUID", accountUuid);
		return sql.query(entityManager, SysPermissionSimpleDto.class);
	}
}

