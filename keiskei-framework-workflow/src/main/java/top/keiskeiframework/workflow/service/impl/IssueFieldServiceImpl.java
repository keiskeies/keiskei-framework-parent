package top.keiskeiframework.workflow.service.impl;

import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.workflow.entity.IssueField;
import top.keiskeiframework.workflow.repository.IssueFieldRepository;
import top.keiskeiframework.workflow.service.IIssueFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 卡片字段 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:38:26
 */
@Service
public class IssueFieldServiceImpl extends ListServiceImpl<IssueField, Long> implements IIssueFieldService {

    @Autowired
    private IssueFieldRepository issueFieldRepository;

}
