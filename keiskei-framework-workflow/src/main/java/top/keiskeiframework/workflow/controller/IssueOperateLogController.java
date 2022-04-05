package top.keiskeiframework.workflow.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.keiskeiframework.common.base.controller.impl.ListControllerImpl;
import top.keiskeiframework.workflow.entity.IssueOperateLog;
import top.keiskeiframework.workflow.service.IIssueOperateLogService;

/**
 * <p>
 * 卡片操作记录 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:42:21
 */
@RestController
@RequestMapping("/workflow/issueOperateLog")
@Api(tags = "工作流 - 卡片操作记录")
public class IssueOperateLogController extends ListControllerImpl<IssueOperateLog, Long> {

    @Autowired
    private IIssueOperateLogService issueOperateLogService;



}
