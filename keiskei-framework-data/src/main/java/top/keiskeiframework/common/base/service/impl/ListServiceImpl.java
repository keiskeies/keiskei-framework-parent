package top.keiskeiframework.common.base.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import top.keiskeiframework.cache.annotation.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.enums.OperateNotifyType;
import top.keiskeiframework.common.util.BeanUtils;

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
public class ListServiceImpl<T extends ListEntity> extends AbstractBaseServiceImpl<T> implements BaseService<T> {


    protected final static String CACHE_NAME = "CACHE:BASE";
    @Autowired
    protected ListServiceImpl<T> baseService;

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #id", unless = "#result == null")
    public T findById(String id) {
        return super.findById(id);
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
        T tOld = baseService.findById(t.getId());
        BeanUtils.copyPropertiesIgnoreJson(t, tOld);
        return mongoRepository.save(tOld);
    }

    @Override
    @CachePut(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #t.id")
    public T update(T t) {
        T tOld = baseService.findById(t.getId());
        BeanUtils.copyPropertiesIgnoreJson(t, tOld);
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
