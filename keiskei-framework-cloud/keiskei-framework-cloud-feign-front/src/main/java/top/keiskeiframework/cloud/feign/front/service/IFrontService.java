package top.keiskeiframework.cloud.feign.front.service;

import top.keiskeiframework.cloud.feign.dto.BaseEntityDTO;
import top.keiskeiframework.cloud.feign.dto.PageResultDTO;
import top.keiskeiframework.cloud.feign.enums.CalcType;
import top.keiskeiframework.cloud.feign.enums.ColumnType;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基础服务接口
 * </p>
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
public interface IFrontService<T extends BaseEntityDTO<ID>, ID extends Serializable> {

    /**
     * 分页查询
     *
     * @param conditions 查询条件
     * @param show       显示字段
     * @param offset     offset
     * @param page       页码
     * @param size       size
     * @param desc       倒序字段
     * @param asc        正序字段
     * @param complete   是否查询完整数据
     * @param tree       是否树状结构
     * @return 。
     */
    PageResultDTO<T> page(
            String conditions,
            String show,
            Long offset,
            Long page,
            Long size,
            String desc,
            String asc,
            Boolean complete,
            Boolean tree
    );

    /**
     * 下拉框
     *
     * @param conditions 查询条件
     * @param show       显示字段
     * @param desc       倒序字段
     * @param asc        正序字段
     * @param complete   是否查询完整数据
     * @param tree       是否树状结构
     * @return 。
     */
    List<T> options(
            String conditions,
            String show,
            String desc,
            String asc,
            Boolean complete,
            Boolean tree
    );


    /**
     * 根据id查询
     *
     * @param id id
     * @return .
     */
    T findById(ID id);


    /**
     * 条件查询单个
     *
     * @param conditions 查询条件
     * @return 。
     */
    T getOne(String conditions);


    /**
     * 数量查询
     *
     * @param conditions 查询条件
     * @return 。
     */
    Long count(String conditions);


    /**
     * 新增
     *
     * @param t .
     * @return .
     */
    T save(T t);


    /**
     * 保存多个
     *
     * @param ts ts
     * @return .
     */
    List<T> save(List<T> ts);


    /**
     * 更新
     *
     * @param t .
     * @return .
     */
    T update(T t);


    /**
     * 保存多个
     *
     * @param ts ts
     * @return .
     */
    List<T> update(List<T> ts);


    /**
     * 删除
     *
     * @param id id
     */
    void deleteById(ID id);


    /**
     * 删除多个
     *
     * @param ids ids
     * @return 。
     */
    Boolean delete(List<ID> ids);


    /**
     * 统计
     *
     * @param column     统计字段
     * @param timeField  时间字段
     * @param columnType 字段类型
     * @param timeDelta  时间间隔
     * @param start      起始时间
     * @param end        结束时间
     * @param calcType   计算方式
     * @param sumColumn  求和字段
     * @param conditions 查询条件
     * @return 。
     */
    Map<String, Double> statistic(
            String column,
            ColumnType columnType,
            String timeField,
            TimeDeltaEnum timeDelta,
            String start,
            String end,
            CalcType calcType,
            String sumColumn,
            String conditions
    );


}
