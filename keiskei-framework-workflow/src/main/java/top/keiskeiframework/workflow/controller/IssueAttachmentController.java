package top.keiskeiframework.workflow.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.keiskeiframework.common.base.controller.impl.ListControllerImpl;
import top.keiskeiframework.workflow.entity.IssueAttachment;
import top.keiskeiframework.workflow.service.IIssueAttachmentService;

/**
 * <p>
 * 卡片附件 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:42:21
 */
@RestController
@RequestMapping("/workflow/issueAttachment")
@Api(tags = "工作流 - 卡片附件")
public class IssueAttachmentController extends ListControllerImpl<IssueAttachment, Long> {

    @Autowired
    private IIssueAttachmentService issueAttachmentService;



}
