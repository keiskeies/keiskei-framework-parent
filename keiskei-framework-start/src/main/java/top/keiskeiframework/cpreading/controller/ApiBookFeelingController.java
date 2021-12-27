package top.keiskeiframework.cpreading.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.keiskeiframework.common.base.controller.ListController;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.cpreading.entity.BookFeeling;
import top.keiskeiframework.cpreading.service.IBookFeelingService;

/**
 * <p>
 * 图书感悟 controller层
 * </p>
 *
 * @author right_way@foxmail.com
 * @since 2021-11-28 22:37:20
 */
@RestController
@RequestMapping("/{api:api|admin}/v2/cpreading/bookFeeling")
@Api(tags = "碎片阅读 - 图书感悟")
public class ApiBookFeelingController {

    @Autowired
    private IBookFeelingService bookFeelingService;


}
