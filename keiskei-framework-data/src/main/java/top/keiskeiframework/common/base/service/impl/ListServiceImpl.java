package top.keiskeiframework.common.base.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.query.Query;
import top.keiskeiframework.cache.annotation.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.enums.OperateNotifyType;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.BaseRequestUtils;

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


    protected final static String CACHE_NAME = "CACHE:BASE";

    @Override
    public List<T> options() {
        try {
            return this.options(getTClass().newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    @Override
    public List<T> options(@NonNull T t) {
        Class<T> tClass = (Class<T>) t.getClass();
        Query query = BaseRequestUtils.getQuery(null, t.getClass());

        return mongoTemplate.find(query, tClass);
    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #id", unless = "#result == null")
    public T getById(String id) {
        return super.getById(id);
    }


    @Override
    @OperateNotify(type = OperateNotifyType.SAVE)
    @Lockable(key = "#t.hashCode()")
    public T saveAndNotify(T t) {
        return super.save(t);
    }

    @Override
    @Lockable(key = "#t.hashCode()")
    public T save(T t) {
        return super.save(t);
    }

    @Override
    @OperateNotify(type = OperateNotifyType.UPDATE)
    @CachePut(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #t.id")
    public T updateAndNotify(T t) {
        return mongoRepository.save(t);
    }

    @Override
    @CachePut(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #t.id")
    public T update(T t) {
        return mongoRepository.save(t);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #id")
    @OperateNotify(type = OperateNotifyType.DELETE)
    public void deleteByIdAndNotify(String id) {
        super.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #id")
    public void deleteById(String id) {
        super.deleteById(id);
    }


}
