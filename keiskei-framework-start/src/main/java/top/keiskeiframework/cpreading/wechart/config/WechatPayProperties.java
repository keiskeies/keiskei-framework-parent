package top.keiskeiframework.cpreading.wechart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * 微信公众号参数
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/12/20 14:07
 */
@Data
@Component
@ConfigurationProperties(prefix = "keiskei.wechat")
public class WechatPayProperties {

    private String appId;
    private String secret;

    private Map<String,MiniAppProperties> miniApps;
    /**
     * 商户号
     */
    private String mchId;
    private String key;
    private String domain = "api.mch.weixin.qq.com";
    private String payName;

    /**
     * 证书位置
     */
    private String certPath = "classpath*:/apiclient_cert.p12";
    /**
     * 支付回调地址
     */
    private String notifyUrl;
    /**
     * 退款回调地址
     */
    private String refundNotifyUrl;


}
