package top.keiskeiframework.workflow.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.*;

import top.keiskeiframework.workflow.entity.IssueFieldItem;
import top.keiskeiframework.workflow.service.IIssueFieldItemService;

/**
 * <p>
 * 卡片字段选项 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:42:21
 */
@RestController
@RequestMapping("/admin/v2/workflow/issueFieldItem")
@Api(tags = "工作流 - 卡片字段选项")
public class IssueFieldItemController extends TreeController<IssueFieldItem, Long>{

    @Autowired
    private IIssueFieldItemService issueFieldItemService;



}
