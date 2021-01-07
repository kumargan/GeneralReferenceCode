package transact.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import transact.beans.UserSession;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static transact.constants.CommonConstants.pmlIdsToSessionMap;

@Component
@EnableScheduling
@Slf4j
public class TopicsComponent {
    private static final int threads = 20;
    @Autowired
    CountActiveConnectionsComponent countComp;
    int total = 10000;
    int span = 10000;
    ExecutorService executorService = Executors.newFixedThreadPool(threads);

    //template.convertAndSend("/topic/"+j, j);
    @EventListener(ApplicationReadyEvent.class)
    //@PostConstruct
    public void forwardToTopic(ApplicationReadyEvent event) {

        //for(int i=0;i<total;i=i+span)
        //executorService.submit(new GenericTask(template, i,i+span));
        //RS//

        for (int i = 0; i < threads; i++) {
            AtomicLong integer = new AtomicLong(i);
            CompletableFuture.runAsync(
                    () ->
                            addMessageToTopics(integer),
                    executorService
            );
        }
        //CompletableFuture.runAsync(this::sendMessage, executorService );
    }

    private void addMessageToTopics(AtomicLong integer) {
        while (true) {
            long l = integer.get();
            for (long i = l * 500 + 1; i <= (l + 1) * 500; i++) {
                for (UserSession userSession : pmlIdsToSessionMap.get(i)) {
                    userSession.getMessageQueue().add("message_" + i);
                }

            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage() {

        while (true) {
            int p = 1;
            try {
                if (pmlIdsToSessionMap.get(1L) != null) {
                    pmlIdsToSessionMap.get(1L).forEach(userSession -> {
                        int size = userSession.getMessageQueue().size();
                        for (int i = 0; i < size; i++) {
                            userSession.getSession().getAsyncRemote()
                                    .sendText(userSession.getMessageQueue().poll());
                        }
                    });
                }
            } catch (Exception e) {
                log.error("Exception caught", e);
            }
        }
    }
}

