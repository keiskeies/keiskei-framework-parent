package top.keiskeiframework.cloud.feign.front.service;

import top.keiskeiframework.cloud.feign.dto.ListEntityDTO;
import top.keiskeiframework.cloud.feign.dto.PageResultDTO;
import top.keiskeiframework.cloud.feign.dto.TreeEntityDTO;

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
public interface ITreeFrontService<T extends TreeEntityDTO<T, ID>, ID extends Serializable> extends IFrontService<T,
        ID> {


    /**
     * 分页查询
     *
     * @param conditions 查询条件
     * @param show       显示字段
     * @param page       页码
     * @param size       size
     * @param desc       倒序字段
     * @param asc        正序字段
     * @param tree       是否树状结构
     * @return 。
     */
    PageResultDTO<T> page(
            String conditions,
            String show,
            Integer page,
            Integer size,
            String desc,
            String asc,
            Boolean tree
    );

    /**
     * 下拉框
     *
     * @param conditions 查询条件
     * @param show       显示字段
     * @param page       页码
     * @param size       size
     * @param desc       倒序字段
     * @param asc        正序字段
     * @param id         根节点ID
     * @param tree       是否树状结构
     * @return 。
     */
    List<T> options(
            String conditions,
            String show,
            Integer page,
            Integer size,
            String desc,
            String asc,
            ID id,
            Boolean tree
    );



}
