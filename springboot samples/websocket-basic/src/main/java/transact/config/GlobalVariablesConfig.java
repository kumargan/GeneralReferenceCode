package transact.config;

import static transact.constants.CommonConstants.pmlIdsToSessionMap;

import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalVariablesConfig {

  @PostConstruct
  public void initializePmlIdsSet(){

    for (long i =0L; i<=10000L; i++){
      pmlIdsToSessionMap.put( i, ConcurrentHashMap.newKeySet( 300 ));
    }

  }

}
