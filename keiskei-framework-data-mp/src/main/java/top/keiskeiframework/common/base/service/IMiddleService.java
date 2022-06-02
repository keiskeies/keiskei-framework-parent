package top.keiskeiframework.common.base.service;

import top.keiskeiframework.common.base.entity.IMiddleEntity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/12 19:56
 */
public interface IMiddleService
        <T extends IMiddleEntity<ID1, ID2>, ID1 extends Serializable, ID2 extends Serializable>
        extends IBaseService<T, String> {

    /**
     * 通过ID1 批量查询
     *
     * @param id1 ID1
     * @return 。
     */
    List<T> getById1(Serializable id1);

    /**
     * 通过ID2 批量查询
     *
     * @param id2 ID2
     * @return 。
     */
    List<T> getById2(Serializable id2);

    /**
     * 批量更新ID1
     *
     * @param ts ts
     * @return .
     */
    List<T> saveOrUpdateById1(List<T> ts);

    /**
     * 批量更新ID2
     *
     * @param ts ts
     * @return .
     */
    List<T> saveOrUpdateById2(List<T> ts);

    /**
     * 通过ID1删除
     *
     * @param id1 ID1
     * @return 。
     */
    Boolean removeById1(ID1 id1);

    /**
     * 通过ID2删除
     *
     * @param id2 ID2
     * @return 。
     */
    Boolean removeById2(ID2 id2);

}
