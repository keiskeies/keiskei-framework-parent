package top.keiskeiframework.content.page.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.content.page.entity.Topic;

/**
 * <p>
 *科技资讯实体类
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/3 00:57
 */
@Repository
public interface TopicRepository extends MongoRepository<Topic, String> {
}
