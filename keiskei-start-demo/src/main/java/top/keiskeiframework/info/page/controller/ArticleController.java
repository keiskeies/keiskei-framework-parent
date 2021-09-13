package top.keiskeiframework.info.page.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.ListController;
import top.keiskeiframework.info.page.entity.Article;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/3 00:59
 */
@RestController
@RequestMapping("/admin/v1/content/page/article")
@Api(tags = "内容管理 - 页面内容管理 - 科技资讯")
public class ArticleController extends ListController<Article> {
}
