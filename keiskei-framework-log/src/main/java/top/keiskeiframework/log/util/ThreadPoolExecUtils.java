package top.keiskeiframework.log.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import top.keiskeiframework.log.config.ThreadPoolProperties;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/1/27 15:11
 */
@Component
public class ThreadPoolExecUtils {

    private static ThreadPoolExecutor THREAD_POOL_EXECUTOR;

    @Autowired
    private void setThreadPoolProperties(ThreadPoolProperties threadPoolProperties) {
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
                threadPoolProperties.getCorePoolSize(),
                threadPoolProperties.getMaximumPoolSize(),
                threadPoolProperties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(threadPoolProperties.getCapacity()),
                new CustomizableThreadFactory(threadPoolProperties.getThreadNamePrefix()));
    }

    public static void execute(Runnable task) {
        if (task == null) {
            throw new RuntimeException("执行任务为空");
        }
        THREAD_POOL_EXECUTOR.execute(task);
    }
}