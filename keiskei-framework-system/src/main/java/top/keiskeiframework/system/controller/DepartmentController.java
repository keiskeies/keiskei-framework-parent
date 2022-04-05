package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.impl.TreeControllerImpl;
import top.keiskeiframework.system.entity.SystemDepartment;

/**
 * <p>
 * 部门管理 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@RestController
@RequestMapping("/system/department")
@Api(tags = "系统设置 - 部门管理")
@ConditionalOnProperty({"keiskei.use-department"})
public class DepartmentController extends TreeControllerImpl<SystemDepartment, Long> {


}
