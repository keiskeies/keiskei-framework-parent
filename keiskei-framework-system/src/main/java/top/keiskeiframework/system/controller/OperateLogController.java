package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.BaseController;
import top.keiskeiframework.system.entity.OperateLog;
import top.keiskeiframework.system.service.IOperateLogService;

/**
 * <p>
 * 操作日志 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@RestController
@RequestMapping("/admin/v1/system/operateLog")
@Api(tags = "系统设置 - 操作日志")
public class OperateLogController extends BaseController<OperateLog> {


}
