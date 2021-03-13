package top.keiskeiframework.generate.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.annotation.validate.Lockable;
import top.keiskeiframework.common.enums.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.file.config.LocalFileProperties;
import top.keiskeiframework.common.util.SecurityUtils;
import top.keiskeiframework.common.vo.TokenUser;
import top.keiskeiframework.generate.config.GenerateProperties;
import top.keiskeiframework.generate.entity.*;
import top.keiskeiframework.generate.enums.BuildStatusEnum;
import top.keiskeiframework.generate.service.*;
import top.keiskeiframework.generate.util.GenerateFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/23 15:53
 */
@Service
public class GenerateServiceImpl implements GenerateService {

    @Autowired
    private IItemInfoService itemInfoService;
    @Autowired
    private IModuleInfoService moduleInfoService;
    @Autowired
    private ITableInfoService tableInfoService;
    @Autowired
    private IFieldInfoService fieldInfoService;
    @Autowired
    private IFieldEnumInfoService fieldEnumInfoService;
    @Autowired
    @Lazy
    private GenerateService generateService;
    @Autowired
    private LocalFileProperties localFileProperties;
    @Autowired
    private GenerateProperties generateProperties;


    @Override
    @Async
    @Lockable(key = "targetClass.name + '-' + #itemInfo.id")
    public void build(Long itemId) {

        if (!BuildStatusEnum.NONE.equals(generateService.refreshStatus(itemId))) {
            throw new BizException(BizExceptionEnum.ERROR);
        }

        ProjectInfo projectInfo = itemInfoService.getById(itemId);

        ModuleInfo moduleQuery = ModuleInfo.builder().itemId(itemId).build();
        List<ModuleInfo> modules = moduleInfoService.options(moduleQuery);
        projectInfo.setModules(modules);

        for (ModuleInfo moduleInfo : modules) {
            List<TableInfo> tables = tableInfoService.options(TableInfo.builder().moduleId(moduleInfo.getId()).build());
            moduleInfo.setTables(tables);
            for (TableInfo tableInfo : tables) {
                List<FieldInfo> fields = fieldInfoService.options(FieldInfo.builder().tableId(tableInfo.getId()).build());
                tableInfo.setFields(fields);
                for (FieldInfo fieldInfo : fields) {
                    List<FieldEnumInfo> fieldEnums = fieldEnumInfoService.options(FieldEnumInfo.builder().fieldId(fieldInfo.getId()).build());
                    fieldInfo.setFieldEnums(fieldEnums);
                }
            }
        }

        this.build(projectInfo);

    }

    @Override
    @Async
    @Lockable(key = "targetClass.name + '-' + #itemInfo.id")
    public void build(ProjectInfo projectInfo) {
        TokenUser tokenUser = SecurityUtils.getSessionUser();

        String basePath = localFileProperties.getPath() + "item/" + tokenUser.getId() + "/" + projectInfo.getId() + "/" + projectInfo.getName();

        if (StringUtils.isEmpty(projectInfo.getAuthor())) {
            projectInfo.setAuthor(tokenUser.getName() + " " + tokenUser.getEmail());
        }

        // 创建项目目录
        File dir = new File(basePath);
        dir.delete();
        if (!dir.mkdirs()) {
            throw new BizException(BizExceptionEnum.ERROR);
        }
        // 拷贝前端基础文件
        try {
            GenerateFileUtils.copyDir(generateProperties.getBaseAdminPath(), basePath + "/admin/");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 生成文件
        GenerateFileUtils.go2Fly(projectInfo, basePath + "/server/");
        // 添加Dockerfile

        // 添加build.sh

        // 文件打包


    }

    @Override
    public void build(String json) {
        ProjectInfo projectInfo = JSON.parseObject(json, ProjectInfo.class);

        itemInfoService.save(projectInfo);

        generateService.build(projectInfo);
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
