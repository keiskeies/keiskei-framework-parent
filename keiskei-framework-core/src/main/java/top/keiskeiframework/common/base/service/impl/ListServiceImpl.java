package top.keiskeiframework.common.base.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import top.keiskeiframework.common.annotation.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.enums.OperateNotifyType;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.BaseRequestUtils;

import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 树形实体类基础服务实现
 * </p>
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class ListServiceImpl<T extends BaseEntity> extends AbstractBaseServiceImpl<T> implements BaseService<T> {


    protected final static String CACHE_NAME = "SPRING_BASE_CACHE";
    @Autowired
    protected ListServiceImpl<T> baseService;

    @Override
    public List<T> options() {
        try {
            return this.options(getTClass().newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    @Override
    public List<T> options(@NonNull T t) {
        CriteriaQuery<T> query = BaseRequestUtils.getCriteriaQuery(t, Collections.singletonList("id"));

        List<?> ids = entityManager.createQuery(query).getResultList();
        List<T> result = new ArrayList<>(ids.size());

        for (Object id : ids) {
            result.add(baseService.getById((Long) id));
        }
        return result;
    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name + '-' + #id", unless = "#result == null")
    public T getById(Long id) {
        return super.getById(id);
    }


    @Override
    @OperateNotify(type = OperateNotifyType.SAVE)
    @Lockable(key = "#t.toString()")
    public T saveAndNotify(T t) {
        return super.save(t);
    }

    @Override
    @Lockable(key = "#t.toString()")
    public T save(T t) {
        return super.save(t);
    }

    @Override
    @OperateNotify(type = OperateNotifyType.UPDATE)
    public T updateAndNotify(T t) {
        return baseService.update(t, t.getId());
    }

    @Override
    public T update(T t) {
        return baseService.update(t, t.getId());
    }

    @CachePut(cacheNames = CACHE_NAME, key = "targetClass.name + '-' + #id")
    public T update(T t, Long id) {
        return super.update(t);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + '-' + #id")
    @OperateNotify(type = OperateNotifyType.DELETE)
    public void deleteByIdAndNotify(Long id) {
        super.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + '-' + #id")
    public void deleteById(Long id) {
        super.deleteById(id);
    }


}
