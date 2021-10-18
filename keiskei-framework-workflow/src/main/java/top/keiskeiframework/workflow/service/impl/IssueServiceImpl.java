package top.keiskeiframework.workflow.service.impl;

import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.workflow.entity.Issue;
import top.keiskeiframework.workflow.repository.IssueRepository;
import top.keiskeiframework.workflow.service.IIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 卡片管理 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:38:26
 */
@Service
public class IssueServiceImpl extends TreeServiceImpl<Issue, Long> implements IIssueService {

    @Autowired
    private IssueRepository issueRepository;

}
