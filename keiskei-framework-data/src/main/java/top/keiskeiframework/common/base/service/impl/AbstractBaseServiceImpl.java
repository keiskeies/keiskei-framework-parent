package top.keiskeiframework.common.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
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
import top.keiskeiframework.common.util.DateTimeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
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
public abstract class AbstractBaseServiceImpl<T extends ListEntity> implements BaseService<T> {

    @Autowired
    protected MongoRepository<T, String> mongoRepository;
    @Autowired
    protected MongoTemplate mongoTemplate;
    private final static String TIME_FIELD_DEFAULT = "createTime";
    private final static String TIME_FIELD_UNDEFINED = "undefined";

    protected Class<T> getTClass() {
        ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
        Type[] types = parameterizedType.getActualTypeArguments();
        return (Class<T>) types[0];
    }


    @Override
    public Page<T> page(Query q, Pageable p) {
        Class<T> tClass = getTClass();
        long count = mongoTemplate.count(q, tClass);
        List<T> tList = mongoTemplate.find(q.with(p), tClass);

        return new PageImpl<>(tList, p, count);
    }

    @Override
    public Page<T> page(Example<T> e, Pageable p) {
        return mongoRepository.findAll(e, p);
    }


    @Override
    public List<T> findAll() {
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
        return mongoRepository.findAll(Sort.by(orders));
    }

    @Override
    public List<T> findAll(Query q) {
        Class<T> tClass = getTClass();
        return mongoTemplate.find(q, tClass);
    }


    @Override
    public List<T> findAll(BaseRequest<T> request) {
        Class<T> tClass = getTClass();
        Query q = request.getQuery(tClass);
        return mongoTemplate.find(q, tClass);
    }

    @Override
    public List<T> findAll(Query q, Sort sort) {
        Class<T> tClass = getTClass();
        return mongoTemplate.find(q.with(sort), tClass);
    }

    @Override
    public List<T> findAll(Example<T> e) {
        return mongoRepository.findAll(e);
    }

    @Override
    public List<T> findAll(Example<T> e, Sort sort) {
        return mongoRepository.findAll(e, sort);
    }

    @Override
    public Page<T> page(BaseRequest<T> request) {
        Class<T> tClass = getTClass();
        Query q = request.getQuery(tClass);
        Pageable p = request.getPageable(tClass);
        long count = mongoTemplate.count(q, tClass);
        List<T> tList = mongoTemplate.find(q.with(p), tClass);

        return new PageImpl<>(tList, p, count);
    }


    @Override
    public T findById(String id) {
        return mongoRepository.findById(id).orElse(null);
    }

    @Override
    public T save(T t) {
        t.setId(null);
        return mongoRepository.save(t);
    }

    @Override
    public List<T> saveAll(List<T> ts) {
        if (CollectionUtils.isEmpty(ts)) {
            return ts;
        }
        ts.forEach(e -> e.setId(null));
        Field[] fields = ts.get(0).getClass().getDeclaredFields();
        long count = mongoRepository.count();
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
        return mongoRepository.saveAll(ts);
    }

