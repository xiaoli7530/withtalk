package com.ctop.base.service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.BaseDictTableDto;
import com.ctop.base.entity.BaseDictTable;
import com.ctop.base.repository.BaseDictTableRepository;
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
public class BaseDictTableService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseDictTableRepository baseDictTableRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	public BaseDictTableDto getById(String id) {
		BaseDictTable baseDictTable = this.baseDictTableRepository.findOne(id);
		return modelMapper.map(baseDictTable, BaseDictTableDto.class);
	}
	
	@Transactional
	@CacheEvict(cacheNames="dicts", key="#baseDictTableDto.dictCode")
	public BaseDictTableDto addBaseDictTable(BaseDictTableDto baseDictTableDto) {
		BaseDictTable baseDictTable = modelMapper.map(baseDictTableDto, BaseDictTable.class);
		baseDictTable = this.baseDictTableRepository.save(baseDictTable);
		return modelMapper.map(baseDictTable, BaseDictTableDto.class);
	} 

	@Transactional
	@CacheEvict(cacheNames="dicts", key="#baseDictTableDto.dictCode")
	public BaseDictTableDto updateBaseDictTable(BaseDictTableDto baseDictTableDto) {
		BaseDictTable baseDictTable = this.baseDictTableRepository.findOne(baseDictTableDto.getBdtUuid());
		modelMapper.map(baseDictTableDto, baseDictTable);
		baseDictTable = this.baseDictTableRepository.save(baseDictTable);
		return modelMapper.map(baseDictTable, BaseDictTableDto.class);
	}
	
	@Transactional
	@CacheEvict(cacheNames="dicts", allEntries=true)
	public void deleteBaseDictTable(String id) {
		BaseDictTable baseDictTable = this.baseDictTableRepository.findOne(id);
		baseDictTable.setIsActive("N");
		this.baseDictTableRepository.save(baseDictTable);
	}
		
	@Transactional
	@CacheEvict(cacheNames="dicts", allEntries=true)
	public void deleteBaseDictTables(List<String> bdtUuids) {
		Iterable<BaseDictTable> baseDictTables = this.baseDictTableRepository.findAll(bdtUuids);
		Iterator<BaseDictTable> it = baseDictTables.iterator();
		while(it.hasNext()) {
			BaseDictTable baseDictTable = it.next();
			baseDictTable.setIsActive("N");
			this.baseDictTableRepository.save(baseDictTable);
		}
	} 

	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseDictTable> pageQuery(PageRequestData request) {
		Specification<BaseDictTable> spec = request.toSpecification(BaseDictTable.class);
		Page<BaseDictTable> page = this.baseDictTableRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BaseDictTable>(page);
	}
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseDictTableDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseDictTable.class, "t");
		sql.append(" from BASE_DICT_TABLE t where 1=1 ");
		return sql.pageQuery(entityManager, request, BaseDictTableDto.class);
	}
	 
	/**
	 * 根据表关联数据字典配置，取
	 * @param bdtUuid
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryWithBaseDictTable(String bdtUuid, Map<String, Object> params) {
		BaseDictTable baseDictTable = this.baseDictTableRepository.findOne(bdtUuid);
		if(baseDictTable == null) {
			return Collections.emptyList();
		}
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		Session session = this.entityManager.unwrap(Session.class);
		String sql = baseDictTable.buildFullSql();
		SQLQuery query = session.createSQLQuery(sql); 
		query.setProperties(params);
		query.setFirstResult(0);
		query.setMaxResults(100);
		//query.setMaxResults(200);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> result2 = query.list();
		
		
		if(params.get("_defaultValue") != null) {
			String sql2 = "select * from (" + baseDictTable.getSql() + ") t where t." + baseDictTable.getColumnKey() + "=:_defaultValue ";
			SQLQuery query2 = session.createSQLQuery(sql2); 
			
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("_defaultValue", params.get("_defaultValue"));
			query2.setProperties(params2);
			
			query2.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List<Map<String, Object>> defaultList = query2.list();
			result.addAll(defaultList);
		}
		result.addAll(result2);
		
		return result;
	}
	
	/**
	 * 组合字典查询
	 * @return
	 */
	public String assembleDictSql(){
		StringBuilder sb = new StringBuilder();
		List<BaseDictTable> list = baseDictTableRepository.findDictTableAvailable();
		for(BaseDictTable dbt:list){
			sb.append(" union all ");
			sb.append(dbt.buildFullSql());
		}
		return sb.toString();
	}
}

