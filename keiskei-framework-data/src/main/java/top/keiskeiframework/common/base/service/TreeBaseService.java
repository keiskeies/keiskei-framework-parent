package top.keiskeiframework.common.base.service;

import top.keiskeiframework.common.base.entity.ListEntity;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/4/15 15:36
 */
public interface TreeBaseService<T extends ListEntity> extends BaseService<T>{

    /**
     * 删除单独ID，不遍历树形结构
     * @param id ID
     * @return 。
     */
    boolean deleteByIdSingle(Serializable id);

}
