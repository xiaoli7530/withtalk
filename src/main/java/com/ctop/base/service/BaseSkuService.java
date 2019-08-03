package com.ctop.base.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.BaseSkuDto;
import com.ctop.base.entity.BaseSku;
import com.ctop.base.repository.BaseSkuRepository;
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
public class BaseSkuService {

	
	
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseSkuRepository baseSkuRepository; 
	@Autowired
	private BasePinyinService basePinyinService;
	
	
	@Transactional(readOnly=true)
	public BaseSkuDto getById(String id) {
		BaseSku baseSku = this.baseSkuRepository.findOne(id);
		return CommonAssembler.assemble(baseSku, BaseSkuDto.class);
	}
	
	@Transactional
	public BaseSkuDto addBaseSku(BaseSkuDto baseSkuDto) {
		int num = baseSkuRepository.selectCodeUnique(baseSkuDto.getSkuCode());
		if(num > 0 ){
			throw new BusinessException("该零件编号已存在，请确保其唯一性");
		}
		BaseSku baseSku = CommonAssembler.assemble(baseSkuDto, BaseSku.class);
		baseSku.setSkuNamePy(basePinyinService.generatePinyin(baseSku.getSkuName()));
		baseSku = this.baseSkuRepository.save(baseSku);
		return CommonAssembler.assemble(baseSku, BaseSkuDto.class);
	} 

	@Transactional
	public BaseSkuDto updateBaseSku(BaseSkuDto baseSkuDto,Set<String> updatedProperties) {
		BaseSku baseSku = this.baseSkuRepository.findOne(baseSkuDto.getSkuUuid());
		CommonAssembler.assemble(baseSkuDto, baseSku,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		baseSku.setSkuNamePy(basePinyinService.generatePinyin(baseSku.getSkuName()));
		baseSku = this.baseSkuRepository.save(baseSku);
		return CommonAssembler.assemble(baseSku, BaseSkuDto.class);
	}
	
	@Transactional
	public void deleteBaseSku(String id) {
		BaseSku baseSku = this.baseSkuRepository.findOne(id);
		baseSku.setIsActive("N");
		this.baseSkuRepository.save(baseSku);
	}
		
	@Transactional
	public void deleteBaseSkus(List<String> skuUuids) {
		Iterable<BaseSku> baseSkus = this.baseSkuRepository.findAll(skuUuids);
		Iterator<BaseSku> it = baseSkus.iterator();
		while(it.hasNext()) {
			BaseSku baseSku = it.next();
			baseSku.setIsActive("N");
			this.baseSkuRepository.save(baseSku);
		}
	} 

	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<BaseSkuDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("bst.name", "skuTypeName",",");
		sql.appendTableColumns(BaseSku.class, "t");
		sql.append(" from BASE_SKU t  ");
		sql.append(" left join BASE_SKU_TYPE bst on t.SKU_TYPE = bst.ST_UUID and bst.IS_ACTIVE ='Y' ");
		sql.append(" where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, BaseSkuDto.class);
	}
	
	
	/**
	 * <pre>
	 * 1.判断sku_uuid是否存在，如果存在则查询出数据返回
	 * 2.根据SKU_CODE判断是否存在，如果不存在则生成新数据，如果存在则查询出数据返回
	 * </pre>
	 * @param companyUuid
	 * @param skuUuid
	 * @param skuCode
	 * @param skuName
	 * @return
	 */
	public BaseSkuDto saveWhenNotExists(String companyUuid,String skuUuid,String skuCode,String skuName,String unit) {
		BaseSkuDto skuDto = new BaseSkuDto();
		skuDto.setCompanyUuid(companyUuid);
		skuDto.setSkuUuid(skuUuid);
		skuDto.setSkuCode(skuCode);
		skuDto.setSkuName(skuName);
		skuDto.setOutUnit(unit);
		skuDto.setInUnit(unit);
		skuDto.setPrintUnit(unit);
		return this.saveWhenNotExists(skuDto);
	}
	
	/**
	 * <pre>
	 * 1.判断sku_uuid是否存在，如果存在则直接返回
	 * 2.根据SKU_CODE判断是否存在，如果不存在则生成新数据，如果存在则查询出数据返回
	 * </pre>
	 * @param skuDto
	 * @return
	 */
	public BaseSkuDto saveWhenNotExists(BaseSkuDto skuDto) {
		if(!StringUtil.isEmpty(skuDto.getSkuUuid())){
			return skuDto;
		}
		BaseSkuDto dto = this.findBySkuCode(skuDto.getCompanyUuid(), skuDto.getSkuCode());
		if(dto==null) {
			//TODO 生成拼音
			dto = this.addBaseSku(skuDto);
		}
		return dto;
	}
	
	/**
	 * 根据物料编号查询sku信息
	 * @param companyUuid
	 * @param skuCode
	 * @return
	 */
	public BaseSkuDto findBySkuCode(String companyUuid,String skuCode) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseSku.class, "t");
		sql.append(" from BASE_SKU t where t.IS_ACTIVE='Y' ");
		sql.andEqualNotNull("t.company_uuid", companyUuid);
		sql.andEqualNotNull("t.sku_code", skuCode);
		List<BaseSkuDto> dtos = sql.query(entityManager, BaseSkuDto.class);
		if(ListUtil.isEmpty(dtos)){
			return null;
		}
		return dtos.get(0);
	}
	
	/**
	 * 根据skuuuid或者skucode查询sku信息
	 * @param companyUuid
	 * @param skuUuid
	 * @param skuCode
	 * @return
	 */
	public BaseSkuDto getBaseSkuDto(String companyUuid,String skuUuid,String skuCode) {
		if(!StringUtil.isEmpty(skuUuid)){
			return this.getById(skuUuid);
		}
		BaseSkuDto dto = this.findBySkuCode(companyUuid, skuCode);
		return dto;
	}
	
	
	public List<BaseSkuDto> find4Combogrid(String companyUuid, String q) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseSku.class, "t");
		sql.append(" from BASE_SKU t where t.IS_ACTIVE='Y' ");
		sql.append("  and t.company_uuid = :companyUuid");
		if(StringUtil.isNotEmpty(q)) {
			sql.append(" and (lower(t.sku_code) like :q or lower(t.sku_name) like :q "
					+ " or lower(t.sku_name_py) like :q) ");
			sql.addParameter("q", "%" + q.toLowerCase() + "%");
		}
		sql.addParameter("companyUuid", companyUuid);
		return sql.query(entityManager, BaseSkuDto.class); 
	}
}

