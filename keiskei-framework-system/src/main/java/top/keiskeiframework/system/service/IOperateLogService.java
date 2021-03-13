package top.keiskeiframework.system.service;

import top.keiskeiframework.common.base.service.BaseService;
import top.keiskeiframework.system.entity.OperateLog;

/**
 * <p>
 * 操作日志 业务接口层
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020-12-16 13:36:30
 */
public interface IOperateLogService extends BaseService<OperateLog> {

    /**
     * 异步保存
     * @param operateLog .
     */
    void saveAsync(OperateLog operateLog);

}
