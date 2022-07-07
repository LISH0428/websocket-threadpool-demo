package com.example.websocketdemo.websocket;


import com.alibaba.fastjson.JSON;
import com.example.websocketdemo.entity.IMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
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

    @SneakyThrows
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
       broadCast(currOnlineCount());
       broadCast(currOnlineList());
    }
    @OnClose
    public void onClose() throws IOException {
        if(serverMap.containsKey(userId)){
            serverMap.remove(userId);
            //从set中删除
            onlineCount-=1;
        }
        log.info("用户退出:"+userId+",当前在线人数为:" + onlineCount);
        broadCast(currOnlineCount());
        broadCast(currOnlineList());
    }

    @OnMessage
    public void onMessage(String message,Session session) throws IOException, InterruptedException {
        log.warn("{} 的消息：{}",userId,message);
        IMessage iMessage= JSON.parseObject(message,IMessage.class);
        String to = iMessage.getTo();
        if (serverMap.containsKey(to)) {
            WebsocketServer websocketServer = serverMap.get(to);
            websocketServer.session.getBasicRemote().sendText(JSON.toJSONString(iMessage));
        }else{
            log.error("目标用户不存在或已离线！");
        }


    }
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }





    //当前在线人数
    private IMessage currOnlineCount(){
        IMessage iMessage=new IMessage();
        iMessage.setMessage(String.valueOf(onlineCount));
        iMessage.setFrom("server");
        iMessage.setTo("count");
        return  iMessage;
    }
    //当前在线人员
    private IMessage currOnlineList(){
        StringBuffer list=new StringBuffer();
        IMessage iMessage=new IMessage();
        Iterator<String> iterator = serverMap.keySet().iterator();
        for (Iterator<String> it = iterator; it.hasNext(); ) {
            String s = it.next();
            list.append(s).append(",");
        }
        list.deleteCharAt(list.length()-1);
        iMessage.setMessage(list.toString());
        iMessage.setFrom("server");
        iMessage.setTo("list");
        return  iMessage;
    }
    //系统通知
    private void broadCast(IMessage iMessage){
        serverMap.forEach((key,value)->{
            try {
                value.session.getBasicRemote().sendText(JSON.toJSONString(iMessage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
