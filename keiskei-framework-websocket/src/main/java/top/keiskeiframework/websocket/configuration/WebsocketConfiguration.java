package top.keiskeiframework.websocket.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * websocket
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/8/7 13:38
 */
@Configuration
public class WebsocketConfiguration {


    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
