package top.keiskeiframework.common.base.service;

import top.keiskeiframework.common.base.entity.IListEntity;

import java.io.Serializable;

/**
 * <p>
 * 树状数据service
 * </p>
 *
 * @param <T>  .
 * @param <ID> .
 * @author v_chenjiamin
 * @since 2022/4/15 15:36
 */
public interface ITreeBaseService<T extends IListEntity<ID>, ID extends Serializable> extends IBaseService<T, ID> {

    /**
     * 删除单独ID，不遍历树形结构
     *
     * @param id ID
     * @return 。
     */
    boolean removeByIdSingle(ID id);

}
