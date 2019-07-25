package transact.components;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class CountActiveConnectionsComponent implements ApplicationListener{
	AtomicInteger connCount = new AtomicInteger(0);

	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		
		if(arg0 instanceof SessionConnectEvent) {
			System.out.println(" new connection ");
			connCount.incrementAndGet();
		}else if(arg0 instanceof SessionDisconnectEvent) {
			System.out.println(" connection closed");
			connCount.decrementAndGet();
		}
		
	}
}
