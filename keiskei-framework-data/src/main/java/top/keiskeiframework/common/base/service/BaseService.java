package top.keiskeiframework.common.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
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
 * @param <T>  实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
public interface BaseService<T extends ListEntity> extends IService<T> {

    /**
     * 列表查询
     *
     * @param request 列表条件
     * @param page    列表条件
     * @return .
     */
    IPage<T> page(BaseRequestVO<T> request, BasePageVO<T> page);

    /**
     * 查询全部
     *
     * @param request 查询条件
     * @return 。
     */
    List<T> findAll(BaseRequestVO<T> request);

    /**
     * 下拉框列表
     *
     * @return .
     */
    List<T> findAll();
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
    Long count(BaseRequestVO<T> request);


    /**
     * 通过单一字段查询
     *
     * @param column 字段名称
     * @param value  字段值
     * @return 。
     */
    T findByColumn(String column, Serializable value);


    /**
     * 新增并通知
     *
     * @param t 。
     * @return 。
     */
    T saveAndNotify(T t);
    T saveAny(Object ojb);


    /**
     * 保存并更新缓存
     * @param t T
     * @return 。
     */
    T saveCache(T t);


    /**
     * 更新并通知
     *
     * @param t 。
     * @return 。
     */
    T updateByIdAndNotify(T t);
    T updateByIdCache(T t);


    /**
     * 更改排序
     *
     * @param baseSortVO .
     */
    void changeSort(BaseSortVO baseSortVO);

    /**
     * 删除并通知
     *
     * @param id 。
     */
    boolean removeByIdAndNotify(Serializable id);


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
