package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.ListControllerImpl;
import top.keiskeiframework.system.entity.SystemRole;

/**
 * <p>
 * 角色管理 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@RestController
@RequestMapping("/system/role")
@Api(tags = "系统设置 - 角色管理")
@ConditionalOnProperty({"keiskei.use-permission"})
public class RoleController extends ListControllerImpl<SystemRole, Long> {


}
