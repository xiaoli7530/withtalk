package com.ctop.fw.sys.service;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
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
import com.ctop.fw.sys.dto.SysOperationDto;
import com.ctop.fw.sys.dto.SysPermissionDto;
import com.ctop.fw.sys.dto.SysResourceDto;
import com.ctop.fw.sys.entity.SysPermission;
import com.ctop.fw.sys.entity.SysResource;
import com.ctop.fw.sys.repository.SysPermissionRepository;
import com.ctop.fw.sys.repository.SysResourceRepository;


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
public class SysResourceService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysResourceRepository sysResourceRepository;
	@Autowired
	private ModelMapper modelMapper;	
	@Autowired
	private SysPermissionService sysPermissionService;
	@Autowired
	private SysOperationService sysOperationService;
	@Autowired
	private SysPermissionRepository sysPermissionRepository;
	
	public SysResourceDto getById(String id) {
		SysResource sysResource = this.sysResourceRepository.findOne(id);
		SysResourceDto dto = modelMapper.map(sysResource, SysResourceDto.class);
		List<SysPermission> sysPermissionList = this.sysPermissionRepository.findByResourceUuid(id);
		if(!ListUtil.isEmpty(sysPermissionList)){
			dto.setOperationUuidList(ListUtil.getOneFieldValue(sysPermissionList, "operationUuid", String.class));
		}
		return dto;
	}
	
	@Transactional
	@CacheEvict(cacheNames="service.sysResources4Company")
	public SysResourceDto addSysResource(SysResourceDto sysResourceDto) {
		this.validateResourceCode(sysResourceDto);
		this.validateSeqNo(sysResourceDto);
		String operationUuids = sysResourceDto.getOperationUuids();
		SysResource sysResource = modelMapper.map(sysResourceDto, SysResource.class);
		this.sysResourceRepository.save(sysResource);
		
		//处理权限按钮
		if(StringUtils.isNotEmpty(operationUuids)){
			String[] operationList = operationUuids.split(",");
			if(operationList.length > 0){
				for(String operationUuid : operationList){
					SysOperationDto operationDto = this.sysOperationService.getById(operationUuid);
					SysPermissionDto sysPermissionDto = new SysPermissionDto();
					sysPermissionDto.setOperationUuid(operationUuid);
					sysPermissionDto.setResourceUuid(sysResource.getResourceUuid());
					sysPermissionDto.setResourceName(sysResource.getResourceName());
					if(operationDto != null){
						sysPermissionDto.setOperationCode(operationDto.getOperationCode());
						sysPermissionDto.setOperationName(operationDto.getName());
					}
					this.sysPermissionService.addSysPermission(UserContextUtil.getCompanyUuid(), sysPermissionDto);
				}
			}
			
		}
		
		
		this.updateTreeCode(sysResource);
		return this.getById(sysResource.getResourceUuid());
	} 

	@Transactional
	@CacheEvict(cacheNames="service.sysResources4Company", key="#sysResourceDto.companyUuid")
	public SysResourceDto updateSysResource(SysResourceDto sysResourceDto,Set<String> updatedProperties) {
		this.validateResourceCode(sysResourceDto);
		this.validateSeqNo(sysResourceDto);
		SysResource sysResource = this.sysResourceRepository.findOne(sysResourceDto.getResourceUuid()); 
		CommonAssembler.assemble(sysResourceDto, sysResource,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		
		//处理权限按钮
		String operationUuids = sysResourceDto.getOperationUuids();
		//先删除这个菜单的所有权限按钮
		this.sysPermissionRepository.deleteByResourceUuid(sysResource.getResourceUuid());
		if(StringUtils.isNotEmpty(operationUuids)){
			String[] operationList = operationUuids.split(",");
			if(operationList.length > 0){
				for(String operationUuid : operationList){
					SysOperationDto operationDto = this.sysOperationService.getById(operationUuid);
					SysPermissionDto sysPermissionDto = new SysPermissionDto();
					sysPermissionDto.setOperationUuid(operationUuid);
					sysPermissionDto.setResourceUuid(sysResource.getResourceUuid());
					sysPermissionDto.setResourceName(sysResource.getResourceName());
					if(operationDto != null){
						sysPermissionDto.setOperationCode(operationDto.getOperationCode());
						sysPermissionDto.setOperationName(operationDto.getName());
					}
					this.sysPermissionService.addSysPermission(UserContextUtil.getCompanyUuid(), sysPermissionDto);
				}
			}
			
		}
		
		this.updateTreeCode(sysResource);
		return this.getById(sysResource.getResourceUuid());
	}
	
	private void validateResourceCode(SysResourceDto sysResource) {
		if (StringUtil.isNotEmpty(sysResource.getResourceCode())) {
			String exReUuid = StringUtil.isEmpty(sysResource.getResourceUuid()) ? "_" : sysResource.getResourceUuid();
			long sameSeqNoCount = this.sysResourceRepository.countSameResourceCode(sysResource.getCompanyUuid(),
					exReUuid, sysResource.getResourceCode());
			if (sameSeqNoCount > 0) {
				//sys.sysResource.duplicateResourceCode=菜单编号已存在!
				throw new BusinessException("sys.sysResource.duplicateResourceCode");
			}
		} 
	}
	
	/** 校验同级节点的序号必须不同 */
	private void validateSeqNo(SysResourceDto sysResource) {
		// 校验同级节点的序号必须不同
		long sameSeqNoCount = 0L;
		String exReUuid = StringUtil.isEmpty(sysResource.getResourceUuid()) ? "_" : sysResource.getResourceUuid();
		if (StringUtil.isEmpty(sysResource.getParentUuid())) {
			sameSeqNoCount = this.sysResourceRepository.countSameSeqNo(sysResource.getCompanyUuid(),
					exReUuid, sysResource.getTreeSeq());
		} else {
			sameSeqNoCount = this.sysResourceRepository.countSameSeqNo(sysResource.getCompanyUuid(),
					sysResource.getParentUuid(), exReUuid, sysResource.getTreeSeq());
		}
		if (sameSeqNoCount > 0) {
			//sys.sysResource.duplicateSeqNo=同节点不能有相同的树序号!
			throw new BusinessException("sys.sysResource.duplicateSeqNo");
		}
	}

	/** 更新树代码 */
	private void updateTreeCode(SysResource sysResource) {
		String prefix = "";
		if (StringUtil.isNotEmpty(sysResource.getParentUuid())) {
			SysResource parent = this.sysResourceRepository.findOne(sysResource.getParentUuid());
			prefix = parent.getTreeCode();
		} else {
			sysResource.setResourceLevel("1");
		}
		String oldTreeCode = sysResource.getTreeCode();
		//sys.sysResource.invalidParentUuid=父节点不能是当前节点自己或当前节点的子节点!
		if (StringUtil.isNotEmpty(prefix) && StringUtil.isNotEmpty(sysResource.getTreeCode()) && prefix.startsWith(oldTreeCode)) {
			throw new BusinessException("sys.sysResource.invalidParentUuid");
		}
		String newTreeCode = prefix + StringUtil.leftPad(sysResource.getTreeSeq().toString(), 3, '0');
		sysResource.setTreeCode(newTreeCode);
		sysResource.setResourceLevel(newTreeCode.length()/3 + "");
		this.sysResourceRepository.saveAndFlush(sysResource);
		this.sysResourceRepository.updateTreeCode(sysResource.getCompanyUuid(), oldTreeCode, newTreeCode);
	}
	
	
	
	@Transactional
	@CacheEvict(cacheNames="service.sysResources4Company")
	public void deleteSysResource(String id) {
		SysResource sysResource = this.sysResourceRepository.findOne(id);
		List<SysResource> sysResources=this.sysResourceRepository.findByParentUuid(sysResource.getResourceUuid());
		if(sysResources.size()==0){
			sysResource.setIsActive("N");
			this.sysResourceRepository.save(sysResource);
		}else{
			//sys.sysResource.cannotDelete.havingSubResource=有子菜单不能删除
			throw BusinessException.code("sys.sysResource.cannotDelete.havingSubResource");
		}
	}
		
	@Transactional
	@CacheEvict(cacheNames="service.sysResources4Company")
	public void deleteSysResources(List<String> resourceUuids) {
		Iterable<SysResource> sysResources = this.sysResourceRepository.findAll(resourceUuids);	
		Iterator<SysResource> it = sysResources.iterator();
		while(it.hasNext()) {
			SysResource sysResource = it.next();
			List<SysResource> sysRs=this.sysResourceRepository.findByParentUuid(sysResource.getResourceUuid());
			if(sysRs.size()==0){
				sysResource.setIsActive("N");
				this.sysResourceRepository.save(sysResource);
				}else{
					throw BusinessException.code("sys.sysResource.cannotDelete.havingSubResource");
				}
		}
	} 

	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<SysResource> pageQuery(PageRequestData request) {
		Specification<SysResource> spec = request.toSpecification(SysResource.class);
		Page<SysResource> page = this.sysResourceRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<SysResource>(page);
	}
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<SysResourceDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("t.resource_name", "text",",");
		sql.appendTableColumns(SysResource.class, "t");
		sql.append(" from SYS_RESOURCE t where t.is_active='Y' ");
		return sql.pageQuery(entityManager, request, SysResourceDto.class);
	}
	
	/**
	 * 去指定的树数据
	 * @param treeType
	 * @return
	 */
	public List<SysResourceDto> findByExample(SysResource example) {
		Sort sort = new Sort(new Order(Direction.ASC, "treeCode"));
		example.setIsActive("Y");
		List<SysResource> list = this.sysResourceRepository.findAll(Example.of(example), sort);
		return ListUtil.map(this.modelMapper, list, SysResourceDto.class);
	} 
	
	/**
	 * 转换成树形结构
	 * @param companyUuid
	 * @return
	 */
	@Transactional
