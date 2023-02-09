package transact.components;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.StompSubProtocolHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CountActiveConnectionsComponent {//implements ApplicationListener{
  AtomicInteger connCount = new AtomicInteger(0);


  public AtomicInteger getConnCount() {
    return connCount;
  }


  public void incConnCount() {  
    connCount.incrementAndGet();
    log.info(" new connection , connections {} ",connCount.get());
  }

  public void decConnCount() {  
    connCount.decrementAndGet();
    log.info(" connection closed , connections {}",connCount.get());
  }


  //@Override
  public void onApplicationEvent(ApplicationEvent arg0) {

    if(arg0 instanceof SessionConnectEvent) 
      incConnCount();
    else if(arg0 instanceof SessionDisconnectEvent) 
      decConnCount();
    
  }
}
