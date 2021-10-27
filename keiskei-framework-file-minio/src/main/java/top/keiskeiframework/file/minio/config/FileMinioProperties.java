package top.keiskeiframework.file.minio.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/10/22 14:27
 */
@Data
@Component
@ConfigurationProperties(prefix = "keiskei.file.minio")
public class FileMinioProperties {

    private String accessKey;
    private String secretKey;
    private String bucket;

    /**
     * 域名
     */
    private String endpoint;

    /**
     *  是否开启外网访问
     */
    private Boolean outsideNet = false;
    private String ulrSuffix;
    /**
     * 协议方式 http:// | https:// | //
     */
    private String protocol = "//";

}
