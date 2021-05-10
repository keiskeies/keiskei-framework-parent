package top.keiskeiframework.dashboard.service.impl;

import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.base.service.EntityFactory;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.enums.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.vo.charts.ChartOptionVO;
import top.keiskeiframework.common.dto.dashboard.SeriesDataDTO;
import top.keiskeiframework.dashboard.entity.Dashboard;
import top.keiskeiframework.dashboard.entity.DashboardDirection;
import top.keiskeiframework.dashboard.enums.DashboardExceptionEnum;
import top.keiskeiframework.dashboard.enums.DashboardType;
import top.keiskeiframework.dashboard.service.IDashboardService;

import java.util.*;

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
        List<DashboardDirection> yFields = dashboard.getYFields();
        List<List<SeriesDataDTO>> dataList = new ArrayList<>(yFields.size());
        ChartRequestDTO chartRequestDTO = new ChartRequestDTO(
                DateTimeUtils.strToTime(dashboard.getStart()),
                DateTimeUtils.strToTime(dashboard.getEnd())
        );
        for (DashboardDirection direction : yFields) {
            chartRequestDTO.setColumn(direction.getField());
            chartRequestDTO.setColumnType(dashboard.getXFieldType());
            chartRequestDTO.setChartType(direction.getType());
            chartRequestDTO.setUnit(DateTimeUtils.getUnitByString(dashboard.getXFieldDelta()));
        }



        return null;
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
                if (DashboardType.LINE.getId().equalsIgnoreCase(direction.getType()) || DashboardType.BAR.getId().equalsIgnoreCase(direction.getType())) {
                    typeContains[0] = 1;
                } else if (DashboardType.PIE.getId().equalsIgnoreCase(direction.getType())) {
                    typeContains[1] = 1;
                } else if (DashboardType.RADAR.getId().equalsIgnoreCase(direction.getType())) {
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
