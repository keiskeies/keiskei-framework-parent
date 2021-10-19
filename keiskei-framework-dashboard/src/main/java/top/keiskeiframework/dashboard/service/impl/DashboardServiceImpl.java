package top.keiskeiframework.dashboard.service.impl;

import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.util.BeanUtils;
import top.keiskeiframework.dashboard.factory.EntityFactory;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.CacheTimeEnum;
import top.keiskeiframework.common.enums.dashboard.ChartType;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.dashboard.TimeTypeEnum;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.util.SpringUtils;
import top.keiskeiframework.common.vo.dashboard.charts.*;
import top.keiskeiframework.common.vo.dashboard.charts.series.LineOrBarSeries;
import top.keiskeiframework.common.vo.dashboard.charts.series.PieSeries;
import top.keiskeiframework.common.vo.dashboard.charts.series.RadarSeries;
import top.keiskeiframework.dashboard.entity.Dashboard;
import top.keiskeiframework.dashboard.entity.DashboardDirection;
import top.keiskeiframework.dashboard.enums.DashboardExceptionEnum;
import top.keiskeiframework.dashboard.service.IDashboardService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 图表
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/4/13 18:40
 */
@Service
@Order
@Slf4j
@ConditionalOnProperty(value = {"keiskei.use-dashboard"})
public class DashboardServiceImpl extends ListServiceImpl<Dashboard> implements IDashboardService {

    private static final String CACHE_NAME = CacheTimeEnum.M10;


    @Override
    public Dashboard save(Dashboard dashboard) {
        validate(dashboard);
        return super.save(dashboard);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + '-detail-' + #dashboard.id")
    public Dashboard update(Dashboard dashboard) {
        validate(dashboard);
        return super.update(dashboard);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "targetClass.name + '-detail-' + #id")
    public void deleteById(String id) {
        super.deleteById(id);
    }


    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "targetClass.name + '-detail-' + #id", unless = "#result == null")
    public ChartOptionVO getChartOption(String id) {
        Dashboard dashboard = super.findById(id);
        Assert.notNull(dashboard, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());

        LocalDateTime[] startAndEnd;
        switch (dashboard.getTimeType()) {
            case CURRENT_DAY:
                startAndEnd = DateTimeUtils.getStartAndEndDayOfDay(null);
                break;
            case CURRENT_WEEKS:
                startAndEnd = DateTimeUtils.getStartAndEndDayOfWeek(null);
                break;
            case CURRENT_MONTH:
                startAndEnd = DateTimeUtils.getStartAndEndDayOfMonth(null);
                break;
            case CURRENT_QUARTER:
                startAndEnd = DateTimeUtils.getStartAndEndDayOfQuarter(null);
                break;
            case CURRENT_YEAR:
                startAndEnd = DateTimeUtils.getStartAndEndDayOfYear(null);
                break;
            default:
                startAndEnd = new LocalDateTime[]{DateTimeUtils.strToTime(dashboard.getStart()), DateTimeUtils.strToTime(dashboard.getEnd())};
                break;
        }

        ChartRequestDTO chartRequestDTO = new ChartRequestDTO();
        chartRequestDTO.setChartType(dashboard.getType());
        chartRequestDTO.setColumnType(dashboard.getFieldType());
        chartRequestDTO.setStart(startAndEnd[0]);
        chartRequestDTO.setEnd(startAndEnd[1]);
        chartRequestDTO.setTimeField(dashboard.getTimeField());

        if (ColumnType.TIME.equals(chartRequestDTO.getColumnType())) {
            chartRequestDTO.setTimeDelta(dashboard.getFieldDelta());
            return getTimeChartOption(chartRequestDTO, dashboard);
        } else {
            return getFieldChartOption(chartRequestDTO, dashboard);
        }
    }

    /**
     * 获取时间坐标的图表数据
     *
     * @param chartRequestDTO 。
     * @param dashboard       。
     * @return 。
     */
    public ChartOptionVO getTimeChartOption(ChartRequestDTO chartRequestDTO, Dashboard dashboard) {
        ChartOptionVO result = new ChartOptionVO();
        chartRequestDTO.setTimeDelta(dashboard.getFieldDelta());
        List<DashboardDirection> yFields = dashboard.getDirections();
        List<String> axisData = DateTimeUtils.timeRange(chartRequestDTO.getStart(), chartRequestDTO.getEnd(), chartRequestDTO.getTimeDelta());
        List<Series> seriesList = new ArrayList<>(yFields.size());
        List<String> legendData = new ArrayList<>(yFields.size());
        AtomicInteger max = new AtomicInteger(0);
        Series series = null;
        for (DashboardDirection direction : yFields) {
            chartRequestDTO.setEntityName(direction.getEntityName());
            Map<String, Long> dataMap = getDataMap(chartRequestDTO, dashboard, direction);
            series = confirmSeries(chartRequestDTO, dataMap, axisData, max, seriesList, series);
            legendData.add(direction.getEntityName());
        }
        confirmChartOptionVO(chartRequestDTO, dashboard, result, axisData, max, seriesList, legendData);
        return result;
    }

