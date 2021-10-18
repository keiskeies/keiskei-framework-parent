package top.keiskeiframework.workflow.service.impl;

import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.workflow.entity.IssueFieldValue;
import top.keiskeiframework.workflow.repository.IssueFieldValueRepository;
import top.keiskeiframework.workflow.service.IIssueFieldValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 卡片字段值 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:38:26
 */
@Service
public class IssueFieldValueServiceImpl extends ListServiceImpl<IssueFieldValue, Long> implements IIssueFieldValueService {

    @Autowired
    private IssueFieldValueRepository issueFieldValueRepository;

}
