package transact.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

@Slf4j
@Component
public class ExecutorServiceUtil {

    private Integer threads = 100;
    private ThreadPoolExecutor threadPoolExecutor;
    private Integer queueSize = 1000;

    @PostConstruct
    public void init() {

        RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandlerImpl();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();

        threadPoolExecutor = new ThreadPoolExecutor(threads,
                threads,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueSize),
                threadFactory,
                rejectedExecutionHandler);
    }

    public synchronized Future<Boolean> checkAndSubmit(PublishTask publishTask) {

        Future<Boolean> future = null;
        if (threadPoolExecutor.getQueue().size() < queueSize) {
            future = threadPoolExecutor.submit(publishTask);
        }
        return future;
    }

    public void stopExecutors() {
        threadPoolExecutor.shutdown();
    }

}
