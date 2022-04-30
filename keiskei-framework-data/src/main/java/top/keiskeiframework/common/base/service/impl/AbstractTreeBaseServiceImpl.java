package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.service.TreeBaseService;
import top.keiskeiframework.common.base.util.BaseRequestUtils;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.enums.log.OperateNotifyType;
import top.keiskeiframework.common.util.TreeEntityUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 基础查询接口
 *
 * @param <T> .
 * @param <ID> .
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public abstract class AbstractTreeBaseServiceImpl<T extends TreeEntity<ID>, ID extends Serializable>
        extends AbstractBaseServiceImpl<T, ID>
        implements TreeBaseService<T, ID> , IService<T> {

    protected final static String SPILT = "/";
    @Autowired
    private TreeBaseService<T, ID> treeService;

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    @Lockable(key = "#t.hashCode()")
    public boolean save(T t) {
        return super.save(t);
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
        return super.updateById(t);
    }


    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    public boolean removeByColumn(String column, Serializable value) {
        List<T> ts = treeService.listByColumn(column, value);
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        List<T> tAll = treeService.list();
        for (T t : ts) {
            Set<Serializable> childIds = new TreeEntityUtils<>(tAll).getChildIds(t.getId());
            for (Serializable cid : childIds) {
                treeService.removeByIdSingle(cid);
            }
        }
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    public boolean removeByCondition(List<QueryConditionVO> conditions) {
        QueryWrapper<T> queryWrapper = BaseRequestUtils.getQueryWrapperByConditions(conditions, getEntityClass());
        List<T> ts = treeService.list(queryWrapper);
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        List<T> tAll = treeService.list();
        for (T t : ts) {
            Set<Serializable> childIds = new TreeEntityUtils<>(tAll).getChildIds(t.getId());
            for (Serializable cid : childIds) {
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
        Set<Serializable> childIds = new TreeEntityUtils<>(treeService.list()).getChildIds(id);
        for (Serializable cid : childIds) {
            treeService.removeByIdSingle(cid);
        }
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    @OperateNotify(type = OperateNotifyType.DELETE)
    public boolean removeByIdSingle(Serializable id) {
        return super.removeById(id);
    }


}
