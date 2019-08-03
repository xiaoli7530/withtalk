package com.ctop.fw.sys.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ctop.base.entity.BasePageTemplate;
import com.ctop.base.repository.BasePageTemplateRepository;
import com.ctop.fw.common.constants.Constants.SysEmailStatus;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.AppContextUtil;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.FileUtil;
import com.ctop.fw.common.utils.FreemarkerUtil;
import com.ctop.fw.common.utils.ListUtil;
import com.ctop.fw.common.utils.SqlBuilder;
import com.ctop.fw.common.utils.SqlBuilderFactory;
import com.ctop.fw.common.utils.StringUtil;
import com.ctop.fw.sys.dto.SysEmailDto;
import com.ctop.fw.sys.dto.SysEmailStatusNumDto;
import com.ctop.fw.sys.dto.SysRemindUser;
import com.ctop.fw.sys.entity.SysEmail;
import com.ctop.fw.sys.entity.SysEmailInfo;
import com.ctop.fw.sys.repository.SysEmailInfoRepository;
import com.ctop.fw.sys.repository.SysEmailRepository;


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
@Transactional(rollbackFor=Exception.class)
public class SysEmailService {
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysEmailRepository sysEmailRepository; 
	@Autowired
	private SysEmailInfoRepository sysEmailInfoRepository; 
	@Autowired
	private BasePageTemplateRepository basePageTemplateRepository;
	
	
	
	@Transactional(readOnly=true)
	public SysEmailDto getById(String id) {
		SysEmail sysEmail = this.sysEmailRepository.findOne(id);
		return CommonAssembler.assemble(sysEmail, SysEmailDto.class);
	}
	
	/**
	 * 发送邮件  ppobuild
	 * @author dalinpeng
	 * @param title
	 * @param content
	 * @param receiveUsers
	 * @return
	 */
	@Transactional
	public SysEmailDto addSysEmail4Ppobuild(String title,String content,List<SysRemindUser> receiveUsers){
		SysEmail email = new SysEmail();
		if(StringUtil.isNotEmpty(title) && StringUtil.isNotEmpty(content) && (!ListUtil.isNullOrEmpty(receiveUsers))){
			email.setTitle(title);
			email.setContent(content);
			email.setStatus(SysEmailStatus.STATUS_AWAIT);
			email.setSendType(SysEmailStatus.SEND_TYPE_COMMON);
			email = this.sysEmailRepository.save(email);
			List<SysEmailInfo> infos = new ArrayList<SysEmailInfo>();
			SysEmailInfo info = null;
			for(SysRemindUser user : receiveUsers){
				if(StringUtil.isNotEmpty(user.getEmail())){
					info = new SysEmailInfo();
					info.setEmailUuid(email.getEmailUuid());
					info.setReceiverEmail(user.getEmail());
					info.setReceiverName(user.getEmpName());
					info.setStatus(SysEmailStatus.STATUS_AWAIT);
					infos.add(info);
				}				
			}
			if(!ListUtil.isNullOrEmpty(infos)){
				this.sysEmailInfoRepository.save(infos);
			}
		}
		return CommonAssembler.assemble(email,SysEmailDto.class);
	}
	
	
	@Transactional
	public SysEmailDto updateSysEmail(SysEmailDto sysEmailDto) {
		SysEmail sysEmail = this.sysEmailRepository.findOne(sysEmailDto.getEmailUuid());
		CommonAssembler.assemble(sysEmailDto, sysEmail);
		sysEmail = this.sysEmailRepository.save(sysEmail);
		return CommonAssembler.assemble(sysEmail, SysEmailDto.class);
	}
	
	@Transactional
	public void deleteSysEmail(String id) {
		SysEmail sysEmail = this.sysEmailRepository.findOne(id);
		sysEmail.setIsActive("N");
		this.sysEmailRepository.save(sysEmail);
	}
		
