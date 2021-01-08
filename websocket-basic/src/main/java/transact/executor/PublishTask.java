package transact.executor;

import lombok.extern.slf4j.Slf4j;
import transact.beans.UserSession;
import transact.beans.response.SubscriptionRes;
import transact.constants.CommonConstants;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;

@Slf4j
public class PublishTask implements Callable<Boolean> {

    private UserSession userSession;

    public PublishTask(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    public Boolean call() {
        try {
            BlockingDeque<String> blockingQueue = userSession.getMessageQueue();
            int currentQueueSize = Math.min(blockingQueue.size(), CommonConstants.PACKETS_BATCH_SIZE);
            while (currentQueueSize != 0) {
                String send = blockingQueue.poll();
                SubscriptionRes subscriptionResponse = new SubscriptionRes();
                subscriptionResponse.setStatus(send);
                userSession.getSession().getAsyncRemote().sendObject(subscriptionResponse);
                currentQueueSize--;
            }
        } catch (Exception e) {
            //TODO: we need to put in error queue
            log.error("Exception while processing the session {} due to {} ", userSession.getSessionId(), e.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
