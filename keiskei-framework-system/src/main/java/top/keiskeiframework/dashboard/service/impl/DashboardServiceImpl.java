package top.keiskeiframework.dashboard.service.impl;

import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.common.base.service.EntityFactory;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.util.SpringUtils;
import top.keiskeiframework.common.vo.charts.*;
import top.keiskeiframework.common.dto.dashboard.SeriesDataDTO;
import top.keiskeiframework.common.vo.charts.series.BarSeries;
import top.keiskeiframework.common.vo.charts.series.LineSeries;
import top.keiskeiframework.common.vo.charts.series.PieSeries;
import top.keiskeiframework.common.vo.charts.series.RadarSeries;
import top.keiskeiframework.common.vo.charts.series.data.PieSeriesData;
import top.keiskeiframework.common.vo.charts.series.data.RadarSeriesData;
import top.keiskeiframework.dashboard.entity.Dashboard;
import top.keiskeiframework.dashboard.entity.DashboardDirection;
import top.keiskeiframework.dashboard.enums.DashboardExceptionEnum;
import top.keiskeiframework.dashboard.enums.DashboardTypeEnum;
import top.keiskeiframework.dashboard.service.IDashboardService;

import java.util.*;
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
public class DashboardServiceImpl extends ListServiceImpl<Dashboard> implements IDashboardService {


    @Override
    public ChartOptionVO getChartOption(Long id) {
        Dashboard dashboard = baseService.getById(id);
        Assert.notNull(dashboard, BizExceptionEnum.NOT_FOUND_ERROR.getMsg());
        ChartRequestDTO chartRequestDTO = new ChartRequestDTO(
                DateTimeUtils.strToTime(dashboard.getStart()),
                DateTimeUtils.strToTime(dashboard.getEnd())
        );
        if (ChartRequestDTO.ColumnType.TIME.equals(chartRequestDTO.getColumnType())) {
            return getTimeChartOption(chartRequestDTO, dashboard);
        } else {
            return getFieldChartOption();
        }
    }


