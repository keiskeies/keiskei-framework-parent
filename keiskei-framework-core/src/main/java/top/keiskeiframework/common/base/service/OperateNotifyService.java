package top.keiskeiframework.common.base.service;

import org.springframework.scheduling.annotation.Async;

/**
 * <p>
 * 操作通知
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/18 17:11
 */
public interface OperateNotifyService {
    /**
     * 保存操作通知
     *
     * @param obj          操作数据
     * @param serviceClass 操作service
     */
    @Async
    void update(Object obj, Object serviceClass);

    /**
     * 更新才做通知
     *
     * @param obj          操作数据
     * @param serviceClass 操作service
     */
    @Async
    void save(Object obj, Object serviceClass);

    /**
     * 删除操作通知
     *
     * @param obj          操作数据
     * @param serviceClass 操作service
     */
    @Async
    void delete(Object obj, Object serviceClass);
}
