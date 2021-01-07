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
import javax.annotation.PostConstruct;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
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
@Component
public class MainClient {

  @PostConstruct
  public void main() throws Exception {
    Long start=System.currentTimeMillis();
    Client client = null;
    int noOfClients = 20;
    List<Client> clients = new ArrayList<>();
        
    for(Integer j=0;j<noOfClients;j++){
      client = new Client(j.toString());
      clients.add(client);
      client.start();
    }

    for(Client clnt:clients){
      while(true) {
        for(Long topic:clnt.list)
          log.info("client & topic : {}, count : {}",clnt.clientName+"-"+topic,clnt.map.get(topic));
        log.info("time spent {} ",(System.currentTimeMillis()-start)/1000);
        Thread.sleep(10000);
      }
    }
    
  }

}
