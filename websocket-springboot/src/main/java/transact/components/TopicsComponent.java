package transact.components;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import transact.executor.GenericTask;

@Component
@EnableScheduling
@Slf4j
public class TopicsComponent {
  @Autowired
  SimpMessagingTemplate template;
  CountActiveConnectionsComponent countComp;
  ExecutorService executorService = Executors.newFixedThreadPool(20);

  @Autowired
  public TopicsComponent(CountActiveConnectionsComponent countComp) {
    this.countComp = countComp;
  }
  
  //template.convertAndSend("/topic/"+j, j);
  @PostConstruct
  public void forwardToTopic() {
    for(int i=0;i<20;i++)
      executorService.submit(new GenericTask(template, i*500));
  }
  
}

