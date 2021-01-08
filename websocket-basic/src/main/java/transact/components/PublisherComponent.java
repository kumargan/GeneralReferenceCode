package transact.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import transact.beans.UserSession;
import transact.executor.ExecutorServiceUtil;
import transact.executor.PublishTask;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

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
            List<Future<Boolean>> boolFutureListPub = new ArrayList<>();
            if (!CollectionUtils.isEmpty(userSessionSessionAndPmlIdsMap)) { //iterate till some active sessions available

                Set<UserSession> userSessionList = userSessionSessionAndPmlIdsMap.keySet();
                for (UserSession userSession : userSessionList) { //iterate over each active session where each have a queue of messages to be published
                    if (userSession.getSession().isOpen() && !userSession.getMessageQueue().isEmpty()) {
                        boolFutureListPub.add(publishToUserSession(userSession));
                    }
                }
                for (Future<Boolean> future : boolFutureListPub) {
                    try {
                        future.get();
                    } catch (Exception e) {
                        log.error("Exception while fetching the future from the list while packets.", e);
                    }
                }
            }
        }
    }

    private Future<Boolean> publishToUserSession(UserSession userSession) {
        Future<Boolean> publishSubmit = executorServiceUtil.checkAndSubmit(new PublishTask(userSession));
        while (publishSubmit == null) {
            try {
                Thread.sleep(5);
            } catch (Exception e) {
                log.error("Exception while sleeping the thread", e);
            }
            publishSubmit = executorServiceUtil.checkAndSubmit(new PublishTask(userSession));
            log.info("queue is full trying again");
        }
        return publishSubmit;
    }

    public Thread getT() {
        return thread;
    }
}
