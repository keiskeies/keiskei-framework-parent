package top.keiskeiframework.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.keiskeiframework.system.entity.Dashboard;

import java.util.List;

/**
 * <p>
 * 图表 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
public interface DashboardRepository extends JpaRepository<Dashboard, Long>, JpaSpecificationExecutor<Dashboard> {

    /**
     * 查询用户下所有图表
     * @param userId 创建人
     * @return 。
     */
    List<Dashboard> findAllByCreateUserIdOrderByCreateTimeDesc(Long userId);

}
