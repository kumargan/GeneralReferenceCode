package com.client;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {
  WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
  Random random = new Random();
  Map<String,BigInteger> map = new ConcurrentHashMap<>();
  List<String> list = new ArrayList<>();
  String clientName=null;

  public Client(String name) {
    clientName=name;
  }

  public ListenableFuture<StompSession> connect() {

    Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
    List<Transport> transports = Collections.singletonList(webSocketTransport);

    SockJsClient sockJsClient = new SockJsClient(transports);
    sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

    WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

    String url = "ws://{host}:{port}/feed/ticks";
    return stompClient.connect(url, headers, new MyHandler(), "feedmux-env-1.eba-pmidvhqn.us-east-1.elasticbeanstalk.com", 80);
  }

  public void subscribeTopic(StompSession stompSession, String topic) throws ExecutionException, InterruptedException {

    map.put(topic, new BigInteger("0"));
    list.add(topic);

    stompSession.subscribe("/topic/"+topic, new StompFrameHandler() {
      String t= topic;
      public Type getPayloadType(StompHeaders stompHeaders) {
        return byte[].class;
      }

      public void handleFrame(StompHeaders stompHeaders, Object o) {
        map.put(t,map.get(t).add(BigInteger.ONE));
        //log.info(new String((byte[]) o));
      }
    });
  }

  public void subscribeUser(StompSession stompSession) throws ExecutionException, InterruptedException {
    stompSession.subscribe("/user/queue/reply", new StompFrameHandler() {

      public Type getPayloadType(StompHeaders stompHeaders) {
        return byte[].class;
      }

      public void handleFrame(StompHeaders stompHeaders, Object o) {
        log.info(new String((byte[]) o));
      }
    });
  }

  public void sendHello(StompSession stompSession) {
    String jsonHello = "{ \"name\" : \"Nick\" }";
    stompSession.send("/app/hello", jsonHello.getBytes());
  }

  private class MyHandler extends StompSessionHandlerAdapter {
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
      log.info("Now connected");
    }
  }
  
  public void start() {
    try {
      ListenableFuture<StompSession> f = this.connect();
      StompSession stompSession = f.get();

      log.info("Subscribing to topic using session " + stompSession);
      this.subscribeUser(stompSession);

      //subscribe to 20 sessions
      for(int i=0;i<20;i++)
        this.subscribeTopic(stompSession,new Integer(random.nextInt(10000)).toString());

     

      //log.info("Sending hello message" + stompSession);
      //helloClient.sendHello(stompSession);
    }catch(Exception e) {
      log.error(" some error {}",e);
    }
  }

}
