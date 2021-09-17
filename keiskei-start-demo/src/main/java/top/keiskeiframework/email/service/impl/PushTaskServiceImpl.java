package top.keiskeiframework.email.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.email.entity.PushTask;
import top.keiskeiframework.email.service.IPushTaskService;

/**
 * <p>
 * 推送任务
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/17 23:23
 */
@Service
public class PushTaskServiceImpl extends ListServiceImpl<PushTask> implements IPushTaskService {
}
