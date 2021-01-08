package transact.executor;

import lombok.extern.slf4j.Slf4j;
import transact.beans.UserSession;
import transact.beans.response.SubscriptionRes;

import java.util.concurrent.BlockingDeque;

@Slf4j
public class PublishTask implements Runnable {

    private UserSession userSession;

    public PublishTask(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    public void run() {
        //check if user session queue has some message then only publish
        try {
            BlockingDeque<String> blockingQueue = userSession.getMessageQueue(); //operating on same reference of queue
            int currentQueueSize = blockingQueue.size();
            while (currentQueueSize != 0) {
                String send = blockingQueue.poll(); //here if queue becomes empty return
                if (send == null) {
                    return;
                }
                SubscriptionRes subscriptionResponse = new SubscriptionRes();
                subscriptionResponse.setStatus(send);
                userSession.getSession().getBasicRemote().sendObject(subscriptionResponse);
                currentQueueSize--;
            }
        } catch (Exception e) {
            //TODO: we need to put in error queue
            log.error("Exception while processing the session {} due to {} ", userSession.getSessionId(), e);
        }
    }
}
