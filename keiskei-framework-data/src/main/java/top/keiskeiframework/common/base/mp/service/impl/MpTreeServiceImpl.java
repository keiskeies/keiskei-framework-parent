package top.keiskeiframework.common.base.mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.annotation.annotation.Lockable;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.PageResultVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.ITreeEntity;
import top.keiskeiframework.common.base.mp.util.MpRequestUtils;
import top.keiskeiframework.common.base.service.ITreeBaseService;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.TreeEntityUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 基础查询接口
 *
 * @param <T>  .
 * @param <ID> .
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class MpTreeServiceImpl<T extends ITreeEntity<ID>, ID extends Serializable, M extends BaseMapper<T>>
        extends AbstractMpServiceImpl<T, ID, M>
        implements ITreeBaseService<T, ID>, IService<T> {
    @Autowired
    protected MpTreeServiceImpl<T, ID, M> treeService;

    @Override
    public PageResultVO<T> page(BaseRequestVO<T, ID> request, BasePageVO page) {
        PageResultVO<T> result = super.page(request, page);
        if (request.getComplete()) {
            for (T record : result.getData()) {
                getManyToMany(record);
                getOneToMany(record);
                getManyToOne(record);
                getOneToOne(record);
            }
        }
        if (request.getTree()) {
            List<T> tTree = new TreeEntityUtils<>(result.getData()).getTreeAll();
            result.setData(tTree);
        }
        return result;
    }

    @Override
    public List<T> findListByCondition(BaseRequestVO<T, ID> request) {
        List<T> tList;
        if (request.conditionEmpty() && request.showEmpty()) {
            tList = treeService.findList();
        } else {
            tList = super.findListByCondition(request);
        }
        if (request.getComplete()) {
            for (T t : tList) {
                getManyToMany(t);
                getOneToMany(t);
                getManyToOne(t);
                getOneToOne(t);
            }
        }
        if (request.getTree()) {
            return new TreeEntityUtils<>(tList).getTreeAll();
        }
        return tList;
    }

    @Override
    @Cacheable(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    public T findOneById(Serializable id) {
        T t = super.findOneById(id);
        getManyToMany(t);
        getOneToMany(t);
        getManyToOne(t);
        getOneToOne(t);
        return t;
    }

    @Override
    @Cacheable(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    public List<T> findList() {
        return super.findList();
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    @Lockable(key = "#t.hashCode()")
    public boolean save(T t) {
        if (null != t.getParentId()) {
            T parent = treeService.findOneById(t.getParentId());
            if (null == parent) {
                throw new BizException(BizExceptionEnum.ERROR);
            }
            saveManyToOne(t);
            super.save(t);
            t.setSign(parent.getSign() + StringPool.SLASH + t.getId());
        } else {
            saveManyToOne(t);
            super.save(t);
            t.setSign(StringPool.SLASH + t.getId());
        }
        saveManyToMany(t);
        saveOneToMany(t);
        saveOneToOne(t);
        super.updateOne(t);
        return true;
    }


    @Override
    @Cacheable(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id", unless = "#result == null")
    public T getById(Serializable id) {
        return super.getById(id);
    }


    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")
    })
    public boolean updateById(T t) {
        if (null != t.getParentId()) {
            T parent = treeService.getById(t.getParentId());
            if (null == parent) {
                throw new BizException(BizExceptionEnum.ERROR);
            }
            t.setSign(parent.getSign() + StringPool.SLASH + t.getId());
        } else {
            t.setSign(StringPool.SLASH + t.getId());
        }
        updateManyToOne(t);
        super.updateById(t);
        updateOneToMany(t);
        updateOneToOne(t);
        return true;
    }


    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean deleteListByColumn(SFunction<T, Serializable> column, Serializable value) {
        List<T> ts = treeService.findListByColumn(column, value);
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        List<T> tAll = treeService.list();
        for (T t : ts) {
            Set<ID> childIds = new TreeEntityUtils<>(tAll).getChildIds(t.getId());
            for (ID cid : childIds) {
                treeService.removeByIdSingle(cid);
            }
        }
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean deleteListByCondition(List<QueryConditionVO> conditions) {
        QueryWrapper<T> queryWrapper = MpRequestUtils.getQueryWrapperByConditions(conditions, getEntityClass());
        List<T> ts = treeService.list(queryWrapper);
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        List<T> tAll = treeService.list();
        for (T t : ts) {
            Set<ID> childIds = new TreeEntityUtils<>(tAll).getChildIds(t.getId());
            for (ID cid : childIds) {
                treeService.removeByIdSingle(cid);
            }
        }
        return true;
    }


    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    })
    public boolean removeById(Serializable id) {
        Set<ID> childIds = new TreeEntityUtils<>(treeService.list()).getChildIds((ID) id);
        for (Serializable cid : childIds) {
            treeService.removeByIdSingle(cid);
        }
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    public boolean removeByIdSingle(Serializable id) {
        T t = treeService.getById(id);
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
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    public boolean saveBatch(Collection<T> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean saveOrUpdateBatch(Collection<T> ts) {
        return super.saveOrUpdateBatch(ts);
    }

    @Override
    public T saveOne(T t) {
        treeService.save(t);
        return t;
    }


    @Override
    public T updateOne(T t) {
        treeService.updateById(t);
        return t;
    }

    @Override
    public List<T> saveList(List<T> ts) {
        treeService.saveBatch(ts);
        return ts;
    }

    @Override
    public List<T> updateList(List<T> ts) {
        treeService.updateBatchById(ts);
        return ts;
    }

    @Override
    public boolean deleteOneById(ID id) {
        return treeService.removeById(id);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + '*'")
    public boolean deleteListByIds(Collection<ID> ids) {
        return treeService.removeByIds(ids);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + '*'")
    public boolean updateListByCondition(List<QueryConditionVO> conditions, T t) {
        return super.updateListByCondition(conditions, t);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")
    })
    public boolean removeById(T t) {
        Set<ID> childIds = new TreeEntityUtils<>(treeService.list()).getChildIds(t.getId());
        for (Serializable cid : childIds) {
            treeService.removeByIdSingle(cid);
        }
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean remove(Wrapper<T> queryWrapper) {
        return super.remove(queryWrapper);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean saveOrUpdate(T entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        return super.saveOrUpdateBatch(entityList, batchSize);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        return super.updateBatchById(entityList, batchSize);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean removeByIds(Collection<?> list) {
        return super.removeByIds(list);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    })
    public boolean removeById(Serializable id, boolean useFill) {
        return super.removeById(id, useFill);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean removeBatchByIds(Collection<?> list, int batchSize) {
        return super.removeBatchByIds(list, batchSize);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean removeBatchByIds(Collection<?> list, int batchSize, boolean useFill) {
        return super.removeBatchByIds(list, batchSize, useFill);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean removeByIds(Collection<?> list, boolean useFill) {
        return super.removeByIds(list, useFill);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean removeBatchByIds(Collection<?> list, boolean useFill) {
        return super.removeBatchByIds(list, useFill);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean update(Wrapper<T> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean updateBatchById(Collection<T> ts) {
        return super.updateBatchById(ts);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean saveOrUpdate(T entity, Wrapper<T> updateWrapper) {
        return super.saveOrUpdate(entity, updateWrapper);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name + '*'")
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        return super.saveBatch(entityList, batchSize);
    }
}
