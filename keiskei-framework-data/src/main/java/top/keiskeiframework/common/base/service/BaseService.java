package top.keiskeiframework.common.base.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import top.keiskeiframework.common.base.BaseRequest;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.dto.base.BaseSortDTO;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;

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
public interface BaseService<T extends BaseEntity<ID>, ID extends Serializable> {

    /**
     * 列表查询
     *
     * @param request 列表条件
     * @return .
     */
    Page<T> page(BaseRequest<T, ID> request);

    Page<T> page(Specification<T> s, Pageable p);

    Page<T> page(Example<T> s, Pageable p);

    /**
     * 查询全部
     *
     * @param s 查询条件
     * @return 。
     */
    List<T> findAll(Specification<T> s);

    List<T> findAll(Specification<T> s, Sort sort);

    List<T> findAll(Example<T> e);

    List<T> findAll(Example<T> e, Sort sort);

    /**
     * 下拉框列表
     *
     * @return .
     */
    List<T> options();

    /**
     * 下拉框列表
     *
     * @param t 查询条件
     * @return .
     */
    List<T> options(T t);

    /**
     * 根据id查询
     *
     * @param id id
     * @return .
     */
    T findById(ID id);

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
     * 更新并通知
     *
     * @param t 。
     * @return 。
     */
    T updateAndNotify(T t);

    /**
     * 更改排序
     *
     * @param baseSortDto .
     */
    void changeSort(BaseSortDTO<ID> baseSortDto);

    /**
     * 删除
     *
     * @param id id
     */
    void deleteById(ID id);

    /**
     * 删除并通知
     *
     * @param id 。
     */
    void deleteByIdAndNotify(ID id);


    /**
     * 数据图表
     *
     * @param chartRequestDTO 图表条件
     * @return 。
     */
    Map<String, Long> getChartOptions(ChartRequestDTO chartRequestDTO);

}
