package com.ctop.base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ctop.base.entity.BaseCompany;

/** 
 */
public interface BaseCompanyRepository
		extends JpaRepository<BaseCompany, String>, JpaSpecificationExecutor<BaseCompany> {
	@Query(nativeQuery = true, value = "select count(1) from BASE_COMPANY w where COMPANY_UUID != ?1 and w.COMPANY_CODE = ?2 and w.IS_ACTIVE='Y' ")
	public long countSameCompany(String exCompanyUuid, String companyCode);

	@Query(nativeQuery = true, value = "select count(1) from BASE_COMPANY w where w.WOR_UUID = ?1  and w.IS_ACTIVE='Y' ")
	public long countSameOutRule(String worUuid);

	@Query("select t from BaseCompany t where t.isActive='Y' and t.companyType = ?1 ")
	public List<BaseCompany> findRevCustName(String companyType);
}
