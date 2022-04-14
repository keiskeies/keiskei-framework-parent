package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.base.controller.impl.TreeControllerImpl;
import top.keiskeiframework.system.entity.SystemPermission;

/**
 * <p>
 * 权限管理 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@RestController
@RequestMapping("/system/permission")
@Api(tags = "系统设置 - 权限管理")
@ConditionalOnProperty({"keiskei.use-permission"})
public class PermissionController extends TreeControllerImpl<SystemPermission> {


}
