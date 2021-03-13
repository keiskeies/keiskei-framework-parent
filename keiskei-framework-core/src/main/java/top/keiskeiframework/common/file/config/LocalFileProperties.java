package top.keiskeiframework.common.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 文件管理配置类
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/11/1 0:46
 */
@Data
@Component
@ConfigurationProperties(prefix = "keiskei-server-conf.local-file")
public class LocalFileProperties {
    /**
     * 文件上传路径
     */
    private String path = "/tmp/file/";

    /**
     * 文件临时路径
     */
    private String tempPath = "/tmp/file/temp/";

    /**
     * 返回文件名是否带路径
     */
    private String urlSuffix = "";
}
