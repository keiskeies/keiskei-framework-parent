package top.keiskeiframework.cpreading.book.repository;

import top.keiskeiframework.cpreading.book.entity.BookSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * 图书章节 DAO 层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
public interface BookSectionRepository extends JpaRepository<BookSection, Long>, JpaSpecificationExecutor<BookSection> {

}
