package top.keiskeiframework.common.base.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.annotation.validate.SortBy;
import top.keiskeiframework.common.base.BaseRequest;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.enums.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.vo.BaseSortDto;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础查询接口
 *
 * @param <T>
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    @Autowired
    protected JpaRepository<T, Long> jpaRepository;
    @Autowired
    protected JpaSpecificationExecutor<T> jpaSpecificationExecutor;
    @Autowired
    protected BaseService<T> baseService;

    protected final static String CACHE_NAME = "SPRING_BASE_CACHE";


    @Override
    public Page<T> page(BaseRequest<T> request) {
        return jpaSpecificationExecutor.findAll(request.getSpecification(), request.getPageable());
    }

    @Override
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
        Field[] fields = t.getClass().getDeclaredFields();
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
        return jpaRepository.findAll(Example.of(t), Sort.by(orders));
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
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name + '-' + #id", unless = "#result == null")
    public T getById(Long id) {
        T t = jpaRepository.findById(id).orElse(null);
        return t;
    }

    @Override
    public T save(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        List<Field> sortFields = new ArrayList<>(1);
        for (Field field : fields) {
            SortBy sortBy = field.getAnnotation(SortBy.class);
            if (null != sortBy) {
                try {
                    field.setAccessible(true);
                    if (null == field.get(t)) {
                        sortFields.add(field);
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        if (!CollectionUtils.isEmpty(sortFields)) {
            long count = jpaRepository.count();
            for (Field field : sortFields) {
                try {
                    field.set(t, ++count);
                } catch (IllegalAccessException ignored) {
                }
            }
        }

        return jpaRepository.save(t);
    }

    @Override
    public List<T> saveAll(List<T> tList) {
        Field[] fields = tList.get(0).getClass().getDeclaredFields();
        List<Field> sortFields = new ArrayList<>(1);
        for (Field field : fields) {
            SortBy sortBy = field.getAnnotation(SortBy.class);
            if (null != sortBy) {
                field.setAccessible(true);
                sortFields.add(field);
            }
        }
        if (!CollectionUtils.isEmpty(sortFields)) {
            long count = jpaRepository.count();
            for (Field field : sortFields) {
                for (T t : tList) {
                    try {
                        field.set(t, ++count);
                    } catch (IllegalAccessException ignored) {
                    }
                }
            }
        }
        return jpaRepository.saveAll(tList);
    }

    @Override
    @CachePut(cacheNames = CACHE_NAME, key = "targetClass.name + '-' + #t.id", unless = "#result == null")
    public T update(T t) {
        Assert.notNull(t.getId(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
        t = jpaRepository.save(t);
        return t;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + '-' + #id")
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }


}
