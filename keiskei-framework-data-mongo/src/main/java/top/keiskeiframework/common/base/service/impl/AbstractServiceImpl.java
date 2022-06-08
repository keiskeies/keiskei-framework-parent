package top.keiskeiframework.common.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.IPageResult;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.BaseEntity;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.common.base.util.BaseRequestUtils;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.vo.PageResult;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/16 15:04
 */
public class AbstractServiceImpl<T extends IBaseEntity<String>> implements IBaseService<T, String> {
    @Autowired
    protected MongoRepository<T, String> mongoRepository;
    @Autowired
    protected MongoTemplate mongoTemplate;
    private Class<T> tClass;

    public AbstractServiceImpl() {
        ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
        Type[] types = parameterizedType.getActualTypeArguments();
        this.tClass = (Class<T>) types[0];
    }


    @Override
    public IPageResult<T> page(BaseRequestVO<T, String> request, BasePageVO page) {
        PageResult<T> result = new PageResult<>(page);

        Query query = BaseRequestUtils.getQuery(request, tClass);
        long total = mongoTemplate.count(query, tClass);
        result.setTotal(total);
        if (0 == total) {
            return result;
        }
        Pageable pageable = new PageResult<>(page);
        List<T> tList = mongoTemplate.find(query.with(pageable), tClass);
        result.setData(tList);

        return result;
    }

    @Override
    public T findOneById(Serializable id) {
        return mongoRepository.findById((String) id).orElse(null);
    }

    @Override
    public T findOneByColumn(String column, Serializable value) {
        Query query = new Query();
        query.addCriteria(Criteria.where(column).is(value));
        return mongoTemplate.findOne(query, tClass);
    }

    @Override
    public T findOneByCondition(BaseRequestVO<T, String> request) {
        Query query = BaseRequestUtils.getQuery(request, tClass);
        return mongoTemplate.findOne(query, tClass);
    }

    @Override
    public T saveOne(T t) {
        return mongoRepository.save(t);
    }

    @Override
    public T updateOne(T t) {
        return mongoRepository.save(t);
    }

    @Override
    public List<T> findList() {
        return mongoRepository.findAll();
    }

    @Override
    public List<T> findListByColumn(String column, Serializable value) {
        Query query = new Query();
        query.addCriteria(Criteria.where(column).is(value));
        return mongoTemplate.find(query, tClass);
    }

    @Override
    public List<T> findListByCondition(BaseRequestVO<T, String> request) {
        Query query = BaseRequestUtils.getQuery(request, tClass);
        return mongoTemplate.find(query, tClass);
    }

    @Override
    public List<T> saveList(List<T> ts) {
        return mongoRepository.saveAll(ts);
    }

    @Override
    public List<T> updateList(List<T> ts) {
        return mongoRepository.saveAll(ts);
    }

    @Override
    public boolean updateListByCondition(List<QueryConditionVO> conditions, T t) {
        return false;
    }

    @Override
    public boolean deleteOneById(String s) {
        mongoRepository.deleteById(s);
        return true;
    }

    @Override
    public boolean deleteListByIds(Collection<String> strings) {
        List<T> ts = strings.stream().map(e -> {
            try {
                T t = tClass.newInstance();
                t.setId(e);
                return t;
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }).collect(Collectors.toList());
        mongoRepository.deleteAll(ts);
        return true;
    }

    @Override
    public boolean deleteListByColumn(String column, Serializable value) {
        Query query = new Query();
        query.addCriteria(Criteria.where(column).is(value));
        mongoTemplate.remove(query);
        return true;
    }

    @Override
    public boolean deleteListByCondition(List<QueryConditionVO> conditions) {
        Query query = BaseRequestUtils.getQuery(conditions, null, tClass);
        mongoTemplate.remove(query, tClass);
        return true;
    }

    @Override
    public Long getCount(BaseRequestVO<T, String> request) {
        Query query = BaseRequestUtils.getQuery(request, tClass);
        return mongoTemplate.count(query, tClass);
    }

    @Override
    public Boolean exist(BaseRequestVO<T, String> request) {
        Query query = BaseRequestUtils.getQuery(request, tClass);
        long total = mongoTemplate.count(query, tClass);
        return total > 0;
    }

    @Override
    public Map<String, Double> getChartOptions(ChartRequestDTO chartRequestDTO) {
        return null;
    }

    public Map<String, Double> getTimeChartOptions(ChartRequestDTO chartRequestDTO) {

        Field field;

        List<T> tList;

        Query query = null;

        // 是否指定新的创建时间字段
        String timeField = chartRequestDTO.getTimeField();
        if (StringUtils.isEmpty(timeField)) {
            timeField = TIME_FIELD_DEFAULT;
        }

        if (CollectionUtils.isEmpty(chartRequestDTO.getConditions())) {
            query = new Query();
            BaseRequestUtils.getQuery(chartRequestDTO.getConditions(), null, tClass);
        }
        if (null == query) {
            query = new Query();
        }

        // 基本时间条件
        Criteria criteria = Criteria.where(timeField).gte(chartRequestDTO.getStart()).lte(chartRequestDTO.getEnd());
        query.addCriteria(criteria);
        org.springframework.data.mongodb.core.query.Field findFields = query.fields();
        findFields.include("id");
        findFields.include(TIME_FIELD_DEFAULT);
        tList = mongoTemplate.find(query, tClass);

        try {
            field = tClass.getDeclaredField(timeField);
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

    public static class
}
