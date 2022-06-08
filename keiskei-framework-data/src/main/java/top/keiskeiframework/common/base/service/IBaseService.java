package top.keiskeiframework.common.base.service;

import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.IPageResult;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
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
 * @param <T>  .
 * @param <ID> .
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
public interface IBaseService<T extends IBaseEntity<ID>, ID extends Serializable> {

    String TIME_FIELD_DEFAULT = "createTime";
    String CACHE_TREE_NAME = "CACHE:TREE";
    String CACHE_LIST_NAME = "CACHE:LIST";
    String CACHE_MIDDLE_NAME = "CACHE:MIDDLE";

    IPageResult<T> page(BaseRequestVO<T, ID> request, BasePageVO page);

    T findOneById(Serializable id);
    T findOneByColumn(String column, Serializable value);
    T findOneByCondition(BaseRequestVO<T, ID> request);

    T saveOne(T t);
    T updateOne(T t);


    List<T> findList();
    List<T> findListByColumn(String column, Serializable value);
    List<T> findListByCondition(BaseRequestVO<T, ID> request);

    List<T> saveList(List<T> ts);
    List<T> updateList(List<T> ts);
    boolean updateListByCondition(List<QueryConditionVO> conditions, T t);




    boolean deleteOneById(ID id);
    boolean deleteListByIds(Collection<ID> ids);
    boolean deleteListByColumn(String column, Serializable value);
    boolean deleteListByCondition(List<QueryConditionVO> conditions);

    Long getCount(BaseRequestVO<T, ID> request);
    Boolean exist(BaseRequestVO<T, ID> request);
    Map<String, Double> getChartOptions(ChartRequestDTO chartRequestDTO);

}
