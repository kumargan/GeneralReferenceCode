package transact.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

        log.error(" Exception in submitting tasks, this error should not have been thrown. There is issue in submitting tasks. Tasks should be submitted only when the Queue size is < no of threads in config");
    }
}
