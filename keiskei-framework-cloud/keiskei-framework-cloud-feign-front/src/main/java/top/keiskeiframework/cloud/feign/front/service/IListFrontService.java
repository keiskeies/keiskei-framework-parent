package top.keiskeiframework.cloud.feign.front.service;

import top.keiskeiframework.cloud.feign.dto.ListEntityDTO;
import top.keiskeiframework.cloud.feign.dto.PageResultDTO;

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
public interface IListFrontService<T extends ListEntityDTO<ID>, ID extends Serializable> extends IFrontService<T, ID> {


    /**
     * 分页查询
     *
     * @param conditions 查询条件
     * @param show       显示字段
     * @param page       页码
     * @param size       size
     * @param desc       倒序字段
     * @param asc        正序字段
     * @param complete   完整数据
     * @return 。
     */
    PageResultDTO<T> page(
            String conditions,
            String show,
            Integer page,
            Integer size,
            String desc,
            String asc,
            Boolean complete
    );

    /**
     * 下拉框
     *
     * @param conditions 查询条件
     * @param show       显示字段
     * @param desc       倒序字段
     * @param asc        正序字段
     * @param complete   完整数据
     * @return 。
     */
    List<T> options(
            String conditions,
            String show,
            String desc,
            String asc,
            Boolean complete
    );

}
