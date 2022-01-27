package top.keiskeiframework.workflow.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.*;

import top.keiskeiframework.workflow.entity.IssueField;
import top.keiskeiframework.workflow.service.IIssueFieldService;

/**
 * <p>
 * 卡片字段 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:42:21
 */
@RestController
@RequestMapping("/workflow/issueField")
@Api(tags = "工作流 - 卡片字段")
public class IssueFieldController extends ListControllerImpl<IssueField, Long> {

    @Autowired
    private IIssueFieldService issueFieldService;



}
