package top.keiskeiframework.email.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.email.entity.PushRecord;

/**
 * <p>
 * 推送记录DAO
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/17 23:20
 */
@Repository
public interface PushRecordRepository extends MongoRepository<PushRecord, String> {
}
