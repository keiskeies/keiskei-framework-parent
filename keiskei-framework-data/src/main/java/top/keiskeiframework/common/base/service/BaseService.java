package top.keiskeiframework.common.base.service;

import org.springframework.data.domain.Page;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.BaseSortVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;

import java.io.Serializable;
import java.util.Collection;
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
public interface BaseService<T extends ListEntity<ID>, ID extends Serializable> {

    /**
     * 列表查询
     *
     * @param request 列表条件
     * @return .
     */
    Page<T> page(BaseRequestVO<T, ID> request, BasePageVO<T, ID> page);

    /**
     * 查询全部
     *
     * @param request 查询条件
     * @param page    只使用排序
     * @return 。
     */
    List<T> findAll(BaseRequestVO<T, ID> request, BasePageVO<T, ID> page);

    List<T> findAll(BaseRequestVO<T, ID> request);

    /**
     * 下拉框列表
     *
     * @return .
     */
    List<T> findAll();

    /**
     * 下拉框列表
     *
     * @param t 查询条件
     * @return .
     */
    List<T> findAll(T t);

    /**
     * 通过单一字段查询
     *
     * @param column 字段名称
     * @param value  字段值
     * @return 。
     */
    List<T> findAllByColumn(String column, Serializable value);

    /**
     * 查询数量
     *
     * @param request request
     * @return 。
     */
    Long count(BaseRequestVO<T, ID> request);

    /**
     * 根据id查询
     *
     * @param id id
     * @return .
     */
    T findById(ID id);

    /**
     * 通过单一字段查询
     *
     * @param column 字段名称
     * @param value  字段值
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
     * 新增并通知
     *
     * @param t 。
     * @return 。
     */
    T saveAndNotify(T t);

    /**
     * 批量新增
     *
     * @param tList .
     * @return .
     */
    List<T> saveAll(List<T> tList);

    /**
     * 更新
     *
     * @param t .
     * @return .
     */
    T update(T t);

    /**
     * 更新多个
     *
     * @param ts ts
     * @return dataList
     */
    List<T> updateAll(List<T> ts);

    /**
     * 更新并通知
     *
     * @param t 。
     * @return 。
     */
    T updateAndNotify(T t);

    /**
     * 更改排序
     *
     * @param baseSortVO .
     */
    void changeSort(BaseSortVO<ID> baseSortVO);

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
     */
    void deleteByIds(Collection<ID> ids);

    /**
     * 删除并通知
     *
     * @param id 。
     */
    void deleteByIdAndNotify(ID id);

    /**
     * 校验数据
     *
     * @param t .
     */
    void validate(T t);


    /**
     * 数据图表
     *
     * @param chartRequestDTO 图表条件
     * @return 。
     */
    Map<String, Double> getChartOptions(ChartRequestDTO chartRequestDTO);

}
