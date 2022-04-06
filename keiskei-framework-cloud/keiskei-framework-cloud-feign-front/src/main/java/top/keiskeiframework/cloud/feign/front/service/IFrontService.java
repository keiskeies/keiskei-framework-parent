package top.keiskeiframework.cloud.feign.front.service;

import top.keiskeiframework.cloud.feign.dto.ListEntityDTO;
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
     * @param conditions 查询条件
     * @return 。
     */
    Map<String, Double> statistic(
            String column,
            String timeField,
            ColumnType columnType,
            TimeDeltaEnum timeDelta,
            String start,
            String end,
            CalcType calcType,
            String conditions
    );


}
