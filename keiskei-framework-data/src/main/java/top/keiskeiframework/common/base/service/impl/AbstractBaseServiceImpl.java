package top.keiskeiframework.common.base.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.annotation.data.SortBy;
import top.keiskeiframework.common.base.dto.BasePageDto;
import top.keiskeiframework.common.base.dto.BaseRequestDto;
import top.keiskeiframework.common.base.dto.QueryConditionDTO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.base.dto.BaseSortDTO;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.dashboard.TimeDeltaEnum;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.base.util.BaseRequestUtils;
import top.keiskeiframework.common.util.DateTimeUtils;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    @Autowired
    protected BaseService<T, ID> baseService;

    protected final static String TIME_FIELD_DEFAULT = "createTime";
    protected final static String TIME_FIELD_UNDEFINED = "undefined";
    protected final static String FIELD_NAME = "fieldName";
    protected final static String FIELD_NUMBER = "fieldNumber";

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
    public Page<T> page(BaseRequestDto<T, ID> request, BasePageDto<T, ID> page) {
        Class<T> tClass = getTClass();
        Pageable pageable = page.getPageable(tClass);

        Specification<T> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = BaseRequestUtils.getPredicates(root, criteriaBuilder, request.getConditions(tClass));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        if (request.showEmpty()) {
            return jpaSpecificationExecutor.findAll(specification, pageable);
        } else {
            long total = jpaSpecificationExecutor.count(specification);
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();
            Root<T> root = query.from(tClass);
            Map<String, Join<T, ?>> joinMap = new ConcurrentHashMap<>();

            Predicate predicate = specification.toPredicate(root, query, builder);
            query.where(predicate);
            query.multiselect(BaseRequestUtils.getSelections(root, request.getShow(tClass), joinMap));
            query.orderBy(BaseRequestUtils.getOrders(root, tClass, page.getAsc(), page.getDesc(), joinMap));
            List<T> contents = BaseRequestUtils.queryDataList(query, page.getPage(), page.getSize(), request.getShow(tClass), tClass);
            return new PageImpl<T>(contents, pageable, total);
        }
    }


    @Override
    public List<T> findAll() {
        Class<T> tClass = getTClass();
        return jpaRepository.findAll(BaseRequestUtils.getSort(tClass));
    }

    @Override
    public List<T> findAll(T t) {
        Class<T> tClass = getTClass();
        return jpaRepository.findAll(Example.of(t), BaseRequestUtils.getSort(tClass));
    }

    @Override
    public List<T> findAll(BaseRequestDto<T, ID> request) {
        Class<T> tClass = getTClass();
        if (request.showEmpty()) {
            return getEntityQueryData(request.getConditions(tClass));
        } else {
            return getTupleQueryData(tClass, request.getConditions(tClass), request.getShow(tClass), null, null);
        }
    }

    @Override
    public List<T> findAll(BaseRequestDto<T, ID> request, BasePageDto<T, ID> page) {
        Class<T> tClass = getTClass();
        if (request.showEmpty()) {
            return getEntityQueryData(request.getConditions(tClass));
        } else {
            return getTupleQueryData(tClass, request.getConditions(tClass), request.getShow(tClass), page.getAsc(), page.getDesc());
        }
    }

    private List<T> getEntityQueryData(List<QueryConditionDTO> conditions) {
        Specification<T> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = BaseRequestUtils.getPredicates(root, criteriaBuilder, conditions);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return jpaSpecificationExecutor.findAll(specification);
    }

    private List<T> getTupleQueryData(Class<T> tClass, List<QueryConditionDTO> conditions, List<String> show, String asc, String desc) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = criteriaBuilder.createTupleQuery();
        Root<T> root = query.from(tClass);
        Map<String, Join<T, ?>> joinMap = new HashMap<>();

        List<Predicate> predicates = BaseRequestUtils.getPredicates(root, criteriaBuilder, conditions);
        query.where(predicates.toArray(new Predicate[0]));
        query.multiselect(BaseRequestUtils.getSelections(root, show, joinMap));
        if (StringUtils.isEmpty(asc) && StringUtils.isEmpty(desc)) {
            query.orderBy(BaseRequestUtils.getOrders(root, tClass));
        } else {
            query.orderBy(BaseRequestUtils.getOrders(root, tClass, asc, desc, joinMap));
        }
        return BaseRequestUtils.queryDataList(query, show, tClass);
    }

    @Override
    public List<T> findAllByColumn(String column, Serializable value) {
        return findAll(new BaseRequestDto<>(column, value));
    }

    @Override
    public Long count(BaseRequestDto<T, ID> request) {
        Class<T> tClass = getTClass();
        Specification<T> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = BaseRequestUtils.getPredicates(root, criteriaBuilder, request.getConditions(tClass));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return jpaSpecificationExecutor.count(specification);
    }

    @Override
    public T findById(ID id) {
        return jpaRepository.findById(id).orElse(null);
    }


    @Override
    public T findByColumn(String column, Serializable value) {
        Class<T> tClass = getTClass();

        try {
            T t = tClass.newInstance();
            Field field = tClass.getDeclaredField(column);
            field.setAccessible(true);
            field.set(t, value);

            Example<T> example = Example.of(t);
            Optional<T> optionalT = jpaRepository.findOne(example);

            return optionalT.orElse(null);
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public T save(T t) {
        baseService.validate(t);
        return jpaRepository.save(t);
    }

    @Override
    public List<T> saveAll(List<T> ts) {
        if (CollectionUtils.isEmpty(ts)) {
            return ts;
        }
        ts.forEach(baseService::validate);
        ts = jpaRepository.saveAll(ts);
        Field[] fields = ts.get(0).getClass().getDeclaredFields();
        for (Field field : fields) {
            SortBy sortBy = field.getAnnotation(SortBy.class);
            if (null != sortBy) {
                field.setAccessible(true);
                for (T t : ts) {
                    try {
                        field.set(t, t.getId());
                    } catch (IllegalAccessException ignored) {
                    }
                }
                return jpaRepository.saveAll(ts);
            }
        }
        return ts;
    }

    @Override
    public T update(T t) {
        Assert.notNull(t.getId(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
        baseService.validate(t);
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
            boolean hasSort = false;
            for (Field field : fields) {
                if ("id".equals(field.getName())) {
                    field.setAccessible(true);
                    field.set(t1, baseSortDto.getId1());
                    field.set(t2, baseSortDto.getId2());
                } else {
                    SortBy sortBy = field.getAnnotation(SortBy.class);
                    if (null != sortBy) {
                        hasSort = true;
                        field.setAccessible(true);
                        field.set(t1, baseSortDto.getSortBy2());
                        field.set(t2, baseSortDto.getSortBy1());
                    }
                }
            }
            if (hasSort) {
                jpaRepository.save(t1);
                jpaRepository.save(t2);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(ID id) {
        jpaRepository.deleteById(id);
        this.reconfirmIndex();
    }

    @Override
    public void validate(T t) {
        log.info("validate from parent : {}", this.getTClass().getSimpleName());
    }

    public void reconfirmIndex() {
        entityManager.createNativeQuery("alter table " + getTClass() + " engine=InnoDB;");
    }

    @Override
    public Map<String, Double> getChartOptions(ChartRequestDTO chartRequestDTO) {

        Class<T> clazz = getTClass();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<T> root = query.from(clazz);


        // 基本时间条件
        List<Predicate> predicates = new ArrayList<>();
        // 是否指定新的创建时间字段
        String timeField = chartRequestDTO.getTimeField();
        if (StringUtils.isEmpty(timeField)) {
            timeField = TIME_FIELD_DEFAULT;
        }

        Expression<LocalDateTime> timeExpression = root.get(timeField);
        if (null != chartRequestDTO.getStart() && null != chartRequestDTO.getEnd()) {
            predicates.add(builder.between(timeExpression, chartRequestDTO.getStart(), chartRequestDTO.getEnd()));
        }

        if (null != chartRequestDTO.getConditions() && !chartRequestDTO.getConditions().isEmpty()) {

            Expression<?> expression;
            for (Map.Entry<String, List<String>> entry : chartRequestDTO.getConditions().entrySet()) {

                Object[] hasValueValues = entry.getValue().stream()
                        .filter(e -> !StringUtils.isEmpty(e))
                        .toArray();
                if (hasValueValues.length > 0) {
                    if (entry.getKey().contains(".")) {
                        String[] columns = entry.getKey().split("\\.");
                        Join<T, ?> join = root.join(columns[0], JoinType.INNER);
                        expression = join.get(columns[1]);
                    } else {
                        expression = root.get(entry.getKey());
                    }

                    if (hasValueValues.length == 1) {
                        predicates.add(builder.equal(expression, hasValueValues[0]));
                    } else {
                        predicates.add(expression.in(hasValueValues));
                    }
                }
            }
        }

        query.where(predicates.toArray(new Predicate[0]));

        Map<String, Double> result = new HashMap<>();
        List<Tuple> list;
        if (ColumnType.TIME.equals(chartRequestDTO.getColumnType())) {
            Expression<String> index = getTimeIndex(builder, root, chartRequestDTO.getColumn(), chartRequestDTO.getTimeDelta());

            query.multiselect(
                    index.alias(FIELD_NAME),
                    builder.count(root).alias(FIELD_NUMBER)
            );
            query.groupBy(index);
            list = entityManager.createQuery(query).getResultList();
            if (TimeDeltaEnum.WEEK_DAY.equals(chartRequestDTO.getTimeDelta())) {
                list.forEach(e -> {
                    result.put(
                            DateTimeUtils.WEEK_RANGE.get(Integer.parseInt(e.get(0, String.class))),
                            e.get(1, Long.class).doubleValue()
                    );
                });
                return result;
            }
        } else {
            Expression<String> index = root.get(chartRequestDTO.getColumn()).as(String.class);
            Expression<?> indexNumber;
            if (chartRequestDTO.getCalcType() == CalcType.SUM) {
                indexNumber = builder.sum(root.get(chartRequestDTO.getColumn()));
            } else {
                indexNumber = builder.count(root.get(chartRequestDTO.getColumn()));
            }
            query.multiselect(
                    index.alias(FIELD_NAME),
                    indexNumber.alias(FIELD_NUMBER)
            );

            query.groupBy(index);
            list = entityManager.createQuery(query).getResultList();
        }
        if (chartRequestDTO.getCalcType() == CalcType.SUM) {
            list.forEach(e -> {
                result.put(
                        e.get(0, String.class),
                        e.get(1, Double.class)
                );
            });
        } else {
            list.forEach(e -> {
                result.put(
                        e.get(0, String.class),
                        e.get(1, Long.class).doubleValue()
                );
            });
        }

        return result;
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
