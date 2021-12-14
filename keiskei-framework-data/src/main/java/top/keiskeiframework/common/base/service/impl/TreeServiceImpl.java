package top.keiskeiframework.common.base.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.dto.BaseRequestDto;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.base.dto.BaseSortDTO;
import top.keiskeiframework.common.enums.OperateNotifyType;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.util.TreeEntityUtils;

import java.io.Serializable;
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
public class TreeServiceImpl<T extends TreeEntity<ID>, ID extends Serializable> extends AbstractBaseServiceImpl<T, ID> implements BaseService<T, ID> {


    protected final static String SPILT = "/";
    protected final static String CACHE_NAME = "CACHE:TREE";

    @Autowired
    private TreeServiceImpl<T, ID> treeService;


    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name", unless = "#result==null")
    public List<T> findAll() {
        return super.findAll();
    }


    @Override
    public List<T> findAll(BaseRequestDto<T, ID> request) {
        return super.findAll(request);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    @OperateNotify(type = OperateNotifyType.SAVE)
    @Lockable(key = "#t.hashCode()")
    public T saveAndNotify(T t) {
        return super.save(t);
    }


    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    @Lockable(key = "#t.hashCode()")
    public T save(T t) {
        if (null != t.getParentId()) {
            T parent = treeService.findById(t.getParentId());
            Assert.notNull(parent, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
            t = super.save(t);
            t.setSign(parent.getSign() + t.getId() + SPILT);

        } else {
            t = super.save(t);
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
        return super.update(t);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    public T update(T t) {
        Assert.notNull(t.getId(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
        if (null != t.getParentId()) {
            T parent = treeService.findById(t.getParentId());
            Assert.notNull(parent, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
            t.setSign(parent.getSign() + t.getId() + SPILT);
        } else {
            t.setSign(t.getId() + SPILT);
        }

        t = super.save(t);
        return t;
    }


    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    public void changeSort(BaseSortDTO<ID> baseSortDto) {
        super.changeSort(baseSortDto);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    @OperateNotify(type = OperateNotifyType.DELETE)
    public void deleteByIdAndNotify(ID id) {
        Set<ID> childIds = new TreeEntityUtils<>(treeService.findAll()).getChildIds(id);
        for (ID cid : childIds) {
            super.deleteById(cid);
        }
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    public void deleteById(ID id) {
        Set<ID> childIds = new TreeEntityUtils<>(treeService.findAll()).getChildIds(id);
        for (ID cid : childIds) {
            super.deleteById(cid);
        }
    }


}
