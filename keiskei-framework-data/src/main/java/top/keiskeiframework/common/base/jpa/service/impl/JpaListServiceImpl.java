package top.keiskeiframework.common.base.jpa.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.IListEntity;
import top.keiskeiframework.common.base.service.IListBaseService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

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
public class JpaListServiceImpl<T extends IListEntity<ID>, ID extends Serializable> extends AbstractJpaServiceImpl<T, ID> implements IListBaseService<T, ID> {


    @Override
    @Cacheable(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    public T findOneById(Serializable id) {
        return super.findOneById(id);
    }

    @Override
    @CachePut(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")
    public T saveOne(T t) {
        return super.saveOne(t);
    }

    @Override
    @CachePut(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")
    public T updateOne(T t) {
        return super.updateOne(t);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean updateListByCondition(List<QueryConditionVO> conditions, T t) {
        return super.updateListByCondition(conditions, t);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public List<T> updateList(List<T> ts) {
        return super.updateList(ts);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    public boolean deleteOneById(ID id) {
        return super.deleteOneById(id);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean deleteListByIds(Collection<ID> ids) {
        return super.deleteListByIds(ids);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean deleteListByColumn(Function<T, ?> column, Serializable value) {
        return super.deleteListByColumn(column, value);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean deleteListByCondition(List<QueryConditionVO> conditions) {
        return super.deleteListByCondition(conditions);
    }
}
