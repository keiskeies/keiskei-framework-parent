package top.keiskeiframework.file.alioss.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * <p>
 *
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2019/10/30 15:01
 */
@Configuration
public class AliOssClintConfiguration {

    @Bean("longOssClient")
    public OSS ossClient(FileAliOssProperties fileAliOssProperties) {
        // 优先内网传输
        String endpoint = fileAliOssProperties.getInternalEndpoint();
        if (StringUtils.isEmpty(endpoint)) {
            endpoint = fileAliOssProperties.getEndpoint();
        }
        return new OSSClientBuilder().build(endpoint, fileAliOssProperties.getAccessKeyId(), fileAliOssProperties.getAccessKeySecret());
    }
}
