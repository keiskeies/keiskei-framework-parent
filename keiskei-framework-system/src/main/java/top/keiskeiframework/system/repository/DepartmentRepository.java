package top.keiskeiframework.system.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.system.entity.Department;

/**
 * <p>
 * 部门管理 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Repository
@ConditionalOnProperty({"keiskei.use-department"})
public interface DepartmentRepository extends MongoRepository<Department, String> {

}
