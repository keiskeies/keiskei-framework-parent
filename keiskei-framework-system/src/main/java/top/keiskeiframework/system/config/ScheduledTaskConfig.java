package top.keiskeiframework.system.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;
import top.keiskeiframework.system.entity.ScheduledTask;
import top.keiskeiframework.system.service.IScheduledTaskService;
import top.keiskeiframework.system.thread.ScheduledOfTask;

import java.util.concurrent.*;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/15 18:13
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty({"keiskei.use-scheduled-task"})
public class ScheduledTaskConfig implements SchedulingConfigurer {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private IScheduledTaskService scheduledTaskService;
    @Override
    public void configureTasks(@NonNull ScheduledTaskRegistrar taskRegistrar) {
        for (ScheduledTask scheduledTask : scheduledTaskService.findAll()) {


            Class<?> clazz;
            ScheduledOfTask task;
            try {
                clazz = Class.forName(scheduledTask.getCronKey());
                Assert.isAssignable(ScheduledOfTask.class, clazz, "定时任务类必须实现ScheduledOfTask接口");
                task = (ScheduledOfTask)clazz.newInstance();
                task.setId(scheduledTask.getId());
                task.setApplicationContext(applicationContext);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // 可以通过改变数据库数据进而实现动态改变执行周期
            taskRegistrar.addTriggerTask(task,
                    triggerContext -> new CronTrigger(scheduledTaskService.getById(scheduledTask.getId()).getCron()).nextExecutionTime(triggerContext)
            );
        }
    }

    @Bean
    public Executor taskExecutor() {
        return new ThreadPoolExecutor(
                2,
                5,
                1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}