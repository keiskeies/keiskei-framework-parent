package top.keiskeiframework.system.repository;

import top.keiskeiframework.system.entity.OperateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * 操作日志 DAO 层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
public interface OperateLogRepository extends JpaRepository<OperateLog, Long>, JpaSpecificationExecutor<OperateLog> {

}
