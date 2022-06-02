package top.keiskeiframework.common.base.service;

import top.keiskeiframework.common.base.entity.IListEntity;

import java.io.Serializable;

/**
 * <p>
 * 列表数据service
 * </p>
 *
 * @param <T>  .
 * @param <ID> .
 * @author v_chenjiamin
 * @since 2022/4/15 15:36
 */
public interface IListBaseService<T extends IListEntity<ID>, ID extends Serializable> extends IBaseService<T, ID> {


}
