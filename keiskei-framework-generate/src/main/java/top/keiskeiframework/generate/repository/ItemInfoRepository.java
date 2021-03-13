package top.keiskeiframework.generate.repository;

import top.keiskeiframework.generate.entity.ProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * 项目信息 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
public interface ItemInfoRepository extends JpaRepository<ProjectInfo, Long>, JpaSpecificationExecutor<ProjectInfo> {

}
