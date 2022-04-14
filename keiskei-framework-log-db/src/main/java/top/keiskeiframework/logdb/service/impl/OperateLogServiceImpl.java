package top.keiskeiframework.logdb.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.keiskeiframework.common.annotation.log.Lockable;
import top.keiskeiframework.common.base.dto.BasePageVO;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.service.impl.ListServiceImpl;
import top.keiskeiframework.common.enums.log.OperateTypeEnum;
import top.keiskeiframework.log.dto.OperateLogDTO;
import top.keiskeiframework.log.service.OperateLogService;
import top.keiskeiframework.logdb.entity.OperateLog;
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
            }
        }
    }

    @Override
    @Async
    @Lockable(key = "#t.hashCode()")
    public boolean save(OperateLog operateLog) {
        return super.save(operateLog);
    }

    @Override
    public IPage<OperateLog> page(BaseRequestVO<OperateLog, Long> request, BasePageVO<OperateLog, Long> page) {
        IPage<OperateLog> result = super.page(request, page);
        this.baseMapper.findManyToOne(new OperateLog());

        return result;
    }
}
