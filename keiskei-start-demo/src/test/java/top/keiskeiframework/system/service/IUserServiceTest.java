package top.keiskeiframework.system.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.keiskeiframework.ApplicationTest;
import top.keiskeiframework.common.enums.dashboard.ColumnType;
import top.keiskeiframework.common.util.DateTimeUtils;
import top.keiskeiframework.common.dto.dashboard.ChartRequestDTO;
import top.keiskeiframework.system.entity.User;

import java.time.temporal.ChronoUnit;


public class IUserServiceTest  extends ApplicationTest {

    @Autowired
    private IUserService userService;

    @Test
    public void getChartOptions() {
    }

}