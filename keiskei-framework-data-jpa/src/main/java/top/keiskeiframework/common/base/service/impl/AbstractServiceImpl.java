package top.keiskeiframework.common.base.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.IPageResult;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.common.base.util.BaseRequestUtils;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.vo.PageResult;

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
import java.util.function.Function;

/**
 * <p>
 * 基础服务实现
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/21 11:46
 */
@Slf4j
public abstract class AbstractServiceImpl<T extends IBaseEntity<ID>, ID extends Serializable> implements IBaseService<T, ID> {

    @Autowired
    protected JpaRepository<T, ID> jpaRepository;
    @Autowired
    protected JpaSpecificationExecutor<T> jpaSpecificationExecutor;
    @Autowired
    protected EntityManager entityManager;
    @Autowired
    protected IBaseService<T, ID> baseService;
    private Class<T> tClass;
    private Class<ID> idClass;

    public AbstractServiceImpl() {
        ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
        Type[] types = parameterizedType.getActualTypeArguments();
        this.tClass = (Class<T>) types[0];
        this.idClass = (Class<ID>) types[1];
    }

    protected final static String TIME_FIELD_DEFAULT = "createTime";
    protected final static String FIELD_NAME = "fieldName";
    protected final static String FIELD_NUMBER = "fieldNumber";


    @Override
    public IPageResult<T> page(BaseRequestVO<T, ID> request, BasePageVO page) {
        if (null == page) {
            page = new BasePageVO();
        }
        Specification<T> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = BaseRequestUtils.getPredicates(root, criteriaBuilder, request.getConditions());
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        if (page.getAll()) {
            return PageResult.of(jpaSpecificationExecutor.findAll(specification));
        } else {
            Pageable pageable = new PageResult<>(page);
            return PageResult.of(jpaSpecificationExecutor.findAll(specification, pageable));
        }

    }

    @Override
    public T findOneByCondition(BaseRequestVO<T, ID> request) {
        Specification<T> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = BaseRequestUtils.getPredicates(root, criteriaBuilder, request.getConditions());
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return jpaSpecificationExecutor.findOne(specification).orElse(null);
    }

    @Override
    public T findOneById(Serializable id) {
        return jpaRepository.getOne((ID) id);
    }

    @Override
    public T findOneByColumn(Function<T, ?> column, Serializable value) {
        try {
            T t = tClass.newInstance();
//            Field field = tClass.getDeclaredField(column.apply(t));
//            field.setAccessible(true);
//            field.set(t, value);
            column.apply(t);

            Example<T> example = Example.of(t);
            return jpaRepository.findOne(example).orElse(null);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BizException(BizExceptionEnum.ERROR);
        }
    }

    @Override
    public T saveOne(T t) {
        return jpaRepository.save(t);
    }

    @Override
    public T updateOne(T t) {
        return jpaRepository.save(t);
    }

    @Override
    public List<T> findList() {
        return jpaRepository.findAll();
    }

    @Override
    public List<T> findListByCondition(BaseRequestVO<T, ID> request) {
        Specification<T> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = BaseRequestUtils.getPredicates(root, criteriaBuilder, request.getConditions());
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return jpaSpecificationExecutor.findAll(specification);
    }

    @Override
    public boolean updateListByCondition(List<QueryConditionVO> conditions, T t) {
        BaseRequestVO<T, ID> baseRequest = new BaseRequestVO<>();
        baseRequest.setListConditions(conditions);
        List<T> ts = this.findListByCondition(baseRequest);
        for (T tt : ts) {
            BeanUtils.copyPropertiesIgnoreNull(t, tt);
        }
        jpaRepository.saveAll(ts);
        return true;
    }

    @Override
    public List<T> findListByColumn(String column, Serializable value) {
        try {
            T t = tClass.newInstance();
            Field field = tClass.getDeclaredField(column);
            field.setAccessible(true);
            field.set(t, value);

            Example<T> example = Example.of(t);
            return jpaRepository.findAll(example);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BizException(BizExceptionEnum.ERROR);
        } catch (NoSuchFieldException e) {
            throw new BizException(BizExceptionEnum.ERROR.getCode(), "字段" + column + "不存在");
        }
    }

    @Override
    public List<T> saveList(List<T> ts) {
        return jpaRepository.saveAll(ts);
    }

    @Override
    public List<T> updateList(List<T> ts) {
        return jpaRepository.saveAll(ts);
    }

    @Override
    public boolean deleteOneById(ID id) {
        jpaRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean deleteListByIds(Collection<ID> ids) {
        ids.forEach(jpaRepository::deleteById);
        return true;
    }

    @Override
    public boolean deleteListByColumn(String column, Serializable value) {
        List<T> ts = this.findListByColumn(column, value);
        if (CollectionUtils.isEmpty(ts)) {
            return true;
        }
        jpaRepository.deleteAll(ts);
        return true;
    }

    @Override
    public boolean deleteListByCondition(List<QueryConditionVO> conditions) {
        if (CollectionUtils.isEmpty(conditions)) {
            jpaRepository.deleteAll();
            return true;
        } else {
            Specification<T> specification = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = BaseRequestUtils.getPredicates(root, criteriaBuilder, conditions);
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
            List<T> ts = jpaSpecificationExecutor.findAll(specification);
            if (!CollectionUtils.isEmpty(ts)) {
                jpaRepository.deleteAll(ts);
            }
            return true;
        }
    }

    @Override
    public Long getCount(BaseRequestVO<T, ID> request) {
        Specification<T> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = BaseRequestUtils.getPredicates(root, criteriaBuilder, request.getConditions());
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return jpaSpecificationExecutor.count(specification);
    }

    @Override
    public Boolean exist(BaseRequestVO<T, ID> request) {
        return this.getCount(request) > 0;
    }


    @Override
    public Map<String, Double> getChartOptions(ChartRequestDTO chartRequestDTO) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<T> root = query.from(tClass);


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

        if (!CollectionUtils.isEmpty(chartRequestDTO.getConditions())) {

            Expression<?> expression;
            for (QueryConditionVO queryConditionVO : chartRequestDTO.getConditions()) {

                Object[] hasValueValues = queryConditionVO.getV().stream()
                        .filter(e -> !StringUtils.isEmpty(e))
                        .toArray();
                if (hasValueValues.length > 0) {
                    if (queryConditionVO.getC().contains(".")) {
                        String[] columns = queryConditionVO.getC().split("\\.");
                        Join<T, ?> join = root.join(columns[0], JoinType.INNER);
                        expression = join.get(columns[1]);
                    } else {
                        expression = root.get(queryConditionVO.getC());
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
