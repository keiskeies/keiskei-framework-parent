package top.keiskeiframework.generate.repository;

import top.keiskeiframework.generate.entity.ModuleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * 模块信息 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
public interface ModuleInfoRepository extends JpaRepository<ModuleInfo, Long>, JpaSpecificationExecutor<ModuleInfo> {

}
