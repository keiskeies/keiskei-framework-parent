package top.keiskeiframework.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.ListController;
import top.keiskeiframework.entity.Article;
import top.keiskeiframework.entity.TestInfo;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/3 00:59
 */
@RestController
@RequestMapping("/admin/v1/article/article")
@Api(tags = "科技资讯 - 科技资讯")
public class ArticleController extends ListController<Article> {
}
