package top.keiskeiframework.cpreading.repository;

import top.keiskeiframework.cpreading.entity.ReaderBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * 读者书库 DAO 层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
public interface ReaderBookRepository extends JpaRepository<ReaderBook, Long>, JpaSpecificationExecutor<ReaderBook> {

}
