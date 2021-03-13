package top.keiskeiframework.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.keiskeiframework.system.entity.Dictionary;

/**
 * <p>
 * 数据字典 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
public interface DictionaryRepository extends JpaRepository<Dictionary, Long>, JpaSpecificationExecutor<Dictionary> {

}
