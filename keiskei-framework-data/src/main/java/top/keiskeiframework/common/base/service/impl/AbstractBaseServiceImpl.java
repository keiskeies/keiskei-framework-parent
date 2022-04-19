package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.BaseSortVO;
import top.keiskeiframework.common.base.entity.ListEntity;
import top.keiskeiframework.common.base.injector.dto.ManyToManyResult;
import top.keiskeiframework.common.base.mapper.BaseEntityMapper;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.base.util.BaseRequestUtils;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.util.SpringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 树形实体类基础服务实现
 * </p>
 *
 * @param <T> 实体类
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public abstract class AbstractBaseServiceImpl<T extends ListEntity> extends ServiceImpl<BaseEntityMapper<T>
        , T> implements BaseService<T>, IService<T> {
    protected final static String CACHE_TREE_NAME = "CACHE:TREE";
    protected final static String CACHE_LIST_NAME = "CACHE:LIST";
    protected final static String RESULT_KEY = "RESULT_KEY";
    protected final static String RESULT_VALUE = "RESULT_VALUE";


    @Autowired
    private BaseService<T> baseService;

    protected void getManyToMany(T t) {
        if (null == t) {
            return;
        }
        boolean hasManyToMany = false;

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ManyToMany oneToMany = field.getAnnotation(ManyToMany.class);
            if (null == oneToMany) {
                continue;
            }
            JoinTable joinTable = field.getAnnotation(JoinTable.class);
            if (null != joinTable) {
                hasManyToMany = true;
                break;
            }
        }
        if (!hasManyToMany) {
            return;
        }

        List<ManyToManyResult> manyTomMany = this.baseMapper.findManyToMany(t);
        if (!CollectionUtils.isEmpty(manyTomMany)) {
            Map<String, List<ManyToManyResult>> manyTomManyMap =
                    manyTomMany.stream().collect(Collectors.groupingBy(ManyToManyResult::getEntity));
            for (Map.Entry<String, List<ManyToManyResult>> entry : manyTomManyMap.entrySet()) {
                BaseService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entry.getKey()) + "ServiceImpl", BaseService.class);
                List<ListEntity> joinResult = new ArrayList<>(entry.getValue().size());

                String fieldName = null;
                for (ManyToManyResult manyToManyResult : entry.getValue()) {
                    ListEntity e = baseService.getById(manyToManyResult.getSecondId());
                    if (null == e) {
                        continue;
                    }
                    joinResult.add(e);
                    fieldName = manyToManyResult.getFieldName();
                }

                try {
                    assert fieldName != null;
                    Field field = t.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(t, joinResult);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    protected void saveManyToMany(T t) {
        if (null == t) {
            return;
        }
        boolean hasManyToMany = false;

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ManyToMany oneToMany = field.getAnnotation(ManyToMany.class);
            if (null == oneToMany) {
                continue;
            }
            JoinTable joinTable = field.getAnnotation(JoinTable.class);
            if (null != joinTable) {
                hasManyToMany = true;
                break;
            }

        }
        if (hasManyToMany) {
            this.baseMapper.saveManyToMany(t);
        }
    }

    protected void updateManyToMany(T t) {
        if (null == t) {
            return;
        }
        boolean hasManyToMany = false;

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ManyToMany oneToMany = field.getAnnotation(ManyToMany.class);
            if (null == oneToMany) {
                continue;
            }
            JoinTable joinTable = field.getAnnotation(JoinTable.class);
            if (null != joinTable) {
                hasManyToMany = true;
                break;
            }

        }
        if (hasManyToMany) {
            this.baseMapper.deleteManyToMany(t.getId());
            this.baseMapper.saveManyToMany(t);
        }
    }

    protected void deleteManyToMany(T t) {
        if (null == t) {
            return;
        }
        boolean hasManyToMany = false;

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ManyToMany oneToMany = field.getAnnotation(ManyToMany.class);
            if (null == oneToMany) {
                continue;
            }
            JoinTable joinTable = field.getAnnotation(JoinTable.class);
            if (null != joinTable) {
                hasManyToMany = true;
                break;
            }

        }
        if (hasManyToMany) {
            this.baseMapper.deleteManyToMany(t.getId());
        }
    }

    protected void getOneToMany(T t) {
        if (null == t) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (null == oneToMany) {
                continue;
            }
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null == joinColumn) {
                continue;
            }
            Type type = field.getGenericType();
            ParameterizedType pt = (ParameterizedType) type;
            Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

            Long id = t.getId();

            String entity = clz.getSimpleName();
            String firstId = BeanUtils.humpToUnderline(joinColumn.name());
            BaseService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", BaseService.class);
            List<?> joinResult = baseService.findAllByColumn(firstId, id);

            if (!CollectionUtils.isEmpty(joinResult)) {
                field.setAccessible(true);
                try {
                    field.set(t, joinResult);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void saveOneToMany(T t) {
        if (null == t) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (null == oneToMany) {
                continue;
            }
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null == joinColumn) {
                continue;
            }
            try {
                Object joDataObj = field.get(t);
                if (null == joDataObj) {
                    continue;
                }
                Collection joinData = (Collection<?>) joDataObj;
                if (CollectionUtils.isEmpty(joinData)) {
                    continue;
                }

                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

                Long id = t.getId();
                Field joinField = clz.getDeclaredField(joinColumn.name());
                joinField.setAccessible(true);
                for (Object joinDatum : joinData) {
                    joinField.setLong(joinDatum, id);
                }

                String entity = clz.getSimpleName();
                BaseService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", BaseService.class);
                baseService.saveBatch(joinData);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    protected void updateOneToMany(T t) {
        if (null == t) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (null == oneToMany) {
                continue;
            }
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null == joinColumn) {
                continue;
            }
            try {
                Object joDataObj = field.get(t);
                if (null == joDataObj) {
                    continue;
                }
                Collection joinData = (Collection<?>) joDataObj;
                if (CollectionUtils.isEmpty(joinData)) {
                    continue;
                }

                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

                Long id = t.getId();
                Field joinField = clz.getDeclaredField(joinColumn.name());
                joinField.setAccessible(true);
                for (Object joinDatum : joinData) {
                    joinField.setLong(joinDatum, id);
                }

                String entity = clz.getSimpleName();
                BaseService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", BaseService.class);

                baseService.deleteAllByColumn(joinColumn.name(), id);
                baseService.saveBatch(joinData);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    protected void deleteOneToMany(T t) {
        if (null == t) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (null == oneToMany) {
                continue;
            }
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null == joinColumn) {
                continue;
            }
            try {
                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

                Long id = t.getId();
                String entity = clz.getSimpleName();
                BaseService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", BaseService.class);

                baseService.deleteAllByColumn(BeanUtils.humpToUnderline(joinColumn.name()), id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void getOneToOne(T t) {
        if (null == t) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            if (null == oneToOne) {
                continue;
            }
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null == joinColumn) {
                continue;
            }
            Type type = field.getGenericType();
            ParameterizedType pt = (ParameterizedType) type;
            Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

            Long id = t.getId();

            String entity = clz.getSimpleName();
            String firstId = BeanUtils.humpToUnderline(joinColumn.name());
            BaseService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", BaseService.class);
            ListEntity joinResult = baseService.findByColumn(firstId, id);

            if (null != joinResult) {
                field.setAccessible(true);
                try {
                    field.set(t, joinResult);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void saveOneToOne(T t) {
        if (null == t) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            if (null == oneToOne) {
                continue;
            }
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null == joinColumn) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object joinDataObj = field.get(t);
                if (null == joinDataObj) {
                    continue;
                }
                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

                Long id = t.getId();

                Field joinField = clz.getDeclaredField(joinColumn.name());
                joinField.setAccessible(true);
                joinField.set(joinDataObj, id);

                String entity = clz.getSimpleName();
                BaseService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", BaseService.class);
                baseService.saveAny(joinDataObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void updateOneToOne(T t) {
        if (null == t) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            if (null == oneToOne) {
                continue;
            }
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null == joinColumn) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object joinDataObj = field.get(t);
                if (null == joinDataObj) {
                    continue;
                }
                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

                Long id = t.getId();

                Field joinField = clz.getDeclaredField(joinColumn.name());
                joinField.setAccessible(true);
                joinField.set(joinDataObj, id);

                String entity = clz.getSimpleName();
                BaseService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", BaseService.class);
                baseService.updateAny(joinDataObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void deleteOneToOne(T t) {
        if (null == t) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            if (null == oneToOne) {
                continue;
            }
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null == joinColumn) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object joinDataObj = field.get(t);
                if (null == joinDataObj) {
                    continue;
                }
                ListEntity joinData = (ListEntity) joinDataObj;
                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];
                String entity = clz.getSimpleName();
                BaseService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", BaseService.class);
                baseService.removeById(joinData.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void getManyToOne(T t) {
        if (null == t) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ManyToOne manyToMany = field.getAnnotation(ManyToOne.class);
            if (null == manyToMany) {
                continue;
            }
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null == joinColumn) {
                continue;
            }

            Class<?> clz = field.getType();
            long value;
            try {
                Field joinField = t.getClass().getDeclaredField(joinColumn.name());
                joinField.setAccessible(true);
                Object v = joinField.get(t);
                if (null != v) {
                    value = Long.parseLong(v.toString());
                } else {
                    continue;
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
                continue;
            }

            String entity = clz.getSimpleName();
            BaseService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", BaseService.class);
            ListEntity joinResult = baseService.getById(value);

            if (null != joinResult) {
                field.setAccessible(true);
                try {
                    field.set(t, joinResult);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void saveManyToOne(T t) {
        if (null == t) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        boolean hasManyToOne = false;
        for (Field field : fields) {
            ManyToOne manyToMany = field.getAnnotation(ManyToOne.class);
            if (null == manyToMany) {
                continue;
            }
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (null == joinColumn) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object joinDataObj = field.get(t);
                if (null == joinDataObj) {
                    continue;
                }
                ListEntity joinData = (ListEntity) joinDataObj;
                Field joinField = t.getClass().getDeclaredField(joinColumn.name());
                joinField.setAccessible(true);
                joinField.set(t, joinData.getId());
                hasManyToOne = true;
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if (hasManyToOne) {
            this.updateById(t);
        }
    }

    protected void updateManyToOne(T t) {
        this.saveManyToOne(t);
    }

    protected void deleteManyToOne(T t){}

    @Override
    public Page<T> page(BaseRequestVO<T> request, BasePageVO<T> page) {
        return this.page(new Page<>(page.getPage(), page.getSize()), BaseRequestUtils.getQueryWrapper(request, getEntityClass()));
    }

    @Override
    public List<T> findAll(BaseRequestVO<T> request) {
        return this.list(BaseRequestUtils.getQueryWrapper(request, getEntityClass()));
    }

    @Override
    public List<T> findAll() {
        return this.list(BaseRequestUtils.getQueryWrapper(null, null));
    }


    @Override
    public List<T> findAll(T t) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>(t);
        return this.list(queryWrapper);
    }

    @Override
    public List<T> findAllByColumn(String column, Serializable value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(column, value);
        return this.list(queryWrapper);
    }

    @Override
    public void deleteAllByColumn(String column, Serializable value) {
        List<T> ts = baseService.findAllByColumn(column, value);
        for (T t : ts) {
            baseService.removeById(t.getId());
        }
    }

    @Override
    public Long count(BaseRequestVO<T> request) {
        return this.count(BaseRequestUtils.getQueryWrapper(request, getEntityClass()));
    }

    @Override
    public T getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public T findByColumn(String column, Serializable value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BeanUtils.humpToUnderline(column), value);
        return this.getOne(queryWrapper);
    }


    @Override
    public boolean save(T t) {
        super.save(t);
        saveManyToMany(t);
        saveOneToMany(t);
        saveManyToOne(t);
        saveOneToOne(t);
        return true;
    }

    @Override
    public T saveAny(Object ojb) {
        try {
            T t = (T) ojb;
            save(t);
            return t;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean updateById(T t) {
        baseMapper.updateById(t);
        updateManyToMany(t);
        updateManyToOne(t);
        updateOneToMany(t);
        updateOneToOne(t);
        return true;
    }

    @Override
    public T updateAny(Object ojb) {
        try {
            T t = (T) ojb;
            updateById(t);
            return t;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void changeSort(BaseSortVO baseSortVO) {
        try {
            Class<T> clazz = getEntityClass();
            Field[] fields = clazz.getDeclaredFields();
            T t1 = clazz.newInstance();
            T t2 = clazz.newInstance();
            boolean hasSort = false;
            for (Field field : fields) {
                if ("id".equals(field.getName())) {
                    field.setAccessible(true);
                    field.set(t1, baseSortVO.getId1());
                    field.set(t2, baseSortVO.getId2());
                } else {
                    OrderBy sortBy = field.getAnnotation(OrderBy.class);
                    if (null != sortBy) {
                        hasSort = true;
                        field.setAccessible(true);
                        field.set(t1, baseSortVO.getSortBy2());
                        field.set(t2, baseSortVO.getSortBy1());
                    }
                }
            }
            if (hasSort) {
                baseService.updateById(t1);
                baseService.updateById(t2);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void validate(T t) {

    }

    @Override
    public boolean removeById(Serializable id) {
        T t = baseService.getById(id);
        if (null != t) {
            deleteManyToMany(t);
            deleteOneToOne(t);
            deleteOneToMany(t);
            deleteManyToOne(t);
            return super.removeById(id);
        } else {
            return true;
        }
    }

    @Override
    public Map<String, Double> getChartOptions(ChartRequestDTO chart) {
        Class<T> tClass = getEntityClass();
        QueryWrapper<T> queryWrapper = BaseRequestUtils.getQueryWrapperByConditions(chart.getConditions(), tClass);
        // 是否指定新的创建时间字段
        String timeField = chart.getTimeField();
        if (StringUtils.isEmpty(timeField)) {
            timeField = "create_time";
        }
        if (null != chart.getStart() && null != chart.getEnd()) {
            queryWrapper.between(BeanUtils.humpToUnderline(timeField), chart.getStart(), chart.getEnd());
        }

        String column = BeanUtils.humpToUnderline(chart.getColumn());
        String resultKey;
        String resultValue;
        if (ColumnType.TIME.equals(chart.getColumnType())) {
            resultKey = getTimeIndex(column, chart.getTimeDelta());
            resultValue = "COUNT(`id`)";
        } else {
            resultKey = column;
            if (CalcType.SUM.equals(chart.getCalcType())) {
                resultValue = "SUM(`" + BeanUtils.humpToUnderline(chart.getSumColumn()) + "`)";
            } else {
                resultValue = "COUNT(`id`)";
            }
        }
        queryWrapper.select(resultKey + " AS " + RESULT_KEY, resultValue + " AS " + RESULT_VALUE);
        queryWrapper.groupBy(RESULT_KEY);
        List<Map<String, Object>> list = this.listMaps(queryWrapper);

        Map<String, Double> result;
        if (CalcType.SUM.equals(chart.getCalcType())) {
            result = list.stream().collect(Collectors.toMap(
                    e -> e.get(RESULT_KEY).toString(),
                    e -> ((BigDecimal)e.get(RESULT_VALUE)).doubleValue()
            ));
        } else if (ColumnType.TIME.equals(chart.getColumnType()) && TimeDeltaEnum.WEEK_DAY.equals(chart.getTimeDelta())) {
            result = list.stream().collect(Collectors.toMap(
                    e -> DateTimeUtils.WEEK_RANGE.get((Integer) e.get(RESULT_KEY)),
                    e -> ((Long)e.get(RESULT_VALUE)).doubleValue()
            ));
        } else {
            result = list.stream().collect(Collectors.toMap(
                    e -> e.get(RESULT_KEY).toString(),
                    e -> ((Long)e.get(RESULT_VALUE)).doubleValue()
            ));
        }
        return result;
    }

    protected static String getTimeIndex(String column, TimeDeltaEnum delta) {
        String index;
        switch (delta) {
            case HOUR: index = "DATE_FORMAT(" + column + ", '%H')"; break;
            case ALL_HOURS: index = "DATE_FORMAT(" + column + ", '%Y-%m-%d %H')"; break;
            case WEEK_DAY: index = "WEEKDAY(" + column + ")"; break;
            case MONTH: index = "DATE_FORMAT(" + column + ", '%m')"; break;
            case MONTH_DAYS: index = "DATE_FORMAT(" + column + ", '%d)"; break;
            case ALL_DAYS: index = "DATE_FORMAT(" + column + ", '%Y-%m-%d')"; break;
            case ALL_MONTHS: index = "DATE_FORMAT(" + column + ", '%Y-%m')"; break;
            case QUARTER: index = "QUARTER(" + column + ")"; break;
            case ALL_QUARTERS: index = "CONCAT(DATE_FORMAT(" + column + ", '%Y'), '-', QUARTER("+column+")"; break;
            case YEAR: index = "DATE_FORMAT(" + column + ", '%Y')"; break;
            default: throw new UnsupportedTemporalTypeException("时间间隔类型错误: " + delta);
        }
        return index;
    }


}
