package top.keiskeiframework.generate.repository;

import top.keiskeiframework.generate.entity.TableInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * 表结构信息 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
public interface TableInfoRepository extends JpaRepository<TableInfo, Long>, JpaSpecificationExecutor<TableInfo> {

}
