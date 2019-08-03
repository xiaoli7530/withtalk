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
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.sys.dto.SysOperationDto;
import com.ctop.fw.sys.entity.SysOperation;
import com.ctop.fw.sys.entity.SysPermission;
import com.ctop.fw.sys.repository.SysOperationRepository;

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
public class SysOperationService {

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysOperationRepository sysOperationRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	SysPermissionService sysPermissionService;

	public SysOperationDto getById(String id) {
		SysOperation sysOperation = this.sysOperationRepository.findOne(id);
		return modelMapper.map(sysOperation, SysOperationDto.class);
	}

	@Transactional
	public SysOperationDto addSysOperation(SysOperationDto sysOperationDto) {
		SysOperation sysOperation = modelMapper.map(sysOperationDto, SysOperation.class);
		this.validateOperationCode(sysOperationDto);
		sysOperation = this.sysOperationRepository.save(sysOperation);
		return modelMapper.map(sysOperation, SysOperationDto.class);
	}

	@Transactional
	public SysOperationDto updateSysOperation(SysOperationDto sysOperationDto) {
		SysOperation sysOperation = this.sysOperationRepository.findOne(sysOperationDto.getOperationUuid());
		this.validateOperationCode(sysOperationDto);
		modelMapper.map(sysOperationDto, sysOperation);
		sysOperation = this.sysOperationRepository.save(sysOperation);
		return modelMapper.map(sysOperation, SysOperationDto.class);
	}

	private void validateOperationCode(SysOperationDto sysOperation) {
		String exOpUuid = StringUtil.isEmpty(sysOperation.getOperationUuid()) ? "_" : sysOperation.getOperationUuid();
		long sameCount = this.sysOperationRepository.countSameOperationCode(exOpUuid, sysOperation.getOperationCode(),
				sysOperation.getSeqNo());
		if (sameCount > 0) {
			throw new BusinessException("已经存在相同的操作代码或序号!");
		}
	}

	@Transactional
	public void deleteSysOperation(String id) {
		List<SysPermission> sysPermissions = this.sysPermissionService.findSysPermissionByOperationUuid(id);
		if (sysPermissions.size() == 0) {
			this.sysOperationRepository.delete(id);
		} else {
			throw BusinessException.code("已被配置，不能删除");
		}
	}

	@Transactional
	public void deleteSysOperations(List<String> operationUuids) {
		Iterable<SysOperation> sysOperations = this.sysOperationRepository.findAll(operationUuids);
		Iterator<SysOperation> it = sysOperations.iterator();
		while (it.hasNext()) {
			SysOperation sysOperation = it.next();
			List<SysPermission> sysPermissions = this.sysPermissionService.findSysPermissionByOperationUuid(sysOperation.getOperationUuid());
			if (sysPermissions.size() == 0) {
				this.sysOperationRepository.delete(sysOperation.getOperationUuid());
			} else {
				throw BusinessException.code("已被配置，不能删除");

			}
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 */
	public PageResponseData<SysOperation> pageQuery(PageRequestData request) {
		Specification<SysOperation> spec = request.toSpecification(SysOperation.class);
		Page<SysOperation> page = sysOperationRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<SysOperation>(page);
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 */
	public PageResponseData<SysOperationDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysOperation.class, "t");
		sql.append(" from SYS_OPERATION t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, SysOperationDto.class);
	}

	/**
	 * 取所有有效的操作
	 * 
	 * @param request
	 * @return
	 */
	public List<SysOperationDto> queryAllOperations() {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysOperation.class, "t");
		sql.append(" from SYS_OPERATION t where t.is_active = 'Y' order by t.SEQ_NO ");
		return sql.query(entityManager, SysOperationDto.class);
	}
}
