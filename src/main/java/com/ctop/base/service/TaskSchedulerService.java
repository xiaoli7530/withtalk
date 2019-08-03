/**
 * 
 */
package com.ctop.base.service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import com.ctop.base.dto.SysScheduleConfigDto;
import com.ctop.base.entity.SysScheduleConfig;
import com.ctop.base.repository.SysScheduleConfigRepository;
import com.ctop.fw.common.constants.Constants.ScheduleConfigBizType;
import com.ctop.fw.common.constants.Constants.ScheduleConfigLastRunStatus;
import com.ctop.fw.common.constants.Constants.ScheduleConfigStatus;
import com.ctop.fw.common.utils.AppContextUtil;
import com.ctop.fw.common.utils.BusinessException;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.common.utils.ListUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author Administrator
 *
 */
@Service("taskSchedulerService")
@Lazy(false)
@Transactional
public class TaskSchedulerService implements ApplicationListener<ContextRefreshedEvent>{
	/**日志*/
	private static Logger logger = LoggerFactory.getLogger(TaskSchedulerService.class); 
	@Autowired
	@Qualifier("taskExecutor")
	private TaskScheduler taskScheduler;
	@Autowired
	private SysScheduleConfigRepository  scheduleConfigRepository;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	/*@Value("${schedule.userDir}")
	private String userDir;*///控制定时任务运行的目录
	
	/**活跃的定时配置*/
	private Map<String, ProxyRunner> configIdMapCache = new ConcurrentHashMap<>(); 
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
	/*	try {
			this.initTaskScheduler();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}*/
	}
	
	
	/**
	 * 初始化调度任务
	 * @throws Exception 
	 */
	//@PostConstruct
	/*private void initTaskScheduler() throws Exception{
		String currentComputerName = this.getCurrentMachineName();
		String currentComputerAddr = this.getCurrentMachineAddress();
		logger.info("正在初始化任务调度器...");
		logger.info("获取当前计算机名称为:{},IP:{}",currentComputerName,currentComputerAddr);
		if(!CollectionUtils.isEmpty(configIdMapCache)){
			logger.info("正在停止正在运行的调度任务....");
			for(Map.Entry<String, ProxyRunner> entry : configIdMapCache.entrySet()){
				ProxyRunner proxyRunner = entry.getValue();
				logger.info("正在停止:{}" , proxyRunner.scheduleConfig.getScheduleName());
				proxyRunner.scheduledFuture.cancel(true);
			}
			configIdMapCache.clear();
			logger.info("停止调度任务结束....");
		}

		List<SysScheduleConfig> configs = scheduleConfigRepository.loadActivateScheduleConfig(
				currentComputerName,currentComputerAddr
				,Arrays.asList(ScheduleConfigStatus.ACTIVE,ScheduleConfigStatus.WAIT_LOAD,ScheduleConfigStatus.WAIT_UNlOAD), userDir);
		if(!ListUtil.isEmpty(configs)){
			for(SysScheduleConfig config : configs){
				if(ScheduleConfigStatus.WAIT_UNlOAD.equals(config.getStatus())){//修改卸责
					config.setStatus(ScheduleConfigStatus.PAUSE);
					scheduleConfigRepository.save(config);
				}else{
					toScheduleQueue(config);
				}
			}
		}
	}*/
	/**
	 * 放入调度队列，
	 * @param config
	 * @throws ClassNotFoundException
	 */
	private void toScheduleQueue(SysScheduleConfig config) {
		ProxyRunner cacheProxyRunner = configIdMapCache.get(config.getScheduleId());
		if(cacheProxyRunner != null){
			if(cacheProxyRunner.scheduledFuture.isCancelled() || cacheProxyRunner.scheduledFuture.isDone()){
				cacheProxyRunner.scheduledFuture.cancel(true);
			}else{//正在运行
				return ;
			}
			
		}
		
		ProxyRunner proxyRunner = new ProxyRunner(config);
		configIdMapCache.put(config.getScheduleId(),proxyRunner);
		//进入调度队列
		ScheduledFuture<?> future = taskScheduler.schedule(proxyRunner, proxyRunner.cronTrigger);
		proxyRunner.setScheduledFuture(future);
	}
	/**
	 * 卸载调度任务
	 * @param scheduleId
	 */
	@Transactional
	public SysScheduleConfigDto uninstall(String scheduleId){
		ProxyRunner proxyRunner = configIdMapCache.get(scheduleId);
		if(proxyRunner != null){//存在当前机器
			SysScheduleConfig newest = scheduleConfigRepository.findOne(scheduleId);
			newest.setStatus(ScheduleConfigStatus.PAUSE);
			scheduleConfigRepository.saveAndFlush(newest);
			proxyRunner.scheduledFuture.cancel(true);
			configIdMapCache.remove(scheduleId);
			return CommonAssembler.assemble(proxyRunner.scheduleConfig, SysScheduleConfigDto.class);
		}else{ //非当前机器或不存在
			SysScheduleConfig config = scheduleConfigRepository.findOne(scheduleId);
			if(config != null){
				//如果当前机器或是待载入状态直接暂停
				if(this.isCurrentMachineRun(config) || ScheduleConfigStatus.WAIT_LOAD.equals(config.getStatus())){
					config.setStatus(ScheduleConfigStatus.PAUSE);
				}else{
					config.setStatus(ScheduleConfigStatus.WAIT_UNlOAD);
				}
				scheduleConfigRepository.saveAndFlush(config);
			}
		}
		return new SysScheduleConfigDto();
	}
	/**
	 * 当前调度守护<负责维护调度任务> 只能重新加载修改，不能暂停
	 * @throws UnknownHostException 
	 */
	//@Scheduled(fixedRate=5 * 1000l,initialDelay=1000)
