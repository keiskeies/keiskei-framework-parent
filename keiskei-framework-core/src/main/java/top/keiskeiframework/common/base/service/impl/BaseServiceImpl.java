package top.keiskeiframework.common.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.base.BaseRequest;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.vo.BaseSortDto;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/21 11:46
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    protected JpaRepository<T, Long> jpaRepository;
    @Autowired
    protected JpaSpecificationExecutor<T> jpaSpecificationExecutor;
    @Autowired
    protected BaseService<T> baseService;


    @Override
    public Page<T> page(Specification<T> s, Pageable p) {
        return jpaSpecificationExecutor.findAll(s, p);
    }

    @Override
    public Page<T> page(Example<T> e, Pageable p) {
        return jpaRepository.findAll(e, p);
    }

    @Override
    public List<T> findAll(Specification<T> s) {
        return jpaSpecificationExecutor.findAll(s);
    }

    @Override
    public List<T> findAll(Specification<T> s, Sort sort) {
        return jpaSpecificationExecutor.findAll(s, sort);
    }

    @Override
    public List<T> findAll(Example<T> e) {
        return jpaRepository.findAll(e);
    }

    @Override
    public List<T> findAll(Example<T> e, Sort sort) {
        return jpaRepository.findAll(e, sort);
    }

    @Override
    public Page<T> page(BaseRequest<T> request) {
        return jpaSpecificationExecutor.findAll(request.getSpecification(), request.getPageable());
    }

    @Override
    public List<T> options() {
        ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
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
                break;
            }
        }
        return jpaRepository.findAll(Example.of(t), Sort.by(orders));
    }

    @Override
    public T getById(Long id) {
        return jpaRepository.findById(id).orElse(null);
    }

    @Override
    public T save(T t) {
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
                break;
            }
        }

        return jpaRepository.save(t);
    }

    @Override
    public List<T> saveAll(List<T> ts) {
        if (CollectionUtils.isEmpty(ts)) {
            return ts;
        }
        Field[] fields = ts.get(0).getClass().getDeclaredFields();
        long count = jpaRepository.count();
        for (Field field : fields) {
            SortBy sortBy = field.getAnnotation(SortBy.class);
            if (null != sortBy) {
                field.setAccessible(true);
                for (T t : ts) {
                    try {
                        field.set(t, ++count);
                    } catch (IllegalAccessException ignored) {
                    }
                }
                break;
            }
        }
        return jpaRepository.saveAll(ts);
    }

    @Override
    public T update(T t) {
        t = jpaRepository.save(t);
        return t;
    }

    @Override
    public void changeSort(BaseSortDto baseSortDto) {
        try {
            ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
            Type[] types = parameterizedType.getActualTypeArguments();
            Class<T> clazz = (Class<T>) types[0];
            Field[] fields = clazz.getDeclaredFields();
            T t1 = clazz.newInstance();
            T t2 = clazz.newInstance();
            for (Field field : fields) {
                if ("id".equals(field.getName())) {
                    field.setAccessible(true);
                    field.set(t1, baseSortDto.getId1());
                    field.set(t2, baseSortDto.getId2());
                } else {
                    SortBy sortBy = field.getAnnotation(SortBy.class);
                    if (null != sortBy) {
                        field.setAccessible(true);
                        field.set(t1, baseSortDto.getSortBy2());
                        field.set(t2, baseSortDto.getSortBy1());
                    }
                }
            }
            jpaRepository.save(t1);
            jpaRepository.save(t2);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