    public ChartOptionVO getTimeChartOption(ChartRequestDTO chartRequestDTO, Dashboard dashboard) {
        ChartOptionVO result = new ChartOptionVO();
        chartRequestDTO.setUnit(DateTimeUtils.getUnitByString(dashboard.getXFieldDelta()));
        List<DashboardDirection> yFields = dashboard.getYFields();
        List<String> axisData = DateTimeUtils.timeRange(chartRequestDTO.getStart(), chartRequestDTO.getEnd(), chartRequestDTO.getUnit());
        List<Series> seriesList = new ArrayList<>(yFields.size());
        Series series = null;
        int max = Integer.MIN_VALUE;
        for (DashboardDirection direction : yFields) {
            Map<String, Long> dataMap = getDataMap(chartRequestDTO, dashboard, direction);
            switch (chartRequestDTO.getChartType()) {
                case "radar":
                    List<Number> data = new ArrayList<>(dataMap.size());
                    for (String key : axisData) {
                        Long value = dataMap.getOrDefault(key, 0L);
                        data.add(value);
                        max = Math.max(max, value.intValue());
                    }
                    series = new RadarSeries(Collections.singleton(new RadarSeriesData(direction.getEntityName(), data)));
                    seriesList.add(series);
                    break;
                case "pie":
                    List<PieSeriesData> pieSeriesDataList = new ArrayList<>(axisData.size());
                    for (Map.Entry<String, Long> entry : dataMap.entrySet()) {
                        pieSeriesDataList.add(new PieSeriesData(entry.getKey(), entry.getValue()));
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
                case "bar":
                    data = new ArrayList<>(dataMap.size());
                    for (String key : axisData) {
                        data.add(dataMap.getOrDefault(key, 0L));
                    }
                    series = new BarSeries(data);
                    seriesList.add(series);
                    break;
                default:
                    data = new ArrayList<>(dataMap.size());
                    for (String key : axisData) {
                        data.add(dataMap.getOrDefault(key, 0L));
                    }
                    series = new LineSeries(data);
                    seriesList.add(series);
                    break;
            }
        }
        if (DashboardTypeEnum.LINE.getId().equalsIgnoreCase(chartRequestDTO.getChartType()) ||
                DashboardTypeEnum.BAR.getId().equalsIgnoreCase(chartRequestDTO.getChartType())) {
            if (dashboard.getYHorizontal()) {
                result.setYAxis(new Axis(axisData));
            } else {
                result.setXAxis(new Axis(axisData));
            }
        }
        switch (chartRequestDTO.getChartType()) {
            case "radar":
                int finalMax = max;
                Radar radar = new Radar(axisData.stream().map(e -> new Radar.Indicator(e, finalMax)).collect(Collectors.toList()));
                result.setRadar(radar);
                break;
            case "pie": result.setLegend(new Legend(axisData)); break;
            default: break;
        }
        result.setSeries(seriesList);
        return result;
    }

    public ChartOptionVO getFieldChartOption(ChartRequestDTO chartRequestDTO, Dashboard dashboard) {
        ChartOptionVO result = new ChartOptionVO();
        List<DashboardDirection> yFields = dashboard.getYFields();
        Set<String> axisData = new HashSet<>();
        for (DashboardDirection direction : yFields) {
            Map<String, Long> dataMap = getDataMap(chartRequestDTO, dashboard, direction);
            axisData.addAll(dataMap.keySet());

        }


        return null;
    }

    public Map<String, Long> getDataMap(ChartRequestDTO chartRequestDTO, Dashboard dashboard, DashboardDirection direction) {
        chartRequestDTO.setColumn(direction.getField());
        chartRequestDTO.setColumnType(dashboard.getXFieldType());
        chartRequestDTO.setChartType(direction.getType());

        BaseService<?> baseService = (BaseService<?>) SpringUtils.getBean(
                direction.getEntityClass().substring(direction.getEntityClass().lastIndexOf(".")) + "ServiceImpl"
        );
        return baseService.getChartOptions(chartRequestDTO);
    }


    @Override
    public Dashboard save(Dashboard dashboard) {
        validate(dashboard);
        return super.save(dashboard);
    }

    @Override
    public Dashboard update(Dashboard dashboard) {
        validate(dashboard);
        return super.update(dashboard);
    }

    private void validate(Dashboard dashboard) {
        if (ChartRequestDTO.ColumnType.TIME.equals(dashboard.getXFieldType()) && StringUtils.isEmpty(dashboard.getXFieldDelta())) {
            throw new BizException(DashboardExceptionEnum.TYPE_EMPTY);
        }
        List<DashboardDirection> yFields = dashboard.getYFields();
        if (yFields.size() > 1) {
            // {折线/柱状图，饼图，雷达图}
            int[] typeContains = {0, 0, 0};

            for (DashboardDirection direction : yFields) {
                if (DashboardTypeEnum.LINE.getId().equalsIgnoreCase(direction.getType()) || DashboardTypeEnum.BAR.getId().equalsIgnoreCase(direction.getType())) {
                    typeContains[0] = 1;
                } else if (DashboardTypeEnum.PIE.getId().equalsIgnoreCase(direction.getType())) {
                    typeContains[1] = 1;
                } else if (DashboardTypeEnum.RADAR.getId().equalsIgnoreCase(direction.getType())) {
                    typeContains[2] = 1;
                }

                if (!EntityFactory.columnEntityContains(direction.getEntityClass(), direction.getField())) {
                    throw new BizException(DashboardExceptionEnum.ENTITY_FIELD_NOT_EXIST);
                }
            }
            if (typeContains[0] + typeContains[1] + typeContains[2] > 1) {
                throw new BizException(DashboardExceptionEnum.TYPE_CONFLICT);
            }
        }
    }


}
