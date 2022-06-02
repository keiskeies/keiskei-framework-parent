package top.keiskeiframework.common.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.IPageResult;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/16 15:04
 */
public class AbstractServiceImpl<T extends IBaseEntity<String>> implements IBaseService<T, String> {
    @Autowired
    protected MongoRepository<T, String> mongoRepository;
    @Autowired
    protected MongoTemplate mongoTemplate;
    private Class<T> tClass;

    public AbstractServiceImpl() {
        ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
        Type[] types = parameterizedType.getActualTypeArguments();
        this.tClass = (Class<T>) types[0];
    }


    @Override
    public IPageResult<T> page(BaseRequestVO<T, String> request, BasePageVO page) {
        return null;
    }

    @Override
    public T findOneById(Serializable id) {
        return null;
    }

    @Override
    public T findOneByColumn(String column, Serializable value) {
        return null;
    }

    @Override
    public T findOneByCondition(BaseRequestVO<T, String> request) {
        return null;
    }

    @Override
    public T saveOne(T t) {
        return null;
    }

    @Override
    public T updateOne(T t) {
        return null;
    }

    @Override
    public List<T> findList() {
        return null;
    }

    @Override
    public List<T> findListByColumn(String column, Serializable value) {
        return null;
    }

    @Override
    public List<T> findListByCondition(BaseRequestVO<T, String> request) {
        return null;
    }

    @Override
    public List<T> saveList(List<T> ts) {
        return null;
    }

    @Override
    public List<T> updateList(List<T> ts) {
        return null;
    }

    @Override
    public boolean updateListByCondition(List<QueryConditionVO> conditions, T t) {
        return false;
    }

    @Override
    public boolean deleteOneById(String s) {
        return false;
    }

    @Override
    public boolean deleteListByIds(Collection<String> strings) {
        return false;
    }

    @Override
    public boolean deleteListByColumn(String column, Serializable value) {
        return false;
    }

    @Override
    public boolean deleteListByCondition(List<QueryConditionVO> conditions) {
        return false;
    }

    @Override
    public Long getCount(BaseRequestVO<T, String> request) {
        return null;
    }

    @Override
    public Boolean exist(BaseRequestVO<T, String> request) {
        return null;
    }

    @Override
    public Map<String, Double> getChartOptions(ChartRequestDTO chartRequestDTO) {
        return null;
    }
}
