package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.annotation.annotation.Lockable;
import top.keiskeiframework.common.base.annotation.ManyToMany;
import top.keiskeiframework.common.base.annotation.ManyToOne;
import top.keiskeiframework.common.base.annotation.OneToMany;
import top.keiskeiframework.common.base.annotation.OneToOne;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.*;
import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.common.base.service.IListBaseService;
import top.keiskeiframework.common.base.service.IMiddleService;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.util.SpringUtils;
import top.keiskeiframework.common.vo.PageResult;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 树形实体类基础服务实现
 * </p>
 *
 * @param <T>  .
 * @param <ID> .
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class ListServiceImpl
        <T extends ListEntity<ID>, ID extends Serializable, M extends BaseMapper<T>>
        extends AbstractServiceImpl<T, ID, M>
        implements IListBaseService<T, ID>, IService<T> {

    @Autowired
    protected ListServiceImpl<T, ID, M> listService;


    @Override
    public PageResult<T> page(BaseRequestVO<T, ID> request, BasePageVO page) {
        PageResult<T> iPage = super.page(request, page);
        if (request.getComplete()) {
            for (T record : iPage.getRecords()) {
                getManyToMany(record);
                getOneToMany(record);
                getManyToOne(record);
                getOneToOne(record);
            }
        }
        return iPage;
    }

    @Override
    public T findOneById(Serializable id) {
        T t = listService.getById(id);
        getManyToMany(t);
        getOneToMany(t);
        getManyToOne(t);
        getOneToOne(t);
        return t;
    }


    @Override
    public List<T> findListByColumn(String column, Serializable value) {
        List<T> ts = super.findListByColumn(column, value);
        for (T t : ts) {
            getManyToMany(t);
            getOneToMany(t);
            getManyToOne(t);
            getOneToOne(t);
        }
        return ts;
    }

    @Override
    public List<T> findListByCondition(BaseRequestVO<T, ID> request) {
        List<T> ts = super.findListByCondition(request);
        if (request.getComplete()) {
            for (T t : ts) {
                getManyToMany(t);
                getOneToMany(t);
                getManyToOne(t);
                getOneToOne(t);
            }
        }
        return ts;
    }



    @Override
    @Cacheable(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id", unless = "#result == null")
    public T getById(Serializable id) {
        T t = super.getById(id);
        if (null == t) {
            try {
                t = getEntityClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    @Override
    @Lockable(key = "targetClass.name + ':' + #t.hashCode()")
    public boolean save(T t) {
        saveManyToOne(t);
        super.baseMapper.insert(t);
        saveManyToMany(t);
        saveOneToMany(t);
        saveOneToOne(t);
        return true;
    }


    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")
    public boolean updateById(T t) {
        T told = listService.getById(t.getId());
        BeanUtils.copyPropertiesIgnoreNull(t, told);
        updateManyToOne(told);
        super.updateById(told);
        updateManyToMany(told);
        updateOneToMany(told);
        updateOneToOne(told);
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean updateBatchById(Collection<T> entityList) {

        if (hasRelation()) {
            for (T t : entityList) {
                listService.updateById(t);
            }
            return true;
        }

        super.updateBatchById(entityList, DEFAULT_BATCH_SIZE);
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")
    public T updateOne(T t) {
        return super.updateOne(t);
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
    public boolean updateListByCondition(List<QueryConditionVO> conditions, T t) {
        return super.updateListByCondition(conditions, t);
    }

    @Override
    public boolean saveOrUpdate(T t) {
        if (null != t.getId()) {
            return listService.updateById(t);
        } else {
            return listService.save(t);
        }
    }

    @Override
    public boolean saveBatch(Collection<T> entityList) {
        if (hasRelation()) {
            for (T t : entityList) {
                listService.save(t);
            }
            return true;
        }
        return super.saveBatch(entityList, DEFAULT_BATCH_SIZE);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean saveOrUpdateBatch(Collection<T> ts) {
        return super.saveOrUpdateBatch(ts);
    }

    @Override
    public boolean removeById(T t) {
        return listService.removeById(t.getId());
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean remove(Wrapper<T> queryWrapper) {
        return super.remove(queryWrapper);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean removeByIds(Collection<?> list, boolean useFill) {
        return super.removeByIds(list, useFill);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean removeBatchByIds(Collection<?> list, boolean useFill) {
        return super.removeBatchByIds(list, useFill);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean update(Wrapper<T> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    public boolean removeById(Serializable id) {
        T t = listService.getById(id);
        if (null != t) {
            deleteManyToMany(t);
            deleteOneToOne(t);
            deleteOneToMany(t);
            deleteManyToOne(t);
            return super.removeById(id);
        }
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean deleteListByColumn(String column, Serializable value) {
        return super.deleteListByColumn(column, value);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean deleteListByCondition(List<QueryConditionVO> conditions) {
        return super.deleteListByCondition(conditions);
    }





}
