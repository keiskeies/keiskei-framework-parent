package top.keiskeiframework.email.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.ListController;
import top.keiskeiframework.email.entity.PushRecord;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/17 23:25
 */
@RestController
@RequestMapping("/admin/v1/email/pushRecord")
@Api(tags = "邮件推送 - 推送记录")
public class PushRecordController extends ListController<PushRecord> {
}
