package top.keiskeiframework.log.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.base.service.OperateLogService;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.dto.log.OperateLogDTO;
import top.keiskeiframework.log.entity.OperateLog;
import top.keiskeiframework.common.enums.log.OperateTypeEnum;
import top.keiskeiframework.log.service.IOperateLogService;
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
public class OperateLogServiceImpl extends ListServiceImpl<OperateLog, Long> implements IOperateLogService, OperateLogService {

    @Autowired
    private IOperateLogService operateLogService;

    @Override
    public void saveLog(OperateLogDTO log) {
        OperateLog operateLog = new OperateLog();
        BeanUtils.copyProperties(log, operateLog);
        String type = OperateTypeEnum.getType(operateLog.getType());
        if (null != type) {
            try {
                operateLog.setType(type);
                operateLogService.save(operateLog);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Async
    @Lockable(key = "#t.hashCode()")
    public OperateLog save(OperateLog operateLog) {
        return super.save(operateLog);
    }
}
