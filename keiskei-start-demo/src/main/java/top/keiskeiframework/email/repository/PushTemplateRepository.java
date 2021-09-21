package top.keiskeiframework.email.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.email.entity.PushTemplate;

/**
 * <p>
 * 推送模版DAO
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/17 23:20
 */
@Repository
public interface PushTemplateRepository extends MongoRepository<PushTemplate, String> {
}
