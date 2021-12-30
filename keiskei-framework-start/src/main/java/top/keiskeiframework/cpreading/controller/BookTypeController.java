package top.keiskeiframework.cpreading.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.*;

import top.keiskeiframework.cpreading.entity.BookType;
import top.keiskeiframework.cpreading.service.IBookTypeService;

/**
 * <p>
 * 图书类型 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@RestController
@RequestMapping("/admin/v2/cpreading/bookType")
@Api(tags = "碎片阅读 - 图书类型")
public class BookTypeController extends TreeController<BookType, Long>{

    @Autowired
    private IBookTypeService bookTypeService;



}
