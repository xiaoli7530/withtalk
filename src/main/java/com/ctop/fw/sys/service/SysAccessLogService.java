package com.ctop.fw.sys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.fw.config.LogFilter.AccessLog;
import com.ctop.fw.sys.entity.SysAccessLog;
import com.ctop.fw.sys.repository.SysAccessLogRepository;

@Service
@Transactional
public class SysAccessLogService {
	private static final int TIMESPAN = 3000;

	@Autowired
	private SysAccessLogRepository sysAccessLogRepository; 
	
	private ConcurrentLinkedQueue<SysAccessLog> queue = new ConcurrentLinkedQueue<SysAccessLog>();
	
	/**
	 * 保存日志时，5秒钟内的日志汇总保存;
	 * @param accessLog
	 */
	public void saveAccessLog(AccessLog accessLog) {
		SysAccessLog log = new SysAccessLog(accessLog);
		queue.offer(log);
	} 
	
	/**
	 * 保存日志时，5秒钟内的日志汇总保存;
	 */
	@Scheduled(fixedRate = TIMESPAN)
	@Transactional
	public void saveAccessLogInQueue() { 
		List<SysAccessLog> list = new ArrayList<SysAccessLog>();
		list.addAll(queue); 
		this.sysAccessLogRepository.save(list);
		this.sysAccessLogRepository.flush();
		queue.removeAll(list);
	}
	
	@PreDestroy
	protected void clear() {
		this.saveAccessLogInQueue();
	}
}
