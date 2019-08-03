package com.ctop.fw.sys.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.common.utils.UserContextUtil;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.sys.entity.SysRemind;
import com.ctop.fw.sys.repository.SysRemindRepository;
import com.ctop.fw.sys.dto.SysRemindDto;



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
public class SysRemindService {


	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysRemindRepository sysRemindRepository; 

	@Transactional(readOnly=true)
	public SysRemindDto getById(String id) {
		SysRemind sysRemind = this.sysRemindRepository.findOne(id);
		return CommonAssembler.assemble(sysRemind, SysRemindDto.class);
	}
	
	@Transactional
	public SysRemindDto addSysRemind(SysRemindDto sysRemindDto) {
		SysRemind sysRemind = CommonAssembler.assemble(sysRemindDto, SysRemind.class);
		sysRemind = this.sysRemindRepository.save(sysRemind);
		return CommonAssembler.assemble(sysRemind, SysRemindDto.class);
	} 

	@Transactional
	public SysRemindDto updateSysRemind(SysRemindDto sysRemindDto,Set<String> updatedProperties) {
		SysRemind sysRemind = this.sysRemindRepository.findOne(sysRemindDto.getRemindUuid());
		CommonAssembler.assemble(sysRemindDto, sysRemind,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		sysRemind = this.sysRemindRepository.save(sysRemind);
		return CommonAssembler.assemble(sysRemind, SysRemindDto.class);
	}
	
	/**
	 * 更新提醒表数据
	 * @param refUuid  业务表主键
	 * @param flowStatus  流程状态，目前传：FlowStatus4Ps2.AUDIT_COMPLATE
	 * @return
	 */
	@Transactional
	public SysRemindDto updateSysRemindByRefUuid(String refUuid,String flowStatus) {
		SysRemind sysRemind = new SysRemind();
		if(StringUtil.isNotEmpty(refUuid)){
			SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
			sql.append("select ");
			sql.appendTableColumns(SysRemind.class, "t");
			sql.append(" from SYS_REMIND t  where t.is_active='Y' and t.in_system='ppobuild' ");
			sql.append(" and t.opration_type='startProcess' and t.flow_status <> 'AUDIT_COMPLATE' ");
			sql.andEqual("t.ref_uuid", refUuid);
			sql.append(" order by t.updated_date desc ");
			List<SysRemind> list = sql.query(entityManager, SysRemind.class);
		}		
		return CommonAssembler.assemble(sysRemind, SysRemindDto.class);
	}
	
	/**
	 * 根据taskId更新为已处理
	 * @param taskId
	 * @return
	 */
	@Transactional
	public SysRemindDto updateSysRemind(String taskId){
		SysRemind sysRemind = new SysRemind();
		sysRemind.setIsActive("Y");
		sysRemind.setTaskId(taskId);
		List<SysRemind> list = this.sysRemindRepository.findAll(Example.of(sysRemind));
		if(!ListUtil.isNullOrEmpty(list)){			
			for(SysRemind remind : list){
				remind.setHandleStatus("Y");
				remind.setFirstReadTime(new Date());
				remind.setRemindStatus("read");
				remind.setHandleTime(new Date());
				remind.setUpdatedDate(new Date());
				remind.setUpdatedBy(UserContextUtil.getAccountUuid());
				sysRemind = this.sysRemindRepository.save(remind);
			}			
		}
		return CommonAssembler.assemble(sysRemind, SysRemindDto.class);
	}
	
	@Transactional
	public void deleteSysRemind(String id) {
		SysRemind sysRemind = this.sysRemindRepository.findOne(id);
		sysRemind.setIsActive("N");
		this.sysRemindRepository.save(sysRemind);
	}
		
	/**
	 * 
	 * @param remindUuids
	 */
	@Transactional
	public void deleteSysReminds(List<String> remindUuids) {
		Iterable<SysRemind> sysReminds = this.sysRemindRepository.findAll(remindUuids);
		Iterator<SysRemind> it = sysReminds.iterator();
		while(it.hasNext()) {
			SysRemind sysRemind = it.next();
			sysRemind.setIsActive("N");
			this.sysRemindRepository.save(sysRemind);
		}
	} 
	
	/**
	 * 表为已读
	 * @param remindUuids
	 */
	@Transactional
	public void updateStatusByRemindUuids(List<String> remindUuids) {
		Iterable<SysRemind> sysReminds = this.sysRemindRepository.findAll(remindUuids);
		Iterator<SysRemind> it = sysReminds.iterator();
		while(it.hasNext()) {
			SysRemind sysRemind = it.next();
			if(StringUtil.equals(sysRemind.getRemindFlowType(),"UR")){
				sysRemind.setHandleStatus("Y");
			}
			sysRemind.setFirstReadTime(new Date());
			sysRemind.setRemindStatus("read");
			sysRemind.setHandleTime(new Date());
			sysRemind.setUpdatedDate(new Date());
			sysRemind.setUpdatedBy(UserContextUtil.getAccountUuid());
			this.sysRemindRepository.save(sysRemind);
		}
	}

  
	
	/**
	 * todolist
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<SysRemindDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysRemind.class, "t");
		sql.append(" from SYS_REMIND t where t.IS_ACTIVE='Y' and t.in_system='ppobuild' ");
		sql.append(" and ( 1=1 ");
		sql.andEqual("t.user_id", UserContextUtil.getEmpUuid());
		sql.append(" or t.user_id in ");
    	sql.append(" (select b.emp_uuid  from bpm_replace_info b ");
    	sql.append(" where b.is_active = 'Y' and b.status = 'open'  ");
    	sql.append(" and b.begin_date < sysdate and trunc(b.end_date) >= trunc(sysdate) ");
    	sql.andEqual("b.replace_uuid", UserContextUtil.getEmpUuid());
    	sql.append(" ))");
		return sql.pageQuery(entityManager, request, SysRemindDto.class);
	}
	
	/**
	 * 我的申请
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<SysRemindDto> sqlPageQuery4My(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("k.id_", "currTaskId", ",");
		sql.appendField("k.assignee_", "assignee", ",");
		sql.appendTableColumns(SysRemind.class, "t");
		sql.append(" from SYS_REMIND t  ");
		sql.append(" left join act_ru_task k on k.proc_inst_id_ = t.flow_uuid  ");
		sql.append(" where t.IS_ACTIVE='Y' and t.in_system='ppobuild' and t.opration_type='startProcess' and t.user_id is null ");
		sql.andEqual("t.emp_uuid", UserContextUtil.getEmpUuid());		
		return sql.pageQuery(entityManager, request, SysRemindDto.class);
	}
	
	
	
	/**
	 * 得到所有催办数据
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<SysRemindDto> findUrgentRemindByAll() {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField(" nvl((select d.name from base_dict_detail d where d.is_active='Y' and d.dict_code='PS2_AUTH_TYPE' and d.code = r.ps2_auth_type and rownum < 2),(select d.name from base_dict_detail d where d.is_active='Y' and d.dict_code='SYS_PS2_REMIND_TYPE' and d.code = r.biz_type and rownum < 2)) ", "bizStatus", ",");
		sql.appendTableColumns(SysRemind.class, "r");
		sql.append(" from sys_remind r where r.is_active='Y' and r.user_id is not null and r.handle_status = 'N'  ");
		sql.append(" and r.biz_type in (select c.biz_type from sys_remind_config c where c.is_active='Y' and c.urgent='Y') ");
		List<SysRemindDto> result = sql.query(this.entityManager, SysRemindDto.class);
		return result;		
	}
}

