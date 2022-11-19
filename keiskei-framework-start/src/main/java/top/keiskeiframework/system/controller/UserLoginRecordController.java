package top.keiskeiframework.system.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.impl.ListControllerImpl;
import top.keiskeiframework.system.entity.UserLoginRecord;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen
 * @since 2022/11/19 21:30
 */
@RestController
@RequestMapping("/system/user/loginRecord")
@Api(tags = "系统设置 - 管理员登录记录")
public class UserLoginRecordController extends ListControllerImpl<UserLoginRecord, Long> {

}
