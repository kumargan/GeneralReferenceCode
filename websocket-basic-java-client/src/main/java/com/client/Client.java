package com.client;

import com.client.enums.Type;
import com.client.request.SubscriptionReq;
import com.client.response.SubscriptionRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Slf4j
public class Client {
    WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
    Random random = new Random();
    Map<Long, BigInteger> map = new ConcurrentHashMap<>();
    List<String> list = new ArrayList<>();
    String clientName = null;

    public Client(String name) {
        clientName = name;
    }

    public ListenableFuture<WebSocketSession> connect() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        String url = "ws://localhost:8080/ticks";
        return webSocketClient.doHandshake(new MyHandler(), url, headers);
    }

    public void subscribeTopic(WebSocketSession webSocketSession, Long topic) throws ExecutionException, InterruptedException, IOException {
        map.put(topic, new BigInteger("0"));
        ObjectMapper objectMapper = new ObjectMapper();
        SubscriptionReq subscriptionReq = new SubscriptionReq();
        subscriptionReq.setPmlId(topic);
        subscriptionReq.setType(Type.S);
        webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(subscriptionReq)));
    }


    public void sendHello(StompSession stompSession) {
        String jsonHello = "{ \"name\" : \"Nick\" }";
        stompSession.send("/app/hello", jsonHello.getBytes());
    }

    public void start() {
        try {
            ListenableFuture<WebSocketSession> f = this.connect();
            WebSocketSession webSocketSession = f.get();

            log.info("Subscribing to pml id using session " + webSocketSession);

            //subscribe to 20 sessions
            for (int i = 0; i < 20; i++)
                this.subscribeTopic(webSocketSession, new Long(random.nextInt(10000)));

        } catch (Exception e) {
            log.error(" some error {}", e.getMessage());
        }
    }

    private class MyHandler implements WebSocketHandler {
/*
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            log.info("Now connected");
        }
*/

        @Override
        public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
            log.info("Now connected");
        }

        @Override
        public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            SubscriptionRes subscriptionRes = objectMapper.readValue(webSocketMessage.getPayload().toString(), SubscriptionRes.class);
            map.put(Long.valueOf(subscriptionRes.getStatus()), map.get(Long.valueOf(subscriptionRes.getStatus())).add(BigInteger.ONE));

        }

        @Override
        public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
            log.info("Now error");
        }

        @Override
        public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
            log.info("Now closed");
        }

        @Override
        public boolean supportsPartialMessages() {
            return false;
        }
    }

}
