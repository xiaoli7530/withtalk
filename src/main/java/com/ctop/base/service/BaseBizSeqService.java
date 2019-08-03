package com.ctop.base.service;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.BaseBizSeqDto;
import com.ctop.base.entity.BaseBizSeq;
import com.ctop.base.entity.BaseBizSeqDetail;
import com.ctop.base.repository.BaseBizSeqDetailRepository;
import com.ctop.base.repository.BaseBizSeqRepository;
import com.ctop.base.utils.BizSeqUtil;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.BusinessException;
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
public class BaseBizSeqService {

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseBizSeqRepository baseBizSeqRepository;
	@Autowired
	private BaseBizSeqDetailRepository baseBizSeqDetailRepository;
	@Autowired
	private ModelMapper modelMapper;

	public BaseBizSeqDto getById(String id) {
		BaseBizSeq baseBizSeq = this.baseBizSeqRepository.findOne(id);
		return modelMapper.map(baseBizSeq, BaseBizSeqDto.class);
	}
	
	
	@Transactional
	public BaseBizSeqDto addBaseBizSeq(BaseBizSeqDto baseBizSeqDto) {
		this.validateCode(baseBizSeqDto);
		BaseBizSeq baseBizSeq = modelMapper.map(baseBizSeqDto, BaseBizSeq.class);
		baseBizSeq = this.baseBizSeqRepository.save(baseBizSeq);
		return modelMapper.map(baseBizSeq, BaseBizSeqDto.class);
	}

	@Transactional
	public BaseBizSeqDto updateBaseBizSeq(BaseBizSeqDto baseBizSeqDto) {
		this.validateCode(baseBizSeqDto);
		BaseBizSeq baseBizSeq = this.baseBizSeqRepository.findOne(baseBizSeqDto.getBbsUuid());
		modelMapper.map(baseBizSeqDto, baseBizSeq);
		baseBizSeq = this.baseBizSeqRepository.save(baseBizSeq);
		return modelMapper.map(baseBizSeq, BaseBizSeqDto.class);
	}
	
	private void validateCode(BaseBizSeqDto dto) {
		String exUuid = StringUtil.isEmpty(dto.getBbsUuid()) ? "_" : dto.getBbsUuid();
		long sameCount = this.baseBizSeqRepository.countSameCode(exUuid, dto.getCode());
		if(sameCount > 0) {
			throw new BusinessException("该单据代码已存在，请检查！");
		}
	}

	@Transactional
	public void deleteBaseBizSeq(String id) {
		BaseBizSeq baseBizSeq = this.baseBizSeqRepository.findOne(id);
		baseBizSeq.setIsActive("N");
		this.baseBizSeqRepository.save(baseBizSeq);
	}

	@Transactional
	public void deleteBaseBizSeqs(List<String> bbsUuids) {
		Iterable<BaseBizSeq> baseBizSeqs = this.baseBizSeqRepository.findAll(bbsUuids);
		Iterator<BaseBizSeq> it = baseBizSeqs.iterator();
		while (it.hasNext()) {
			BaseBizSeq baseBizSeq = it.next();
			baseBizSeq.setIsActive("N");
			this.baseBizSeqRepository.save(baseBizSeq);
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseBizSeq> pageQuery(PageRequestData request) {
		Specification<BaseBizSeq> spec = request.toSpecification(BaseBizSeq.class);
		Page<BaseBizSeq> page = baseBizSeqRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BaseBizSeq>(page);
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseBizSeqDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseBizSeq.class, "t");
		sql.append(" from BASE_BIZ_SEQ t where 1=1 ");
		return sql.pageQuery(entityManager, request, BaseBizSeqDto.class);
	}

	/**
	 * 生成业务单据号 Map<String, String> map = new HashMap<String, String>();
	 * map.put("no", "NO1"); String text = generateBizSeq("test", map);
	 * System.out.println(text);
	 * 
	 * @param bizSeqCode
	 * @param params
	 * @return
	 */
	@Transactional
	public String generateBizSeq(String bizSeqCode, Object params) {
		return this.generateBizSeq(bizSeqCode, params, 1)[0];
	}
	
	@Transactional
	public String[] generateBizSeq(String bizSeqCode, Object params, int num) {
		BaseBizSeq baseBizSeq = this.baseBizSeqRepository.findByCode(bizSeqCode);
		if (baseBizSeq == null) {
			throw BusinessException.template("业务单据号{0}配置不存在!", bizSeqCode);
		}
		String bizSeqHolder = BizSeqUtil.buildBizSeqHolder(baseBizSeq, params);
		BaseBizSeqDetail baseBizSeqDetail = this.baseBizSeqDetailRepository.findByBizSeqHolder(baseBizSeq.getBbsUuid(),
				bizSeqHolder);
		if (baseBizSeqDetail == null) {
			baseBizSeqDetail = new BaseBizSeqDetail();
			baseBizSeqDetail.setBbsUuid(baseBizSeq.getBbsUuid());
			if(baseBizSeq.getInitialValue()!=null){
				baseBizSeqDetail.setSequence(baseBizSeq.getInitialValue().longValue());
			}else{
				baseBizSeqDetail.setSequence(new Long(num));
			}
			baseBizSeqDetail.setBizSeqHolder(bizSeqHolder);
			this.baseBizSeqDetailRepository.saveAndFlush(baseBizSeqDetail);
		} else {
			baseBizSeqDetail.increaseSequence(num);
			this.baseBizSeqDetailRepository.saveAndFlush(baseBizSeqDetail);
		}
		String[] bizSeqs = BizSeqUtil.buildBizSeq(baseBizSeq, params, baseBizSeqDetail, num);
		return bizSeqs;
	}
}
