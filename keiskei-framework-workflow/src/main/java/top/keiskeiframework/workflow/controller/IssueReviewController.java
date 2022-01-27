package top.keiskeiframework.workflow.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.*;

import top.keiskeiframework.workflow.entity.IssueReview;
import top.keiskeiframework.workflow.service.IIssueReviewService;

/**
 * <p>
 * 卡片评论 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:42:21
 */
@RestController
@RequestMapping("/workflow/issueReview")
@Api(tags = "工作流 - 卡片评论")
public class IssueReviewController extends TreeControllerImpl<IssueReview, Long> {

    @Autowired
    private IIssueReviewService issueReviewService;



}
