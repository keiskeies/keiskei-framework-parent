package top.keiskeiframework.common.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.base.BaseRequest;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.dto.base.BaseSortDTO;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.dashboard.TimeDeltaEnum;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.util.DateTimeUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.temporal.UnsupportedTemporalTypeException;
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
        Assert.notNull(t.getId(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
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
    public Map<String, Long> getChartOptions(ChartRequestDTO chartRequestDTO) {

        ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
        Type[] types = parameterizedType.getActualTypeArguments();
        Class<T> clazz = (Class<T>) types[0];
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);

        // 基本时间条件
        List<Predicate> predicates = new ArrayList<>();
        Expression<LocalDateTime> expression = root.get("createTime");
        predicates.add(builder.between(expression, chartRequestDTO.getStart(), chartRequestDTO.getEnd()));

        query.where(predicates.toArray(new Predicate[0]));

        List<T> list;
        if (ColumnType.TIME.equals(chartRequestDTO.getColumnType())) {
            Expression<String> index = getTimeIndex(builder, root, chartRequestDTO.getColumn(), chartRequestDTO.getTimeDelta());

            query.multiselect(
                    index.alias("index"),
                    builder.count(root).alias("indexNumber")
            );
            query.groupBy(index);
            list = entityManager.createQuery(query).getResultList();
            if (TimeDeltaEnum.WEEK_DAY.equals(chartRequestDTO.getTimeDelta())) {
                list.forEach(e -> e.setIndex(DateTimeUtils.WEEK_RANGE.get(Integer.parseInt(e.getIndex()))));
            }
        } else {

            Expression<String> index = root.get(chartRequestDTO.getColumn()).as(String.class);

            query.multiselect(
                    index.alias("index"),
                    builder.count(root.get(chartRequestDTO.getColumn())).alias("indexNumber")
            );

            query.groupBy(index);
            list = entityManager.createQuery(query).getResultList();
        }
        return list.stream().collect(Collectors.toMap(T::getIndex, T::getIndexNumber));
    }


    /**
     * 组装jpa时间统计字段
     *
     * @param builder 构建工具
     * @param root    root
     * @param column  字段名
     * @param delta    单位
     * @return 。
     */
    protected Expression<String> getTimeIndex(CriteriaBuilder builder, Root<T> root, String column, TimeDeltaEnum delta) {
        Expression<String> index;
        switch (delta) {
            case HOUR:
                index = builder.function("DATE_FORMAT", LocalDateTime.class, root.get(column), builder.literal("%H")).as(String.class);
                break;
            case ALL_HOURS:
                index = builder.function("DATE_FORMAT", LocalDateTime.class, root.get(column), builder.literal("%Y-%m-%d %H")).as(String.class);
                break;
            case WEEK_DAY:
                index = builder.function("WEEKDAY", LocalDateTime.class, root.get(column)).as(String.class);
                break;
            case MONTH:
                index = builder.function("DATE_FORMAT", LocalDateTime.class, root.get(column), builder.literal("%m")).as(String.class);
                break;
            case MONTH_DAYS:
                index = builder.function("DATE_FORMAT", LocalDateTime.class, root.get(column), builder.literal("%d")).as(String.class);
                break;
            case ALL_DAYS:
                index = builder.function("DATE_FORMAT", LocalDateTime.class, root.get(column), builder.literal("%Y-%m-%d")).as(String.class);
                break;
            case ALL_MONTHS:
                index = builder.function("DATE_FORMAT", LocalDateTime.class, root.get(column), builder.literal("%Y-%m")).as(String.class);
                break;
            case QUARTER:
                index = builder.function("QUARTER", LocalDateTime.class, root.get(column)).as(String.class);
                break;
            case ALL_QUARTERS:
                index = builder.function( "CONCAT",
                        LocalDateTime.class,
                        builder.function("DATE_FORMAT", LocalDateTime.class, root.get(column), builder.literal("%Y")),
                        builder.literal("-"),
                        builder.function("QUARTER", LocalDateTime.class, root.get(column))
                ).as(String.class);
                break;
            case YEAR:
                index = builder.function("DATE_FORMAT", LocalDateTime.class, root.get(column), builder.literal("%Y")).as(String.class);
                break;
            default:
                throw new UnsupportedTemporalTypeException("Unsupported unit: " + delta);
        }

        return index;
    }


}
