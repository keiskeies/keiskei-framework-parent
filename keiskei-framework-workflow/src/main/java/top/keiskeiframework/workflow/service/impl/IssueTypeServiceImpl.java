package top.keiskeiframework.workflow.service.impl;

import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.workflow.entity.IssueType;
import top.keiskeiframework.workflow.repository.IssueTypeRepository;
import top.keiskeiframework.workflow.service.IIssueTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 卡片类型 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:38:26
 */
@Service
public class IssueTypeServiceImpl extends ListServiceImpl<IssueType, Long> implements IIssueTypeService {

    @Autowired
    private IssueTypeRepository issueTypeRepository;

}
