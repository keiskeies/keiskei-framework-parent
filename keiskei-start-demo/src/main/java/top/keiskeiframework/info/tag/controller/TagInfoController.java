package top.keiskeiframework.info.tag.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.TreeController;
import top.keiskeiframework.info.tag.entity.TagInfo;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/13 22:15
 */
@RestController
@RequestMapping("/admin/v1/content/tag/tagInfo")
@Api(tags = "内容管理 - 标签管理 - 标签")
public class TagInfoController extends TreeController<TagInfo> {
}
