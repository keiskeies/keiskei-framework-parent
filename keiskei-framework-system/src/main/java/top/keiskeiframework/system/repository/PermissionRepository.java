package top.keiskeiframework.system.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty({"keiskei.use-permission"})
public interface PermissionRepository extends MongoRepository<Permission, String> {

}
