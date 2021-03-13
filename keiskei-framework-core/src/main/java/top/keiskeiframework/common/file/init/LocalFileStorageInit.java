package top.keiskeiframework.common.file.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.keiskeiframework.common.file.config.LocalFileProperties;
import top.keiskeiframework.common.file.util.MultiFileUtils;

/**
 * <p>
 * 初始化文件目录
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019/11/3 0:17
 */
@Component
public class LocalFileStorageInit implements CommandLineRunner {

    @Autowired
    private LocalFileProperties localFileProperties;

    @Override
    public void run(String... args) throws Exception {
        MultiFileUtils.checkDir(localFileProperties.getPath());
        MultiFileUtils.checkDir(localFileProperties.getTempPath());
    }
}
