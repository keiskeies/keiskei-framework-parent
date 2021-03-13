package top.keiskeiframework.system.service.impl;

import org.springframework.scheduling.annotation.Async;
import top.keiskeiframework.common.base.service.impl.BaseServiceImpl;
import top.keiskeiframework.system.entity.OperateLog;
import top.keiskeiframework.system.enums.OperateTypeEnum;
import top.keiskeiframework.system.repository.OperateLogRepository;
import top.keiskeiframework.system.service.IOperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Service
public class IOperateLogServiceImpl extends BaseServiceImpl<OperateLog> implements IOperateLogService {

    @Autowired
    private OperateLogRepository operateLogRepository;

    @Async
    @Override
    public void saveAsync(OperateLog operateLog) {
        String type = OperateTypeEnum.getType(operateLog.getType());
        if (null != type) {
            operateLogRepository.save(operateLog);
        }
    }
}
