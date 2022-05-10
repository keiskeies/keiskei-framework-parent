package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.cache.service.CacheStorageService;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.base.service.TreeBaseService;
import top.keiskeiframework.common.base.util.BaseRequestUtils;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.enums.log.OperateNotifyType;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.TreeEntityUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基础查询接口
 *
 * @param <T>  .
 * @param <ID> .
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public abstract class AbstractTreeBaseServiceImpl<T extends TreeEntity<ID>, ID extends Serializable, M extends BaseMapper<T>>
        extends AbstractBaseServiceImpl<T, ID, M>
        implements TreeBaseService<T, ID>, BaseService<T, ID>, IService<T> {

    protected final static String SPILT = "/";
    @Autowired
    protected TreeBaseService<T, ID> treeBaseService;
    @Autowired
    protected CacheStorageService cacheStorageService;

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    @Lockable(key = "#t.hashCode()")
    public boolean save(T t) {

        if (null != t.getParentId()) {
            T parent = treeBaseService.getById(t.getParentId());
            if (null == parent) {
                throw new BizException(BizExceptionEnum.ERROR);
            }
            super.save(t);
            t.setSign(parent.getSign() + SPILT + t.getId());
        } else {
            super.save(t);
            t.setSign(SPILT + t.getId());
        }
        return super.updateById(t);
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
            T parent = treeBaseService.getById(t.getParentId());
            if (null == parent) {
                throw new BizException(BizExceptionEnum.ERROR);
            }
            t.setSign(parent.getSign() + SPILT + t.getId());
        } else {
            t.setSign(SPILT + t.getId());
        }
        return super.updateById(t);
    }


    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    public boolean removeByColumn(String column, Serializable value) {
        List<T> ts = treeBaseService.listByColumn(column, value);
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        List<T> tAll = treeBaseService.list();
        for (T t : ts) {
            Set<Serializable> childIds = new TreeEntityUtils<>(tAll).getChildIds(t.getId());
            for (Serializable cid : childIds) {
                treeBaseService.removeByIdSingle(cid);
            }
        }
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    public boolean removeByCondition(List<QueryConditionVO> conditions) {
        QueryWrapper<T> queryWrapper = BaseRequestUtils.getQueryWrapperByConditions(conditions, getEntityClass());
        List<T> ts = treeBaseService.list(queryWrapper);
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        List<T> tAll = treeBaseService.list();
        for (T t : ts) {
            Set<Serializable> childIds = new TreeEntityUtils<>(tAll).getChildIds(t.getId());
            for (Serializable cid : childIds) {
                treeBaseService.removeByIdSingle(cid);
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
        Set<Serializable> childIds = new TreeEntityUtils<>(treeBaseService.list()).getChildIds(id);
        for (Serializable cid : childIds) {
            treeBaseService.removeByIdSingle(cid);
        }
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    @OperateNotify(type = OperateNotifyType.DELETE)
    public boolean removeByIdSingle(Serializable id) {
        return super.removeById(id);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    public boolean saveBatch(Collection<T> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    public boolean saveOrUpdateBatch(Collection<T> ts) {
        List<String> cacheKeys = new ArrayList<>();
        for (T t : ts) {
            if (null != t.getId()) {
                cacheKeys.add(CACHE_LIST_NAME + "::" + this.getClass().getName() + ":" + t.getId());
            }
        }
        super.saveOrUpdateBatch(ts);
        cacheStorageService.del(cacheKeys);
        return true;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")
    })
    public boolean removeById(T t) {
        return super.removeById(t);
    }

    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        List<T> ts = super.listByMap(columnMap);
        for (T t : ts) {
            treeBaseService.removeById(t.getId());
        }
        return true;
    }

    @Override
    public boolean remove(Wrapper<T> queryWrapper) {
        List<T> ts = super.list(queryWrapper);
        for (T t : ts) {
            treeBaseService.removeById(t.getId());
        }
        return true;
    }

    @Override
    public boolean removeByIds(Collection<?> list, boolean useFill) {
        for (Object t : list) {
            treeBaseService.removeById(((ListEntity<?>) t).getId());
        }
        return true;
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        for (Object t : list) {
            treeBaseService.removeById(((ListEntity<?>) t).getId());
        }
        return true;
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list, boolean useFill) {
        for (Object t : list) {
            treeBaseService.removeById(((ListEntity<?>) t).getId());
        }
        return true;
    }

    @Override
    public boolean update(Wrapper<T> updateWrapper) {
        List<T> ts = super.list(updateWrapper);
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        List<String> cacheKeys =
                ts.stream().map(e -> CACHE_LIST_NAME + "::" + this.getClass().getName() + ":" + e.getId()).collect(Collectors.toList());
        super.update(updateWrapper);
        cacheStorageService.del(cacheKeys);
        return true;
    }

    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        List<T> ts = super.list(new QueryWrapper<>(entity));
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        List<String> cacheKeys =
                ts.stream().map(e -> CACHE_LIST_NAME + "::" + this.getClass().getName() + ":" + e.getId()).collect(Collectors.toList());
        super.update(entity, updateWrapper);
        cacheStorageService.del(cacheKeys);
        return true;
    }

    @Override
    public boolean updateBatchById(Collection<T> ts) {
        List<String> cacheKeys =
                ts.stream().map(e -> CACHE_LIST_NAME + "::" + this.getClass().getName() + ":" + e.getId()).collect(Collectors.toList());
        super.updateBatchById(ts);
        cacheStorageService.del(cacheKeys);
        return true;
    }
}
