package top.keiskeiframework.email.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.ListController;
import top.keiskeiframework.email.entity.PushTask;

/**
 * <p>
 * 推送任务
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/17 23:25
 */
@RestController
@RequestMapping("/admin/v1/email/pushTask")
@Api(tags = "邮件推送 - 推送任务")
public class PushTaskController extends ListController<PushTask> {
}
