package top.keiskeiframework.notify.service;

import top.keiskeiframework.notify.dto.NotifyMessageDTO;

/**
 * <p>
 * 通知
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/13 23:13
 */
public interface NotifyStorageService {
    /**
     * 消息推送
     * @param notifyMessage 消息
     */
    void send(NotifyMessageDTO notifyMessage);
}
