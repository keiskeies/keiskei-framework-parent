package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.util.Assert;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.dto.BaseSortVO;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.service.TreeBaseService;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.enums.log.OperateNotifyType;
import top.keiskeiframework.common.util.TreeEntityUtils;

import java.io.Serializable;
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
        implements TreeBaseService<T> , IService<T> {

    protected final static String SPILT = "/";
    @Autowired
    private TreeBaseService<T> treeService;


    @Override
    @OperateNotify(type = OperateNotifyType.SAVE)
    @Lockable(key = "#t.hashCode()")
    public T saveAndNotify(T t) {
        return treeService.saveCache(t);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")
    @Lockable(key = "#t.hashCode()")
    public T saveCache(T t) {
        if (null != t.getParentId()) {
            T parent = treeService.getById(t.getParentId());
            Assert.notNull(parent, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
            super.save(t);
            t.setSign(parent.getSign() + t.getId() + SPILT);

        } else {
            super.save(t);
            t.setSign(t.getId() + SPILT);
        }
        super.save(t);
        return t;
    }

    @Override
    @Cacheable(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id", unless = "#result == null")
    public T getById(Serializable id) {
        return super.getById(id);
    }


    @Override
    public boolean updateById(T t) {
        treeService.updateByIdCache(t);
        return true;
    }

    @Override
    @OperateNotify(type = OperateNotifyType.UPDATE)
    public T updateByIdAndNotify(T t) {
        return treeService.updateByIdCache(t);
    }


    @Override
    @Caching(
            evict = {@CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name")},
            put = {@CachePut(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #t.id")}
    )
    @OperateNotify(type = OperateNotifyType.UPDATE)
    public T updateByIdCache(T t) {
        Assert.notNull(t.getId(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
        if (null != t.getParentId()) {
            T parent = treeService.getById(t.getParentId());
            Assert.notNull(parent, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
            t.setSign(parent.getSign() + t.getId() + SPILT);
        } else {
            t.setSign(t.getId() + SPILT);
        }

        super.updateById(t);
        return t;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #baseSortVO.id1"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #baseSortVO.id2")
    })
    public void changeSort(BaseSortVO baseSortVO) {
        super.changeSort(baseSortVO);
    }

    @Override

    @OperateNotify(type = OperateNotifyType.DELETE)
    public boolean removeByIdAndNotify(Serializable id) {
        return treeService.removeById(id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_TREE_NAME, key = "targetClass.name"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    })
    public boolean removeById(Serializable id) {
        Set<Serializable> childIds = new TreeEntityUtils<>(treeService.findAll()).getChildIds(id);
        for (Serializable cid : childIds) {
            treeService.deleteByIdSingle(cid);
        }
        return true;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    @OperateNotify(type = OperateNotifyType.DELETE)
    public boolean deleteByIdSingle(Serializable id) {
        return super.removeById(id);
    }


}
