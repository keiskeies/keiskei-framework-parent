package top.keiskeiframework.common.base.service;

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
public interface ListBaseService<T extends ListEntity> extends BaseService<T>{


    /**
     * 通过缓存获取，不拼装链接表数据
     * @param id ID
     * @return 。
     */
    T getByIdCache(Serializable id);


    /**
     * 查询针对某个字段的缓存
     * @param column 字段
     * @param value 值
     * @return 。
     */
    List<T> findAllByColumnCache(String column, Serializable value);
}
