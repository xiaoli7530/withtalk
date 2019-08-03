package com.ctop.base.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.BaseCompanyDto;
import com.ctop.base.entity.BaseCompany;
import com.ctop.base.repository.BaseCompanyRepository;
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
public class BaseCompanyService {

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BaseCompanyRepository baseCompanyRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private BasePinyinService basePinyinService;

	public BaseCompanyDto getById(String id) {
		BaseCompany baseCompany = this.baseCompanyRepository.findOne(id);
		if(baseCompany == null) {
			return null;
		}
		return modelMapper.map(baseCompany, BaseCompanyDto.class);
	}

	@Transactional
	public BaseCompanyDto addBaseCompany(BaseCompanyDto baseCompanyDto) {
		this.validateCompanyCode(baseCompanyDto);
		BaseCompany baseCompany = modelMapper.map(baseCompanyDto, BaseCompany.class);
		baseCompany.setCompanyNamePy(basePinyinService.generatePinyin(baseCompany.getCompanyName()));
		if(StringUtil.isEmpty(baseCompany.getCompanyUuid())) {
			baseCompany.setCompanyUuid(StringUtil.getUuid());
		}
		
		baseCompany = this.baseCompanyRepository.save(baseCompany);
		return modelMapper.map(baseCompany, BaseCompanyDto.class);
	}

	@Transactional
	public BaseCompanyDto updateBaseCompany(BaseCompanyDto baseCompanyDto,Set<String> updatedProperties) {
		this.validateCompanyCode(baseCompanyDto);
		BaseCompany baseCompany = this.baseCompanyRepository.findOne(baseCompanyDto.getCompanyUuid());
		//modelMapper.map(baseCompanyDto, baseCompany);
		CommonAssembler.assemble(baseCompanyDto, baseCompany,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		baseCompany = this.baseCompanyRepository.save(baseCompany);
		
		baseCompany.setCompanyNamePy(basePinyinService.generatePinyin(baseCompany.getCompanyName()));
		baseCompany = this.baseCompanyRepository.save(baseCompany);
		return modelMapper.map(baseCompany, BaseCompanyDto.class);
	}

	/** 校验同客户代码不能相同 */
	private void validateCompanyCode(BaseCompanyDto dto) {

		String exCompanyUuid = StringUtil.isEmpty(dto.getCompanyUuid()) ? "_" : dto.getCompanyUuid();
		long sameCount = this.baseCompanyRepository.countSameCompany(exCompanyUuid, dto.getCompanyCode());
		if (sameCount > 0) {
			throw new BusinessException("该客户编码已存在，请检查！");
		}
	}

	@Transactional
	public void deleteBaseCompany(String id) {
		BaseCompany baseCompany = this.baseCompanyRepository.findOne(id);
		if (StringUtil.equals(baseCompany.getUsableFlag(), "Y")) {
			throw new BusinessException("客户编号:" + baseCompany.getCompanyCode() + "已激活，不能删除！");
		} else {
			baseCompany.setIsActive("N");
			this.baseCompanyRepository.save(baseCompany);
		}
	}

	@Transactional
	public void deleteBaseCompanys(List<String> companyUuids) {
		Iterable<BaseCompany> baseCompanys = this.baseCompanyRepository.findAll(companyUuids);
		Iterator<BaseCompany> it = baseCompanys.iterator();
		while (it.hasNext()) {
			BaseCompany baseCompany = it.next();
			if (StringUtil.equals(baseCompany.getUsableFlag(), "Y")) {
				throw new BusinessException("客户编号:" + baseCompany.getCompanyCode() + "已激活，不能删除！");
			} else {
				baseCompany.setIsActive("N");
				this.baseCompanyRepository.save(baseCompany);
			}
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseCompany> pageQuery(PageRequestData request) {
		Specification<BaseCompany> spec = request.toSpecification(BaseCompany.class);
		Page<BaseCompany> page = baseCompanyRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<BaseCompany>(page);
	}

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @return
	 */
	public PageResponseData<BaseCompanyDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("a.COMPANY_NAME", "companyCarryName", ",");
		sql.appendTableColumns(BaseCompany.class, "t");
		sql.append(" from BASE_COMPANY t left join base_company a ");
		sql.append(" on t.TRS_NAME = a.company_uuid where t.IS_ACTIVE = 'Y' ");
		return sql.pageQuery(entityManager, request, BaseCompanyDto.class);
	}

	/**
	 * @param q
	 * @return
	 */
	public List<BaseCompanyDto> loadCompanyForCombox(String q) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseCompany.class, "t");
		sql.append(" from BASE_COMPANY t where t.IS_ACTIVE='Y'  ");
		if (StringUtil.isNotEmpty(q)) {
			sql.append(" and t.company_name like '%" + q + "%' ");
		}
		sql.append(" and rownum < 10");
		List<BaseCompanyDto> result = sql.query(entityManager, BaseCompanyDto.class);
		return result;
	}

	public List<BaseCompanyDto> findRevCustName(String companyType) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseCompany.class, "t");
		sql.append(" from BASE_COMPANY t where t.IS_ACTIVE = 'Y' ");
		sql.andEqual("t.company_type", companyType);
		return sql.query(entityManager, BaseCompanyDto.class);
	}
	
	public BaseCompanyDto findByBccUuid(String bccUuid) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(BaseCompany.class, "t");
		sql.append(" from base_company t join base_carrier_company t1 on t.company_uuid = t1.company_uuid ");
		sql.append("  where t1.is_active = 'Y' and t.is_active = 'Y' ");
		sql.andEqual("t1.bcc_uuid", bccUuid);
		List<BaseCompanyDto> lst = sql.query(entityManager, BaseCompanyDto.class);
		if(ListUtil.isEmpty(lst)) {
			return null;
		} else {
			return lst.get(0);
		}
	}
	
	public List<BaseCompanyDto> queryCarryInfo() {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("nvl((select count(1) from Mp_Shipment s where s.is_active='Y' and s.carrier_company_uuid = t.COMPANY_UUID), 0)", "carryCount", ","); 
		sql.appendTableColumns(BaseCompany.class, "t");
		sql.append(" from BASE_COMPANY t where t.IS_ACTIVE = 'Y' ");
		sql.andEqual("t.company_type", "carry");
		return sql.query(entityManager, BaseCompanyDto.class);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void synCompanyByEpms(BaseCompanyDto e) {
		BaseCompany s =this.baseCompanyRepository.findOne(e.getCompanyUuid());
		if(s!=null){
			this.updateBaseCompany(e, null);
		}else{
			this.addBaseCompany(e);
		}
	}
}
