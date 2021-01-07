package transact.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AsyncProcessing {

    @Autowired
    CountActiveConnectionsComponent countActiveConnections;

    @Scheduled(fixedDelay = 10000)
    public void logTotalActiveConnections() {
        log.info("Total active connections: {}", countActiveConnections.getConnCount());
/*    log.info("Map userSessionSessionAndPmlIdsMap : {}", userSessionSessionAndPmlIdsMap);
    log.info("Map pmlIdsToSessionMap : {}", pmlIdsToSessionMap);
    for (UserSession userSession : userSessionSessionAndPmlIdsMap.keySet()){
      log.info("SessionId {}, Length : {}", userSession.getSessionId(), userSession.getMessageQueue().size());
    }*/
    }
}
