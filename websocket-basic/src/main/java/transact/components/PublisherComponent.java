package transact.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import transact.beans.UserSession;
import transact.beans.response.SubscriptionRes;
import transact.executor.ExecutorServiceUtil;
import transact.executor.PublishTask;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingDeque;

import static transact.constants.CommonConstants.userSessionSessionAndPmlIdsMap;

@Slf4j
@Component
public class PublisherComponent implements Runnable { //when does this thread stop which will try to pull and publish to sessions
    //it will run as a thread / cron every second infinite loop
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
                    //check if user session queue has some message then only publish
                    try {
                        if (userSession.getSession().isOpen() && !userSession.getMessageQueue().isEmpty()) {
                            int currentQueueSize = userSession.getMessageQueue().size();
                            List<String> subscriptionRes = new ArrayList<>();
                            BlockingDeque<String> blockingQueue = userSession.getMessageQueue();
                            while (currentQueueSize != 0) {
                                log.info("publishing {} size data", currentQueueSize);
                                subscriptionRes.add(blockingQueue.poll());
                                currentQueueSize--;
                            }
                            if (!subscriptionRes.isEmpty()) {
                                log.info("Publish message {}", subscriptionRes);
                                //user's queue over send messages asynchronously
                                SubscriptionRes subscriptionRes1 = new SubscriptionRes();
                                subscriptionRes1.setStatus(subscriptionRes);
                                userSession.getSession().getBasicRemote().sendObject(subscriptionRes1);
                            }
                        }
                    } catch (Exception e) {
                        //TODO: we need to put in error queue
                    }
                }
            }
        }


    }

    //can be used later
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
