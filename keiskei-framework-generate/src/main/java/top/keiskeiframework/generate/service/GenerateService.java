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
public interface GenerateService {

    /**
     * 构建指定项目
     * @param itemId 项目id
     */
    void build(Long itemId);

    /**
     * 构建指定项目
     * @param projectInfo 项目信息
     */
    void build(ProjectInfo projectInfo);

    /**
     * 构建指定项目
     * @param json 项目json
     */
    void build(String json);

    /**
     * 刷新项目状态
     * @param itemId 项目id
     * @return .
     */
    BuildStatusEnum refreshStatus(Long itemId);

    /**
     * 获取下载地址
     * @param itemId 项目id
     * @return .
     */
    String getDownloadAddress(Long itemId);

}