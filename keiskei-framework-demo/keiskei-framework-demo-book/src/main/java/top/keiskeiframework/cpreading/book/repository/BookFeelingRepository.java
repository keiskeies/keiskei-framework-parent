package top.keiskeiframework.cpreading.book.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import top.keiskeiframework.cpreading.book.entity.BookFeeling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 * 图书感悟 DAO 层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
public interface BookFeelingRepository extends JpaRepository<BookFeeling, Long>, JpaSpecificationExecutor<BookFeeling> {

    /**
     * 赞
     * @param id 。
     * @return 。
     */
    @Query("update BookFeeling set likeNum = likeNum + 1 where id = ?1")
    @Modifying
    int addLikeNum(Long id);

    /**
     * 取消赞
     * @param id 。
     * @return 。
     */
    @Query("update BookFeeling set likeNum = likeNum - 1 where id = ?1")
    @Modifying
    int delLikeNum(Long id);

}
