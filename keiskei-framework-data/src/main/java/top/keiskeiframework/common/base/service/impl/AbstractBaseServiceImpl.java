package top.keiskeiframework.common.base.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.hibernate.query.criteria.internal.CriteriaQueryImpl;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.base.BaseRequest;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.dto.base.BaseSortDTO;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.dashboard.TimeDeltaEnum;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.util.BaseRequestUtils;
import top.keiskeiframework.common.util.DateTimeUtils;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 基础服务实现
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/21 11:46
 */
@Slf4j
public abstract class AbstractBaseServiceImpl<T extends ListEntity<ID>, ID extends Serializable> implements BaseService<T, ID> {

    @Autowired
    protected JpaRepository<T, ID> jpaRepository;
    @Autowired
    protected JpaSpecificationExecutor<T> jpaSpecificationExecutor;
    @Autowired
    protected EntityManager entityManager;

    protected Class<T> getTClass() {
        ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
        Type[] types = parameterizedType.getActualTypeArguments();
        return (Class<T>) types[0];
    }
    protected Class<T> getIDClass() {
        ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
        Type[] types = parameterizedType.getActualTypeArguments();
        return (Class<T>) types[1];
    }


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
    public Page<T> page(BaseRequest<T, ID> request) {
        Class<T> tClass = getTClass();
        Pageable pageable = request.getPageable(tClass);

        if (CollectionUtils.isEmpty(request.getShow())) {
            return jpaSpecificationExecutor.findAll(request.getSpecification(tClass), pageable);
        } else {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();
            Root<T> root = query.from(tClass);


            if (!CollectionUtils.isEmpty(request.getConditions())) {
                Map<String, Class<?>> fieldMap = BaseRequestUtils.getFieldMap(tClass);
                List<Predicate> predicates = BaseRequestUtils.getPredicates(query.from(tClass), builder, request.getConditions(), fieldMap);
                query.where(predicates.toArray(new Predicate[0]));
            }

            List<Selection<?>> selections = new ArrayList<>(request.getShow().size());
            for (String showColumn : request.getShow()) {
                selections.add(root.get(showColumn));
            }
            query.multiselect(selections);
            List<Order> orders = new ArrayList<>();

            if (!StringUtils.isEmpty(request.getDesc())) {
                orders.add(new OrderImpl(root.get(request.getDesc()), false));
            }
            if (!StringUtils.isEmpty(request.getAsc())) {
                orders.add(new OrderImpl(root.get(request.getAsc()), true));
            }

            if (CollectionUtils.isEmpty(orders)) {
                orders.add(new OrderImpl(root.get("createTime"), false));
            }
            query.orderBy(orders);


            TypedQuery<Tuple> tTypedQuery = entityManager.createQuery(query);
            tTypedQuery.setFirstResult((request.getPage() - 1) * request.getSize());
            tTypedQuery.setMaxResults(request.getSize());

            List<Tuple> tList = tTypedQuery.getResultList();
            if (CollectionUtils.isEmpty(tList)) {
                return new PageImpl<T>(Collections.emptyList(), pageable, 0);
            }
            List<T> contents = new ArrayList<>(tList.size());
            for (Tuple tuple : tList) {
                List<TupleElement<?>> tupleElements = tuple.getElements();
                T t = null;
                try {
                    t = tClass.newInstance();
                    for (int i = 0; i < tupleElements.size(); i++) {
                        TupleElement<?> tupleElement = tupleElements.get(i);
                        Field field = null;
                        try {
                            field = tClass.getDeclaredField(request.getShow().get(i));
                        } catch (NoSuchFieldException | SecurityException e) {
                            field = tClass.getSuperclass().getDeclaredField(request.getShow().get(i));
                        }
                        field.setAccessible(true);
                        field.set(t, tuple.get(i));
                        log.info("alias: {}, type: {}, value: {}",tupleElement.getAlias(), tupleElement.getJavaType().getName(), tuple.get(i).toString());
                    }
                } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                contents.add(t);
            }
            return new PageImpl<T>(contents, pageable, 300);
        }
    }


    @Override
    public List<T> options() {
        Class<T> clazz = getTClass();
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
    public T findById(ID id) {
        return jpaRepository.findById(id).orElse(null);
    }

    @Override
    public T save(T t) {
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
    public void changeSort(BaseSortDTO<ID> baseSortDto) {
        try {
            Class<T> clazz = getTClass();
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
    public void deleteById(ID id) {
        jpaRepository.deleteById(id);
        this.reconfirmIndex();
    }

    public void reconfirmIndex() {
        entityManager.createNativeQuery("alter table " + getTClass() + " engine=InnoDB;");
        entityManager.close();
    }

    @Override
    public Map<String, Long> getChartOptions(ChartRequestDTO chartRequestDTO) {

        Class<T> clazz = getTClass();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);


        // 基本时间条件
        List<Predicate> predicates = new ArrayList<>();
        Expression<LocalDateTime> expression = root.get("createTime");
        predicates.add(builder.between(expression, chartRequestDTO.getStart(), chartRequestDTO.getEnd()));

        if (null != chartRequestDTO.getConditions() && !chartRequestDTO.getConditions().isEmpty()) {
            for (Map.Entry<String, List<String>> entry : chartRequestDTO.getConditions().entrySet()) {

                Object[] hasValueValues = entry.getValue().stream()
                        .filter(e -> !StringUtils.isEmpty(e))
                        .toArray();
                if (hasValueValues.length > 0) {
                    predicates.add(root.get(entry.getKey()).in(hasValueValues));
                }
            }
        }

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
     * @param delta   单位
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
                index = builder.function("CONCAT",
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
