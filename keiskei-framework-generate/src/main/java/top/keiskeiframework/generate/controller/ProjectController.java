package top.keiskeiframework.generate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.keiskeiframework.common.base.controller.impl.ListControllerImpl;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.generate.entity.ProjectInfo;
import top.keiskeiframework.generate.enums.BuildStatusEnum;
import top.keiskeiframework.generate.service.IGenerateService;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 项目
 * </p>
 * @since 2020/12/27 22:58
 */
@RestController
@RequestMapping("/generate/project")
@Api(tags = "文件生成 - 项目管理")
public class ProjectController extends ListControllerImpl<ProjectInfo> {

    @Autowired
    private IGenerateService generateService;

    @PostMapping("/{id}/build")
    @ApiOperation("创建代码")
    public R<Boolean> build(@PathVariable("id") Long id) {
        generateService.build(id);
        return R.ok(true, "项目构建中，请稍等。。。");
    }

    @GetMapping("/{id}/status")
    @ApiOperation("代码创建状态")
    public R<BuildStatusEnum> status(@PathVariable("id") Long id) {
        return R.ok(generateService.refreshStatus(id));
    }

    @GetMapping("/{id}/download")
    @ApiOperation("代码下载地址")
    public R<String> download(@PathVariable("id") Long id) {
        return R.ok(generateService.getDownloadAddress(id));
    }
}
