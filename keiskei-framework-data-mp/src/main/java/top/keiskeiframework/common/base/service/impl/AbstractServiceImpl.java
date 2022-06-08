package top.keiskeiframework.common.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.entity.IBaseEntity;
import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.common.base.util.BaseRequestUtils;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.dashboard.CalcType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.timer.TimeDeltaEnum;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.vo.PageResult;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
    public List<T> findListByColumn(String column, Serializable value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(column, value);
        return this.list(queryWrapper);
    }

    @Override
    public boolean deleteListByColumn(String column, Serializable value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(column, value);
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
    public T findOneByColumn(String column, Serializable value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BeanUtils.humpToUnderline(column), value);
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


}