package top.keiskeiframework.common.base.service;

import top.keiskeiframework.common.base.entity.ListEntity;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * @param <T> .
 * @param <ID> .
 * @author v_chenjiamin
 * @since 2022/4/15 15:36
 */
public interface TreeBaseService<T extends ListEntity<ID>, ID extends Serializable> extends BaseService<T, ID>{

    /**
     * 删除单独ID，不遍历树形结构
     * @param id ID
     * @return 。
     */
    boolean removeByIdSingle(Serializable id);

}
