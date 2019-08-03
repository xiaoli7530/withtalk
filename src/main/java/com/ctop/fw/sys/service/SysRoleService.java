package com.ctop.fw.sys.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
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
import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.sys.dto.SysAccountRoleDto;
import com.ctop.fw.sys.dto.SysRoleAndAccountDto;
import com.ctop.fw.sys.dto.SysRoleDto;
import com.ctop.fw.sys.entity.SysAccountRole;
import com.ctop.fw.sys.entity.SysRole;
import com.ctop.fw.sys.entity.SysRolePermission;
import com.ctop.fw.sys.repository.SysRolePermissionRepository;
import com.ctop.fw.sys.repository.SysRoleRepository;


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
public class SysRoleService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysRoleRepository sysRoleRepository;
	@Autowired
	private SysRolePermissionRepository sysRolePermissionRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	public SysRoleDto getById(String id) {
		SysRole sysRole = this.sysRoleRepository.findOne(id);
		return modelMapper.map(sysRole, SysRoleDto.class);
	}
	
	@Transactional
	public SysRoleDto addSysRole(SysRoleDto sysRoleDto) {
		this.validateRoleCode(sysRoleDto);
		SysRole sysRole = modelMapper.map(sysRoleDto, SysRole.class);
		sysRole = this.sysRoleRepository.save(sysRole);
		return modelMapper.map(sysRole, SysRoleDto.class);
	} 

	@Transactional
	public SysRoleDto updateSysRole(SysRoleDto sysRoleDto,Set<String> updatedProperties) {
		SysRole sysRole = this.sysRoleRepository.findOne(sysRoleDto.getRoleUuid());
		this.validateRoleCode(sysRoleDto);
		//modelMapper.map(sysRoleDto, sysRole);
		CommonAssembler.assemble(sysRoleDto, sysRole,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		sysRole = this.sysRoleRepository.save(sysRole);
		return modelMapper.map(sysRole, SysRoleDto.class);
	}
	
	private void validateRoleCode(SysRoleDto sysRole) {
		// 校验同级节点的序号必须不同
		String exRUuid = StringUtil.isEmpty(sysRole.getRoleUuid()) ? "_" : sysRole.getRoleUuid();
		long sameCount = this.sysRoleRepository.countSameRoleCode(sysRole.getCompanyUuid(), exRUuid, sysRole.getRoleCode()); 
		if (sameCount > 0) {
			throw new BusinessException("角色代码已存在!");
		}
	}
	
	@Transactional
	public void deleteSysRole(String roleUuid) {
		this.deleteSysRoles(Arrays.asList(roleUuid));
	}
		
	@Transactional
	public void deleteSysRoles(List<String> roleUuids) {
		List<String> usedRoleUuids = this.sysRoleRepository.listUsedRoleUuids(roleUuids);
		if(!usedRoleUuids.isEmpty()) { 
			List<SysRole> sysRoles = this.sysRoleRepository.findAll(usedRoleUuids);
			String names = ListUtil.joinField(sysRoles, "name", ", ");
			throw new BusinessException("sys.role.cannot.delete.used", new Object[]{names});
		}
		//删除角色分配的权限
		for(String roleUuid: roleUuids) {
			this.sysRolePermissionRepository.deleteByRoleUuid(roleUuid);
		}
		//删除角色
		for(String roleUuid: roleUuids) {
			this.sysRoleRepository.delete(roleUuid);
		}
	} 
	
	/**
	 * 给角色分配权限
	 * @param sysRoleDto
	 */
	@Transactional
	public void assignRolePermission(SysRoleDto sysRoleDto) {
		//1. 统一删除原有权限；
		this.sysRolePermissionRepository.deleteByRoleUuid(sysRoleDto.getRoleUuid());
		List<SysRolePermission> list = new ArrayList<SysRolePermission>();
		if(sysRoleDto.getPermissionUuids() != null) {
			for(String permissionUuid : sysRoleDto.getPermissionUuids()) {
				SysRolePermission perm = new SysRolePermission();
				perm.setRoleUuid(sysRoleDto.getRoleUuid());
				perm.setPermissionUuid(permissionUuid);
				list.add(perm);
			}
			//2. 批量保存设置的权限；
			this.sysRolePermissionRepository.save(list);
		}
	}

	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<SysRole> pageQuery(PageRequestData request) {
		Specification<SysRole> spec = request.toSpecification(SysRole.class);
		Page<SysRole> page = sysRoleRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<SysRole>(page);
	}
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<SysRoleDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysRole.class, "t");
		sql.append(" from SYS_ROLE t where t.IS_ACTIVE = 'Y' and t.from_type='ppobuild' ");
		sql.andEqual("t.COMPANY_UUID", UserContextUtil.getCompanyUuid());
		return sql.pageQuery(entityManager, request, SysRoleDto.class);
	}
	
	public SysRoleDto getRoleWithPermissionUuids(String roleUuid) {
		SysRole sysRole = this.sysRoleRepository.findOne(roleUuid);
		SysRoleDto dto = modelMapper.map(sysRole, SysRoleDto.class);
		List<String> permissionUuids = this.sysRolePermissionRepository.findPermissionUuidByRoleUuid(roleUuid);
		dto.setPermissionUuids(permissionUuids);
		return dto;
	}
	
	public List<SysRoleDto> findAvailableRoles() {
		 List<SysRole> list = this.sysRoleRepository.findAllAvailable(UserContextUtil.getCompanyUuid());
		 return ListUtil.map(modelMapper, list, SysRoleDto.class);
	}
	
	
	/**
	 * 根据角色类型获取
	 * @param roleOrigin
	 * @return
	 */
	public List<SysRoleAndAccountDto> getAvailableRolesByRoleOrigin(String roleOrigin,String accountUuid) {
		 SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
			sql.append("select ");
			sql.appendField("sr.role_uuid", "roleUuid",",");
			sql.appendField("sr.name", "name",",");
			sql.appendField("sr.role_code", "roleCode",",");
			sql.appendField("sr.remark", "remark",",");
			sql.appendField("sr.role_origin", "roleOrigin",",");
			sql.appendField("sar.account_uuid", "accountUuid","");
			sql.append(" from sys_role sr  ");
			sql.append(" left join sys_account_role sar on sr.role_uuid = sar.role_uuid  and sar.is_active='Y' ");
			sql.andEqual("sar.account_uuid", accountUuid);
			sql.append(" where sr.is_active='Y' ");
			sql.andEqual("sr.role_origin", roleOrigin);
			return sql.query(entityManager, SysRoleAndAccountDto.class); 
	}
	
	public List<SysRoleDto> selectAllRoles(List<String> roleUuids){
		if(roleUuids.size() == 0){
			return null;
		}
		List<SysRole> roles = this.sysRoleRepository.findAllRoles(roleUuids);
		return ListUtil.map(modelMapper, roles, SysRoleDto.class);
	}
	
	public List<SysAccountRoleDto> getAccountUuidByRoleCode(String roleCode) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysAccountRole.class, "sar");
		sql.append(" from sys_account_role sar ");
		sql.append(" join sys_role sr on (sar.role_uuid=sr.role_uuid and sr.is_active='Y') ");
		sql.append("  where sar.is_active='Y' ");
		sql.andEqual("sr.role_code",roleCode);
		return sql.query(entityManager,SysAccountRoleDto.class);
	}
}
