package top.keiskeiframework.websocket.service;

import top.keiskeiframework.websocket.dto.User;

import javax.websocket.Session;

/**
 * websocket token 处理
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/8/7 14:51
 */
public interface WebsocketService {

    /**
     * 验证用户信息
     *
     * @param token 用户token
     * @return .
     */
    User validate(Object token);

    /**
     * 接收用户消息
     *
     * @param message 消息
     * @param id     发送者id
     * @param session 发送者session
     */
    void onMessage(String message, Integer id, Session session);
}
