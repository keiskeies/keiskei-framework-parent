package top.keiskeiframework.common.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.entity.ListEntity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/4/15 15:36
 */
public interface ListBaseService<T extends ListEntity> extends BaseService<T> {

    /**
     * 完整数据列表查询
     *
     * @param request 列表条件
     * @param page    列表条件
     * @return .
     */
    IPage<T> pageComplete(BaseRequestVO<T> request, BasePageVO<T> page);


    /**
     * 完整数据列表查询
     *
     * @param request 查询条件
     * @return 。
     */
    List<T> findAllComplete(BaseRequestVO<T> request);

    /**
     * 通过缓存获取，不拼装链接表数据
     *
     * @param id ID
     * @return 。
     */
    T getByIdCache(Serializable id);


    /**
     * 查询针对某个字段的缓存
     *
     * @param column 字段
     * @param value  值
     * @return 。
     */
    List<T> findAllByColumnCache(String column, Serializable value);

    /**
     * 删除针对某个字段的缓存
     *
     * @param column 字段
     * @param value  值
     */
    void deleteAllByColumnCache(String column, Serializable value);
}
