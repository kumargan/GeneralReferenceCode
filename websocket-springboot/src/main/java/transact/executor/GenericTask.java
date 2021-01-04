package transact.executor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericTask implements Runnable{
  SimpMessagingTemplate template;
  int start;
  int end;

  public GenericTask(SimpMessagingTemplate template,int start,int end) {
    this.template=template;
    this.start=start;
    this.end=end;
  }

  public void run() {
    try {
      while(true) {
        Thread.sleep(100);
        for(int i=start;i<end;i++) {
          template.convertAndSend("/topic/"+i, "qerr");
          //log.info("writing {}",i);
        }
      }
    }catch(Exception e) {
      log.error("Process method in genericProcessor should not throw any exception. PLease make code change. Exception is", e);
    }

  }

}
