package com.example.websocketdemo.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@ServerEndpoint("/api/pushMessage/{userId}")
public class WebsocketServer {
    //记录在线人数
    private static int onlineCount = 0;
    //存储连接信息
    private static ConcurrentHashMap<String,WebsocketServer> serverMap=new ConcurrentHashMap<>();

    private Session session;
    private String userId="";

    @OnOpen
    public void  onOpen(Session session,@PathParam("userId")String userId){
       this.session=session;
       this.userId=userId;
       if(!serverMap.containsKey(userId)){
           serverMap.put(userId, this);
           onlineCount+=1;
           log.info("{} 登录成功，当前在线人数{}",userId,onlineCount);
       }else{
           log.info("{} 已登录，当前在线人数{}",userId,onlineCount);
       }
    }
    @OnClose
    public void onClose() {
        if(serverMap.containsKey(userId)){
            serverMap.remove(userId);
            //从set中删除
            onlineCount-=1;
        }
        log.info("用户退出:"+userId+",当前在线人数为:" + onlineCount);
    }

    @OnMessage
    public void onMessage(String message,Session session) throws IOException, InterruptedException {
        log.warn("{} 的消息：{}",userId,message);
        for(Integer i=20;i>0;i--){
            Thread.sleep(1000);
            session.getBasicRemote().sendText("====还有"+i+"秒自爆===");
        }
        session.getBasicRemote().sendText("BOom");

    }
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }

}
