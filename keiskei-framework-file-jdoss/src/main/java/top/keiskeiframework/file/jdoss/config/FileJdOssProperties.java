package top.keiskeiframework.file.jdoss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里oss配置信息
 *
 * @author 陈加敏
 * @since 2019/7/23 15:30
 */
@Data
@Component("aliOssProperties")
@ConfigurationProperties(prefix = "keiskei.file.jd-oss")
public class FileJdOssProperties {
    /**
     * 桶名
     */
    private String bucket;
    /**
     * 域名
     */
    private String endpoint;
    /**
     * 内网域名
     */
    private String internalEndpoint;

    /**
     * 地区
     */
    private String signingRegion;
    /**
     * AccessKeyId
     */
    private String accessKeyId;
    /**
     * AccessKeySecret
     */
    private String accessKeySecret;
    /**
     *  是否开启外网访问
     */
    private Boolean outsideNet = true;
    /**
     * 协议方式 http:// | https:// | //
     */
    private String protocol = "//";

}
