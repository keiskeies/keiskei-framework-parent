package top.keiskeiframework.workflow.service.impl;

import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.workflow.entity.IssuePlan;
import top.keiskeiframework.workflow.repository.IssuePlanRepository;
import top.keiskeiframework.workflow.service.IIssuePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 卡片计划 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:38:26
 */
@Service
public class IssuePlanServiceImpl extends TreeServiceImpl<IssuePlan, Long> implements IIssuePlanService {

    @Autowired
    private IssuePlanRepository issuePlanRepository;

}
