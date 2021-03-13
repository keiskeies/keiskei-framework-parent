package top.keiskeiframework.system.repository;

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
public interface ScheduledTaskRepository extends JpaRepository<ScheduledTask, Long>, JpaSpecificationExecutor<ScheduledTask> {
    /**
     *
     * @param cronKey
     * @return
     */
    ScheduledTask findTopByCronKey(String cronKey);

}
