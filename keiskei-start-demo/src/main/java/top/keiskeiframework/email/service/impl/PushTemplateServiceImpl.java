package top.keiskeiframework.email.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.email.entity.PushRecord;
import top.keiskeiframework.email.entity.PushTemplate;
import top.keiskeiframework.email.service.IPushRecordService;
import top.keiskeiframework.email.service.IPushTemplateService;

/**
 * <p>
 * 推送模版
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/17 23:24
 */
@Service
public class PushTemplateServiceImpl extends ListServiceImpl<PushTemplate> implements IPushTemplateService {
}
