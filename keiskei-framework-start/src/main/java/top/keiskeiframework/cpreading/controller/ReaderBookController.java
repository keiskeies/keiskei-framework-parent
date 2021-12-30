package top.keiskeiframework.cpreading.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.*;

import top.keiskeiframework.cpreading.entity.ReaderBook;
import top.keiskeiframework.cpreading.service.IReaderBookService;

/**
 * <p>
 * 读者书库 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@RestController
@RequestMapping("/admin/v2/cpreading/readerBook")
@Api(tags = "碎片阅读 - 读者书库")
public class ReaderBookController extends ListController<ReaderBook, Long>{

    @Autowired
    private IReaderBookService readerBookService;



}
