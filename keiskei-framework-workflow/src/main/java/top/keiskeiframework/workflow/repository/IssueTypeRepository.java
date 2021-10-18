package top.keiskeiframework.workflow.repository;

import top.keiskeiframework.workflow.entity.IssueType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * 卡片类型 DAO 层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:38:26
 */
public interface IssueTypeRepository extends JpaRepository<IssueType, Long>, JpaSpecificationExecutor<IssueType> {

}
