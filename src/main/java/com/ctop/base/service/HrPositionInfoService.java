package com.ctop.base.service;

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

import com.ctop.base.entity.HrPositionInfo;
import com.ctop.base.repository.HrPositionInfoRepository;
import com.ctop.base.dto.HrPositionInfoDto;
import com.ctop.base.entity.HrPositionInfo;


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
public class HrPositionInfoService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private HrPositionInfoRepository hrPositionInfoRepository; 
	
	@Transactional(readOnly=true)
	public HrPositionInfoDto getById(String id) {
		HrPositionInfo hrPositionInfo = this.hrPositionInfoRepository.findOne(id);
		return CommonAssembler.assemble(hrPositionInfo, HrPositionInfoDto.class);
	}
	
	@Transactional
	public HrPositionInfoDto addHrPositionInfo(HrPositionInfoDto hrPositionInfoDto) {
		HrPositionInfo hrPositionInfo = CommonAssembler.assemble(hrPositionInfoDto, HrPositionInfo.class);
		if(StringUtil.isEmpty(hrPositionInfo.getPositionInfoUuid())) {
			hrPositionInfo.setPositionInfoUuid(StringUtil.getUuid());
		}
		hrPositionInfo = this.hrPositionInfoRepository.save(hrPositionInfo);
		return CommonAssembler.assemble(hrPositionInfo, HrPositionInfoDto.class);
	} 

	@Transactional
	public HrPositionInfoDto updateHrPositionInfo(HrPositionInfoDto hrPositionInfoDto,Set<String> updatedProperties) {
		HrPositionInfo hrPositionInfo = this.hrPositionInfoRepository.findOne(hrPositionInfoDto.getPositionInfoUuid());
		CommonAssembler.assemble(hrPositionInfoDto, hrPositionInfo,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		hrPositionInfo = this.hrPositionInfoRepository.save(hrPositionInfo);
		return CommonAssembler.assemble(hrPositionInfo, HrPositionInfoDto.class);
	}
	
	@Transactional
	public void deleteHrPositionInfo(String id) {
		HrPositionInfo hrPositionInfo = this.hrPositionInfoRepository.findOne(id);
		hrPositionInfo.setIsActive("N");
		this.hrPositionInfoRepository.save(hrPositionInfo);
	}
		
	@Transactional
	public void deleteHrPositionInfos(List<String> positionInfoUuids) {
		Iterable<HrPositionInfo> hrPositionInfos = this.hrPositionInfoRepository.findAll(positionInfoUuids);
		Iterator<HrPositionInfo> it = hrPositionInfos.iterator();
		while(it.hasNext()) {
			HrPositionInfo hrPositionInfo = it.next();
			hrPositionInfo.setIsActive("N");
			this.hrPositionInfoRepository.save(hrPositionInfo);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	@Transactional(readOnly=true)
	public PageResponseData<HrPositionInfo> pageQuery(PageRequestData request) {
		Specification<HrPositionInfo> spec = request.toSpecification(HrPositionInfo.class);
		Page<HrPositionInfo> page = hrPositionInfoRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<HrPositionInfo>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<HrPositionInfoDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(HrPositionInfo.class, "t");
		sql.append(" from HR_POSITION_INFO t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, HrPositionInfoDto.class);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void synHpiByEpms(HrPositionInfoDto e) {
		HrPositionInfo s = this.hrPositionInfoRepository.findOne(e.getPositionInfoUuid());
		if(s != null){
			this.updateHrPositionInfo(e, null);
		}else{
			this.addHrPositionInfo(e);
		}
	}
	
}

