package top.keiskeiframework.system.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.system.entity.Dictionary;

/**
 * <p>
 * 数据字典 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Repository
public interface DictionaryRepository extends MongoRepository<Dictionary, ObjectId> {

}
