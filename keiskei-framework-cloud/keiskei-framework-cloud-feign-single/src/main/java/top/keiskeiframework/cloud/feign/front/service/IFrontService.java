package top.keiskeiframework.cloud.feign.front.service;

import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.PageResultVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
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
public interface IFrontService<T extends IBaseEntity<ID>, ID extends Serializable> {

    /**
     * 分页查询
     *
     * @param requestVO 查询条件
     * @param pageVO    分页条件
     * @return 。
     */
    PageResultVO<T> page(BaseRequestVO<T, ID> requestVO, BasePageVO pageVO);

    /**
     * 下拉框
     *
     * @param requestVO 查询条件
     * @return 。
     */
    List<T> options(BaseRequestVO<T, ID> requestVO);


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
