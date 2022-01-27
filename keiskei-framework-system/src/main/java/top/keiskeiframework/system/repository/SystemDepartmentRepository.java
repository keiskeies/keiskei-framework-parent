package top.keiskeiframework.system.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import top.keiskeiframework.system.entity.SystemDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * 部门管理 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@ConditionalOnProperty({"keiskei.use-department"})
public interface SystemDepartmentRepository extends JpaRepository<SystemDepartment, Long>, JpaSpecificationExecutor<SystemDepartment> {

}
