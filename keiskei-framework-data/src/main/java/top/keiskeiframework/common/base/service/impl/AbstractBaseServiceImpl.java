package top.keiskeiframework.common.base.service.impl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public abstract class AbstractBaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    @Autowired
    protected MongoRepository<T, ObjectId> mongoRepository;
    @Autowired
    protected MongoTemplate mongoTemplate;

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
    public List<T> findAll(Query q) {
        Class<T> tClass = getTClass();
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
        return mongoRepository.findAll(Sort.by(orders));
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
        return mongoRepository.findAll(Example.of(t), Sort.by(orders));
    }

    @Override
    public T getById(ObjectId id) {
        return mongoRepository.findById(id).orElse(null);
    }

    @Override
    public T save(T t) {
        return mongoRepository.save(t);
    }

    @Override
    public List<T> saveAll(List<T> ts) {
        if (CollectionUtils.isEmpty(ts)) {
            return ts;
        }
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
            mongoRepository.save(t1);
            mongoRepository.save(t2);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void deleteById(ObjectId id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public Map<String, Long> getChartOptions(ChartRequestDTO chartRequestDTO) {

        Class<T> clazz = getTClass();
        Query query = new Query();

        // 基本时间条件
        Criteria criteria = Criteria.where("createTime").gte(chartRequestDTO.getStart()).lte(chartRequestDTO.getEnd());
        query.addCriteria(criteria);
        List<T> tList = mongoTemplate.find(query, clazz);
        if (ColumnType.TIME.equals(chartRequestDTO.getColumnType())) {
            tList.forEach(e -> {
                try {
                    e.setIndex(
                            getColumnGroupValue((LocalDateTime) clazz.getField("createTime").get(e), chartRequestDTO.getTimeDelta())
                    );
                    e.setIndexNumber(1L);
                } catch (IllegalAccessException | NoSuchFieldException ignored) {
                }
            });
        } else {
            tList.forEach(e -> {
                try {
                    e.setIndex(
                            clazz.getField(chartRequestDTO.getColumn()).get(e).toString()
                    );
                    e.setIndexNumber(1L);
                } catch (IllegalAccessException | NoSuchFieldException ignored) {
                }
            });
        }
        return tList.stream().collect(Collectors.toMap(
                T::getIndex,
                T::getIndexNumber,
                Long::sum
        ));
    }

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
