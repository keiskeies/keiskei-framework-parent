package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import top.keiskeiframework.common.base.annotation.ManyToMany;
import top.keiskeiframework.common.base.annotation.ManyToOne;
import top.keiskeiframework.common.base.annotation.OneToMany;
import top.keiskeiframework.common.base.annotation.OneToOne;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.*;
import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.common.base.service.IMiddleService;
import top.keiskeiframework.common.base.util.BaseRequestUtils;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.util.SpringUtils;
import top.keiskeiframework.common.vo.PageResult;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
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
public abstract class AbstractServiceImpl
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
    public PageResult<T> page(BaseRequestVO<T, ID> request, BasePageVO page) {
        if (null == page) {
            page = new BasePageVO();
        }
        if (page.getAll()) {
            List<T> tList = this.findListByCondition(request);
            if (CollectionUtils.isEmpty(tList)) {
                return new PageResult<>(page.getPage(), page.getSize(), page.getOffset());
            } else {
                int total = tList.size();
                PageResult<T> pageResult =new PageResult<>(page.getPage(), total, 0);
                pageResult.setRecords(tList);
                return pageResult;
            }
        }
         return this.page(new PageResult<>(page), BaseRequestUtils.getQueryWrapper(request, getEntityClass()));
    }

    @Override
    public List<T> findListByCondition(BaseRequestVO<T, ID> request) {
        return this.list(BaseRequestUtils.getQueryWrapper(request, getEntityClass()));
    }

    @Override
    public T findOneByCondition(BaseRequestVO<T, ID> request) {
        return this.getOne(BaseRequestUtils.getQueryWrapper(request, getEntityClass()));
    }

    @Override
    public boolean updateListByCondition(List<QueryConditionVO> conditions, T t) {
        QueryWrapper<T> queryWrapper = BaseRequestUtils.getQueryWrapperByConditions(conditions, getEntityClass());
        return this.update(t, queryWrapper);
    }

    @Override
    public Boolean exist(BaseRequestVO<T, ID> request) {
        return super.count(BaseRequestUtils.getQueryWrapperByConditions(request, getEntityClass())) > 0;
    }

    @Override
    public List<T> findListByColumn(Function<T, ?> column, Serializable value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq((SFunction<T, ?>) column, value);
        return this.list(queryWrapper);
    }

    @Override
    public boolean deleteListByColumn(Function<T, ?> column, Serializable value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq((SFunction<T, ?>) column, value);
        return super.remove(queryWrapper);
    }

    @Override
    public boolean deleteListByCondition(List<QueryConditionVO> conditions) {
        QueryWrapper<T> queryWrapper = BaseRequestUtils.getQueryWrapperByConditions(conditions, getEntityClass());
        return super.remove(queryWrapper);
    }

    @Override
    public Long getCount(BaseRequestVO<T, ID> request) {
        return this.count(BaseRequestUtils.getQueryWrapper(request, getEntityClass()));
    }


    @Override
    public T findOneByColumn(Function<T, ?> column, Serializable value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq((SFunction<T, ?>) column, value);
        return this.getOne(queryWrapper);
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
            if (null != field.getAnnotation(ManyToMany.class)) {
                if (!MANY_TO_MANY_FLAG.containsKey(tClassName)) {
                    MANY_TO_MANY_FLAG.put(tClassName, Boolean.TRUE);
                }
                return true;
            }
            if (null != field.getAnnotation(ManyToOne.class)) {
                if (!MANY_TO_ONE_FLAG.containsKey(tClassName)) {
                    MANY_TO_ONE_FLAG.put(tClassName, Boolean.TRUE);
                }
                return true;
            }
            if (null != field.getAnnotation(OneToMany.class)) {
                if (!ONE_TO_MANY_FLAG.containsKey(tClassName)) {
                    ONE_TO_MANY_FLAG.put(tClassName, Boolean.TRUE);
                }
                return true;
            }
            if (null != field.getAnnotation(OneToOne.class)) {
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
            ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
            if (null != manyToMany) {
                hasManyToMany = true;
                IMiddleService<?, ?, ?> middleService = SpringUtils.getBean(
                        StringUtils.firstToLowerCase(manyToMany.middleClass().getSimpleName()) + "ServiceImpl",
                        IMiddleService.class);
                IService<?> baseService = SpringUtils.getBean(
                        StringUtils.firstToLowerCase(manyToMany.targetClass().getSimpleName()) + "ServiceImpl",
                        IService.class
                );

                List<Object> joinData = new ArrayList<>();
                if (manyToMany.id1()) {
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
            ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
            if (null != manyToMany) {
                hasManyToMany = true;
                try {
                    field.setAccessible(true);
                    Object joDataObj = field.get(t);
                    if (null == joDataObj) {
                        continue;
                    }
                    Collection<BaseEntity<?>> joinData = (Collection<BaseEntity<?>>) joDataObj;
                    if (!CollectionUtils.isEmpty(joinData)) {

                        IMiddleService middleService = SpringUtils.getBean(
                                StringUtils.firstToLowerCase(manyToMany.middleClass().getSimpleName()) + "ServiceImpl",
                                IMiddleService.class);
                        IBaseService baseService = SpringUtils.getBean(
                                StringUtils.firstToLowerCase(manyToMany.targetClass().getSimpleName()) + "ServiceImpl",
                                IBaseService.class);

                        for (IBaseEntity<?> joinObj : joinData) {
                            if (null == joinObj.getId()) {
                                baseService.saveOne(joinObj);
                            }
                        }

                        List<IMiddleEntity<?,?>> middleEntities = new ArrayList<>(joinData.size());
                        for (BaseEntity<?> baseEntity : joinData) {
                            try {
                                IMiddleEntity middleEntity = manyToMany.middleClass().newInstance();
                                if (manyToMany.id1()) {
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
                        if (manyToMany.id1()) {
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
            ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
            if (null != manyToMany) {
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
            ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
            if (null != manyToMany) {
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
                Class<? extends MiddleEntity<?, ?>> middleClass = manyToMany.middleClass();
                String entity = middleClass.getSimpleName();
                IMiddleService middleService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl",
                        IMiddleService.class);
                if (manyToMany.id1()) {
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
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (null == oneToMany) {
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
            List<?> joinResult = baseService.findListByColumn(e -> BeanUtils.humpToUnderline(oneToMany.filedName()), id);

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
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (null == oneToMany) {
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

                Class<?> joinClass = oneToMany.targetClass();
                Field joinField = joinClass.getDeclaredField(oneToMany.filedName());
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
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (null == oneToMany) {
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

                Class<?> joinClass = oneToMany.targetClass();

                ID id = t.getId();
                Field joinField = joinClass.getDeclaredField(oneToMany.filedName());
                joinField.setAccessible(true);
                for (Object joinDatum : joinData) {
                    joinField.set(joinDatum, id);
                }

                IService baseService = SpringUtils.getBean(
                        StringUtils.firstToLowerCase(joinClass.getSimpleName()) + "ServiceImpl",
                        IService.class);
                baseService.removeByMap(Collections.singletonMap(oneToMany.filedName(), id));
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
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (null == oneToMany) {
                continue;
            }
            hasOneToMany = true;
            try {
                ID id = t.getId();
                IBaseService baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(oneToMany.targetClass().getSimpleName()) + "ServiceImpl", IBaseService.class);
                baseService.deleteListByColumn(e -> BeanUtils.humpToUnderline(oneToMany.filedName()), id);
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
            OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            if (null == oneToOne) {
                continue;
            }
            hasOneToOne = true;
            Type type = field.getGenericType();
            ParameterizedType pt = (ParameterizedType) type;
            Class<?> clz = (Class<?>) pt.getActualTypeArguments()[0];

            ID id = t.getId();

            String entity = clz.getSimpleName();
            IBaseService<?, ?> baseService = SpringUtils.getBean(StringUtils.firstToLowerCase(entity) + "ServiceImpl", IBaseService.class);
            IBaseEntity<?> joinResult = baseService.findOneByColumn(e -> BeanUtils.humpToUnderline(oneToOne.filedName()), id);

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
            OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            if (null == oneToOne) {
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

                Field joinField = clz.getDeclaredField(oneToOne.filedName());
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
            OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            if (null == oneToOne) {
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

                Field joinField = clz.getDeclaredField(oneToOne.filedName());
                joinField.setAccessible(true);
                joinField.set(joinDataObj, id);

                String entity = clz.getSimpleName();
                IService baseService = SpringUtils.getBean( StringUtils.firstToLowerCase(entity) + "ServiceImpl", IService.class);
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
            OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            if (null == oneToOne) {
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
            ManyToOne manyToMany = field.getAnnotation(ManyToOne.class);
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
            ManyToOne manyToMany = field.getAnnotation(ManyToOne.class);
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
                BaseEntity<?> joinData = (ListEntity<?>) joinDataObj;
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