    /**
     * 获取字段类型图表数据
     *
     * @param chartRequestDTO 。
     * @param dashboard       。
     * @return 。
     */
    public ChartOptionVO getFieldChartOption(ChartRequestDTO chartRequestDTO, Dashboard dashboard) {
        ChartOptionVO result = new ChartOptionVO();
        List<DashboardDirection> yFields = dashboard.getDirections();
        Set<String> axisDataSet = new HashSet<>();
        Map<DashboardDirection, Map<String, Long>> dataList = new HashMap<>(yFields.size());
        List<String> legendData = new ArrayList<>(yFields.size());
        for (DashboardDirection direction : yFields) {
            Map<String, Long> dataMap = getDataMap(chartRequestDTO, dashboard, direction);
            axisDataSet.addAll(dataMap.keySet());
            dataList.put(direction, dataMap);
        }
        List<String> axisData = new ArrayList<>(axisDataSet);
        List<Series> seriesList = new ArrayList<>(yFields.size());
        AtomicInteger max = new AtomicInteger(0);
        Series series = null;
        for (Map.Entry<DashboardDirection, Map<String, Long>> entry : dataList.entrySet()) {
            DashboardDirection direction = entry.getKey();
            Map<String, Long> dataMap = entry.getValue();
            chartRequestDTO.setEntityName(direction.getEntityName());
            series = confirmSeries(chartRequestDTO, dataMap, axisData, max, seriesList, series);
            legendData.add(direction.getEntityName());
        }
        confirmChartOptionVO(chartRequestDTO, dashboard, result, axisData, max, seriesList, legendData);
        return result;
    }

    /**
     * 组装图表完整结构
     *
     * @param chartRequestDTO 图表基本查询参数
     * @param dashboard       图表参数
     * @param result          图表完整结构数据
     * @param axisData        图表下标
     * @param max             数据最大值（雷达图使用）
     * @param seriesList      图表数据
     * @param legendData      数据指示器
     */
    public void confirmChartOptionVO(ChartRequestDTO chartRequestDTO, Dashboard dashboard, ChartOptionVO result, List<String> axisData, AtomicInteger max, List<Series> seriesList, List<String> legendData) {
        switch (chartRequestDTO.getChartType()) {
            case RADAR:
                int maxNumber = getMaxRange(max.intValue());
                Radar radar = new Radar(axisData.stream().map(e -> new Radar.Indicator(e, maxNumber)).collect(Collectors.toList()));
                result.setRadar(radar);
                result.setLegend(new Legend(legendData));
                break;
            case PIE:
                result.setLegend(new Legend(axisData));
                break;
            default:
                result.setAxis(new Axis(axisData));
                result.setLegend(new Legend(legendData));
                break;
        }
        result.setSeries(seriesList);
        result.setHorizontal(dashboard.getHorizontal());
        result.setTitle(new Title(dashboard.getName()));
    }


