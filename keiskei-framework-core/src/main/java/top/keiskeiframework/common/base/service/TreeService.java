package top.keiskeiframework.common.base.service;

import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.vo.BaseSortDto;

import java.util.List;

/**
 * 基础查询接口
 *
 * @param <T>
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
public interface TreeService<T extends TreeEntity> {


    /**
     * 下拉框列表
     *
     * @return .
     */
    List<T> options();

    /**
     * 根据id查询
     *
     * @param id id
     * @return .
     */
    T getById(Long id);

    /**
     * 新增
     *
     * @param t .
     * @return .
     */
    T save(T t);

    /**
     * 更新
     *
     * @param t .
     * @return .
     */
    T update(T t);


    /**
     * 更改排序
     *
     * @param baseSortDto .
     */
    void changeSort(BaseSortDto baseSortDto);

    /**
     * 删除
     *
     * @param id id
     */
    void deleteById(Long id);

}
