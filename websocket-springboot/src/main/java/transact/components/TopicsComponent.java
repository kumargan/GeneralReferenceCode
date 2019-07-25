package transact.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import transact.beans.response.Greeting;

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
	
	@Scheduled(fixedDelay = 2000L)
    public void forwardToTopic() {
    	template.convertAndSend("/topic/greetings", new Greeting(countComp.connCount.toString()));
    }
}
