package transact.components;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CountActiveConnectionsComponent {
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
}
