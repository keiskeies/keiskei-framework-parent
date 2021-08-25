package top.keiskeiframework.websocket.server;

import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.SpringUtils;
import top.keiskeiframework.websocket.configurator.GetHttpSessionConfigurator;
import top.keiskeiframework.websocket.constant.WebsocketConstants;
import top.keiskeiframework.websocket.dto.User;
import top.keiskeiframework.websocket.service.WebsocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/8/7 14:46
 */
@Slf4j
@ServerEndpoint(value = "/admin/v1/webSocket", configurator = GetHttpSessionConfigurator.class)
@Component
public class WebSocketServer {


    private final WebsocketService websocketService = SpringUtils.getBean(WebsocketService.class);
    private static final CopyOnWriteArraySet<WebSocketServer> WEB_SOCKET_SET = new CopyOnWriteArraySet<>();
    private Integer id;
    private String name;
    private Session session;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        Object token = config.getUserProperties().get(WebsocketConstants.WEBSOCKET_HEADER);
        User user = websocketService.validate(token);
        if (null == user) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new BizException(BizExceptionEnum.AUTH_FORBIDDEN);
        }
        this.id = user.getId();
        this.name = user.getName();
        this.session = session;

        WEB_SOCKET_SET.add(this);
        log.info("{} - {} 上线", id, name);
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("webSocket IO异常");
        }
    }

    @OnClose
    public void onClose() {
        WEB_SOCKET_SET.remove(this);
        log.info("{} - {} 下线", id, name);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        websocketService.onMessage(message, id, session);

    }

    @OnError
    public void onError(Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static void sendInfo(String message, Integer id) {
        for (WebSocketServer item : WEB_SOCKET_SET) {
            if (item.id.equals(id)) {
                log.info("推送消息给: {} - {} , 推送内容: {}", item.id, item.name, message);
                try {
                    item.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendInfo(String message) {
        log.info("群发消息: {}",message);
        for (WebSocketServer item : WEB_SOCKET_SET) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebSocketServer that = (WebSocketServer) o;
        return Objects.equals(websocketService, that.websocketService) &&
                id.equals(that.id) &&
                Objects.equals(name, that.name) &&
                session.equals(that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(websocketService, id, name, session);
    }
}
















