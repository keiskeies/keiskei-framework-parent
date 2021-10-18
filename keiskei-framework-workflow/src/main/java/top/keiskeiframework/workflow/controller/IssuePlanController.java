package top.keiskeiframework.workflow.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.*;

import top.keiskeiframework.workflow.entity.IssuePlan;
import top.keiskeiframework.workflow.service.IIssuePlanService;

/**
 * <p>
 * 卡片计划 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:42:21
 */
@RestController
@RequestMapping("/admin/v1/workflow/issuePlan")
@Api(tags = "工作流 - 卡片计划")
public class IssuePlanController extends TreeController<IssuePlan, Long>{

    @Autowired
    private IIssuePlanService issuePlanService;



}