	@Transactional
	public void deleteSysEmails(List<String> emailUuids) {
		Iterable<SysEmail> sysEmails = this.sysEmailRepository.findAll(emailUuids);
		Iterator<SysEmail> it = sysEmails.iterator();
		while(it.hasNext()) {
			SysEmail sysEmail = it.next();
			sysEmail.setIsActive("N");
			this.sysEmailRepository.save(sysEmail);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	@Transactional(readOnly=true)
	public PageResponseData<SysEmail> pageQuery(PageRequestData request) {
		Specification<SysEmail> spec = request.toSpecification(SysEmail.class);
		Page<SysEmail> page = sysEmailRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<SysEmail>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<SysEmailDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysEmail.class, "t");
		sql.append(" from SYS_EMAIL t where t.IS_ACTIVE='Y' ");
		return sql.pageQuery(entityManager, request, SysEmailDto.class);
	}
	
	@Transactional(readOnly=true)
	public SysEmailDto findFirstSendedMailData(){
		SysEmailDto sysEmailDto = null;
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysEmail.class, "d");
		sql.append(" from (select rownum as topNum,t.* from (select rownum as rownums,e.* from sys_email e ");
		sql.append(" where e.is_active = 'Y' and (e.status = '0' or e.status = '1') order by e.send_type desc,e.created_date asc ");
		sql.append(" ) t ) d where d.topNum = 1 ");
		List<SysEmailDto> waitingLs = sql.query(entityManager, SysEmailDto.class);
		if (waitingLs != null && waitingLs.size() > 0)
			sysEmailDto = waitingLs.get(0);
		return sysEmailDto;
	}
	
	@Transactional(readOnly=true)
	public List<SysEmailStatusNumDto> countNumByStatus() {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendField("t.STATUS", "status", ",");
		sql.appendField("count(1)", "num");
		sql.append(" from SYS_EMAIL t where t.IS_ACTIVE='Y' group by t.status order by t.STATUS desc ");
		return sql.query(entityManager, SysEmailStatusNumDto.class);
	}

	
	//根据ext5(otUuid)获取发送记录，定时器发送邮件专用
		@Transactional(readOnly=true)
		public SysEmailDto getByOtId(String otid) {
			List<SysEmailDto> list ;
			SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
			sql.append("select ");
			sql.appendTableColumns(SysEmail.class, "se");
			sql.append(" from sys_email se where  se.is_active='Y' ");
			sql.append(" and se.title like '%待审核%' ");
			sql.andEqual(" se.ext5", otid);
			list =  sql.query(entityManager, SysEmailDto.class);
			if(list.isEmpty()){
				return null;
			}else{
				return list.get(0);
			}
		}
	
		/** 得到页面
		 * @param templateName
		 * @return
		 */
		public String getPageTemplate(String templateName, Object templateData) {
			String result = "";
			// 从数据库得到模板
			List<BasePageTemplate> basePageTemplateList = basePageTemplateRepository.findByTemplateName(templateName);
			if(ListUtil.isNotNullOrEmpty(basePageTemplateList)) {
				BasePageTemplate basePageTemplate = basePageTemplateList.get(0);
				
				//String classPath = SysEmailService.class.getClassLoader().getResource("freeMarker").getPath();
				String classPath = "classpath:freeMarker/";
				try {
					URL url=AppContextUtil.getContext().getResource(classPath).getURL();
					classPath=url.getPath();
					
					// 把模板保存为文件
					FileUtil.writeTextFile(classPath, basePageTemplate.getTemplateName()+".tpl", basePageTemplate.getTemplateContent());
					
					// 根据模板和数据生成页面
					result = FreemarkerUtil.getInstance(classPath).process(templateData, basePageTemplate.getTemplateName()+".tpl");
					
				} catch (Exception e) {
					e.printStackTrace();
					throw new BusinessException(e.getMessage());
				}
				
			}
			//System.out.println(result);
			return result;
		}	
	
}