package top.keiskeiframework.dashboard.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.cache.enums.CacheTimeEnum;
import top.keiskeiframework.common.base.dto.QueryConditionVO;
import top.keiskeiframework.common.base.mp.service.impl.MpListServiceImpl;
import top.keiskeiframework.common.base.service.IBaseService;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.util.SpringUtils;
import top.keiskeiframework.common.util.data.TagSerializer;
import top.keiskeiframework.common.vo.dashboard.charts.*;
import top.keiskeiframework.common.vo.dashboard.charts.series.LineOrBarSeries;
import top.keiskeiframework.common.vo.dashboard.charts.series.PieSeries;
import top.keiskeiframework.common.vo.dashboard.charts.series.RadarSeries;
import top.keiskeiframework.dashboard.entity.Dashboard;
import top.keiskeiframework.dashboard.entity.DashboardDirection;
import top.keiskeiframework.dashboard.entity.DashboardDirectionCondition;
import top.keiskeiframework.dashboard.mapper.DashboardMapper;
import top.keiskeiframework.dashboard.service.IDashboardService;

import java.io.Serializable;
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
@Slf4j
public class DashboardServiceImpl extends MpListServiceImpl<Dashboard, Integer, DashboardMapper> implements IDashboardService {

    @Autowired
    private IDashboardService dashboardService;

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheTimeEnum.M10, key = "targetClass.name + '-detail-' + #dashboard.id"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #dashboard.id")
    })
    public boolean updateById(Dashboard dashboard) {
        return super.updateById(dashboard);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheTimeEnum.M10, key = "targetClass.name + '-detail-' + #id"),
            @CacheEvict(cacheNames = CACHE_LIST_NAME, key = "targetClass.name + ':' + #id")
    })
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }


    @Override
    @Cacheable(cacheNames = CacheTimeEnum.M10, key = "targetClass.name + '-detail-' + #id", unless = "#result == null")
    public ChartOptionVO getChartOption(Integer id) {
        Dashboard dashboard = dashboardService.findOneById(id);
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

        ChartRequestDTO chartRequestDTO = new ChartRequestDTO(
                dashboard.getType(),
                dashboard.getFieldType(),
                startAndEnd[0],
                startAndEnd[1]
        );
        if (ColumnType.TIME.equals(chartRequestDTO.getColumnType())) {
            chartRequestDTO.setTimeDelta(dashboard.getFieldDelta());
            return getTimeChartOption(chartRequestDTO, dashboard);
        } else {
            return getFieldChartOption(chartRequestDTO, dashboard);
        }
    }

    @Override
    @CachePut(cacheNames = CacheTimeEnum.M10, key = "targetClass.name + '-detail-' + #id", unless = "#result == null")
    public ChartOptionVO refreshChartOption(Integer id) {
        return getChartOption(id);
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
        Collection<DashboardDirection> yFields = dashboard.getDirections();
        List<String> axisData = DateTimeUtils.timeRange(chartRequestDTO.getStart(), chartRequestDTO.getEnd(), chartRequestDTO.getTimeDelta());
        List<Series> seriesList = new ArrayList<>(yFields.size());
        List<String> legendData = new ArrayList<>(yFields.size());
        AtomicInteger max = new AtomicInteger(0);
        Series series = null;
        for (DashboardDirection direction : yFields) {
            chartRequestDTO.setEntityName(direction.getEntityName());
            Map<String, Double> dataMap = getDataMap(chartRequestDTO, dashboard, direction);
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
        Collection<DashboardDirection> yFields = dashboard.getDirections();
        Set<String> axisDataSet = new HashSet<>();
        Map<DashboardDirection, Map<String, Double>> dataList = new HashMap<>(yFields.size());
        List<String> legendData = new ArrayList<>(yFields.size());
        for (DashboardDirection direction : yFields) {
            Map<String, Double> dataMap = getDataMap(chartRequestDTO, dashboard, direction);
            axisDataSet.addAll(dataMap.keySet());
            dataList.put(direction, dataMap);
        }
        List<String> axisData = new ArrayList<>(axisDataSet);
        List<Series> seriesList = new ArrayList<>(yFields.size());
        AtomicInteger max = new AtomicInteger(0);
        Series series = null;
        for (Map.Entry<DashboardDirection, Map<String, Double>> entry : dataList.entrySet()) {
            DashboardDirection direction = entry.getKey();
            Map<String, Double> dataMap = entry.getValue();
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
    public Series confirmSeries(ChartRequestDTO chartRequestDTO, Map<String, Double> dataMap, List<String> axisData, AtomicInteger max, List<Series> seriesList, Series series) {
        switch (chartRequestDTO.getChartType()) {
            case RADAR:
                List<Number> data = new ArrayList<>(dataMap.size());
                for (String key : axisData) {
                    Double value = dataMap.getOrDefault(key, 0D);
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
                for (Map.Entry<String, Double> dataEntry : dataMap.entrySet()) {
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
                    data.add(dataMap.getOrDefault(key, 0D));
                }
                series = new LineOrBarSeries(data, chartRequestDTO.getEntityName());
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
    public Map<String, Double> getDataMap(ChartRequestDTO chartRequestDTO, Dashboard dashboard, DashboardDirection direction) {
        chartRequestDTO.setColumn(direction.getField());
        chartRequestDTO.setColumnType(dashboard.getFieldType());
        chartRequestDTO.setChartType(direction.getType());

        if (!CollectionUtils.isEmpty(direction.getConditions())) {
            List<QueryConditionVO> queryConditions = new ArrayList<>(direction.getConditions().size());
            for (DashboardDirectionCondition condition : direction.getConditions()) {
                queryConditions.add(new QueryConditionVO(condition.getField(), TagSerializer.convertValue(condition.getRangeValue())));
            }
            chartRequestDTO.setConditions(queryConditions);
        }

        String className = direction.getEntityClass().substring(direction.getEntityClass().lastIndexOf(".")).replace(".", "");

        IBaseService<?, ?> baseService = SpringUtils.getBean(lowCaseFirst(className) + "ServiceImpl", IBaseService.class);
        return baseService.getChartOptions(chartRequestDTO);
    }

    private String lowCaseFirst(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return str.substring(0, 1).toLowerCase(Locale.ROOT) + str.substring(1);

    }

//
//    /**
//     * 图表记录数据校验
//     *
//     * @param dashboard 。
//     */
//    @Override
//    public void validate(Dashboard dashboard) {
//        if (ColumnType.TIME.equals(dashboard.getFieldType()) && StringUtils.isEmpty(dashboard.getFieldDelta())) {
//            throw new BizException(DashboardExceptionEnum.TYPE_EMPTY);
//        }
//        if (TimeTypeEnum.NORMAL.equals(dashboard.getTimeType())) {
//            if (StringUtils.isEmpty(dashboard.getStart()) || StringUtils.isEmpty(dashboard.getEnd())) {
//                throw new BizException(DashboardExceptionEnum.TIME_EMPTY);
//            }
//        }
//        Collection<DashboardDirection> yFields = dashboard.getDirections();
//        if (yFields.size() > 1) {
//            for (DashboardDirection direction : yFields) {
//                if (ChartType.LINE_BAR.equals(dashboard.getType())) {
//                    if (!ChartType.LINE.equals(direction.getType()) && !ChartType.BAR.equals(direction.getType())) {
//                        throw new BizException(DashboardExceptionEnum.TYPE_CONFLICT);
//                    }
//                } else {
//                    if (!dashboard.getType().equals(direction.getType())) {
//                        throw new BizException(DashboardExceptionEnum.TYPE_CONFLICT);
//                    }
//                }
//                if (!EntityFactory.columnEntityContains(direction.getEntityClass(), direction.getField())) {
//                    throw new BizException(DashboardExceptionEnum.ENTITY_FIELD_NOT_EXIST);
//                }
//            }
//        }
//    }

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