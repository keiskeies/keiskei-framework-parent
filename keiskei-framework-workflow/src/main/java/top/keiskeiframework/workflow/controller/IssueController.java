package top.keiskeiframework.workflow.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.*;

import top.keiskeiframework.workflow.entity.Issue;
import top.keiskeiframework.workflow.service.IIssueService;

/**
 * <p>
 * 卡片管理 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:42:21
 */
@RestController
@RequestMapping("/workflow/issue")
@Api(tags = "工作流 - 卡片管理")
public class IssueController extends TreeControllerImpl<Issue, Long> {

    @Autowired
    private IIssueService issueService;



}
