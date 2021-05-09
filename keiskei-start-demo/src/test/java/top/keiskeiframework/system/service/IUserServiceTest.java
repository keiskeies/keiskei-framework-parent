package top.keiskeiframework.system.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.keiskeiframework.ApplicationTest;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.vo.base.ChartRequestDTO;
import top.keiskeiframework.system.entity.User;

import java.time.temporal.ChronoUnit;


public class IUserServiceTest  extends ApplicationTest {

    @Autowired
    private IUserService userService;

    @Test
    public void getChartOptions() {
        ChartRequestDTO chartRequestDTO = new ChartRequestDTO();
//        chartRequestDTO.setColumn("username");
        chartRequestDTO.setColumnType(ChartRequestDTO.ColumnType.TIME);
        chartRequestDTO.setUnit(ChronoUnit.WEEKS);
        chartRequestDTO.setStart(DateTimeUtils.strToTime("2020-01-01 00:00:00"));
        chartRequestDTO.setEnd(DateTimeUtils.strToTime("2022-01-01 00:00:00"));
        userService.getChartOptions(chartRequestDTO);
        User user = new User("username",1L);
    }

}