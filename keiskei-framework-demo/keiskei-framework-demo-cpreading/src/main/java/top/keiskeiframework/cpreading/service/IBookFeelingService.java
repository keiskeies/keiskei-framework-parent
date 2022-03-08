package top.keiskeiframework.cpreading.service;

import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.cpreading.entity.BookFeeling;

/**
 * <p>
 * 图书感悟 业务接口层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
public interface IBookFeelingService extends BaseService<BookFeeling, Long> {
    /**
     * 点赞
     * @param id 。
     * @return 。
     */
    int addLike(Long id);

    /**
     * 取消赞
     * @param id 。
     * @return 。
     */
    int delLike(Long id);
}
