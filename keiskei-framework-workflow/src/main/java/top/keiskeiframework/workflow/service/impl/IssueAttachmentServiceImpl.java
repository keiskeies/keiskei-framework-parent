package top.keiskeiframework.workflow.service.impl;

import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.workflow.entity.IssueAttachment;
import top.keiskeiframework.workflow.repository.IssueAttachmentRepository;
import top.keiskeiframework.workflow.service.IIssueAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 卡片附件 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:38:26
 */
@Service
public class IssueAttachmentServiceImpl extends ListServiceImpl<IssueAttachment, Long> implements IIssueAttachmentService {

    @Autowired
    private IssueAttachmentRepository issueAttachmentRepository;

}
