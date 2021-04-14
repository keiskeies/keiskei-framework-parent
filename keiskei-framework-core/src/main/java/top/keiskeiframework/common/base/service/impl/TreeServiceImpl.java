package top.keiskeiframework.common.base.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.base.BaseRequest;
import top.keiskeiframework.common.base.entity.TreeEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.enums.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.TreeEntityUtils;
import top.keiskeiframework.common.vo.BaseSortDto;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 基础查询接口
 *
 * @param <T>
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class TreeServiceImpl<T extends TreeEntity> implements BaseService<T> {

    @Autowired
    protected JpaRepository<T, Long> jpaRepository;
    @Autowired
    protected JpaSpecificationExecutor<T> jpaSpecificationExecutor;
    @Autowired
    protected BaseService<T> baseService;

    protected final static String SPILT = "/";
    protected final static String CACHE_NAME = "SPRING_TREE_CACHE";

    @Override
    public Page<T> page(BaseRequest<T> request) {
        return null;
    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name", unless = "#result==null")
    public List<T> options() {
        ParameterizedType parameterizedType = ((ParameterizedType)this.getClass().getGenericSuperclass());
        Type[] types = parameterizedType.getActualTypeArguments();
        Class<?> clazz = (Class<?>) types[0];
        Field[] fields = clazz.getDeclaredFields();

        List<Sort.Order> orders = new ArrayList<>();

        for (Field field : fields) {
            SortBy sortBy = field.getAnnotation(SortBy.class);
            if (null != sortBy) {
                if (sortBy.desc()) {
                    orders.add(Sort.Order.desc(field.getName()));
                } else {
                    orders.add(Sort.Order.asc(field.getName()));
                }
            }
        }

        return jpaRepository.findAll(Sort.by(orders));
    }

    @Override
    public List<T> options(T t) {
        return null;
    }

    @Override
    public T getById(Long id) {
        return jpaRepository.findById(id).orElse(null);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name")
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
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            SortBy sortBy = field.getAnnotation(SortBy.class);
            if (null != sortBy) {
                long count = jpaRepository.count();
                try {
                    field.setAccessible(true);
                    if (null == field.get(t)) {
                        field.set(t, ++count);
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return jpaRepository.save(t);
    }

    @Override
    public List<T> saveAll(List<T> ts) {
        return null;
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
    public void changeSort(BaseSortDto baseSortDto) {

        ParameterizedType parameterizedType = ((ParameterizedType)this.getClass().getGenericSuperclass());
        Type[] types = parameterizedType.getActualTypeArguments();
        Class<T> clazz = (Class<T>) types[0];

        Field[] fields = clazz.getDeclaredFields();

        List<Field> sortFields = new ArrayList<>();
        for (Field field : fields) {
            SortBy sortBy = field.getAnnotation(SortBy.class);
            if (null != sortBy) {
                field.setAccessible(true);
                sortFields.add(field);
            }
        }

        if (CollectionUtils.isEmpty(sortFields)) {
            throw new BizException(BizExceptionEnum.NOT_FOUND_ERROR);
        }

        try {
            T t1 = clazz.newInstance();
            t1.setId(baseSortDto.getId1());
            for (Field field : sortFields) {
                field.set(t1, baseSortDto.getSortBy2());
            }


            T t2 = clazz.newInstance();
            t2.setId(baseSortDto.getId2());
            for (Field field : sortFields) {
                field.set(t2, baseSortDto.getSortBy1());
            }

            jpaRepository.save(t1);
            jpaRepository.save(t2);

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
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
