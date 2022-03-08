package top.keiskeiframework.cpreading.feeling.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.TreeControllerImpl;
import top.keiskeiframework.cpreading.feeling.entity.BookFeeling;
import top.keiskeiframework.cpreading.feeling.service.IBookFeelingService;

/**
 * <p>
 * 图书感悟 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@RestController
@RequestMapping("/cpreading/bookFeeling")
@Api(tags = "碎片阅读 - 图书感悟")
public class BookFeelingController extends TreeControllerImpl<BookFeeling, Long>{

    @Autowired
    private IBookFeelingService bookFeelingService;



}
