package top.keiskeiframework.log.service;

import top.keiskeiframework.log.dto.OperateLogDTO;

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
