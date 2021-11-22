package top.keiskeiframework.generate.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.generate.entity.TableInfo;

/**
 * <p>
 * 表结构信息 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Repository
public interface TableInfoRepository extends MongoRepository<TableInfo, String> {

}
