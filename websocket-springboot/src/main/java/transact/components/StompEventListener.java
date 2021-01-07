package transact.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import lombok.extern.slf4j.Slf4j;
import transact.constants.CommonConstants;

@Slf4j
@Component
public class StompEventListener {
  
  @Autowired
  SimpMessagingTemplate template;
  
  @Autowired
  CountActiveConnectionsComponent counter;

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
      counter.incConnCount();
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
      counter.decConnCount();
    }

    @EventListener
    private void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
      
      //log.info(" subscription received {}",event.getMessage());
      StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
      //log.info(" subscription headers.getSessionId() {}",headers.getDestination());
      
      if(!headers.getDestination().trim().equals(CommonConstants.USER_QUEUE_REPLY)) {
        //write first packet to user
        template.convertAndSendToUser(headers.getSessionId(),CommonConstants.QUEUE_REPLY ," etf "+headers.getSessionId(), 
            createHeaders(headers.getSessionId()));
      }
    }
    
    private MessageHeaders createHeaders(String sessionId) {
      SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
      headerAccessor.setSessionId(sessionId);
      headerAccessor.setLeaveMutable(true);
      return headerAccessor.getMessageHeaders();
    }

    @EventListener
    private void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {

    }
}