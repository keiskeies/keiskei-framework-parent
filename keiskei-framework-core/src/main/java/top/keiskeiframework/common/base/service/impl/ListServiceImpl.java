package top.keiskeiframework.common.base.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.base.service.BaseService;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 基础查询接口
 *
 * @param <T>
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class ListServiceImpl<T extends BaseEntity> extends BaseServiceImpl<T> implements BaseService<T> {


    protected final static String CACHE_NAME = "SPRING_BASE_CACHE";
    @Autowired
    protected ListServiceImpl<T> baseService;

    @Override
    public List<T> options() {
        ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
        Type[] types = parameterizedType.getActualTypeArguments();
        Class<T> clazz = (Class<T>) types[0];
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);

        query.multiselect(root.get("id"));
        List<T> result = entityManager.createQuery(query).getResultList();
        for (int i = 0; i < result.size(); i++) {
            result.set(i, baseService.getById( result.get(i).getId()));
        }
        return result;
    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name + '-' + #id", unless = "#result == null")
    public T getById(Long id) {
        return super.getById(id);
    }

    @Override
    @CachePut(cacheNames = CACHE_NAME, key = "targetClass.name + '-' + #id", unless = "#result == null")
    public T update(T t, Long id) {
        return super.update(t);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + '-' + #id")
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }


}