//	@Cacheable(cacheNames="service.sysResources4Company", key="#companyUuid_#resourceOrigin")
	public List<SysResourceDto> findSysResourcesByCompanyUuid(String companyUuid,String resourceOrigin) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
//		sql.appendField("t.RESOURCE_UUID", "id", ",");
//		sql.appendField("t.RESOURCE_NAME", "text", ",");
//		sql.appendField("t.PARENT_UUID", "parentId", ",");
		sql.appendTableColumns(SysResource.class, "t");
		sql.append(" from SYS_RESOURCE t where t.IS_ACTIVE = 'Y' "); 
		//sql.andEqual("t.COMPANY_UUID", companyUuid);
		sql.append(" order by t.TREE_CODE asc" );
		return sql.query(entityManager, SysResourceDto.class);
	}
	
	/**
	 * RF 登陆返回菜单权限
	 * @param companyUuid
	 * @return
	 */
	@Transactional
	public List<SysResourceDto> findSysResourcesByRf(String accountUuid,String resourceOrigin) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysResource.class, "t");
		sql.append(" from SYS_RESOURCE t where t.IS_ACTIVE = 'Y' "
				+ "and exists "
				+ "(select 1 from SYS_ACCOUNT_ROLE sar, SYS_ROLE_PERMISSION o, SYS_ROLE sr, SYS_PERMISSION p "
				+ " where sar.ROLE_UUID = o.ROLE_UUID and sar.ROLE_UUID = sr.ROLE_UUID and sr.IS_ACTIVE = 'Y' "
				+ " and p.PERMISSION_UUID = o.PERMISSION_UUID and p.IS_ACTIVE = 'Y' and p.RESOURCE_UUID = t.RESOURCE_UUID"); 
		sql.andEqual("sar.ACCOUNT_UUID", accountUuid);
		sql.append(")");
		sql.andEqual("t.resource_Origin", resourceOrigin);
		sql.append(" order by t.TREE_CODE asc" );
		return sql.query(entityManager, SysResourceDto.class);
	}
	
	/**
	 * 转换成树形结构  rf
	 * @param companyUuid
	 * @return
	 */
	public List<SysResourceDto> findSysResourcesByCompanyUuidRf(String companyUuid,String resourceOrigin) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
//		sql.appendField("t.RESOURCE_UUID", "resourceUuid", ",");
//		sql.appendField("t.RESOURCE_NAME", "resourceName", ",");
//		sql.appendField("t.resource_Code", "resourceCode", ",");
//		sql.appendField("t.tree_Seq", "treeSeq", ",");
		sql.appendTableColumns(SysResource.class, "t");
		sql.append(" from SYS_RESOURCE t where t.IS_ACTIVE = 'Y' "); 
		//sql.andEqual("t.COMPANY_UUID", companyUuid);
		sql.andEqual("t.resource_Origin", resourceOrigin);
		sql.append(" order by t.TREE_CODE asc" );
		return sql.query(entityManager, SysResourceDto.class);
	}
	
	public List<SysResourceDto> findSysResourcesAllMenu() {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysResource.class, "t");
		sql.append(" from SYS_RESOURCE t where t.IS_ACTIVE = 'Y' "); 
		sql.append(" order by t.TREE_CODE asc" );
		return sql.query(entityManager, SysResourceDto.class);
	}
}

