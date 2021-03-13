package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.BaseController;
import top.keiskeiframework.system.entity.User;


/**
 * <p>
 * 管理员 controller层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-10 14:11:30
 */
@RestController
@RequestMapping("/admin/{version}/system/user")
@Api(tags = "系统设置 - 管理员")
public class UserController extends BaseController<User> {


}
