package top.keiskeiframework.generate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.keiskeiframework.generate.entity.FieldEnumInfo;

/**
 * <p>
 * 表字段信息 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
public interface FieldEnumInfoRepository extends JpaRepository<FieldEnumInfo, Long>, JpaSpecificationExecutor<FieldEnumInfo> {

}
