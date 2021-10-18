package top.keiskeiframework.workflow.service.impl;

import top.keiskeiframework.common.base.service.impl.TreeServiceImpl;
import top.keiskeiframework.workflow.entity.IssueFieldItem;
import top.keiskeiframework.workflow.repository.IssueFieldItemRepository;
import top.keiskeiframework.workflow.service.IIssueFieldItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 卡片字段选项 业务实现层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-10-10 21:38:26
 */
@Service
public class IssueFieldItemServiceImpl extends TreeServiceImpl<IssueFieldItem, Long> implements IIssueFieldItemService {

    @Autowired
    private IssueFieldItemRepository issueFieldItemRepository;

}
