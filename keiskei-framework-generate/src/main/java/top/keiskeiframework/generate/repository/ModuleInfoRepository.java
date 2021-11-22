package top.keiskeiframework.generate.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.generate.entity.ModuleInfo;

/**
 * <p>
 * 模块信息 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Repository
public interface ModuleInfoRepository extends MongoRepository<ModuleInfo, String> {

}
