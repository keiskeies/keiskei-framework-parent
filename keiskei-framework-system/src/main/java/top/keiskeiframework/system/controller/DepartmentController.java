package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.TreeController;
import top.keiskeiframework.system.entity.Department;

/**
 * <p>
 * 部门管理 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@RestController
@RequestMapping("/admin/{version}/system/department")
@Api(tags = "系统设置 - 部门管理")
public class DepartmentController extends TreeController<Department> {


}
