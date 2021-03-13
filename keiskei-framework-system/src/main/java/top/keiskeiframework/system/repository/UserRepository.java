package top.keiskeiframework.system.repository;

import top.keiskeiframework.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author v_chenjiamin
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return .
     */
    User findTopByUsername(String username);
}
