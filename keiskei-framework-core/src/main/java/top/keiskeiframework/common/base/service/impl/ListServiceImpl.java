package top.keiskeiframework.common.base.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.annotation.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.dto.base.QueryConditionDTO;
import top.keiskeiframework.common.enums.OperateNotifyType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 树形实体类基础服务实现
 * </p>
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class ListServiceImpl<T extends BaseEntity> extends AbstractBaseServiceImpl<T> implements BaseService<T> {


    protected final static String CACHE_NAME = "SPRING_BASE_CACHE";
    @Autowired
    private ListServiceImpl<T> baseService;

    @Override
    public List<T> options() {
        return this.options(new ArrayList<>(0));
    }

    @Override
    public List<T> options(List<QueryConditionDTO> queryConditions) {
        ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
        Type[] types = parameterizedType.getActualTypeArguments();
        Class<T> clazz = (Class<T>) types[0];
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);

        query.select(root.get("id"));

        if (CollectionUtils.isEmpty(queryConditions)) {
            for (QueryConditionDTO queryCondition : queryConditions) {
                query.where(builder.and(builder.equal(root.get(queryCondition.getColumn()), queryCondition.getValue())));
            }
        }

        List<?> ids = entityManager.createQuery(query).getResultList();
        List<T> result = new ArrayList<>(ids.size());

        for (Object id : ids) {
            result.add(baseService.getById((Long) id));
        }
        return result;
    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name + '-' + #id", unless = "#result == null")
    public T getById(Long id) {
        return super.getById(id);
    }


    @Override
    @OperateNotify(type = OperateNotifyType.SAVE)
    @Lockable(key = "#t.toString()")
    public T saveAndNotify(T t) {
        return super.save(t);
    }

    @Override
    @Lockable(key = "#t.toString()")
    public T save(T t) {
        return super.save(t);
    }

    @Override
    @OperateNotify(type = OperateNotifyType.UPDATE)
    public T updateAndNotify(T t) {
        return baseService.update(t, t.getId());
    }

    @Override
    public T update(T t) {
        return baseService.update(t, t.getId());
    }

    @CachePut(cacheNames = CACHE_NAME, key = "targetClass.name + '-' + #id")
    public T update(T t, Long id) {
        return super.update(t);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + '-' + #id")
    @OperateNotify(type = OperateNotifyType.DELETE)
    public void deleteByIdAndNotify(Long id) {
        super.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + '-' + #id")
    public void deleteById(Long id) {
        super.deleteById(id);
    }


}
