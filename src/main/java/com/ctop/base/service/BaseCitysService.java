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

import com.ctop.base.dto.BaseCitysDto;
import com.ctop.base.dto.BaseProvincesDto;
import com.ctop.base.entity.BaseCitys;
import com.ctop.base.entity.BaseProvinces;
import com.ctop.base.repository.BaseCitysRepository;
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
public class BaseCitysService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseCitysRepository baseCitysRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	public BaseCitysDto getById(String id) {
		BaseCitys baseCitys = this.baseCitysRepository.findOne(id);
		return modelMapper.map(baseCitys, BaseCitysDto.class);
	}
	
	@Transactional
	public BaseCitysDto addBaseCitys(BaseCitysDto baseCitysDto) {
		BaseCitys baseCitys = modelMapper.map(baseCitysDto, BaseCitys.class);
		baseCitys = this.baseCitysRepository.save(baseCitys);
		return modelMapper.map(baseCitys, BaseCitysDto.class);
	} 

	@Transactional
	public BaseCitysDto updateBaseCitys(BaseCitysDto baseCitysDto) {
		BaseCitys baseCitys = this.baseCitysRepository.findOne(baseCitysDto.getCityUuid());
		modelMapper.map(baseCitysDto, baseCitys);
		baseCitys = this.baseCitysRepository.save(baseCitys);
		return modelMapper.map(baseCitys, BaseCitysDto.class);
	}
	
	@Transactional
	public void deleteBaseCitys(String id) {
		BaseCitys baseCitys = this.baseCitysRepository.findOne(id);
		baseCitys.setIsActive("N");
		this.baseCitysRepository.save(baseCitys);
	}
		
	@Transactional
	public void deleteBaseCityss(List<String> cityUuids) {
		Iterable<BaseCitys> baseCityss = this.baseCitysRepository.findAll(cityUuids);
		Iterator<BaseCitys> it = baseCityss.iterator();
		while(it.hasNext()) {
			BaseCitys baseCitys = it.next();
			baseCitys.setIsActive("N");
			this.baseCitysRepository.save(baseCitys);
		}
	} 

	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseCitys> pageQuery(PageRequestData request) {
		Specification<BaseCitys> spec = request.toSpecification(BaseCitys.class);
		Page<BaseCitys> page = baseCitysRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BaseCitys>(page);
	}
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseCitysDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseCitys.class, "t");
		sql.append(" from BASE_CITYS t where 1=1 ");
		return sql.pageQuery(entityManager, request, BaseCitysDto.class);
	}
	
	/**
	 * 查询所有城市
	 * @param request
	 * @return
	 */
	public List<BaseCitys> findAllCitys() {
		return baseCitysRepository.findAll();
	}
	
	@Transactional
	public List<Map<String, String>> getCombobox(String provinceUuid) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		List<BaseCitys> list = null;
		if("".equals(provinceUuid)){
			list = this.baseCitysRepository.findAllByIsActive();
		}else{
			list = this.baseCitysRepository.findAllByIsActive(provinceUuid);
		}
		if(!ListUtil.isEmpty(list)){
			for(BaseCitys pm:list){
				Map<String, String> m = new HashMap<String, String>();
				m.put("label", pm.getCityName());
				m.put("value", pm.getCityUuid());
				result.add(m);
			}
		}
		return result;
	} 
	public List<BaseCitysDto> getCitys(String q,String provinceUuid) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseCitys.class, "t");
		sql.append(" from BASE_CITYS t where 1=1 ");
		if (StringUtil.isNotEmpty(provinceUuid)) {
			sql.andEqual("t.PROVINCE_UUID", provinceUuid);
		}
		if (StringUtil.isNotEmpty(q)) {
			sql.append(sql.or(sql.contains("t.CITY_NAME", q), sql.contains("t.CITY_CODE", q),
					sql.contains("t.PINYIN", q)));
		}
		return sql.query(entityManager,  BaseCitysDto.class);
	}
}

