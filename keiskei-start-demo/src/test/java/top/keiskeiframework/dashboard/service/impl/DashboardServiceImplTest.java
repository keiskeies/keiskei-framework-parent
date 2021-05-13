package top.keiskeiframework.dashboard.service.impl;


import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.keiskeiframework.ApplicationTest;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.vo.charts.ChartOptionVO;
import top.keiskeiframework.dashboard.entity.Dashboard;
import top.keiskeiframework.dashboard.entity.DashboardDirection;

import java.util.ArrayList;
import java.util.List;

public class DashboardServiceImplTest extends ApplicationTest {

    @Autowired
    private DashboardServiceImpl dashboardService;

    @Test
    public void testOption() {
//        Dashboard dashboard = new Dashboard();
//        dashboard.setStart("2021-02-20 00:00:00");
//        dashboard.setEnd("2021-05-11 00:00:00");
//        dashboard.setName("AAA");
//        dashboard.setXFieldDelta("Weeks");
//        dashboard.setXFieldType(ChartRequestDTO.ColumnType.FIELD);
//        List<DashboardDirection> dashboardDirections = new ArrayList<>();
//        dashboardDirections.add(new DashboardDirection(
//                "type",
//                "top.keiskeiframework.system.entity.OperateLog",
//                "操作日志",
//                "pie"
//        ));
//
//        dashboard.setYFields(dashboardDirections);
//
//        ChartRequestDTO chartRequestDTO = new ChartRequestDTO(
//                dashboard.getXFieldType(),
//                DateTimeUtils.strToTime(dashboard.getStart()),
//                DateTimeUtils.strToTime(dashboard.getEnd())
//        );
//        ChartOptionVO chartOptionVO;
//        if (ChartRequestDTO.ColumnType.TIME.equals(chartRequestDTO.getColumnType())) {
//            chartRequestDTO.setUnit(DateTimeUtils.getUnitByString(dashboard.getXFieldDelta()));
//            chartOptionVO = dashboardService.getTimeChartOption(chartRequestDTO, dashboard);
//        } else {
//            chartOptionVO = dashboardService.getFieldChartOption(chartRequestDTO, dashboard);
//        }
//        System.out.println(JSON.toJSONString(chartOptionVO));
    }

}
