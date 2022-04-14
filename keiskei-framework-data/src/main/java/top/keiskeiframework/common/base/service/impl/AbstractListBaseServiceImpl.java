package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import top.keiskeiframework.cache.service.CacheStorageService;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.dto.BaseSortVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.enums.log.OperateNotifyType;

import java.io.Serializable;
import java.lang.reflect.Field;
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
public abstract class AbstractListBaseServiceImpl<T extends ListEntity> extends AbstractBaseServiceImpl<T> implements BaseService<T>, IService<T> {

    protected final static String CACHE_NAME = "CACHE:LIST";
    @Autowired
    @Lazy
    private BaseService<T> listService;
    @Autowired
    private CacheStorageService cacheStorageService;

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #id", unless = "#result == null")
    public T findByIdCache(Long id) {
        return super.findById(id);
    }

    @Override
    @OperateNotify(type = OperateNotifyType.SAVE)
    @Lockable(key = "targetClass.name + ':' + #t.hashCode()")
    public T saveAndNotify(T t) {
        return super.saveAndNotify(t);
    }

    @Override
    public List<T> findAllByColumn(String column, Serializable value) {
        T t;
        try {
            t = getEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return super.findAllByColumn(column, value);
        }
        if (StringUtils.isEmpty(t.getOneToMany()) || !t.getOneToMany().equals(column)) {
            return super.findAllByColumn(column, value);
        }
        return listService.findAllByColumnCache(t.getOneToMany(), column, value);
    }


    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #oneToMany + ':' + #value",
            unless = "#result == null")
    public List<T> findAllByColumnCache(String oneToMany, String column, Serializable value) {
        return super.findAllByColumn(column, value);
    }


    @Override
    @OperateNotify(type = OperateNotifyType.UPDATE)
    @Caching(
            put = {@CachePut(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #t.id")},
            evict = {@CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #t.oneToMany  + ':' + #t[#t.oneToMany]")}
    )
    public T updateAndNotify(T t) {
        return super.updateAndNotify(t);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #baseSortVO.id1"),
            @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #baseSortVO.id2")
    })
    public void changeSort(BaseSortVO baseSortVO) {
        super.changeSort(baseSortVO);
    }


    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #id")
    @OperateNotify(type = OperateNotifyType.DELETE)
    public void deleteByIdAndNotify(Long id) {
        T t = listService.findById(id);
        if (null != t) {
            if (StringUtils.isEmpty(t.getOneToMany())) {
                super.deleteByIdAndNotify(id);
            } else {
                try {
                    Field onToManyField = t.getClass().getDeclaredField(t.getOneToMany());
                    onToManyField.setAccessible(true);
                    long value = onToManyField.getLong(t);
                    cacheStorageService.del(CACHE_NAME + ":" + this.getClass().getName() + ":" + t.getOneToMany() + ":" + value);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    super.deleteByIdAndNotify(id);
                }
            }
        }
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #id")
    @OperateNotify(type = OperateNotifyType.DELETE)
    public void deleteByIdAndNotifySingle(Long id) {
        T t = listService.findById(id);
        if (null != t) {
            if (StringUtils.isEmpty(t.getOneToMany())) {
                super.deleteByIdAndNotify(id);
            } else {
                try {
                    Field onToManyField = t.getClass().getDeclaredField(t.getOneToMany());
                    onToManyField.setAccessible(true);
                    long value = onToManyField.getLong(t);
                    cacheStorageService.del(CACHE_NAME + ":" + this.getClass().getName() + ":" + t.getOneToMany() + ":" + value);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    super.deleteByIdAndNotify(id);
                }
            }
        }
    }


}
