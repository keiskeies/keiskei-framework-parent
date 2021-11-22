package top.keiskeiframework.common.base.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import top.keiskeiframework.common.annotation.Lockable;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.enums.OperateNotifyType;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.BaseRequestUtils;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
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
public class ListServiceImpl<T extends ListEntity<ID>, ID extends Serializable> extends AbstractBaseServiceImpl<T, ID> implements BaseService<T, ID> {


    protected final static String CACHE_NAME = "CACHE:LIST";
    @Autowired
    private ListServiceImpl<T, ID> listService;

    @Override
    public List<T> options() {
        try {
            return super.options(super.getTClass().newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    @Override
    public List<T> options(@NonNull T t) {
        CriteriaQuery<T> query = BaseRequestUtils.getCriteriaQuery(t, Collections.singletonList("id"));

        List<?> ids = super.entityManager.createQuery(query).getResultList();
        List<T> result = new ArrayList<>(ids.size());

        for (Object id : ids) {
            result.add(listService.findById((ID) id));
        }
        return result;
    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #id", unless = "#result == null")
    public T findById(ID id) {
        return super.findById(id);
    }


    @Override
    @OperateNotify(type = OperateNotifyType.SAVE)
    @Lockable(key = "#t.hashCode()")
    public T saveAndNotify(T t) {
        return super.save(t);
    }

    @Override
    @Lockable(key = "#t.hashCode()")
    public T save(T t) {
        return super.save(t);
    }

    @Override
    @OperateNotify(type = OperateNotifyType.UPDATE)
    @CachePut(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #t.id")
    public T updateAndNotify(T t) {
        return super.save(t);
    }

    @Override
    @CachePut(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #t.id")
    public T update(T t) {
        return super.save(t);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #id")
    @OperateNotify(type = OperateNotifyType.DELETE)
    public void deleteByIdAndNotify(ID id) {
        super.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + ':' + #id")
    public void deleteById(ID id) {
        super.deleteById(id);
    }


}
