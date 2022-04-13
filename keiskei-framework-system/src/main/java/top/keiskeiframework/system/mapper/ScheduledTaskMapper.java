package top.keiskeiframework.system.mapper;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import top.keiskeiframework.common.base.mapper.BaseEntityMapper;
import top.keiskeiframework.system.entity.ScheduledTask;

/**
 * <p>
 * 定时任务 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@ConditionalOnProperty({"keiskei.use-scheduled-task"})
public interface ScheduledTaskMapper extends BaseEntityMapper<ScheduledTask, Long> {

}