    /**
     * 组装图表数据
     *
     * @param chartRequestDTO 图表基本查询参数
     * @param dataMap         实体类查询结果
     * @param axisData        图表下标
     * @param max             数据最大值（雷达图使用）
     * @param seriesList      图表数据
     * @param series          图表数据结果
     * @return .
     */
    public Series confirmSeries(ChartRequestDTO chartRequestDTO, Map<String, Long> dataMap, List<String> axisData, AtomicInteger max, List<Series> seriesList, Series series) {
        switch (chartRequestDTO.getChartType()) {
            case RADAR:
                List<Number> data = new ArrayList<>(dataMap.size());
                for (String key : axisData) {
                    Long value = dataMap.getOrDefault(key, 0L);
                    data.add(value);
                    max.set(Math.max(max.intValue(), value.intValue()));
                }
                series = new RadarSeries(Collections.singleton(
                        new RadarSeries.RadarSeriesData(chartRequestDTO.getEntityName(), data)
                ));
                seriesList.add(series);
                break;
            case PIE:
                List<PieSeries.PieSeriesData> pieSeriesDataList = new ArrayList<>(axisData.size());
                for (Map.Entry<String, Long> dataEntry : dataMap.entrySet()) {
                    pieSeriesDataList.add(new PieSeries.PieSeriesData(dataEntry.getKey(), dataEntry.getValue()));
                }
                if (null == series) {
                    series = new PieSeries(pieSeriesDataList);
                    seriesList.add(series);
                } else {
                    assert series instanceof PieSeries;
                    PieSeries pieSeries = (PieSeries) series;
                    pieSeries.getData().addAll(pieSeriesDataList);
                }
                break;
            default:
                data = new ArrayList<>(dataMap.size());
                for (String key : axisData) {
                    data.add(dataMap.getOrDefault(key, 0L));
                }
                series = new LineOrBarSeries(data);
                seriesList.add(series);
                break;
        }
        series.setName(chartRequestDTO.getEntityName());
        series.setType(chartRequestDTO.getChartType().name());
        return series;
    }

    /**
     * 获取图表实体类基础数据
     *
     * @param chartRequestDTO 。
     * @param dashboard       。
     * @param direction       。
     * @return 。
     */
    public Map<String, Long> getDataMap(ChartRequestDTO chartRequestDTO, Dashboard dashboard, DashboardDirection direction) {
        chartRequestDTO.setColumn(direction.getField());
        chartRequestDTO.setColumnType(dashboard.getFieldType());
        chartRequestDTO.setChartType(direction.getType());

        if (!CollectionUtils.isEmpty(direction.getConditions())) {
            Map<String, List<String>> conditions = new HashMap<>();
            for (DashboardDirection.DashboardDirectionCondition condition : direction.getConditions()) {
                if (StringUtils.isEmpty(condition.getField()) || CollectionUtils.isEmpty(condition.getRangeValue())) {
                    continue;
                }
                conditions.put(condition.getField(), condition.getRangeValue());
            }
            chartRequestDTO.setConditions(conditions);
        }

        String className = direction.getEntityClass().substring(direction.getEntityClass().lastIndexOf(".")).replace(".", "");
        BaseService<?> baseService = (BaseService<?>) SpringUtils.getBean(lowCaseFirst(className) + "ServiceImpl");
        return baseService.getChartOptions(chartRequestDTO);
    }

    private String lowCaseFirst(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return str.substring(0,1).toLowerCase(Locale.ROOT) + str.substring(1);

    }


    /**
     * 图表记录数据校验
     *
     * @param dashboard 。
     */
    private void validate(Dashboard dashboard) {
        if (ColumnType.TIME.equals(dashboard.getFieldType()) && StringUtils.isEmpty(dashboard.getFieldDelta())) {
            throw new BizException(DashboardExceptionEnum.TYPE_EMPTY);
        }
        if (TimeTypeEnum.NORMAL.equals(dashboard.getTimeType())) {
            if (StringUtils.isEmpty(dashboard.getStart()) || StringUtils.isEmpty(dashboard.getEnd())) {
                throw new BizException(DashboardExceptionEnum.TIME_EMPTY);
            }
        }
        List<DashboardDirection> yFields = dashboard.getDirections();
        if (yFields.size() > 1) {
            for (DashboardDirection direction : yFields) {
                if (ChartType.LINE_BAR.equals(dashboard.getType())) {
                    if (!ChartType.LINE.equals(direction.getType()) && !ChartType.BAR.equals(direction.getType())) {
                        throw new BizException(DashboardExceptionEnum.TYPE_CONFLICT);
                    }
                } else {
                    if (!dashboard.getType().equals(direction.getType())) {
                        throw new BizException(DashboardExceptionEnum.TYPE_CONFLICT);
                    }
                }
                if (!EntityFactory.columnEntityContains(direction.getEntityClass(), direction.getField())) {
                    throw new BizException(DashboardExceptionEnum.ENTITY_FIELD_NOT_EXIST);
                }
            }
        }
    }

    private final static List<Integer> MAX_RANGE = new ArrayList<>(64);

    static {
        int number = 10;
        MAX_RANGE.add(number);
        int delta = 1;
        for (int i = 0; i < 63; i++) {
            if (i % 9 == 0) {
                delta *= 10;
            }
            MAX_RANGE.add(number += delta);
        }
    }


    private static int getMaxRange(int number) {
        for (int i : MAX_RANGE) {
            if (number < i) {
                return i;
            }
        }
        return number;
    }
}
