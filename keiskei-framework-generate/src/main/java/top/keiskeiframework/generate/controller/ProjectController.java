package top.keiskeiframework.generate.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.ListController;
import top.keiskeiframework.generate.entity.ProjectInfo;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 项目
 * </p>
 * @since 2020/12/27 22:58
 */
@RestController
@RequestMapping("/admin/v1/generate/project")
@Api(tags = "文件生成 - 项目管理")
public class ProjectController extends ListController<ProjectInfo> {
}
