package top.keiskeiframework.common.base.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import top.keiskeiframework.common.annotation.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.dto.base.QueryConditionDTO;
import top.keiskeiframework.common.enums.OperateNotifyType;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.util.TreeEntityUtils;
import top.keiskeiframework.common.dto.base.BaseSortDTO;

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
public class TreeServiceImpl<T extends TreeEntity> extends AbstractBaseServiceImpl<T> implements BaseService<T> {


    protected final static String SPILT = "/";
    protected final static String CACHE_NAME = "SPRING_TREE_CACHE";

    @Autowired
    private TreeServiceImpl<T> baseService;

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name", unless = "#result==null")
    public List<T> options() {
        return super.options();
    }

    @Override
    public List<T> options(List<QueryConditionDTO> t) {
        return baseService.options();
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    @OperateNotify(type = OperateNotifyType.SAVE)
    @Lockable(key = "#t.toString()")
    public T saveAndNotify(T t) {
        if (null != t.getParentId()) {
            T parent = this.getById(t.getParentId());
            Assert.notNull(parent, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
            t = jpaRepository.save(t);
            t.setSign(parent.getSign() + t.getId() + SPILT);

        } else {
            t = jpaRepository.save(t);
            t.setSign(t.getId() + SPILT);
        }
        return super.save(t);
    }


    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    @Lockable(key = "#t.toString()")
    public T save(T t) {
        if (null != t.getParentId()) {
            T parent = this.getById(t.getParentId());
            Assert.notNull(parent, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
            t = jpaRepository.save(t);
            t.setSign(parent.getSign() + t.getId() + SPILT);

        } else {
            t = jpaRepository.save(t);
            t.setSign(t.getId() + SPILT);
        }
        return super.save(t);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    public List<T> saveAll(List<T> ts) {
        return super.saveAll(ts);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    @OperateNotify(type = OperateNotifyType.UPDATE)
    public T updateAndNotify(T t) {
        Assert.notNull(t.getId(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
        if (null != t.getParentId()) {
            T parent = this.getById(t.getParentId());
            Assert.notNull(parent, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
            t.setSign(parent.getSign() + t.getId() + SPILT);
        } else {
            t.setSign(t.getId() + SPILT);
        }

        t = jpaRepository.save(t);
        return t;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    public T update(T t) {
        Assert.notNull(t.getId(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
        if (null != t.getParentId()) {
            T parent = this.getById(t.getParentId());
            Assert.notNull(parent, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
            t.setSign(parent.getSign() + t.getId() + SPILT);
        } else {
            t.setSign(t.getId() + SPILT);
        }

        t = jpaRepository.save(t);
        return t;
    }


    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    public void changeSort(BaseSortDTO baseSortDto) {
        super.changeSort(baseSortDto);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    @OperateNotify(type = OperateNotifyType.DELETE)
    public void deleteByIdAndNotify(Long id) {
        Set<Long> childIds = new TreeEntityUtils<>(baseService.options()).getChildIds(id);
        for (Long cid : childIds) {
            jpaRepository.deleteById(cid);
        }
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    public void deleteById(Long id) {
        Set<Long> childIds = new TreeEntityUtils<>(baseService.options()).getChildIds(id);
        for (Long cid : childIds) {
            jpaRepository.deleteById(cid);
        }
    }


}
