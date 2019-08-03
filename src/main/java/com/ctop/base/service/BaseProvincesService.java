package com.ctop.base.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.BaseProvincesDto;
import com.ctop.base.entity.BaseProvinces;
import com.ctop.base.repository.BaseProvincesRepository;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.ListUtil;
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
public class BaseProvincesService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseProvincesRepository baseProvincesRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	public BaseProvincesDto getById(String id) {
		BaseProvinces baseProvinces = this.baseProvincesRepository.findOne(id);
		return modelMapper.map(baseProvinces, BaseProvincesDto.class);
	}
	
	@Transactional
	public BaseProvincesDto addBaseProvinces(BaseProvincesDto baseProvincesDto) {
		BaseProvinces baseProvinces = modelMapper.map(baseProvincesDto, BaseProvinces.class);
		baseProvinces = this.baseProvincesRepository.save(baseProvinces);
		return modelMapper.map(baseProvinces, BaseProvincesDto.class);
	} 

	@Transactional
	public BaseProvincesDto updateBaseProvinces(BaseProvincesDto baseProvincesDto) {
		BaseProvinces baseProvinces = this.baseProvincesRepository.findOne(baseProvincesDto.getProvinceUuid());
		modelMapper.map(baseProvincesDto, baseProvinces);
		baseProvinces = this.baseProvincesRepository.save(baseProvinces);
		return modelMapper.map(baseProvinces, BaseProvincesDto.class);
	}
	
	@Transactional
	public void deleteBaseProvinces(String id) {
		BaseProvinces baseProvinces = this.baseProvincesRepository.findOne(id);
		baseProvinces.setIsActive("N");
		this.baseProvincesRepository.save(baseProvinces);
	}
		
	@Transactional
	public void deleteBaseProvincess(List<String> provinceUuids) {
		Iterable<BaseProvinces> baseProvincess = this.baseProvincesRepository.findAll(provinceUuids);
		Iterator<BaseProvinces> it = baseProvincess.iterator();
		while(it.hasNext()) {
			BaseProvinces baseProvinces = it.next();
			baseProvinces.setIsActive("N");
			this.baseProvincesRepository.save(baseProvinces);
		}
	} 

	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseProvinces> pageQuery(PageRequestData request) {
		Specification<BaseProvinces> spec = request.toSpecification(BaseProvinces.class);
		Page<BaseProvinces> page = baseProvincesRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BaseProvinces>(page);
	}
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseProvincesDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseProvinces.class, "t");
		sql.append(" from BASE_PROVINCES t where 1=1 ");
		return sql.pageQuery(entityManager, request, BaseProvincesDto.class);
	}
	public List<BaseProvincesDto> getProvincess(String q) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseProvinces.class, "t");
		sql.append(" from BASE_PROVINCES t where t.country_uuid='101' ");
		if (StringUtil.isNotEmpty(q)) {
			sql.append(sql.or(sql.contains("t.PROV_NAME", q), sql.contains("t.PROV_CODE", q),
					sql.contains("t.PINYIN", q)));
		}
		return sql.query(entityManager,  BaseProvincesDto.class);
	}
	@Transactional
	public List<Map<String, String>> getCombobox() {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		List<BaseProvinces> list = this.baseProvincesRepository.findAllByIsActive();
		if(!ListUtil.isEmpty(list)){
			for(BaseProvinces pm:list){
				Map<String, String> m = new HashMap<String, String>();
				m.put("label", pm.getProvName());
				m.put("value", pm.getProvinceUuid());
				result.add(m);
			}
		}
		return result;
	} 
}

