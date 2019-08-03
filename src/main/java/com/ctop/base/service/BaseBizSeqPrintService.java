package com.ctop.base.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.BaseBizSeqPrintDto;
import com.ctop.base.entity.BaseBizSeqPrint;
import com.ctop.base.entity.BaseBizSeqPrintDetail;
import com.ctop.base.repository.BaseBizSeqPrintDetailRepository;
import com.ctop.base.repository.BaseBizSeqPrintRepository;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.UserContextUtil;


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
public class BaseBizSeqPrintService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseBizSeqPrintRepository baseBizSeqPrintRepository;
	@Autowired
	private BaseBizSeqPrintDetailRepository baseBizSeqPrintDetailRepository;
	@Autowired
	private ModelMapper modelMapper;
    @Autowired
    private BaseBizSeqService baseBizSeqService;
	
	
	public BaseBizSeqPrintDto getById(String id) {
		BaseBizSeqPrint baseBizSeqPrint = this.baseBizSeqPrintRepository.findOne(id);
		List<String> bizSeqNos = this.baseBizSeqPrintDetailRepository.selectSeqNoById(baseBizSeqPrint.getBbspUuid());
		BaseBizSeqPrintDto dto = modelMapper.map(baseBizSeqPrint, BaseBizSeqPrintDto.class);
		dto.setBizSeqNos(bizSeqNos);
		return dto;
	}
	
	@Transactional
	public BaseBizSeqPrintDto addBaseBizSeqPrint(BaseBizSeqPrintDto baseBizSeqPrintDto) {
		BaseBizSeqPrint baseBizSeqPrint = modelMapper.map(baseBizSeqPrintDto, BaseBizSeqPrint.class);
		String[] arr = baseBizSeqService.generateBizSeq(baseBizSeqPrintDto.getBizSeqCode(), null, baseBizSeqPrintDto.getQuantity().intValue());
		List<BaseBizSeqPrintDetail> details = new ArrayList<BaseBizSeqPrintDetail>();
		baseBizSeqPrint.setDetails(details);
		for(String bizSeqNo : arr) {
			BaseBizSeqPrintDetail detail = new BaseBizSeqPrintDetail();
			detail.setBbsUuid(baseBizSeqPrint.getBbsUuid());
			detail.setBizSeqNo(bizSeqNo);
			detail.setUsed("N");
			details.add(detail);
		}
		baseBizSeqPrint.setBizSeqMax(arr[arr.length - 1]);
		baseBizSeqPrint.setBizSeqMin(arr[0]);
		baseBizSeqPrint = this.baseBizSeqPrintRepository.saveAndFlush(baseBizSeqPrint); 
		BaseBizSeqPrintDto dto = modelMapper.map(baseBizSeqPrint, BaseBizSeqPrintDto.class);
//		dto.setDetails(ListUtil.map(modelMapper, details, BaseBizSeqPrintDetail.class));
		//在list集合中的某个属性设置给对象属性
		dto.setBizSeqNos(ListUtil.getOneFieldValue(details, "bizSeqNo", String.class));
		return dto;
	} 

	@Transactional
	public BaseBizSeqPrintDto updateBaseBizSeqPrint(BaseBizSeqPrintDto baseBizSeqPrintDto) {
		BaseBizSeqPrint baseBizSeqPrint = this.baseBizSeqPrintRepository.findOne(baseBizSeqPrintDto.getBbspUuid());
		modelMapper.map(baseBizSeqPrintDto, baseBizSeqPrint);
		baseBizSeqPrint = this.baseBizSeqPrintRepository.save(baseBizSeqPrint);		
		return modelMapper.map(baseBizSeqPrint, BaseBizSeqPrintDto.class);
	}
	
	@Transactional
	public void deleteBaseBizSeqPrint(String id) {
		BaseBizSeqPrint baseBizSeqPrint = this.baseBizSeqPrintRepository.findOne(id);
		baseBizSeqPrint.setIsActive("N");
		this.baseBizSeqPrintRepository.save(baseBizSeqPrint);
	}
		
	@Transactional
	public void deleteBaseBizSeqPrints(List<String> bbspUuids) {
		Iterable<BaseBizSeqPrint> baseBizSeqPrints = this.baseBizSeqPrintRepository.findAll(bbspUuids);
		Iterator<BaseBizSeqPrint> it = baseBizSeqPrints.iterator();
		while(it.hasNext()) {
			BaseBizSeqPrint baseBizSeqPrint = it.next();
			baseBizSeqPrint.setIsActive("N");
			this.baseBizSeqPrintRepository.save(baseBizSeqPrint);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	
	/*public PageResponseData<BaseBizSeqPrint> pageQuery(PageRequestData request) {
		Specification<BaseBizSeqPrint> spec = request.toSpecification(BaseBizSeqPrint.class);
		Page<BaseBizSeqPrint> page = baseBizSeqPrintRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BaseBizSeqPrint>(page);
	}
	*/
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseBizSeqPrintDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("bbs.NAME", "bizName", ",");
		sql.appendField("sa.NAME", "printName", ",");
		sql.appendTableColumns(BaseBizSeqPrint.class, "t");
		sql.append(" from BASE_BIZ_SEQ_PRINT t left join base_biz_seq bbs on(t.bbs_uuid=bbs.bbs_uuid and bbs.is_active='Y')"
				+"left join sys_account sa on(t.printer_name=sa.account_uuid and sa.is_active='Y') where t.IS_ACTIVE='Y' ");
		sql.andEqual("t.printer_name", UserContextUtil.getAccountUuid());		
		return sql.pageQuery(entityManager, request, BaseBizSeqPrintDto.class);
	}
	
}

