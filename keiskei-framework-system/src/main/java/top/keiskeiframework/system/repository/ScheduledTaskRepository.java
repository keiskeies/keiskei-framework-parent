package top.keiskeiframework.system.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
public interface ScheduledTaskRepository extends JpaRepository<ScheduledTask, Long>, JpaSpecificationExecutor<ScheduledTask> {
    /**
     * 时间表达式查找
     *
     * @param cronKey 定时方式
     * @return 定时任务
     */
    ScheduledTask findTopByCronKey(String cronKey);

}
