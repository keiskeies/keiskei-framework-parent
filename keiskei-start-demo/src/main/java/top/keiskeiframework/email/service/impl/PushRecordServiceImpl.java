package top.keiskeiframework.email.service.impl;

import org.springframework.stereotype.Service;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.email.entity.PushRecord;
import top.keiskeiframework.email.service.IPushRecordService;

/**
 * <p>
 * 推送记录
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/17 23:24
 */
@Service
public class PushRecordServiceImpl extends ListServiceImpl<PushRecord> implements IPushRecordService {
}
