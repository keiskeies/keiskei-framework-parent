package top.keiskeiframework.common.base.service;

import top.keiskeiframework.common.dto.log.OperateLogDTO;

/**
 * <p>
 * 日志操作
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/19 16:21
 */
public interface OperateLogService {
    /**
     * 保存日志
     * @param log 日志
     */
    void saveLog(OperateLogDTO log);
}
