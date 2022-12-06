package top.keiskeiframework.logdb.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.keiskeiframework.common.annotation.annotation.Lockable;
import top.keiskeiframework.common.base.mp.service.impl.MpListServiceImpl;
import top.keiskeiframework.common.enums.notify.OperateTypeEnum;
import top.keiskeiframework.common.util.ThreadPoolExecUtils;
import top.keiskeiframework.log.dto.OperateLogDTO;
import top.keiskeiframework.log.service.OperateLogService;
import top.keiskeiframework.logdb.entity.OperateLog;
import top.keiskeiframework.logdb.mapper.OperateLogMapper;
import top.keiskeiframework.logdb.service.IOperateLogService;

/**
 * <p>
 * 操作日志 业务实现层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
@Service
public class OperateLogServiceImpl extends MpListServiceImpl<OperateLog, Integer, OperateLogMapper> implements IOperateLogService, OperateLogService {

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
                ThreadPoolExecUtils.execute(() -> operateLogService.saveOne(operateLog));
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    @Lockable(key = "targetClass.name + ':' + #operateLog.hashCode()", autoUnlock = false)
    public boolean save(OperateLog operateLog) {
        return super.save(operateLog);
    }

}
