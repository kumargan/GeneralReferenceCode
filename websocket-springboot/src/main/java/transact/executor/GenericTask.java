package transact.executor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericTask implements Runnable{
  SimpMessagingTemplate template;
  int start;

  public GenericTask(SimpMessagingTemplate template,int start) {
    this.template=template;
    this.start=start;
  }

  public void run() {
    try {
      while(true)
        for(int i=start;i<start+500;i++) {
          template.convertAndSend("/topic/"+i, i);
          //log.info("writing {}",i);
        }
    }catch(Exception e) {
      log.error("Process method in genericProcessor should not throw any exception. PLease make code change. Exception is", e);
    }

  }

}
