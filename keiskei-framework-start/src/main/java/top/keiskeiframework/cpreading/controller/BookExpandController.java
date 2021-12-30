package top.keiskeiframework.cpreading.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.*;

import top.keiskeiframework.cpreading.entity.BookExpand;
import top.keiskeiframework.cpreading.service.IBookExpandService;

/**
 * <p>
 * 图书拓展 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@RestController
@RequestMapping("/admin/v2/cpreading/bookExpand")
@Api(tags = "碎片阅读 - 图书拓展")
public class BookExpandController extends ListController<BookExpand, Long>{

    @Autowired
    private IBookExpandService bookExpandService;



}
