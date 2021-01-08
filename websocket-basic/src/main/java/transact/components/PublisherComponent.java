package transact.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import transact.beans.UserSession;
import transact.executor.ExecutorServiceUtil;
import transact.executor.PublishTask;

import javax.annotation.PostConstruct;
import java.util.Set;

import static transact.constants.CommonConstants.userSessionSessionAndPmlIdsMap;

@Slf4j
@Component
public class PublisherComponent implements Runnable {

    Thread thread;
    @Autowired
    ExecutorServiceUtil executorServiceUtil;

    @PostConstruct
    public void init() {
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while (true) {
            if (!CollectionUtils.isEmpty(userSessionSessionAndPmlIdsMap)) { //iterate till some active sessions available
                Set<UserSession> userSessionList = userSessionSessionAndPmlIdsMap.keySet();
                for (UserSession userSession : userSessionList) { //iterate over each active session where each have a queue of messages to be published
                    if (userSession.getSession().isOpen() && !userSession.getMessageQueue().isEmpty()) {
                        publishToUserSession(userSession);
                    }
                }
            }
            try {
                Thread.sleep(0);
            } catch (Exception e) {
                log.error("Error while making thread sleep in the publisher {}", e.getMessage());
            }
        }
    }

    private void publishToUserSession(UserSession userSession) {
        boolean publishSubmit = executorServiceUtil.checkAndSubmit(new PublishTask(userSession));
        while (!publishSubmit) {
            try {
                Thread.sleep(3);
            } catch (Exception e) {
                log.error("Exception while sleeping the thread", e);
            }
            log.info("queue is full trying again");
            publishSubmit = executorServiceUtil.checkAndSubmit(new PublishTask(userSession));
        }
    }

    public Thread getT() {
        return thread;
    }
}
