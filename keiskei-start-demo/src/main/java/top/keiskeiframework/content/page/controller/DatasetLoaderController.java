package top.keiskeiframework.content.page.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.ListController;
import top.keiskeiframework.content.page.entity.DatasetLoader;

/**
 * <p>
 * DatasetLoader
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/3 00:59
 */
@RestController
@RequestMapping("/admin/v1/content/page/dataset/loader")
@Api(tags = "内容管理-页面-数据集-Loader")
public class DatasetLoaderController extends ListController<DatasetLoader> {
}