    @Override
    public T update(T t) {
        Assert.notNull(t.getId(), BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
        t = mongoRepository.save(t);
        return t;
    }


    @Override
    public void changeSort(BaseSortDTO baseSortDto) {
        try {
            Class<T> clazz = getTClass();
            Field[] fields = clazz.getDeclaredFields();
            T t1 = clazz.newInstance();
            T t2 = clazz.newInstance();
            boolean hasSortField = false;
            for (Field field : fields) {
                if ("id".equals(field.getName())) {
                    field.setAccessible(true);
                    field.set(t1, baseSortDto.getId1());
                    field.set(t2, baseSortDto.getId2());
                } else {
                    SortBy sortBy = field.getAnnotation(SortBy.class);
                    if (null != sortBy) {
                        hasSortField = true;
                        field.setAccessible(true);
                        field.set(t1, baseSortDto.getSortBy2());
                        field.set(t2, baseSortDto.getSortBy1());
                    }
                }
            }
            if (hasSortField) {
                mongoRepository.save(t1);
                mongoRepository.save(t2);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public Map<String, Long> getChartOptions(ChartRequestDTO chartRequestDTO) {

        if (ColumnType.TIME.equals(chartRequestDTO.getColumnType())) {
            return getTimeChartOptions(chartRequestDTO);
        } else {
            return getFieldChartOptions(chartRequestDTO);
        }
    }

    /**
     * 时间格式数据图表
     *
     * @param chartRequestDTO 图表条件
     * @return 。
     */
    public Map<String, Long> getTimeChartOptions(ChartRequestDTO chartRequestDTO) {
        Class<T> clazz = getTClass();

        Field field;

        List<T> tList;

        Query query = new Query();

        // 是否指定新的创建时间字段
        String timeField = chartRequestDTO.getTimeField();
        if (StringUtils.isEmpty(timeField)) {
            timeField = TIME_FIELD_DEFAULT;
        } else if (TIME_FIELD_UNDEFINED.equals(timeField)) {
            return Collections.emptyMap();
        }
        // 基本时间条件
        Criteria criteria = Criteria.where(timeField).gte(chartRequestDTO.getStart()).lte(chartRequestDTO.getEnd());
        query.addCriteria(criteria);
        if (null != chartRequestDTO.getConditions() && !chartRequestDTO.getConditions().isEmpty()) {
            for (Map.Entry<String, List<String>> entry : chartRequestDTO.getConditions().entrySet()) {
                if (StringUtils.isEmpty(entry.getKey()) || CollectionUtils.isEmpty(entry.getValue())) {
                    continue;
                }

                Object[] hasValueValues = entry.getValue().stream()
                        .filter(e -> !StringUtils.isEmpty(e))
                        .toArray();
                if (hasValueValues.length > 0) {
                    Criteria criteriaC = Criteria.where(entry.getKey()).in(hasValueValues);
                    query.addCriteria(criteriaC);
                }
            }
        }
        org.springframework.data.mongodb.core.query.Field findFields = query.fields();
        findFields.include("id");
        findFields.include(TIME_FIELD_DEFAULT);
        tList = mongoTemplate.find(query, clazz);

        try {
            field = clazz.getDeclaredField(timeField);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            return Collections.emptyMap();
        }
        for (T t : tList) {
            try {
                Object obj = field.get(t);
                if (null == obj) {
                    continue;
                }
                t.setIndex(getColumnGroupValue((LocalDateTime) obj, chartRequestDTO.getTimeDelta()));
                t.setIndexNumber(1L);
            } catch (IllegalAccessException error) {
                error.printStackTrace();
            }
        }
        return tList.stream().collect(Collectors.toMap(
                T::getIndex,
                T::getIndexNumber,
                Long::sum
        ));
    }

    /**
     * 字段格式数据图表
     *
     * @param chartRequestDTO 图表条件
     * @return 。
     */
    public Map<String, Long> getFieldChartOptions(ChartRequestDTO chartRequestDTO) {
        Class<T> clazz = getTClass();

        Field field;

        List<T> tList;

        Query query = new Query();

        // 是否指定新的创建时间字段
        String timeField = chartRequestDTO.getTimeField();
        if (StringUtils.isEmpty(timeField)) {
            timeField = TIME_FIELD_DEFAULT;
        }
        if (!TIME_FIELD_UNDEFINED.equals(timeField)) {
            // 基本时间条件
            Criteria criteria = Criteria.where(timeField).gte(chartRequestDTO.getStart()).lte(chartRequestDTO.getEnd());
            query.addCriteria(criteria);
        }
        if (null != chartRequestDTO.getConditions() && !chartRequestDTO.getConditions().isEmpty()) {
            for (Map.Entry<String, List<String>> entry : chartRequestDTO.getConditions().entrySet()) {
                if (StringUtils.isEmpty(entry.getKey()) || CollectionUtils.isEmpty(entry.getValue())) {
                    continue;
                }

                Object[] hasValueValues = entry.getValue().stream()
                        .filter(e -> !StringUtils.isEmpty(e))
                        .toArray();
                if (hasValueValues.length > 0) {
                    Criteria criteria = Criteria.where(entry.getKey()).in(hasValueValues);
                    query.addCriteria(criteria);
                }
            }
        }

        org.springframework.data.mongodb.core.query.Field findFields = query.fields();
        findFields.include("id");
        findFields.include(chartRequestDTO.getColumn());
        tList = mongoTemplate.find(query, clazz);
        try {
            field = clazz.getDeclaredField(chartRequestDTO.getColumn());
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            return Collections.emptyMap();
        }
        for (T t : tList) {
            try {
                Object obj = field.get(t);
                if (null == obj) {
                    continue;
                }

                t.setIndex(obj.toString());
                t.setIndexNumber(1L);
            } catch (IllegalAccessException error) {
                error.printStackTrace();
            }
        }
        return tList.stream().collect(Collectors.toMap(
                T::getIndex,
                T::getIndexNumber,
                Long::sum
        ));
    }

    /**
     * 转换时间格式
     * 获取星期/季度/小时等
     *
     * @param createTime 创建时间
     * @param delta      时间跨度
     * @return 。
     */
    protected String getColumnGroupValue(LocalDateTime createTime, TimeDeltaEnum delta) {
        switch (delta) {
            case WEEK_DAY:
                return DateTimeUtils.WEEK_RANGE.get(createTime.getDayOfWeek().getValue());
            case QUARTER:
                return DateTimeUtils.getQuarter(createTime.getMonthValue()) + "";
            case ALL_QUARTERS:
                return DateTimeUtils.timeToString(createTime, DateTimeUtils.Y) + "-" + DateTimeUtils.getQuarter(createTime.getMonthValue());
            default:
                return DateTimeUtils.timeToString(createTime, delta.getValue());
        }
    }


}
