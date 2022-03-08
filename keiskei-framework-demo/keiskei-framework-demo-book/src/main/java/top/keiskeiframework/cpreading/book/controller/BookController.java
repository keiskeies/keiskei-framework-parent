package top.keiskeiframework.cpreading.book.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.ListControllerImpl;
import top.keiskeiframework.cpreading.book.entity.Book;
import top.keiskeiframework.cpreading.book.service.IBookService;

/**
 * <p>
 * 图书管理 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@RestController
@RequestMapping("/cpreading/book")
@Api(tags = "碎片阅读 - 图书管理")
public class BookController extends ListControllerImpl<Book, Long>{

    @Autowired
    private IBookService bookService;



}
