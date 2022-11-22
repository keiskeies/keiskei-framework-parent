package top.keiskeiframework.common.base.mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.base.entity.IListEntity;
import top.keiskeiframework.common.base.entity.IMiddleEntity;
import top.keiskeiframework.common.base.mp.annotation.MpManyToMany;
import top.keiskeiframework.common.base.mp.annotation.MpManyToOne;
import top.keiskeiframework.common.base.mp.annotation.MpOneToMany;
import top.keiskeiframework.common.base.mp.annotation.MpOneToOne;
import top.keiskeiframework.common.base.mp.service.IMiddleService;
import top.keiskeiframework.common.base.mp.util.MpRequestUtils;
import top.keiskeiframework.common.base.mp.vo.MpPageResult;
import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.util.ColumnFunctionUtils;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.util.SpringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>
 * 树形实体类基础服务实现
 * </p>
 *
 * @param <T>  .
 * @param <ID> .
 * @author JamesChen right_way@foxmail.com
 * @since 2020年12月9日20:03:04
 */
@Slf4j
public abstract class AbstractMpServiceImpl
        <T extends IBaseEntity<ID>, ID extends Serializable, M extends BaseMapper<T>>
        extends ServiceImpl<M, T>
        implements IBaseService<T, ID>, IService<T> {

    protected final static String RESULT_KEY = "RESULT_KEY";
    protected final static String RESULT_VALUE = "RESULT_VALUE";

    private static final Map<String, Boolean> MANY_TO_MANY_FLAG = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> MANY_TO_ONE_FLAG = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> ONE_TO_MANY_FLAG = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> ONE_TO_ONE_FLAG = new ConcurrentHashMap<>();


    @Override
    public T findOneById(Serializable id) {
        return this.getById(id);
    }

    @Override
    public T saveOne(T t) {
        this.save(t);
        return t;
    }

    @Override
    public T updateOne(T t) {
        this.updateById(t);
        return t;
    }

    @Override
    public List<T> findList() {
        return this.list();
    }

    @Override
    public List<T> saveList(List<T> ts) {
        this.saveBatch(ts);
        return ts;
    }

    @Override
    public List<T> updateList(List<T> ts) {
        this.updateBatchById(ts);
        return ts;
    }

    @Override
    public boolean deleteOneById(ID id) {
        return this.removeById(id);
    }

    @Override
    public boolean deleteListByIds(Collection<ID> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public MpPageResult<T> page(BaseRequestVO<T, ID> request, BasePageVO page) {
        if (null == page) {
            page = new BasePageVO();
        }
        if (page.getAll()) {
            List<T> tList = this.findListByCondition(request);
            if (CollectionUtils.isEmpty(tList)) {
                return new MpPageResult<>(page.getPage(), page.getSize(), page.getOffset());
            } else {
                int total = tList.size();
                MpPageResult<T> pageResult = new MpPageResult<>(page.getPage(), total, 0);
                pageResult.setRecords(tList);
                return pageResult;
            }
        }
        return this.page(new MpPageResult<>(page), MpRequestUtils.getQueryWrapper(request, getEntityClass()));
    }

    @Override
    public List<T> findListByCondition(BaseRequestVO<T, ID> request) {
        return this.list(MpRequestUtils.getQueryWrapper(request, getEntityClass()));
    }

    @Override
    public T findOneByCondition(BaseRequestVO<T, ID> request) {
        return this.getOne(MpRequestUtils.getQueryWrapper(request, getEntityClass()));
    }

    @Override
    public boolean updateListByCondition(List<QueryConditionVO> conditions, T t) {
        QueryWrapper<T> queryWrapper = MpRequestUtils.getQueryWrapperByConditions(conditions, getEntityClass());
        return this.update(t, queryWrapper);
    }

    @Override
    public Boolean exist(BaseRequestVO<T, ID> request) {
        return super.count(MpRequestUtils.getQueryWrapperByConditions(request, getEntityClass())) > 0;
    }

    @Override
    public List<T> findListByColumn(SFunction<T, Serializable> column, Serializable value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        String columnStr = ColumnFunctionUtils.getFiledColumnName(column);
        queryWrapper.eq(columnStr, value);
        return super.list(queryWrapper);
    }

    @Override
    public boolean deleteListByColumn(SFunction<T, Serializable> column, Serializable value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        String columnStr = ColumnFunctionUtils.getFiledColumnName(column);
        queryWrapper.eq(columnStr, value);
        return super.remove(queryWrapper);
    }

    @Override
    public boolean deleteListByCondition(List<QueryConditionVO> conditions) {
        QueryWrapper<T> queryWrapper = MpRequestUtils.getQueryWrapperByConditions(conditions, getEntityClass());
        return super.remove(queryWrapper);
    }

    @Override
    public Long getCount(BaseRequestVO<T, ID> request) {
        return this.count(MpRequestUtils.getQueryWrapper(request, getEntityClass()));
    }


    @Override
    public T findOneByColumn(SFunction<T, Serializable> column, Serializable value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        String columnStr = ColumnFunctionUtils.getFiledColumnName(column);
        queryWrapper.eq(columnStr, value);
        return this.getOne(queryWrapper);
    }


    @Override
    public Map<String, Double> getChartOptions(ChartRequestDTO chart) {
        Class<T> tClass = getEntityClass();
        QueryWrapper<T> queryWrapper = MpRequestUtils.getQueryWrapperByConditions(chart.getConditions(), tClass);
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
                    e -> ((BigDecimal) e.get(RESULT_VALUE)).doubleValue()
            ));
        } else if (ColumnType.TIME.equals(chart.getColumnType()) && TimeDeltaEnum.WEEK_DAY.equals(chart.getTimeDelta())) {
            result = list.stream().collect(Collectors.toMap(
                    e -> DateTimeUtils.WEEK_RANGE.get((Integer) e.get(RESULT_KEY)),
                    e -> ((Long) e.get(RESULT_VALUE)).doubleValue()
            ));
        } else {
            result = list.stream().collect(Collectors.toMap(
                    e -> e.get(RESULT_KEY).toString(),
                    e -> ((Long) e.get(RESULT_VALUE)).doubleValue()
            ));
        }
        return result;
    }

    protected static String getTimeIndex(String column, TimeDeltaEnum delta) {
        String index;
        switch (delta) {
            case HOUR:
                index = "DATE_FORMAT(" + column + ", '%H')";
                break;
            case ALL_HOURS:
                index = "DATE_FORMAT(" + column + ", '%Y-%m-%d %H')";
                break;
            case WEEK_DAY:
                index = "WEEKDAY(" + column + ")";
                break;
            case MONTH:
                index = "DATE_FORMAT(" + column + ", '%m')";
                break;
            case MONTH_DAYS:
                index = "DATE_FORMAT(" + column + ", '%d)";
                break;
            case ALL_DAYS:
                index = "DATE_FORMAT(" + column + ", '%Y-%m-%d')";
                break;
            case ALL_MONTHS:
                index = "DATE_FORMAT(" + column + ", '%Y-%m')";
                break;
            case QUARTER:
                index = "QUARTER(" + column + ")";
                break;
            case ALL_QUARTERS:
                index = "CONCAT(DATE_FORMAT(" + column + ", '%Y'), '-', QUARTER(" + column + ")";
                break;
            case YEAR:
                index = "DATE_FORMAT(" + column + ", '%Y')";
                break;
            default:
                throw new UnsupportedTemporalTypeException("时间间隔类型错误: " + delta);
        }
        return index;
    }

    protected boolean hasRelation() {
        Class<T> tClass = getEntityClass();
        String tClassName = tClass.getName();
        if (MANY_TO_MANY_FLAG.containsKey(tClassName) && MANY_TO_MANY_FLAG.get(tClassName)) {
            return true;
        }
        if (MANY_TO_ONE_FLAG.containsKey(tClassName) && MANY_TO_ONE_FLAG.get(tClassName)) {
            return true;
        }
        if (ONE_TO_MANY_FLAG.containsKey(tClassName) && ONE_TO_MANY_FLAG.get(tClassName)) {
            return true;
        }
        if (ONE_TO_ONE_FLAG.containsKey(tClassName) && ONE_TO_ONE_FLAG.get(tClassName)) {
            return true;
        }
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            if (null != field.getAnnotation(MpManyToMany.class)) {
                if (!MANY_TO_MANY_FLAG.containsKey(tClassName)) {
                    MANY_TO_MANY_FLAG.put(tClassName, Boolean.TRUE);
                }
                return true;
            }
            if (null != field.getAnnotation(MpManyToOne.class)) {
                if (!MANY_TO_ONE_FLAG.containsKey(tClassName)) {
                    MANY_TO_ONE_FLAG.put(tClassName, Boolean.TRUE);
                }
                return true;
            }
            if (null != field.getAnnotation(MpOneToMany.class)) {
                if (!ONE_TO_MANY_FLAG.containsKey(tClassName)) {
                    ONE_TO_MANY_FLAG.put(tClassName, Boolean.TRUE);
                }
                return true;
            }
            if (null != field.getAnnotation(MpOneToOne.class)) {
                if (!ONE_TO_ONE_FLAG.containsKey(tClassName)) {
                    ONE_TO_ONE_FLAG.put(tClassName, Boolean.TRUE);
                }
                return true;
            }
        }
        if (!MANY_TO_MANY_FLAG.containsKey(tClassName)) {
            MANY_TO_MANY_FLAG.put(tClassName, Boolean.FALSE);
        }
        if (!MANY_TO_ONE_FLAG.containsKey(tClassName)) {
            MANY_TO_ONE_FLAG.put(tClassName, Boolean.FALSE);
        }
        if (!ONE_TO_MANY_FLAG.containsKey(tClassName)) {
            ONE_TO_MANY_FLAG.put(tClassName, Boolean.FALSE);
        }
        if (!ONE_TO_ONE_FLAG.containsKey(tClassName)) {
            ONE_TO_ONE_FLAG.put(tClassName, Boolean.FALSE);
        }
        return false;
    }

    protected void getManyToMany(T t) {
        if (null == t) {
            return;
        }
        String tClassName = t.getClass().getName();
        if (MANY_TO_MANY_FLAG.containsKey(tClassName) && !MANY_TO_MANY_FLAG.get(tClassName)) {
            return;
        }
        boolean hasManyToMany = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            MpManyToMany mpManyToMany = field.getAnnotation(MpManyToMany.class);
            if (null != mpManyToMany) {
                hasManyToMany = true;
                IMiddleService<?, ?, ?> middleService = SpringUtils.getBean(
                        StringUtils.firstToLowerCase(mpManyToMany.middleClass().getSimpleName()) + "ServiceImpl",
                        IMiddleService.class);
                IService<?> baseService = SpringUtils.getBean(
                        StringUtils.firstToLowerCase(mpManyToMany.targetClass().getSimpleName()) + "ServiceImpl",
                        IService.class
                );

                List<Object> joinData = new ArrayList<>();
                if (mpManyToMany.id1()) {
                    List<? extends IMiddleEntity<?, ?>> middleData = middleService.getById1(t.getId());
                    if (!CollectionUtils.isEmpty(middleData)) {
                        for (IMiddleEntity<?, ?> iMiddleEntity : middleData) {
                            joinData.add(baseService.getById((Serializable) iMiddleEntity.getId2()));
                        }
                    }
                } else {
                    List<? extends IMiddleEntity<?, ?>> middleData = middleService.getById2(t.getId());
                    if (!CollectionUtils.isEmpty(middleData)) {
                        for (IMiddleEntity<?, ?> iMiddleEntity : middleData) {
                            joinData.add(baseService.getById((Serializable) iMiddleEntity.getId1()));
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(joinData)) {
                    field.setAccessible(true);
                    try {
                        field.set(t, joinData);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        if (!MANY_TO_MANY_FLAG.containsKey(tClassName)) {
            MANY_TO_MANY_FLAG.put(tClassName, hasManyToMany);
        }
    }

    protected void saveManyToMany(T t) {
        if (null == t) {
            return;
        }
        String tClassName = t.getClass().getName();
        if (MANY_TO_MANY_FLAG.containsKey(tClassName) && !MANY_TO_MANY_FLAG.get(tClassName)) {
            return;
        }
        boolean hasManyToMany = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            MpManyToMany mpManyToMany = field.getAnnotation(MpManyToMany.class);
            if (null != mpManyToMany) {
                hasManyToMany = true;
                try {
                    field.setAccessible(true);
                    Object joDataObj = field.get(t);
                    if (null == joDataObj) {
                        continue;
                    }
                    Collection<IBaseEntity<?>> joinData = (Collection<IBaseEntity<?>>) joDataObj;
                    if (!CollectionUtils.isEmpty(joinData)) {

                        IMiddleService middleService = SpringUtils.getBean(
                                StringUtils.firstToLowerCase(mpManyToMany.middleClass().getSimpleName()) + "ServiceImpl",
                                IMiddleService.class);
                        IBaseService baseService = SpringUtils.getBean(
                                StringUtils.firstToLowerCase(mpManyToMany.targetClass().getSimpleName()) + "ServiceImpl",
                                IBaseService.class);

                        for (IBaseEntity<?> joinObj : joinData) {
                            if (null == joinObj.getId()) {
                                baseService.saveOne(joinObj);
                            }
                        }

                        List<IMiddleEntity<?, ?>> middleEntities = new ArrayList<>(joinData.size());
                        for (IBaseEntity<?> baseEntity : joinData) {
                            try {
                                IMiddleEntity middleEntity = mpManyToMany.middleClass().newInstance();
                                if (mpManyToMany.id1()) {
                                    middleEntity.setId1(t.getId());
                                    middleEntity.setId2(baseEntity.getId());
                                } else {
                                    middleEntity.setId1(baseEntity.getId());
                                    middleEntity.setId2(t.getId());
                                }
                                middleEntities.add(middleEntity);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            }
                        }
                        if (mpManyToMany.id1()) {
                            middleService.saveOrUpdateById1(middleEntities);
                        } else {
                            middleService.saveOrUpdateById2(middleEntities);
                        }

                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!MANY_TO_MANY_FLAG.containsKey(tClassName)) {
            MANY_TO_MANY_FLAG.put(tClassName, hasManyToMany);
        }
    }

    protected void updateManyToMany(T t) {
        if (null == t) {
            return;
        }
        String tClassName = t.getClass().getName();
        if (MANY_TO_MANY_FLAG.containsKey(tClassName) && !MANY_TO_MANY_FLAG.get(tClassName)) {
            return;
        }
        boolean hasManyToMany = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            MpManyToMany mpManyToMany = field.getAnnotation(MpManyToMany.class);
            if (null != mpManyToMany) {
                hasManyToMany = true;
                break;
            }
        }
        if (!MANY_TO_MANY_FLAG.containsKey(tClassName)) {
            MANY_TO_MANY_FLAG.put(tClassName, hasManyToMany);
        }
        if (hasManyToMany) {
            this.deleteManyToMany(t);
            this.saveManyToMany(t);
        }

    }

    protected void deleteManyToMany(T t) {
        if (null == t) {
            return;
        }
        String tClassName = t.getClass().getName();
        if (MANY_TO_MANY_FLAG.containsKey(tClassName) && !MANY_TO_MANY_FLAG.get(tClassName)) {
            return;
        }
        boolean hasManyToOne = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            MpManyToMany mpManyToMany = field.getAnnotation(MpManyToMany.class);
            if (null != mpManyToMany) {
                hasManyToOne = true;
                field.setAccessible(true);
                Object joDataObj = null;
                try {
                    joDataObj = field.get(t);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (null == joDataObj) {
                    continue;
                }
                Class<? extends IMiddleEntity<?, ?>> middleClass = mpManyToMany.middleClass();
                String entity = middleClass.getSimpleName();
                IMiddleService middleService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl",
                        IMiddleService.class);
                if (mpManyToMany.id1()) {
                    middleService.removeById1(t.getId());
                } else {
                    middleService.removeById2(t.getId());
                }
            }
        }
        if (!MANY_TO_MANY_FLAG.containsKey(tClassName)) {
            MANY_TO_MANY_FLAG.put(tClassName, hasManyToOne);
        }
    }

    protected void getOneToMany(T t) {
        if (null == t) {
            return;
        }
        String tClassName = t.getClass().getName();
        if (ONE_TO_MANY_FLAG.containsKey(tClassName) && !ONE_TO_MANY_FLAG.get(tClassName)) {
            return;
        }
        boolean hasOneToMany = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            MpOneToMany mpOneToMany = field.getAnnotation(MpOneToMany.class);
            if (null == mpOneToMany) {
                continue;
            }
            hasOneToMany = true;
            Type type = field.getGenericType();
            ParameterizedType pt = (ParameterizedType) type;
            Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

            ID id = t.getId();

            String entity = clz.getSimpleName();
            IBaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl",
                    IBaseService.class);
            List<?> joinResult = baseService.findListByColumn(e -> BeanUtils.humpToUnderline(mpOneToMany.filedName()), id);

            if (!CollectionUtils.isEmpty(joinResult)) {
                field.setAccessible(true);
                try {
                    field.set(t, joinResult);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!ONE_TO_MANY_FLAG.containsKey(tClassName)) {
            ONE_TO_MANY_FLAG.put(tClassName, hasOneToMany);
        }
    }

    protected void saveOneToMany(T t) {
        if (null == t) {
            return;
        }
        String tClassName = t.getClass().getName();
        if (ONE_TO_MANY_FLAG.containsKey(tClassName) && !ONE_TO_MANY_FLAG.get(tClassName)) {
            return;
        }
        boolean hasOneToMany = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            MpOneToMany mpOneToMany = field.getAnnotation(MpOneToMany.class);
            if (null == mpOneToMany) {
                continue;
            }
            hasOneToMany = true;
            try {
                field.setAccessible(true);
                Object joDataObj = field.get(t);
                if (null == joDataObj) {
                    continue;
                }
                Collection<?> joinData = (Collection<?>) joDataObj;
                if (CollectionUtils.isEmpty(joinData)) {
                    continue;
                }

                Class<?> joinClass = mpOneToMany.targetClass();
                Field joinField = joinClass.getDeclaredField(mpOneToMany.filedName());
                joinField.setAccessible(true);
                for (Object joinDatum : joinData) {
                    joinField.set(joinDatum, t.getId());
                }

                IService baseService = SpringUtils.getBean(
                        StringUtils.firstToLowerCase(joinClass.getSimpleName()) + "ServiceImpl",
                        IService.class);
                baseService.saveBatch(joinData);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if (!ONE_TO_MANY_FLAG.containsKey(tClassName)) {
            ONE_TO_MANY_FLAG.put(tClassName, hasOneToMany);
        }
    }

    protected void updateOneToMany(T t) {
        if (null == t) {
            return;
        }
        String tClassName = t.getClass().getName();
        if (ONE_TO_MANY_FLAG.containsKey(tClassName) && !ONE_TO_MANY_FLAG.get(tClassName)) {
            return;
        }
        boolean hasOneToMany = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            MpOneToMany mpOneToMany = field.getAnnotation(MpOneToMany.class);
            if (null == mpOneToMany) {
                continue;
            }
            hasOneToMany = true;
            try {
                Object joDataObj = field.get(t);
                if (null == joDataObj) {
                    continue;
                }
                Collection joinData = (Collection) joDataObj;
                if (CollectionUtils.isEmpty(joinData)) {
                    continue;
                }

                Class<?> joinClass = mpOneToMany.targetClass();

                ID id = t.getId();
                Field joinField = joinClass.getDeclaredField(mpOneToMany.filedName());
                joinField.setAccessible(true);
                for (Object joinDatum : joinData) {
                    joinField.set(joinDatum, id);
                }

                IService baseService = SpringUtils.getBean(
                        StringUtils.firstToLowerCase(joinClass.getSimpleName()) + "ServiceImpl",
                        IService.class);
                baseService.removeByMap(Collections.singletonMap(mpOneToMany.filedName(), id));
                baseService.saveBatch(joinData);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if (!ONE_TO_MANY_FLAG.containsKey(tClassName)) {
            ONE_TO_MANY_FLAG.put(tClassName, hasOneToMany);
        }
    }

    protected void deleteOneToMany(T t) {
        if (null == t) {
            return;
        }
        String tClassName = t.getClass().getName();
        if (ONE_TO_MANY_FLAG.containsKey(tClassName) && !ONE_TO_MANY_FLAG.get(tClassName)) {
            return;
        }
        boolean hasOneToMany = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            MpOneToMany mpOneToMany = field.getAnnotation(MpOneToMany.class);
            if (null == mpOneToMany) {
                continue;
            }
            hasOneToMany = true;
            try {
                ID id = t.getId();
                IBaseService baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(mpOneToMany.targetClass().getSimpleName()) + "ServiceImpl", IBaseService.class);
                baseService.deleteListByColumn(e -> BeanUtils.humpToUnderline(mpOneToMany.filedName()), id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!ONE_TO_MANY_FLAG.containsKey(tClassName)) {
            ONE_TO_MANY_FLAG.put(tClassName, hasOneToMany);
        }
    }

    protected void getOneToOne(T t) {
        if (null == t) {
            return;
        }
        String tClassName = t.getClass().getName();
        if (ONE_TO_ONE_FLAG.containsKey(tClassName) && !ONE_TO_ONE_FLAG.get(tClassName)) {
            return;
        }
        boolean hasOneToOne = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            MpOneToOne mpOneToOne = field.getAnnotation(MpOneToOne.class);
            if (null == mpOneToOne) {
                continue;
            }
            hasOneToOne = true;
            Type type = field.getGenericType();
            ParameterizedType pt = (ParameterizedType) type;
            Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

            ID id = t.getId();

            String entity = clz.getSimpleName();
            IBaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", IBaseService.class);
            IBaseEntity<?> joinResult = baseService.findOneByColumn(e -> BeanUtils.humpToUnderline(mpOneToOne.filedName()), id);

            if (null != joinResult) {
                field.setAccessible(true);
                try {
                    field.set(t, joinResult);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!ONE_TO_ONE_FLAG.containsKey(tClassName)) {
            ONE_TO_ONE_FLAG.put(tClassName, hasOneToOne);
        }
    }

    protected void saveOneToOne(T t) {
        if (null == t) {
            return;
        }
        String tClassName = t.getClass().getName();
        if (ONE_TO_ONE_FLAG.containsKey(tClassName) && !ONE_TO_ONE_FLAG.get(tClassName)) {
            return;
        }
        boolean hasOneToOne = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            MpOneToOne mpOneToOne = field.getAnnotation(MpOneToOne.class);
            if (null == mpOneToOne) {
                continue;
            }
            hasOneToOne = true;
            try {
                field.setAccessible(true);
                Object joinDataObj = field.get(t);
                if (null == joinDataObj) {
                    continue;
                }
                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

                ID id = t.getId();

                Field joinField = clz.getDeclaredField(mpOneToOne.filedName());
                joinField.setAccessible(true);
                joinField.set(joinDataObj, id);

                String entity = clz.getSimpleName();
                IService baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", IService.class);
                baseService.save(joinDataObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!ONE_TO_ONE_FLAG.containsKey(tClassName)) {
            ONE_TO_ONE_FLAG.put(tClassName, hasOneToOne);
        }
    }

    protected void updateOneToOne(T t) {
        if (null == t) {
            return;
        }
        String tClassName = t.getClass().getName();
        if (ONE_TO_ONE_FLAG.containsKey(tClassName) && !ONE_TO_ONE_FLAG.get(tClassName)) {
            return;
        }
        boolean hasOneToOne = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            MpOneToOne mpOneToOne = field.getAnnotation(MpOneToOne.class);
            if (null == mpOneToOne) {
                continue;
            }
            hasOneToOne = true;
            try {
                field.setAccessible(true);
                Object joinDataObj = field.get(t);
                if (null == joinDataObj) {
                    continue;
                }
                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

                ID id = t.getId();

                Field joinField = clz.getDeclaredField(mpOneToOne.filedName());
                joinField.setAccessible(true);
                joinField.set(joinDataObj, id);

                String entity = clz.getSimpleName();
                IService baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", IService.class);
                baseService.updateById(field.get(t));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!ONE_TO_ONE_FLAG.containsKey(tClassName)) {
            ONE_TO_ONE_FLAG.put(tClassName, hasOneToOne);
        }
    }

    protected void deleteOneToOne(T t) {
        if (null == t) {
            return;
        }
        String tClassName = t.getClass().getName();
        if (ONE_TO_ONE_FLAG.containsKey(tClassName) && !ONE_TO_ONE_FLAG.get(tClassName)) {
            return;
        }
        boolean hasOneToOne = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            MpOneToOne mpOneToOne = field.getAnnotation(MpOneToOne.class);
            if (null == mpOneToOne) {
                continue;
            }
            hasOneToOne = true;
            try {
                field.setAccessible(true);
                Object joinDataObj = field.get(t);
                if (null == joinDataObj) {
                    continue;
                }
                IBaseEntity<?> joinData = (IBaseEntity<?>) joinDataObj;
                Type type = field.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];
                String entity = clz.getSimpleName();
                IService<?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", IService.class);
                baseService.removeById(joinData.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!ONE_TO_ONE_FLAG.containsKey(tClassName)) {
            ONE_TO_ONE_FLAG.put(tClassName, hasOneToOne);
        }
    }

    protected void getManyToOne(T t) {
        if (null == t) {
            return;
        }
        String tClassName = t.getClass().getName();
        if (MANY_TO_ONE_FLAG.containsKey(tClassName) && !MANY_TO_ONE_FLAG.get(tClassName)) {
            return;
        }
        boolean hasManyToOne = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            MpManyToOne manyToMany = field.getAnnotation(MpManyToOne.class);
            if (null == manyToMany) {
                continue;
            }
            hasManyToOne = true;
            Class<?> clz = field.getType();
            int value;
            try {
                Field joinField = t.getClass().getDeclaredField(manyToMany.filedName());
                joinField.setAccessible(true);
                Object v = joinField.get(t);
                if (null != v) {
                    value = Integer.parseInt(v.toString());
                } else {
                    continue;
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
                continue;
            }

            String entity = clz.getSimpleName();
            IBaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", IBaseService.class);
            IBaseEntity<?> joinResult = baseService.findOneById(value);

            if (null != joinResult) {
                field.setAccessible(true);
                try {
                    field.set(t, joinResult);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!MANY_TO_ONE_FLAG.containsKey(tClassName)) {
            MANY_TO_ONE_FLAG.put(tClassName, hasManyToOne);
        }
    }

    protected void saveManyToOne(T t) {
        if (null == t) {
            return;
        }
        String tClassName = t.getClass().getName();
        if (MANY_TO_ONE_FLAG.containsKey(tClassName) && !MANY_TO_ONE_FLAG.get(tClassName)) {
            return;
        }
        boolean hasManyToOne = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            MpManyToOne manyToMany = field.getAnnotation(MpManyToOne.class);
            if (null == manyToMany) {
                continue;
            }
            hasManyToOne = true;
            try {
                field.setAccessible(true);
                Object joinDataObj = field.get(t);
                if (null == joinDataObj) {
                    continue;
                }
                IBaseEntity<?> joinData = (IListEntity<?>) joinDataObj;
                Field joinField = t.getClass().getDeclaredField(manyToMany.filedName());
                joinField.setAccessible(true);
                joinField.set(t, joinData.getId());
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if (!MANY_TO_ONE_FLAG.containsKey(tClassName)) {
            MANY_TO_ONE_FLAG.put(tClassName, hasManyToOne);
        }
    }

    protected void updateManyToOne(T t) {
        this.saveManyToOne(t);
    }

    protected void deleteManyToOne(T t) {
    }


}
