package top.keiskeiframework.log.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.ListController;
import top.keiskeiframework.log.entity.OperateLog;

/**
 * <p>
 * 操作日志 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@RestController
@RequestMapping("/admin/v2/log/operateLog")
@Api(tags = "系统设置 - 操作日志")
public class OperateLogController extends ListController<OperateLog, Long> {


}
