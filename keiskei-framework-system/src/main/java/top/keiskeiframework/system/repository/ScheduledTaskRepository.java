package top.keiskeiframework.system.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.system.entity.ScheduledTask;

/**
 * <p>
 * 定时任务 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@Repository 
public interface ScheduledTaskRepository extends MongoRepository<ScheduledTask, ObjectId> {
    /**
     * 时间表达式查找
     *
     * @param cronKey 定时方式
     * @return 定时任务
     */
    ScheduledTask findTopByCronKey(String cronKey);

}
