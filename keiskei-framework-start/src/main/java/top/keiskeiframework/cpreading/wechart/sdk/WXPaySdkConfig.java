package top.keiskeiframework.cpreading.wechart.sdk;

import top.keiskeiframework.cpreading.wechart.config.MiniAppProperties;
import top.keiskeiframework.cpreading.wechart.config.WechatPayProperties;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 *
 * </p>
 * @since 2020/11/2 11:21
 */
public class WXPaySdkConfig extends WXPayConfig {

    public WXPaySdkConfig(WechatPayProperties payConfig, String appName) {

        if (!StringUtils.isEmpty(appName)) {
            MiniAppProperties miniAppProperties = payConfig.getMiniApps().get(appName);
            if (null == miniAppProperties) {
                throw new RuntimeException("小程序配置错误");
            }
            this.appID = miniAppProperties.getAppId();
        } else {
            this.appID = payConfig.getAppId();
        }


        this.domain = payConfig.getDomain();
        this.certPath = payConfig.getCertPath();
        this.key = payConfig.getKey();
        this.mchID = payConfig.getMchId();
    }


    @Getter
    private final String appID;
    @Getter
    private final String mchID;
    @Getter
    private final String key;
    private final String certPath;
    private final String domain;


    @Override
    public InputStream getCertStream() {
        try {
            return new FileInputStream(new File(certPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {

            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo(domain, true);
            }
        };
    }



}
