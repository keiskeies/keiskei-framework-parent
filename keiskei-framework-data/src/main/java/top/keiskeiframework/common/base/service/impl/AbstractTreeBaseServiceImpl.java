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
import org.springframework.util.Assert;
import top.keiskeiframework.cache.service.CacheStorageService;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.dto.BaseSortVO;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.enums.log.OperateNotifyType;
import top.keiskeiframework.common.util.TreeEntityUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * 基础查询接口
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public abstract class AbstractTreeBaseServiceImpl<T extends TreeEntity>
        extends AbstractBaseServiceImpl<T>
        implements BaseService<T>, IService<T> {


    protected final static String SPILT = "/";
    protected final static String CACHE_NAME = "CACHE:TREE";
    protected final static String CACHE_LIST_NAME = "CACHE:LIST";

    @Autowired
    @Lazy
    private BaseService<T> treeService;

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    @OperateNotify(type = OperateNotifyType.SAVE)
    @Lockable(key = "#t.hashCode()")
    public T saveAndNotify(T t) {
        if (null != t.getParentId()) {
            T parent = treeService.findById(t.getParentId());
            Assert.notNull(parent, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
            super.saveAndNotify(t);
            t.setSign(parent.getSign() + t.getId() + SPILT);

        } else {
            super.saveAndNotify(t);
            t.setSign(t.getId() + SPILT);
        }
        super.save(t);
        return t;
    }


    @Override
    @Cacheable(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id", unless = "#result == null")
    public T findByIdCache(Long id) {
        return super.findById(id);
    }


    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
            },
            put = {
                    @CachePut(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")
            }
    )
    @OperateNotify(type = OperateNotifyType.UPDATE)
    public T updateAndNotify(T t) {
        Assert.notNull(t.getId(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
        if (null != t.getParentId()) {
            T parent = treeService.findById(t.getParentId());
            Assert.notNull(parent, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
            t.setSign(parent.getSign() + t.getId() + SPILT);
        } else {
            t.setSign(t.getId() + SPILT);
        }

        super.updateAndNotify(t);
        return t;
    }

    @Override
    public List<T> findAllByColumnCache(String oneToMany, String column, Serializable value) {
        return super.findAllByColumn(column, value);
    }


    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #baseSortVO.id1"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #baseSortVO.id2")
    })
    public void changeSort(BaseSortVO baseSortVO) {
        super.changeSort(baseSortVO);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    })
    @OperateNotify(type = OperateNotifyType.DELETE)
    public void deleteByIdAndNotify(Long id) {
        Set<Long> childIds = new TreeEntityUtils<>(treeService.findAll()).getChildIds(id);
        for (Long cid : childIds) {
            treeService.deleteByIdAndNotifySingle(cid);
        }
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    @OperateNotify(type = OperateNotifyType.DELETE)
    public void deleteByIdAndNotifySingle(Long id) {
        super.removeById(id);
    }


}
