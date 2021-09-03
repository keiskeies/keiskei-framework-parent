package top.keiskeiframework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.keiskeiframework.entity.TestInfo;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/3 00:57
 */
public interface TestInfoRepository extends JpaRepository<TestInfo, String>, JpaSpecificationExecutor<TestInfo> {
}
