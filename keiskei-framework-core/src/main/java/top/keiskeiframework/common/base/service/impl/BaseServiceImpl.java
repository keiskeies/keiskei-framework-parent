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
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.base.BaseRequest;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.vo.base.BaseSortDTO;
import top.keiskeiframework.common.vo.base.ChartRequestDTO;
import top.keiskeiframework.common.vo.charts.Axis;
import top.keiskeiframework.common.vo.charts.ChartOptionVO;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/21 11:46
 */
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    @Autowired
    protected JpaRepository<T, Long> jpaRepository;
    @Autowired
    protected JpaSpecificationExecutor<T> jpaSpecificationExecutor;
    @Autowired
    protected EntityManager entityManager;
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
    public void changeSort(BaseSortDTO baseSortDto) {
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

    @Override
    public ChartOptionVO getChartOptions(ChartRequestDTO chartRequestDTO) {
        ChartOptionVO result = new ChartOptionVO();

        ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
        Type[] types = parameterizedType.getActualTypeArguments();
        Class<T> clazz = (Class<T>) types[0];
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);

        if (null != chartRequestDTO.getStart() && null != chartRequestDTO.getEnd()) {
            List<Predicate> predicates = new ArrayList<>();
            Expression<LocalDateTime> expression = root.get("createTime");
            predicates.add(builder.between(expression, chartRequestDTO.getStart(), chartRequestDTO.getEnd()));

            query.where(predicates.toArray(new Predicate[0]));
        }

        List<T> list;
        Axis axis = new Axis();
        if (chartRequestDTO.getColumnType().equals(ChartRequestDTO.ColumnType.TIME)) {

            query.multiselect(
                    builder.function("WEEKDAY", LocalDateTime.class, root.get("createTime")).as(String.class).alias("index").alias("index"),
                    builder.count(root).alias("indexNumber")
            );
            query.groupBy(builder.literal("index"));
            list = entityManager.createQuery(query).getResultList();
            axis.setData(DateTimeUtils.timeRange(chartRequestDTO.getStart(), chartRequestDTO.getEnd(), chartRequestDTO.getUnit()));
        } else {

            query.multiselect(
                    root.get(chartRequestDTO.getColumn()).as(String.class).alias("index"),
                    builder.count(root.get(chartRequestDTO.getColumn())).alias("indexNumber")
            );


            query.groupBy(root.get(chartRequestDTO.getColumn()));
            list = entityManager.createQuery(query).getResultList();
            axis.setData(list.stream().map(T::getIndex).collect(Collectors.toList()));
        }
        if (chartRequestDTO.getYHorizontal()) {
            result.setYAxis(axis);
        } else {
            result.setXAxis(axis);
        }
        switch (chartRequestDTO.getChartType()) {
            case "bar":
                break;
            case "radar":
                break;
            default:
                Map<String, List<T>> tMap = list.stream().collect(Collectors.groupingBy(T::getIndex));

                break;
        }
        return null;
    }


}
