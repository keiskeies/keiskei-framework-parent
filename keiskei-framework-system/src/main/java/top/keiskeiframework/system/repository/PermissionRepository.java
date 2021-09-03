package top.keiskeiframework.system.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.system.entity.Permission;

/**
 * <p>
 * 权限管理 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@Repository 
public interface PermissionRepository extends MongoRepository<Permission, ObjectId> {

}