/*	@Transactional
	public void taskDaemonScheduler() throws UnknownHostException {
		logger.info("任务守护进程正在运行...");
		List<SysScheduleConfig> configs = scheduleConfigRepository.loadActivateScheduleConfig(
				getCurrentMachineName(),getCurrentMachineAddress(),
				Arrays.asList(ScheduleConfigStatus.WAIT_LOAD,ScheduleConfigStatus.WAIT_UNlOAD), userDir);
		if (!ListUtil.isEmpty(configs)) {
			for (SysScheduleConfig config : configs) {
				if(StringUtils.equals(ScheduleConfigStatus.WAIT_LOAD, config.getStatus())){//重新载入调度
					config.setStatus(ScheduleConfigStatus.ACTIVE);
					toScheduleQueue(config);
				}else if(StringUtils.equals(ScheduleConfigStatus.WAIT_UNlOAD, config.getStatus())){ //卸载调度
					config.setStatus(ScheduleConfigStatus.PAUSE);
					uninstall(config.getScheduleId());
				}
				scheduleConfigRepository.saveAndFlush(config);
			
			}
		}
	}*/
	/**
	 * 放入执行队列
	 * @param scheduleId
	 * @throws ClassNotFoundException 
	 * @throws UnknownHostException 
	 */
	public SysScheduleConfigDto toExecuteQueue(String scheduleId) {
		SysScheduleConfigDto sscDto = new SysScheduleConfigDto();
		sscDto.setStartStatus("no");
		if (StringUtils.isBlank(scheduleId)) {
			return sscDto;
		}
		ProxyRunner proxyRunner = configIdMapCache.get(scheduleId);
		if (proxyRunner != null) { //异常情况
			proxyRunner.scheduleConfig.setStatus(ScheduleConfigStatus.ACTIVE);
			scheduleConfigRepository.saveAndFlush(proxyRunner.scheduleConfig);
			sscDto.setStartStatus("ok");
		}else{ //未在执行队列
			SysScheduleConfig scheduleConfig = scheduleConfigRepository.findOne(scheduleId);
			if(scheduleConfig != null){
				if(isCurrentMachineRun(scheduleConfig)){//当前机器
					toScheduleQueue(scheduleConfig);
					sscDto = CommonAssembler.assemble(scheduleConfig, SysScheduleConfigDto.class);
					sscDto.setStartStatus("ok");
				}
				scheduleConfig.setStatus(ScheduleConfigStatus.WAIT_LOAD);
				scheduleConfigRepository.saveAndFlush(scheduleConfig);
			}
		}
		return sscDto;
	}
	/**
	 * 是否是当前机器
	 * @param scheduleConfig
	 * @return
	 * @throws UnknownHostException 
	 */
	private boolean isCurrentMachineRun(SysScheduleConfig scheduleConfig){
		boolean isPass = (StringUtils.equals(getCurrentMachineName(), scheduleConfig.getServerName().toLowerCase()) 
				|| StringUtils.equals(getCurrentMachineAddress(), scheduleConfig.getServerName())) ;
		/*
		 * if(StringUtils.isNotBlank(scheduleConfig.getUserDir())){ String workDir =
		 * this.getWorkDir(); isPass = isPass &&
		 * StringUtils.equals(scheduleConfig.getUserDir(),workDir); }
		 */
		
		return isPass;
	}
	/**
	 * 重新加载调度任务<可能出现延迟>
	 * @param scheduleId
	 * @throws ClassNotFoundException 
	 * @throws UnknownHostException 
	 */
	public void reloadScheduleConfig(String scheduleId) throws ClassNotFoundException, UnknownHostException{
		if (StringUtils.isBlank(scheduleId)) {
			return;
		}
		SysScheduleConfig scheduleConfig = scheduleConfigRepository.findOne(scheduleId);
		if(scheduleConfig != null){
			if(isCurrentMachineRun(scheduleConfig)){
				toScheduleQueue(scheduleConfig);
			}
		}
	}
	/**
	 * 执行任务
	 * @param scheduleConfig
	 * @param targetBeanInstance
	 * @param targetMethod
	 * @param proxyRunner 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void executeTask(SysScheduleConfig scheduleConfig
			, ProxyRunner proxyRunner,boolean isUpdateStatus) {
		long currentTimeMillis = System.currentTimeMillis();
		String exeMsg = "执行成功!";
		String lastRunStatus = ScheduleConfigLastRunStatus.SUCCESS;
		try {
			if(ScheduleConfigBizType.TASK_CLASS.equals(scheduleConfig.getBizType())){           //执行java类
				if (scheduleConfig.getParameters().indexOf(":") > 0) {
					List<String> paras = Arrays.asList(scheduleConfig.getParameters().split(";"));
					List paraToUsers = new ArrayList(paras.size());
					paras.forEach(str -> {
						try {
							String clName = str.substring(0,str.indexOf(":"));
							if (clName.substring(clName.lastIndexOf(".") + 1).equals("Integer")) {
								paraToUsers.add(Integer.valueOf(str.substring(str.indexOf(":") + 1)));
							} else {
								paraToUsers.add(str.substring(str.indexOf(":") + 1));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
					Object[] args = new Object[paraToUsers.size()];
					Method method = proxyRunner.targetMethod;
					method.invoke(proxyRunner.targetBeanInstance, paraToUsers.toArray(args));
				} else {
					proxyRunner.targetMethod.invoke(proxyRunner.targetBeanInstance);
				}
			}else if(ScheduleConfigBizType.TASK_SQL.equals(scheduleConfig.getBizType())){      //执行sql
				jdbcTemplate.execute(proxyRunner.scheduleMethod);
			}else if(ScheduleConfigBizType.TASK_PROCEDURE.equals(scheduleConfig.getBizType())){//存储过程
				jdbcTemplate.execute(proxyRunner.scheduleMethod);
			}else if(ScheduleConfigBizType.TASK_REST.equals(scheduleConfig.getBizType())){//存储过程
				//TODO
				this.restInvoke(scheduleConfig);
			}else{
				throw new BusinessException("无效的执行类型!");
			}
		} catch (Exception e) {
			logger.error("执行调度任务失败:",e);
			if(e.getCause() != null){
				exeMsg = e.getCause().getMessage();
			}else{
				exeMsg = e.getMessage();
			}
			lastRunStatus = ScheduleConfigLastRunStatus.FAIL;
			throw new RuntimeException(e);
		}finally{
			Long lastRunSeconds = (System.currentTimeMillis() - currentTimeMillis) / 1000L;
			scheduleConfig.setLastRunSeconds(lastRunSeconds.intValue());
			scheduleConfig.setLastRunMsg(exeMsg.length() > 4000 ? exeMsg.substring(0, 1000) : exeMsg);
			scheduleConfig.setLastRunStatus(lastRunStatus);
			scheduleConfig.setLastRunTime(new Date());
			if(isUpdateStatus){
				scheduleConfig.setStatus(ScheduleConfigStatus.ACTIVE);
			}
			//更新版本
			scheduleConfig = scheduleConfigRepository.saveAndFlush(scheduleConfig);
			if(proxyRunner != null){
				proxyRunner.scheduleConfig = scheduleConfig;
			}
		}
	}
	public void restInvoke(SysScheduleConfig sysScheduleConfig){
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> postForEntity = template.postForEntity(sysScheduleConfig.getScheduleMethod(), sysScheduleConfig.getParameters(), String.class);
		if(postForEntity.getStatusCodeValue() != 200){
			throw new BusinessException("非200状态,code:" + postForEntity.getStatusCode()); //+ " , msg:" +  postForEntity.getBody());
		}
	}
	public void test(){
		logger.error("测试方法111111111111111111111111111111111111111111111111" + this);
	}
	public void test2(){
		logger.error("我是测试方法2222222222222222222222222222222222222222222222" + this);
		//throw new RuntimeException("haha");
	}
	private class ProxyRunner implements Runnable{
		SysScheduleConfig scheduleConfig; //持有当前对象
		CronTrigger cronTrigger;
		ScheduledFuture<?> scheduledFuture;
		Method targetMethod;
		Object targetBeanInstance;
		String scheduleMethod;
		
		public ProxyRunner(SysScheduleConfig scheduleConfig){
			try {
				this.scheduleConfig = scheduleConfig;
				this.cronTrigger = new CronTrigger(scheduleConfig.getCron());
				if (ScheduleConfigBizType.TASK_CLASS.equals(scheduleConfig.getBizType())) {
					String methodFullName = scheduleConfig.getScheduleMethod();
					String methodName = methodFullName.substring(methodFullName.lastIndexOf(".") + 1);
					String classFullName = methodFullName.substring(0, methodFullName.lastIndexOf("."));

					targetBeanInstance = AppContextUtil.getBean(Class.forName(classFullName));

					
					
					if (scheduleConfig.getParameters().indexOf(":") > 0) {
						String parameters = scheduleConfig.getParameters();
						List<String> paras = Arrays.asList(parameters.split(";"));
						List<Class> paraToUsers = new ArrayList<Class>(paras.size());
						paras.forEach(str -> {
							try {
								paraToUsers.add(Class.forName(str.substring(0,str.indexOf(":"))));
							} catch (Exception e) {
								e.printStackTrace();
							}
						});
						Class[] classes = new Class[paraToUsers.size()];
						targetMethod = ReflectionUtils.findMethod(targetBeanInstance.getClass(), methodName,paraToUsers.toArray(classes));
					} else {
						targetMethod = ReflectionUtils.findMethod(targetBeanInstance.getClass(), methodName);
					}
				} else if (ScheduleConfigBizType.TASK_SQL.equals(scheduleConfig.getBizType())) {
					scheduleMethod = scheduleConfig.getScheduleMethod();
				} else if (ScheduleConfigBizType.TASK_PROCEDURE.equals(scheduleConfig.getBizType())) {
					scheduleMethod = scheduleConfig.getScheduleMethod();
				} 
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
			this.scheduledFuture = scheduledFuture;
		}
		
		@Override
		public void run() {
			logger.info("开始执行任务:" + scheduleConfig.getScheduleName());
			
			SysScheduleConfig dbConfig = scheduleConfigRepository.findOne(scheduleConfig.getScheduleId());
			boolean isRun = StringUtils.equals(dbConfig.getStatus(), ScheduleConfigStatus.ACTIVE)
					|| StringUtils.equals(dbConfig.getStatus(), ScheduleConfigStatus.WAIT_LOAD);
			if(!isRun){
				logger.error("无法执行非正常状态的调度任务! 开始卸载调度任务,调度名称:{}",dbConfig.getScheduleName());
				//卸载调度任务 
				uninstall(scheduleConfig.getScheduleId());
				return ;
			}
			/*if(!MathUtil.isEqualsNumber(dbConfig.getVersion(),scheduleConfig.getVersion())){
				throw new BusinessException("调度配置版本与当前持有版本不相同!");
			}*/
			TaskSchedulerService.this.executeTask(dbConfig,this,true);
		}
		
		@Override
		protected void finalize() throws Throwable {
			logger.warn("正在销毁当前任务调度代理对象,{} , 对象{}",scheduleConfig.getScheduleName(),this);
			super.finalize();
		}
	}
	
	public String getCurrentMachineName(){
		try{
			return InetAddress.getLocalHost().getHostName().toLowerCase();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public String getCurrentMachineAddress() {
		try{
			return InetAddress.getLocalHost().getHostAddress();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public String getWorkDir(){
		String curDir = System.getProperty("user.dir");
		return curDir;
	}
	/**
	 * @param scheduleId
	 * @return
	 */
	public SysScheduleConfigDto exeTask(String scheduleId) {
		SysScheduleConfigDto sscDto = new SysScheduleConfigDto();
		SysScheduleConfig cfg = scheduleConfigRepository.findOne(scheduleId);
		ProxyRunner proxyRunner = new ProxyRunner(cfg);
		this.executeTask(cfg, proxyRunner,false);
		sscDto.setStatus(cfg.getLastRunStatus());
		sscDto.setMemo(cfg.getLastRunMsg());
		return sscDto;
	}
	public static void main(String[] args) throws Exception {
		ObjectMapper om = new ObjectMapper();
		Map readValue = om.readValue("{}",Map.class);
	}
}
