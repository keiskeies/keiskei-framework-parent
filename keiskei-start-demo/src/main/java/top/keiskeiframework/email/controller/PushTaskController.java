package top.keiskeiframework.email.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.keiskeiframework.common.base.controller.ListController;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.email.entity.PushTask;
import top.keiskeiframework.email.service.IPushTaskService;

import java.io.IOException;

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
@Api(tags = "邮件推送-推送任务")
public class PushTaskController extends ListController<PushTask> {

    @Autowired
    private IPushTaskService pushTaskService;

    @PostMapping("/{id}/toUsers/import")
    @ApiOperation("导入发送人")
    public R<Boolean> toUsersImport(@PathVariable String id, MultipartFile file) throws IOException {
        pushTaskService.toUsersImport(id, file);
        return R.ok(true);
    }

    @PatchMapping("/{id}/send/start")
    @ApiOperation("发送")
    public R<Boolean> sendStart(@PathVariable String id) {
        pushTaskService.sendStart(id);
        return R.ok(true);
    }
    @PatchMapping("/{id}/send/stop")
    @ApiOperation("发送")
    public R<Boolean> sendStop(@PathVariable String id) {
        pushTaskService.sendStop(id);
        return R.ok(true);
    }

}
