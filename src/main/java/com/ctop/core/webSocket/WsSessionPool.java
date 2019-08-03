package com.ctop.core.webSocket;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import com.ctop.fw.common.utils.DateUtil;


@Scope("global session")
public class WsSessionPool {
	//websocket链接集合
	private static final Map<String,Session > connections = new HashMap< String,Session>();
	//用户session集合，用于多点登录时也可以接受消息
	private static final Map<String,Map<String,Session>> userSessions = new HashMap<String,Map<String,Session>>();
	
	/**
	 * @title: 获取session
	 * @author: YangHongyuan
	 * @date: 2015年4月29日
	 * @param key
	 */
	public static Session getSession(String key){
		return connections.get(key);
	}
	
	//向连接池中添加连接
	public static void addSession(Session s){
		String key = s.getId();
		String qs=s.getQueryString();
		String[] args = qs.split(";");
		String userId=args[0];
		if(!StringUtils.isEmpty(key)){
			connections.put(key, s);
			// 获取用户的sessionId集
			Map<String,Session> userMap=userSessions.get(userId);
			if(userMap==null){
				userMap = new HashMap<String,Session>();
			}
			userMap.put(key, s);
			userSessions.put(userId, userMap);
			if("Web".equals(args[2])){
				System.out.println(args[1] + "从web端上线;SessionID:[" +key + "]，userId:["+userId+"],时间：" +DateUtil.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}else{
				System.out.println(args[1]+ "从app端上线;SessionID:[" +key + "]，userId:["+userId+"],时间：" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
		}
	}
	
	/**
	 * @title: 移除session
	 * @author: YangHongyuan
	 * @date: 2015年4月29日
	 * @param s
	 */
	public static boolean removeSession(String key,String reason){
		boolean rtn = false;
		Session s = connections.get(key);
		if(s!=null){
			rtn=true;
			String qs=s.getQueryString();
			String[] args = qs.split(";");
			String userId=args[0];
			
			// 获取用户的sessionId集
			Map<String,Session> userMap=userSessions.get(userId);
			if(userMap!=null){
				userMap.remove(key);
			}
			if("Web".equals(args[2])){
				System.out.println(reason + args[1] + "从web端下线;SessionID:[" +key + "]，userId:["+userId+"],时间：" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}else{
				System.out.println(reason + args[1] + "从App端下线;SessionID:[" +key + "]，userId:["+userId+"],时间：" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
			s.getOpenSessions().remove(s);
			connections.remove(key);
		}

		return rtn;
	}
	
	/**
	 * @title: 获取session
	 * @author: YangHongyuan
	 * @date: 2015年4月29日
	 * @param key
	 */
	public static Map<String,Session> getUserSessions(String userId){
		return userSessions.get(userId);
	}
}
