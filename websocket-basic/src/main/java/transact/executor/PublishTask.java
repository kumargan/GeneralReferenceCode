package transact.executor;

import lombok.extern.slf4j.Slf4j;
import transact.beans.UserSession;

@Slf4j
public class PublishTask implements Runnable {

    private UserSession userSession;

    public PublishTask(UserSession userSession) {
        this.userSession = userSession;
    }


    @Override
    public void run() {
        log.info("Publish task started");
    }
}
