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
 * @param <T> .
 * @param <ID> .
 * @author v_chenjiamin
 * @since 2022/4/15 15:36
 */
public interface ListBaseService<T extends ListEntity<ID>, ID extends Serializable> extends BaseService<T, ID> {

    /**
     * 完整数据列表查询
     *
     * @param request 列表条件
     * @param page    列表条件
     * @return .
     */
    IPage<T> pageComplete(BaseRequestVO<T, ID> request, BasePageVO page);


    /**
     * 完整数据列表查询
     *
     * @param request 查询条件
     * @return 。
     */
    List<T> listComplete(BaseRequestVO<T, ID> request);

    /**
     * 通过缓存获取，不拼装链接表数据
     *
     * @param id ID
     * @return 。
     */
    T getByIdCache(Serializable id);

    /**
     * 保存并更新缓存
     * @param t T
     * @return 。
     */
    T saveCache(T t);


    T updateByIdCache(T t);
    /**
     * 查询针对某个字段的缓存
     *
     * @param column 字段
     * @param value  值
     * @return 。
     */
    List<T> listByColumnCache(String column, Serializable value);

    boolean removeByColumn(String column, Serializable value);

    /**
     * 删除针对某个字段的缓存
     *
     * @param column 字段
     * @param value  值
     */
    void removeByColumnCache(String column, Serializable value);
}
