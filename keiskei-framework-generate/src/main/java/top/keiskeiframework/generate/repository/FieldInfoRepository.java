package top.keiskeiframework.generate.repository;

import top.keiskeiframework.generate.entity.FieldInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * 表字段信息 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
public interface FieldInfoRepository extends JpaRepository<FieldInfo, Long>, JpaSpecificationExecutor<FieldInfo> {

}
