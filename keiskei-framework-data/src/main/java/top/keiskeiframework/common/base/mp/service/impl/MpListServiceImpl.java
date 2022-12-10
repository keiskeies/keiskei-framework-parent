package top.keiskeiframework.common.base.mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import top.keiskeiframework.common.annotation.annotation.Lockable;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.PageResultVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.IListEntity;
import top.keiskeiframework.common.base.service.IListBaseService;
import top.keiskeiframework.common.util.BeanUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
public class MpListServiceImpl
        <T extends IListEntity<ID>, ID extends Serializable, M extends BaseMapper<T>>
        extends AbstractMpServiceImpl<T, ID, M>
        implements IListBaseService<T, ID>, IService<T> {

    @Autowired
    protected MpListServiceImpl<T, ID, M> listService;


    @Override
    public PageResultVO<T> page(BaseRequestVO request, BasePageVO page) {
        PageResultVO<T> iPage = super.page(request, page);
        for (T t : iPage.getData()) {
            getManyToOne(t);
            getOneToOne(t);
            if (request.getComplete()) {
                getManyToMany(t);
                getOneToMany(t);
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
    public List<T> findListByColumn(SFunction<T, Serializable> column, Serializable value) {
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
    public List<T> findListByCondition(BaseRequestVO request) {
        List<T> ts = super.findListByCondition(request);
        for (T t : ts) {
            getManyToOne(t);
            getOneToOne(t);
            if (request.getComplete()) {
                getManyToMany(t);
                getOneToMany(t);
            }
        }
        return ts;
    }


    @Override
    @Cacheable(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id", unless = "#result == null")
    public T getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public T saveOne(T t) {
        listService.save(t);
        return t;
    }

    @Override
    @Lockable(key = "targetClass.name + ':' + #t.hashCode()", autoUnlock = false)
    public boolean save(T t) {
        saveManyToOne(t);
        super.baseMapper.insert(t);
        saveManyToMany(t);
        listService.cleanCache(t);
        saveOneToMany(t);
        saveOneToOne(t);
        return true;
    }

    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")
    public void cleanCache(T t) {

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
        return baseMapper.delete(queryWrapper) > 0;
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
            return baseMapper.deleteById(t) >= 0;
        }
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean deleteListByColumn(SFunction<T, Serializable> column, Serializable value) {
        return super.deleteListByColumn(column, value);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':*'")
    public boolean deleteListByCondition(List<QueryConditionVO> conditions) {
        return super.deleteListByCondition(conditions);
    }


}
