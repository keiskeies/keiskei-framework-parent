package top.keiskeiframework.websocket.constant;

/**
 * websocket 常数
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/8/7 17:42
 */
public class WebsocketConstants {

    /**
     * 地址
     */
    public final static String URL = "/{api:api|admin}/webSocket";
    /**
     * 允许通过的来源
     */
    public final static String ALLOWED_ORIGINS = "*";

    /**
     * websocket 请求头
     */
    public final static String WEBSOCKET_HEADER = "Sec-WebSocket-Protocol";
}
