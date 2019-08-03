package com.ctop.base.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.commons.beanutils.BeanUtilsBean2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.BaseDictDetailDto;
import com.ctop.base.dto.BaseDictDto;
import com.ctop.base.dto.BaseDictTableDto;
import com.ctop.base.dto.BaseSimpleDict;
import com.ctop.base.entity.BaseDict;
import com.ctop.base.entity.BaseDictDetail;
import com.ctop.base.entity.BaseDictTable;
import com.ctop.base.repository.BaseDictDetailRepository;
import com.ctop.base.repository.BaseDictRepository;
import com.ctop.base.repository.BaseDictTableRepository;
import com.ctop.fw.common.constants.Constants.CacheKeys;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.CommonAssembler;
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
public class BaseDictService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseDictRepository baseDictRepository;
	@Autowired
	private BaseDictTableService baseDictTableService;
	@Autowired
	private BaseDictDetailRepository baseDictDetailRepository;
	@Autowired
	private BaseDictTableRepository baseDictTableRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CacheManager cacheManager;//缓存
	
	public BaseDictDto getById(String id) {
		BaseDict baseDict = this.baseDictRepository.findOne(id);
		return modelMapper.map(baseDict, BaseDictDto.class);
	}
	
	@Transactional
	@CachePut(cacheNames="dicts", key="#baseDictDto.dictCode")
	public BaseDictDto addBaseDict(BaseDictDto baseDictDto) {
		this.validateDictCode(baseDictDto);
		BaseDict baseDict = modelMapper.map(baseDictDto, BaseDict.class);
		baseDict = this.baseDictRepository.save(baseDict);
		return modelMapper.map(baseDict, BaseDictDto.class);
	} 

	@Transactional
	@CachePut(cacheNames="dicts", key="#baseDictDto.dictCode")
	public BaseDictDto updateBaseDict(BaseDictDto baseDictDto,Set<String> updatedProperties) {
		this.validateDictCode(baseDictDto);
		BaseDict baseDict = this.baseDictRepository.findOne(baseDictDto.getDictUuid());
	//	modelMapper.map(baseDictDto, baseDict); 
		CommonAssembler.assemble(baseDictDto, baseDict,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		baseDict = this.baseDictRepository.save(baseDict);
		return modelMapper.map(baseDict, BaseDictDto.class);
	}
	
	private void validateDictCode(BaseDictDto dto) {
		String exDictUuid = StringUtil.isEmpty(dto.getDictUuid()) ? "_" : dto.getDictUuid();
		long sameCount = this.baseDictRepository.countSameDictCode(exDictUuid, dto.getDictCode());
		if(sameCount > 0) {
			//base.baseDict.duplicateDictCode=存在重复的字典代码！
			throw new BusinessException("base.baseDict.duplicateDictCode");
		}
	}
	
	@Transactional
	@CacheEvict(cacheNames="dicts", allEntries=true)
	public void deleteBaseDict(String id) {
		BaseDict baseDict = this.baseDictRepository.findOne(id);
		baseDict.setIsActive("N");
		this.baseDictRepository.save(baseDict);
	}
	
	@Transactional
	@CacheEvict(cacheNames="dicts", allEntries=true)
	public void deleteBaseDicts(List<String> dictUuids) {
		List<BaseDict>  deleteList = this.baseDictRepository.findAll(dictUuids);
		for(BaseDict dict : deleteList){
			dict.setIsActive("N");
			this.baseDictRepository.save(dict);
			this.baseDictDetailRepository.delDictDetail4Name(dict.getDictCode());
			baseDictTableRepository.delDictTable4Name(dict.getDictCode());
		}	
	} 
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseDict> pageQuery(PageRequestData request) {
		Specification<BaseDict> spec = request.toSpecification(BaseDict.class);
		
		Page<BaseDict> page = this.baseDictRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BaseDict>(page);
	}
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseDictDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseDict.class, "t");
		sql.append(" from BASE_DICT t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, BaseDictDto.class);
	}
	
	/**取出所有的字典*/
	public List<BaseDictDto> findAllAvailable() {
		List<BaseDict> baseDicts = this.baseDictRepository.findAllAvailable();
		List<BaseDictDetail> baseDictDetails = this.baseDictDetailRepository.findAllAvailable();
		List<BaseDictTable> baseDictTables = this.baseDictTableRepository.findAllAvailable();
		return baseDicts.stream().map(baseDict -> {
			BaseDictDto baseDictDto = this.modelMapper.map(baseDict, BaseDictDto.class);
			if("list".equals(baseDict.getType())) {
				List<BaseDictDetail> details = baseDictDetails.stream()
						.filter(item -> item.getDictCode() != null && item.getDictCode().equals(baseDict.getDictCode()))
						.collect(Collectors.toList());
				baseDictDetails.removeAll(details);
				List<BaseDictDetailDto> detailDtos = ListUtil.map(modelMapper, details, BaseDictDetailDto.class);
				baseDictDto.setDetails(detailDtos);
			} else {
				Optional<BaseDictTable> table = baseDictTables.stream().filter(item -> item.getDictCode().equals(baseDict.getDictCode())).findFirst();
				baseDictDto.setBaseDictTable(table.isPresent() ? modelMapper.map(table.get(), BaseDictTableDto.class) : null);
			}
			return baseDictDto;
		}).collect(Collectors.toList());
	}
	
	/**查询数据字典及其明细*/
	//@Cacheable(cacheNames="dicts", key="#dictCode")
	@Transactional
	public BaseDictDto findBaseDictWithDetails(String dictCode) {
		BaseDict baseDict = this.baseDictRepository.findByDictCode(dictCode);
		if(baseDict == null) {
			return null;
		}
		BaseDictDto baseDictDto = this.modelMapper.map(baseDict, BaseDictDto.class);
		if("list".equals(baseDict.getType())) {
			List<BaseDictDetail> details = this.baseDictDetailRepository.findByDictCodeAndIsActive(dictCode, "Y");
			List<BaseDictDetailDto> detailDtos = details.stream()
					.map(item -> modelMapper.map(item, BaseDictDetailDto.class))
					.collect(Collectors.toList());
			baseDictDto.setDetails(detailDtos);
		} else {
			BaseDictTable baseDictTable = this.baseDictTableRepository.findByDictCode(dictCode);
			if(baseDictTable != null) {
				BaseDictTableDto baseDictTableDto = modelMapper.map(baseDictTable, BaseDictTableDto.class);
				baseDictDto.setBaseDictTable(baseDictTableDto);
			}
		}
		return baseDictDto;
	}
	
	/**
	 * 取出所有的数据字典，精简化后的数据，只有code、name
	 * 没有取BaseDictTable表的数据
	 * @return
	 */
	public Map<String, List<Map<String, String>>> findAllAvailableShort() {
		List<BaseSimpleDict> baseDicts = this.findAllDicts();
		List<BaseDictTable> baseDictTables = this.baseDictTableRepository.findAllAvailable();
		
		Map<String, List<Map<String, String>>> dictsMap = new HashMap<String, List<Map<String, String>>>();
		for(int i = 0, length = baseDicts.size(); i < length; i++) {
			BaseSimpleDict dict = baseDicts.get(i);
			String dictCode = dict.getDictCode();
			String code = dict.getCode();
			String name = dict.getName();
			Map<String, String> m = new HashMap<String, String>();
			if(!dictsMap.containsKey(dictCode)) {
				List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
				m.put("code", code);
				m.put("name", name);
				detailList.add(m);
				dictsMap.put(dictCode, detailList);
			} else {
				List<Map<String, String>> detailList = dictsMap.get(dictCode);
				m.put("code", code);
				m.put("name", name);
				detailList.add(m);
				dictsMap.put(dictCode, detailList);
			}
		}
		//表关联
		for(BaseDictTable bdt : baseDictTables){
			try {
				dictsMap.put(bdt.getDictCode(), Arrays.asList(BeanUtilsBean2.getInstance().describe(bdt)));
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException("生成表关联字典失败!");
			}
	    }
		return dictsMap;
	}
	
	/**
	 * 查询所有简单字典信息
	 * @return
	 */
	public List<BaseSimpleDict> findAllDicts(){
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append(" select ");
		sql.appendField(" dict_code ", "dictCode", ",");
		sql.appendField(" code ", "code", ",");
		sql.appendField(" name ", "name");
		sql.append("  from (select * from (select dict_code, code, name from base_dict_detail d where d.is_active='Y' order by d.dict_code,d.seq_no asc) ");
		sql.append(baseDictTableService.assembleDictSql());
		sql.append(")");
		
		List<BaseSimpleDict> list = sql.query(entityManager, BaseSimpleDict.class);
		return list;
	}
	
	/**
	 * 得到数据字典的中文名称
	 * @param dictCode
	 * @param code
	 * @return
	 */
	public String getDictName(String dictCode, String code) {
		BaseDictDto dict = this.findBaseDictWithDetails(dictCode);
		if(dict == null || code == null) {
			return code;
		}
		List<BaseDictDetailDto> details = dict.getDetails();
		if(details != null) {
			for(int i = 0, length = details.size(); i < length; i++) {
				BaseDictDetailDto temp = details.get(i);
				if(code.equals(temp.getCode())) {
					return temp.getName();
				}
			}
		}
		return code;
	}
	/**
	 * 得到数据字典的英文名称
	 * @param dictCode
	 * @param code
	 * @return
	 */
	public String getDictNameEn(String dictCode, String name) {
		BaseDictDto dict = this.findBaseDictWithDetails(dictCode);
		if(dict == null || name == null) {
			return name;
		}
		List<BaseDictDetailDto> details = dict.getDetails();
		if(details != null) {
			for(int i = 0, length = details.size(); i < length; i++) {
				BaseDictDetailDto temp = details.get(i);
				if(name.equals(temp.getName())) {
					return temp.getCode();
				}
			}
		}
		return name;
	}
	
	/**取出所有的字典*/
	public List<BaseDictDto> getAllAvailable() {
		List<BaseDict> baseDicts = this.baseDictRepository.findAllAvailable();
		return baseDicts.stream().map(baseDict -> {
			BaseDictDto baseDictDto = this.modelMapper.map(baseDict, BaseDictDto.class);
			if("list".equals(baseDict.getType())) {
				baseDictDto.setRemark("手动字典");
			}else if("table".equals(baseDict.getType())) {
				baseDictDto.setRemark("自动字典");
			}else{
				baseDictDto.setRemark("是字典吗");
			}
			return baseDictDto;
		}).collect(Collectors.toList());
	}
	
	
	/**根据类型取出所有的字典*/
	public List<BaseDictDto> getAllAvailableByType(String type) {
		List<BaseDict> baseDicts = this.baseDictRepository.findAllAvailableByType(type);
		return baseDicts.stream().map(baseDict -> {
			BaseDictDto baseDictDto = this.modelMapper.map(baseDict, BaseDictDto.class);			
			return baseDictDto;
		}).collect(Collectors.toList());
	}


	/**
	 * 从缓存中加载字典信息
	 * @param refreshFlag
	 * @return
	 */
	public Map<String, List<Map<String, String>>> findAllAvailableShortWithCache(boolean refreshFlag){
		Cache cache = this.cacheManager.getCache(CacheKeys.DICT_ALL);
		if(cache==null) {
			throw new BusinessException("需开启 "+CacheKeys.DICT_ALL+" 缓存");
		}
		ValueWrapper value = cache.get("allDict");
		if(value == null||refreshFlag==true) {
			cache.put("allDict", this.findAllAvailableShort());		
			value = cache.get("allDict");
		}
		@SuppressWarnings("unchecked")
		Map<String, List<Map<String, String>>> allDict = (Map<String, List<Map<String, String>>>)(value.get());
		
		return allDict;
	}
	
	
	/**
	 * 将字典转换为中文
	 * @param dictCode
	 * @param codes
	 * @return
	 */
	public Map<String,String> getDictNameFromCache(String dictCode, Collection<String> codes) {
		Map<String,String> dictDetailMaps = new HashMap<>();
		if (StringUtil.isEmpty(dictCode) || ListUtil.isEmpty(codes)) {
			return dictDetailMaps;
		}
		Map<String, List<Map<String, String>>> dicts = this.findAllAvailableShortWithCache(false);
		if (dicts == null) {
			return dictDetailMaps;
		}
		List<Map<String, String>> dictDetails = dicts.get(dictCode);
		if (dictDetails == null) {
			return dictDetailMaps;
		}
		boolean matched = false;
		for (String code : codes) {
			matched = false;
			if(dictDetailMaps.containsKey(code)) {
				continue;
			}
			for (Map<String, String> detail : dictDetails) {
				if (StringUtil.equals(code, detail.get("code"))) {
					dictDetailMaps.put(code, detail.get("name"));
					matched = true;
					continue;
				}
			}
			if (matched == false) {
				dictDetailMaps.put(code, code);
			}
		}

		return dictDetailMaps;
	}
	
	/**
	 * 设置List 对象中字典的name字段属性
	 * @param bizObjects
	 * @param dictCode
	 * @param codeField
	 * @param nameField
	 */
	public void setObjectDictNameField(List<?> bizObjects, String dictCode, String codeField, String nameField) {
		if (ListUtil.isEmpty(bizObjects)) {
			return;
		}
		List<String> units = ListUtil.getOneFieldValue(bizObjects, codeField, String.class);
		Map<String, String> dictDetails = this.getDictNameFromCache(dictCode, units);

		for (Object detail : bizObjects) {
			BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(detail);
			String code = (String) bw.getPropertyValue(codeField);
			bw.setPropertyValue(nameField, dictDetails.getOrDefault(code, code));
		}
	}
	
	/**
	 * 设置对象中字典的name字段属性
	 * @param bizObject
	 * @param dictCode
	 * @param codeField
	 * @param nameField
	 */
	public void setObjectDictNameField(Object bizObject, String dictCode, String codeField, String nameField) {
		List<Object> bizObjects= new ArrayList<>();
		bizObjects.add(bizObject);
		this.setObjectDictNameField(bizObjects, dictCode, codeField, nameField);
	}
}

