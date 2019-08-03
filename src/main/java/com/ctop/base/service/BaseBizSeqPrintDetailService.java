package com.ctop.base.service;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.BaseBizSeqPrintDetailDto;
import com.ctop.base.dto.BaseBizSeqPrintDto;
import com.ctop.base.entity.BaseBizSeqPrint;
import com.ctop.base.entity.BaseBizSeqPrintDetail;
import com.ctop.base.repository.BaseBizSeqPrintDetailRepository;
import com.ctop.base.repository.BaseBizSeqPrintRepository;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;


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
public class BaseBizSeqPrintDetailService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseBizSeqPrintDetailRepository baseBizSeqPrintDetailRepository;
	@Autowired
	private BaseBizSeqPrintRepository baseBizSeqPrintRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private BaseBizSeqPrintService baseBizSeqPrintService;
	
	public BaseBizSeqPrintDetailDto getById(String id) {
		BaseBizSeqPrintDetail baseBizSeqPrintDetail = this.baseBizSeqPrintDetailRepository.findOne(id);
		return modelMapper.map(baseBizSeqPrintDetail, BaseBizSeqPrintDetailDto.class);
	}
	
	//根据打印类型跟编码查询详细信息
	public String selectBbspdByNo(String bizSeqCode,String bizSeqNo){
		String start=null;
		BaseBizSeqPrintDetail baseBizSeqPrintDetail = this.baseBizSeqPrintDetailRepository.selectBbspdByNo(bizSeqCode, bizSeqNo);
		if(null == baseBizSeqPrintDetail){
			start="条码不存在";
		}else if(baseBizSeqPrintDetail.getUsed().equals("Y")){
			start="条码已使用";
		}else{
			//把状态设置为已使用
			baseBizSeqPrintDetail.setUsed("Y");
			//更新打印明细表
			BaseBizSeqPrintDetailDto dtoDetail = modelMapper.map(baseBizSeqPrintDetail, BaseBizSeqPrintDetailDto.class);
			updateBaseBizSeqPrintDetail(dtoDetail);
			//更新打印头表
			BaseBizSeqPrint baseBizSeqPrint =  baseBizSeqPrintRepository.selectBbspByUuid(baseBizSeqPrintDetail.getBbspUuid());
			baseBizSeqPrint.setQuantity(baseBizSeqPrint.getQuantity()-1);
			BaseBizSeqPrintDto dto = modelMapper.map(baseBizSeqPrint, BaseBizSeqPrintDto.class);
			baseBizSeqPrintService.updateBaseBizSeqPrint(dto);
			start="成功使用条码";
		}
		return start;
	}
	
	@Transactional
	public BaseBizSeqPrintDetailDto addBaseBizSeqPrintDetail(BaseBizSeqPrintDetailDto baseBizSeqPrintDetailDto) {
		BaseBizSeqPrintDetail baseBizSeqPrintDetail = modelMapper.map(baseBizSeqPrintDetailDto, BaseBizSeqPrintDetail.class);
		baseBizSeqPrintDetail = this.baseBizSeqPrintDetailRepository.save(baseBizSeqPrintDetail);
		return modelMapper.map(baseBizSeqPrintDetail, BaseBizSeqPrintDetailDto.class);
	} 

	@Transactional
	public BaseBizSeqPrintDetailDto updateBaseBizSeqPrintDetail(BaseBizSeqPrintDetailDto baseBizSeqPrintDetailDto) {
		BaseBizSeqPrintDetail baseBizSeqPrintDetail = this.baseBizSeqPrintDetailRepository.findOne(baseBizSeqPrintDetailDto.getBbspdUuid());
		modelMapper.map(baseBizSeqPrintDetailDto, baseBizSeqPrintDetail);
		baseBizSeqPrintDetail = this.baseBizSeqPrintDetailRepository.save(baseBizSeqPrintDetail);
		return modelMapper.map(baseBizSeqPrintDetail, BaseBizSeqPrintDetailDto.class);
	}
	
	@Transactional
	public void deleteBaseBizSeqPrintDetail(String id) {
		BaseBizSeqPrintDetail baseBizSeqPrintDetail = this.baseBizSeqPrintDetailRepository.findOne(id);
		baseBizSeqPrintDetail.setIsActive("N");
		this.baseBizSeqPrintDetailRepository.save(baseBizSeqPrintDetail);
	}
		
	@Transactional
	public void deleteBaseBizSeqPrintDetails(List<String> bbspdUuids) {
		Iterable<BaseBizSeqPrintDetail> baseBizSeqPrintDetails = this.baseBizSeqPrintDetailRepository.findAll(bbspdUuids);
		Iterator<BaseBizSeqPrintDetail> it = baseBizSeqPrintDetails.iterator();
		while(it.hasNext()) {
			BaseBizSeqPrintDetail baseBizSeqPrintDetail = it.next();
			baseBizSeqPrintDetail.setIsActive("N");
			this.baseBizSeqPrintDetailRepository.save(baseBizSeqPrintDetail);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	public PageResponseData<BaseBizSeqPrintDetail> pageQuery(PageRequestData request) {
		Specification<BaseBizSeqPrintDetail> spec = request.toSpecification(BaseBizSeqPrintDetail.class);
		Page<BaseBizSeqPrintDetail> page = baseBizSeqPrintDetailRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BaseBizSeqPrintDetail>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseBizSeqPrintDetailDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseBizSeqPrintDetail.class, "t");
		sql.append(" from BASE_BIZ_SEQ_PRINT_DETAIL t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, BaseBizSeqPrintDetailDto.class);
	}
	
}

