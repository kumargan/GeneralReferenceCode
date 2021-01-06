package transact.constants;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import transact.beans.SessionAndPmlIds;
import transact.beans.UserSession;

public class CommonConstants {
  public static final String QUEUE_REPLY = "/queue/reply";
  public static final String USER_QUEUE_REPLY = "/user/queue/reply";



  //Session and PML Ids
  public static Map<UserSession, SessionAndPmlIds> userSessionSessionAndPmlIdsMap
      = new ConcurrentHashMap<>(10000);

  // PML Ids and session map
  public static Map<Long, Set<UserSession>> pmlIdsToSessionMap
      = new ConcurrentHashMap<>( 100000 );

  // Map to hold lock on a session. Hold lock whenever edit is done on map.
  public static Map<UserSession, AtomicBoolean> sessionLockedMap
      = new ConcurrentHashMap<>( 100000 );
}
