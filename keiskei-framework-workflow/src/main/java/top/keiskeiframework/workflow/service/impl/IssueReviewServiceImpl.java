package top.keiskeiframework.workflow.service.impl;

import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.workflow.entity.IssueReview;
import top.keiskeiframework.workflow.repository.IssueReviewRepository;
import top.keiskeiframework.workflow.service.IIssueReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 卡片评论 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:38:26
 */
@Service
public class IssueReviewServiceImpl extends TreeServiceImpl<IssueReview, Long> implements IIssueReviewService {

    @Autowired
    private IssueReviewRepository issueReviewRepository;

}
