package top.keiskeiframework.info.tag.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.info.tag.entity.TagInfo;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/13 22:12
 */
@Repository
public interface TagInfoRepository extends MongoRepository<TagInfo, String> {
}
