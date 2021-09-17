package top.keiskeiframework.email.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.email.entity.PushTask;

/**
 * <p>
 * 推送任务DAO
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/17 23:20
 */
@Repository
public interface PushTaskRepository extends MongoRepository<PushTask, String> {
}
