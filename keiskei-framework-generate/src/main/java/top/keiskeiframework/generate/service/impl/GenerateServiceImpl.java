package top.keiskeiframework.generate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.annotation.Lockable;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.SecurityUtils;
import top.keiskeiframework.common.vo.user.TokenUser;
import top.keiskeiframework.generate.config.GenerateProperties;
import top.keiskeiframework.generate.entity.ProjectInfo;
import top.keiskeiframework.generate.enums.BuildStatusEnum;
import top.keiskeiframework.generate.service.GenerateService;
import top.keiskeiframework.generate.service.IProjectInfoService;
import top.keiskeiframework.generate.util.GenerateFileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/23 15:53
 */
@Service
public class GenerateServiceImpl implements GenerateService {

    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    @Lazy
    private GenerateService generateService;
    @Autowired
    private GenerateProperties generateProperties;


    @Override
    @Async
    @Lockable(key = "#itemId", message="代码构建中，请稍候~")
    public void build(Long itemId) {

        if (!BuildStatusEnum.NONE.equals(generateService.refreshStatus(itemId))) {
            throw new BizException(BizExceptionEnum.ERROR);
        }

        ProjectInfo project = projectInfoService.getById(itemId);

        buildProject(project);

    }

    private void buildProject(ProjectInfo project) {
        TokenUser tokenUser = SecurityUtils.getSessionUser();

        String basePath = generateProperties.getBasePath() + tokenUser.getId() + "/" + project.getId() + "/" + project.getName();

        if (StringUtils.isEmpty(project.getAuthor())) {
            project.setAuthor(tokenUser.getName() + " " + tokenUser.getEmail());
        }

        // 创建项目目录
        File dir = new File(basePath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new BizException(BizExceptionEnum.ERROR);
        }
        // 拷贝前端基础文件
        try {
            GenerateFileUtils.copyDir(generateProperties.getBaseAdminPath(), basePath + "/admin/");

        } catch (IOException e) {
            e.printStackTrace();
        }
        // 生成文件
        GenerateFileUtils.go2Fly(project, basePath);
        // 文件打包


    }

    @Override
    public BuildStatusEnum refreshStatus(Long itemId) {
        return BuildStatusEnum.NONE;
    }

    @Override
    public String getDownloadAddress(Long itemId) {
        return null;
    }
}
