package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.impl.ListControllerImpl;
import top.keiskeiframework.system.entity.DepartmentLevel;

/**
 * <p>
 * 组织管理 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@RestController
@RequestMapping("/system/department/level")
@Api(tags = "系统设置 - 组织管理")
public class DepartmentLevelController extends ListControllerImpl<DepartmentLevel, Integer> {


}
