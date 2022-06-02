package top.keiskeiframework.common.base.dto;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 22:18
 */

public interface IPageResult<T> {
    /**
     * 数据
     *
     * @return 。
     */
    List<T> getData();

    /**
     * 总数
     *
     * @return .
     */
    long getTotal();

    /**
     * 分页大小
     *
     * @return .
     */
    long getSize();

    /**
     * 页码
     *
     * @return .
     */
    long getPage();

    /**
     * offset
     *
     * @return .
     */
    long getOffset();
}
