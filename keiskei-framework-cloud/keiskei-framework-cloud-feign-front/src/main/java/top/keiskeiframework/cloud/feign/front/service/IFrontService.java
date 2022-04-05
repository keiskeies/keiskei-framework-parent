package top.keiskeiframework.cloud.feign.front.service;

import top.keiskeiframework.cloud.feign.dto.BaseRequestDTO;
import top.keiskeiframework.cloud.feign.dto.ListEntityDTO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 基础服务接口
 * </p>
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
public interface IFrontService<T extends ListEntityDTO<ID>, ID extends Serializable> {



    /**
     * 根据id查询
     *
     * @param id id
     * @return .
     */
    T findById(ID id);


    /**
     * 指定字段查询
     *
     * @param column 字段
     * @param value  值
     * @return 。
     */
    T findByColumn(String column, Serializable value);


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
     * 删除
     *
     * @param id id
     */
    void deleteById(ID id);


}
