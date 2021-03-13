package top.keiskeiframework.system.thread.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.keiskeiframework.common.base.SqlService;
import top.keiskeiframework.system.entity.ScheduledTask;
import top.keiskeiframework.system.thread.ScheduledOfTask;

/**
 * <p>
 * 默认SQL定时任务
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/15 19:19
 */
@Component
@Slf4j
public class SqlExecuteScheduledOfTaskImpl extends ScheduledOfTask {


    @Override
    public void execute(ScheduledTask scheduledTask) {

        SqlService sqlService = applicationContext.getBean(SqlService.class);
        int affectRoNum = sqlService.executeSql(scheduledTask.getParam());
        log.info("SQL定时任务\n任务描述: {}\n任务定时: {}\n执行参数: {}\n影响行数: {}",
                scheduledTask.getDescription(), scheduledTask.getCron(), scheduledTask.getParam(), affectRoNum);

    }
}
