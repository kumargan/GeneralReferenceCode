package transact.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import transact.beans.CacheBean;

public class IdCache {
  Map<String,CacheBean> cache = new ConcurrentHashMap<>();
}
