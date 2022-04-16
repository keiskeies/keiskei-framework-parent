package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import top.keiskeiframework.cache.service.CacheStorageService;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.dto.BaseSortVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.ListBaseService;
import top.keiskeiframework.common.enums.log.OperateNotifyType;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

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
public abstract class AbstractListBaseServiceImpl<T extends ListEntity>
        extends AbstractBaseServiceImpl<T>
        implements ListBaseService<T> , IService<T> {

    @Autowired
    private ListBaseService<T> listService;
    @Autowired
    private CacheStorageService cacheStorageService;
    protected final static String CACHE_COLUMN_LIST = "CACHE:COLUMN_LIST";


    @Override
    @Cacheable(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id", unless = "#result == null")
    public T getByIdCache(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean save(T t) {
        listService.saveCache(t);
        return true;
    }


    @Override
    @OperateNotify(type = OperateNotifyType.SAVE)
    public T saveAndNotify(T t) {
        return listService.saveCache(t);
    }


    @Override
    @Lockable(key = "targetClass.name + ':' + #t.hashCode()")
    public T saveCache(T t) {
        super.save(t);
        cleanColumnCache(t);
        return t;
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
        return listService.findAllByColumnCache(column, value);
    }


    @Override
    @Cacheable(cacheNames = CACHE_COLUMN_LIST, key = "targetClass.name + ':' + #column + ':' + #value",
            unless = "#result == null")
    public List<T> findAllByColumnCache(String column, Serializable value) {
        return super.findAllByColumn(column, value);
    }

    @Override
    public boolean updateById(T t) {
        if (StringUtils.isEmpty(t.getOneToMany())) {
            listService.updateByIdCache(t);
        } else {
            try {
                T old = listService.getByIdCache(t.getId());
                Field onToManyField = t.getClass().getDeclaredField(t.getOneToMany());
                onToManyField.setAccessible(true);
                Object oldValue = onToManyField.get(old);
                Object newValue = onToManyField.get(t);
                if (!Objects.equals(oldValue, newValue)) {
                    cacheStorageService.del(CACHE_COLUMN_LIST + ":" + this.getClass().getName() + ":" + t.getOneToMany() + ":" + oldValue);
                    cacheStorageService.del(CACHE_COLUMN_LIST + ":" + this.getClass().getName() + ":" + t.getOneToMany() + ":" + newValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            } finally {
                listService.updateByIdCache(t);
            }

        }
        return true;
    }


    @Override
    @OperateNotify(type = OperateNotifyType.UPDATE)
    public T updateByIdAndNotify(T t) {
        if (StringUtils.isEmpty(t.getOneToMany())) {
            super.updateById(t);
            return t;
        }
        return listService.updateByIdCache(t);
    }

    @Override
    @CachePut(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")
    public T updateByIdCache(T t) {
        if (StringUtils.isEmpty(t.getOneToMany())) {
            super.updateById(t);
        } else {
            try {
                T old = listService.getByIdCache(t.getId());
                Field onToManyField = t.getClass().getDeclaredField(t.getOneToMany());
                onToManyField.setAccessible(true);
                Object oldValue = onToManyField.get(old);
                Object newValue = onToManyField.get(t);
                if (!Objects.equals(oldValue, newValue)) {
                    cacheStorageService.del(CACHE_COLUMN_LIST + ":" + this.getClass().getName() + ":" + t.getOneToMany() + ":" + oldValue);
                    cacheStorageService.del(CACHE_COLUMN_LIST + ":" + this.getClass().getName() + ":" + t.getOneToMany() + ":" + newValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            } finally {
                super.updateById(t);
            }
        }
        return t;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #baseSortVO.id1"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #baseSortVO.id2")
    })
    public void changeSort(BaseSortVO baseSortVO) {
        super.changeSort(baseSortVO);
    }

    @Override
    @OperateNotify(type = OperateNotifyType.DELETE)
    public boolean removeByIdAndNotify(Serializable id) {
        return listService.removeById(id);
    }


    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    public boolean removeById(Serializable id) {
        T t = listService.getByIdCache(id);
        cleanColumnCache(t);
        return super.removeById(id);
    }


    protected void cleanColumnCache(T t) {
        if (!StringUtils.isEmpty(t.getOneToMany())) {
            try {
                Field onToManyField = t.getClass().getDeclaredField(t.getOneToMany());
                onToManyField.setAccessible(true);
                Object value = onToManyField.get(t);
                if (null != value) {
                    cacheStorageService.del(CACHE_COLUMN_LIST + ":" + this.getClass().getName() + ":" + t.getOneToMany() + ":" + value);
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }
    }




}
