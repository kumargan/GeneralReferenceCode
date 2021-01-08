package transact.beans;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Data;

@Data
public class SessionAndPmlIds {

  private UserSession userSession;

  private volatile Set<Long> pmlIds = ConcurrentHashMap.newKeySet( 300 );

  public SessionAndPmlIds( UserSession userSession){
    this.userSession = userSession;
  }

}
