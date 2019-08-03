package com.ctop.base.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.BaseCitysDto;
import com.ctop.base.dto.BaseCountrysDto;
import com.ctop.base.dto.BaseProvincesDto;
import com.ctop.base.entity.BaseCitys;
import com.ctop.base.entity.BaseCountrys;
import com.ctop.base.entity.BaseProvinces;
import com.ctop.base.repository.BaseCountrysRepository;
import com.ctop.fw.common.constants.Constants;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
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
public class BaseCountrysService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseCountrysRepository baseCountrysRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	public BaseCountrysDto getById(String id) {
		BaseCountrys baseCountrys = this.baseCountrysRepository.findOne(id);
		return modelMapper.map(baseCountrys, BaseCountrysDto.class);
	}
	
	@Transactional
	public BaseCountrysDto addBaseCountrys(BaseCountrysDto baseCountrysDto) {
		BaseCountrys baseCountrys = modelMapper.map(baseCountrysDto, BaseCountrys.class);
		baseCountrys = this.baseCountrysRepository.save(baseCountrys);
		return modelMapper.map(baseCountrys, BaseCountrysDto.class);
	} 

	@Transactional
	public BaseCountrysDto updateBaseCountrys(BaseCountrysDto baseCountrysDto) {
		BaseCountrys baseCountrys = this.baseCountrysRepository.findOne(baseCountrysDto.getCountryUuid());
		modelMapper.map(baseCountrysDto, baseCountrys);
		baseCountrys = this.baseCountrysRepository.save(baseCountrys);
		return modelMapper.map(baseCountrys, BaseCountrysDto.class);
	}
	
	@Transactional
	public void deleteBaseCountrys(String id) {
		BaseCountrys baseCountrys = this.baseCountrysRepository.findOne(id);
		baseCountrys.setIsActive("N");
		this.baseCountrysRepository.save(baseCountrys);
	}
		
	@Transactional
	public void deleteBaseCountryss(List<String> countryUuids) {
		Iterable<BaseCountrys> baseCountryss = this.baseCountrysRepository.findAll(countryUuids);
		Iterator<BaseCountrys> it = baseCountryss.iterator();
		while(it.hasNext()) {
			BaseCountrys baseCountrys = it.next();
			baseCountrys.setIsActive("N");
			this.baseCountrysRepository.save(baseCountrys);
		}
	} 

	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseCountrys> pageQuery(PageRequestData request) {
		Specification<BaseCountrys> spec = request.toSpecification(BaseCountrys.class);
		Page<BaseCountrys> page = baseCountrysRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BaseCountrys>(page);
	}
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseCountrysDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseCountrys.class, "t");
		sql.append(" from BASE_COUNTRYS t where 1=1 ");
		return sql.pageQuery(entityManager, request, BaseCountrysDto.class);
	}
	
	/**取所有的国家 省份，城市数据*/
	@Transactional
	public List<BaseCountrysDto> findAllCountrysAndAreas() {
		List<BaseCountrysDto> countrys = this.findAllCountrys();
		List<BaseProvincesDto> provinces = this.findAllProvinces();
		List<BaseCitysDto> citys = this.findAllCitys();
		for(BaseProvincesDto province : provinces) {
			List<BaseCitysDto> coCitys = new ArrayList<BaseCitysDto>();
			Iterator<BaseCitysDto> it = citys.iterator();
			while(it.hasNext()) {
				BaseCitysDto city = it.next();
				if(province.getProvinceUuid().equals(city.getProvinceUuid())) {
					coCitys.add(city);
					it.remove();
				}
			} 
			province.setCitys(coCitys); 
		}
		for(BaseCountrysDto country : countrys) {
			List<BaseProvincesDto> coProvinces = new ArrayList<BaseProvincesDto>();
			Iterator<BaseProvincesDto> it  = provinces.iterator();
			while(it.hasNext()) {
				BaseProvincesDto province = it.next();
				if(country.getCountryUuid().equals(province.getCountryUuid())) {
					coProvinces.add(province);
					it.remove();
				}
			}
			country.setProvinces(coProvinces);
		}
		return countrys;
	}
	
	private List<BaseCountrysDto> findAllCountrys() {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseCountrys.class, "t");
		sql.append(" from BASE_COUNTRYS t where t.IS_ACTIVE='Y' order by SEQ_NO asc ");
		return sql.query(entityManager, BaseCountrysDto.class);
	}
	
	private List<BaseProvincesDto> findAllProvinces() {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseProvinces.class, "t");
		sql.append(" from BASE_PROVINCES t where t.IS_ACTIVE='Y' order by SEQ_NO asc");
		return sql.query(entityManager, BaseProvincesDto.class);
	}
	
	private List<BaseCitysDto> findAllCitys() {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseCitys.class, "t");
		sql.append(" from BASE_CITYS t where t.IS_ACTIVE='Y' order by SEQ_NO asc");
		return sql.query(entityManager, BaseCitysDto.class);
	}

}