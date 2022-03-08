package top.keiskeiframework.cpreading.book.repository;

import top.keiskeiframework.cpreading.book.entity.BookExpand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * 图书拓展 DAO 层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
public interface BookExpandRepository extends JpaRepository<BookExpand, Long>, JpaSpecificationExecutor<BookExpand> {

}
