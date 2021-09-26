package top.keiskeiframework.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.system.entity.ScheduledTask;
import top.keiskeiframework.system.service.IScheduledTaskService;
import top.keiskeiframework.system.thread.ScheduledOfTask;

/**
 * <p>
 * 定时任务 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@Service
@ConditionalOnProperty({"keiskei.use-scheduled-task"})
public class ScheduledTaskServiceImpl extends ListServiceImpl<ScheduledTask, Long> implements IScheduledTaskService {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    @Lazy
    private IScheduledTaskService scheduledTaskService;


    @Override
    public void excute(Long id) {

        ScheduledTask scheduledTask = scheduledTaskService.findById(id);
        Assert.notNull(scheduledTask, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
        try {
            ((ScheduledOfTask) applicationContext.getBean(Class.forName(scheduledTask.getCronKey()))).execute(scheduledTask);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
