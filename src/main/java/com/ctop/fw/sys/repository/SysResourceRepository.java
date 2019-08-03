package com.ctop.fw.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ctop.fw.sys.entity.SysResource;

/** 
 */
public interface SysResourceRepository extends JpaRepository<SysResource, String>, JpaSpecificationExecutor<SysResource> {

	@Query("select b from SysResource b where b.parentUuid = ?1 and b.isActive='Y' ")
	public List<SysResource> findByParentUuid(String parentUuid);

	@Query("select b.resourceCode from SysResource b where b.parentUuid=?1 and  b.isActive='Y' ")
	public List<String> findAllResourceCode(String parentUuid);

	@Query("select b.resourceCode from SysResource b where b.parentUuid is null and  b.isActive='Y' ")
	public List<String> findAllResourceCode();
	
	/**
	 * 查询货主的某菜单节点的子节点中的指定序号数量；用来校验同节点下序号唯一
	 * @param companyUuid
	 * @param parentUuid
	 * @param stUuid
	 * @param seqNo
	 * @return
	 */
	@Query(nativeQuery = true, value = "select count(1) from SYS_RESOURCE a where a.COMPANY_UUID = ?1 and a.PARENT_UUID = ?2 and a.RESOURCE_UUID != ?3 and a.TREE_SEQ = ?4 and a.IS_ACTIVE='Y' ")
	public long countSameSeqNo(String companyUuid, String parentUuid, String resourceUuid, Long seqNo);
	
	/**
	 * 查询货主的顶级菜单节点中的指定序号数量；用来校验同节点下序号唯一
	 * @param companyUuid
	 * @param parentUuid
	 * @param stUuid
	 * @param seqNo
	 * @return
	 */
	@Query(nativeQuery = true, value = "select count(1) from SYS_RESOURCE a where a.COMPANY_UUID = ?1 and a.PARENT_UUID is null and a.RESOURCE_UUID != ?2 and a.TREE_SEQ = ?3 and a.IS_ACTIVE='Y' ")
	public long countSameSeqNo(String companyUuid, String resourceUuid, Long seqNo);
	

	@Modifying
	@Query(nativeQuery = true, value="update SYS_RESOURCE b set b.TREE_CODE=regexp_replace(TREE_CODE, ?2, ?3, 1, 1) where b.TREE_CODE like ?2 || '%'  and b.IS_ACTIVE='Y' and b.COMPANY_UUID =?1 ")
	public int updateTreeCode(String companyUuid, String oldTreeCode, String newTreeCode);
	 
	
	/**
	 * 查询货主的的同菜单编号的菜单数量
	 * @param companyUuid
	 * @param parentUuid
	 * @param stUuid
	 * @param seqNo
	 * @return
	 */
	@Query(nativeQuery = true, value = "select count(1) from SYS_RESOURCE a where a.COMPANY_UUID = ?1 and a.RESOURCE_UUID != ?2 and a.RESOURCE_CODE = ?3 and a.IS_ACTIVE='Y' ")
	public long countSameResourceCode(String companyUuid, String resourceUuid, String resourceCode);
}
