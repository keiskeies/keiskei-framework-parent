package top.keiskeiframework.common.base.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.BaseRequest;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.dto.base.BaseSortDTO;
import top.keiskeiframework.common.enums.OperateNotifyType;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.util.TreeEntityUtils;

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
    protected final static String CACHE_NAME = "CACHE:TREE";

    @Autowired
    private TreeServiceImpl<T> baseService;

    @Override
    public List<T> findAll(BaseRequest<T> request) {
        return baseService.findAll();
    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name", unless = "#result==null")
    public List<T> findAll() {
        return super.findAll();
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    @OperateNotify(type = OperateNotifyType.SAVE)
    public T saveAndNotify(T t) {
        return this.save(t);
    }


    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    public T save(T t) {
        if (null != t.getParentId()) {
            T parent = this.findById(t.getParentId());
            Assert.notNull(parent, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
            t = mongoRepository.save(t);
            t.setSign(parent.getSign() + t.getId() + SPILT);

        } else {
            t = mongoRepository.save(t);
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
        T tOld = baseService.findById(t.getId());
        BeanUtils.copyPropertiesIgnoreJson(t, tOld);
        return this.update(t);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    public T update(T t) {
        Assert.notNull(t.getId(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
        T tOld = baseService.findById(t.getId());
        BeanUtils.copyPropertiesIgnoreJson(t, tOld);
        if (null != t.getParentId()) {
            T parent = this.findById(t.getParentId());
            Assert.notNull(parent, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
            t.setSign(parent.getSign() + t.getId() + SPILT);
        } else {
            t.setSign(t.getId() + SPILT);
        }

        t = mongoRepository.save(t);
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
    public void deleteByIdAndNotify(String id) {
        Set<String> childIds = new TreeEntityUtils<>(baseService.findAll()).getChildIds(id);
        for (String cid : childIds) {
            mongoRepository.deleteById(cid);
        }
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
    public void deleteById(String id) {
        Set<String> childIds = new TreeEntityUtils<>(baseService.findAll()).getChildIds(id);
        for (String cid : childIds) {
            mongoRepository.deleteById(cid);
        }
    }


}
