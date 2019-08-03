package com.ctop.base.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.SysScheduleConfigDto;
import com.ctop.base.entity.SysScheduleConfig;
import com.ctop.base.repository.SysScheduleConfigRepository;
import com.ctop.fw.common.constants.Constants.ScheduleConfigBizType;
import com.ctop.fw.common.constants.Constants.ScheduleConfigStatus;
import com.ctop.fw.common.model.PageRequestData;
import com.ctop.fw.common.model.PageResponseData;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.CommonAssembler;
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
public class SysScheduleConfigService {
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SysScheduleConfigRepository sysScheduleConfigRepository; 
	@Autowired
	private TaskSchedulerService taskSchedulerService;
	/*@Value("${schedule.userDir}")
	private String userDir;*///控制定时任务运行的目录
	
	@Transactional(readOnly=true)
	public SysScheduleConfigDto getById(String id) {
		SysScheduleConfig sysScheduleConfig = this.sysScheduleConfigRepository.findOne(id);
		return CommonAssembler.assemble(sysScheduleConfig, SysScheduleConfigDto.class);
	}
	
	@Transactional
	public SysScheduleConfigDto addSysScheduleConfig(SysScheduleConfigDto sysScheduleConfigDto) {
		validateFrom(sysScheduleConfigDto);
		SysScheduleConfig sysScheduleConfig = CommonAssembler.assemble(sysScheduleConfigDto, SysScheduleConfig.class);
		sysScheduleConfig.setStatus(ScheduleConfigStatus.PAUSE);
		sysScheduleConfig.setServerName(sysScheduleConfig.getServerName().toLowerCase());
		sysScheduleConfig = this.sysScheduleConfigRepository.save(sysScheduleConfig);
		return CommonAssembler.assemble(sysScheduleConfig, SysScheduleConfigDto.class);
	}
	/**
	 * 验证是否
	 * @param sysScheduleConfigDto
	 */
	@SuppressWarnings("rawtypes")
	public void validateFrom(SysScheduleConfigDto sysScheduleConfigDto){
		StringBuffer errorInfo = new StringBuffer();
		String cron = sysScheduleConfigDto.getCron();
		String scheduleMethod = sysScheduleConfigDto.getScheduleMethod();
		String parameters = sysScheduleConfigDto.getParameters();
		if(!CronSequenceGenerator.isValidExpression(cron)){
			errorInfo.append("无效的cron表达式,");
		}
		if(ScheduleConfigBizType.TASK_CLASS.equals(sysScheduleConfigDto.getBizType())){
			if(scheduleMethod.contains(".")){
				String methodName = scheduleMethod.substring(scheduleMethod.lastIndexOf(".") + 1);
				String classFullName = scheduleMethod.substring(0, scheduleMethod.lastIndexOf("."));
				try {
					Class<?> clazz = Class.forName(classFullName);
					if (StringUtil.isNotEmpty(parameters) && parameters.indexOf(":") != -1) {
						List<String> paras = Arrays.asList(parameters.split(";"));
						List<Class> paraToUsers = new ArrayList<Class>(paras.size());
						paras.forEach(str -> {
							try {
								paraToUsers.add(Class.forName(str.substring(0,str.indexOf(":"))));
							} catch (Exception e) {
								errorInfo.append("无效的参数");
							}
						});
						Class[] classes = new Class[paraToUsers.size()];
						clazz.getMethod(methodName,paraToUsers.toArray(classes));
					} else {
						clazz.getMethod(methodName);
					}
					
				} catch (Exception e) {
					errorInfo.append("无效的调度方法,");
				}
			}else{
				errorInfo.append("无效的调度方法");
			}
		}
		if(errorInfo.length() > 0){
			throw new BusinessException(errorInfo.toString());
		}
	}

	@Transactional
	public SysScheduleConfigDto updateSysScheduleConfig(SysScheduleConfigDto sysScheduleConfigDto,Set<String> updatedProperties) {
		validateFrom(sysScheduleConfigDto);
		SysScheduleConfig sysScheduleConfig = this.sysScheduleConfigRepository.findOne(sysScheduleConfigDto.getScheduleId());
		CommonAssembler.assemble(sysScheduleConfigDto, sysScheduleConfig,updatedProperties, CommonAssembler.DEFAULT_IGNORE_PROPS);
		sysScheduleConfig = this.sysScheduleConfigRepository.save(sysScheduleConfig);
		return CommonAssembler.assemble(sysScheduleConfig, SysScheduleConfigDto.class);
	}
	
	@Transactional
	public void deleteSysScheduleConfig(String id) {
		SysScheduleConfig sysScheduleConfig = this.sysScheduleConfigRepository.findOne(id);
		sysScheduleConfig.setIsActive("N");
		this.sysScheduleConfigRepository.save(sysScheduleConfig);
	}
		
	@Transactional
	public void deleteSysScheduleConfigs(List<String> scheduleIds) {
		Iterable<SysScheduleConfig> sysScheduleConfigs = this.sysScheduleConfigRepository.findAll(scheduleIds);
		Iterator<SysScheduleConfig> it = sysScheduleConfigs.iterator();
		while(it.hasNext()) {
			SysScheduleConfig sysScheduleConfig = it.next();
			sysScheduleConfig.setIsActive("N");
			this.sysScheduleConfigRepository.save(sysScheduleConfig);
		}
	} 

 
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	 /*
	@Transactional(readOnly=true)
	public PageResponseData<SysScheduleConfig> pageQuery(PageRequestData request) {
		Specification<SysScheduleConfig> spec = request.toSpecification(SysScheduleConfig.class);
		Page<SysScheduleConfig> page = sysScheduleConfigRepository.findAll(spec, request.buildPageRequest());
		return new PageResponseData<SysScheduleConfig>(page);
	}*/
	
	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageResponseData<SysScheduleConfigDto> sqlPageQuery(PageRequestData request) {
		SqlBuilder sql = SqlBuilderFactory.sqlBuilder();
		sql.append("select ");
		sql.appendTableColumns(SysScheduleConfig.class, "t");
		sql.append(" from SYS_SCHEDULE_CONFIG t where t.IS_ACTIVE='Y' ");
	//	sql.andEqual("t.user_dir", userDir);
		return sql.pageQuery(entityManager, request, SysScheduleConfigDto.class);
	}

	/**
	 * @param scheduleId
	 * @return
	 */
	public SysScheduleConfigDto pause(String scheduleId) {
		return taskSchedulerService.uninstall(scheduleId);
	}

	/**
	 * @param scheduleId
	 * @return
	 * @throws ClassNotFoundException 
	 */
	public SysScheduleConfigDto reply(String scheduleId) throws ClassNotFoundException {
		return taskSchedulerService.toExecuteQueue(scheduleId);
	}

	/**
	 * @param scheduleId
	 * @return
	 */
	public SysScheduleConfigDto exeTask(String scheduleId) {
		return taskSchedulerService.exeTask(scheduleId);
	}
	
	public void test(){
		System.out.println("test..................");
	}
}

