package top.keiskeiframework.cpreading.feeling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.keiskeiframework.cpreading.feeling.entity.BookFeelingLike;

/**
 * <p>
 * 图书感悟 DAO 层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
public interface BookFeelingLikeRepository extends JpaRepository<BookFeelingLike, Long>, JpaSpecificationExecutor<BookFeelingLike> {

}
