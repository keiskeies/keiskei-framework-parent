package top.keiskeiframework.cpreading.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.TreeControllerImpl;
import top.keiskeiframework.cpreading.entity.BookFeeling;

/**
 * <p>
 * 图书感悟 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@RestController
@RequestMapping("/api/v2/cpreading/bookFeeling")
@Api(tags = "碎片阅读 - 图书感悟")
public class ApiBookFeelingController extends TreeControllerImpl<BookFeeling, Long> {


}
