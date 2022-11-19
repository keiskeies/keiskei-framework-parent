package top.keiskeiframework.system.service;

import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.system.entity.ScheduledTask;

/**
 * <p>
 * 定时任务 业务接口层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
public interface IScheduledTaskService extends IBaseService<ScheduledTask, Integer> {

    /**
     * 更改任务状态
     *
     * @param id .
     */
    void execute(Integer id);

}
