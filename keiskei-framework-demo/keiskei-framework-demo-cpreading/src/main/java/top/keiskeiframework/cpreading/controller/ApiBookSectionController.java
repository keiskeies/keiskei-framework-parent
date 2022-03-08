package top.keiskeiframework.cpreading.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.ListControllerImpl;
import top.keiskeiframework.cpreading.entity.BookSection;
import top.keiskeiframework.cpreading.service.IBookSectionService;

/**
 * <p>
 * 图书章节 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@RestController
@RequestMapping("/api/v2/cpreading/bookSection")
@Api(tags = "碎片阅读 - 图书章节")
public class ApiBookSectionController extends ListControllerImpl<BookSection, Long> {

    @Autowired
    private IBookSectionService bookSectionService;



}
