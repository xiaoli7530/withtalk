package com.ctop.fw.sys.service;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.sys.dto.SysRolePermissionDto;
import com.ctop.fw.sys.entity.SysRolePermission;
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
public class SysRolePermissionService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysRolePermissionRepository sysRolePermissionRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	public SysRolePermissionDto getById(String id) {
		SysRolePermission sysRolePermission = this.sysRolePermissionRepository.findOne(id);
		return modelMapper.map(sysRolePermission, SysRolePermissionDto.class);
	}
	
	@Transactional
	public SysRolePermissionDto addSysRolePermission(SysRolePermissionDto sysRolePermissionDto) {
		SysRolePermission sysRolePermission = modelMapper.map(sysRolePermissionDto, SysRolePermission.class);
		sysRolePermission = this.sysRolePermissionRepository.save(sysRolePermission);
		return modelMapper.map(sysRolePermission, SysRolePermissionDto.class);
	} 

	@Transactional
	public SysRolePermissionDto updateSysRolePermission(SysRolePermissionDto sysRolePermissionDto) {
		SysRolePermission sysRolePermission = this.sysRolePermissionRepository.findOne(sysRolePermissionDto.getRolePermissionUuid());
		modelMapper.map(sysRolePermissionDto, sysRolePermission);
		sysRolePermission = this.sysRolePermissionRepository.save(sysRolePermission);
		return modelMapper.map(sysRolePermission, SysRolePermissionDto.class);
	}
	
	@Transactional
	public void deleteSysRolePermission(String id) {
		SysRolePermission sysRolePermission = this.sysRolePermissionRepository.findOne(id);
		sysRolePermission.setIsActive("N");
		this.sysRolePermissionRepository.save(sysRolePermission);
	}
		
	@Transactional
	public void deleteSysRolePermissions(List<String> rolePermissionUuids) {
		Iterable<SysRolePermission> sysRolePermissions = this.sysRolePermissionRepository.findAll(rolePermissionUuids);
		Iterator<SysRolePermission> it = sysRolePermissions.iterator();
		while(it.hasNext()) {
			SysRolePermission sysRolePermission = it.next();
			sysRolePermission.setIsActive("N");
			this.sysRolePermissionRepository.save(sysRolePermission);
		}
	} 

	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<SysRolePermission> pageQuery(PageRequestData request) {
		Specification<SysRolePermission> spec = request.toSpecification(SysRolePermission.class);
		Page<SysRolePermission> page = sysRolePermissionRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<SysRolePermission>(page);
	}
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<SysRolePermissionDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysRolePermission.class, "t");
		sql.append(" from SYS_ROLE_PERMISSION t where 1=1 ");
		return sql.pageQuery(entityManager, request, SysRolePermissionDto.class);
	}
	
}

