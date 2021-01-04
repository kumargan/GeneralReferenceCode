package transact.components;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@EnableScheduling
@Slf4j
public class TopicsComponent {
  @Autowired
  CountActiveConnectionsComponent countComp;
  int total = 10000;
  int span = 10000       ;
  ExecutorService executorService = Executors.newFixedThreadPool(total/span);
  
  //template.convertAndSend("/topic/"+j, j);
  @PostConstruct
  public void forwardToTopic() {
    
    //for(int i=0;i<total;i=i+span)
      //executorService.submit(new GenericTask(template, i,i+span));
  }
  
}

