package transact.constants;

import com.fasterxml.jackson.databind.ObjectMapper;
import transact.beans.SessionAndPmlIds;
import transact.beans.UserSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommonConstants {
    public static final String QUEUE_REPLY = "/queue/reply";
    public static final String USER_QUEUE_REPLY = "/user/queue/reply";
    public static final int PACKETS_BATCH_SIZE = 100;

    public static ObjectMapper objectMapper = new ObjectMapper();


    //Session and PML Ids
    public static volatile Map<UserSession, SessionAndPmlIds> userSessionSessionAndPmlIdsMap
            = new ConcurrentHashMap<>(10000);

    // PML Ids and session map
    public static volatile Map<Long, Set<UserSession>> pmlIdsToSessionMap
            = new ConcurrentHashMap<>(100000);

    // Map to hold lock on a session. Hold lock whenever edit is done on map.
    public static volatile Map<UserSession, AtomicBoolean> sessionLockedMap
            = new ConcurrentHashMap<>(100000);
    // Set to store inactive sessions
    public static volatile BlockingQueue<UserSession> inactiveSessionsSet
            = new LinkedBlockingQueue<>(100000);
}
