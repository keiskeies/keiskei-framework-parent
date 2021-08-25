package top.keiskeiframework.websocket.configurator;

import top.keiskeiframework.websocket.constant.WebsocketConstants;
import org.springframework.util.CollectionUtils;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;
import java.util.List;

/**
 * 往端点参数中赋值
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/8/10 20:35
 */
public class GetHttpSessionConfigurator extends Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {

        List<String> wsHeaderValues = request.getHeaders().get(WebsocketConstants.WEBSOCKET_HEADER);
        if (!CollectionUtils.isEmpty(wsHeaderValues)) {
            config.getUserProperties().put(WebsocketConstants.WEBSOCKET_HEADER, wsHeaderValues.get(0));
        }
    }

}