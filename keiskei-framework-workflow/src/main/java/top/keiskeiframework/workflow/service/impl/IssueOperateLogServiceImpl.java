package top.keiskeiframework.workflow.service.impl;

import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.workflow.entity.IssueOperateLog;
import top.keiskeiframework.workflow.repository.IssueOperateLogRepository;
import top.keiskeiframework.workflow.service.IIssueOperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 卡片操作记录 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:38:26
 */
@Service
public class IssueOperateLogServiceImpl extends ListServiceImpl<IssueOperateLog, Long> implements IIssueOperateLogService {

    @Autowired
    private IssueOperateLogRepository issueOperateLogRepository;

}
