package top.keiskeiframework.demo.repository;

import top.keiskeiframework.demo.entity.Demo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * 表单测试 DAO 层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-18 14:23:37
 */
public interface DemoRepository extends JpaRepository<Demo, Long>, JpaSpecificationExecutor<Demo> {

}
