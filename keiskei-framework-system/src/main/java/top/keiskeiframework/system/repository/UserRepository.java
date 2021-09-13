package top.keiskeiframework.system.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.keiskeiframework.system.entity.User;

/**
 * @author v_chenjiamin
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return .
     */
    User findTopByUsername(String username);
}
