package com.ctop.core.webSocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.PongMessage;
import javax.websocket.Session;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ctop.fw.common.constants.Constants;
import com.ctop.fw.common.utils.DateUtil;
import com.ctop.fw.common.utils.JsonUtil;

@Component
public class WsEndpoint extends Endpoint {
	
	private static final ByteBuffer pongload = ByteBuffer.wrap("asdfghjkl".getBytes(Charset.defaultCharset()));
	String sessionId;
	private int msgCount=1;
    private static Timer timer;
    private static final int INTERVAL=60000;// 设定指定的定时器周期,此处为1分钟

	
	public WsEndpoint() {
	}
	
	@Override
	public void onOpen(Session session, EndpointConfig config) {
		System.out.println("websocket connection opened,SessionId: " + session.getId());
		MyPongHandler pongHandler = new MyPongHandler(session);
		session.addMessageHandler(new MyMessageHandler(session, pongHandler));
		session.addMessageHandler(pongHandler);

		String qs=session.getQueryString();
		String[] args = qs.split(";");
		try{
			if(args.length>0){
				System.out.println("sessionPoolSize:"+session.getOpenSessions().size());
				String me = args[1];
				String myId = args[0];
				WsMessage msg = new WsMessage();
				msg.setFromUser(me);
				msg.setFromId(args[0]);
				// 存session到缓存池
				WsSessionPool.addSession(session);

				// 推送离线消息
				//List<WsOfflineMsg> offMsgs = offlineMsgMng.findByUserId(myId);
				//sendStoredMsg(offMsgs,myId);
			}
			// 后台定时推送任务
			if(timer==null){
		        timer = new Timer(true);
		        OfflineMsgTimeTask timeTask = new OfflineMsgTimeTask();
		        timer.schedule(timeTask,INTERVAL, INTERVAL);//
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClose(Session s, CloseReason closeReason) {
		// 从缓存池移除session
		WsSessionPool.removeSession(s.getId(),closeReason.toString());
		super.onClose(s, closeReason);
	}
	
	@Override
	public void onError(Session session, Throwable thr) {
		System.out.println("websocket connection exception: "+ thr.getMessage() + session.getId() + " threw " + thr.getClass().getSimpleName() + DateUtil.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
	}
	
	/**
	 * @title: 发送离线消息给所有人
	 * @author: YangHongyuan
	 * @date: 2015年4月30日
	 * @param s
	 * @param userId
	 * @return
	 */
	private void sendAllStoredMsg(){
		// 发送离线消息
		/*Map<String,List<WsOfflineMsg>> msgMap = offlineMsgMng.findAll();
		for (String userId : msgMap.keySet()) {  
				this.sendStoredMsg(msgMap.get(userId), userId);
		} */
		/*test start
		String userId= "4028810a579d0e6e01579d137fdb0004";
		List<WsOfflineMsg> list = new ArrayList<WsOfflineMsg>();
		WsOfflineMsg wsMsg=new WsOfflineMsg();
		wsMsg.setToUuid(userId);
		wsMsg.setMsgType(Constants.WsMsgType.TASK);
		wsMsg.setContent("测试Websocket消息,No["+msgCount+"]");
		list.add(wsMsg);
		this.sendStoredMsg(list,userId);
		msgCount++;
		*///test end
	}
	/**
	 * @title: 上线时发送离线消息
	 * @author: YangHongyuan
	 * @date: 2015年4月30日
	 * @param s
	 * @param userId
	 * @return
	 */
	private int sendStoredMsg(List<?> userMsgs,String userId){
		// 发送离线消息
		Map<String,Session> sMap = WsSessionPool.getUserSessions(userId);
		boolean sendFlag = false;
		/*if((sMap!=null)&&(!sMap.isEmpty())){
			for(WsOfflineMsg msg:userMsgs){
				WsMessage wsMsg = new WsMessage();
				wsMsg.setFromId(msg.getFromUuid());
				wsMsg.setFromUser(msg.getFromUser());
				wsMsg.setToId(msg.getToUuid());
				wsMsg.setToUser(msg.getToUser());
				wsMsg.setContent(msg.getContent());
				wsMsg.setTimestamp(DateUtil.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
				wsMsg.setType(msg.getMsgType());
				wsMsg.setSubType(msg.getSubType());
				String msgStr = StringEscapeUtils.unescapeHtml(JsonUtil.toJsonStr(wsMsg));
				for (String key : sMap.keySet()) {  
					Session s = sMap.get(key);
					if(s!=null){
						try {
							s.getBasicRemote().sendText(msgStr);
							sendFlag = true;
						} catch (IOException e) {
							// 尝试再次发送
							try {
								s.getBasicRemote().sendText(msgStr);
								sendFlag = true;
							} catch (IOException e1) {
								// 从缓存池移除session
								WsSessionPool.removeSession(sessionId,"Session 失效");
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
		if(sendFlag){
			// 删除离线消息
			//offlineMsgMng.deleteByUserId(userId);
			return userMsgs.size();
		}else{
			return 0;
		}*/
		return 0;
	}
	
	class MyMessageHandler implements MessageHandler.Whole<String> {
		final Session session;
		final MyPongHandler pongHandler;
		
		public MyMessageHandler(Session session, MyPongHandler pongHandler) {
			this.session = session;
			this.pongHandler = pongHandler;
		}
		
		@Override
		public void onMessage(String message) {
			if(message.contains("ping@")){
				System.out.println("接收ping包:" + message +" "+ DateUtil.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
				return;
			}
			WsMessage msg = JsonUtil.toBean(message, WsMessage.class);
			// 消息类型
			String msgType = msg.getType();
			msg.setTimestamp(DateUtil.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss"));

			// 通知
			if(Constants.WsMsgType.NOTICE.equals(msgType) && ("system".equals(msg.getFromUser()))){
				for(Session s:session.getOpenSessions()){
					String qs=s.getQueryString();
					String[] args = qs.split(";");
					if(!StringUtils.isEmpty(args[1])){
						msg.setToUser(args[1]);
						s.getAsyncRemote().sendText(message);
						System.out.println("发送系统消息给：" + args[1]);
					}
				}
			// 聊天与业务消息
			}else {
				try {
					sendChatMsg(message, msg);
				} catch (IOException e) {
					//尝试再次发送
					try {
						sendChatMsg(message, msg);
					} catch (IOException e1) {
						// 从缓存池移除session
						WsSessionPool.removeSession(sessionId,"Session 失效");
						e1.printStackTrace();
					}
				}
			}
		}
		/**
		 * @title: 发送聊天消息
		 * @author: YangHongyuan
		 * @date: 2015年6月27日
		 * @param message
		 * @param msg
		 * @throws IOException
		 */
		private void sendChatMsg(String message,WsMessage msg) throws IOException{
			// 发送手机端消息
			int sended = 0;
			String toUserId = msg.getToId();

			Map<String,Session> s2s =WsSessionPool.getUserSessions(toUserId);
			// 给同一个用户的多端
			if(s2s!=null){
				for(Map.Entry<String, Session> entry:s2s.entrySet()){ 
					message = StringEscapeUtils.unescapeHtml(message);
					Session s = entry.getValue();
					s.getBasicRemote().sendText(message);
					sessionId = s.getId();
					sended ++ ;
					System.out.println("发送消息给：[" + msg.getToUser() +"]:" +msg.getContent());
				}
			}
			
			// 储存离线消息
			if(sended == 0){
			}
		}
	}
	
	class MyPongHandler implements MessageHandler.Whole<PongMessage> {
		final Session session;
		boolean isResponding = true;
		
		public MyPongHandler(Session session) {
			this.session = session;
		}
		
		@Override
		public void onMessage(PongMessage message) {
			try {
				if(isResponding){
					session.getAsyncRemote().sendPong(pongload);
				}
			} catch (IllegalArgumentException | IOException e) {
				e.printStackTrace();
			}
		}

		public boolean isResponding() {
			return isResponding;
		}

		public void setResponding(boolean isResponding) {
			this.isResponding = isResponding;
		}
	}
    class OfflineMsgTimeTask extends TimerTask{
        @Override
        public void run() {
            try {
            	sendAllStoredMsg();
            } catch (Exception e){
                e.printStackTrace();
            }
      }
    }
}
