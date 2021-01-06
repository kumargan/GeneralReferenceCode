package transact.components;

import static transact.constants.CommonConstants.pmlIdsToSessionMap;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import transact.beans.UserSession;

@Component
@EnableScheduling
@Slf4j
public class TopicsComponent {
  @Autowired
  CountActiveConnectionsComponent countComp;

  int total = 10000;
  int span = 10000;
  private static int threads = 10;
  ExecutorService executorService = Executors.newFixedThreadPool(threads );
  
  //template.convertAndSend("/topic/"+j, j);
  @EventListener(ApplicationReadyEvent.class)
  public void forwardToTopic() {
    
    //for(int i=0;i<total;i=i+span)
      //executorService.submit(new GenericTask(template, i,i+span));
    //RS//

    for (int i =0 ;i < threads; i++) {
      AtomicInteger integer = new AtomicInteger( i );
      CompletableFuture.runAsync(
          () ->
              addMessageToTopics(integer),
          executorService
      );
    }

  }
  private void addMessageToTopics( AtomicInteger integer){
    int l = integer.get();
    for (int i = l*500 + 1; i<=(l+1)*500;i++){
      for (UserSession userSession : pmlIdsToSessionMap.get(i)) {
        userSession.getMessageQueue().add( "message" + i );
      }
    }
  }
  
}

