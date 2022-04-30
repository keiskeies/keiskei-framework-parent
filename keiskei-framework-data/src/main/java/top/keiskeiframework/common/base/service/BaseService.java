package top.keiskeiframework.common.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基础服务接口
 * </p>
 *
 * @param <T> .
 * @param <ID> .
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
public interface BaseService<T extends ListEntity<ID>, ID extends Serializable> extends IService<T> {

    /**
     * 列表查询
     *
     * @param request 列表条件
     * @param page    列表条件
     * @return .
     */
    IPage<T> page(BaseRequestVO<T, ID> request, BasePageVO page);

    /**
     * 条件查询单个
     * @param request 查询条件
     * @return 。
     */
    T getOne(BaseRequestVO<T, ID> request);

    /**
     * 查询全部
     *
     * @param request 查询条件
     * @return 。
     */
    List<T> list(BaseRequestVO<T, ID> request);


    /**
     * 通过单一字段查询
     *
     * @param column 字段名称
     * @param value  字段值
     * @return 。
     */
    List<T> listByColumn(String column, Serializable value);


    /**
     * 查询数量
     *
     * @param request request
     * @return 。
     */
    Long count(BaseRequestVO<T, ID> request);


    /**
     * 判断是否存在
     * @param request request
     * @return .
     */
    Boolean exist(BaseRequestVO<T, ID> request);

    /**
     * 通过单一字段查询
     *
     * @param column 字段名称
     * @param value  字段值
     * @return 。
     */
    T findByColumn(String column, Serializable value);


    /**
     * 删除并通知
     *
     */
    boolean removeByColumn(String column, Serializable value);
    boolean removeByCondition(List<QueryConditionVO> conditions);

    /**
     * 数据图表
     *
     * @param chartRequestDTO 图表条件
     * @return 。
     */
    Map<String, Double> getChartOptions(ChartRequestDTO chartRequestDTO);

}
