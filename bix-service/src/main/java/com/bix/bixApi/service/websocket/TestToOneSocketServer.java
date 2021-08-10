package com.bix.bixApi.service.websocket;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bix.config.auth.interceptor.UnAnth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/message")
@Component
@Slf4j
public class TestToOneSocketServer {
    /**
     * concurrent包的线程安全map，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, Set<TestToOneSocketServer>> messageSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 当前链接的用户
     */
    private String name = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    @UnAnth
    public void onOpen(Session session) {
        this.session = session;
        log.info("消息推送webSocket连接加入！");
        sendMessage(WebSocketData.builder().message("消息推送webSocket连接成功").build());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info("消息推送webSocket会话连接关闭!{}", this.name);
        if (!StringUtils.isEmpty(this.name)) {
            if (messageSocketMap.containsKey(this.name)) {
                Set<TestToOneSocketServer> userSet = messageSocketMap.get(this.name);
                if (userSet != null) {
                    userSet.remove(this);
                    if (userSet.isEmpty()) {
                        messageSocketMap.remove(this.name);
                    } else {
                        messageSocketMap.put(this.name, userSet);
                    }
                }
            }
//            messageSocketMap.remove(this.name);
        }
        log.info("消息推送webSocket会话连接关闭!");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 连接会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        if (StringUtils.equals("HeartBeat", message)) {
            log.info("消息webSocket心跳：{}", message);
            return;
        }
        this.session = session;
        this.name = message;
        Set<TestToOneSocketServer> userSet;
        if (messageSocketMap.containsKey(message)) {//该用户已经在其他客户端链接，则加入该用户集合中
            userSet = messageSocketMap.get(message);
        } else {
            userSet = new HashSet<>();
        }
        userSet.add(this);
        messageSocketMap.put(message, userSet);
//        messageSocketMap.put(message, this);//加入map中
        log.info("收到客户端的消息，session:{}，message:{}", session.getId(), message);
    }

    /**
     * @param session 连接会话
     * @param error   异常
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("会话连接异常，error:{}", error.getMessage(), error);
        try {
            session.close();
        } catch (IOException e) {
            log.error("关闭会话异常，error:{}", e.getMessage(), e);
        }
    }

    /**
     * 给指定的人发送消息
     *
     * @param webSocketData
     * @param userName
     */
    public void sendToUser(WebSocketData webSocketData, String userName) {
        try {
            if (messageSocketMap.containsKey(userName)) {
                Set<TestToOneSocketServer> userSet = messageSocketMap.get(userName);
                for (TestToOneSocketServer sendMessageServer : userSet) {
                    sendMessageServer.sendMessage(webSocketData);
                }
                log.info("发送消息给{}成功，消息内容：{}", userName, webSocketData.getData());
            } else {
                log.info("当前用户{}不在线", userName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发生自定义消息
     *
     * @param data 消息
     */
    private synchronized void sendMessage(WebSocketData data) {
        if (this.session.isOpen()) {
            try {
                this.session.getBasicRemote().sendText(JSONObject.toJSONString(data, SerializerFeature.WriteMapNullValue));
            } catch (IOException e) {
                log.error("推送信息异常，error:{}", e.getMessage(), e);
            }
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
