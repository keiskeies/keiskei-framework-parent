package top.keiskeiframework.system.repository;

import top.keiskeiframework.system.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author v_chenjiamin
 */
public interface SystemUserRepository extends JpaRepository<SystemUser, Long>, JpaSpecificationExecutor<SystemUser> {
    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return .
     */
    SystemUser findTopByUsername(String username);
}
