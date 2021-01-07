package transact.components;

import static transact.constants.CommonConstants.pmlIdsToSessionMap;
import static transact.constants.CommonConstants.sessionLockedMap;
import static transact.constants.CommonConstants.userSessionSessionAndPmlIdsMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import transact.beans.UserSession;

@Component
@Slf4j
public class AsyncProcessing {

  @Autowired
  CountActiveConnectionsComponent countActiveConnections;

  @Scheduled( fixedDelay = 20000)
  public void logTotalActiveConnections(){
    log.info("Total active connections: {}", countActiveConnections.getConnCount());
    /*log.info("Map userSessionSessionAndPmlIdsMap : {}", userSessionSessionAndPmlIdsMap);
    log.info("Map pmlIdsToSessionMap : {}", pmlIdsToSessionMap);
    for (UserSession userSession : userSessionSessionAndPmlIdsMap.keySet()){
      log.info("SessionId {}, Length : {}", userSession.getSessionId(), userSession.getMessageQueue().size());
    }*/
  }
}
