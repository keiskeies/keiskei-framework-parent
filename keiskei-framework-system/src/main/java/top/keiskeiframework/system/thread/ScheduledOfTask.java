package top.keiskeiframework.system.thread;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import top.keiskeiframework.common.util.SpringUtils;
import top.keiskeiframework.system.entity.ScheduledTask;
import top.keiskeiframework.system.service.IScheduledTaskService;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/15 18:17
 */
@Setter
public class ScheduledOfTask implements Runnable {

    private Integer id;
    protected ApplicationContext applicationContext;

    /**
     * 定时任务方法
     * @param scheduledTask 定时任务
     */
    public void execute(ScheduledTask scheduledTask) {
    }


    /**
     * 实现控制定时任务启用或禁用的功能
     */
    @Override
    public void run() {
        IScheduledTaskService scheduledTaskService = SpringUtils.getBean(IScheduledTaskService.class);
        ScheduledTask scheduledTask = scheduledTaskService.getById(id);
        if (null == scheduledTask || !scheduledTask.getEnable()) {
            return;
        }
        execute(scheduledTask);
    }
}