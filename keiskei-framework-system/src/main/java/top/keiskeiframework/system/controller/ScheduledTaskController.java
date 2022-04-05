package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.impl.ListControllerImpl;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.system.entity.ScheduledTask;
import top.keiskeiframework.system.service.IScheduledTaskService;

/**
 * <p>
 * 定时任务 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@RestController
@RequestMapping("/system/scheduledTask")
@Api(tags = "系统设置 - 定时任务")
@ConditionalOnProperty({"keiskei.use-scheduled-task"})
public class ScheduledTaskController extends ListControllerImpl<ScheduledTask, Long> {

    @Autowired
    private IScheduledTaskService scheduledTaskService;

    @PostMapping("/excute/{id}")
    @ApiOperation("执行")
    public R<Boolean> patch(@PathVariable Long id) {
        scheduledTaskService.excute(id);
        return R.ok(Boolean.TRUE);
    }

}
