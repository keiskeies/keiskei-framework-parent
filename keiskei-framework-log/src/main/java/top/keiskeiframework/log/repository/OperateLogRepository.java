package top.keiskeiframework.log.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.log.entity.OperateLog;

/**
 * <p>
 * 操作日志 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Repository
@ConditionalOnProperty(value = {"keiskei.use-operateLog"})
public interface OperateLogRepository extends MongoRepository<OperateLog, String> {

}
