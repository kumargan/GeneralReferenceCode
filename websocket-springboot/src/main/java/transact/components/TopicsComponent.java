package transact.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class TopicsComponent {
	@Autowired
	SimpMessagingTemplate template;
	CountActiveConnectionsComponent countComp;
	
	@Autowired
	public TopicsComponent(CountActiveConnectionsComponent countComp) {
		this.countComp = countComp;
	}
	
	@Scheduled(fixedDelay = 2L)
    public void forwardToTopic() {
	  
	  for(int i=0;i<=10000;i++) {
    	template.convertAndSend("/topic/"+i, i);
	  }
      
    }
}
