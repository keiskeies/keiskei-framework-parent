package top.keiskeiframework.generate.service;

import top.keiskeiframework.generate.entity.ProjectInfo;
import top.keiskeiframework.generate.enums.BuildStatusEnum;

/**
 * <p>
 * 文件生成 service
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/23 15:44
 */
public interface IGenerateService {

    /**
     * 构建指定项目
     * @param itemId 项目id
     */
    void build(String itemId);

    /**
     * 刷新项目状态
     * @param itemId 项目id
     * @return .
     */
    BuildStatusEnum refreshStatus(String itemId);

    /**
     * 获取下载地址
     * @param itemId 项目id
     * @return .
     */
    String getDownloadAddress(String itemId);

}
