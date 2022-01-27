package top.keiskeiframework.system.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import top.keiskeiframework.system.entity.SystemRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * 角色管理 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@ConditionalOnProperty({"keiskei.use-permission"})
public interface SystemRoleRepository extends JpaRepository<SystemRole, Long>, JpaSpecificationExecutor<SystemRole> {

}
