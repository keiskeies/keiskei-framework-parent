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
import top.keiskeiframework.common.annotation.data.BatchCacheField;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.dto.BaseSortVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.ListBaseService;
import top.keiskeiframework.common.enums.log.OperateNotifyType;
import top.keiskeiframework.common.util.BeanUtils;

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
        implements ListBaseService<T>, IService<T> {

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
        Class<T> tClass = getEntityClass();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            BatchCacheField batchCacheField = field.getAnnotation(BatchCacheField.class);
            if (null != batchCacheField) {
                String cacheField = BeanUtils.humpToUnderline(field.getName());
                column = BeanUtils.humpToUnderline(column);
                if (cacheField.equals(column)) {
                    return listService.findAllByColumnCache(column, value);
                }
            }
        }
        return super.findAllByColumn(column, value);
    }


    @Override
    @Cacheable(cacheNames = CACHE_COLUMN_LIST, key = "targetClass.name + ':' + #column + ':' + #value",
            unless = "#result == null")
    public List<T> findAllByColumnCache(String column, Serializable value) {
        return super.findAllByColumn(column, value);
    }

    @Override
    public void deleteAllByColumn(String column, Serializable value) {
        Class<T> tClass = getEntityClass();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            BatchCacheField batchCacheField = field.getAnnotation(BatchCacheField.class);
            if (null != batchCacheField) {
                String cacheField = BeanUtils.humpToUnderline(field.getName());
                if (cacheField.equals(column)) {
                    listService.deleteAllByColumnCache(column, value);
                    return;
                }
            }
        }
        super.deleteAllByColumn(column, value);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_COLUMN_LIST, key = "targetClass.name + ':' + #column + ':' + #value")
    public void deleteAllByColumnCache(String column, Serializable value) {
        super.deleteAllByColumn(column, value);
    }

    @Override
    public boolean updateById(T t) {
        listService.updateByIdCache(t);
        return true;
    }


    @Override
    @OperateNotify(type = OperateNotifyType.UPDATE)
    public T updateByIdAndNotify(T t) {
        return listService.updateByIdCache(t);
    }

    @Override
    @CachePut(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")
    public T updateByIdCache(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        T old = null;
        for (Field field : fields) {
            BatchCacheField batchCacheField = field.getAnnotation(BatchCacheField.class);
            if (null != batchCacheField) {
                try {
                    if (null == old) {
                        old = listService.getByIdCache(t.getId());
                    }
                    field.setAccessible(true);
                    Object oldValue = field.get(old);
                    Object newValue = field.get(t);
                    String cacheField = BeanUtils.humpToUnderline(field.getName());
                    cacheStorageService.del(CACHE_COLUMN_LIST + ":" + this.getClass().getName() + ":" + cacheField + ":" + oldValue);
                    cacheStorageService.del(CACHE_COLUMN_LIST + ":" + this.getClass().getName() + ":" + cacheField + ":" + newValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        super.updateById(t);
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
        if (null != t) {
            cleanColumnCache(t);
            return super.removeById(id);
        }
        return true;
    }


    protected void cleanColumnCache(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        T old = null;
        for (Field field : fields) {
            BatchCacheField batchCacheField = field.getAnnotation(BatchCacheField.class);
            if (null != batchCacheField) {
                try {
                    if (null == old) {
                        old = listService.getByIdCache(t.getId());
                    }
                    field.setAccessible(true);
                    Object newValue = field.get(t);
                    String cacheField = BeanUtils.humpToUnderline(field.getName());
                    cacheStorageService.del(CACHE_COLUMN_LIST + ":" + this.getClass().getName() + ":" + cacheField + ":" + newValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
