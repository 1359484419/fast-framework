package com.bix.bixApi.service.websocket;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bix.config.auth.interceptor.UnAnth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/ws/order")
@Component
@Slf4j
public class TestToAllSocketServer {

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<TestToAllSocketServer> orderWebSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 群发自定义消息
     */
    public static void sendInfo(WebSocketData webSocketData) {
        log.debug("WebSocket消息，{}", webSocketData);
        orderWebSocketSet.parallelStream().forEach(item -> {
            if (item != null) {
                item.sendMessage(webSocketData);
            }
        });
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    @UnAnth
    public void onOpen(Session session) {
        this.session = session;
        orderWebSocketSet.add(this);
        log.info("webSocket连接加入！");
        sendMessage(WebSocketData.builder().message("webSocket连接成功").build());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        orderWebSocketSet.remove(this);
        log.info("webSocket会话连接关闭!");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 连接会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
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
